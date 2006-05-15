package it.eng.spagobi.importexport.version18;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.importexport.IImportManager;
import it.eng.spagobi.importexport.JdbcConnection;
import it.eng.spagobi.importexport.JndiConnection;
import it.eng.spagobi.importexport.MetadataAssociations;
import it.eng.spagobi.metadata.HibernateUtil;
import it.eng.spagobi.metadata.SbiChecks;
import it.eng.spagobi.metadata.SbiDomains;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.metadata.SbiFuncRole;
import it.eng.spagobi.metadata.SbiFuncRoleId;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.metadata.SbiLov;
import it.eng.spagobi.metadata.SbiObjPar;
import it.eng.spagobi.metadata.SbiObjParId;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.metadata.SbiParameters;
import it.eng.spagobi.metadata.SbiParuse;
import it.eng.spagobi.metadata.SbiParuseCk;
import it.eng.spagobi.metadata.SbiParuseCkId;
import it.eng.spagobi.metadata.SbiParuseDet;
import it.eng.spagobi.metadata.SbiParuseDetId;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ImportManager implements IImportManager {

	private String pathImportTmpFolder = "";
    private String pathBaseFolder = "";
    private String pathDBFolder = "";  
    private String pathContentFolder = "";
	private ImporterMetadata importer = null;
	private Properties props = null;
	private SessionFactory sessionFactoryExpDB = null;
	private Session sessionExpDB = null;
	private Transaction txExpDB = null;
	private Session sessionCurrDB = null;
	private Transaction txCurrDB = null;
	private MetadataAssociations metaAss = null;
	
	
	public void prepareImport(String pathImpTmpFold, String archiveName, byte[] archiveContent) throws EMFUserError {
		// create directories of the tmp import folder
		File impTmpFold = new File(pathImpTmpFold);
		impTmpFold.mkdirs();
		// write content uploaded into a tmp archive
		String pathArchiveFile = pathImpTmpFold + "/" +archiveName;
		File archive = new File(pathArchiveFile);
		try{
			FileOutputStream fos = new FileOutputStream(archive); 
			fos.write(archiveContent);
			fos.flush();
			fos.close();
		} catch (IOException ioe) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "prepareImport",
		               			   "Error while writing archive content into a tmp file " + ioe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		}
		// decompress archive
		ImportUtilities.decompressArchive(pathImpTmpFold, pathArchiveFile);
		// erase archive file 
		archive.delete();
		int lastDotPos = archiveName.lastIndexOf(".");
		if(lastDotPos!=-1)
			archiveName = archiveName.substring(0, lastDotPos);
		pathImportTmpFolder = pathImpTmpFold;
		pathBaseFolder = pathImportTmpFolder + "/" + archiveName;
	    pathDBFolder =  pathBaseFolder + "/metadata";
	    pathContentFolder = pathBaseFolder + "/contents";
	    String propFilePath = pathBaseFolder + "/export.properties";
		try{
		    FileInputStream fis = new FileInputStream(propFilePath);
			props = new Properties();
			props.load(fis);
			fis.close();
		} catch (Exception e){
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "prepareImport",
		   			               "Error while reading properties file " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		}
		importer = new ImporterMetadata();
		sessionFactoryExpDB = ImportUtilities.getHibSessionExportDB(pathDBFolder);
		sessionExpDB = sessionFactoryExpDB.openSession();
		txExpDB = sessionExpDB.beginTransaction();
		sessionCurrDB = HibernateUtil.currentSession();
		txCurrDB = sessionCurrDB.beginTransaction();
		metaAss = new MetadataAssociations();
	}
	
	
	
	
	public String getExportVersion() {
		return props.getProperty("spagobi-version");
	}
	
	
	
	public String getCurrentVersion() {
		ConfigSingleton conf = ConfigSingleton.getInstance();
		SourceBean curVerSB = (SourceBean)conf.getAttribute("IMPORTEXPORT.CURRENTVERSION");
		String curVer = (String)curVerSB.getAttribute("version");
		return curVer;
	}
	
	
	
	private String getExportedCmsBaseFolder() {
		return props.getProperty("cms-basefolder");
	}
	
	
	
	private String getCurrentCmsBaseFolder()throws EMFUserError {
		String pathSysFunct = "";
		try{
			ConfigSingleton conf = ConfigSingleton.getInstance();
			SourceBean pathSysFunctSB = (SourceBean)conf.getAttribute("SPAGOBI.CMS_PATHS.SYSTEM_FUNCTIONALITIES_PATH");
		    pathSysFunct = pathSysFunctSB.getCharacters();
		} catch (Exception e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getCurrentCmsBaseFolder",
                    			  "Error while retriving current cms base folder " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		}
		return pathSysFunct;
	}
	
	
	
	public List getExportedRoles() throws EMFUserError {
		List exportedRoles = null;
		exportedRoles = importer.getAllExportedRoles(txExpDB, sessionExpDB);
		return exportedRoles;
	}

	
	public List getExportedEngines() throws EMFUserError {
		List exportedEngines = null;
		exportedEngines = importer.getAllExportedEngines(txExpDB, sessionExpDB);
		return exportedEngines;
	}

	
	
	public void checkRoleReferences(Map roleAssociations) throws EMFUserError {
		// each exported role should be associate only to one system role
		Set rolesAssKeys = roleAssociations.keySet();
		Iterator iterRoleAssKeys1 = rolesAssKeys.iterator();
		while(iterRoleAssKeys1.hasNext()) {
			Integer roleExpId = (Integer)iterRoleAssKeys1.next();
			Integer roleAssId = (Integer)roleAssociations.get(roleExpId);
			Iterator iterRoleAssKeys2 = rolesAssKeys.iterator();
			while(iterRoleAssKeys2.hasNext()) {
				Integer otherRoleExpId = (Integer)iterRoleAssKeys2.next();
				if(otherRoleExpId.compareTo(roleExpId)!=0) {
					Integer otherRoleAssId = (Integer)roleAssociations.get(otherRoleExpId);
					if(otherRoleAssId.compareTo(roleAssId)==0){
						throw new EMFUserError(EMFErrorSeverity.ERROR, 8001);
					}
				}
			}
		}	
	}
	
	
	public void updateConnectionReferences(Map connAssociations) throws EMFUserError {
		/* 
		 * The key of the map are the name of the exported connections
		 * Each key value is the name of the current system connection associate
		 */
		importer.updateConnRefs(connAssociations, txExpDB, sessionExpDB);
	}
	
	
	private void closeSessionFactory() {
		if(sessionFactoryExpDB!=null){
			sessionFactoryExpDB.close();
		}
	}
	
	
	private void closeSession() {
		if(sessionExpDB!=null){
			if(sessionExpDB.isOpen()) {
				sessionExpDB.close();
			}
		}
		if(sessionCurrDB!=null){
			if(sessionCurrDB.isOpen()) {
				sessionCurrDB.close();
			}
		}		
	}

	
	
	
	private void rollback() {
		if(txExpDB!=null)
			txExpDB.rollback();
		if(txCurrDB!=null)
			txCurrDB.rollback();
		closeSession();
		closeSessionFactory();
	}
	
	
	
	public void commitAllChanges() throws EMFUserError {
		txExpDB.commit();
		txCurrDB.commit();
		closeSession();
		closeSessionFactory();
		metaAss.clear();
		ImpExpGeneralUtilities.deleteDir(new File(pathBaseFolder));
	}
	
		
	
	public void importObjects() throws EMFUserError {
		checkRoleReferences(metaAss.getRoleIDAssociation());
		updateConnectionReferences(metaAss.getConnectionAssociation());
		importRoles();
		importEngines();
		importFunctionalities();
		importChecks();
		importParameters();
		importLovs();
		importParuse();
		importBIObjects();
		importFunctRoles();
		importParuseDet();
		importParuseCheck();
		importBIObjPar();
	}
	

	private void importRoles() throws EMFUserError {
		try{
			List exportedRoles = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiExtRoles");
			Iterator iterSbiRoles = exportedRoles.iterator();
			while(iterSbiRoles.hasNext()){
				SbiExtRoles role = (SbiExtRoles)iterSbiRoles.next();
				Integer oldId = role.getExtRoleId();
				Map roleIdAss = metaAss.getRoleIDAssociation();
			    Set roleIdAssSet = roleIdAss.keySet();
				if(roleIdAssSet.contains(oldId)){
					continue;
				}
				SbiExtRoles newRole = ImportUtilities.makeNewSbiExtRole(role);
				String roleCd = role.getRoleTypeCode();
				Map unique = new HashMap();
				unique.put("valuecd", roleCd);
				unique.put("domaincd", "ROLE_TYPE");
				SbiDomains existDom = (SbiDomains)importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
				if(existDom!=null) {
					newRole.setRoleType(existDom);
					newRole.setRoleTypeCode(existDom.getValueCd());
				}
				importer.insertObject(newRole, sessionCurrDB);
			    Integer newId = newRole.getExtRoleId();
			    metaAss.insertCoupleRole(oldId, newId);
			}
		} finally {}
	}
	
	
	
	private void importEngines() throws EMFUserError  {
		try{
			List exportedEngines = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiEngines");
			Iterator iterSbiEngines = exportedEngines.iterator();
			Map engineAss = new HashMap();
			while(iterSbiEngines.hasNext()){
				SbiEngines engine = (SbiEngines)iterSbiEngines.next();
				Integer oldId = engine.getEngineId();
				Map engIdAss = metaAss.getEngineIDAssociation();
			    Set engIdAssSet = engIdAss.keySet();
				if(engIdAssSet.contains(oldId)){
					continue;
				}
				SbiEngines newEng = ImportUtilities.makeNewSbiEngine(engine);
			    importer.insertObject(newEng, sessionCurrDB);
			    Integer newId = newEng.getEngineId();
			    metaAss.insertCoupleEngine(oldId, newId);
			}
		} finally {}
	}
	
	
	
	private void importFunctionalities() throws EMFUserError {
		try{
			List exportedFuncts = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiFunctions");
			Iterator iterSbiFuncts = exportedFuncts.iterator();
			Map functAss = new HashMap();
			while(iterSbiFuncts.hasNext()){
				SbiFunctions funct = (SbiFunctions)iterSbiFuncts.next();
				String expPath = funct.getPath();
				String expCmsBaseFoldPath = getExportedCmsBaseFolder();
				String relativeExpPath = expPath.substring(expCmsBaseFoldPath.length());
				String currCmsBaseFoldPath = getCurrentCmsBaseFolder();
				String curPath = currCmsBaseFoldPath + relativeExpPath;
				importer.insertCmsFunctionality(currCmsBaseFoldPath, relativeExpPath);
				funct.setPath(curPath);
				funct.setSbiFuncRoles(new HashSet());
				funct.setSbiObjFuncs(new HashSet());
				Integer oldId = funct.getFunctId();
				Map functIdAss = metaAss.getFunctIDAssociation();
			    Set functIdAssSet = functIdAss.keySet();
				if(functIdAssSet.contains(oldId)){
					continue;
				}
				SbiFunctions newFunct = ImportUtilities.makeNewSbiFunction(funct);
				String functCd = funct.getFunctTypeCd();
				Map unique = new HashMap();
				unique.put("valuecd", functCd);
				unique.put("domaincd", "FUNCT_TYPE");
				SbiDomains existDom = (SbiDomains)importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
				if(existDom!=null) {
					newFunct.setFunctType(existDom);
					newFunct.setFunctTypeCd(existDom.getValueCd());
				}
			    importer.insertObject(newFunct, sessionCurrDB);
			    Integer newId = newFunct.getFunctId(); 
			    metaAss.insertCoupleFunct(oldId, newId);
			}
		} finally {}
	}
	
	
	
	private void importLovs() throws EMFUserError {
		try{
			List exportedLovs = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiLov");
			Iterator iterSbiLovs = exportedLovs.iterator();
			Map lovAss = new HashMap();
			while(iterSbiLovs.hasNext()){
				SbiLov lov = (SbiLov)iterSbiLovs.next();
				Integer oldId = lov.getLovId();
				Map lovIdAss = metaAss.getLovIDAssociation();
				Set lovIdAssSet = lovIdAss.keySet();
				if(lovIdAssSet.contains(oldId)){
					continue;
				}
				SbiLov newlov = ImportUtilities.makeNewSbiLov(lov);
				String inpTypeCd = lov.getInputTypeCd();
				Map unique = new HashMap();
				unique.put("valuecd", inpTypeCd);
				unique.put("domaincd", "INPUT_TYPE");
				SbiDomains existDom = (SbiDomains)importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
				if(existDom!=null) {
					newlov.setInputType(existDom);
					newlov.setInputTypeCd(existDom.getValueCd());
				}
			    importer.insertObject(newlov, sessionCurrDB);
			    Integer newId = newlov.getLovId(); 
			    metaAss.insertCoupleLov(oldId, newId);
			}
		} finally {}
	}
	
	
	
	private void importChecks() throws EMFUserError {
		try{
			List exportedChecks = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiChecks");
			Iterator iterSbiChecks = exportedChecks.iterator();
			Map checkAss = new HashMap();
			while(iterSbiChecks.hasNext()){
				SbiChecks check = (SbiChecks)iterSbiChecks.next();
				Integer oldId = check.getCheckId();
				Map checkIdAss = metaAss.getCheckIDAssociation();
			    Set checkIdAssSet = checkIdAss.keySet();
				if(checkIdAssSet.contains(oldId)){
					continue;
				}
				SbiChecks newck = ImportUtilities.makeNewSbiCheck(check);
				String valueCd = check.getValueTypeCd();
				Map unique = new HashMap();
				unique.put("valuecd", valueCd);
				unique.put("domaincd", "CHECK");
				SbiDomains existDom = (SbiDomains)importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
				if(existDom!=null) {
					newck.setCheckType(existDom);
					newck.setValueTypeCd(existDom.getValueCd());
				}
			    importer.insertObject(newck, sessionCurrDB);
			    Integer newId = newck.getCheckId();
			    metaAss.insertCoupleCheck(oldId, newId);
			}
		} finally {}
	}
	
	
	
	private void importParameters() throws EMFUserError {
		try{
			List exportedParams = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParameters");
			Iterator iterSbiParams = exportedParams.iterator();
			Map paramAss = new HashMap();
			while(iterSbiParams.hasNext()){
				SbiParameters param = (SbiParameters)iterSbiParams.next();
				Integer oldId = param.getParId();
				Map paramIdAss = metaAss.getParameterIDAssociation();
			    Set paramIdAssSet = paramIdAss.keySet();
				if(paramIdAssSet.contains(oldId)){
					continue;
				}
				SbiParameters newPar = ImportUtilities.makeNewSbiParameter(param);
				String typeCd = param.getParameterTypeCode();
				Map unique = new HashMap();
				unique.put("valuecd", typeCd);
				unique.put("domaincd", "PAR_TYPE");
				SbiDomains existDom = (SbiDomains)importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
				if(existDom!=null) {
					newPar.setParameterType(existDom);
					newPar.setParameterTypeCode(existDom.getValueCd());
				}
			    importer.insertObject(newPar, sessionCurrDB);
			    Integer newId = newPar.getParId();
			    metaAss.insertCoupleParameter(oldId, newId);
			}
		} finally {}
	}
	
	
	
	private void importBIObjects() throws EMFUserError {
		try{
			List exportedBIObjs = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiObjects");
			Iterator iterSbiObjs = exportedBIObjs.iterator();
			Map objAss = new HashMap();
			while(iterSbiObjs.hasNext()){
				SbiObjects obj = (SbiObjects)iterSbiObjs.next();
				SbiEngines engine = obj.getSbiEngines();
				Integer oldEngId = engine.getEngineId();
				Map assEngs = metaAss.getEngineIDAssociation();
                Integer newEngId = (Integer)assEngs.get(oldEngId);
				if(newEngId!=null) {
					SbiEngines newEng = ImportUtilities.makeNewSbiEngine(engine, newEngId);
					obj.setSbiEngines(newEng);
				}
				Integer oldId = obj.getBiobjId();
				Map objIdAss = metaAss.getBIobjIDAssociation();
			    Set objIdAssSet = objIdAss.keySet();
				if(objIdAssSet.contains(oldId)){
					continue;
				}
				SbiObjects newObj = ImportUtilities.makeNewSbiObject(obj);
				String typeCd = obj.getObjectTypeCode();
				Map unique = new HashMap();
				unique.put("valuecd", typeCd);
				unique.put("domaincd", "BIOBJ_TYPE");
				SbiDomains existDom = (SbiDomains)importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
				if(existDom!=null) {
					newObj.setObjectType(existDom);
					newObj.setObjectTypeCode(existDom.getValueCd());
				}
				String stateCd = obj.getStateCode();
				unique = new HashMap();
				unique.put("valuecd", stateCd);
				unique.put("domaincd", "STATE");
				SbiDomains existDomSt = (SbiDomains)importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
				if(existDomSt!=null) {
					newObj.setState(existDomSt);
					newObj.setStateCode(existDomSt.getValueCd());
				}
				obj = importer.insertBIObject(newObj, pathContentFolder, sessionCurrDB);
			    Integer newId = newObj.getBiobjId(); 
			    metaAss.insertCoupleBIObj(oldId, newId);
			}
		} finally {}
	}
	
	
	
	private void importParuse() throws EMFUserError {
		try{
			List exportedParuses = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParuse");
			Iterator iterSbiParuses = exportedParuses.iterator();
			Map paruseAss = new HashMap();
			while(iterSbiParuses.hasNext()){
				SbiParuse paruse = (SbiParuse)iterSbiParuses.next();
				SbiParameters param = paruse.getSbiParameters();
				SbiLov lov = paruse.getSbiLov();
				Integer oldParamId = param.getParId();
				Integer oldLovId = lov.getLovId();
				Map assLovs = metaAss.getLovIDAssociation();
				Map assParams = metaAss.getParameterIDAssociation();
				Integer newParamId = (Integer)assParams.get(oldParamId);
				Integer newLovId = (Integer)assLovs.get(oldLovId);
				if(newParamId!=null) {
					SbiParameters newParam = ImportUtilities.makeNewSbiParameter(param, newParamId);
					paruse.setSbiParameters(newParam);
				}
				if(newLovId!=null){
					SbiLov newlov = ImportUtilities.makeNewSbiLov(lov, newLovId);
					paruse.setSbiLov(newlov);
				}
				Integer oldId = paruse.getUseId();
				Map paruseIdAss = metaAss.getParuseIDAssociation();
			    Set paruseIdAssSet = paruseIdAss.keySet();
				if(paruseIdAssSet.contains(oldId)){
					continue;
				}
				SbiParuse newParuse = ImportUtilities.makeNewSbiParuse(paruse);
			    importer.insertObject(newParuse, sessionCurrDB);
			    Integer newId = newParuse.getUseId();
			    sessionExpDB.evict(paruse);
			    metaAss.insertCoupleParuse(oldId, newId);
			}
		} finally {}
	}
	
	
	private void importParuseDet() throws EMFUserError {
		try{
			List exportedParuseDets = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParuseDet");
			Iterator iterSbiParuseDets = exportedParuseDets.iterator();
			while(iterSbiParuseDets.hasNext()){
				SbiParuseDet parusedet = (SbiParuseDet)iterSbiParuseDets.next();
				// get ids of exported role and paruse associzted
				Integer paruseid = parusedet.getId().getSbiParuse().getUseId();
				Integer roleid = parusedet.getId().getSbiExtRoles().getExtRoleId();
				// get association of roles and paruses
				Map paruseIdAss = metaAss.getParuseIDAssociation();
				Map roleIdAss = metaAss.getRoleIDAssociation();
				// try to get from association the id associate to the exported metadata
				Integer newParuseid = (Integer)paruseIdAss.get(paruseid);
				Integer newRoleid = (Integer)roleIdAss.get(roleid);
				// build a new id for the SbiParuseDet
				SbiParuseDetId parusedetid = parusedet.getId();
				if(newParuseid!=null) {
					SbiParuse sbiparuse = parusedetid.getSbiParuse();
					SbiParuse newParuse = ImportUtilities.makeNewSbiParuse(sbiparuse, newParuseid);				
					parusedetid.setSbiParuse(newParuse);
					paruseid = newParuseid;
				}
				if(newRoleid!=null){
					SbiExtRoles sbirole = parusedetid.getSbiExtRoles();
					SbiExtRoles newRole = ImportUtilities.makeNewSbiExtRole(sbirole, newRoleid);
					parusedetid.setSbiExtRoles(newRole);
					roleid=newRoleid;
				}
				parusedet.setId(parusedetid);
				// check if the association between metadata already exist
				Map unique = new HashMap();
				unique.put("paruseid", paruseid);
				unique.put("roleid", roleid);
				Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiParuseDet());
				if(existObj==null)
					importer.insertObject(parusedet, sessionCurrDB);
			}
		} finally {}
	}
	
	
	private void importParuseCheck() throws EMFUserError {
		try{
			List exportedParuseChecks = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParuseCk");
			Iterator iterSbiParuseChecks = exportedParuseChecks.iterator();
			while(iterSbiParuseChecks.hasNext()){
				SbiParuseCk paruseck = (SbiParuseCk)iterSbiParuseChecks.next();
				// get ids of exported paruse and check associzted
				Integer paruseid = paruseck.getId().getSbiParuse().getUseId();
				Integer checkid = paruseck.getId().getSbiChecks().getCheckId();
				// get association of checks and paruses
				Map paruseIdAss = metaAss.getParuseIDAssociation();
				Map checkIdAss = metaAss.getCheckIDAssociation();
				// try to get from association the id associate to the exported metadata
				Integer newParuseid = (Integer)paruseIdAss.get(paruseid);
				Integer newCheckid = (Integer)checkIdAss.get(checkid);
				// build a new id for the SbiParuseCheck
				SbiParuseCkId parusecheckid = paruseck.getId();
				if(newParuseid!=null) {
					SbiParuse sbiparuse = parusecheckid.getSbiParuse();
					SbiParuse newParuse = ImportUtilities.makeNewSbiParuse(sbiparuse, newParuseid);				
					parusecheckid.setSbiParuse(newParuse);
					paruseid = newParuseid;
				}
				if(newCheckid!=null){
					SbiChecks sbicheck = parusecheckid.getSbiChecks();
					SbiChecks newCheck = ImportUtilities.makeNewSbiCheck(sbicheck, newCheckid);
					parusecheckid.setSbiChecks(newCheck);
					checkid=newCheckid;
				}
				paruseck.setId(parusecheckid);
				// check if the association between metadata already exist
				Map unique = new HashMap();
				unique.put("paruseid", paruseid);
				unique.put("checkid", checkid);
				Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiParuseCk());
				if(existObj==null)
					importer.insertObject(paruseck, sessionCurrDB);
			}
		} finally {}
	}
	
	
	private void importFunctRoles() throws EMFUserError {
		try{
			List exportedFunctRoles = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiFuncRole");
			Iterator iterSbiFunctRoles = exportedFunctRoles.iterator();
			while(iterSbiFunctRoles.hasNext()){
				SbiFuncRole functrole = (SbiFuncRole)iterSbiFunctRoles.next();
				// get ids of exported role, function and state associzted
				Integer functid = functrole.getId().getFunction().getFunctId();
				Integer roleid = functrole.getId().getRole().getExtRoleId();
				Integer stateid = functrole.getId().getState().getValueId();
				// get association of roles and paruses
				Map functIdAss = metaAss.getFunctIDAssociation();
				Map roleIdAss = metaAss.getRoleIDAssociation();
				// try to get from association the id associate to the exported metadata
				Integer newFunctid = (Integer)functIdAss.get(functid);
				Integer newRoleid = (Integer)roleIdAss.get(roleid);
				// build a new id for the SbiFunctRole
				SbiFuncRoleId functroleid = functrole.getId();
				if(newFunctid!=null) {
					SbiFunctions sbifunct = functroleid.getFunction();
					SbiFunctions newFunct = ImportUtilities.makeNewSbiFunction(sbifunct, newFunctid);
					functroleid.setFunction(newFunct);
					functid = newFunctid;
				}
				if(newRoleid!=null){
					SbiExtRoles sbirole = functroleid.getRole();
					SbiExtRoles newRole = ImportUtilities.makeNewSbiExtRole(sbirole, newRoleid);
					functroleid.setRole(newRole);
					roleid=newRoleid;
				}
				// get sbiomain of the current system
				String stateCd = functrole.getStateCd();
				Map uniqueDom = new HashMap();
				uniqueDom.put("valuecd", stateCd);
				uniqueDom.put("domaincd", "STATE");
				SbiDomains existDom = (SbiDomains)importer.checkExistence(uniqueDom, sessionCurrDB, new SbiDomains());
				if(existDom!=null) {
					functroleid.setState(existDom);
				}
				functrole.setId(functroleid);
				functrole.setStateCd(existDom.getValueCd());
				// check if the association between metadata already exist
				Map unique = new HashMap();
				unique.put("stateid", stateid);
				unique.put("roleid", roleid);
				unique.put("functionid", functid);
				Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiFuncRole());
				if(existObj==null)
					importer.insertObject(functrole, sessionCurrDB);
			}
		} finally {}
	}
	
		
	private void importBIObjPar() throws EMFUserError {
		try{
			List exportedObjPars = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiObjPar");
			Iterator iterSbiObjPar = exportedObjPars.iterator();
			while(iterSbiObjPar.hasNext()){
				SbiObjPar objpar = (SbiObjPar)iterSbiObjPar.next();
				// get ids of exported param, object and prog associzted
				Integer paramid = objpar.getId().getSbiParameters().getParId();
				Integer biobjid = objpar.getId().getSbiObjects().getBiobjId();
				Integer prog = objpar.getId().getProg();
				// get association of biobj and params 
				Map paramIdAss = metaAss.getParameterIDAssociation();
				Map objIdAss = metaAss.getBIobjIDAssociation();
				// try to get from association the id associate to the exported metadata
				Integer newParamid = (Integer)paramIdAss.get(paramid);
				Integer newObjid = (Integer)objIdAss.get(biobjid);
				// build a new id for the SbiObjPar
				SbiObjParId objparid = objpar.getId();
				if(newParamid!=null) {
					SbiParameters sbiparam = objparid.getSbiParameters();
					SbiParameters newParam = ImportUtilities.makeNewSbiParameter(sbiparam, newParamid);
					objparid.setSbiParameters(newParam);
					paramid = newParamid;
				}
				if(newObjid!=null){
					SbiObjects sbiobj = objparid.getSbiObjects();
					SbiObjects newObj = ImportUtilities.makeNewSbiObject(sbiobj, newObjid);
					objparid.setSbiObjects(newObj);
					biobjid = newObjid;
				}
				objpar.setId(objparid);
				// check if the association between metadata already exist
				Map unique = new HashMap();
				unique.put("paramid", paramid);
				unique.put("biobjid", biobjid);
				unique.put("prog", prog);
				Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiObjPar());
				if(existObj==null)
					importer.insertObject(objpar, sessionCurrDB);
			}
		} finally {}
	}

	
	
	public void stopImport() {
		metaAss.clear();
		rollback();
		ImpExpGeneralUtilities.deleteDir(new File(pathBaseFolder));
	}

	
	
	public List getExportedConnections() throws EMFUserError {
		List connections = new ArrayList();
		String connFilePath = pathBaseFolder + "/connections.xml";
		try{
			FileInputStream fis = new FileInputStream(connFilePath);
			byte[] content =  GeneralUtilities.getByteArrayFromInputStream(fis);
			fis.close();
			String contentStr = new String(content);
			SourceBean connsSB = SourceBean.fromXMLString(contentStr);
			List connsList = connsSB.getAttributeAsList("CONNECTION-POOL");
			Iterator iterConns = connsList.iterator();
			while(iterConns.hasNext()){
				SourceBean connSB = (SourceBean)iterConns.next();
				String name = (String)connSB.getAttribute("connectionPoolName");
				String descr = (String)connSB.getAttribute("connectionDescription");
				if(descr==null) descr="";
				SourceBean driverSB = (SourceBean)connSB.getFilteredSourceBeanAttribute("CONNECTION-POOL-PARAMETER", 
												  "parameterName", "driverClass");
				if(driverSB==null) {
					SourceBean contNameSB=(SourceBean)connSB.getFilteredSourceBeanAttribute("CONNECTION-POOL-PARAMETER", 
			  							  "parameterName", "initialContext");
					String contName = (String)contNameSB.getAttribute("parameterValue");
					SourceBean jndiNameSB=(SourceBean)connSB.getFilteredSourceBeanAttribute("CONNECTION-POOL-PARAMETER", 
							  "parameterName", "resourceName");
					String jndiName = (String)jndiNameSB.getAttribute("parameterValue");
					JndiConnection conn = new JndiConnection();
					conn.setJndiType(true);
					conn.setName(name);
					conn.setDescription(descr);
					conn.setJndiContextName(contName);
					conn.setJndiName(jndiName);
					connections.add(conn);
				} else {
					String drivClass = (String)connSB.getAttribute("parameterValue");
					SourceBean connUrlSB = (SourceBean)connSB.getFilteredSourceBeanAttribute("CONNECTION-POOL-PARAMETER", 
							  				"parameterName", "connectionString");
					String connUrl = (String)connUrlSB.getAttribute("parameterValue");
					JdbcConnection conn = new JdbcConnection();
					conn.setJdbcType(true);
					conn.setName(name);
					conn.setDescription(descr);
					conn.setDriverClassName(drivClass);
					conn.setConnectionString(connUrl);
					connections.add(conn);
				}
			}
		} catch (FileNotFoundException fnfe) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getExportedConnections", 
			"Error while reading connections file " + fnfe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		} catch (SourceBeanException sbe) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getExportedConnections", 
			"Error while reading connections file " + sbe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		}catch (IOException ioe) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "getExportedConnections", 
			"Error while reading connections file " + ioe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		} finally {}
		return connections;
	}




	public void checkExistingMetadata() throws EMFUserError {
		/*
		List exportedDomains = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiDomains");
		Iterator iterSbiDomains = exportedDomains.iterator();
		while(iterSbiDomains.hasNext()){
			SbiDomains domExp = (SbiDomains)iterSbiDomains.next();
			String valuecd = domExp.getValueCd();
			String domcd = domExp.getDomainCd();
			Map unique = new HashMap();
			unique.put("valuecd", valuecd);
			unique.put("domaincd", domcd);
			Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
			if(existObj!=null) {
				SbiDomains domCurr = (SbiDomains)existObj;
				metaAss.insertCoupleDomain(domExp.getValueId(), domCurr.getValueId());
				metaAss.insertCoupleDomain(domExp, domCurr);
			}
		}
		*/
		List exportedParams = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParameters");
		Iterator iterSbiParams = exportedParams.iterator();
		while(iterSbiParams.hasNext()){
			SbiParameters paramExp = (SbiParameters)iterSbiParams.next();
			String labelPar = paramExp.getLabel();
			Object existObj = importer.checkExistence(labelPar, sessionCurrDB, new SbiParameters());
			if(existObj!=null) {
				SbiParameters paramCurr = (SbiParameters)existObj;
				metaAss.insertCoupleParameter(paramExp.getParId(), paramCurr.getParId());
				metaAss.insertCoupleParameter(paramExp, paramCurr);
			}
		}
		List exportedRoles = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiExtRoles");
		Iterator iterSbiRoles = exportedRoles.iterator();
		while(iterSbiRoles.hasNext()){
			SbiExtRoles roleExp = (SbiExtRoles)iterSbiRoles.next();
			String roleName = roleExp.getName();
			Integer expRoleId = roleExp.getExtRoleId();
			Map rolesAss = metaAss.getRoleIDAssociation();
			Set keysExpRoleAss = rolesAss.keySet();
			if(keysExpRoleAss.contains(expRoleId))
				continue;
			Object existObj = importer.checkExistence(roleName, sessionCurrDB, new SbiExtRoles());
			if(existObj!=null) {
				SbiExtRoles roleCurr = (SbiExtRoles)existObj;
				metaAss.insertCoupleRole(roleExp.getExtRoleId(), roleCurr.getExtRoleId());
				metaAss.insertCoupleRole(roleExp, roleCurr);
			}
		}
		List exportedParuse = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParuse");
		Iterator iterSbiParuse = exportedParuse.iterator();
		while(iterSbiParuse.hasNext()){
			SbiParuse paruseExp = (SbiParuse)iterSbiParuse.next();
			String label = paruseExp.getLabel();          
			SbiParameters par = paruseExp.getSbiParameters();
			Integer idPar = par.getParId();
			// check if the parameter has been associated to a current system parameter
			Map paramsAss = metaAss.getParameterIDAssociation();
			Integer idParAss = (Integer)paramsAss.get(idPar);
			if(idParAss!=null)
				idPar = idParAss;
			Map unique = new HashMap();
			unique.put("label", label);
			unique.put("idpar", idPar);
			Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiParuse());
			if(existObj!=null) {
				SbiParuse paruseCurr = (SbiParuse)existObj;
				metaAss.insertCoupleParuse(paruseExp.getUseId(), paruseCurr.getUseId());
				metaAss.insertCoupleParuse(paruseExp, paruseCurr);
			}
		}
		List exportedBiobj = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiObjects");
		Iterator iterSbiBiobj = exportedBiobj.iterator();
		while(iterSbiBiobj.hasNext()){
			SbiObjects objExp = (SbiObjects)iterSbiBiobj.next();
			String label = objExp.getLabel();
			Object existObj = importer.checkExistence(label, sessionCurrDB, new SbiObjects());
			if(existObj!=null) {
				SbiObjects objCurr = (SbiObjects)existObj;
				metaAss.insertCoupleBIObj(objExp.getBiobjId(), objCurr.getBiobjId());
				metaAss.insertCoupleBIObj(objExp, objCurr);
			}
		}
		List exportedLov = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiLov");
		Iterator iterSbiLov = exportedLov.iterator();
		while(iterSbiLov.hasNext()){
			SbiLov lovExp = (SbiLov)iterSbiLov.next();
			String label = lovExp.getLabel();
			Object existObj = importer.checkExistence(label, sessionCurrDB, new SbiLov());
			if(existObj!=null) {
				SbiLov lovCurr = (SbiLov)existObj;
				metaAss.insertCoupleLov(lovExp.getLovId(), lovCurr.getLovId());
				metaAss.insertCoupleLov(lovExp, lovCurr);
			}
		}
		List exportedFunct = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiFunctions");
		Iterator iterSbiFunct = exportedFunct.iterator();
		while(iterSbiFunct.hasNext()){
			SbiFunctions functExp = (SbiFunctions)iterSbiFunct.next();
			String code = functExp.getCode();
			Object existObj = importer.checkExistence(code, sessionCurrDB, new SbiFunctions());
			if(existObj!=null) {
				SbiFunctions functCurr = (SbiFunctions)existObj;
				metaAss.insertCoupleFunct(functExp.getFunctId(), functCurr.getFunctId());
				metaAss.insertCoupleFunct(functExp, functCurr);
			}
		}
		List exportedEngine = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiEngines");
		Iterator iterSbiEng = exportedEngine.iterator();
		while(iterSbiEng.hasNext()){
			SbiEngines engExp = (SbiEngines)iterSbiEng.next();
			String label = engExp.getLabel();
			Integer expEngineId = engExp.getEngineId();
			Map engAss = metaAss.getEngineIDAssociation();
			Set keysExpEngAss = engAss.keySet();
			if(keysExpEngAss.contains(expEngineId))
				continue;
			Object existObj = importer.checkExistence(label, sessionCurrDB, new SbiEngines());
			if(existObj!=null) {
				SbiEngines engCurr = (SbiEngines)existObj;
				metaAss.insertCoupleEngine(engExp.getEngineId(), engCurr.getEngineId());
				metaAss.insertCoupleEngine(engExp, engCurr);
			}
		}
		List exportedCheck = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiChecks");
		Iterator iterSbiCheck = exportedCheck.iterator();
		while(iterSbiCheck.hasNext()){
			SbiChecks checkExp = (SbiChecks)iterSbiCheck.next();
			String label = checkExp.getLabel();
			Object existObj = importer.checkExistence(label, sessionCurrDB, new SbiChecks());
			if(existObj!=null) {
				SbiChecks checkCurr = (SbiChecks)existObj;
				metaAss.insertCoupleCheck(checkExp.getCheckId(), checkCurr.getCheckId());
				metaAss.insertCoupleCheck(checkExp, checkCurr);
			}
		}
	}




	public MetadataAssociations getMetadataAssociation() {
		return metaAss;
	}
	
	
	

	
}
