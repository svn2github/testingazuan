/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.importexport;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjFunc;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjFuncId;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjPar;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjTemplates;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSnapshots;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSubObjects;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSubreports;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSubreportsId;
import it.eng.spagobi.analiticalmodel.functionalitytree.metadata.SbiFuncRole;
import it.eng.spagobi.analiticalmodel.functionalitytree.metadata.SbiFunctions;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiObjParuse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiObjParuseId;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParameters;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuseCk;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuseCkId;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuseDet;
import it.eng.spagobi.behaviouralmodel.check.metadata.SbiChecks;
import it.eng.spagobi.behaviouralmodel.lov.metadata.SbiLov;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IRoleDAO;
import it.eng.spagobi.commons.metadata.SbiBinContents;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.commons.utilities.FileUtilities;
import it.eng.spagobi.commons.utilities.HibernateUtil;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.engines.config.dao.IEngineDAO;
import it.eng.spagobi.engines.config.metadata.SbiEngines;
import it.eng.spagobi.mapcatalogue.metadata.SbiGeoFeatures;
import it.eng.spagobi.mapcatalogue.metadata.SbiGeoMapFeatures;
import it.eng.spagobi.mapcatalogue.metadata.SbiGeoMapFeaturesId;
import it.eng.spagobi.mapcatalogue.metadata.SbiGeoMaps;
import it.eng.spagobi.tools.dataset.metadata.SbiDataSetConfig;
import it.eng.spagobi.tools.datasource.bo.DataSource;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.tools.datasource.dao.IDataSourceDAO;
import it.eng.spagobi.tools.datasource.metadata.SbiDataSource;
import it.eng.spagobi.tools.importexport.bo.AssociationFile;
import it.eng.spagobi.tools.importexport.transformers.TransformersUtilities;

import java.io.File;
import java.io.FileInputStream;
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

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Implements the interface which defines methods for managing the import
 * requests
 */
public class ImportManager implements IImportManager, Serializable {

    static private Logger logger = Logger.getLogger(ImportManager.class);

