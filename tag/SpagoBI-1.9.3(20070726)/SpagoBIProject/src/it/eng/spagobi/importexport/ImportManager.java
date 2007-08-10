/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.importexport;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.importexport.transformers.TransformersUtilities;
import it.eng.spagobi.metadata.HibernateUtil;
import it.eng.spagobi.metadata.SbiChecks;
import it.eng.spagobi.metadata.SbiDomains;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.metadata.SbiFuncRole;
import it.eng.spagobi.metadata.SbiFuncRoleId;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.metadata.SbiLov;
import it.eng.spagobi.metadata.SbiObjFunc;
import it.eng.spagobi.metadata.SbiObjFuncId;
import it.eng.spagobi.metadata.SbiObjPar;
import it.eng.spagobi.metadata.SbiObjParuse;
import it.eng.spagobi.metadata.SbiObjParuseId;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.metadata.SbiParameters;
import it.eng.spagobi.metadata.SbiParuse;
import it.eng.spagobi.metadata.SbiParuseCk;
import it.eng.spagobi.metadata.SbiParuseCkId;
import it.eng.spagobi.metadata.SbiParuseDet;
import it.eng.spagobi.metadata.SbiParuseDetId;
import it.eng.spagobi.metadata.SbiSubreports;
import it.eng.spagobi.metadata.SbiSubreportsId;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Implements the interface which defines methods for managing the import requests
 */
