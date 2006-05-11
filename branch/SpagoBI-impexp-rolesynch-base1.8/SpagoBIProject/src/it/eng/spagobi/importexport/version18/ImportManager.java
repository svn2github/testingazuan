package it.eng.spagobi.importexport.version18;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.importexport.ExistingMetadata;
import it.eng.spagobi.importexport.IImportManager;
import it.eng.spagobi.importexport.JdbcConnection;
import it.eng.spagobi.importexport.JndiConnection;
import it.eng.spagobi.metadata.HibernateUtil;
import it.eng.spagobi.metadata.SbiChecks;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.metadata.SbiFuncRole;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.metadata.SbiLov;
import it.eng.spagobi.metadata.SbiObjPar;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.metadata.SbiParameters;
import it.eng.spagobi.metadata.SbiParuse;
import it.eng.spagobi.metadata.SbiParuseCk;
import it.eng.spagobi.metadata.SbiParuseDet;
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
	private static Session sessionExpDB = null;
	private static Transaction txExpDB = null;
	private static Session sessionCurrDB = null;
	private static Transaction txCurrDB = null;
	
	
	
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

	
	
	public void updateRoleReferences(Map roleAssociations) throws EMFUserError {
		/* 
		 * The key of the map are the id of the exported roles
		 * Each key value is the id of the system role associate
		 */
		// each exported role should be associate only to one system role
		Set rolesAssKeys = roleAssociations.keySet();
		Iterator iterRoleAssKeys1 = rolesAssKeys.iterator();
		while(iterRoleAssKeys1.hasNext()) {
			String roleExpId = (String)iterRoleAssKeys1.next();
			String roleAssId = (String)roleAssociations.get(roleExpId);
			Iterator iterRoleAssKeys2 = rolesAssKeys.iterator();
			while(iterRoleAssKeys2.hasNext()) {
				String otherRoleExpId = (String)iterRoleAssKeys2.next();
				if(!otherRoleExpId.equals(roleExpId)) {
					String otherRoleAssId = (String)roleAssociations.get(otherRoleExpId);
					if(otherRoleAssId.equals(roleAssId)){
						throw new EMFUserError(EMFErrorSeverity.ERROR, 8001);
					}
				}
			}
		}
		importer.updateReferences(roleAssociations, txExpDB, sessionExpDB, SbiExtRoles.class);		
	}
	
	
	
	public void updateEngineReferences(Map roleAssociations) throws EMFUserError {
		/* 
		 * The key of the map are the id of the exported engines
		 * Each key value is the id of the system engine associate
		 */
		importer.updateReferences(roleAssociations, txExpDB, sessionExpDB, SbiEngines.class);
	}
	
	
	public void updateConnectionReferences(Map connAssociations) throws EMFUserError {
		/* 
		 * The key of the map are the name of the exported connections
		 * Each key value is the name of the current system connection associate
		 */
		importer.updateConnRefs(connAssociations, txExpDB, sessionExpDB);
	}
	
	
	public void updateMetadataReferences(ExistingMetadata emd) throws EMFUserError {
		importer.updateReferences(emd.getParuseIDAssociation(), txExpDB, sessionExpDB, SbiParuse.class);
		importer.updateReferences(emd.getBIobjIDAssociation(), txExpDB, sessionExpDB, SbiObjects.class);
		importer.updateReferences(emd.getRoleIDAssociation(), txExpDB, sessionExpDB, SbiExtRoles.class);
		importer.updateReferences(emd.getLovIDAssociation(), txExpDB, sessionExpDB, SbiLov.class);
		importer.updateReferences(emd.getFunctIDAssociation(), txExpDB, sessionExpDB, SbiFunctions.class);
		importer.updateReferences(emd.getEngineIDAssociation(), txExpDB, sessionExpDB, SbiEngines.class);
		importer.updateReferences(emd.getCheckIDAssociation(), txExpDB, sessionExpDB, SbiChecks.class);
		importer.updateReferences(emd.getParameterIDAssociation(), txExpDB, sessionExpDB, SbiParameters.class);	
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
		ImpExpGeneralUtilities.deleteDir(new File(pathBaseFolder));
	}
	
		
	
	public void importObjects() throws EMFUserError {
		importRoles();
		importEngines();
		importFunctionalities();
		importFunctRoles();
		importLovs();
		importChecks();
		importParameters();
		importParuse();
		importParuseDet();
		importParuseCheck();
		importBIObjects();
		importBIObjPar();
	}
	

	private void importRoles() throws EMFUserError {
		try{
			List exportedRoles = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiExtRoles");
			Iterator iterSbiRoles = exportedRoles.iterator();
			Map roleAss = new HashMap();
			while(iterSbiRoles.hasNext()){
				SbiExtRoles role = (SbiExtRoles)iterSbiRoles.next();
				role.setSbiParuseDets(new HashSet());
				role.setSbiFuncRoles(new HashSet());
				Integer oldId = role.getExtRoleId();
			    importer.insertObject(role, sessionCurrDB);
			    Integer newId = role.getExtRoleId();
			    sessionExpDB.evict(role);
			    roleAss.put(oldId.toString(), newId.toString());
			}
			importer.updateReferences(roleAss, txExpDB, sessionExpDB, SbiExtRoles.class);
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
			    importer.insertObject(engine, sessionCurrDB);
			    Integer newId = engine.getEngineId();
			    sessionExpDB.evict(engine);
			    engineAss.put(oldId.toString(), newId.toString());
			}
			importer.updateReferences(engineAss, txExpDB, sessionExpDB, SbiEngines.class);
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
			    importer.insertObject(funct, sessionCurrDB);
			    Integer newId = funct.getFunctId(); 
			    sessionExpDB.evict(funct);
			    functAss.put(oldId.toString(), newId.toString());
			}
			importer.updateReferences(functAss, txExpDB, sessionExpDB, SbiFunctions.class);
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
			    importer.insertObject(lov, sessionCurrDB);
			    Integer newId = lov.getLovId(); 
			    sessionExpDB.evict(lov);
			    lovAss.put(oldId.toString(), newId.toString());
			}
			importer.updateReferences(lovAss, txExpDB, sessionExpDB, SbiLov.class);
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
			    importer.insertObject(check, sessionCurrDB);
			    Integer newId = check.getCheckId();
			    sessionExpDB.evict(check);
			    checkAss.put(oldId.toString(), newId.toString());
			}
			importer.updateReferences(checkAss, txExpDB, sessionExpDB, SbiChecks.class);
		} finally {}
	}
	
	
	
	private void importParameters() throws EMFUserError {
		try{
			List exportedParams = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParameters");
			Iterator iterSbiParams = exportedParams.iterator();
			Map paramAss = new HashMap();
			while(iterSbiParams.hasNext()){
				SbiParameters param = (SbiParameters)iterSbiParams.next();
				param.setSbiObjPars(new HashSet());
				param.setSbiParuses(new HashSet());
				Integer oldId = param.getParId();
			    importer.insertObject(param, sessionCurrDB);
			    Integer newId = param.getParId();
			    sessionExpDB.evict(param);
			    paramAss.put(oldId.toString(), newId.toString());
			}
			importer.updateReferences(paramAss, txExpDB, sessionExpDB, SbiParameters.class);
		} finally {}
	}
	
	
	
	private void importParuse() throws EMFUserError {
		try{
			List exportedParuses = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParuse");
			Iterator iterSbiParuses = exportedParuses.iterator();
			Map paruseAss = new HashMap();
			while(iterSbiParuses.hasNext()){
				SbiParuse paruse = (SbiParuse)iterSbiParuses.next();
				paruse.setSbiParuseCks(new HashSet());
				paruse.setSbiParuseDets(new HashSet());
				Integer oldId = paruse.getUseId();
			    importer.insertObject(paruse, sessionCurrDB);
			    Integer newId = paruse.getUseId();
			    sessionExpDB.evict(paruse);
			    paruseAss.put(oldId.toString(), newId.toString());
			}
			importer.updateReferences(paruseAss, txExpDB, sessionExpDB, SbiParuse.class);
		} finally {}
	}
	
	
	private void importParuseDet() throws EMFUserError {
		try{
			List exportedParuseDets = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParuseDet");
			Iterator iterSbiParuseDets = exportedParuseDets.iterator();
			while(iterSbiParuseDets.hasNext()){
				SbiParuseDet parusedet = (SbiParuseDet)iterSbiParuseDets.next();
				Map unique = new HashMap();
				unique.put("paruseid", parusedet.getId().getSbiParuse().getUseId());
				unique.put("roleid", parusedet.getId().getSbiExtRoles().getExtRoleId());
				Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiParuseDet());
				if(existObj==null)
					importer.insertObject(parusedet, sessionCurrDB);
				
				
			}
			txCurrDB = sessionCurrDB.beginTransaction();
		} finally {}
	}
	
	
	private void importParuseCheck() throws EMFUserError {
		try{
			List exportedParuseChecks = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParuseCk");
			Iterator iterSbiParuseChecks = exportedParuseChecks.iterator();
			while(iterSbiParuseChecks.hasNext()){
				SbiParuseCk paruseck = (SbiParuseCk)iterSbiParuseChecks.next();
				Map unique = new HashMap();
				unique.put("paruseid", paruseck.getId().getSbiParuse().getUseId());
				unique.put("checkid", paruseck.getId().getSbiChecks().getCheckId());
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
				Map unique = new HashMap();
				unique.put("stateid", functrole.getId().getState().getValueId());
				unique.put("roleid", functrole.getId().getRole().getExtRoleId());
				unique.put("functionid", functrole.getId().getFunction().getFunctId());
				Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiFuncRole());
				if(existObj==null)
					importer.insertObject(functrole, sessionCurrDB);
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
				Integer oldId = obj.getBiobjId();
				obj.setSbiObjFuncs(new HashSet());
				obj.setSbiObjPars(new HashSet());
				obj.setSbiObjStates(new HashSet());
				obj = importer.insertBIObject(obj, pathContentFolder, sessionCurrDB);
			    Integer newId = obj.getBiobjId(); 
			    sessionExpDB.evict(obj);
			    objAss.put(oldId.toString(), newId.toString());
			}
			importer.updateReferences(objAss, txExpDB, sessionExpDB, SbiObjects.class);
		} finally {}
	}
	
	
	
	private void importBIObjPar() throws EMFUserError {
		try{
			List exportedObjPars = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiObjPar");
			Iterator iterSbiObjPar = exportedObjPars.iterator();
			while(iterSbiObjPar.hasNext()){
				SbiObjPar objpar = (SbiObjPar)iterSbiObjPar.next();
				Map unique = new HashMap();
				unique.put("paramid", objpar.getId().getSbiParameters().getParId());
				unique.put("biobjid", objpar.getId().getSbiObjects().getBiobjId());
				unique.put("prog", objpar.getId().getProg());
				Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiObjPar());
				if(existObj==null)
					importer.insertObject(objpar, sessionCurrDB);
			}
		} finally {}
	}

	
	
	public void stopImport() {
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




	public ExistingMetadata checkExistingMetadata() throws EMFUserError {
		
		ExistingMetadata existMD = new ExistingMetadata();
		List exportedParams = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParameters");
		Iterator iterSbiParams = exportedParams.iterator();
		while(iterSbiParams.hasNext()){
			SbiParameters paramExp = (SbiParameters)iterSbiParams.next();
			String labelPar = paramExp.getLabel();
			Object existObj = importer.checkExistence(labelPar, sessionCurrDB, new SbiParameters());
			if(existObj!=null) {
				SbiParameters paramCurr = (SbiParameters)existObj;
				existMD.insertCoupleParameter(paramExp, paramCurr);
			}
		}
		List exportedRoles = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiExtRoles");
		Iterator iterSbiRoles = exportedRoles.iterator();
		while(iterSbiRoles.hasNext()){
			SbiExtRoles roleExp = (SbiExtRoles)iterSbiRoles.next();
			String roleName = roleExp.getName();
			Object existObj = importer.checkExistence(roleName, sessionCurrDB, new SbiExtRoles());
			if(existObj!=null) {
				SbiExtRoles roleCurr = (SbiExtRoles)existObj;
				existMD.insertCoupleRole(roleExp, roleCurr);
			}
		}
		List exportedParuse = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParuse");
		Iterator iterSbiParuse = exportedParuse.iterator();
		while(iterSbiParuse.hasNext()){
			SbiParuse paruseExp = (SbiParuse)iterSbiParuse.next();
			String label = paruseExp.getLabel();          
			SbiParameters par = paruseExp.getSbiParameters();
			Integer idPar = par.getParId();
			Map unique = new HashMap();
			unique.put("label", label);
			unique.put("idpar", idPar);
			Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiParuse());
			if(existObj!=null) {
				SbiParuse paruseCurr = (SbiParuse)existObj;
				existMD.insertCoupleParuse(paruseExp, paruseCurr);
			}
			sessionExpDB.evict(paruseExp);
		}
		List exportedBiobj = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiObjects");
		Iterator iterSbiBiobj = exportedBiobj.iterator();
		while(iterSbiBiobj.hasNext()){
			SbiObjects objExp = (SbiObjects)iterSbiBiobj.next();
			String label = objExp.getLabel();
			Object existObj = importer.checkExistence(label, sessionCurrDB, new SbiObjects());
			if(existObj!=null) {
				SbiObjects objCurr = (SbiObjects)existObj;
				existMD.insertCoupleBIObj(objExp, objCurr);
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
				existMD.insertCoupleLov(lovExp, lovCurr);
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
				existMD.insertCoupleFunct(functExp, functCurr);
			}
		}
		List exportedEngine = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiEngines");
		Iterator iterSbiEng = exportedEngine.iterator();
		while(iterSbiEng.hasNext()){
			SbiEngines engExp = (SbiEngines)iterSbiEng.next();
			String label = engExp.getLabel();
			Object existObj = importer.checkExistence(label, sessionCurrDB, new SbiEngines());
			if(existObj!=null) {
				SbiEngines engCurr = (SbiEngines)existObj;
				existMD.insertCoupleEngine(engExp, engCurr);
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
				existMD.insertCoupleCheck(checkExp, checkCurr);
			}
		}
		return existMD;
	}
	
	
	

	
}
