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
package it.eng.spagobi.booklets.modules;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.bo.dao.IRoleDAO;
import it.eng.spagobi.booklets.bo.ConfiguredBIDocument;
import it.eng.spagobi.booklets.bo.WorkflowConfiguration;
import it.eng.spagobi.booklets.constants.BookletsConstants;
import it.eng.spagobi.booklets.dao.IBookletsCmsDao;
import it.eng.spagobi.booklets.dao.BookletsCmsDaoImpl;
import it.eng.spagobi.constants.SpagoBIConstants;
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
 * This class implements a module which  handles pamphlets management.
 */
public class BookletsManagementModule extends AbstractModule {
	

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
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
				            "service","begin of detail Engine modify/visualization service with operation =" +operation);
		try{
			if((operation==null)||(operation.trim().equals(""))) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 100, "component_pamphlets_messages");
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
						            "service", "The operation parameter is null");
				throw userError;
			} else if(operation.equalsIgnoreCase(SpagoBIConstants.OPERATION_NEW_BOOKLET_TEMPLATE)) {
				newTemplateHandler(request, response);
			} else if(operation.equalsIgnoreCase(SpagoBIConstants.OPERATION_EDIT_BOOKLET_TEMPLATE)) {
				String pathBiobj = (String)request.getAttribute(SpagoBIConstants.PATH);
				request.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathBiobj + "/template");
				bookletDetailHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_NEW_CONFIGURED_DOCUMENT)) {
				newConfiguredDocumentHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_SAVE_CONFIGURED_DOCUMENT)) {
				saveConfiguredDocumentHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_DETAIL_BOOKLET)) {
				bookletDetailHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_DELETE_CONFIGURED_DOCUMENT)) {
				deleteConfiguredDocumentHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_DETAIL_CONFIGURED_DOCUMENT)) {
				detailConfiguredDocumentHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_LOAD_OOTEMPLATE_BOOKLET)) {
				loadOOTemplateHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_SAVE_WORKFLOWDATA)) {
				saveWorkflowDataHandler(request, response);
			}
			
			
			
			
			
			
			
			
			
			/*
			else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_NEW_PAMPHLET)) {
				pamphletsNewHandler(request, response);
			}      else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_GENERATE_DOCUMENT_PARTS)) {
				generateDocumentPartsHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_ERASE_PAMPHLET)) {
				erasePamphletHandler(request, response);
			} 
			*/
			
			
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

	
	
	private void newTemplateHandler(SourceBean request, SourceBean response) throws SourceBeanException, EMFUserError {
		String pathBiObject = (String)request.getAttribute(SpagoBIConstants.CMS_BIOBJECTS_PATH);
		IBookletsCmsDao bookDao = new BookletsCmsDaoImpl();
		String bookTemplatePath = bookDao.createNewConfigurationNode(pathBiObject);
		if(bookTemplatePath==null) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass().getName(), 
		                        "newTemplateHandler", 
		                        "Error while creating cms node that holds booklet configuration");
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		String tempOOFileName = "";
		List confDoc = new ArrayList();
		WorkflowConfiguration workConf = new WorkflowConfiguration();
		List functionalities;
		try {
			functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
		} catch (EMFUserError e) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass().getName(), 
					            "newTemplateHandler", "Error while loading documents tree", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
        response.setAttribute(BookletsConstants.CONFIGURED_DOCUMENT_LIST, confDoc);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletDetail");
		response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, bookTemplatePath);
		response.setAttribute(BookletsConstants.OO_TEMPLATE_FILENAME, tempOOFileName);
		response.setAttribute(BookletsConstants.WORKFLOW_CONFIGURATION, workConf);
	}
	
	
	
	
	
	private void newConfiguredDocumentHandler(SourceBean request, SourceBean response) 
											  throws SourceBeanException, EMFUserError {
		Object objPath = request.getAttribute(BookletsConstants.PATH_OBJECT);
		if(!(objPath instanceof String)){
			throw new EMFUserError(EMFErrorSeverity.ERROR, 102, "component_booklets_messages");
		}
		String pathObject = (String)objPath;
        String pathBookConf = (String)request.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
		BIObject obj = null;
		List params = null;
		List roleList = null;
		try{
			IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
			obj = biobjdao.loadBIObjectForDetail(pathObject);
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
		response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathBookConf); 
	}
	
	
	
	
	private void saveConfiguredDocumentHandler(SourceBean request, SourceBean response) throws Exception {
		// get path pamphlet and id object
		String pathConfBook = (String)request.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
		String idobj = (String)request.getAttribute("idbiobject");
		// get logical name assigned to the configured document
		String logicalName = (String)request.getAttribute("logicalname");
		if( (logicalName==null) || logicalName.trim().equalsIgnoreCase("") ) {
			throw new EMFUserError(EMFErrorSeverity.ERROR, 103, "component_booklets_messages");
		}
		// load biobject using id
		Integer id = new Integer(idobj);
		IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
		BIObject obj = biobjdao.loadBIObjectForDetail(id);
		IBIObjectParameterDAO biobjpardao = DAOFactory.getBIObjectParameterDAO();
		// gets parameters of the biobject
		List params = biobjpardao.loadBIObjectParametersById(id);
		Iterator iterParams = params.iterator();
		// get map of the param url name and value assigned
		boolean findOutFormat = false;
		Map paramMap = new HashMap();
		while(iterParams.hasNext()) {
			BIObjectParameter par = (BIObjectParameter)iterParams.next();
			String parUrlName = par.getParameterUrlName();
			if(parUrlName.equalsIgnoreCase("param_output_format"))
				findOutFormat = true;
			String value = (String)request.getAttribute(parUrlName);
			paramMap.put(parUrlName, value);
		}
		if(!findOutFormat){
			paramMap.put("param_output_format", "JPGBASE64");
		}
		// fill a configured document bo with data retrived
		ConfiguredBIDocument confDoc = new ConfiguredBIDocument();
		confDoc.setDescription(obj.getDescription());
		confDoc.setId(obj.getId());
		confDoc.setLabel(obj.getLabel());
		confDoc.setParameters(paramMap);
		confDoc.setName(obj.getName());
		confDoc.setLogicalName(logicalName);
		// store the configured document
		IBookletsCmsDao bookDao = new BookletsCmsDaoImpl();
		bookDao.addConfiguredDocument(pathConfBook, confDoc);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsLoopbackBookletDetail");
		response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook);
	}
	
	
	
	private void bookletDetailHandler(SourceBean request, SourceBean response) throws SourceBeanException, EMFUserError {
		String pathConfBook = (String)request.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
		IBookletsCmsDao bookDao = new BookletsCmsDaoImpl();
		List roleList = null;
		try{
			IRoleDAO roleDao = DAOFactory.getRoleDAO();
			roleList = roleDao.loadAllRoles();
		} catch(Exception e) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass().getName(),
		                        "bookletsDetailHandler", "Error while loading all roles", e);
		}
		// get the current template file name
		String tempFileName = bookDao.getBookletTemplateFileName(pathConfBook);
		// get list of the configured document
		List confDoc = bookDao.getConfiguredDocumentList(pathConfBook);
		// get the current process definition file name
		String procDefFileName = bookDao.getBookletProcessDefinitionFileName(pathConfBook);
		//WorkflowConfiguration workConf = bookDao.getWorkflowConfiguration(pathConfBook);
		List functionalities;
		try {
			functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(true);
		} catch (EMFUserError e) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass()
					.getName(), "bookletsDetailHandler", "Error while loading documents tree", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
        response.setAttribute(BookletsConstants.CONFIGURED_DOCUMENT_LIST, confDoc);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletDetail");
		response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook);
		response.setAttribute(BookletsConstants.OO_TEMPLATE_FILENAME, tempFileName);
		response.setAttribute(BookletsConstants.WF_PROCESS_DEFINTIION_FILENAME, procDefFileName);
		//response.setAttribute(BookletsConstants.WORKFLOW_CONFIGURATION, workConf);
	}
	
	
	private void deleteConfiguredDocumentHandler(SourceBean request, SourceBean response) throws Exception {
		String pathConfBook = (String)request.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
		String confDocIdent = (String)request.getAttribute("configureddocumentidentifier");
		// delete the configured document
		IBookletsCmsDao pampDao = new BookletsCmsDaoImpl();
		pampDao.deleteConfiguredDocument(pathConfBook, confDocIdent);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsLoopbackBookletDetail");
		response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook);
	}
	
	
	
	private void detailConfiguredDocumentHandler(SourceBean request, SourceBean response) throws Exception {
		String pathConfBook = (String)request.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
		String confDocIdent = (String)request.getAttribute("configureddocumentidentifier");
		// get configured document
		IBookletsCmsDao bookDao = new BookletsCmsDaoImpl();
		ConfiguredBIDocument confDoc = bookDao.getConfigureDocument(pathConfBook, confDocIdent);
		// get parameter value map
		Map paramValueMap = confDoc.getParameters();
		// create parameter name map
		Integer idobj = confDoc.getId();
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
		response.setAttribute("idobj", confDoc.getId());
		response.setAttribute("description", confDoc.getDescription());
		response.setAttribute("label", confDoc.getLabel());
		response.setAttribute("name", confDoc.getName());
		response.setAttribute("logicalname", confDoc.getLogicalName());
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletConfiguredDocumentDetail"); 
		response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook); 
	}
	
	
	
	

	private void loadOOTemplateHandler(SourceBean request, SourceBean response) throws Exception {
		PortletRequest portletRequest = PortletUtilities.getPortletRequest();
		if (portletRequest instanceof ActionRequest) {
			ActionRequest actionRequest = (ActionRequest) portletRequest;
			if (PortletFileUpload.isMultipartContent(actionRequest)) {
				request = PortletUtilities.getServiceRequestFromMultipartPortletRequest(portletRequest);
			}
		}
		String pathBookConf = (String)request.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
		Object upFileObj = request.getAttribute("UPLOADED_FILE");
		UploadedFile upFile = (UploadedFile)request.getAttribute("UPLOADED_FILE");
		String fileName = upFile.getFileName();
		byte[] fileContent = upFile.getFileContent();
		// store the content of the template
		IBookletsCmsDao bookDao = new BookletsCmsDaoImpl();
		bookDao.storeBookletTemplate(pathBookConf, fileName, fileContent);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsLoopbackBookletDetail");
		response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathBookConf);
	}
	
	
	private void saveWorkflowDataHandler(SourceBean request, SourceBean response) throws Exception {
		PortletRequest portletRequest = PortletUtilities.getPortletRequest();
		if (portletRequest instanceof ActionRequest) {
			ActionRequest actionRequest = (ActionRequest) portletRequest;
			if (PortletFileUpload.isMultipartContent(actionRequest)) {
				request = PortletUtilities.getServiceRequestFromMultipartPortletRequest(portletRequest);
			}
		}
		String pathBookConf = (String)request.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
		UploadedFile upFile = (UploadedFile)request.getAttribute("UPLOADED_FILE");
		String fileName = upFile.getFileName();
		byte[] fileContent = upFile.getFileContent();
		// store the content of the template
		IBookletsCmsDao bookDao = new BookletsCmsDaoImpl();
		bookDao.storeBookletProcessDefinition(pathBookConf, fileName, fileContent);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsLoopbackBookletDetail");
		response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathBookConf);
		//****************** OLD CODE *****************************
		//*********************************************************
		/*
		// get name workflow package and process
		String packageName = (String)request.getAttribute("nameWorkPackage");
		String processName = (String)request.getAttribute("nameWorkProcess");
		String pathPamp = (String)request.getAttribute(BookletsConstants.PATH_PAMPHLET);
		if(!((packageName==null) ||
		   (processName==null) ||
		   packageName.trim().equals("") ||
		   processName.trim().equals(""))) {
			WorkflowConfiguration workConf = new WorkflowConfiguration();
			workConf.setNameWorkflowPackage(packageName);
			workConf.setNameWorkflowProcess(processName);
			IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
			pampDao.saveWorkflowConfiguration(pathPamp, workConf);
		}
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "PamphletsLoopbackPamphletDetail");
		response.setAttribute(BookletsConstants.PATH_PAMPHLET, pathPamp);
		*/
	}
	
	
	
	
	
	
	
	
	

	/*
	private void pamphletsNewHandler(SourceBean request, SourceBean response) throws SourceBeanException {
		
		ConfigSingleton config = ConfigSingleton.getInstance();
		SourceBean cmsPathPampSB = (SourceBean)config.getAttribute("PAMPHLETS.BASE_CMS_NODE");
		String cmsPathPamp = (String)cmsPathPampSB.getAttribute("path");
		String namePamp = (String)request.getAttribute("nameNewPamphlet");
		IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
		pampDao.addPamphlet(cmsPathPamp, namePamp);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "PamphletsLoopbackManagementHome");
		
	}
	
	
	private void generateDocumentPartsHandler(SourceBean request, SourceBean response) throws Exception {
		
		RequestContainer reqCont = RequestContainer.getRequestContainer();
		SessionContainer sessCont = reqCont.getSessionContainer();
		SessionContainer permCont = sessCont.getPermanentContainer();
		IEngUserProfile userProfile = (IEngUserProfile)permCont.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String nameUser = userProfile.getUserUniqueIdentifier().toString();
		
		String pathPamp = (String)request.getAttribute(BookletsConstants.PATH_PAMPHLET);
		IWorkflowEngine wfEngine = (IWorkflowEngine)ApplicationContainer.getInstance().getAttribute("WfEngine");
		IWorkflowConnection wfConnection = wfEngine.getWorkflowConnection();
		//wfConnection.open("biadmin","biadmin");
		wfConnection.open(nameUser, nameUser);
		Map context = new HashMap();
		context.put("PathPamphlet", pathPamp);
		
		IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
		WorkflowConfiguration workConf = pampDao.getWorkflowConfiguration(pathPamp);
		String namePackage = workConf.getNameWorkflowPackage();
		String nameProcess = workConf.getNameWorkflowProcess();
		
		if(namePackage.trim().equals("") || nameProcess.trim().equals("") ) {
			throw new EMFUserError(EMFErrorSeverity.ERROR, 101, "component_pamphlets_messages");
		}
		IWorkflowProcess aWfProcess = wfConnection.createProcessWithInitialContext(namePackage,
																				   nameProcess, 
																				   context);
		
		//IWorkflowProcess aWfProcess = wfConnection.createProcessWithInitialContext("SpagoBI", "SpagoBI_Phamplet_ExampleDocument", context);
		aWfProcess.start();
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "PamphletsLoopbackPamphletDetail");
		response.setAttribute(BookletsConstants.PATH_PAMPHLET, pathPamp);
	}
	
	
	
	private void erasePamphletHandler(SourceBean request, SourceBean response) throws Exception {
		String pathPamp = (String)request.getAttribute(BookletsConstants.PATH_PAMPHLET);
		// delete pamphlet
		IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
		pampDao.deletePamphlet(pathPamp);
		response.setAttribute(BookletsConstants.PUBLISHER_NAME, "PamphletsLoopbackManagementHome");
	}
	
	
	
	
	
	*/
	
}