public class ImportManager implements IImportManager, Serializable {

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
	private MetadataLogger metaLog = null;
	private String exportedFileName = "";
	
	
	/**
	 * Prepare the environment for the import procedure
	 * @param pathImpTmpFold The path of the temporary import folder
	 * @param archiveName the name of the compress exported file
	 * @param archiveContent the bytes of the compress exported file 
	 */
	public void prepareImport(String pathImpTmpFold, String archiveName, byte[] archiveContent) throws EMFUserError {
		// create directories of the tmp import folder
		File impTmpFold = new File(pathImpTmpFold);
		impTmpFold.mkdirs();
		// write content uploaded into a tmp archive
		String pathArchiveFile = pathImpTmpFold + "/" +archiveName;
		File archive = new File(pathArchiveFile);
		exportedFileName = archiveName;
		try{
			FileOutputStream fos = new FileOutputStream(archive); 
			fos.write(archiveContent);
			fos.flush();
			fos.close();
		} catch (IOException ioe) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "prepareImport",
		               			   "Error while writing archive content into a tmp file " + ioe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
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
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "prepareImport",
		   			               "Error while reading properties file " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		}
		importer = new ImporterMetadata();
		sessionFactoryExpDB = ImportUtilities.getHibSessionExportDB(pathDBFolder);
		sessionExpDB = sessionFactoryExpDB.openSession();
		txExpDB = sessionExpDB.beginTransaction();
		sessionCurrDB = HibernateUtil.currentSession();
		txCurrDB = sessionCurrDB.beginTransaction();
		metaAss = new MetadataAssociations();
		metaLog = new MetadataLogger();
	}
	
	
	
	/**
	 * Gets the SpagoBI version of the exported file
	 * @return The SpagoBI version of the exported file
	 */
	public String getExportVersion() {
		return props.getProperty("spagobi-version");
	}
	
	
	/**
	 * Gets the current SpagobI version
	 * @return The current SpagoBI version
	 */
	public String getCurrentVersion() {
		ConfigSingleton conf = ConfigSingleton.getInstance();
		SourceBean curVerSB = (SourceBean)conf.getAttribute("IMPORTEXPORT.CURRENTVERSION");
		String curVer = (String)curVerSB.getAttribute("version");
		return curVer;
	}
	
	
	/**
	 * Gets the path of the cms base folder of the exported archive
	 * @return The path of the cms base folder of the exported archive
	 */
	private String getExportedCmsBaseFolder() {
		return props.getProperty("cms-basefolder");
	}
	
	/**
	 * Gets the list of all exported roles
	 * @return The list of exported roles
	 */
	public List getExportedRoles() throws EMFUserError {
		List exportedRoles = null;
		exportedRoles = importer.getAllExportedRoles(txExpDB, sessionExpDB);
		return exportedRoles;
	}

	/**
	 * Gets the list of all exported engines
	 * @return The list of exported engines
	 */
	public List getExportedEngines() throws EMFUserError {
		List exportedEngines = null;
		exportedEngines = importer.getAllExportedEngines(txExpDB, sessionExpDB);
		return exportedEngines;
	}

	
	/**
	 * checks if two or more exported roles are associate to the same current role
	 * @param roleAssociations Map of assocaition between exported roles and 
	 * roles of the portal in use
	 * @throws EMFUserError if two ore more exported roles are associate
	 * to the same current role
	 */
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
						throw new EMFUserError(EMFErrorSeverity.ERROR, "8001", "component_impexp_messages");
					}
				}
			}
		}	
	}
	
	
	/**
	 * Update the connection name for each list of values of type query 
	 * based on association between exported connections and current system connections
	 * @param connAssociations Map of the associations between exported connections
	 * and current system connections
	 * @throws EMFUserError
	 */
	public void updateConnectionReferences(Map connAssociations) throws EMFUserError {
		/* 
		 * The key of the map are the name of the exported connections
		 * Each key value is the name of the current system connection associate
		 */
		importer.updateConnRefs(connAssociations, txExpDB, sessionExpDB, metaLog);
	}
	
	
	/**
	 * Closes Hibernate session factory for the exported database
	 */
	private void closeSessionFactory() {
		if(sessionFactoryExpDB!=null){
			sessionFactoryExpDB.close();
		}
	}
	
	
	/**
	 * Close Hibernate sessions for exported and current database
	 */
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

	
	
	/**
	 * Rollbacks each previous changes made on exported and current databases
	 */
	private void rollback() {
		if(txExpDB!=null)
			txExpDB.rollback();
		if(txCurrDB!=null)
			txCurrDB.rollback();
		closeSession();
		closeSessionFactory();
	}
	
	
	
	/**
	 * Commits all changes made on exported and current databases
	 */
	public ImportResultInfo commitAllChanges() throws EMFUserError {
		// commit all database changes and close hibernate connection
		txCurrDB.commit();
		closeSession();
		closeSessionFactory();
		// clear metadata association
		metaAss.clear();
		// create the import info bean 
		ImportResultInfo iri = new ImportResultInfo();
		// create the folder which contains the import result files
		Date now = new Date();
		String pathFolderImportOutcome = pathImportTmpFolder + "/import_" + now.getTime();
		File fileFolderImportOutcome = new File(pathFolderImportOutcome);
		fileFolderImportOutcome.mkdirs();
		// fill the result bean with eventual manual task info	
		String pathManualTaskFolder = pathBaseFolder + "/" + ImportExportConstants.MANUALTASK_FOLDER_NAME;
		File fileManualTaskFolder = new File(pathManualTaskFolder);
		if(fileManualTaskFolder.exists()) {
			String[] manualTaskFolders = fileManualTaskFolder.list();
			Map manualTaskMap = new HashMap();
			String nameTask = "";
			for(int i=0; i<manualTaskFolders.length; i++) {
				try{
					String pathMTFolder = pathManualTaskFolder + "/" + manualTaskFolders[i];
					File fileMTFolder = new File(pathMTFolder);
					if(!fileMTFolder.isDirectory())
						continue;
					String pathFilePropMT = pathManualTaskFolder + "/"+manualTaskFolders[i]+".properties";
					File filePropMT = new File(pathFilePropMT);
					FileInputStream fisProp = new FileInputStream(filePropMT);
					Properties props = new Properties();
					props.load(fisProp);
					nameTask = props.getProperty("name");
					fisProp.close();
				    // copy the properties 
				    FileOutputStream fosProp = new FileOutputStream(pathFolderImportOutcome + "/" + manualTaskFolders[i] + ".properties");
				    props.store(fosProp, "");
				    //GeneralUtilities.flushFromInputStreamToOutputStream(fisProp, fosProp, true);
				    // create zip of the task folder
				    String manualTaskZipFilePath = pathFolderImportOutcome + "/" + manualTaskFolders[i] + ".zip";
				    FileOutputStream fosMT = new FileOutputStream(manualTaskZipFilePath);
					ZipOutputStream zipoutMT = new ZipOutputStream(fosMT);
					TransformersUtilities.compressFolder(pathMTFolder, pathMTFolder, zipoutMT);
					zipoutMT.flush();
					zipoutMT.close();
					fosMT.close();
					// put task into the manual task map 
					manualTaskMap.put(nameTask, manualTaskZipFilePath);
				} catch (Exception e) {
					SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
							               "commitAllChanges", "Error while generatin zip file for manual task " + nameTask, e);
				}
			}
			iri.setManualTasks(manualTaskMap);
		}
		// delete the tmp directory of the current import operation
		ImpExpGeneralUtilities.deleteDir(new File(pathBaseFolder));
	    // generate the log file 
		File logFile = new File(pathFolderImportOutcome + "/" + exportedFileName + ".log");
	    if(logFile.exists())
	    	logFile.delete();
		try{
		    FileOutputStream fos = new FileOutputStream(logFile);
		    fos.write(metaLog.getLogBytes());
		    fos.flush();
		    fos.close();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "commitAllChanges",
		                           "Error while writing log file " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		}
		// set into the result bean the log file path
		iri.setPathLogFile(logFile.getPath());
		// return the result info bean
	    return iri;
	}
	
		
	/**
	 * Imports the exported objects
	 */
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
		importBIObjectLinks();
		importFunctObject();
		importFunctRoles();
		importParuseDet();
		importParuseCheck();
		importBIObjPar();
		importObjParUse();
	}
	
   
	/**
	 * Import exported roles
	 * @throws EMFUserError
	 */
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
					metaLog.log("Exported role "+role.getName()+" not inserted" +
							" because it has been associated to an existing role or it has the same name " +
							" of an existing role");
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
				metaLog.log("Inserted new role " + newRole.getName());
			    Integer newId = newRole.getExtRoleId();
			    metaAss.insertCoupleRole(oldId, newId);
			}
		} finally {}
	}
	
	
	/**
	 * Imports exported engines
	 * @throws EMFUserError
	 */
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
					metaLog.log("Exported engine "+engine.getName()+" not inserted" +
							" because it has been associated to an existing engine or it has the same label " +
							" of an existing engine");
					continue;
				}
				SbiEngines newEng = ImportUtilities.makeNewSbiEngine(engine);
				SbiDomains engineTypeDomain = newEng.getEngineType();
				Map uniqueEngineType = new HashMap();
				uniqueEngineType.put("valuecd", engineTypeDomain.getValueCd());
				uniqueEngineType.put("domaincd", "ENGINE_TYPE");
				SbiDomains existEngineTypeDomain = (SbiDomains)importer.checkExistence(uniqueEngineType, sessionCurrDB, new SbiDomains());
				if(existEngineTypeDomain!=null) {
					newEng.setEngineType(existEngineTypeDomain);
				}
				SbiDomains biobjectTypeDomain = newEng.getBiobjType();
				Map uniqueBiobjectType = new HashMap();
				uniqueBiobjectType.put("valuecd", biobjectTypeDomain.getValueCd());
				uniqueBiobjectType.put("domaincd", "BIOBJ_TYPE");
				SbiDomains existBiobjectTypeDomain = (SbiDomains)importer.checkExistence(uniqueBiobjectType, sessionCurrDB, new SbiDomains());
				if(existBiobjectTypeDomain!=null) {
					newEng.setBiobjType(existBiobjectTypeDomain);
				}
			    importer.insertObject(newEng, sessionCurrDB);
			    metaLog.log("Inserted new engine " + engine.getName());
			    Integer newId = newEng.getEngineId();
			    metaAss.insertCoupleEngine(oldId, newId);
			}
		} finally {}
	}
	
	
	/**
	 * Imports exported functionalities
	 * @throws EMFUserError
	 */
	private void importFunctionalities() throws EMFUserError {
		try{
			List exportedFuncts = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiFunctions");
			
			while(exportedFuncts.size()!=0) {
				Iterator iterSbiFuncts = exportedFuncts.iterator();
				int minEl = 1000;
				SbiFunctions functToInsert = null;
				SbiFunctions funct = null;
				while(iterSbiFuncts.hasNext()){
					funct = (SbiFunctions)iterSbiFuncts.next();
					String path = funct.getPath();
					int numEl = path.split("/").length;
					if(numEl<minEl) {
						minEl = numEl;
						functToInsert = funct;
					}
				}
				// remove function from list
				exportedFuncts = removeFromList(exportedFuncts, functToInsert);
				//insert function
				//functToInsert.setSbiFuncRoles(new HashSet());
				//functToInsert.setSbiObjFuncs(new HashSet());
				Integer oldId = functToInsert.getFunctId();
				Map functIdAss = metaAss.getFunctIDAssociation();
			    Set functIdAssSet = functIdAss.keySet();
				if(functIdAssSet.contains(oldId)){
					metaLog.log("Exported functionality "+functToInsert.getName()+" not inserted" +
							" because it has the same label (and the same path) of an existing functionality");
					continue;
				}
				SbiFunctions newFunct = ImportUtilities.makeNewSbiFunction(functToInsert);
				String functCd = functToInsert.getFunctTypeCd();
				Map unique = new HashMap();
				unique.put("valuecd", functCd);
				unique.put("domaincd", "FUNCT_TYPE");
				SbiDomains existDom = (SbiDomains)importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
				if(existDom!=null) {
					newFunct.setFunctType(existDom);
					newFunct.setFunctTypeCd(existDom.getValueCd());
				}
				String path = newFunct.getPath();
				String parentPath = path.substring(0, path.lastIndexOf('/'));
				Query hibQuery = sessionCurrDB.createQuery(" from SbiFunctions where path = '" + parentPath + "'");
		    	SbiFunctions functParent = (SbiFunctions) hibQuery.uniqueResult();
		    	if(functParent!=null) {
		    		newFunct.setParentFunct(functParent);
		    	}
				// manages prog column that determines the folders order
				if (functParent == null) newFunct.setProg(new Integer(1));
				else {
					// loads sub functionalities
					Query query = sessionCurrDB.createQuery("select max(s.prog) from SbiFunctions s where s.parentFunct.functId = " + functParent.getFunctId());
					Integer maxProg = (Integer) query.uniqueResult();
					if (maxProg != null) newFunct.setProg(new Integer(maxProg.intValue() + 1));
					else newFunct.setProg(new Integer(1));
				}
			    importer.insertObject(newFunct, sessionCurrDB);
			    metaLog.log("Inserted new functionality " + newFunct.getName() + " with path " + newFunct.getPath());
			    Integer newId = newFunct.getFunctId(); 
			    metaAss.insertCoupleFunct(oldId, newId);
				
			}
		} finally {}
		
		
		//throw new EMFUserError(EMFErrorSeverity.ERROR, 8008);
	}
	

	private List removeFromList(List complete, SbiFunctions funct) {
		List toReturn = new ArrayList();
		Iterator iterList = complete.iterator();
		while(iterList.hasNext()) {
			SbiFunctions listFunct = (SbiFunctions)iterList.next();
			if(!listFunct.getPath().equals(funct.getPath())) {
				toReturn.add(listFunct);
			}
		}
		return toReturn;
	}
	
	
	/**
	 * Import exported lovs
	 * @throws EMFUserError
	 */
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
					metaLog.log("Exported lov "+lov.getName()+" not inserted" +
								" because it has the same label of an existing lov");
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
			    metaLog.log("Inserted new lov " + newlov.getName());
			    Integer newId = newlov.getLovId(); 
			    metaAss.insertCoupleLov(oldId, newId);
			}
		} finally {}
	}
	
	
	/**
	 * Import exported checks
	 * @throws EMFUserError
	 */
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
					metaLog.log("Exported check "+check.getName()+" not inserted" +
								" because it has the same label of an existing check");
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
			    metaLog.log("Inserted new check " + newck.getName());
			    Integer newId = newck.getCheckId();
			    metaAss.insertCoupleCheck(oldId, newId);
			}
		} finally {}
	}
	
	
	/**
	 * Import exported parameters
	 * @throws EMFUserError
	 */
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
					metaLog.log("Exported parameter "+param.getName()+" not inserted" +
								" because it has the same label of an existing parameter");
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
			    metaLog.log("Inserted new parameter " + newPar.getName());
			    Integer newId = newPar.getParId();
			    metaAss.insertCoupleParameter(oldId, newId);
			}
		} finally {}
	}
	
	
	/**
	 * import exported biobjects
	 * @throws EMFUserError
	 */
	private void importBIObjects() throws EMFUserError {
		try{
			List exportedBIObjs = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiObjects");
			Iterator iterSbiObjs = exportedBIObjs.iterator();
			Map objAss = new HashMap();			
			while(iterSbiObjs.hasNext()){
				SbiObjects obj = (SbiObjects)iterSbiObjs.next();
				
				String oldpath = obj.getPath();
				
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
					metaLog.log("Exported biobject "+obj.getName()+" not inserted" +
								" because it has the same label of an existing biobject");
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
				 metaLog.log("Inserted new biobject " + newObj.getName());
			    Integer newId = newObj.getBiobjId(); 
			    metaAss.insertCoupleBIObj(oldId, newId);
			    
			    //importer.insertSubObjects(oldpath, pathContentFolder,newObj); 
			   
			}
		} finally {}
	}
	
	
	/**
	 * Imports exported paruses
	 * @throws EMFUserError
	 */
	private void importParuse() throws EMFUserError {
		try{
			List exportedParuses = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiParuse");
			Iterator iterSbiParuses = exportedParuses.iterator();
			Map paruseAss = new HashMap();
			while(iterSbiParuses.hasNext()){
				SbiParuse paruse = (SbiParuse)iterSbiParuses.next();
				SbiParameters param = paruse.getSbiParameters();
				Integer oldParamId = param.getParId();
				Map assParams = metaAss.getParameterIDAssociation();
				Integer newParamId = (Integer)assParams.get(oldParamId);
				if(newParamId!=null) {
					SbiParameters newParam = ImportUtilities.makeNewSbiParameter(param, newParamId);
					paruse.setSbiParameters(newParam);
				}
				
				SbiLov lov = paruse.getSbiLov();
				if(lov!=null){
					Integer oldLovId = lov.getLovId();
					Map assLovs = metaAss.getLovIDAssociation();
					Integer newLovId = (Integer)assLovs.get(oldLovId);
					if(newLovId!=null){
						SbiLov newlov = ImportUtilities.makeNewSbiLov(lov, newLovId);
						paruse.setSbiLov(newlov);
					}
				}
				
				Integer oldId = paruse.getUseId();
				Map paruseIdAss = metaAss.getParuseIDAssociation();
			    Set paruseIdAssSet = paruseIdAss.keySet();
				if(paruseIdAssSet.contains(oldId)){
					metaLog.log("Exported parameter use "+paruse.getName()+" not inserted" +
								" because it has the same label of an existing parameter use");
					continue;
				}
				SbiParuse newParuse = ImportUtilities.makeNewSbiParuse(paruse);
			    importer.insertObject(newParuse, sessionCurrDB);
			    metaLog.log("Inserted new parameter use " + newParuse.getName() + " for param " + param.getName());
			    Integer newId = newParuse.getUseId();
			    sessionExpDB.evict(paruse);
			    metaAss.insertCoupleParuse(oldId, newId);
			}
		} finally {}
	}
	
	/**
	 * Importa exported paruse details
	 * @throws EMFUserError
	 */
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
				if(existObj==null) {
					importer.insertObject(parusedet, sessionCurrDB);
					metaLog.log("Inserted new association between paruse " + 
					     	      parusedet.getId().getSbiParuse().getName() + 
						        " and role " +  parusedet.getId().getSbiExtRoles().getName());
				}
			}
		} finally {}
	}
	
	/**
	 * Imports associations between parameter uses and checks
	 * @throws EMFUserError
	 */
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
				if(existObj==null) {
					importer.insertObject(paruseck, sessionCurrDB);
					metaLog.log("Inserted new association between paruse " + 
				     	      paruseck.getId().getSbiParuse().getName() + 
					        " and check " +  paruseck.getId().getSbiChecks().getName());
				}
			}
		} finally {}
	}
	
	
	
	
	/**
	 * Imports biobject links
	 * @throws EMFUserError
	 */
	private void importBIObjectLinks() throws EMFUserError {
		try{
			List exportedBIObjectsLinks = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiSubreports");
			Iterator iterSbiObjLinks = exportedBIObjectsLinks.iterator();
			while(iterSbiObjLinks.hasNext()){
				SbiSubreports objlink = (SbiSubreports)iterSbiObjLinks.next();
//				// get ids of master and subreport
//				Integer masterid = objlink.getMaster_rpt_id();
//				Integer subid = objlink.getSub_rpt_id();
////				 get biobjects
//				SbiObjects masterBIObj = importer.getExportedSbiObject(masterid, txExpDB, sessionExpDB);
//				SbiObjects subBIObj = importer.getExportedSbiObject(subid, txExpDB, sessionExpDB);
				
				//get biobjects
				SbiObjects masterBIObj = objlink.getId().getMasterReport();
				SbiObjects subBIObj = objlink.getId().getSubReport();
				Integer masterid = masterBIObj.getBiobjId();
				Integer subid = subBIObj.getBiobjId();
				// get association of object
				Map biobjIdAss = metaAss.getBIobjIDAssociation();
				// try to get from association the id associate to the exported metadata
				Integer newMasterId = (Integer)biobjIdAss.get(masterid);
				Integer newSubId = (Integer)biobjIdAss.get(subid);
				if (newMasterId!=null) {
					masterid = newMasterId;
				}
				if (newSubId!=null) {
					subid = newSubId;
				}
				// build a new id for the SbiSubreport
				SbiSubreportsId sbiSubReportsId = objlink.getId();
				if (sbiSubReportsId != null) {
					SbiObjects master = sbiSubReportsId.getMasterReport();
					SbiObjects sub = sbiSubReportsId.getMasterReport();
					SbiObjects newMaster = ImportUtilities.makeNewSbiObject(master, newMasterId);
					SbiObjects newSub = ImportUtilities.makeNewSbiObject(sub, newSubId);
					sbiSubReportsId.setMasterReport(newMaster);
					sbiSubReportsId.setSubReport(newSub);
				}
				objlink.setId(sbiSubReportsId);
				// check if the association between metadata already exist
				Map unique = new HashMap();
				unique.put("masterid", masterid);
				unique.put("subid", subid);
				Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiSubreports());
				if (existObj==null) {
					importer.insertObject(objlink, sessionCurrDB);
					metaLog.log("Inserted new link between master object " + 
							     masterBIObj.getLabel() + 
					            " and sub object " + subBIObj.getLabel());
				}
			}
		} finally {}
	}
	
	
	
	/**
	 * Imports associations between functionalities and objects
	 * @throws EMFUserError
	 */
	private void importFunctObject() throws EMFUserError {
		try{
			List exportedFunctObjects = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiObjFunc");
			Iterator iterSbiFunctObjects = exportedFunctObjects.iterator();
			while(iterSbiFunctObjects.hasNext()){
				SbiObjFunc objfunct = (SbiObjFunc)iterSbiFunctObjects.next();
				// get ids of exported role, function and state associzted
				Integer functid = objfunct.getId().getSbiFunctions().getFunctId();
				Integer objid = objfunct.getId().getSbiObjects().getBiobjId();
				Integer prog = objfunct.getProg();
				// get association of roles and paruses
				Map functIdAss = metaAss.getFunctIDAssociation();
				Map biobjIdAss = metaAss.getBIobjIDAssociation();
				// try to get from association the id associate to the exported metadata
				Integer newFunctid = (Integer)functIdAss.get(functid);
				Integer newObjectid = (Integer)biobjIdAss.get(objid);
				// build a new id for the SbiObjFunct
				SbiObjFuncId objfunctid = objfunct.getId();
				if(objfunctid!=null) {
					SbiFunctions sbifunct = objfunctid.getSbiFunctions();
					SbiFunctions newFunct = ImportUtilities.makeNewSbiFunction(sbifunct, newFunctid);
					objfunctid.setSbiFunctions(newFunct);
					functid = newFunctid;
				}
				if(newObjectid!=null){
					SbiObjects sbiobj = objfunctid.getSbiObjects();
					SbiObjects newObj = ImportUtilities.makeNewSbiObject(sbiobj, newObjectid);
					objfunctid.setSbiObjects(newObj);
					objid=newObjectid;
				}
				objfunct.setId(objfunctid);
				// check if the association between metadata already exist
				Map unique = new HashMap();
				unique.put("objectid", objid);
				unique.put("functionid", functid);
				Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiObjFunc());
				if(existObj==null) {
					importer.insertObject(objfunct, sessionCurrDB);
					metaLog.log("Inserted new association between function " + 
							objfunct.getId().getSbiFunctions().getName() + 
					            " and object " + objfunct.getId().getSbiObjects().getName());
				}
			}
		} finally {}
	}
	
	
	
	/**
	 * Imports associations between functionalities and roles
	 * @throws EMFUserError
	 */
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
				// get sbidomain of the current system
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
				unique.put("stateid", existDom.getValueId());
				unique.put("roleid", roleid);
				unique.put("functionid", functid);
				Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiFuncRole());
				if(existObj==null) {
					importer.insertObject(functrole, sessionCurrDB);
					metaLog.log("Inserted new association between function " + 
							    functrole.getId().getFunction().getName() + 
					            " and role " + functrole.getId().getRole().getName());
				}
			}
		} finally {}
	}
	
	
	
	
	
	/**
	 * Imports associations between exported biobjects and parameters
	 * @throws EMFUserError
	 */
	private void importBIObjPar() throws EMFUserError {
		try{
			List exportedObjPars = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiObjPar");
			Iterator iterSbiObjPar = exportedObjPars.iterator();
			while(iterSbiObjPar.hasNext()){
				SbiObjPar objpar = (SbiObjPar)iterSbiObjPar.next();
				SbiParameters param = objpar.getSbiParameter();
				SbiObjects biobj = objpar.getSbiObject();
				Integer oldParamId = param.getParId();
				Integer oldBIObjId = biobj.getBiobjId();
				Map assBIObj = metaAss.getBIobjIDAssociation();
				Map assParams = metaAss.getParameterIDAssociation();
				Integer newParamId = (Integer)assParams.get(oldParamId);
				Integer newBIObjId = (Integer)assBIObj.get(oldBIObjId);
				if(newParamId!=null) {
					SbiParameters newParam = ImportUtilities.makeNewSbiParameter(param, newParamId);
					objpar.setSbiParameter(newParam);
				}
				if(newBIObjId!=null){
					SbiObjects newObj = ImportUtilities.makeNewSbiObject(biobj, newBIObjId);
					objpar.setSbiObject(newObj);
				}
				Integer oldId = objpar.getObjParId();
				
				// check if the association already exist
			    Map uniqueMap = new HashMap();
				uniqueMap.put("biobjid", newBIObjId);
				uniqueMap.put("paramid", newParamId);
				uniqueMap.put("urlname", objpar.getParurlNm());
				Object existObj = importer.checkExistence(uniqueMap, sessionCurrDB, new SbiObjPar());
				if(existObj!=null) {
					metaLog.log("Exported association between object "+objpar.getSbiObject().getName()+" " +
						    	" and parameter "+objpar.getSbiParameter().getName()+
						    	" with url name "+objpar.getParurlNm()+" not inserted" +
								" because already existing into the current database");
					continue;
				}
				/*	
				Map objparIdAss = metaAss.getObjparIDAssociation();
				Set objparIdAssSet = objparIdAss.keySet();	
			    if(objparIdAssSet.contains(oldId)){
					metaLog.log("Exported association between object "+objpar.getSbiObject().getName()+" " +
							    " and parameter "+objpar.getSbiParameter().getName()+
							    " with url name "+objpar.getParurlNm()+" not inserted" +
								" because already existing into the current database");
					continue;
				}
			    */
				SbiObjPar newObjpar = ImportUtilities.makeNewSbiObjpar(objpar);
			    importer.insertObject(newObjpar, sessionCurrDB);
			    metaLog.log("Inserted new biobject parameter with " + newObjpar.getParurlNm() +
			    		    " for biobject " + newObjpar.getSbiObject().getName());
			    Integer newId = newObjpar.getObjParId();
			    sessionExpDB.evict(objpar);
			    metaAss.insertCoupleObjpar(oldId, newId);
			}
		} finally {}
	}
	
	
	
	/**
	 * Imports biparameter dependencies
	 * @throws EMFUserError
	 */
	private void importObjParUse() throws EMFUserError {
		try{
			List exportedParDepends = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiObjParuse");
			Iterator iterParDep = exportedParDepends.iterator();
			while(iterParDep.hasNext()){
				SbiObjParuse pardep = (SbiObjParuse)iterParDep.next();
				// get ids of objpar and paruse associated
				Integer objparId = pardep.getId().getSbiObjPar().getObjParId();
				Integer paruseId = pardep.getId().getSbiParuse().getUseId();
				Integer objparfathId = pardep.getId().getSbiObjParFather().getObjParId();
				String filterOp = pardep.getId().getFilterOperation();
				// get association of objpar and paruses
				Map objparIdAss = metaAss.getObjparIDAssociation();
				Map paruseIdAss = metaAss.getParuseIDAssociation();
				// try to get from association the id associate to the exported metadata
				Integer newObjparId = (Integer)objparIdAss.get(objparId);
				Integer newParuseId = (Integer)paruseIdAss.get(paruseId);
				Integer newObjParFathId = (Integer)objparIdAss.get(objparfathId);
				// build a new id for the SbiObjParuse
				SbiObjParuseId objparuseid = pardep.getId();
				objparuseid.setFilterOperation(filterOp);
				if(newParuseId!=null) {
					SbiParuse sbiparuse = objparuseid.getSbiParuse();
					SbiParuse newParuse = ImportUtilities.makeNewSbiParuse(sbiparuse, newParuseId);
					objparuseid.setSbiParuse(newParuse);
					paruseId = newParuseId;
				}
				if(newObjparId!=null){
					SbiObjPar sbiobjpar = objparuseid.getSbiObjPar();
					SbiObjPar newObjPar = ImportUtilities.makeNewSbiObjpar(sbiobjpar, newObjparId);
					objparuseid.setSbiObjPar(newObjPar);
					objparId=newObjparId;
				}
				if(newObjParFathId!=null){
					SbiObjPar sbiobjparfath = objparuseid.getSbiObjParFather();
					SbiObjPar newObjParFath = ImportUtilities.makeNewSbiObjpar(sbiobjparfath, newObjParFathId);
					objparuseid.setSbiObjParFather(newObjParFath);
					objparfathId=newObjParFathId;
				}
				
				pardep.setId(objparuseid);
				// change reference to the parent parameter
				//SbiObjPar parParent = pardep.getSbiObjParFather();
				//Integer parParentId = parParent.getObjParId(); 
				//Integer newparParentId = (Integer)objparIdAss.get(parParentId);
				//SbiObjPar newparParent = ImportUtilities.makeNewSbiObjpar(parParent, newparParentId);
				//pardep.setSbiObjParFather(newparParent);
				// check if the association between metadata already exist
				Map unique = new HashMap();
				unique.put("objparid", objparId);
				unique.put("paruseid", paruseId);
				unique.put("objparfathid", objparfathId);
				unique.put("filterop", filterOp);
				Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiObjParuse());
				if(existObj==null) {
					importer.insertObject(pardep, sessionCurrDB);
					metaLog.log("Inserted new dependecies between biparameter " +  pardep.getId().getSbiObjPar().getLabel() +
					            " of the biobject " + pardep.getId().getSbiObjPar().getSbiObject().getLabel() + 
					            " and paruse " + pardep.getId().getSbiParuse().getLabel());
				}
			}
		} finally {}
	}
	
	
	
	
	
	
	/**
	 * Ends the import procedure
	 */
	public void stopImport() {
		metaAss.clear();
		rollback();
		ImpExpGeneralUtilities.deleteDir(new File(pathBaseFolder));
	}

	
	/**
	 * Gets the list of exported connections
	 * @return List of the exported connections
	 */
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
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "getExportedConnections", 
			"Error while reading connections file " + fnfe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		} catch (SourceBeanException sbe) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "getExportedConnections", 
			"Error while reading connections file " + sbe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		}catch (IOException ioe) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "getExportedConnections", 
			"Error while reading connections file " + ioe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		} finally {}
		return connections;
	}



    /**
     * Check the existance of the exported metadata into the current system metadata
     * and insert their associations into the association object MeatadataAssociation
     */
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
				metaLog.log("Found an existing Parameter "+paramCurr.getName() +" with " +
						    "the same label of the exported parameter " + paramExp.getName());
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
				metaLog.log("Found an existing Role "+roleCurr.getName() +" with " +
					        "the same name of the exported role " + roleExp.getName());
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
				metaLog.log("Found an existing Parameter use "+paruseCurr.getName() +" with " +
					        "the same label of the exported parameter use " + paruseExp.getName());
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
				metaLog.log("Found an existing BIObject "+objCurr.getName() +" with " +
					        "the same label and path of the exported BIObject " + objExp.getName());
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
				metaLog.log("Found an existing Lov "+lovCurr.getName() +" with " +
					    "the same label of the exported lov " + lovExp.getName());
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
				metaLog.log("Found an existing Functionality "+functCurr.getName() +" with " +
					    "the same label and path of the exported functionality " + functExp.getName());
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
				metaLog.log("Found an existing Engine "+engCurr.getName() +" with " +
					    "the same label of the exported engine " + engExp.getName());
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
				metaLog.log("Found an existing check "+checkCurr.getName() +" with " +
					    "the same label of the exported check " + checkExp.getName());
			}
		}
		List exportedObjPar = importer.getAllExportedSbiObjects(txExpDB, sessionExpDB, "SbiObjPar");
		Iterator iterSbiObjPar = exportedObjPar.iterator();
		while(iterSbiObjPar.hasNext()){
			SbiObjPar objparExp = (SbiObjPar)iterSbiObjPar.next();
			String urlName = objparExp.getParurlNm();
			
			Integer objid = objparExp.getSbiObject().getBiobjId();
			Map objIdAss = metaAss.getBIobjIDAssociation();
			Integer newObjid = (Integer)objIdAss.get(objid);
			if(newObjid!=null)
				objid = newObjid;
			
			Integer parid = objparExp.getSbiParameter().getParId();
			Map parIdAss = metaAss.getParameterIDAssociation();
			Integer newParid = (Integer)parIdAss.get(parid);
			if(newParid!=null)
				parid = newParid;
			
			
			Map uniqueMap = new HashMap();
			uniqueMap.put("biobjid", objid);
			uniqueMap.put("paramid", parid);
			uniqueMap.put("urlname", urlName);
			Object existObj = importer.checkExistence(uniqueMap, sessionCurrDB, new SbiObjPar());
			
			if(existObj!=null) {
				SbiObjPar objParCurr = (SbiObjPar)existObj;
				metaAss.insertCoupleObjpar(objparExp.getObjParId(), objParCurr.getObjParId());
				metaAss.insertCoupleObjpar(objparExp, objParCurr);
				metaLog.log("Found an existing association between object "+objparExp.getSbiObject().getName() +
						    " and parameter " +objparExp.getSbiParameter().getName() + " with " +
					        " the same url "+ objparExp.getParurlNm() +" name of the exported objpar ");
			}
		}
	}



    /**
     * Gets the object which contains the association between exported metadata 
     * and the current system metadata
     * @return MetadataAssociation the object which contains the association 
     * between exported metadata and the current system metadata
     */
	public MetadataAssociations getMetadataAssociation() {
		return metaAss;
	}
	
	
	

	
}