    private String pathImportTmpFolder = "";
    private String pathBaseFolder = "";
    private String pathDBFolder = "";
    private ImporterMetadata importer = null;
    private Properties props = null;
    private SessionFactory sessionFactoryExpDB = null;
    private Session sessionExpDB = null;
    private Transaction txExpDB = null;
    private Session sessionCurrDB = null;
    private Transaction txCurrDB = null;
    private MetadataAssociations metaAss = null;
    private MetadataLogger metaLog = null;
    private UserAssociationsKeeper usrAss = null;
    private String exportedFileName = "";
    private AssociationFile associationFile = null;
    private String impAssMode = IMPORT_ASS_DEFAULT_MODE;

    
    /**
     * Prepare the environment for the import procedure.
     * 
     * @param pathImpTmpFold The path of the temporary import folder
     * @param archiveName the name of the compress exported file
     * @param archiveContent the bytes of the compress exported file
     * 
     * @throws EMFUserError the EMF user error
     */
    public void init(String pathImpTmpFold, String archiveName, byte[] archiveContent) throws EMFUserError {
	logger.debug("IN");
	// create directories of the tmp import folder
	File impTmpFold = new File(pathImpTmpFold);
	impTmpFold.mkdirs();
	// write content uploaded into a tmp archive
	String pathArchiveFile = pathImpTmpFold + "/" + archiveName;
	File archive = new File(pathArchiveFile);
	exportedFileName = archiveName.substring(0, archiveName.indexOf(".zip"));
	FileOutputStream fos = null;
	try {
		fos = new FileOutputStream(archive);
	    fos.write(archiveContent);
	    fos.flush();
	} catch (Exception ioe) {
	    logger.error("Error while writing archive content into a tmp file ", ioe);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
				logger.error("Error while closing stream", e);
			}
		}
	}
	// decompress archive
	ImportUtilities.decompressArchive(pathImpTmpFold, pathArchiveFile);
	// erase archive file
	archive.delete();
	int lastDotPos = archiveName.lastIndexOf(".");
	if (lastDotPos != -1)
	    archiveName = archiveName.substring(0, lastDotPos);
	pathImportTmpFolder = pathImpTmpFold;
	pathBaseFolder = pathImportTmpFolder + "/" + archiveName;
	pathDBFolder = pathBaseFolder + "/metadata";
	String propFilePath = pathBaseFolder + "/export.properties";
	FileInputStream fis = null;
	try {
	    fis = new FileInputStream(propFilePath);
	    props = new Properties();
	    props.load(fis);
	} catch (Exception e) {
	    logger.error("Error while reading properties file ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
		if (fis != null)
			try {
				fis.close();
			} catch (IOException e) {
				logger.error("Error while closing stream", e);
			}
	}
	importer = new ImporterMetadata();
	sessionFactoryExpDB = ImportUtilities.getHibSessionExportDB(pathDBFolder);
	metaAss = new MetadataAssociations();
	metaLog = new MetadataLogger();
	usrAss = new UserAssociationsKeeper();
	logger.debug("OUT");
    }

    public void openSession() throws EMFUserError {
    	logger.debug("IN");
    	try  {
	    	sessionExpDB = sessionFactoryExpDB.openSession();
	    	txExpDB = sessionExpDB.beginTransaction();
	    	sessionCurrDB = HibernateUtil.currentSession();
	    	txCurrDB = sessionCurrDB.beginTransaction();
    	} catch (Exception e) {
    		logger.error("Error while opening session. May be the import manager was not correctly initialized.", e);
    		throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
    	}
    	logger.debug("OUT");
    }

    public void closeSession() {
    	logger.debug("IN");
    	if (txExpDB != null && txExpDB.isActive()) {
    		txExpDB.rollback();
    	}
    	if (sessionExpDB != null) {
    	    if (sessionExpDB.isOpen()) {
    	    	sessionExpDB.close();
    	    }
    	}
    	if (txCurrDB != null && txCurrDB.isActive()) {
    		txCurrDB.commit();
    	}
    	if (sessionCurrDB != null) {
    	    if (sessionCurrDB.isOpen()) {
    	    	sessionCurrDB.close();
    	    }
    	}
    	logger.debug("OUT");
    }
    
    /**
     * Imports the exported objects.
     * 
     * @param overwrite the overwrite
     * 
     * @throws EMFUserError the EMF user error
     */
    public void importObjects(boolean overwrite) throws EMFUserError {
	logger.debug("IN");
	updateDataSourceReferences(metaAss.getDataSourceIDAssociation());
	importDataSource(overwrite);
	importDataSet(overwrite);
	importRoles();
	importEngines();
	importFunctionalities();
	importChecks();
	importParameters(overwrite);
	importLovs(overwrite);
	importParuse();
	importParuseDet();
	importParuseCheck();
	importFunctRoles();
	importBIObjects(overwrite);
	importBIObjectLinks();
	importMapCatalogue(overwrite);
//	importResources(overwrite);
	logger.debug("OUT");
    }


	/**
     * Gets the SpagoBI version of the exported file.
     * 
     * @return The SpagoBI version of the exported file
     */
    public String getExportVersion() {
	return props.getProperty("spagobi-version");
    }

    /**
     * Gets the current SpagobI version.
     * 
     * @return The current SpagoBI version
     */
    public String getCurrentVersion() {
	logger.debug("IN");
	ConfigSingleton conf = ConfigSingleton.getInstance();
	SourceBean curVerSB = (SourceBean) conf.getAttribute("IMPORTEXPORT.CURRENTVERSION");
	String curVer = (String) curVerSB.getAttribute("version");
	logger.debug("OUT");
	return curVer;
    }

    /**
     * Gets the list of all exported roles.
     * 
     * @return The list of exported roles
     * 
     * @throws EMFUserError the EMF user error
     */
    public List getExportedRoles() throws EMFUserError {
	List exportedRoles = null;
	exportedRoles = importer.getAllExportedRoles(sessionExpDB);
	return exportedRoles;
    }

    /**
     * Gets the list of all exported engines.
     * 
     * @return The list of exported engines
     * 
     * @throws EMFUserError the EMF user error
     */
    public List getExportedEngines() throws EMFUserError {
	List exportedEngines = null;
	exportedEngines = importer.getAllExportedEngines(sessionExpDB);
	return exportedEngines;
    }

    /**
     * checks if two or more exported roles are associate to the same current
     * role.
     * 
     * @param roleAssociations Map of assocaition between exported roles and roles of the
     * portal in use
     * 
     * @throws EMFUserError if two ore more exported roles are associate to the same
     * current role
     */
    public void checkRoleReferences(Map roleAssociations) throws EMFUserError {
	logger.debug("IN");
	// each exported role should be associate only to one system role
	Set rolesAssKeys = roleAssociations.keySet();
	Iterator iterRoleAssKeys1 = rolesAssKeys.iterator();
	while (iterRoleAssKeys1.hasNext()) {
	    Integer roleExpId = (Integer) iterRoleAssKeys1.next();
	    Integer roleAssId = (Integer) roleAssociations.get(roleExpId);
	    Iterator iterRoleAssKeys2 = rolesAssKeys.iterator();
	    while (iterRoleAssKeys2.hasNext()) {
		Integer otherRoleExpId = (Integer) iterRoleAssKeys2.next();
		if (otherRoleExpId.compareTo(roleExpId) != 0) {
		    Integer otherRoleAssId = (Integer) roleAssociations.get(otherRoleExpId);
		    if (otherRoleAssId.compareTo(roleAssId) == 0) {
			logger.debug("OUT. The checkRoleReferences method doesn't work ");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8001", "component_impexp_messages");
		    }
		}
	    }
	}
	logger.debug("OUT");
    }

    /**
     * Update the data source name for each list of values of type query based on
     * association between exported data sources and current system data sources.
     * 
     * @param mapDataSources Map of the associations between exported data sources and
     * current system data sources
     * 
     * @throws EMFUserError the EMF user error
     */
    public void updateDataSourceReferences(Map mapDataSources) throws EMFUserError {
	/*
	 * The key of the map are the name of the exported data sources Each key
	 * value is the name of the current system data source associate
	 */
	importer.updateDSRefs(mapDataSources, sessionExpDB, metaLog);
    }

    /**
     * Closes Hibernate session factory for the exported database
     */
    private void closeSessionFactory() {
	if (sessionFactoryExpDB != null) {
	    sessionFactoryExpDB.close();
	}
    }

    /**
     * Rollbacks each previous changes made on exported and current databases
     */
    private void rollback() {
	if (txExpDB != null && txExpDB.isActive())
	    txExpDB.rollback();
	if (txCurrDB != null && txCurrDB.isActive())
	    txCurrDB.rollback();
	closeSession();
	closeSessionFactory();
    }

    /**
     * Commits changes done till now and open a new transaction
     */
    private void commit() {
    	if (txCurrDB != null && txCurrDB.isActive()) {
    		txCurrDB.commit();
    		txCurrDB = sessionCurrDB.beginTransaction();
    	}
    }
    
    /**
     * Commits all changes made on exported and current databases.
     * 
     * @return the import result info
     * 
     * @throws EMFUserError the EMF user error
     */
    public ImportResultInfo commitAllChanges() throws EMFUserError {
	logger.debug("IN");
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
	String folderImportOutcomeName = "import" + now.getTime();
	String pathFolderImportOutcome = pathImportTmpFolder + "/" + folderImportOutcomeName;
	File fileFolderImportOutcome = new File(pathFolderImportOutcome);
	fileFolderImportOutcome.mkdirs();
	// fill the result bean with eventual manual task info
	String pathManualTaskFolder = pathBaseFolder + "/" + ImportExportConstants.MANUALTASK_FOLDER_NAME;
	File fileManualTaskFolder = new File(pathManualTaskFolder);
	if (fileManualTaskFolder.exists()) {
	    String[] manualTaskFolders = fileManualTaskFolder.list();
	    Map manualTaskMap = new HashMap();
	    String nameTask = "";
	    for (int i = 0; i < manualTaskFolders.length; i++) {
		try {
		    String pathMTFolder = pathManualTaskFolder + "/" + manualTaskFolders[i];
		    File fileMTFolder = new File(pathMTFolder);
		    if (!fileMTFolder.isDirectory())
			continue;
		    String pathFilePropMT = pathManualTaskFolder + "/" + manualTaskFolders[i] + ".properties";
		    File filePropMT = new File(pathFilePropMT);
		    FileInputStream fisProp = new FileInputStream(filePropMT);
		    Properties props = new Properties();
		    props.load(fisProp);
		    nameTask = props.getProperty("name");
		    fisProp.close();
		    // copy the properties
		    FileOutputStream fosProp = new FileOutputStream(pathFolderImportOutcome + "/"
			    + manualTaskFolders[i] + ".properties");
		    props.store(fosProp, "");
		    // GeneralUtilities.flushFromInputStreamToOutputStream(fisProp,
		    // fosProp, true);
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
		    logger.error("Error while generatin zip file for manual task " + nameTask, e);
		}
	    }
	    iri.setManualTasks(manualTaskMap);
	}
	// delete the tmp directory of the current import operation
	FileUtilities.deleteDir(new File(pathBaseFolder));
	// generate the log file
	File logFile = new File(pathFolderImportOutcome + "/" + exportedFileName + ".log");
	if (logFile.exists())
	    logFile.delete();
	try {
	    FileOutputStream fos = new FileOutputStream(logFile);
	    fos.write(metaLog.getLogBytes());
	    fos.flush();
	    fos.close();
	} catch (Exception e) {
	    logger.error("Error while writing log file ", e);
	}
	// generate the association file
	File assFile = new File(pathFolderImportOutcome + "/" + exportedFileName + ".xml");
	if (assFile.exists())
	    assFile.delete();
	try {
	    FileOutputStream fos = new FileOutputStream(assFile);
	    fos.write(usrAss.toXml().getBytes());
	    fos.flush();
	    fos.close();
	} catch (Exception e) {
	    logger.error("Error while writing the associations file ", e);
	}
	iri.setFolderName(folderImportOutcomeName);
	// set into the result bean the log file path
	iri.setLogFileName(exportedFileName);
	// set into the result bean the associations file path
	iri.setAssociationsFileName(exportedFileName);
	// return the result info bean
	logger.debug("OUT");
	return iri;
    }


    /**
     * Import exported roles
     * 
     * @throws EMFUserError
     */
    private void importRoles() throws EMFUserError {
	logger.debug("IN");
	SbiExtRoles role = null;
	try {
	    List exportedRoles = importer.getAllExportedSbiObjects(sessionExpDB, "SbiExtRoles", null);
	    Iterator iterSbiRoles = exportedRoles.iterator();
	    while (iterSbiRoles.hasNext()) {
			role = (SbiExtRoles) iterSbiRoles.next();
			Integer oldId = role.getExtRoleId();
			Map roleIdAss = metaAss.getRoleIDAssociation();
			Set roleIdAssSet = roleIdAss.keySet();
			if (roleIdAssSet.contains(oldId)) {
			    metaLog.log("Exported role " + role.getName() + " not inserted"
				    + " because it has been associated to an existing role or it has the same name "
				    + " of an existing role");
			    continue;
			}
			SbiExtRoles newRole = ImportUtilities.makeNewSbiExtRole(role);
			String roleCd = role.getRoleTypeCode();
			Map unique = new HashMap();
			unique.put("valuecd", roleCd);
			unique.put("domaincd", "ROLE_TYPE");
			SbiDomains existDom = (SbiDomains) importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
			if (existDom != null) {
			    newRole.setRoleType(existDom);
			    newRole.setRoleTypeCode(existDom.getValueCd());
			}
			sessionCurrDB.save(newRole);
			metaLog.log("Inserted new role " + newRole.getName());
			Integer newId = newRole.getExtRoleId();
			metaAss.insertCoupleRole(oldId, newId);
	    }
	} catch (Exception e) {
		if (role != null) {
			logger.error("Error while importing exported role with name [" + role.getName() + "].", e);
		}
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Imports exported engines
     * 
     * @throws EMFUserError
     */
    private void importEngines() throws EMFUserError {
	logger.debug("IN");
	SbiEngines engine = null;
	try {
	    List exportedEngines = importer.getAllExportedSbiObjects(sessionExpDB, "SbiEngines", null);
	    Iterator iterSbiEngines = exportedEngines.iterator();
	    while (iterSbiEngines.hasNext()) {
			engine = (SbiEngines) iterSbiEngines.next();
			Integer oldId = engine.getEngineId();
			Map engIdAss = metaAss.getEngineIDAssociation();
			Set engIdAssSet = engIdAss.keySet();
			if (engIdAssSet.contains(oldId)) {
			    metaLog.log("Exported engine " + engine.getName() + " not inserted"
				    + " because it has been associated to an existing engine or it has the same label "
				    + " of an existing engine");
			    continue;
			}
			SbiEngines newEng = ImportUtilities.makeNewSbiEngine(engine);
			SbiDomains engineTypeDomain = engine.getEngineType();
			Map uniqueEngineType = new HashMap();
			uniqueEngineType.put("valuecd", engineTypeDomain.getValueCd());
			uniqueEngineType.put("domaincd", "ENGINE_TYPE");
			SbiDomains existEngineTypeDomain = (SbiDomains) importer.checkExistence(uniqueEngineType,
				sessionCurrDB, new SbiDomains());
			if (existEngineTypeDomain != null) {
			    newEng.setEngineType(existEngineTypeDomain);
			}
			SbiDomains biobjectTypeDomain = engine.getBiobjType();
			Map uniqueBiobjectType = new HashMap();
			uniqueBiobjectType.put("valuecd", biobjectTypeDomain.getValueCd());
			uniqueBiobjectType.put("domaincd", "BIOBJ_TYPE");
			SbiDomains existBiobjectTypeDomain = (SbiDomains) importer.checkExistence(uniqueBiobjectType,
				sessionCurrDB, new SbiDomains());
			if (existBiobjectTypeDomain != null) {
			    newEng.setBiobjType(existBiobjectTypeDomain);
			}
			// check datasource link
			SbiDataSource expDs = engine.getDataSource();
			if (expDs != null) {
				Integer dsId = (Integer) metaAss.getDataSourceIDAssociation().get(new Integer(expDs.getDsId()));
				SbiDataSource localDS = (SbiDataSource) sessionCurrDB.load(SbiDataSource.class, dsId);
				newEng.setDataSource(localDS);
			}
			sessionCurrDB.save(newEng);
			metaLog.log("Inserted new engine " + engine.getName());
			Integer newId = newEng.getEngineId();
			metaAss.insertCoupleEngine(oldId, newId);
	    }
	} catch (Exception e) {
		if (engine != null) {
			logger.error("Error while importing exported engine with label [" + engine.getLabel() + "].", e);
		}
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    private void importDataSource(boolean overwrite) throws EMFUserError {
	logger.debug("IN");
	SbiDataSource dataSource = null;
	try {
	    List exportedDS = importer.getAllExportedSbiObjects(sessionExpDB, "SbiDataSource", null);
	    Iterator iterSbiDataSource = exportedDS.iterator();

	    while (iterSbiDataSource.hasNext()) {
			dataSource = (SbiDataSource) iterSbiDataSource.next();
			Integer oldId = new Integer(dataSource.getDsId());
			Integer existingDatasourceId = null;
			Map dsIdAss = metaAss.getDataSourceIDAssociation();
			Set engIdAssSet = dsIdAss.keySet();
			if (engIdAssSet.contains(oldId) && !overwrite) {
			    metaLog.log("Exported dataSource " + dataSource.getLabel() + " not inserted"
				    + " because exist dataSource with the same label ");
			    continue;
			} else {
				existingDatasourceId = (Integer) dsIdAss.get(oldId);
			}
			
			if (existingDatasourceId != null) {
				logger.info("The data source with label:[" + dataSource.getLabel() + "] is just present. It will be updated.");
			    metaLog.log("The data source with label = [" + dataSource.getLabel() + "] will be updated.");
			    SbiDataSource existingDs = ImportUtilities.modifyExistingSbiDataSource(dataSource, sessionCurrDB, existingDatasourceId);
			    ImportUtilities.associateWithExistingEntities(existingDs, dataSource, sessionCurrDB, importer, metaAss);
			    sessionCurrDB.update(existingDs);
			} else {
				SbiDataSource newDS = ImportUtilities.makeNewSbiDataSource(dataSource);
				ImportUtilities.associateWithExistingEntities(newDS, dataSource, sessionCurrDB, importer, metaAss);
				Integer newId = (Integer) sessionCurrDB.save(newDS);
				metaLog.log("Inserted new datasource " + newDS.getLabel());
				metaAss.insertCoupleDataSources(oldId, newId);
			}
	    }
	} catch (Exception e) {
		if (dataSource != null) {
			logger.error("Error while importing exported datasource with label [" + dataSource.getLabel() + "].", e);
		}
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    private void importDataSet(boolean overwrite) throws EMFUserError {
    	logger.debug("IN");
    	SbiDataSetConfig exportedDataSet = null;
		try {
		    List exportedDatasets = importer.getAllExportedSbiObjects(sessionExpDB, "SbiDataSetConfig", null);
		    Iterator iterSbiDataSet = exportedDatasets.iterator();

		    while (iterSbiDataSet.hasNext()) {
				exportedDataSet = (SbiDataSetConfig) iterSbiDataSet.next();
				Integer oldId = new Integer(exportedDataSet.getDsId());
				Integer existingDatasetId = null;
				Map datasetAss = metaAss.getDataSetIDAssociation();
				Set datasetAssSet = datasetAss.keySet();
				if (datasetAssSet.contains(oldId) && !overwrite) {
				    metaLog.log("Exported dataset " + exportedDataSet.getLabel() + " not inserted"
					    + " because exist dataset with the same label ");
				    continue;
				} else {
					existingDatasetId = (Integer) datasetAss.get(oldId);
				}
				if (existingDatasetId != null) {
					logger.info("The dataset with label:[" + exportedDataSet.getLabel() + "] is just present. It will be updated.");
				    metaLog.log("The dataset with label = [" + exportedDataSet.getLabel() + "] will be updated.");
					SbiDataSetConfig existingDataset = ImportUtilities.modifyExistingSbiDataSet(exportedDataSet, sessionCurrDB, existingDatasetId);
				    ImportUtilities.associateWithExistingEntities(existingDataset, exportedDataSet, sessionCurrDB, importer, metaAss);
				    sessionCurrDB.update(existingDataset);
				} else {
					SbiDataSetConfig newDataset = ImportUtilities.makeNewSbiDataSet(exportedDataSet);
					ImportUtilities.associateWithExistingEntities(newDataset, exportedDataSet, sessionCurrDB, importer, metaAss);
					sessionCurrDB.save(newDataset);
					metaLog.log("Inserted new dataset " + newDataset.getName());
					Integer newId = new Integer(newDataset.getDsId());
					metaAss.insertCoupleDataSets(oldId, newId);
				}
		    }
		} catch (Exception e) {
			if (exportedDataSet != null) {
				logger.error("Error while importing exported dataset with label [" + exportedDataSet.getLabel() + "].", e);
			}
		    logger.error("Error while inserting object ", e);
		    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		} finally {
		    logger.debug("OUT");
		}
    }
    
    /**
     * Imports exported functionalities
     * 
     * @throws EMFUserError
     */
    private void importFunctionalities() throws EMFUserError {
	logger.debug("IN");
	SbiFunctions functToInsert = null;
	try {
	    List exportedFuncts = importer.getAllExportedSbiObjects(sessionExpDB, "SbiFunctions", null);

	    while (exportedFuncts.size() != 0) {
		Iterator iterSbiFuncts = exportedFuncts.iterator();
		int minEl = 1000;
		
		SbiFunctions funct = null;

		// search the functionality for insert
		while (iterSbiFuncts.hasNext()) {
		    funct = (SbiFunctions) iterSbiFuncts.next();
		    String path = funct.getPath();
		    int numEl = path.split("/").length; // the number of levels
		    if (numEl < minEl) {
			minEl = numEl;
			functToInsert = funct;
		    }
		}

		// remove function from list
		exportedFuncts = removeFromList(exportedFuncts, functToInsert);

		logger.info("Insert the Funtionality (Path):" + functToInsert.getPath());

		// insert function
		Integer expId = functToInsert.getFunctId();
		Map functIdAss = metaAss.getFunctIDAssociation();
		Set functIdAssSet = functIdAss.keySet();
		// if the functionality is present skip the insert
		if (functIdAssSet.contains(expId)) {
		    logger.info("Exported functionality " + functToInsert.getName() + " not inserted"
			    + " because it has the same label (and the same path) of an existing functionality");
		    metaLog.log("Exported functionality " + functToInsert.getName() + " not inserted"
			    + " because it has the same label (and the same path) of an existing functionality");
		    continue;
		}
		SbiFunctions newFunct = ImportUtilities.makeNewSbiFunction(functToInsert);
		String functCd = functToInsert.getFunctTypeCd();
		Map unique = new HashMap();
		unique.put("valuecd", functCd);
		unique.put("domaincd", "FUNCT_TYPE");
		SbiDomains existDom = (SbiDomains) importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
		if (existDom != null) {
		    newFunct.setFunctType(existDom);
		    newFunct.setFunctTypeCd(existDom.getValueCd());
		}
		String path = newFunct.getPath();
		String parentPath = path.substring(0, path.lastIndexOf('/'));
		Query hibQuery = sessionCurrDB.createQuery(" from SbiFunctions where path = '" + parentPath + "'");
		SbiFunctions functParent = (SbiFunctions) hibQuery.uniqueResult();
		if (functParent != null) {
		    newFunct.setParentFunct(functParent);
		}
		// manages prog column that determines the folders order
		if (functParent == null)
		    newFunct.setProg(new Integer(1));
		else {
		    // loads sub functionalities
		    Query query = sessionCurrDB
			    .createQuery("select max(s.prog) from SbiFunctions s where s.parentFunct.functId = "
				    + functParent.getFunctId());
		    Integer maxProg = (Integer) query.uniqueResult();
		    if (maxProg != null)
			newFunct.setProg(new Integer(maxProg.intValue() + 1));
		    else
			newFunct.setProg(new Integer(1));
		}
		sessionCurrDB.save(newFunct);
		metaLog.log("Inserted new functionality " + newFunct.getName() + " with path " + newFunct.getPath());
		Integer newId = newFunct.getFunctId();
		metaAss.insertCoupleFunct(expId, newId);

	    }
	} catch (Exception e) {
		if (functToInsert != null) {
			logger.error("Error while importing exported functionality with path [" + functToInsert.getPath() + "].", e);
		}
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}

    }

    private List removeFromList(List complete, SbiFunctions funct) {
	logger.debug("IN");
	List toReturn = new ArrayList();
	Iterator iterList = complete.iterator();
	while (iterList.hasNext()) {
	    SbiFunctions listFunct = (SbiFunctions) iterList.next();
	    if (!listFunct.getPath().equals(funct.getPath())) {
		toReturn.add(listFunct);
	    }
	}
	logger.debug("OUT");
	return toReturn;
    }

    /**
     * Import exported lovs
     * 
     * @throws EMFUserError
     */
    private void importLovs(boolean overwrite) throws EMFUserError {
	logger.debug("IN");
	SbiLov exportedLov = null;
	try {
	    List exportedLovs = importer.getAllExportedSbiObjects(sessionExpDB, "SbiLov", null);
	    Iterator iterSbiLovs = exportedLovs.iterator();
	    while (iterSbiLovs.hasNext()) {
			exportedLov = (SbiLov) iterSbiLovs.next();
			Integer oldId = exportedLov.getLovId();
			Integer existingLovId = null; 
			Map lovIdAss = metaAss.getLovIDAssociation();
			Set lovIdAssSet = lovIdAss.keySet();
			if (lovIdAssSet.contains(oldId) && !overwrite) {
			    metaLog.log("Exported lov " + exportedLov.getName() + " not inserted"
				    + " because it has the same label of an existing lov");
			    continue;
			} else {
				existingLovId = (Integer) lovIdAss.get(oldId);
			}
			if (existingLovId != null) {
				logger.info("The lov with label:[" + exportedLov.getLabel() + "] is just present. It will be updated.");
			    metaLog.log("The lov with label = [" + exportedLov.getLabel() + "] will be updated.");
				SbiLov existinglov = ImportUtilities.modifyExistingSbiLov(exportedLov, sessionCurrDB, existingLovId);
			    ImportUtilities.associateWithExistingEntities(existinglov, exportedLov, sessionCurrDB, importer, metaAss);
			    sessionCurrDB.update(existinglov);
			} else {
				SbiLov newlov = ImportUtilities.makeNewSbiLov(exportedLov);
				ImportUtilities.associateWithExistingEntities(newlov, exportedLov, sessionCurrDB, importer, metaAss);
				sessionCurrDB.save(newlov);
				metaLog.log("Inserted new lov " + newlov.getName());
				Integer newId = newlov.getLovId();
				metaAss.insertCoupleLov(oldId, newId);
			}
	    }
	} catch (Exception e) {
		if (exportedLov != null) {
			logger.error("Error while importing exported lov with label [" + exportedLov.getLabel() + "].", e);
		}
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Import exported checks
     * 
     * @throws EMFUserError
     */
    private void importChecks() throws EMFUserError {
	logger.debug("IN");
	SbiChecks check = null;
	try {
	    List exportedChecks = importer.getAllExportedSbiObjects(sessionExpDB, "SbiChecks", null);
	    Iterator iterSbiChecks = exportedChecks.iterator();
	    while (iterSbiChecks.hasNext()) {
			check = (SbiChecks) iterSbiChecks.next();
			Integer oldId = check.getCheckId();
			Map checkIdAss = metaAss.getCheckIDAssociation();
			Set checkIdAssSet = checkIdAss.keySet();
			if (checkIdAssSet.contains(oldId)) {
			    metaLog.log("Exported check " + check.getName() + " not inserted"
				    + " because it has the same label of an existing check");
			    continue;
			}
			SbiChecks newck = ImportUtilities.makeNewSbiCheck(check);
			String valueCd = check.getValueTypeCd();
			Map unique = new HashMap();
			unique.put("valuecd", valueCd);
			unique.put("domaincd", "CHECK");
			SbiDomains existDom = (SbiDomains) importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
			if (existDom != null) {
			    newck.setCheckType(existDom);
			    newck.setValueTypeCd(existDom.getValueCd());
			}
			sessionCurrDB.save(newck);
	
			metaLog.log("Inserted new check " + newck.getName());
			Integer newId = newck.getCheckId();
			metaAss.insertCoupleCheck(oldId, newId);
	    }
	} catch (Exception e) {
		if (check != null) {
			logger.error("Error while importing exported check with label [" + check.getLabel() + "].", e);
		}
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Import exported parameters
     * 
     * @throws EMFUserError
     */
    private void importParameters(boolean overwrite) throws EMFUserError {
	logger.debug("IN");
	SbiParameters exportedParameter = null;
	try {
	    List exportedParams = importer.getAllExportedSbiObjects(sessionExpDB, "SbiParameters", null);
	    Iterator iterSbiParams = exportedParams.iterator();
	    while (iterSbiParams.hasNext()) {
			exportedParameter = (SbiParameters) iterSbiParams.next();
			Integer oldId = exportedParameter.getParId();
			Integer existingParId = null;
			Map paramIdAss = metaAss.getParameterIDAssociation();
			Set paramIdAssSet = paramIdAss.keySet();
			if (paramIdAssSet.contains(oldId) && !overwrite) {
			    metaLog.log("Exported parameter " + exportedParameter.getName() + " not inserted"
				    + " because it has the same label of an existing parameter");
			    continue;
			} else {
				existingParId = (Integer) paramIdAss.get(oldId);
			}
			if (existingParId != null) {
			    logger.info("The parameter with label:[" + exportedParameter.getLabel() + "] is just present. It will be updated.");
			    metaLog.log("The parameter with label = [" + exportedParameter.getLabel() + "] will be updated.");
				SbiParameters existingParameter = ImportUtilities.modifyExistingSbiParameter(exportedParameter, sessionCurrDB, existingParId);
				ImportUtilities.associateWithExistingEntities(existingParameter, exportedParameter, sessionCurrDB, importer, metaAss);
				sessionCurrDB.update(existingParameter);
			} else {
				SbiParameters newPar = ImportUtilities.makeNewSbiParameter(exportedParameter);
				ImportUtilities.associateWithExistingEntities(newPar, exportedParameter, sessionCurrDB, importer, metaAss);
				sessionCurrDB.save(newPar);
				metaLog.log("Inserted new parameter " + newPar.getName());
				Integer newId = newPar.getParId();
				metaAss.insertCoupleParameter(oldId, newId);
			}
	    }
	} catch (Exception e) {
		if (exportedParameter != null) {
			logger.error("Error while importing exported parameter with label [" + exportedParameter.getLabel() + "].", e);
		}
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * import exported biobjects
     * 
     * @throws EMFUserError
     */
    private void importBIObjects(boolean overwrite) throws EMFUserError {
		logger.debug("IN");
		SbiObjects exportedObj = null;
		try {
		    List exportedBIObjs = importer.getAllExportedSbiObjects(sessionExpDB, "SbiObjects", "label");
		    Iterator iterSbiObjs = exportedBIObjs.iterator();
		    while (iterSbiObjs.hasNext()) {
				exportedObj = (SbiObjects) iterSbiObjs.next();
				Integer expId = exportedObj.getBiobjId();
				Integer existingObjId = null;
				Map objIdAss = metaAss.getBIobjIDAssociation();
			    Set objIdAssSet = objIdAss.keySet();
				if (objIdAssSet.contains(expId) && !overwrite) {
					metaLog.log("Exported biobject "+exportedObj.getName()+" not inserted" +
								" because it has the same label of an existing biobject");
					continue;
				} else {
					existingObjId = (Integer) objIdAss.get(expId);
				}
				
				SbiObjects obj = null;
				if (existingObjId != null) {
					logger.info("The document with label:[" + exportedObj.getLabel() + "] is just present. It will be updated.");
				    metaLog.log("The document with label = [" + exportedObj.getLabel() + "] will be updated.");
				    obj = ImportUtilities.modifyExistingSbiObject(exportedObj, sessionCurrDB, existingObjId);
				    ImportUtilities.associateWithExistingEntities(obj, exportedObj, sessionCurrDB, importer, metaAss);
				    sessionCurrDB.update(obj);
				} else {
					obj = ImportUtilities.makeNewSbiObject(exportedObj);
					ImportUtilities.associateWithExistingEntities(obj, exportedObj, sessionCurrDB, importer, metaAss);
				    // insert document
				    Integer newId = (Integer) sessionCurrDB.save(obj);
				    metaLog.log("Inserted new biobject " + obj.getName());
				    metaAss.insertCoupleBIObj(expId, newId);
				}
				// manage object template
				insertObjectTemplate(obj, exportedObj.getBiobjId());
				// manage sub_object here
			    insertSubObject(obj, exportedObj);
			    // manage snapshot here
			    insertSnapshot(obj, exportedObj);
			    // insert object into folders tree
				importFunctObject(exportedObj.getBiobjId());
				// puts parameters into object
				importBIObjPar(exportedObj.getBiobjId());
				// puts dependencies into object
				importObjParUse(exportedObj.getBiobjId());
				
			    commit();
			    
			    // TODO controllare che fa questo e se serve!!!
			    //updateSubObject(obj, exportedObj.getBiobjId());
		    }
		} catch (Exception e) {
			if (exportedObj != null) {
				logger.error("Error while importing exported biobject with label [" + exportedObj.getLabel() + "]", e);
			}
		    logger.error("Error while inserting object ", e);
		    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		} finally {
		    logger.debug("OUT");
		}
    }

    private void insertSnapshot(SbiObjects obj, SbiObjects exportedObj) throws EMFUserError {
		logger.debug("IN");
		List exportedSnapshotsList = null;
		List currentSnapshotsList = null;
		SbiSnapshots expSbiSnapshots = null;
		try {
		    Query hibQuery = sessionExpDB.createQuery(" from SbiSnapshots ot where ot.sbiObject.biobjId = " + exportedObj.getBiobjId());
		    exportedSnapshotsList = hibQuery.list();
		    if (exportedSnapshotsList.isEmpty()) {
		    	logger.debug("Exported document with label = [" + exportedObj.getLabel() + "] has no snapshots");
		    	return;
		    }
		    hibQuery = sessionCurrDB
	    		.createQuery(" from SbiSnapshots ot where ot.sbiObject.biobjId = " + obj.getBiobjId());
		    currentSnapshotsList = hibQuery.list();
		    Iterator exportedSnapshotsListIt = exportedSnapshotsList.iterator();
		    while (exportedSnapshotsListIt.hasNext()) {
		    	expSbiSnapshots = (SbiSnapshots) exportedSnapshotsListIt.next();
		    	if (isAlreadyExisting(expSbiSnapshots, currentSnapshotsList)) {
		    		logger.info("Exported snaphost with name = [" + expSbiSnapshots.getName() + "] and creation date = [" + expSbiSnapshots.getCreationDate() + "] (of document with name = [" + exportedObj.getName() + "] and label = [" + exportedObj.getLabel() + "]) is already existing, most likely it is the same snapshot, so it will not be inserted.");
		    		metaLog.log("Exported snaphost with name = [" + expSbiSnapshots.getName() + "] and creation date = [" + expSbiSnapshots.getCreationDate() + "] (of document with name = [" + exportedObj.getName() + "] and label = [" + exportedObj.getLabel() + "]) is already existing, most likely it is the same snapshot, so it will not be inserted.");
		    		continue;
		    	} else {
				    SbiSnapshots newSnapshots = ImportUtilities.makeNewSbiSnapshots(expSbiSnapshots);
				    newSnapshots.setSbiObject(obj);
				    SbiBinContents binary = insertBinaryContent(expSbiSnapshots.getSbiBinContents());
				    newSnapshots.setSbiBinContents(binary);
				    sessionCurrDB.save(newSnapshots);
		    	}
		    }
		} catch (Exception e) {
			if (expSbiSnapshots != null) {
				logger.error("Error while importing exported snapshot with name [" + expSbiSnapshots.getName() + "] " +
						"of biobject with label [" + obj.getLabel() + "]", e);
			}
		    logger.error("Error while getting exported template objects ", e);
		    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		} finally {
		    logger.debug("OUT");
		}
    }
    
    private boolean isAlreadyExisting(SbiSnapshots expSbiSnapshots,
			List currentSnapshotsList) {
		Iterator currentSnapshotsListIt = currentSnapshotsList.iterator();
		while (currentSnapshotsListIt.hasNext()) {
			SbiSnapshots currentSnapshot = (SbiSnapshots) currentSnapshotsListIt.next();
			if (((currentSnapshot.getName() == null && expSbiSnapshots.getName() == null) || 
					(currentSnapshot.getName() != null && currentSnapshot.getName().equals(expSbiSnapshots.getName()))) 
				&& ((currentSnapshot.getDescription() == null && expSbiSnapshots.getDescription() == null) || 
						(currentSnapshot.getDescription() != null && currentSnapshot.getDescription().equals(expSbiSnapshots.getDescription())))
				&& currentSnapshot.getCreationDate().equals(expSbiSnapshots.getCreationDate())) {
						return true;
			}
		}
		return false;
	}


	private void updateSnapshot(SbiObjects obj, Integer objIdExp) throws EMFUserError {
	logger.debug("IN");
	List subObjList = null;
	SbiSnapshots expSbiSnapshots = null;
	try {
	    Query hibQuery = sessionCurrDB.createQuery(" from SbiSnapshots ot where ot.sbiObject.biobjId = " + obj.getBiobjId());
	    subObjList = hibQuery.list();
	    if (subObjList.isEmpty()) {
	    	logger.warn(" Error during reading of Existing SbiSnapshots");
	    }
	    SbiSnapshots existingSbiSnapshots = (SbiSnapshots) subObjList.get(0);
	    
	    hibQuery = sessionExpDB.createQuery(" from SbiSnapshots ot where ot.sbiObject.biobjId = " + objIdExp);
	    subObjList = hibQuery.list();
	    if (subObjList.isEmpty()) {
	    	logger.warn(" SbiSnapshots is not present");
	    	return;
	    }
	    
	    expSbiSnapshots = (SbiSnapshots) subObjList.get(0);
	    
	    existingSbiSnapshots.setCreationDate(expSbiSnapshots.getCreationDate());
	    existingSbiSnapshots.setDescription(expSbiSnapshots.getDescription());
	    existingSbiSnapshots.setName(expSbiSnapshots.getName());
	    //existingSbiSnapshots.setSbiObject(obj);
	    SbiBinContents existingBinaryContent=existingSbiSnapshots.getSbiBinContents();
	    sessionCurrDB.delete(existingBinaryContent);
	    SbiBinContents binary = insertBinaryContent(expSbiSnapshots.getSbiBinContents());
	    existingSbiSnapshots.setSbiBinContents(binary);
	    sessionCurrDB.update(existingSbiSnapshots);

	} catch (Exception e) {
		if (expSbiSnapshots != null) {
			logger.error("Error while updating exported snapshot with name [" + expSbiSnapshots.getName() + "] " +
					"of biobject with label [" + obj.getLabel() + "]", e);
		}
	    logger.error("Error while getting exported template objects ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }    

    private void updateSubObject(SbiObjects obj, Integer objIdExp) throws EMFUserError {
	logger.debug("IN");
	List subObjList = null;
	try {
	    // read the existing sub object
	    Query hibQuery = sessionCurrDB
	    .createQuery(" from SbiSubObjects ot where ot.sbiObject.biobjId = " + obj.getBiobjId());
            subObjList = hibQuery.list();
            if (subObjList.isEmpty()) {
        	logger.warn(" Existing Sub Object is not present");
            }	
            SbiSubObjects existingSubObject = (SbiSubObjects) subObjList.get(0);
            if (existingSubObject==null){
        	logger.warn("Don't read the Existing SubObject ... ERROR");
        	return;
            }
            // read the import sub object
	    hibQuery = sessionExpDB
		    .createQuery(" from SbiSubObjects ot where ot.sbiObject.biobjId = " + objIdExp);
	    subObjList = hibQuery.list();
	    if (subObjList.isEmpty()) {
		logger.warn(" Sub Object is not present");
		return;
	    }
	    SbiSubObjects expSubObject = (SbiSubObjects) subObjList.get(0);
	    existingSubObject.setCreationDate(expSubObject.getCreationDate());
	    existingSubObject.setDescription(expSubObject.getDescription());
	    existingSubObject.setLastChangeDate(expSubObject.getLastChangeDate());
	    existingSubObject.setIsPublic(expSubObject.getIsPublic());
	    existingSubObject.setName(expSubObject.getName());
	    existingSubObject.setOwner(expSubObject.getOwner());
	    //existingSubObject.setSbiObject(obj);
	    SbiBinContents existingBinaryContent=existingSubObject.getSbiBinContents();
	    sessionCurrDB.delete(existingBinaryContent);
	    SbiBinContents binary = insertBinaryContent(expSubObject.getSbiBinContents());
	    existingSubObject.setSbiBinContents(binary);
	    sessionCurrDB.update(existingSubObject);

	} catch (HibernateException he) {
	    logger.error("Error while getting exported template objects ", he);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }
    
    private void insertSubObject(SbiObjects obj, SbiObjects exportedObj) throws EMFUserError {
		logger.debug("IN");
		List exportedSubObjList = null;
		List currentSubObjList = null;
		SbiSubObjects expSubObject = null;
		try {
		    Query hibQuery = sessionExpDB
			    .createQuery(" from SbiSubObjects ot where ot.sbiObject.biobjId = " + exportedObj.getBiobjId());
		    exportedSubObjList = hibQuery.list();
		    if (exportedSubObjList.isEmpty()) {
		    	logger.debug("Exported document with label=[" + exportedObj.getLabel() + "] has no subobjects");
		    	return;
		    }
		    hibQuery = sessionCurrDB
		    	.createQuery(" from SbiSubObjects ot where ot.sbiObject.biobjId = " + obj.getBiobjId());
		    currentSubObjList = hibQuery.list();
		    Iterator exportedSubObjListIt = exportedSubObjList.iterator();
		    while (exportedSubObjListIt.hasNext()) {
		    	expSubObject = (SbiSubObjects) exportedSubObjListIt.next();
		    	if (isAlreadyExisting(expSubObject, currentSubObjList)) {
		    		logger.info("Exported subobject with name = [" + expSubObject.getName() + "] and owner = [" + expSubObject.getOwner() + "] and visibility = [" + expSubObject.getIsPublic() + "] and creation date = [" + expSubObject.getCreationDate() + "] (of document with name = [" + exportedObj.getName() + "] and label = [" + exportedObj.getLabel() + "]) is already existing, so it will not be inserted.");
		    		metaLog.log("Exported subobject with name = [" + expSubObject.getName() + "] and owner = [" + expSubObject.getOwner() + "] and visibility = [" + expSubObject.getIsPublic() + "] and creation date = [" + expSubObject.getCreationDate() + "] (of document with name = [" + exportedObj.getName() + "] and label = [" + exportedObj.getLabel() + "]) is already existing, most likely it is the same subobject, so it will not be inserted.");
		    		continue;
		    	} else {
				    SbiSubObjects newSubObj = ImportUtilities.makeNewSbiSubObjects(expSubObject);
				    newSubObj.setSbiObject(obj);
				    SbiBinContents binary = insertBinaryContent(expSubObject.getSbiBinContents());
				    newSubObj.setSbiBinContents(binary);
				    sessionCurrDB.save(newSubObj);
		    	}
		    }
		} catch (Exception e) {
			if (expSubObject != null) {
				logger.error("Error while importing exported subobject with name [" + expSubObject.getName() + "] " +
						"of biobject with label [" + exportedObj.getLabel() + "]", e);
			}
		    logger.error("Error while getting exported template objects ", e);
		    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		} finally {
		    logger.debug("OUT");
		}
    }

   /**
    * Controls if a subobject is already existing (i.e. they have the same name, owner, visibility, 
    * creation date and last modification date)
    * @param expSubObject
    * @param currentSubObjList
    * @return the true if the subobject is already existing, false otherwise
    */
    private boolean isAlreadyExisting(SbiSubObjects expSubObject,
			List currentSubObjList) {
		Iterator currentSubObjListIt = currentSubObjList.iterator();
		while (currentSubObjListIt.hasNext()) {
			SbiSubObjects currentSubObject = (SbiSubObjects) currentSubObjListIt.next();
			if (((currentSubObject.getName() == null && expSubObject.getName() == null) ||
					(currentSubObject.getName() != null && currentSubObject.getName().equals(expSubObject.getName())))  
				&& ((currentSubObject.getOwner() == null && expSubObject.getOwner() == null) ||
					(currentSubObject.getOwner() != null && currentSubObject.getOwner().equals(expSubObject.getOwner())))
				&& currentSubObject.getIsPublic().equals(expSubObject.getIsPublic())
				&& currentSubObject.getCreationDate().equals(expSubObject.getCreationDate())
				&& currentSubObject.getLastChangeDate().equals(expSubObject.getLastChangeDate())) {
					return true;
			}
		}
		return false;
	}


	private void insertObjectTemplate(SbiObjects obj, Integer objIdExp) throws EMFUserError {
	logger.debug("IN");
	List templateList = null;
	try {
	    Query hibQuery = sessionExpDB.createQuery(" from SbiObjTemplates ot where ot.sbiObject.biobjId = "
		    + objIdExp);
	    templateList = hibQuery.list();
	    if (templateList.isEmpty()) {
			logger.warn("WARN: exported document with id = " + objIdExp + " has no template");
			return;
	    }
	    // finds the next prog value
	    Integer nextProg = getNextProg(obj.getBiobjId());
	    
	    SbiObjTemplates expTemplate = (SbiObjTemplates) templateList.get(0);
	    SbiObjTemplates newObj = ImportUtilities.makeNewSbiObjTemplates(expTemplate);
	    newObj.setProg(nextProg);
	    if (nextProg.intValue() > 1) {
	    	// old current template is no more active
			logger.debug("Update template...");
			SbiObjTemplates existingObjTemplate = getCurrentActiveTemplate(obj.getBiobjId());
			existingObjTemplate.setActive(new Boolean(false));
			sessionCurrDB.save(existingObjTemplate);
	    }
	    newObj.setSbiObject(obj);
	    SbiBinContents binary = insertBinaryContent(expTemplate.getSbiBinContents());
	    newObj.setSbiBinContents(binary);
	    sessionCurrDB.save(newObj);
	} catch (Exception he) {
	    logger.error("Error while getting exported template objects ", he);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    private SbiObjTemplates getCurrentActiveTemplate(Integer biobjId) {
    	logger.debug("IN");
    	String hql = "from SbiObjTemplates sot where sot.active=true and sot.sbiObject.biobjId=" + biobjId;
		Query query = sessionCurrDB.createQuery(hql);
		SbiObjTemplates hibObjTemp = (SbiObjTemplates)query.uniqueResult();
		logger.debug("OUT");
		return hibObjTemp;
    }
    
    private Integer getNextProg(Integer objId) {
    	logger.debug("IN");
	    String hql = "select max(sot.prog) as maxprog from SbiObjTemplates sot where sot.sbiObject.biobjId = " + objId;
	    Query query = sessionCurrDB.createQuery(hql);
		Integer maxProg = (Integer) query.uniqueResult();
		if (maxProg == null) {
			maxProg = new Integer(1);
		} else {
			maxProg = new Integer(maxProg.intValue() + 1);
		}
		logger.debug("OUT");
		return maxProg;
    }
    
    private SbiBinContents insertBinaryContent(SbiBinContents binaryContent) throws EMFUserError {
	logger.debug("IN");
	List templateList = null;
	SbiBinContents newObj = null;
	try {
	    Query hibQuery = sessionExpDB.createQuery(" from SbiBinContents where id = " + binaryContent.getId());
	    templateList = hibQuery.list();
	    if (templateList.isEmpty()) {
		logger.warn(" Binary Content is not present");
		return null;
	    }
	    SbiBinContents expBinaryContent = (SbiBinContents) templateList.get(0);
	    newObj = ImportUtilities.makeNewSbiBinContents(expBinaryContent);
	    // save new binary content
	    sessionCurrDB.save(newObj);
	    return newObj;

	} catch (HibernateException he) {
	    logger.error("Error while getting exported binary content objects ", he);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Imports exported paruses
     * 
     * @throws EMFUserError
     */
    private void importParuse() throws EMFUserError {
	logger.debug("IN");
	SbiParuse paruse = null;
	try {
	    List exportedParuses = importer.getAllExportedSbiObjects(sessionExpDB, "SbiParuse", null);
	    Iterator iterSbiParuses = exportedParuses.iterator();
	    while (iterSbiParuses.hasNext()) {
			paruse = (SbiParuse) iterSbiParuses.next();
			SbiParameters param = paruse.getSbiParameters();
			Integer oldParamId = param.getParId();
			Map assParams = metaAss.getParameterIDAssociation();
			Integer newParamId = (Integer) assParams.get(oldParamId);
			if (newParamId != null) {
			    SbiParameters newParam = ImportUtilities.makeNewSbiParameter(param, newParamId);
			    paruse.setSbiParameters(newParam);
			}
	
			SbiLov lov = paruse.getSbiLov();
			if (lov != null) {
			    Integer oldLovId = lov.getLovId();
			    Map assLovs = metaAss.getLovIDAssociation();
			    Integer newLovId = (Integer) assLovs.get(oldLovId);
			    if (newLovId != null) {
				SbiLov newlov = ImportUtilities.makeNewSbiLov(lov, newLovId);
				paruse.setSbiLov(newlov);
			    }
			}
	
			Integer oldId = paruse.getUseId();
			Map paruseIdAss = metaAss.getParuseIDAssociation();
			Set paruseIdAssSet = paruseIdAss.keySet();
			if (paruseIdAssSet.contains(oldId)) {
			    metaLog.log("Exported parameter use " + paruse.getName() + " not inserted"
				    + " because it has the same label of an existing parameter use");
			    continue;
			}
			SbiParuse newParuse = ImportUtilities.makeNewSbiParuse(paruse);
			sessionCurrDB.save(newParuse);
			metaLog.log("Inserted new parameter use " + newParuse.getName() + " for param " + param.getName());
			Integer newId = newParuse.getUseId();
			sessionExpDB.evict(paruse);
			metaAss.insertCoupleParuse(oldId, newId);
	    }
	} catch (Exception e) {
		if (paruse != null) {
			logger.error("Error while importing exported parameter use with label [" + paruse.getLabel() + "].", e);
		}
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Import exported paruses association with roles
     * 
     * @throws EMFUserError
     */
    private void importParuseDet() throws EMFUserError {
	logger.debug("IN");
	SbiParuseDet parusedet = null;
	try {
	    List exportedParuseDets = importer.getAllExportedSbiObjects(sessionExpDB, "SbiParuseDet", null);
	    Iterator iterSbiParuseDets = exportedParuseDets.iterator();
	    while (iterSbiParuseDets.hasNext()) {
			parusedet = (SbiParuseDet) iterSbiParuseDets.next();
			// get ids of exported role and paruse associzted
			Integer paruseid = parusedet.getId().getSbiParuse().getUseId();
			Integer roleid = parusedet.getId().getSbiExtRoles().getExtRoleId();
			// get association of roles and paruses
			Map paruseIdAss = metaAss.getParuseIDAssociation();
			Map roleIdAss = metaAss.getRoleIDAssociation();
			// try to get from association the id associate to the exported
			// metadata
			Integer newParuseid = (Integer) paruseIdAss.get(paruseid);
			Integer newRoleid = (Integer) roleIdAss.get(roleid);
			// build a new SbiParuseDet
			SbiParuseDet newParuseDet = ImportUtilities.makeNewSbiParuseDet(parusedet, newParuseid, newRoleid);
			// check if the association between metadata already exist
			Map unique = new HashMap();
			unique.put("paruseid", newParuseid);
			unique.put("roleid", newRoleid);
			Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiParuseDet());
			if (existObj == null) {
			    sessionCurrDB.save(newParuseDet);
			    metaLog.log("Inserted new association between paruse " + parusedet.getId().getSbiParuse().getName()
				    + " and role " + parusedet.getId().getSbiExtRoles().getName());
			}
			
	    }
	} catch (Exception e) {
		if (parusedet != null) {
			logger.error("Error while importing association between exported parameter use with label [" + parusedet.getId().getSbiParuse().getLabel()
				    + "] and exported role with name [" + parusedet.getId().getSbiExtRoles().getName() + "]", e);
		}
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Imports associations between parameter uses and checks
     * 
     * @throws EMFUserError
     */
    private void importParuseCheck() throws EMFUserError {
	logger.debug("IN");
	SbiParuseCk paruseck = null;
	try {
	    List exportedParuseChecks = importer.getAllExportedSbiObjects(sessionExpDB, "SbiParuseCk", null);
	    Iterator iterSbiParuseChecks = exportedParuseChecks.iterator();
	    while (iterSbiParuseChecks.hasNext()) {
			paruseck = (SbiParuseCk) iterSbiParuseChecks.next();
			// get ids of exported paruse and check associzted
			Integer paruseid = paruseck.getId().getSbiParuse().getUseId();
			Integer checkid = paruseck.getId().getSbiChecks().getCheckId();
			// get association of checks and paruses
			Map paruseIdAss = metaAss.getParuseIDAssociation();
			Map checkIdAss = metaAss.getCheckIDAssociation();
			// try to get from association the id associate to the exported
			// metadata
			Integer newParuseid = (Integer) paruseIdAss.get(paruseid);
			Integer newCheckid = (Integer) checkIdAss.get(checkid);
			// build a new paruse check
			SbiParuseCk newParuseCk = ImportUtilities.makeNewSbiParuseCk(paruseck, newParuseid, newCheckid);
			// check if the association between metadata already exist
			Map unique = new HashMap();
			unique.put("paruseid", newParuseid);
			unique.put("checkid", newCheckid);
			Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiParuseCk());
			if (existObj == null) {
			    sessionCurrDB.save(newParuseCk);
			    metaLog.log("Inserted new association between paruse " + paruseck.getId().getSbiParuse().getName()
				    + " and check " + paruseck.getId().getSbiChecks().getName());
			}
			

			// build a new id for the SbiParuseCheck
			SbiParuseCkId parusecheckid = paruseck.getId();
			if (newParuseid != null) {
			    SbiParuse sbiparuse = parusecheckid.getSbiParuse();
			    SbiParuse newParuse = ImportUtilities.makeNewSbiParuse(sbiparuse, newParuseid);
			    parusecheckid.setSbiParuse(newParuse);
			    paruseid = newParuseid;
			}
			if (newCheckid != null) {
			    SbiChecks sbicheck = parusecheckid.getSbiChecks();
			    SbiChecks newCheck = ImportUtilities.makeNewSbiCheck(sbicheck, newCheckid);
			    parusecheckid.setSbiChecks(newCheck);
			    checkid = newCheckid;
			}
			paruseck.setId(parusecheckid);
			
	    }
	} catch (Exception e) {
		if (paruseck != null) {
			logger.error("Error while importing association between exported parameter use with label [" + paruseck.getId().getSbiParuse().getLabel()
				    + "] and exported check with label [" + paruseck.getId().getSbiChecks().getLabel() + "]", e);
		}
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Imports biobject links
     * 
     * @throws EMFUserError
     */
    private void importBIObjectLinks() throws EMFUserError {
	logger.debug("IN");
	SbiSubreports objlink = null;
	try {
	    List exportedBIObjectsLinks = importer.getAllExportedSbiObjects(sessionExpDB, "SbiSubreports", null);
	    Iterator iterSbiObjLinks = exportedBIObjectsLinks.iterator();
	    while (iterSbiObjLinks.hasNext()) {
			objlink = (SbiSubreports) iterSbiObjLinks.next();
	
			// get biobjects
			SbiObjects masterBIObj = objlink.getId().getMasterReport();
			SbiObjects subBIObj = objlink.getId().getSubReport();
			Integer masterid = masterBIObj.getBiobjId();
			Integer subid = subBIObj.getBiobjId();
			// get association of object
			Map biobjIdAss = metaAss.getBIobjIDAssociation();
			// try to get from association the id associate to the exported
			// metadata
			Integer newMasterId = (Integer) biobjIdAss.get(masterid);
			Integer newSubId = (Integer) biobjIdAss.get(subid);

			// build a new SbiSubreport
			// build a new id for the SbiSubreport
			SbiSubreportsId sbiSubReportsId = objlink.getId();
			SbiSubreportsId newSubReportsId = new SbiSubreportsId();
			if (sbiSubReportsId != null) {
			    SbiObjects master = sbiSubReportsId.getMasterReport();
			    SbiObjects sub = sbiSubReportsId.getMasterReport();
			    SbiObjects newMaster = ImportUtilities.makeNewSbiObject(master, newMasterId);
			    SbiObjects newSub = ImportUtilities.makeNewSbiObject(sub, newSubId);
			    newSubReportsId.setMasterReport(newMaster);
			    newSubReportsId.setSubReport(newSub);
			}
			SbiSubreports newSubReport = new SbiSubreports();
			newSubReport.setId(newSubReportsId);
			// check if the association between metadata already exist
			Map unique = new HashMap();
			unique.put("masterid", newMasterId);
			unique.put("subid", newSubId);
			Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiSubreports());
			if (existObj == null) {
			    sessionCurrDB.save(newSubReport);
			    metaLog.log("Inserted new link between master object " + masterBIObj.getLabel()
				    + " and sub object " + subBIObj.getLabel());
			}
	    }
	} catch (Exception e) {
		if (objlink != null) {
			logger.error("Error while importing association between exported master biobject with label [" + objlink.getId().getMasterReport().getLabel()
				    + "] and exported sub biobject with label [" + objlink.getId().getSubReport().getLabel() + "]", e);
		}
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }


    /**
     * Imports associations between functionalities and current object
     * 
     * @param exportedBIObjectId The id of the current exported object
     * @throws EMFUserError
     */
    private void importFunctObject(Integer exportedBIObjectId) throws EMFUserError {
	logger.debug("IN");
	try {
	    List exportedFunctObjects = importer.getFilteredExportedSbiObjects(sessionExpDB, "SbiObjFunc", "id.sbiObjects.biobjId", exportedBIObjectId);
	    Iterator iterSbiFunctObjects = exportedFunctObjects.iterator();
	    while (iterSbiFunctObjects.hasNext()) {
		SbiObjFunc objfunct = (SbiObjFunc) iterSbiFunctObjects.next();
		// get ids of exported role, function and state associzted
		Integer functid = objfunct.getId().getSbiFunctions().getFunctId();
		Integer objid = objfunct.getId().getSbiObjects().getBiobjId();
		Integer prog = objfunct.getProg();
		// get association of roles and paruses
		Map functIdAss = metaAss.getFunctIDAssociation();
		Map biobjIdAss = metaAss.getBIobjIDAssociation();
		// try to get from association the id associate to the exported
		// metadata
		Integer newFunctid = (Integer) functIdAss.get(functid);
		Integer newObjectid = (Integer) biobjIdAss.get(objid);
		// build a new id for the SbiObjFunct
		SbiObjFuncId objfunctid = objfunct.getId();
		if (objfunctid != null) {
		    SbiFunctions sbifunct = objfunctid.getSbiFunctions();
		    SbiFunctions newFunct = ImportUtilities.makeNewSbiFunction(sbifunct, newFunctid);
		    objfunctid.setSbiFunctions(newFunct);
		    functid = newFunctid;
		}
		if (newObjectid != null) {
		    SbiObjects sbiobj = objfunctid.getSbiObjects();
		    SbiObjects newObj = ImportUtilities.makeNewSbiObject(sbiobj, newObjectid);
		    objfunctid.setSbiObjects(newObj);
		    objid = newObjectid;
		}
		objfunct.setId(objfunctid);
		// check if the association between metadata already exist
		Map unique = new HashMap();
		unique.put("objectid", objid);
		unique.put("functionid", functid);
		Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiObjFunc());
		if (existObj == null) {
		    sessionCurrDB.save(objfunct);
		    metaLog.log("Inserted new association between function "
			    + objfunct.getId().getSbiFunctions().getName() + " and object "
			    + objfunct.getId().getSbiObjects().getName());
		}
	    }
	} catch (HibernateException he) {
	    logger.error("Error while inserting object ", he);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} catch (Exception e) {
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Imports associations between functionalities and roles
     * 
     * @throws EMFUserError
     */
    private void importFunctRoles() throws EMFUserError {
	logger.debug("IN");
	SbiFuncRole functrole = null;
	try {
	    List exportedFunctRoles = importer.getAllExportedSbiObjects(sessionExpDB, "SbiFuncRole", null);
	    Iterator iterSbiFunctRoles = exportedFunctRoles.iterator();
	    while (iterSbiFunctRoles.hasNext()) {
			functrole = (SbiFuncRole) iterSbiFunctRoles.next();
			// get ids of exported role, function and state associzted
			Integer functid = functrole.getId().getFunction().getFunctId();
			Integer roleid = functrole.getId().getRole().getExtRoleId();
			Integer stateid = functrole.getId().getState().getValueId();
			// get association of roles and paruses
			Map functIdAss = metaAss.getFunctIDAssociation();
			Map roleIdAss = metaAss.getRoleIDAssociation();
			// try to get from association the id associate to the exported
			// metadata
			Integer newFunctid = (Integer) functIdAss.get(functid);
			Integer newRoleid = (Integer) roleIdAss.get(roleid);
			// build a new SbiFuncRole
			SbiFuncRole newFunctRole = ImportUtilities.makeNewSbiFunctRole(functrole, newFunctid, newRoleid);
			// get sbidomain of the current system
			String stateCd = functrole.getStateCd();
			Map uniqueDom = new HashMap();
			uniqueDom.put("valuecd", stateCd);
			uniqueDom.put("domaincd", "STATE");
			SbiDomains existDom = (SbiDomains) importer.checkExistence(uniqueDom, sessionCurrDB, new SbiDomains());
			if (existDom != null) {
				newFunctRole.getId().setState(existDom);
				newFunctRole.setStateCd(existDom.getValueCd());
			}
			// check if the association between metadata already exist
			Map unique = new HashMap();
			unique.put("stateid", existDom.getValueId());
			unique.put("roleid", newRoleid);
			unique.put("functionid", newFunctid);
			Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiFuncRole());
			if (existObj == null) {
			    sessionCurrDB.save(newFunctRole);
			    metaLog.log("Inserted new association between function "
				    + functrole.getId().getFunction().getName() + " and role "
				    + functrole.getId().getRole().getName());
			}
	    }
	} catch (Exception e) {
		if (functrole != null) {
			logger.error("Error while importing association between exported function with path [" + functrole.getId().getFunction().getPath()
				    + "] and exported role with name [" + functrole.getId().getRole().getName() + "]", e);
		}
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }


    /**
     * Imports associations between parameters and current exported biobject
     * 
     * @param exportedBIObjectId The id of the current exported object
     * @throws EMFUserError
     */
    private void importBIObjPar(Integer exportedBIObjectId) throws EMFUserError {
	logger.debug("IN");
	try {
	    List exportedObjPars = importer.getFilteredExportedSbiObjects(sessionExpDB, "SbiObjPar", "sbiObject.biobjId", exportedBIObjectId);
	    Iterator iterSbiObjPar = exportedObjPars.iterator();
	    while (iterSbiObjPar.hasNext()) {
		SbiObjPar objpar = (SbiObjPar) iterSbiObjPar.next();
		SbiParameters param = objpar.getSbiParameter();
		SbiObjects biobj = objpar.getSbiObject();
		Integer oldParamId = param.getParId();
		Integer oldBIObjId = biobj.getBiobjId();
		Map assBIObj = metaAss.getBIobjIDAssociation();
		Map assParams = metaAss.getParameterIDAssociation();
		Integer newParamId = (Integer) assParams.get(oldParamId);
		Integer newBIObjId = (Integer) assBIObj.get(oldBIObjId);
		if (newParamId != null) {
		    SbiParameters newParam = ImportUtilities.makeNewSbiParameter(param, newParamId);
		    objpar.setSbiParameter(newParam);
		}
		if (newBIObjId != null) {
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
		if (existObj != null) {
		    metaLog.log("Exported association between object " + objpar.getSbiObject().getName() + " "
			    + " and parameter " + objpar.getSbiParameter().getName() + " with url name "
			    + objpar.getParurlNm() + " not inserted"
			    + " because already existing into the current database");
		    continue;
		}

		SbiObjPar newObjpar = ImportUtilities.makeNewSbiObjpar(objpar);
		sessionCurrDB.save(newObjpar);
		metaLog.log("Inserted new biobject parameter with " + newObjpar.getParurlNm() + " for biobject "
			+ newObjpar.getSbiObject().getName());
		Integer newId = newObjpar.getObjParId();
		sessionExpDB.evict(objpar);
		metaAss.insertCoupleObjpar(oldId, newId);
	    }
	} catch (HibernateException he) {
	    logger.error("Error while inserting object ", he);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} catch (Exception e) {
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }


    /**
     * Imports biparameter dependencies for current exported biobject
     * 
     * @param exportedBIObjectId The id of the current exported biobject
     * @throws EMFUserError
     */
    private void importObjParUse(Integer exportedBIObjectId) throws EMFUserError {
	logger.debug("IN");
	try {
	    List exportedParDepends = importer.getFilteredExportedSbiObjects(sessionExpDB, "SbiObjParuse", "id.sbiObjPar.sbiObject.biobjId", exportedBIObjectId);
	    Iterator iterParDep = exportedParDepends.iterator();
	    while (iterParDep.hasNext()) {
		SbiObjParuse pardep = (SbiObjParuse) iterParDep.next();
		// get ids of objpar and paruse associated
		Integer objparId = pardep.getId().getSbiObjPar().getObjParId();
		Integer paruseId = pardep.getId().getSbiParuse().getUseId();
		Integer objparfathId = pardep.getId().getSbiObjParFather().getObjParId();
		String filterOp = pardep.getId().getFilterOperation();
		// get association of objpar and paruses
		Map objparIdAss = metaAss.getObjparIDAssociation();
		Map paruseIdAss = metaAss.getParuseIDAssociation();
		// try to get from association the id associate to the exported
		// metadata
		Integer newObjparId = (Integer) objparIdAss.get(objparId);
		Integer newParuseId = (Integer) paruseIdAss.get(paruseId);
		Integer newObjParFathId = (Integer) objparIdAss.get(objparfathId);
		// build a new id for the SbiObjParuse
		SbiObjParuseId objparuseid = pardep.getId();
		objparuseid.setFilterOperation(filterOp);
		if (newParuseId != null) {
		    SbiParuse sbiparuse = objparuseid.getSbiParuse();
		    SbiParuse newParuse = ImportUtilities.makeNewSbiParuse(sbiparuse, newParuseId);
		    objparuseid.setSbiParuse(newParuse);
		    paruseId = newParuseId;
		}
		if (newObjparId != null) {
		    SbiObjPar sbiobjpar = objparuseid.getSbiObjPar();
		    SbiObjPar newObjPar = ImportUtilities.makeNewSbiObjpar(sbiobjpar, newObjparId);
		    objparuseid.setSbiObjPar(newObjPar);
		    objparId = newObjparId;
		}
		if (newObjParFathId != null) {
		    SbiObjPar sbiobjparfath = objparuseid.getSbiObjParFather();
		    SbiObjPar newObjParFath = ImportUtilities.makeNewSbiObjpar(sbiobjparfath, newObjParFathId);
		    objparuseid.setSbiObjParFather(newObjParFath);
		    objparfathId = newObjParFathId;
		}

		pardep.setId(objparuseid);

		Map unique = new HashMap();
		unique.put("objparid", objparId);
		unique.put("paruseid", paruseId);
		unique.put("objparfathid", objparfathId);
		unique.put("filterop", filterOp);
		Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiObjParuse());
		if (existObj == null) {
		    sessionCurrDB.save(pardep);
		    metaLog.log("Inserted new dependecies between biparameter "
			    + pardep.getId().getSbiObjPar().getLabel() + " of the biobject "
			    + pardep.getId().getSbiObjPar().getSbiObject().getLabel() + " and paruse "
			    + pardep.getId().getSbiParuse().getLabel());
		}
	    }
	} catch (HibernateException he) {
	    logger.error("Error while inserting object ", he);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} catch (Exception e) {
	    logger.error("Error while inserting object ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Ends the import procedure.
     */
    public void stopImport() {
	logger.debug("IN");
	metaAss.clear();
	rollback();
	FileUtilities.deleteDir(new File(pathBaseFolder));
	logger.debug("OUT");
    }

    /**
     * Gets the list of exported data sources.
     * 
     * @return List of the exported data sources
     * 
     * @throws EMFUserError the EMF user error
     */
    public List getExportedDataSources() throws EMFUserError {
	logger.debug("IN");
	List datasources = new ArrayList();
	try {
	    List exportedDS = importer.getAllExportedSbiObjects(sessionExpDB, "SbiDataSource", null);
	    Iterator iterSbiDataSource = exportedDS.iterator();
	    while (iterSbiDataSource.hasNext()) {
		SbiDataSource dataSource = (SbiDataSource) iterSbiDataSource.next();
		DataSource ds = new DataSource();
		ds.setDsId(dataSource.getDsId());
		ds.setLabel(dataSource.getLabel());
		ds.setDescr(dataSource.getDescr());
		ds.setUser(dataSource.getUser());
		ds.setPwd(dataSource.getPwd());
		ds.setDriver(dataSource.getDriver());
		ds.setUrlConnection(dataSource.getUrl_connection());
		ds.setJndi(dataSource.getJndi());
		ds.setDialectId(dataSource.getDialect().getValueId());
		datasources.add(ds);
	    }
	} finally {
	    logger.debug("OUT");
	}

	logger.debug("OUT");
	return datasources;
    }

    /**
     * Check the existance of the exported metadata into the current system
     * metadata and insert their associations into the association object
     * MeatadataAssociation.
     * 
     * @throws EMFUserError the EMF user error
     */
    public void checkExistingMetadata() throws EMFUserError {
	logger.debug("IN");
	List exportedParams = importer.getAllExportedSbiObjects(sessionExpDB, "SbiParameters", null);
	Iterator iterSbiParams = exportedParams.iterator();
	while (iterSbiParams.hasNext()) {
	    SbiParameters paramExp = (SbiParameters) iterSbiParams.next();
	    String labelPar = paramExp.getLabel();
	    Object existObj = importer.checkExistence(labelPar, sessionCurrDB, new SbiParameters());
	    if (existObj != null) {
		SbiParameters paramCurr = (SbiParameters) existObj;
		metaAss.insertCoupleParameter(paramExp.getParId(), paramCurr.getParId());
		metaAss.insertCoupleParameter(paramExp, paramCurr);
		metaLog.log("Found an existing Parameter " + paramCurr.getName() + " with "
			+ "the same label of the exported parameter " + paramExp.getName());
	    }
	}
	List exportedRoles = importer.getAllExportedSbiObjects(sessionExpDB, "SbiExtRoles", null);
	Iterator iterSbiRoles = exportedRoles.iterator();
	while (iterSbiRoles.hasNext()) {
	    SbiExtRoles roleExp = (SbiExtRoles) iterSbiRoles.next();
	    String roleName = roleExp.getName();
	    Integer expRoleId = roleExp.getExtRoleId();
	    Map rolesAss = metaAss.getRoleIDAssociation();
	    Set keysExpRoleAss = rolesAss.keySet();
	    if (keysExpRoleAss.contains(expRoleId))
		continue;
	    Object existObj = importer.checkExistence(roleName, sessionCurrDB, new SbiExtRoles());
	    if (existObj != null) {
		SbiExtRoles roleCurr = (SbiExtRoles) existObj;
		metaAss.insertCoupleRole(roleExp.getExtRoleId(), roleCurr.getExtRoleId());
		metaAss.insertCoupleRole(roleExp, roleCurr);
		metaLog.log("Found an existing Role " + roleCurr.getName() + " with "
			+ "the same name of the exported role " + roleExp.getName());
	    }
	}
	List exportedParuse = importer.getAllExportedSbiObjects(sessionExpDB, "SbiParuse", null);
	Iterator iterSbiParuse = exportedParuse.iterator();
	while (iterSbiParuse.hasNext()) {
	    SbiParuse paruseExp = (SbiParuse) iterSbiParuse.next();
	    String label = paruseExp.getLabel();
	    SbiParameters par = paruseExp.getSbiParameters();
	    Integer idPar = par.getParId();
	    // check if the parameter has been associated to a current system
	    // parameter
	    Map paramsAss = metaAss.getParameterIDAssociation();
	    Integer idParAss = (Integer) paramsAss.get(idPar);
	    if (idParAss != null)
		idPar = idParAss;
	    Map unique = new HashMap();
	    unique.put("label", label);
	    unique.put("idpar", idPar);
	    Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiParuse());
	    if (existObj != null) {
		SbiParuse paruseCurr = (SbiParuse) existObj;
		metaAss.insertCoupleParuse(paruseExp.getUseId(), paruseCurr.getUseId());
		metaAss.insertCoupleParuse(paruseExp, paruseCurr);
		metaLog.log("Found an existing Parameter use " + paruseCurr.getName() + " with "
			+ "the same label of the exported parameter use " + paruseExp.getName());
	    }
	}
	List exportedBiobj = importer.getAllExportedSbiObjects(sessionExpDB, "SbiObjects", null);
	Iterator iterSbiBiobj = exportedBiobj.iterator();
	while (iterSbiBiobj.hasNext()) {
	    SbiObjects objExp = (SbiObjects) iterSbiBiobj.next();
	    String label = objExp.getLabel();
	    Object existObj = importer.checkExistence(label, sessionCurrDB, new SbiObjects());
	    if (existObj != null) {
		SbiObjects objCurr = (SbiObjects) existObj;
		metaAss.insertCoupleBIObj(objExp.getBiobjId(), objCurr.getBiobjId());
		metaAss.insertCoupleBIObj(objExp, objCurr);
		metaLog.log("Found an existing BIObject " + objCurr.getName() + " with "
			+ "the same label and path of the exported BIObject " + objExp.getName());
	    }
	}
	List exportedLov = importer.getAllExportedSbiObjects(sessionExpDB, "SbiLov", null);
	Iterator iterSbiLov = exportedLov.iterator();
	while (iterSbiLov.hasNext()) {
	    SbiLov lovExp = (SbiLov) iterSbiLov.next();
	    String label = lovExp.getLabel();
	    Object existObj = importer.checkExistence(label, sessionCurrDB, new SbiLov());
	    if (existObj != null) {
		SbiLov lovCurr = (SbiLov) existObj;
		metaAss.insertCoupleLov(lovExp.getLovId(), lovCurr.getLovId());
		metaAss.insertCoupleLov(lovExp, lovCurr);
		metaLog.log("Found an existing Lov " + lovCurr.getName() + " with "
			+ "the same label of the exported lov " + lovExp.getName());
	    }
	}
	List exportedFunct = importer.getAllExportedSbiObjects(sessionExpDB, "SbiFunctions", null);
	Iterator iterSbiFunct = exportedFunct.iterator();
	while (iterSbiFunct.hasNext()) {
	    SbiFunctions functExp = (SbiFunctions) iterSbiFunct.next();
	    String code = functExp.getCode();
	    Object existObj = importer.checkExistence(code, sessionCurrDB, new SbiFunctions());
	    if (existObj != null) {
		SbiFunctions functCurr = (SbiFunctions) existObj;
		metaAss.insertCoupleFunct(functExp.getFunctId(), functCurr.getFunctId());
		metaAss.insertCoupleFunct(functExp, functCurr);
		metaLog.log("Found an existing Functionality " + functCurr.getName() + " with "
			+ "the same CODE of the exported functionality " + functExp.getName());
	    }
	}
	List exportedEngine = importer.getAllExportedSbiObjects(sessionExpDB, "SbiEngines", null);
	Iterator iterSbiEng = exportedEngine.iterator();
	while (iterSbiEng.hasNext()) {
	    SbiEngines engExp = (SbiEngines) iterSbiEng.next();
	    String label = engExp.getLabel();
	    Integer expEngineId = engExp.getEngineId();
	    Map engAss = metaAss.getEngineIDAssociation();
	    Set keysExpEngAss = engAss.keySet();
	    if (keysExpEngAss.contains(expEngineId))
		continue;
	    Object existObj = importer.checkExistence(label, sessionCurrDB, new SbiEngines());
	    if (existObj != null) {
		SbiEngines engCurr = (SbiEngines) existObj;
		metaAss.insertCoupleEngine(engExp.getEngineId(), engCurr.getEngineId());
		metaAss.insertCoupleEngine(engExp, engCurr);
		metaLog.log("Found an existing Engine " + engCurr.getName() + " with "
			+ "the same label of the exported engine " + engExp.getName());
	    }
	}
	List exportedCheck = importer.getAllExportedSbiObjects(sessionExpDB, "SbiChecks", null);
	Iterator iterSbiCheck = exportedCheck.iterator();
	while (iterSbiCheck.hasNext()) {
	    SbiChecks checkExp = (SbiChecks) iterSbiCheck.next();
	    String label = checkExp.getLabel();
	    Object existObj = importer.checkExistence(label, sessionCurrDB, new SbiChecks());
	    if (existObj != null) {
		SbiChecks checkCurr = (SbiChecks) existObj;
		metaAss.insertCoupleCheck(checkExp.getCheckId(), checkCurr.getCheckId());
		metaAss.insertCoupleCheck(checkExp, checkCurr);
		metaLog.log("Found an existing check " + checkCurr.getName() + " with "
			+ "the same label of the exported check " + checkExp.getName());
	    }
	}
	List exportedObjPar = importer.getAllExportedSbiObjects(sessionExpDB, "SbiObjPar", null);
	Iterator iterSbiObjPar = exportedObjPar.iterator();
	while (iterSbiObjPar.hasNext()) {
	    SbiObjPar objparExp = (SbiObjPar) iterSbiObjPar.next();
	    String urlName = objparExp.getParurlNm();

	    Integer objid = objparExp.getSbiObject().getBiobjId();
	    Map objIdAss = metaAss.getBIobjIDAssociation();
	    Integer newObjid = (Integer) objIdAss.get(objid);
	    if (newObjid != null)
		objid = newObjid;

	    Integer parid = objparExp.getSbiParameter().getParId();
	    Map parIdAss = metaAss.getParameterIDAssociation();
	    Integer newParid = (Integer) parIdAss.get(parid);
	    if (newParid != null)
		parid = newParid;

	    Map uniqueMap = new HashMap();
	    uniqueMap.put("biobjid", objid);
	    uniqueMap.put("paramid", parid);
	    uniqueMap.put("urlname", urlName);
	    Object existObj = importer.checkExistence(uniqueMap, sessionCurrDB, new SbiObjPar());

	    if (existObj != null) {
		SbiObjPar objParCurr = (SbiObjPar) existObj;
		metaAss.insertCoupleObjpar(objparExp.getObjParId(), objParCurr.getObjParId());
		metaAss.insertCoupleObjpar(objparExp, objParCurr);
		metaLog.log("Found an existing association between object " + objparExp.getSbiObject().getName()
			+ " and parameter " + objparExp.getSbiParameter().getName() + " with " + " the same url "
			+ objparExp.getParurlNm() + " name of the exported objpar ");
	    }
	}
	List exportedDs = importer.getAllExportedSbiObjects(sessionExpDB, "SbiDataSource", null);
	Iterator iterSbiDs = exportedDs.iterator();
	while (iterSbiDs.hasNext()) {
		SbiDataSource dsExp = (SbiDataSource) iterSbiDs.next();
		String label = dsExp.getLabel();
	    Object existObj = importer.checkExistence(label, sessionCurrDB, new SbiDataSource());
	    if (existObj != null) {
	    	SbiDataSource dsCurr = (SbiDataSource) existObj;
	    	metaAss.insertCoupleDataSources(new Integer(dsExp.getDsId()), new Integer(dsCurr.getDsId()));
			metaLog.log("Found an existing data source " + dsCurr.getLabel() + " with "
				+ "the same label of one exported data source");
	    }
	}
	List exportedDataset = importer.getAllExportedSbiObjects(sessionExpDB, "SbiDataSetConfig", null);
	Iterator iterSbiDataset = exportedDataset.iterator();
	while (iterSbiDataset.hasNext()) {
		SbiDataSetConfig dsExp = (SbiDataSetConfig) iterSbiDataset.next();
		String label = dsExp.getLabel();
	    Object existObj = importer.checkExistence(label, sessionCurrDB, new SbiDataSetConfig());
	    if (existObj != null) {
	    	SbiDataSetConfig dsCurr = (SbiDataSetConfig) existObj;
	    	metaAss.insertCoupleDataSets(new Integer(dsExp.getDsId()), new Integer(dsCurr.getDsId()));
			metaLog.log("Found an existing dataset " + dsCurr.getLabel() + " with "
				+ "the same label of one exported dataset");
	    }
	}
	logger.debug("OUT");
    }

    /**
     * Gets the object which contains the association between exported metadata
     * and the current system metadata.
     * 
     * @return MetadataAssociation the object which contains the association
     * between exported metadata and the current system metadata
     */
    public MetadataAssociations getMetadataAssociation() {
	return metaAss;
    }

    /* (non-Javadoc)
     * @see it.eng.spagobi.tools.importexport.IImportManager#getExistingObject(java.lang.Integer, java.lang.Class)
     */
    public Object getExistingObject(Integer id, Class objClass) {
	return importer.getObject(id, objClass, txCurrDB, sessionCurrDB);
    }

    /* (non-Javadoc)
     * @see it.eng.spagobi.tools.importexport.IImportManager#getExportedObject(java.lang.Integer, java.lang.Class)
     */
    public Object getExportedObject(Integer id, Class objClass) {
	return importer.getObject(id, objClass, txExpDB, sessionExpDB);
    }

    /* (non-Javadoc)
     * @see it.eng.spagobi.tools.importexport.IImportManager#getUserAssociation()
     */
    public UserAssociationsKeeper getUserAssociation() {
	return usrAss;
    }

    /* (non-Javadoc)
     * @see it.eng.spagobi.tools.importexport.IImportManager#getImpAssMode()
     */
    public String getImpAssMode() {
	return impAssMode;
    }

    /* (non-Javadoc)
     * @see it.eng.spagobi.tools.importexport.IImportManager#setImpAssMode(java.lang.String)
     */
    public void setImpAssMode(String impAssMode) {
	this.impAssMode = impAssMode;
    }
    
	/* (non-Javadoc)
	 * @see it.eng.spagobi.tools.importexport.IImportManager#getAssociationFile()
	 */
	public AssociationFile getAssociationFile() {
		return associationFile;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.tools.importexport.IImportManager#setAssociationFile(it.eng.spagobi.tools.importexport.bo.AssociationFile)
	 */
	public void setAssociationFile(AssociationFile associationFile) {
		this.associationFile = associationFile;
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.tools.importexport.IImportManager#associateAllExportedRolesByUserAssociation()
	 */
	public boolean associateAllExportedRolesByUserAssociation() throws EMFUserError {
		logger.debug("IN");
		try {
			List exportedRoles = this.getExportedRoles();
			IRoleDAO roleDAO = DAOFactory.getRoleDAO();
			List currentRoles = roleDAO.loadAllRoles();
			Iterator exportedRolesIt = exportedRoles.iterator();
			while (exportedRolesIt.hasNext()) {
				Role exportedRole = (Role) exportedRolesIt.next();
				String associatedRoleName = this.getUserAssociation().getAssociatedRole(exportedRole.getName());
				if (associatedRoleName == null || associatedRoleName.trim().equals("")) return true;
				Iterator currentRolesIt = currentRoles.iterator();
				boolean associatedRoleNameExists = false;
				while (currentRolesIt.hasNext()) {
					Role currentRole = (Role) currentRolesIt.next();
					if (currentRole.getName().equals(associatedRoleName)) {
						associatedRoleNameExists = true;
						metaAss.insertCoupleRole(exportedRole.getId(), currentRole.getId());
						break;
					}
				}
				if (!associatedRoleNameExists) return true;
			}
			return false;
		} finally {
			logger.debug("OUT");
		}
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.tools.importexport.IImportManager#associateAllExportedEnginesByUserAssociation()
	 */
	public boolean associateAllExportedEnginesByUserAssociation() throws EMFUserError {
		logger.debug("IN");
		try {
			List exportedEngines = this.getExportedEngines();
			IEngineDAO engineDAO = DAOFactory.getEngineDAO();
			List currentEngines = engineDAO.loadAllEngines();
			Iterator exportedEnginesIt = exportedEngines.iterator();
			while (exportedEnginesIt.hasNext()) {
				Engine exportedEngine = (Engine) exportedEnginesIt.next();
				String associatedEngineLabel = this.getUserAssociation().getAssociatedEngine(exportedEngine.getLabel());
				if (associatedEngineLabel == null || associatedEngineLabel.trim().equals("")) return true;
				Iterator currentEngineIt = currentEngines.iterator();
				boolean associatedEngineLabelExists = false;
				while (currentEngineIt.hasNext()) {
					Engine currentEngine = (Engine) currentEngineIt.next();
					if (currentEngine.getLabel().equals(associatedEngineLabel)) {
						associatedEngineLabelExists = true;
						metaAss.insertCoupleEngine(exportedEngine.getId(), currentEngine.getId());
						break;
					}
				}
				if (!associatedEngineLabelExists) return true;
			}
			return false;
		} finally {
			logger.debug("OUT");
		}
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.tools.importexport.IImportManager#associateAllExportedDataSourcesByUserAssociation()
	 */
	public boolean associateAllExportedDataSourcesByUserAssociation() throws EMFUserError {
		logger.debug("IN");
		try {
			List exportedDataSources = this.getExportedDataSources();
			if (exportedDataSources == null || exportedDataSources.size() == 0) return false;
			IDataSourceDAO datasourceDAO = DAOFactory.getDataSourceDAO();
			List currentDataSources = datasourceDAO.loadAllDataSources();
			Iterator exportedDataSourcesIt = exportedDataSources.iterator();
			while (exportedDataSourcesIt.hasNext()) {
				IDataSource exportedDataSource = (IDataSource) exportedDataSourcesIt.next();
				String associatedDataSourceLabel = this.getUserAssociation().getAssociatedDataSource(exportedDataSource.getLabel());
				if (associatedDataSourceLabel == null || associatedDataSourceLabel.trim().equals("")) return true;
				Iterator currentDataSourcesIt = currentDataSources.iterator();
				boolean associatedDataSourceLabelExists = false;
				while (currentDataSourcesIt.hasNext()) {
					IDataSource currentDataSource = (IDataSource) currentDataSourcesIt.next();
					if (currentDataSource.getLabel().equals(associatedDataSourceLabel)) {
						associatedDataSourceLabelExists = true;
						metaAss.insertCoupleDataSources(new Integer(exportedDataSource.getDsId()), new Integer(currentDataSource.getDsId()));
						break;
					}
				}
				if (!associatedDataSourceLabelExists) return true;
			}
			return false;
		} finally {
			logger.debug("OUT");
		}
	}
	
    private void importMapCatalogue(boolean overwrite) throws EMFUserError {
		logger.debug("IN");
		try {
			// import maps
			List exportedMaps = importer.getAllExportedSbiObjects(sessionExpDB, "SbiGeoMaps", null);
			Iterator iterMaps = exportedMaps.iterator();
			while (iterMaps.hasNext()) {
				SbiGeoMaps expMap = (SbiGeoMaps) iterMaps.next();
				String name = expMap.getName();
				Object existObj = importer.checkExistence(name, sessionCurrDB, new SbiGeoMaps());
				SbiGeoMaps newMap = null;
				if (existObj != null) {
					if (!overwrite) {
						metaLog.log("Found an existing map '" + name + "' with "
							+ "the same name of the exported map. It will be not overwritten.");
						continue;
					} else {
						metaLog.log("Found an existing map '" + name + "' with "
								+ "the same name of the exported map. It will be overwritten.");
						newMap = (SbiGeoMaps) existObj;
					}
				} else {
					newMap = new SbiGeoMaps();
				}
				newMap.setName(expMap.getName());
				newMap.setDescr(expMap.getDescr());
				newMap.setFormat(expMap.getFormat());
				newMap.setUrl(expMap.getUrl());
				
				if (expMap.getBinContents() != null) {
				    SbiBinContents binary = insertBinaryContent(expMap.getBinContents());
				    newMap.setBinContents(binary);
				} else {
					metaLog.log("WARN: exported map with name '" + expMap.getName() + "' has no content!!");
					newMap.setBinContents(null);
				}
				
				sessionCurrDB.save(newMap);
				metaAss.insertCoupleMaps(new Integer(expMap.getMapId()), new Integer(newMap.getMapId()));
			}
			
			// import features
			List exportedFeatures = importer.getAllExportedSbiObjects(sessionExpDB, "SbiGeoFeatures", null);
			Iterator iterFeatures = exportedFeatures.iterator();
			while (iterFeatures.hasNext()) {
				SbiGeoFeatures expFeature = (SbiGeoFeatures) iterFeatures.next();
				String name = expFeature.getName();
				Object existObj = importer.checkExistence(name, sessionCurrDB, new SbiGeoFeatures());
				SbiGeoFeatures newFeature = null;
				if (existObj != null) {
					if (!overwrite) {
						metaLog.log("Found an existing feature '" + name + "' with "
							+ "the same name of the exported feature. It will be not overwritten.");
						continue;
					} else {
						metaLog.log("Found an existing feature '" + name + "' with "
								+ "the same name of the exported feature. It will be overwritten.");
						newFeature = (SbiGeoFeatures) existObj;
					}
				} else {
					newFeature = new SbiGeoFeatures();
				}
				newFeature.setName(expFeature.getName());
				newFeature.setDescr(expFeature.getDescr());
				newFeature.setType(expFeature.getType());
				sessionCurrDB.save(newFeature);
				metaAss.insertCoupleFeatures(new Integer(expFeature.getFeatureId()), new Integer(newFeature.getFeatureId()));
			}
			
			// import association between maps and features
			List exportedMapFeatures = importer.getAllExportedSbiObjects(sessionExpDB, "SbiGeoMapFeatures", null);
			Iterator iterMapFeatures = exportedMapFeatures.iterator();
			while (iterMapFeatures.hasNext()) {
				SbiGeoMapFeatures expMapFeature = (SbiGeoMapFeatures) iterMapFeatures.next();
				Integer expMapId = new Integer(expMapFeature.getId().getMapId());
				Integer expFeatureId = new Integer(expMapFeature.getId().getFeatureId());
				Integer existingMapId = null;
				Integer existingFeatureId = null;
				// find associated map id
				Map mapsIDAssociations = metaAss.getMapIDAssociation();
				Set mapsIDAssociationsKeySet = mapsIDAssociations.keySet();
				if (!mapsIDAssociationsKeySet.contains(expMapId)) {
					metaLog.log("Association between exported map with id = " + expMapId + " and exported feature with id = " + expFeatureId + 
							" will not be imported: the map was not imported.");
					continue;
				} else {
					existingMapId = (Integer) mapsIDAssociations.get(expMapId);
				}
				// find associated feature id
				Map featuresIDAssociations = metaAss.getFeaturesIDAssociation();
				Set featuresIDAssociationsKeySet = featuresIDAssociations.keySet();
				if (!featuresIDAssociationsKeySet.contains(expFeatureId)) {
					metaLog.log("Association between exported map with id = " + expMapId + " and exported feature with id = " + expFeatureId + 
							" will not be imported: the feature was not imported.");
					continue;
				} else {
					existingFeatureId = (Integer) featuresIDAssociations.get(expFeatureId);
				}
				
				Map unique = new HashMap();
				unique.put("mapId", existingMapId);
				unique.put("featureId", existingFeatureId);
				Object existObj = importer.checkExistence(unique, sessionCurrDB, new SbiGeoMapFeatures());
				SbiGeoMapFeatures newMapFeature = null;
				if (existObj != null) {
					if (!overwrite) {
						metaLog.log("Found an existing association between map " + existingMapId + " and feature " + existingFeatureId + ". " +
								"It will be not overwritten.");
						continue;
					} else {
						metaLog.log("Found an existing association between map " + existingMapId + " and feature " + existingFeatureId + ". " +
								"It will be overwritten.");
						newMapFeature = (SbiGeoMapFeatures) existObj;
					}
				} else {
					newMapFeature = new SbiGeoMapFeatures();
					SbiGeoMapFeaturesId hibMapFeatureId = new SbiGeoMapFeaturesId();			
					hibMapFeatureId.setMapId(existingMapId.intValue());
					hibMapFeatureId.setFeatureId(existingFeatureId.intValue());
					newMapFeature.setId(hibMapFeatureId);
				}
				newMapFeature.setSvgGroup(expMapFeature.getSvgGroup());
				newMapFeature.setVisibleFlag(expMapFeature.getVisibleFlag());
				sessionCurrDB.save(newMapFeature);
			}
			
			/*
			// copy all exported map files
			File mapsDir = new File(ConfigSingleton.getRootPath() + "/components/mapcatalogue/maps");
			if (!mapsDir.exists()) mapsDir.mkdirs();
			File exportedmapsDir = new File(pathBaseFolder + "/components/mapcatalogue/maps");
			if (exportedmapsDir.exists() && exportedmapsDir.isDirectory()) {
				File[] exportedMapsFiles = exportedmapsDir.listFiles();
				for (int i = 0; i < exportedMapsFiles.length; i++) {
					File exportedMapFile = exportedMapsFiles[i];
					if (exportedMapFile.isFile()) {
						FileOutputStream fos = null;
						InputStream is = null;
						try {
							File copy = new File(mapsDir.getAbsolutePath() + "/" + exportedMapFile.getName());
							if (copy.exists()) {
								if (!overwrite) continue;
								if (!copy.delete()) {
									logger.warn("Could not delete file [" + copy.getAbsolutePath() + "]. Map cannot be updated.");
									metaLog.log("Could not delete file [" + copy.getAbsolutePath() + "]. Map cannot be updated.");
									continue;
								}
							}
					        fos = new FileOutputStream(copy);
					        is = new FileInputStream(exportedMapFile);
					        int read = 0;
					        while ((read = is.read()) != -1) {
					        	fos.write(read);
					        }
					        fos.flush();
						} catch (Exception e) {
						    logger.error("Error while coping map catalogue files ", e);
						    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
						} finally {
				        	try {
					        	if (fos != null) {
					        		fos.close();
					        	}
					        	if (is != null) {
					        		is.close();
					        	}
				        	} catch (Exception e) {
				        	    logger.error("Error while closing streams " , e);
				        	}
						}
					}
				}
			}
			*/
			
		} finally {
			logger.debug("OUT");
		}
	}
 
    
    /*
	private void importResources(boolean overwrite) {
		logger.debug("IN");
		try {
			File exportedResourcesDir = new File(pathBaseFolder + "/resources");
			if (exportedResourcesDir.exists() && exportedResourcesDir.isDirectory()) {
				SourceBean config = (SourceBean) ConfigSingleton.getInstance().getAttribute("SPAGOBI.RESOURCE_PATH_JNDI_NAME");
				logger.debug("RESOURCE_PATH_JNDI_NAME configuration found: " + config);
				String resourcePathJndiName = config.getCharacters();
				Context ctx = new InitialContext();
				String value = (String)ctx.lookup(resourcePathJndiName);
				logger.debug("Resource path found from jndi: " + value);
				File resourcesDir = new File(value);
				if (overwrite) {
					FileUtilities.copyDirectory(exportedResourcesDir, resourcesDir, true, true, true);
				} else {
					FileUtilities.copyDirectory(exportedResourcesDir, resourcesDir, true, false, false);
				}
			}
		} catch (Exception e) {
			logger.debug("Error while importing resources", e);
		} finally {
			logger.debug("OUT");
		}
	}
	*/
	
}
