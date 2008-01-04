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

package it.eng.spagobi.tools.importexport.services;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.validation.EMFValidationError;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IRoleDAO;
import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.commons.utilities.ChannelUtilities;
import it.eng.spagobi.commons.utilities.PortletUtilities;
import it.eng.spagobi.commons.utilities.UploadedFile;
import it.eng.spagobi.engines.config.dao.IEngineDAO;
import it.eng.spagobi.engines.config.metadata.SbiEngines;
import it.eng.spagobi.tools.importexport.IExportManager;
import it.eng.spagobi.tools.importexport.IImportManager;
import it.eng.spagobi.tools.importexport.ImportExportConstants;
import it.eng.spagobi.tools.importexport.ImportResultInfo;
import it.eng.spagobi.tools.importexport.MetadataAssociations;
import it.eng.spagobi.tools.importexport.TransformManager;
import it.eng.spagobi.tools.importexport.UserAssociationsKeeper;
import it.eng.spagobi.tools.importexport.bo.AssociationFile;
import it.eng.spagobi.tools.importexport.dao.AssociationFileDAO;
import it.eng.spagobi.tools.importexport.dao.IAssociationFileDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This class implements a module which handles the import / export operations
 */
public class ImportExportModule extends AbstractModule {

    static private Logger logger = Logger.getLogger(ImportExportModule.class);

    /**
     * Initialize the module
     * 
     * @param config
     *                Configuration sourcebean of the module
     */
    public void init(SourceBean config) {
    }

