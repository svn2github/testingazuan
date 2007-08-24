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

package it.eng.spagobi.services.modules;

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
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IEngineDAO;
import it.eng.spagobi.bo.dao.IRoleDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.importexport.IExportManager;
import it.eng.spagobi.importexport.IImportManager;
import it.eng.spagobi.importexport.ImportExportConstants;
import it.eng.spagobi.importexport.ImportResultInfo;
import it.eng.spagobi.importexport.MetadataAssociations;
import it.eng.spagobi.importexport.TransformManager;
import it.eng.spagobi.utilities.ChannelUtilities;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;

import org.apache.commons.fileupload.portlet.PortletFileUpload;

/**
 * This class implements a module which  handles the import / export operations
 */
public class ImportExportModule extends AbstractModule {
	
    /**
     * Initialize the module 
     * @param config Configuration sourcebean of the module
     */
	public void init(SourceBean config) {
	}

	/**
	 * Reads the operation asked by the user and calls the export or import methods
	 * @param request The Source Bean containing all request parameters
	 * @param response The Source Bean containing all response parameters
	 * @throws exception If an exception occurs
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		if(ChannelUtilities.isPortletRunning()){
			if(PortletUtilities.isMultipartRequest()) {
				request = ChannelUtilities.getSpagoRequestFromMultipart();
			}
		}
		
		String message = (String) request.getAttribute("MESSAGEDET");
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "service",
				"begin of import / export service with message =" +message);
		EMFErrorHandler errorHandler = getErrorHandler();
		try{
			if(message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, "101", "component_impexp_messages");
				SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "service", 
						"The message parameter is null");
				errorHandler.addError(userError);
				throw userError;
			}
			if(message.trim().equalsIgnoreCase(ImportExportConstants.EXPORT)) {
				exportConf(request, response);
			} else if(message.trim().equalsIgnoreCase(ImportExportConstants.IMPORT)) {
				importConf(request, response);
			} else if(message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_ROLE_ASSOCIATION))	{
				associateRoles(request, response);
			} else if(message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_ENGINE_ASSOCIATION)) {
				associateEngines(request, response);
			} else if(message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_CONNECTION_ASSOCIATION)) {
				associateConnections(request, response);
			} else if(message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_METADATA_ASS)) {
				associateMetadata(request, response);
			}else if(message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_EXIT)) {
				exitImport(request, response);
			} else if(message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_BACK_ENGINE_ASS)) {
				backEngineAssociation(request, response);
			} else if(message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_BACK_CONN_ASS)) {
				backConnAssociation(request, response);
			} else if(message.trim().equalsIgnoreCase(ImportExportConstants.IMPEXP_BACK_METADATA_ASS)) {
				backMetadataAssociation(request, response);
			}
		} catch (EMFUserError emfu) {
			errorHandler.addError(emfu);
		} catch (Exception ex) {
			SpagoBITracer.debug(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "service", 
			"Error during the service execution" + ex);
			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, "100", "component_impexp_messages");
			errorHandler.addError(error);
			return;
		}
	}
	
	/**
	 * Manages the request of the user to export some selected objects
	 * @param request Spago SourceBean request 
	 * @param response Spago SourceBean response
	 * @throws EMFUserError
	 */
	private void exportConf(SourceBean request, SourceBean response) throws EMFUserError {
		IExportManager expManager = null;
		String exportFileName = (String)request.getAttribute("exportFileName");
		if((exportFileName==null) || (exportFileName.trim().equals(""))) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "exportConf",
     							  "Missing name of the exported file");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8006", "component_impexp_messages");
		}
		try{
			String exportSubObject = (String)request.getAttribute("exportSubObj");
			boolean expSubObj = false;
			if(exportSubObject!=null) {
				expSubObj = true;
			}
			String exportSnapshots = (String)request.getAttribute("exportSnapshots");
			boolean exportSnaps = false;
			if(exportSnapshots!=null) {
				exportSnaps = true;
			}
			ConfigSingleton conf = ConfigSingleton.getInstance();
			SourceBean exporterSB = (SourceBean)conf.getAttribute("IMPORTEXPORT.EXPORTER");
			String pathExportFolder = (String)exporterSB.getAttribute("exportFolder");
			if(!pathExportFolder.startsWith("/")){
				String pathcont = ConfigSingleton.getRootPath();
				pathExportFolder = pathcont + "/" + pathExportFolder;
			}
			List id_paths = request.getAttributeAsList(ImportExportConstants.OBJECT_ID_PATHFUNCT);
			List ids = extractObjId(id_paths);
			String expClassName = (String)exporterSB.getAttribute("class");
	        Class expClass = Class.forName(expClassName);
			expManager = (IExportManager)expClass.newInstance();
			expManager.prepareExport(pathExportFolder, exportFileName, expSubObj, exportSnaps);
			String exportedFilePath = "";
			exportedFilePath = expManager.exportObjects(ids);
			response.setAttribute(ImportExportConstants.EXPORT_FILE_PATH, exportedFilePath);	
		} catch (EMFUserError emfue) {
			expManager.cleanExportEnvironment();
			throw emfue;
		} catch (Exception e) {
			expManager.cleanExportEnvironment();
			SpagoBITracer.warning(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "exportConf",
								 "Error while exporting " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		} 
	}
	
	private List extractObjId(List requests) {
		List toReturn = new ArrayList();
		Iterator iter = requests.iterator();
		while(iter.hasNext()) {
			String id_path = (String)iter.next();
			String id = id_path.substring(0, id_path.indexOf('_'));
			toReturn.add(id);
		}
		return toReturn;
	}
	
	
	/**
	 * Manages the request of the user to import contents of an exported archive
	 * @param request Spago SourceBean request 
	 * @param response Spago SourceBean response
	 * @throws EMFUserError
	 */
	private void importConf(SourceBean request, SourceBean response) throws EMFUserError {
		IImportManager impManager = null;
		// get content and name of the uploaded archive
		UploadedFile archive = (UploadedFile)request.getAttribute("UPLOADED_FILE");
		String archiveName = archive.getFileName();
		if(archiveName.trim().equals("")){
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "importConf",
			  					   "Missing exported file");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8007", "component_impexp_messages");
		}
		byte[] archiveBytes = archive.getFileContent();
		try{
			// get path of the import tmp directory
			ConfigSingleton conf = ConfigSingleton.getInstance();
			SourceBean importerSB = (SourceBean)conf.getAttribute("IMPORTEXPORT.IMPORTER");
			String pathImpTmpFolder = (String)importerSB.getAttribute("tmpFolder");
			if(!pathImpTmpFolder.startsWith("/")){
				String pathcont = ConfigSingleton.getRootPath();
				pathImpTmpFolder = pathcont + "/" + pathImpTmpFolder;
			}
			// apply transformation
			TransformManager transManager = new TransformManager();
			archiveBytes = transManager.applyTransformations(archiveBytes, archiveName, pathImpTmpFolder);
			
			
			// instance the importer class
			String impClassName = (String)importerSB.getAttribute("class");
	        Class impClass = Class.forName(impClassName);
			impManager = (IImportManager)impClass.newInstance();
			// prepare import environment
			impManager.prepareImport(pathImpTmpFolder, archiveName, archiveBytes);
			// start import operations
			String exportVersion = impManager.getExportVersion();
			String curVersion = impManager.getCurrentVersion();
			List exportedRoles = impManager.getExportedRoles();
			IRoleDAO roleDAO = DAOFactory.getRoleDAO();
			List currentRoles = roleDAO.loadAllRoles();
			try{
				RequestContainer requestContainer = this.getRequestContainer();
				SessionContainer session = requestContainer.getSessionContainer();
				session.setAttribute(ImportExportConstants.IMPORT_MANAGER, impManager);
				response.setAttribute(ImportExportConstants.LIST_EXPORTED_ROLES, exportedRoles);
				response.setAttribute(ImportExportConstants.LIST_CURRENT_ROLES, currentRoles);
				response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportRoleAssociation");
			} catch (SourceBeanException sbe) {
				SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "importConf",
						            "Error while populating response source bean " + sbe);
				throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");	
			}
		} catch (EMFUserError emfue) {
			if(impManager!=null)
				impManager.stopImport();
			throw emfue;
		} catch (ClassNotFoundException cnde) {
			SpagoBITracer.warning(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "importConf",
					"Importer class not found" + cnde);
			if(impManager!=null)
				impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		} catch (InstantiationException ie) {
			SpagoBITracer.warning(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "importConf",
					"Cannot create an instance of importer class " + ie);
			if(impManager!=null)	
				impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		} catch (IllegalAccessException iae) {
			SpagoBITracer.warning(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "importConf",
					"Cannot create an instance of importer class " + iae);
			if(impManager!=null)
				impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		}
	}
	

	/**
	 * Manages the request of the user to associate some exported roles 
	 * to the roles of the portal in use
	 * @param request Spago SourceBean request 
	 * @param response Spago SourceBean response
	 * @throws EMFUserError
	 */
	private void associateRoles(SourceBean request, SourceBean response) throws EMFUserError {
		IImportManager impManager = null;
		try{
			RequestContainer requestContainer = this.getRequestContainer();
			SessionContainer session = requestContainer.getSessionContainer();
			impManager = (IImportManager)session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
			MetadataAssociations metaAss = impManager.getMetadataAssociation();
			List expRoleIds = request.getAttributeAsList("expRole");
			Iterator iterExpRoles = expRoleIds.iterator();
			while(iterExpRoles.hasNext()){
				String expRoleId = (String)iterExpRoles.next();
				String roleAssociateId = (String)request.getAttribute("roleAssociated"+expRoleId);
				if(!roleAssociateId.trim().equals(""))
					metaAss.insertCoupleRole(new Integer(expRoleId), new Integer(roleAssociateId));
			}
            List exportedEngines = impManager.getExportedEngines();
			IEngineDAO engineDAO = DAOFactory.getEngineDAO();
			List currentEngines = engineDAO.loadAllEngines();
			try{
				response.setAttribute(ImportExportConstants.LIST_EXPORTED_ENGINES, exportedEngines);
				response.setAttribute(ImportExportConstants.LIST_CURRENT_ENGINES, currentEngines);
				response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportEngineAssociation");
			} catch (SourceBeanException sbe) {
				SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "associateRoles",
						            "Error while populating response source bean " + sbe);
				throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
			}
		} catch (EMFUserError emfue) {
			if(impManager!=null)	
				impManager.stopImport();
			throw emfue;
		} catch (Exception e) {
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "associateRoles",
		                        "Error while getting role association " + e);
			if(impManager!=null)	
				impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		}
	}
	
	
	
	/**
	 * Manages the request of the user to associate some exported engines 
	 * to the engines of the portal in use
	 * @param request Spago SourceBean request 
	 * @param response Spago SourceBean response
	 * @throws EMFUserError
	 */
	private void associateEngines(SourceBean request, SourceBean response) throws EMFUserError {
		IImportManager impManager = null;
		try{
			RequestContainer requestContainer = this.getRequestContainer();
			SessionContainer session = requestContainer.getSessionContainer();
			impManager = (IImportManager)session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
			MetadataAssociations metaAss = impManager.getMetadataAssociation();
			List expEngineIds = request.getAttributeAsList("expEngine");
			Iterator iterExpEngines = expEngineIds.iterator();
			while(iterExpEngines.hasNext()){
				String expEngineId = (String)iterExpEngines.next();
				String engineAssociateId = (String)request.getAttribute("engineAssociated"+expEngineId);
				if(!engineAssociateId.trim().equals(""))
					metaAss.insertCoupleEngine(new Integer(expEngineId), new Integer(engineAssociateId));
			}
			List exportedConnection = impManager.getExportedConnections();
			Map currentConnections = getCurrentConnectionInfo();
			try{
				response.setAttribute(ImportExportConstants.LIST_EXPORTED_CONNECTIONS, exportedConnection);
				response.setAttribute(ImportExportConstants.MAP_CURRENT_CONNECTIONS, currentConnections);
				response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportConnectionAssociation");
			} catch (SourceBeanException sbe) {
				SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "associateEngines",
						            "Error while populating response source bean " + sbe);
				throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
			}
			
		} catch (EMFUserError emfue) {
			if(impManager!=null)
				impManager.stopImport();
			throw emfue;
		} catch (Exception e) {
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "associateEngines",
		                        "Error while getting engine association " + e);
			if(impManager!=null)
				impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		}
	}
	
	
	
	/**
	 * Manages the request of the user to associate some exported connections 
	 * to the connections of the portal in use
	 * @param request Spago SourceBean request 
	 * @param response Spago SourceBean response
	 * @throws EMFUserError
	 */
	private void associateConnections(SourceBean request, SourceBean response) throws EMFUserError {
		IImportManager impManager = null;
		try{
			RequestContainer requestContainer = this.getRequestContainer();
			SessionContainer session = requestContainer.getSessionContainer();
			impManager = (IImportManager)session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
			MetadataAssociations metaAss = impManager.getMetadataAssociation();
			List expConnNameList = request.getAttributeAsList("expConn");
			Iterator iterExpConn = expConnNameList.iterator();
			while(iterExpConn.hasNext()){
				String expConnName= (String)iterExpConn.next();
				String connNameAss = (String)request.getAttribute("connAssociated"+ expConnName);
				if(!connNameAss.equals("")) {
					metaAss.insertCoupleConnections(expConnName, connNameAss);
				} else {
					SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "associateConnections",
	                        			"Exported connection " +expConnName+" is not associate to a current " +
	                        			"system connection");
					List exportedConnection = impManager.getExportedConnections();
					Map currentConnections = getCurrentConnectionInfo();
					response.setAttribute(ImportExportConstants.LIST_EXPORTED_CONNECTIONS, exportedConnection);
					response.setAttribute(ImportExportConstants.MAP_CURRENT_CONNECTIONS, currentConnections);
					response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportConnectionAssociation");
					throw new EMFValidationError(EMFErrorSeverity.ERROR, "connAssociated"+ expConnName, "sbi.impexp.connNotAssociated");
				}
			}
			impManager.checkExistingMetadata();
			if(metaAss.isEmpty()) {
				impManager.importObjects();
				ImportResultInfo iri = impManager.commitAllChanges(); 
				response.setAttribute(ImportExportConstants.IMPORT_RESULT_INFO, iri);	
			} else {
				try{
					response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportExistingMetadataAssociation");
				} catch (SourceBeanException sbe) {
					SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "associateConnections",
							            "Error while populating response source bean " + sbe);
					throw new EMFUserError(EMFErrorSeverity.ERROR, "8004");
				}
			}
		} catch (EMFValidationError emfve) {
			throw emfve; 
		} catch (EMFUserError emfue) {
			if(impManager!=null)
				impManager.stopImport();
			throw emfue; 
		} catch (SourceBeanException sbe) {
			SpagoBITracer.warning(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "associateConnections",
					"Cannot populate response " + sbe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		} catch (Exception e) {
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "associateConnections",
                    			"Error while getting connection association " + e);
			if(impManager!=null)
				impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8003", "component_impexp_messages");
		}
		
	}
	
	
	
	/**
	 * Manages the associations between the exported metadata and the one of the portal in use
	 * @param request Spago SourceBean request 
	 * @param response Spago SourceBean response
	 * @throws EMFUserError
	 */
	private void associateMetadata(SourceBean request, SourceBean response) throws EMFUserError {
		IImportManager impManager = null;
		try{
			RequestContainer requestContainer = this.getRequestContainer();
			SessionContainer session = requestContainer.getSessionContainer();
			impManager = (IImportManager)session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
			MetadataAssociations metaAss = impManager.getMetadataAssociation();
			impManager.importObjects();
			ImportResultInfo iri = impManager.commitAllChanges();
			response.setAttribute(ImportExportConstants.IMPORT_RESULT_INFO, iri);	
		} catch (EMFUserError emfue) {
			if(impManager!=null)
				impManager.stopImport();
			throw emfue; 
		} catch (SourceBeanException sbe) {
			SpagoBITracer.warning(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "associateMetadata",
					"Cannot populate response " + sbe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		} catch (Exception e) {
			if(impManager!=null)
				impManager.stopImport();
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "associateMetadata",
					"error after connection association " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		}	
	}
	
	
	
	
	
	/**
	 * Manages the request of the user to exit from the import procedure
	 * @param request Spago SourceBean request 
	 * @param response Spago SourceBean response
	 * @throws EMFUserError
	 */
	private void exitImport(SourceBean request, SourceBean response) throws EMFUserError {
		RequestContainer requestContainer = this.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		IImportManager impManager = (IImportManager)session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
		impManager.stopImport();
		try{
			response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportLoopbackStopImport");
		} catch (SourceBeanException sbe) {
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "exitImport",
					            "Error while populating response source bean " + sbe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		}
	}
	
	
	/**
	 * Manages the request of the user to go back from the engines association to the roles association
	 * @param request Spago SourceBean request 
	 * @param response Spago SourceBean response
	 * @throws EMFUserError
	 */
	private void backEngineAssociation(SourceBean request, SourceBean response) throws EMFUserError {
		RequestContainer requestContainer = this.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		IImportManager impManager = (IImportManager)session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
		List exportedRoles = impManager.getExportedRoles();
		IRoleDAO roleDAO = DAOFactory.getRoleDAO();
		List currentRoles = roleDAO.loadAllRoles();
		try{
			response.setAttribute(ImportExportConstants.LIST_EXPORTED_ROLES, exportedRoles);
			response.setAttribute(ImportExportConstants.LIST_CURRENT_ROLES, currentRoles);
			response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportRoleAssociation");
		} catch (SourceBeanException sbe) {
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "backEngineAssociation",
					            "Error while populating response source bean " + sbe);
			impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		}
	}
	
	
	/**
	 * Manages the request of the user to go back from the connections association 
	 * to the engines association
	 * @param request Spago SourceBean request 
	 * @param response Spago SourceBean response
	 * @throws EMFUserError
	 */
	private void backConnAssociation(SourceBean request, SourceBean response) throws EMFUserError {
		RequestContainer requestContainer = this.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		IImportManager impManager = (IImportManager)session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
		List exportedEngines = impManager.getExportedEngines();
		IEngineDAO engineDAO = DAOFactory.getEngineDAO();
		List currentEngines = engineDAO.loadAllEngines();
		try{
			response.setAttribute(ImportExportConstants.LIST_EXPORTED_ENGINES, exportedEngines);
			response.setAttribute(ImportExportConstants.LIST_CURRENT_ENGINES, currentEngines);
			response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportEngineAssociation");
		} catch (SourceBeanException sbe) {
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "backConnAssociation",
					            "Error while populating response source bean " + sbe);
			impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		}
	}
	
	
	
	/**
	 * Manages the request of the user to go back from the metadata association 
	 * to the connections association
	 * @param request Spago SourceBean request 
	 * @param response Spago SourceBean response
	 * @throws EMFUserError
	 */
	private void backMetadataAssociation(SourceBean request, SourceBean response) throws EMFUserError {
		RequestContainer requestContainer = this.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		IImportManager impManager = (IImportManager)session.getAttribute(ImportExportConstants.IMPORT_MANAGER);
		List exportedConnection = impManager.getExportedConnections();
		Map currentConnections = getCurrentConnectionInfo();
		try{
			response.setAttribute(ImportExportConstants.LIST_EXPORTED_CONNECTIONS, exportedConnection);
			response.setAttribute(ImportExportConstants.MAP_CURRENT_CONNECTIONS, currentConnections);
			response.setAttribute(ImportExportConstants.PUBLISHER_NAME, "ImportExportConnectionAssociation");
		} catch (SourceBeanException sbe) {
			SpagoBITracer.major(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "backMetadataAssociation",
					            "Error while populating response source bean " + sbe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8004", "component_impexp_messages");
		}
	}
	
	
	
	
	/**
	 * Gather information about the connections defined into the current SpagoBI platform.
	 * @return Map A map containing the name of the connection pools as keys and 
	 * their description as value
	 */
	private Map getCurrentConnectionInfo() {
		Map curConns = new HashMap();
		ConfigSingleton conf = ConfigSingleton.getInstance();
		List connList = conf.getAttributeAsList("DATA-ACCESS.CONNECTION-POOL");
		Iterator iterConn = connList.iterator();
		while(iterConn.hasNext()) {
			SourceBean connSB = (SourceBean)iterConn.next();
			String name = (String)connSB.getAttribute("connectionPoolName");
			String descr = (String)connSB.getAttribute("connectionDescription");
			if((descr==null)||(descr.trim().equals("")))
				descr=name;
			curConns.put(name, descr);
		}
	   return curConns;
	}
	
	
}
