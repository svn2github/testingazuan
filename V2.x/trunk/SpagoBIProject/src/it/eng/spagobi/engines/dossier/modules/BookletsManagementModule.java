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
package it.eng.spagobi.engines.dossier.modules;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorCategory;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IBIObjectParameterDAO;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IRoleDAO;
import it.eng.spagobi.commons.utilities.PortletUtilities;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.commons.utilities.UploadedFile;
import it.eng.spagobi.engines.dossier.bo.ConfiguredBIDocument;
import it.eng.spagobi.engines.dossier.bo.WorkflowConfiguration;
import it.eng.spagobi.engines.dossier.constants.BookletsConstants;
import it.eng.spagobi.engines.dossier.dao.BookletsCmsDaoImpl;
import it.eng.spagobi.engines.dossier.dao.DossierDAOHibImpl;
import it.eng.spagobi.engines.dossier.dao.IDossierDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

/**
 * This class implements a module which  handles pamphlets management.
 */
public class BookletsManagementModule extends AbstractModule {
	
	static private Logger logger = Logger.getLogger(BookletsManagementModule.class);

	public void init(SourceBean config) {
	}

	/**
	 * Reads the operation asked by the user and calls the right operation handler
	 * @param request The Source Bean containing all request parameters
	 * @param response The Source Bean containing all response parameters
	 * @throws exception If an exception occurs
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		EMFErrorHandler errorHandler = getErrorHandler();
		String operation = (String) request.getAttribute(SpagoBIConstants.OPERATION);
		logger.debug("Begin of detail Engine modify/visualization service with operation =" +operation);
		try{
			if((operation==null)||(operation.trim().equals(""))) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, "100", "component_pamphlets_messages");
				logger.error("The operation parameter is null");
				throw userError;
			} else if(operation.equalsIgnoreCase(SpagoBIConstants.NEW_DOCUMENT_TEMPLATE)) {
				newTemplateHandler(request, response);
			} else if(operation.equalsIgnoreCase(SpagoBIConstants.EDIT_DOCUMENT_TEMPLATE)) {
				initDossierTempFolder(request, response);
				dossierDetailHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_NEW_CONFIGURED_DOCUMENT)) {
				newConfiguredDocumentHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_SAVE_CONFIGURED_DOCUMENT)) {
				saveConfiguredDocumentHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_DETAIL_BOOKLET)) {
				dossierDetailHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_DELETE_CONFIGURED_DOCUMENT)) {
				deleteConfiguredDocumentHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_DETAIL_CONFIGURED_DOCUMENT)) {
				detailConfiguredDocumentHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_SAVE_DETAIL_BOOKLET)) {
				saveBookletDetailHandler(request, response);
//			} else if(operation.equalsIgnoreCase(BookletsConstants.OPERATION_SAVE_NEW_VERSION_BOOKLET)){
//				saveBookletDetailHandler(request, response, true);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_LOAD_PRESENTATION_TEMPLATE)) {
				loadPresentationTemplateHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_LOAD_PROCESS_DEFINITION_FILE)) {
				loadProcessDefinitionFileHandler(request, response);
			}
		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass().getName(),
					            "service", "Error while processin request", ex);
			EMFUserError emfue = new EMFUserError(EMFErrorSeverity.ERROR, 100);
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	}

	
	private void initDossierTempFolder(SourceBean request, SourceBean response) throws EMFUserError {
		String objIdStr = (String) request.getAttribute(SpagoBIConstants.OBJECT_ID);
		Integer objId = new Integer(objIdStr);
		BIObject dossier = DAOFactory.getBIObjectDAO().loadBIObjectById(objId);
		IDossierDAO dossierDao = new DossierDAOHibImpl();
		dossierDao.unzipTemplate(dossier);
	}

	private void loadProcessDefinitionFileHandler(SourceBean request,
			SourceBean response) throws SourceBeanException, EMFUserError {
		String objIdStr = (String) request.getAttribute(SpagoBIConstants.OBJECT_ID);
		Integer objId = new Integer(objIdStr);
		BIObject dossier = DAOFactory.getBIObjectDAO().loadBIObjectById(objId);
		IDossierDAO dossierDao = new DossierDAOHibImpl();
		UploadedFile upFile = (UploadedFile) request.getAttribute("UPLOADED_FILE");
		if (upFile != null) {
			String fileName = upFile.getFileName();
			if (!fileName.toUpperCase().endsWith(".XML")) {
				// TODO error!!!
			} else {
				byte[] fileContent = upFile.getFileContent();
				dossierDao.storeProcessDefinitionFile(dossier, fileName, fileContent);
			}
		} else {
			logger.warn("Upload file was null!!!");
		}
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsLoopbackBookletDetail");
		response.setAttribute(SpagoBIConstants.OBJECT_ID, objIdStr);
	}

	private void loadPresentationTemplateHandler(SourceBean request,
			SourceBean response) throws SourceBeanException, EMFUserError {
		String objIdStr = (String) request.getAttribute(SpagoBIConstants.OBJECT_ID);
		Integer objId = new Integer(objIdStr);
		BIObject dossier = DAOFactory.getBIObjectDAO().loadBIObjectById(objId);
		IDossierDAO dossierDao = new DossierDAOHibImpl();
		UploadedFile upFile = (UploadedFile) request.getAttribute("UPLOADED_FILE");
		if (upFile != null) {
			String fileName = upFile.getFileName();
			if (!fileName.toUpperCase().endsWith(".PPT")) {
				// TODO error!!!
			} else {
				byte[] fileContent = upFile.getFileContent();
				dossierDao.storePresentationTemplateFile(dossier, fileName, fileContent);
			}
		} else {
			logger.warn("Upload file was null!!!");
		}
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsLoopbackBookletDetail");
		response.setAttribute(SpagoBIConstants.OBJECT_ID, objIdStr);
	}

	private void newTemplateHandler(SourceBean request, SourceBean response) throws SourceBeanException, EMFUserError {
		String tempOOFileName = "";
		List confDoc = new ArrayList();
		WorkflowConfiguration workConf = new WorkflowConfiguration();
		List functionalities;
		try {
			functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
		} catch (EMFUserError e) {
			logger.error("Error while loading documents tree", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
        response.setAttribute(BookletsConstants.CONFIGURED_DOCUMENT_LIST, confDoc);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletDetail");
		response.setAttribute(BookletsConstants.OO_TEMPLATE_FILENAME, tempOOFileName);
		response.setAttribute(BookletsConstants.WORKFLOW_CONFIGURATION, workConf);
	}
	
	
	
	
	
	private void newConfiguredDocumentHandler(SourceBean request, SourceBean response) 
											  throws SourceBeanException, EMFUserError {
		String dossierIdStr = (String)request.getAttribute(SpagoBIConstants.OBJECT_ID);
		Object objIdObj = request.getAttribute(BookletsConstants.BOOKLET_CONFIGURED_BIOBJECT_ID);
		if(!(objIdObj instanceof String)){
			Map errBackPars = new HashMap();
			errBackPars.put("PAGE", BookletsConstants.BOOKLET_MANAGEMENT_PAGE);
			errBackPars.put(SpagoBIConstants.OBJECT_ID, dossierIdStr);
			errBackPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
			errBackPars.put(SpagoBIConstants.OPERATION, BookletsConstants.OPERATION_DETAIL_BOOKLET);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "102", null, errBackPars, "component_booklets_messages");
		}
		String objIdStr = (String) objIdObj;
		Integer objId = new Integer(objIdStr);
		BIObject obj = null;
		List params = null;
		List roleList = null;
		try{
			IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
			obj = biobjdao.loadBIObjectById(objId);
			Integer id = obj.getId();
			IBIObjectParameterDAO biobjpardao = DAOFactory.getBIObjectParameterDAO();
			params = biobjpardao.loadBIObjectParametersById(id);
			IRoleDAO roleDao = DAOFactory.getRoleDAO();
			roleList = roleDao.loadAllRoles();
		} catch (Exception e) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass().getName(),
                                 "newConfiguredDocumentHandler", 
                                 "Error while loading biobje parameters and roles", e);
		}
		Integer id = obj.getId();
		String descr = obj.getDescription();
		String label = obj.getLabel();
		String name = obj.getName();
		Iterator iterParams = params.iterator();
		HashMap parNamesMap = new HashMap();
		HashMap parValueMap = new HashMap();
		while(iterParams.hasNext()) {
			BIObjectParameter par = (BIObjectParameter)iterParams.next();
			String parLabel = par.getLabel();
			String parUrlName = par.getParameterUrlName();
			parNamesMap.put(parLabel, parUrlName);
			parValueMap.put(parUrlName, "");
		}
		response.setAttribute("parnamemap", parNamesMap);
		response.setAttribute("parvaluemap", parValueMap);
		//response.setAttribute(BookletsConstants.ROLE_LIST, roleList);
		response.setAttribute("idobj", id);
		response.setAttribute("description", descr);
		response.setAttribute("label", label);
		response.setAttribute("name", name);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletConfiguredDocumentDetail"); 
		response.setAttribute(SpagoBIConstants.OBJECT_ID, dossierIdStr); 
	}
	
	
	
	
	private void saveConfiguredDocumentHandler(SourceBean request, SourceBean response) throws Exception {
		// get path pamphlet and id object
		String dossierIdStr = (String) request.getAttribute(SpagoBIConstants.OBJECT_ID);
		Integer dossierId = new Integer(dossierIdStr);
		BIObject dossier = DAOFactory.getBIObjectDAO().loadBIObjectById(dossierId);
		
		String label = (String)request.getAttribute("biobject_label");
		// get logical name assigned to the configured document
		String logicalName = (String)request.getAttribute("logicalname");
		if( (logicalName==null) || logicalName.trim().equalsIgnoreCase("") ) {
			logicalName = "";
			//throw new EMFUserError(EMFErrorSeverity.ERROR, 103, "component_booklets_messages");
		}
		// load biobject using label
//		Integer id = new Integer(idobj);
		IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
		BIObject obj = biobjdao.loadBIObjectByLabel(label);
		IBIObjectParameterDAO biobjpardao = DAOFactory.getBIObjectParameterDAO();
		// gets parameters of the biobject
		List params = biobjpardao.loadBIObjectParametersById(obj.getId());
		Iterator iterParams = params.iterator();
		// get map of the param url name and value assigned
		boolean findOutFormat = false;
		Map paramValueMap = new HashMap();
		Map paramNameMap = new HashMap();
		while(iterParams.hasNext()) {
			BIObjectParameter par = (BIObjectParameter)iterParams.next();
			String parUrlName = par.getParameterUrlName();
			if(parUrlName.equalsIgnoreCase("param_output_format"))
				findOutFormat = true;
			String value = (String)request.getAttribute(parUrlName);
			paramValueMap.put(parUrlName, value);
			paramNameMap.put(par.getLabel(), par.getParameterUrlName());
		}
		if(!findOutFormat){
			paramValueMap.put("param_output_format", "JPGBASE64");
		}
		// fill a configured document bo with data retrived
		ConfiguredBIDocument confDoc = new ConfiguredBIDocument();
		confDoc.setDescription(obj.getDescription());
//		confDoc.setId(obj.getId());
		confDoc.setLabel(obj.getLabel());
		confDoc.setParameters(paramValueMap);
		confDoc.setName(obj.getName());
		confDoc.setLogicalName(logicalName);
		
		// check if the error handler contains validation errors
		EMFErrorHandler errorHandler = getResponseContainer().getErrorHandler();
		if(errorHandler.isOKByCategory(EMFErrorCategory.VALIDATION_ERROR)){
			// store the configured document
			IDossierDAO dossierDao = new DossierDAOHibImpl();
			dossierDao.addConfiguredDocument(dossier, confDoc);
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsLoopbackBookletDetail");
			response.setAttribute(SpagoBIConstants.OBJECT_ID, dossierIdStr);
		} else {
			// set attribute into response
			response.setAttribute("parnamemap", paramNameMap);
			response.setAttribute("parvaluemap", paramValueMap);
//			response.setAttribute("idobj", confDoc.getId());
			response.setAttribute("description", confDoc.getDescription());
			response.setAttribute("label", confDoc.getLabel());
			response.setAttribute("name", confDoc.getName());
			response.setAttribute("logicalname", confDoc.getLogicalName());
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletConfiguredDocumentDetail"); 
			response.setAttribute(SpagoBIConstants.OBJECT_ID, dossierIdStr);
		}	
	}
	
	
	
	private void dossierDetailHandler(SourceBean request, SourceBean response) throws SourceBeanException, EMFUserError {
		String objIdStr = (String) request.getAttribute(SpagoBIConstants.OBJECT_ID);
		Integer objId = new Integer(objIdStr);
		BIObject dossier = DAOFactory.getBIObjectDAO().loadBIObjectById(objId);
		IDossierDAO dossierDao = new DossierDAOHibImpl();
//		List roleList = null;
//		try{
//			IRoleDAO roleDao = DAOFactory.getRoleDAO();
//			roleList = roleDao.loadAllRoles();
//		} catch(Exception e) {
//			logger.error("Error while loading all roles", e);
//		}
		// get the current template file name
		String tempFileName = dossierDao.getPresentationTemplateFileName(dossier);
		if (tempFileName == null) tempFileName = "";
		// get list of the configured document
		List confDoc = dossierDao.getConfiguredDocumentList(dossier);
		// get the current process definition file name
		String procDefFileName = dossierDao.getProcessDefinitionFileName(dossier);
		if (procDefFileName == null) procDefFileName = "";
		//WorkflowConfiguration workConf = bookDao.getWorkflowConfiguration(pathConfBook);
		List functionalities;
		try {
			functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
		} catch (EMFUserError e) {
			logger.error("Error while loading documents tree", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
        response.setAttribute(BookletsConstants.CONFIGURED_DOCUMENT_LIST, confDoc);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletDetail");
		response.setAttribute(BookletsConstants.OO_TEMPLATE_FILENAME, tempFileName);
		response.setAttribute(BookletsConstants.WF_PROCESS_DEFINTIION_FILENAME, procDefFileName);
		//response.setAttribute(BookletsConstants.WORKFLOW_CONFIGURATION, workConf);
	}
	
	
	private void deleteConfiguredDocumentHandler(SourceBean request, SourceBean response) throws Exception {
		String objIdStr = (String) request.getAttribute(SpagoBIConstants.OBJECT_ID);
		Integer objId = new Integer(objIdStr);
		BIObject dossier = DAOFactory.getBIObjectDAO().loadBIObjectById(objId);
		String confDocIdent = (String)request.getAttribute("configureddocumentidentifier");
		IDossierDAO dossierDao = new DossierDAOHibImpl();
		// delete the configured document
		dossierDao.deleteConfiguredDocument(dossier, confDocIdent);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsLoopbackBookletDetail");
		response.setAttribute(SpagoBIConstants.OBJECT_ID, objIdStr);
	}
	
	
	
	private void detailConfiguredDocumentHandler(SourceBean request, SourceBean response) throws Exception {
		String objIdStr = (String) request.getAttribute(SpagoBIConstants.OBJECT_ID);
		Integer objId = new Integer(objIdStr);
		BIObject dossier = DAOFactory.getBIObjectDAO().loadBIObjectById(objId);
		String confDocIdent = (String)request.getAttribute("configureddocumentidentifier");
		// get configured document
		IDossierDAO dossierDao = new DossierDAOHibImpl();
		ConfiguredBIDocument confDoc = dossierDao.getConfiguredDocument(dossier, confDocIdent);
		// get parameter value map
		Map paramValueMap = confDoc.getParameters();
		// create parameter name map
//		Integer idobj = confDoc.getId();
		String label = confDoc.getLabel();
		BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectByLabel(label);
		Integer idobj = obj.getId();
		IBIObjectParameterDAO biobjpardao = DAOFactory.getBIObjectParameterDAO();
		List params = biobjpardao.loadBIObjectParametersById(idobj);
		Iterator iterParams = params.iterator();
		Map paramNameMap = new HashMap();
		while(iterParams.hasNext()) {
			BIObjectParameter par = (BIObjectParameter)iterParams.next();
			String parLabel = par.getLabel();
			String parUrlName = par.getParameterUrlName();
			paramNameMap.put(parLabel, parUrlName);	
		}
		// set attribute into response
		response.setAttribute("parnamemap", paramNameMap);
		response.setAttribute("parvaluemap", paramValueMap);
//		response.setAttribute("idobj", confDoc.getId());
		response.setAttribute("description", confDoc.getDescription());
		response.setAttribute("label", confDoc.getLabel());
		response.setAttribute("name", confDoc.getName());
		response.setAttribute("logicalname", confDoc.getLogicalName());
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletConfiguredDocumentDetail"); 
		response.setAttribute(SpagoBIConstants.OBJECT_ID, objIdStr);
	}
	
	
	
	private void saveBookletDetailHandler(SourceBean request, SourceBean response) throws Exception {
		String objIdStr = (String) request.getAttribute(SpagoBIConstants.OBJECT_ID);
		Integer objId = new Integer(objIdStr);
		BIObject dossier = DAOFactory.getBIObjectDAO().loadBIObjectById(objId);
		IDossierDAO dossierDao = new DossierDAOHibImpl();
		// save information
		dossierDao.storeTemplate(dossier);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsLoopbackBookletDetail");
		response.setAttribute(SpagoBIConstants.OBJECT_ID, objIdStr);
	}
	
}