    /**
     * Reads the operation asked by the user and calls the export or import
     * methods
     * 
     * @param request
     *                The Source Bean containing all request parameters
     * @param response
     *                The Source Bean containing all response parameters
     * @throws exception
     *                 If an exception occurs
     */
    public void service(SourceBean request, SourceBean response) throws Exception {
	logger.debug("IN");
	if (ChannelUtilities.isPortletRunning()) {
	    if (PortletUtilities.isMultipartRequest()) {
		request = ChannelUtilities.getSpagoRequestFromMultipart();
	    }
	}

	String message = (String) request.getAttribute("MESSAGEDET");
	logger.debug("begin of import / export service with message =" + message);
	EMFErrorHandler errorHandler = getErrorHandler();
	try {
	    if (message == null) {
		EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, "101", "component_impexp_messages");
		logger.warn("The message parameter is null");
		errorHandler.addError(userError);
		throw userError;
	    }
	    if (message.trim().equalsIgnoreCase(ImportExportConstants.EXPORT)) {
		exportConf(request, response);
	    } else if (message.trim().equalsIgnoreCase(ImportExportConstants.IMPORT)) {
		importConf(request, response);
	    } else if (message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_ROLE_ASSOCIATION)) {
		associateRoles(request, response);
	    } else if (message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_ENGINE_ASSOCIATION)) {
		associateEngines(request, response);
	    } else if (message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_CONNECTION_ASSOCIATION)) {
		associateConnections(request, response);
	    } else if (message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_METADATA_ASS)) {
		associateMetadata(request, response);
	    } else if (message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_EXIT)) {
		exitImport(request, response);
	    } else if (message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_BACK_ENGINE_ASS)) {
		backEngineAssociation(request, response);
	    } else if (message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_BACK_CONN_ASS)) {
		backConnAssociation(request, response);
	    } else if (message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_BACK_METADATA_ASS)) {
		backMetadataAssociation(request, response);
	    }
	} catch (EMFUserError emfu) {
	    errorHandler.addError(emfu);
	} catch (Exception ex) {
	    logger.error("Error during the service execution", ex);
	    EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, "100", "component_impexp_messages");
	    errorHandler.addError(error);
	    return;
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Manages the request of the user to export some selected objects
     * 
     * @param request
     *                Spago SourceBean request
     * @param response
     *                Spago SourceBean response
     * @throws EMFUserError
     */
    private void exportConf(SourceBean request, SourceBean response) throws EMFUserError {
	logger.debug("IN");
	IExportManager expManager = null;
	String exportFileName = (String) request.getAttribute("exportFileName");
	if ((exportFileName == null) || (exportFileName.trim().equals(""))) {
	    logger.error("Missing name of the exported file");
	    throw new EMFValidationError(EMFErrorSeverity.ERROR, "exportFileName", "8006", "component_impexp_messages");
	    // throw new EMFUserError(EMFErrorSeverity.ERROR, "8006",
	    // "component_impexp_messages");
	}
	try {
	    String exportSubObject = (String) request.getAttribute("exportSubObj");
	    boolean expSubObj = false;
	    if (exportSubObject != null) {
		expSubObj = true;
	    }
	    String exportSnapshots = (String) request.getAttribute("exportSnapshots");
	    boolean exportSnaps = false;
	    if (exportSnapshots != null) {
		exportSnaps = true;
	    }
	    ConfigSingleton conf = ConfigSingleton.getInstance();
	    SourceBean exporterSB = (SourceBean) conf.getAttribute("IMPORTEXPORT.EXPORTER");
	    String pathExportFolder = (String) exporterSB.getAttribute("exportFolder");
	    if (!pathExportFolder.startsWith("/")) {
		String pathcont = ConfigSingleton.getRootPath();
		pathExportFolder = pathcont + "/" + pathExportFolder;
	    }
	    List id_paths = request.getAttributeAsList(ImportExportConstants.OBJECT_ID_PATHFUNCT);
	    List ids = extractObjId(id_paths);
	    String expClassName = (String) exporterSB.getAttribute("class");
	    Class expClass = Class.forName(expClassName);
	    expManager = (IExportManager) expClass.newInstance();
	    expManager.prepareExport(pathExportFolder, exportFileName, expSubObj, exportSnaps);
	    String exportedFilePath = "";
	    exportedFilePath = expManager.exportObjects(ids);
	    response.setAttribute(ImportExportConstants.EXPORT_FILE_PATH, exportedFilePath);
	} catch (EMFUserError emfue) {
	    expManager.cleanExportEnvironment();
	    throw emfue;
	} catch (Exception e) {
	    expManager.cleanExportEnvironment();
	    logger.error("Error while exporting ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    private List extractObjId(List requests) {
	logger.debug("IN");
	List toReturn = new ArrayList();
	Iterator iter = requests.iterator();
	while (iter.hasNext()) {
	    String id_path = (String) iter.next();
	    String id = id_path.substring(0, id_path.indexOf('_'));
	    toReturn.add(id);
	}
	logger.debug("OUT");
	return toReturn;
    }

    /**
     * Manages the request of the user to import contents of an exported archive
     * 
     * @param request
     *                Spago SourceBean request
     * @param response
     *                Spago SourceBean response
     * @throws EMFUserError
     */
    private void importConf(SourceBean request, SourceBean response) throws EMFUserError {
	logger.debug("IN");
	IImportManager impManager = null;
	// get exported file and eventually the associations file
	UploadedFile archive = null;
	UploadedFile associationsFile = null;
	List uplFiles = request.getAttributeAsList("UPLOADED_FILE");
	Iterator uplFilesIter = uplFiles.iterator();
	while (uplFilesIter.hasNext()) {
	    UploadedFile uplFile = (UploadedFile) uplFilesIter.next();
	    String nameInForm = uplFile.getFieldNameInForm();
	    if (nameInForm.equals("exportedArchive")) {
		archive = uplFile;
	    } else if (nameInForm.equals("associationsFile")) {
		associationsFile = uplFile;
	    }
	}
	// check that the name of the uploaded archive is not empty
	String archiveName = archive.getFileName();
	if (archiveName.trim().equals("")) {
	    logger.error("Missing exported file");
	    try {
		response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportLoopbackStopImport");
	    } catch (SourceBeanException e) {
		logger.error("Error while populating response source bean ", e);
		throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	    }
	    throw new EMFValidationError(EMFErrorSeverity.ERROR, "exportedArchive", "8007", "component_impexp_messages");
	}
	// check if the name of associations file is empty (in this case set
	// null to the variable)
	String associationsFileName = associationsFile.getFileName();
	if (associationsFileName.trim().equals("")) {
	    associationsFile = null;
	}
	// if the association file is empty then check if there is an
	// association id
	// rebuild the uploaded file and assign it to associationsFile
	if (associationsFile == null) {
	    String assId = (String) request.getAttribute("hidAssId");
	    if ((assId != null) && !assId.trim().equals("")) {
		IAssociationFileDAO assfiledao = new AssociationFileDAO();
		AssociationFile assFile = assfiledao.loadFromID(assId);
		byte[] content = assfiledao.getContent(assFile);
		UploadedFile uplFile = new UploadedFile();
		uplFile.setSizeInBytes(content.length);
		uplFile.setFileContent(content);
		uplFile.setFileName("association.xml");
		uplFile.setFieldNameInForm("");
		associationsFile = uplFile;
	    }
	}
	// get the association mode
	String assMode = IImportManager.IMPORT_ASS_DEFAULT_MODE;
	String assKindFromReq = (String) request.getAttribute("importAssociationKind");
	if (assKindFromReq.equalsIgnoreCase("predefinedassociations")) {
	    assMode = IImportManager.IMPORT_ASS_PREDEFINED_MODE;
	}
	// get bytes of the archive
	byte[] archiveBytes = archive.getFileContent();
	try {
	    // get path of the import tmp directory
	    ConfigSingleton conf = ConfigSingleton.getInstance();
	    SourceBean importerSB = (SourceBean) conf.getAttribute("IMPORTEXPORT.IMPORTER");
	    String pathImpTmpFolder = (String) importerSB.getAttribute("tmpFolder");
	    if (!pathImpTmpFolder.startsWith("/")) {
		String pathcont = ConfigSingleton.getRootPath();
		pathImpTmpFolder = pathcont + "/" + pathImpTmpFolder;
	    }
	    // apply transformation
	    TransformManager transManager = new TransformManager();
	    archiveBytes = transManager.applyTransformations(archiveBytes, archiveName, pathImpTmpFolder);

	    // instance the importer class
	    String impClassName = (String) importerSB.getAttribute("class");
	    Class impClass = Class.forName(impClassName);
	    impManager = (IImportManager) impClass.newInstance();
	    // prepare import environment
	    impManager.prepareImport(pathImpTmpFolder, archiveName, archiveBytes);

	    // if the associations file has been uploaded fill the association
	    // keeper
	    if (associationsFile != null) {
		byte[] assFilebys = associationsFile.getFileContent();
		String assFileStr = new String(assFilebys);
		impManager.getUserAssociation().fillFromXml(assFileStr);
	    }

	    // set into import manager the association import mode
	    impManager.setImpAssMode(assMode);

	    // start import operations
	    String exportVersion = impManager.getExportVersion();
	    String curVersion = impManager.getCurrentVersion();
	    List exportedRoles = impManager.getExportedRoles();
	    IRoleDAO roleDAO = DAOFactory.getRoleDAO();
	    List currentRoles = roleDAO.loadAllRoles();
	    try {
		RequestContainer requestContainer = this.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		session.setAttribute(ImportExportConstants.IMPORT_MANAGER, impManager);
		response.setAttribute(ImportExportConstants.LIST_EXPORTED_ROLES, exportedRoles);
		response.setAttribute(ImportExportConstants.LIST_CURRENT_ROLES, currentRoles);
		response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportRoleAssociation");
	    } catch (SourceBeanException sbe) {
		logger.error("Error while populating response source bean ", sbe);
		throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	    }
	} catch (EMFUserError emfue) {
	    if (impManager != null)
		impManager.stopImport();
	    throw emfue;
	} catch (ClassNotFoundException cnde) {
	    logger.error("Importer class not found", cnde);
	    if (impManager != null)
		impManager.stopImport();
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} catch (InstantiationException ie) {
	    logger.error("Cannot create an instance of importer class ", ie);
	    if (impManager != null)
		impManager.stopImport();
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} catch (IllegalAccessException iae) {
	    logger.error("Cannot create an instance of importer class ", iae);
	    if (impManager != null)
		impManager.stopImport();
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Manages the request of the user to associate some exported roles to the
     * roles of the portal in use
     * 
     * @param request
     *                Spago SourceBean request
     * @param response
     *                Spago SourceBean response
     * @throws EMFUserError
     */
    private void associateRoles(SourceBean request, SourceBean response) throws EMFUserError {
	logger.debug("IN");
	IImportManager impManager = null;
	try {
	    RequestContainer requestContainer = this.getRequestContainer();
	    SessionContainer session = requestContainer.getSessionContainer();
	    impManager = (IImportManager) session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
	    MetadataAssociations metaAss = impManager.getMetadataAssociation();
	    List expRoleIds = request.getAttributeAsList("expRole");
	    Iterator iterExpRoles = expRoleIds.iterator();
	    while (iterExpRoles.hasNext()) {
		String expRoleId = (String) iterExpRoles.next();
		String roleAssociateId = (String) request.getAttribute("roleAssociated" + expRoleId);
		if (!roleAssociateId.trim().equals("")) {
		    metaAss.insertCoupleRole(new Integer(expRoleId), new Integer(roleAssociateId));
		    // insert into user associations
		    try {
			Object existingRoleObj = impManager.getExistingObject(new Integer(roleAssociateId),
				SbiExtRoles.class);
			Object exportedRoleObj = impManager
				.getExportedObject(new Integer(expRoleId), SbiExtRoles.class);
			if ((existingRoleObj != null) && (exportedRoleObj != null)) {
			    SbiExtRoles existingRole = (SbiExtRoles) existingRoleObj;
			    SbiExtRoles exportedRole = (SbiExtRoles) exportedRoleObj;
			    UserAssociationsKeeper usrAssKeep = impManager.getUserAssociation();
			    String expRoleName = exportedRole.getName();
			    String exiRoleName = existingRole.getName();
			    usrAssKeep.recordRoleAssociation(expRoleName, exiRoleName);
			} else {
			    throw new Exception("hibernate object of existing or exported role not recovered");
			}
		    } catch (Exception e) {
			logger.error("Error while recording user role association", e);
		    }

		}
	    }
	    // check role associations
	    impManager.checkRoleReferences(metaAss.getRoleIDAssociation());
	    // get the existing and exported engines
	    List exportedEngines = impManager.getExportedEngines();
	    IEngineDAO engineDAO = DAOFactory.getEngineDAO();
	    List currentEngines = engineDAO.loadAllEngines();
	    try {
		response.setAttribute(ImportExportConstants.LIST_EXPORTED_ENGINES, exportedEngines);
		response.setAttribute(ImportExportConstants.LIST_CURRENT_ENGINES, currentEngines);
		response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportEngineAssociation");
	    } catch (SourceBeanException sbe) {
		logger.error("Error while populating response source bean ", sbe);
		throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	    }
	} catch (EMFUserError emfue) {
	    if (impManager != null)
		impManager.stopImport();
	    throw emfue;
	} catch (Exception e) {
	    logger.error("Error while getting role association ", e);
	    if (impManager != null)
		impManager.stopImport();
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Manages the request of the user to associate some exported engines to the
     * engines of the portal in use
     * 
     * @param request
     *                Spago SourceBean request
     * @param response
     *                Spago SourceBean response
     * @throws EMFUserError
     */
    private void associateEngines(SourceBean request, SourceBean response) throws EMFUserError {
	logger.debug("IN");
	IImportManager impManager = null;
	try {
	    RequestContainer requestContainer = this.getRequestContainer();
	    SessionContainer session = requestContainer.getSessionContainer();
	    impManager = (IImportManager) session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
	    MetadataAssociations metaAss = impManager.getMetadataAssociation();
	    List expEngineIds = request.getAttributeAsList("expEngine");
	    Iterator iterExpEngines = expEngineIds.iterator();
	    while (iterExpEngines.hasNext()) {
		String expEngineId = (String) iterExpEngines.next();
		String engineAssociateId = (String) request.getAttribute("engineAssociated" + expEngineId);
		if (!engineAssociateId.trim().equals("")) {
		    metaAss.insertCoupleEngine(new Integer(expEngineId), new Integer(engineAssociateId));

		    // insert into user associations
		    try {
			Object existingEngineObj = impManager.getExistingObject(new Integer(engineAssociateId),
				SbiEngines.class);
			Object exportedEngineObj = impManager.getExportedObject(new Integer(expEngineId),
				SbiEngines.class);
			if ((existingEngineObj != null) && (exportedEngineObj != null)) {
			    SbiEngines existingEngine = (SbiEngines) existingEngineObj;
			    SbiEngines exportedEngine = (SbiEngines) exportedEngineObj;
			    impManager.getUserAssociation().recordEngineAssociation(exportedEngine.getLabel(),
				    existingEngine.getLabel());
			} else {
			    throw new Exception("hibernate object of existing or exported engine not recovered");
			}
		    } catch (Exception e) {
			logger.error("Error while recording user engine association", e);
		    }

		}
	    }
	    List exportedConnection = impManager.getExportedConnections();
	    Map currentConnections = getCurrentConnectionInfo();
	    try {
		response.setAttribute(ImportExportConstants.LIST_EXPORTED_CONNECTIONS, exportedConnection);
		response.setAttribute(ImportExportConstants.MAP_CURRENT_CONNECTIONS, currentConnections);
		response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportConnectionAssociation");
	    } catch (SourceBeanException sbe) {
		logger.error("Error while populating response source bean ", sbe);
		throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	    }

	} catch (EMFUserError emfue) {
	    if (impManager != null)
		impManager.stopImport();
	    throw emfue;
	} catch (Exception e) {
	    logger.error("Error while getting engine association ", e);
	    if (impManager != null)
		impManager.stopImport();
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Manages the request of the user to associate some exported connections to
     * the connections of the portal in use
     * 
     * @param request
     *                Spago SourceBean request
     * @param response
     *                Spago SourceBean response
     * @throws EMFUserError
     */
    private void associateConnections(SourceBean request, SourceBean response) throws EMFUserError {
	logger.debug("IN");
	IImportManager impManager = null;
	try {
	    RequestContainer requestContainer = this.getRequestContainer();
	    SessionContainer session = requestContainer.getSessionContainer();
	    impManager = (IImportManager) session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
	    MetadataAssociations metaAss = impManager.getMetadataAssociation();
	    List expConnNameList = request.getAttributeAsList("expConn");
	    Iterator iterExpConn = expConnNameList.iterator();
	    while (iterExpConn.hasNext()) {
		String expConnName = (String) iterExpConn.next();
		String connNameAss = (String) request.getAttribute("connAssociated" + expConnName);
		if (!connNameAss.equals("")) {
		    metaAss.insertCoupleConnections(expConnName, connNameAss);
		    impManager.getUserAssociation().recordConnectionAssociation(expConnName, connNameAss);
		} else {
		    logger.error("Exported connection " + expConnName + " is not associate to a current "
			    + "system connection");
		    List exportedConnection = impManager.getExportedConnections();
		    Map currentConnections = getCurrentConnectionInfo();
		    response.setAttribute(ImportExportConstants.LIST_EXPORTED_CONNECTIONS, exportedConnection);
		    response.setAttribute(ImportExportConstants.MAP_CURRENT_CONNECTIONS, currentConnections);
		    response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportConnectionAssociation");
		    throw new EMFValidationError(EMFErrorSeverity.ERROR, "connAssociated" + expConnName,
			    "sbi.impexp.connNotAssociated");
		}
	    }
	    impManager.checkExistingMetadata();
	    if (metaAss.isEmpty()) {
		impManager.importObjects();
		ImportResultInfo iri = impManager.commitAllChanges();
		response.setAttribute(ImportExportConstants.IMPORT_RESULT_INFO, iri);
	    } else {
		try {
		    response.setAttribute(ImportExportConstants.PUBLISHER_NAME,
			    "ImportExportExistingMetadataAssociation");
		} catch (SourceBeanException sbe) {
		    logger.error("Error while populating response source bean ", sbe);
		    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004");
		}
	    }
	} catch (EMFValidationError emfve) {
	    throw emfve;
	} catch (EMFUserError emfue) {
	    if (impManager != null)
		impManager.stopImport();
	    throw emfue;
	} catch (SourceBeanException sbe) {
	    logger.error("Cannot populate response ", sbe);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} catch (Exception e) {
	    logger.error("Error while getting connection association ", e);
	    if (impManager != null)
		impManager.stopImport();
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8003", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}

    }

    /**
     * Manages the associations between the exported metadata and the one of the
     * portal in use
     * 
     * @param request
     *                Spago SourceBean request
     * @param response
     *                Spago SourceBean response
     * @throws EMFUserError
     */
    private void associateMetadata(SourceBean request, SourceBean response) throws EMFUserError {
	logger.debug("IN");
	IImportManager impManager = null;
	try {
	    RequestContainer requestContainer = this.getRequestContainer();
	    SessionContainer session = requestContainer.getSessionContainer();
	    impManager = (IImportManager) session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
	    MetadataAssociations metaAss = impManager.getMetadataAssociation();
	    impManager.importObjects();
	    ImportResultInfo iri = impManager.commitAllChanges();
	    response.setAttribute(ImportExportConstants.IMPORT_RESULT_INFO, iri);
	} catch (EMFUserError emfue) {
	    if (impManager != null)
		impManager.stopImport();
	    throw emfue;
	} catch (SourceBeanException sbe) {
	    logger.error("Cannot populate response ", sbe);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} catch (Exception e) {
	    if (impManager != null)
		impManager.stopImport();
	    logger.error("error after connection association ", e);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Manages the request of the user to exit from the import procedure
     * 
     * @param request
     *                Spago SourceBean request
     * @param response
     *                Spago SourceBean response
     * @throws EMFUserError
     */
    private void exitImport(SourceBean request, SourceBean response) throws EMFUserError {
	logger.debug("IN");
	RequestContainer requestContainer = this.getRequestContainer();
	SessionContainer session = requestContainer.getSessionContainer();
	IImportManager impManager = (IImportManager) session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
	impManager.stopImport();
	try {
	    response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportLoopbackStopImport");
	} catch (SourceBeanException sbe) {
	    logger.error("Error while populating response source bean ", sbe);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Manages the request of the user to go back from the engines association
     * to the roles association
     * 
     * @param request
     *                Spago SourceBean request
     * @param response
     *                Spago SourceBean response
     * @throws EMFUserError
     */
    private void backEngineAssociation(SourceBean request, SourceBean response) throws EMFUserError {
	logger.debug("IN");
	RequestContainer requestContainer = this.getRequestContainer();
	SessionContainer session = requestContainer.getSessionContainer();
	IImportManager impManager = (IImportManager) session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
	List exportedRoles = impManager.getExportedRoles();
	IRoleDAO roleDAO = DAOFactory.getRoleDAO();
	List currentRoles = roleDAO.loadAllRoles();
	try {
	    response.setAttribute(ImportExportConstants.LIST_EXPORTED_ROLES, exportedRoles);
	    response.setAttribute(ImportExportConstants.LIST_CURRENT_ROLES, currentRoles);
	    response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportRoleAssociation");
	} catch (SourceBeanException sbe) {
	    logger.error("Error while populating response source bean ", sbe);
	    impManager.stopImport();
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Manages the request of the user to go back from the connections
     * association to the engines association
     * 
     * @param request
     *                Spago SourceBean request
     * @param response
     *                Spago SourceBean response
     * @throws EMFUserError
     */
    private void backConnAssociation(SourceBean request, SourceBean response) throws EMFUserError {
	logger.debug("IN");
	RequestContainer requestContainer = this.getRequestContainer();
	SessionContainer session = requestContainer.getSessionContainer();
	IImportManager impManager = (IImportManager) session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
	List exportedEngines = impManager.getExportedEngines();
	IEngineDAO engineDAO = DAOFactory.getEngineDAO();
	List currentEngines = engineDAO.loadAllEngines();
	try {
	    response.setAttribute(ImportExportConstants.LIST_EXPORTED_ENGINES, exportedEngines);
	    response.setAttribute(ImportExportConstants.LIST_CURRENT_ENGINES, currentEngines);
	    response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportEngineAssociation");
	} catch (SourceBeanException sbe) {
	    logger.error("Error while populating response source bean ", sbe);
	    impManager.stopImport();
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Manages the request of the user to go back from the metadata association
     * to the connections association
     * 
     * @param request
     *                Spago SourceBean request
     * @param response
     *                Spago SourceBean response
     * @throws EMFUserError
     */
    private void backMetadataAssociation(SourceBean request, SourceBean response) throws EMFUserError {
	logger.debug("IN");
	RequestContainer requestContainer = this.getRequestContainer();
	SessionContainer session = requestContainer.getSessionContainer();
	IImportManager impManager = (IImportManager) session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
	List exportedConnection = impManager.getExportedConnections();
	Map currentConnections = getCurrentConnectionInfo();
	try {
	    response.setAttribute(ImportExportConstants.LIST_EXPORTED_CONNECTIONS, exportedConnection);
	    response.setAttribute(ImportExportConstants.MAP_CURRENT_CONNECTIONS, currentConnections);
	    response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportConnectionAssociation");
	} catch (SourceBeanException sbe) {
	    logger.error("Error while populating response source bean ", sbe);
	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
	} finally {
	    logger.debug("OUT");
	}
    }

    /**
     * Gather information about the connections defined into the current SpagoBI
     * platform.
     * 
     * @return Map A map containing the name of the connection pools as keys and
     *         their description as value
     */
    private Map getCurrentConnectionInfo() {
	logger.debug("IN");
	Map curConns = new HashMap();
	ConfigSingleton conf = ConfigSingleton.getInstance();
	List connList = conf.getAttributeAsList("DATA-ACCESS.CONNECTION-POOL");
	Iterator iterConn = connList.iterator();
	while (iterConn.hasNext()) {
	    SourceBean connSB = (SourceBean) iterConn.next();
	    String name = (String) connSB.getAttribute("connectionPoolName");
	    String descr = (String) connSB.getAttribute("connectionDescription");
	    if ((descr == null) || (descr.trim().equals("")))
		descr = name;
	    curConns.put(name, descr);
	}
	logger.debug("OUT");
	return curConns;
    }

}
