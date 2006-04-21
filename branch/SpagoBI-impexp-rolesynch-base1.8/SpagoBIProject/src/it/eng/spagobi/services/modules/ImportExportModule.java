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

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IEngineDAO;
import it.eng.spagobi.bo.dao.IRoleDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.importexport.IExportManager;
import it.eng.spagobi.importexport.IImportManager;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class implements a module which  handles the import / export operations
 */
public class ImportExportModule extends AbstractModule {
	
	private static IImportManager impManager = null; 
	
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
			}
		} catch (EMFUserError emfu) {
			errorHandler.addError(emfu);
		} catch (Exception ex) {
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "service", 
			"Error during the service execution" + ex);
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	}
	
	
	private void exportConf(SourceBean request, SourceBean response) throws EMFUserError, EMFInternalError {
		try{
			//TODO get from the request the name of the expor file
			String exportFileName = "provaexport";
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
			IExportManager expManager = (IExportManager)expClass.newInstance();
			expManager.prepareExport(pathExportFolder, exportFileName);
			expManager.exportObjects(paths);
		} catch (ClassNotFoundException cnde) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exportConf",
					"Exporter class not found" + cnde);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (InstantiationException ie) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exportConf",
					"Cannot create an instance of exporter class " + ie);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (IllegalAccessException iae) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "exportConf",
					"Cannot create an instance of exporter class " + iae);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	private void importConf(SourceBean request, SourceBean response) throws EMFUserError, EMFInternalError {
		try{
			//************************** TO CHANGE START
			ConfigSingleton conf = ConfigSingleton.getInstance();
			SourceBean exporterSB = (SourceBean)conf.getAttribute("IMPORTEXPORT.EXPORTER");
			String pathExportFolder = (String)exporterSB.getAttribute("exportFolder");
			if(!pathExportFolder.startsWith("/")){
				String pathcont = ConfigSingleton.getRootPath();
				pathExportFolder = pathcont + "/" + pathExportFolder;
			}
			String pathArchiveFile = pathExportFolder + "/provaexport.zip"; 
			//************************** TO CHANGE END
			SourceBean importerSB = (SourceBean)conf.getAttribute("IMPORTEXPORT.IMPORTER");
			String pathImpTmpFolder = (String)importerSB.getAttribute("tmpFolder");
			if(!pathImpTmpFolder.startsWith("/")){
				String pathcont = ConfigSingleton.getRootPath();
				pathImpTmpFolder = pathcont + "/" + pathImpTmpFolder;
			}		
			String impClassName = (String)importerSB.getAttribute("class");
	        Class impClass = Class.forName(impClassName);
			impManager = (IImportManager)impClass.newInstance();
			impManager.prepareImport(pathImpTmpFolder, pathArchiveFile);
			String exportVersion = impManager.getExportVersion();
			String curVersion = impManager.getCurrentVersion();
			List exportedRoles = impManager.getExportedRoles();
			
			IRoleDAO roleDAO = DAOFactory.getRoleDAO();
			List currentRoles = roleDAO.loadAllRoles();
			try{
				response.setAttribute(SpagoBIConstants.LIST_EXPORTED_ROLES, exportedRoles);
				response.setAttribute(SpagoBIConstants.LIST_CURRENT_ROLES, currentRoles);
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ImportExportRoleAssociation");
			} catch (SourceBeanException sbe) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "importConf",
						            "Error while populating response source bean " + sbe);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
		
		} catch (ClassNotFoundException cnde) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "importConf",
					"Importer class not found" + cnde);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (InstantiationException ie) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "importConf",
					"Cannot create an instance of importer class " + ie);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (IllegalAccessException iae) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "importConf",
					"Cannot create an instance of importer class " + iae);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	

	
	private void associateRoles(SourceBean request, SourceBean response) throws EMFUserError, EMFInternalError {
		try{
			Map associations = new HashMap();
			List expRoleIds = request.getAttributeAsList("expRole");
			Iterator iterExpRoles = expRoleIds.iterator();
			while(iterExpRoles.hasNext()){
				String expRoleId = (String)iterExpRoles.next();
				String roleAssociateId = (String)request.getAttribute("roleAssociated"+expRoleId);
				if(!roleAssociateId.trim().equals(""))
					associations.put(expRoleId, roleAssociateId);
			}
			impManager.updateRoleReferences(associations);
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
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			
		} catch (EMFUserError emfue) {
			throw emfue;
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "associateRoles",
		                        "Error while getting role association " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	private void associateEngines(SourceBean request, SourceBean response) throws EMFUserError, EMFInternalError {
		try{
			Map associations = new HashMap();
			List expEngineIds = request.getAttributeAsList("expEngine");
			Iterator iterExpEngines = expEngineIds.iterator();
			while(iterExpEngines.hasNext()){
				String expEngineId = (String)iterExpEngines.next();
				String engineAssociateId = (String)request.getAttribute("engineAssociated"+expEngineId);
				if(!engineAssociateId.trim().equals(""))
					associations.put(expEngineId, engineAssociateId);
			}
			impManager.updateEngineReferences(associations);
            // get exported connections 
			// get current connections
			// set into response exported and current connection
			try{
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ImportExportConnectionAssociation");
			} catch (SourceBeanException sbe) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "associateEngines",
						            "Error while populating response source bean " + sbe);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			
		} catch (EMFUserError emfue) {
			throw emfue;
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "associateEngines",
		                        "Error while getting engine association " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	
	
	private void associateConnections(SourceBean request, SourceBean response) throws EMFUserError, EMFInternalError {
		try{
			impManager.importObjects();
			impManager.commitAllChanges();
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "associateConnections",
		                        "Error while getting connection association " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
}
