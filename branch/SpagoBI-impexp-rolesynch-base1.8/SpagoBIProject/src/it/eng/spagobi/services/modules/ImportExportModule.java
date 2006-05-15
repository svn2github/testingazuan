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
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IEngineDAO;
import it.eng.spagobi.bo.dao.IRoleDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.importexport.IExportManager;
import it.eng.spagobi.importexport.IImportManager;
import it.eng.spagobi.importexport.MetadataAssociations;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

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
		
		PortletRequest portletRequest = PortletUtilities.getPortletRequest();
		if (PortletFileUpload.isMultipartContent((ActionRequest)portletRequest)){
			request = PortletUtilities.getServiceRequestFromMultipartPortletRequest(portletRequest);
		}
		
		String message = (String) request.getAttribute("MESSAGEDET");
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "service",
				"begin of import / export service with message =" +message);
		EMFErrorHandler errorHandler = getErrorHandler();
		try{
			if(message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "service", 
						"The message parameter is null");
				errorHandler.addError(userError);
				throw userError;
			}
			if(message.trim().equalsIgnoreCase(SpagoBIConstants.EXPORT)) {
				exportConf(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.IMPORT)) {
				importConf(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.IMPEXP_ROLE_ASSOCIATION))	{
				associateRoles(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.IMPEXP_ENGINE_ASSOCIATION)) {
				associateEngines(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.IMPEXP_CONNECTION_ASSOCIATION)) {
				associateConnections(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.IMPEXP_METADATA_ASS)) {
				associateMetadata(request, response);
			}else if(message.trim().equalsIgnoreCase(SpagoBIConstants.IMPEXP_EXIT)) {
				exitImport(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.IMPEXP_BACK_ENGINE_ASS)) {
				backEngineAssociation(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.IMPEXP_BACK_CONN_ASS)) {
				backConnAssociation(request, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.IMPEXP_BACK_METADATA_ASS)) {
				backMetadataAssociation(request, response);
			}
		} catch (EMFUserError emfu) {
			errorHandler.addError(emfu);
		} catch (Exception ex) {
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "service", 
			"Error during the service execution" + ex);
			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 100);
			errorHandler.addError(error);
			return;
		}
	}
	
	
	private void exportConf(SourceBean request, SourceBean response) throws EMFUserError {
		IExportManager expManager = null;
		String exportFileName = (String)request.getAttribute("exportFileName");
		if((exportFileName==null) || (exportFileName.trim().equals(""))) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exportConf",
     							  "Missing name of the exported file");
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8006);
		}
		try{
			String exportSubObject = (String)request.getAttribute("exportSubObj");
			boolean expSubObj = false;
			if(exportSubObject!=null) {
				expSubObj = true;
			}
			ConfigSingleton conf = ConfigSingleton.getInstance();
			SourceBean exporterSB = (SourceBean)conf.getAttribute("IMPORTEXPORT.EXPORTER");
			String pathExportFolder = (String)exporterSB.getAttribute("exportFolder");
			if(!pathExportFolder.startsWith("/")){
				String pathcont = ConfigSingleton.getRootPath();
				pathExportFolder = pathcont + "/" + pathExportFolder;
			}
			List paths = request.getAttributeAsList(SpagoBIConstants.PATH);
			String expClassName = (String)exporterSB.getAttribute("class");
	        Class expClass = Class.forName(expClassName);
			expManager = (IExportManager)expClass.newInstance();
			expManager.prepareExport(pathExportFolder, exportFileName, expSubObj);
			String exportedFilePath = "";
			exportedFilePath = expManager.exportObjects(paths);
			response.setAttribute(SpagoBIConstants.EXPORT_FILE_PATH, exportedFilePath);	
		} catch (EMFUserError emfue) {
			expManager.cleanExportEnvironment();
			throw emfue;
		}catch (ClassNotFoundException cnde) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exportConf",
					"Exporter class not found" + cnde);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005);
		} catch (InstantiationException ie) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exportConf",
					"Cannot create an instance of exporter class " + ie);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005);
		} catch (IllegalAccessException iae) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exportConf",
					"Cannot create an instance of exporter class " + iae);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005);
		} catch (SourceBeanException sbe) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exportConf",
					"Cannot populate response " + sbe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8005);
		}
	}
	
	
	
	
	private void importConf(SourceBean request, SourceBean response) throws EMFUserError {
		IImportManager impManager = null;
		// get content and name of the uploaded archive
		UploadedFile archive = (UploadedFile)request.getAttribute("UPLOADED_FILE");
		String archiveName = archive.getFileName();
		if(archiveName.trim().equals("")){
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "importConf",
			  					   "Missing exported file");
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8007);
		}
		byte[]archiveBytes = archive.getFileContent();
		try{
			// get path of the import tmp directory
			ConfigSingleton conf = ConfigSingleton.getInstance();
			SourceBean importerSB = (SourceBean)conf.getAttribute("IMPORTEXPORT.IMPORTER");
			String pathImpTmpFolder = (String)importerSB.getAttribute("tmpFolder");
			if(!pathImpTmpFolder.startsWith("/")){
				String pathcont = ConfigSingleton.getRootPath();
				pathImpTmpFolder = pathcont + "/" + pathImpTmpFolder;
			}
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
				session.setAttribute(SpagoBIConstants.IMPORT_MANAGER, impManager);
				response.setAttribute(SpagoBIConstants.LIST_EXPORTED_ROLES, exportedRoles);
				response.setAttribute(SpagoBIConstants.LIST_CURRENT_ROLES, currentRoles);
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ImportExportRoleAssociation");
			} catch (SourceBeanException sbe) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "importConf",
						            "Error while populating response source bean " + sbe);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);	
			}
		} catch (EMFUserError emfue) {
			if(impManager!=null)
				impManager.stopImport();
			throw emfue;
		} catch (ClassNotFoundException cnde) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "importConf",
					"Importer class not found" + cnde);
			if(impManager!=null)
				impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		} catch (InstantiationException ie) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "importConf",
					"Cannot create an instance of importer class " + ie);
			if(impManager!=null)	
				impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		} catch (IllegalAccessException iae) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "importConf",
					"Cannot create an instance of importer class " + iae);
			if(impManager!=null)
				impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		}
	}
	

	
	private void associateRoles(SourceBean request, SourceBean response) throws EMFUserError {
		IImportManager impManager = null;
		try{
			RequestContainer requestContainer = this.getRequestContainer();
			SessionContainer session = requestContainer.getSessionContainer();
			impManager = (IImportManager)session.getAttribute(SpagoBIConstants.IMPORT_MANAGER);
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
				response.setAttribute(SpagoBIConstants.LIST_EXPORTED_ENGINES, exportedEngines);
				response.setAttribute(SpagoBIConstants.LIST_CURRENT_ENGINES, currentEngines);
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ImportExportEngineAssociation");
			} catch (SourceBeanException sbe) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "associateRoles",
						            "Error while populating response source bean " + sbe);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
			}
		} catch (EMFUserError emfue) {
			if(impManager!=null)	
				impManager.stopImport();
			throw emfue;
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "associateRoles",
		                        "Error while getting role association " + e);
			if(impManager!=null)	
				impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		}
	}
	
	
	
	
	private void associateEngines(SourceBean request, SourceBean response) throws EMFUserError {
		IImportManager impManager = null;
		try{
			RequestContainer requestContainer = this.getRequestContainer();
			SessionContainer session = requestContainer.getSessionContainer();
			impManager = (IImportManager)session.getAttribute(SpagoBIConstants.IMPORT_MANAGER);
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
				response.setAttribute(SpagoBIConstants.LIST_EXPORTED_CONNECTIONS, exportedConnection);
				response.setAttribute(SpagoBIConstants.MAP_CURRENT_CONNECTIONS, currentConnections);
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ImportExportConnectionAssociation");
			} catch (SourceBeanException sbe) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "associateEngines",
						            "Error while populating response source bean " + sbe);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
			}
			
		} catch (EMFUserError emfue) {
			if(impManager!=null)
				impManager.stopImport();
			throw emfue;
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "associateEngines",
		                        "Error while getting engine association " + e);
			if(impManager!=null)
				impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		}
	}
	
	
	
	
	private void associateConnections(SourceBean request, SourceBean response) throws EMFUserError {
		IImportManager impManager = null;
		try{
			RequestContainer requestContainer = this.getRequestContainer();
			SessionContainer session = requestContainer.getSessionContainer();
			impManager = (IImportManager)session.getAttribute(SpagoBIConstants.IMPORT_MANAGER);
			MetadataAssociations metaAss = impManager.getMetadataAssociation();
			List expConnNameList = request.getAttributeAsList("expConn");
			Iterator iterExpConn = expConnNameList.iterator();
			while(iterExpConn.hasNext()){
				String expConnName= (String)iterExpConn.next();
				String connNameAss = (String)request.getAttribute("connAssociated"+ expConnName);
				if(!connNameAss.equals("")) {
					metaAss.insertCoupleConnections(expConnName, connNameAss);
				} else {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "associateConnections",
	                        			"Exported connection " +expConnName+" is not associate to a current " +
	                        			"system connection");
					throw new EMFUserError(EMFErrorSeverity.ERROR, 8002);
				}
			}
			impManager.checkExistingMetadata();
			if(metaAss.isEmpty()) {
				impManager.importObjects();
				impManager.commitAllChanges(); 
			} else {
				try{
					response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ImportExportExistingMetadataAssociation");
				} catch (SourceBeanException sbe) {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "associateConnections",
							            "Error while populating response source bean " + sbe);
					throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
				}
			}
		} catch (EMFUserError emfue) {
			if(impManager!=null)
				impManager.stopImport();
			throw emfue; 
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "associateConnections",
                    			"Error while getting connection association " + e);
			if(impManager!=null)
				impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8003);
		}
		
	}
	
	
	
	
	private void associateMetadata(SourceBean request, SourceBean response) throws EMFUserError {
		IImportManager impManager = null;
		try{
			RequestContainer requestContainer = this.getRequestContainer();
			SessionContainer session = requestContainer.getSessionContainer();
			impManager = (IImportManager)session.getAttribute(SpagoBIConstants.IMPORT_MANAGER);
			MetadataAssociations metaAss = impManager.getMetadataAssociation();
			impManager.importObjects();
			impManager.commitAllChanges();
		} catch (EMFUserError emfue) {
			if(impManager!=null)
				impManager.stopImport();
			throw emfue; 
		}	
	}
	
	
	
	
	
	
	private void exitImport(SourceBean request, SourceBean response) throws EMFUserError {
		RequestContainer requestContainer = this.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		IImportManager impManager = (IImportManager)session.getAttribute(SpagoBIConstants.IMPORT_MANAGER);
		impManager.stopImport();
		try{
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ImportExportLoopbackStopImport");
		} catch (SourceBeanException sbe) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exitImport",
					            "Error while populating response source bean " + sbe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		}
	}
	
	
	
	private void backEngineAssociation(SourceBean request, SourceBean response) throws EMFUserError {
		RequestContainer requestContainer = this.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		IImportManager impManager = (IImportManager)session.getAttribute(SpagoBIConstants.IMPORT_MANAGER);
		List exportedRoles = impManager.getExportedRoles();
		IRoleDAO roleDAO = DAOFactory.getRoleDAO();
		List currentRoles = roleDAO.loadAllRoles();
		try{
			response.setAttribute(SpagoBIConstants.LIST_EXPORTED_ROLES, exportedRoles);
			response.setAttribute(SpagoBIConstants.LIST_CURRENT_ROLES, currentRoles);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ImportExportRoleAssociation");
		} catch (SourceBeanException sbe) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "backEngineAssociation",
					            "Error while populating response source bean " + sbe);
			impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		}
	}
	
	
	
	private void backConnAssociation(SourceBean request, SourceBean response) throws EMFUserError {
		RequestContainer requestContainer = this.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		IImportManager impManager = (IImportManager)session.getAttribute(SpagoBIConstants.IMPORT_MANAGER);
		List exportedEngines = impManager.getExportedEngines();
		IEngineDAO engineDAO = DAOFactory.getEngineDAO();
		List currentEngines = engineDAO.loadAllEngines();
		try{
			response.setAttribute(SpagoBIConstants.LIST_EXPORTED_ENGINES, exportedEngines);
			response.setAttribute(SpagoBIConstants.LIST_CURRENT_ENGINES, currentEngines);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ImportExportEngineAssociation");
		} catch (SourceBeanException sbe) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "backConnAssociation",
					            "Error while populating response source bean " + sbe);
			impManager.stopImport();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		}
	}
	
	
	
	private void backMetadataAssociation(SourceBean request, SourceBean response) throws EMFUserError {
		RequestContainer requestContainer = this.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		IImportManager impManager = (IImportManager)session.getAttribute(SpagoBIConstants.IMPORT_MANAGER);
		List exportedConnection = impManager.getExportedConnections();
		Map currentConnections = getCurrentConnectionInfo();
		try{
			response.setAttribute(SpagoBIConstants.LIST_EXPORTED_CONNECTIONS, exportedConnection);
			response.setAttribute(SpagoBIConstants.MAP_CURRENT_CONNECTIONS, currentConnections);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ImportExportConnectionAssociation");
		} catch (SourceBeanException sbe) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "backMetadataAssociation",
					            "Error while populating response source bean " + sbe);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 8004);
		}
	}
	
	
	
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
