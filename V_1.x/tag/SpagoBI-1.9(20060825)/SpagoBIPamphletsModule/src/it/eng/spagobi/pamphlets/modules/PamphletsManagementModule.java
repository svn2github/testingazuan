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
package it.eng.spagobi.pamphlets.modules;

import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.workflow.api.IWorkflowConnection;
import it.eng.spago.workflow.api.IWorkflowEngine;
import it.eng.spago.workflow.api.IWorkflowProcess;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.bo.dao.IRoleDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.pamphlets.bo.ConfiguredBIDocument;
import it.eng.spagobi.pamphlets.bo.PamphletGenerator;
import it.eng.spagobi.pamphlets.bo.WorkflowConfiguration;
import it.eng.spagobi.pamphlets.constants.PamphletsConstants;
import it.eng.spagobi.pamphlets.dao.IPamphletsCmsDao;
import it.eng.spagobi.pamphlets.dao.PamphletsCmsDaoImpl;
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
 * This class implements a module which  handles pamphlets management.
 */
public class PamphletsManagementModule extends AbstractModule {
	

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
			} else if (operation.equalsIgnoreCase(SpagoBIConstants.OPERATION_PAMPHLETS_VIEW_TREE)) {
				pamphletsViewTreeHandler(request, response);
			} else if (operation.equalsIgnoreCase(PamphletsConstants.OPERATION_NEW_PAMPHLET)) {
				pamphletsNewHandler(request, response);
			} else if (operation.equalsIgnoreCase(PamphletsConstants.OPERATION_DETAIL_PAMPHLET)) {
				pamphletsDetailHandler(request, response);
			} else if (operation.equalsIgnoreCase(PamphletsConstants.OPERATION_NEW_CONFIGURED_DOCUMENT)) {
				newConfiguredDocumentHandler(request, response);
			} else if (operation.equalsIgnoreCase(PamphletsConstants.OPERATION_SAVE_CONFIGURED_DOCUMENT)) {
				saveConfiguredDocumentHandler(request, response);
			} else if (operation.equalsIgnoreCase(PamphletsConstants.OPERATION_DELETE_CONFIGURED_DOCUMENT)) {
				deleteConfiguredDocumentHandler(request, response);
			} else if (operation.equalsIgnoreCase(PamphletsConstants.OPERATION_DETAIL_CONFIGURED_DOCUMENT)) {
				detailConfiguredDocumentHandler(request, response);
			} else if (operation.equalsIgnoreCase(PamphletsConstants.OPERATION_LOAD_TEMPLATE_PAMPHLET)) {
				loadTemplatePamphletHandler(request, response);
			} else if (operation.equalsIgnoreCase(PamphletsConstants.OPERATION_GENERATE_DOCUMENT_PARTS)) {
				generateDocumentPartsHandler(request, response);
			} else if (operation.equalsIgnoreCase(PamphletsConstants.OPERATION_ERASE_PAMPHLET)) {
				erasePamphletHandler(request, response);
			} else if (operation.equalsIgnoreCase(PamphletsConstants.OPERATION_SAVE_WORKFLOWDATA)) {
				saveWorkflowDataHandler(request, response);
			}
		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			SpagoBITracer.major(PamphletsConstants.NAME_MODULE, this.getClass().getName(),
					            "service", "Error while processin request", ex);
			EMFUserError emfue = new EMFUserError(EMFErrorSeverity.ERROR, 100);
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	}

	
	
	
	private void pamphletsViewTreeHandler(SourceBean request, SourceBean response) throws SourceBeanException {
		ConfigSingleton config = ConfigSingleton.getInstance();
		SourceBean cmsPathPampSB = (SourceBean)config.getAttribute("PAMPHLETS.BASE_CMS_NODE");
		String cmsPathPamp = (String)cmsPathPampSB.getAttribute("path");
		IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
		List pamphlets = pampDao.getPamphletList(cmsPathPamp);
		response.setAttribute(PamphletsConstants.PAMPHLET_LIST, pamphlets);
	}

	
	private void pamphletsNewHandler(SourceBean request, SourceBean response) throws SourceBeanException {
		
		ConfigSingleton config = ConfigSingleton.getInstance();
		SourceBean cmsPathPampSB = (SourceBean)config.getAttribute("PAMPHLETS.BASE_CMS_NODE");
		String cmsPathPamp = (String)cmsPathPampSB.getAttribute("path");
		String namePamp = (String)request.getAttribute("nameNewPamphlet");
		IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
		pampDao.addPamphlet(cmsPathPamp, namePamp);
		response.setAttribute(PamphletsConstants.PUBLISHER_NAME, "PamphletsLoopbackManagementHome");
		
	}
	
	private void pamphletsDetailHandler(SourceBean request, SourceBean response) throws SourceBeanException, EMFUserError {
		String pathPamp = (String)request.getAttribute(PamphletsConstants.PATH_PAMPHLET);
		IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
		List roleList = null;
		try{
			IRoleDAO roleDao = DAOFactory.getRoleDAO();
			roleList = roleDao.loadAllRoles();
		} catch(Exception e) {
			SpagoBITracer.major(PamphletsConstants.NAME_MODULE, this.getClass().getName(),
		                        "pamphletsDetailHandler", "Error while loading all roles", e);
		}
		// get the current template file name
		String tempFileName = pampDao.getPamphletTemplateFileName(pathPamp);
		// get list of the configured document
		List confDoc = pampDao.getConfiguredDocumentList(pathPamp);
		WorkflowConfiguration workConf = pampDao.getWorkflowConfiguration(pathPamp);
		List functionalities;
		try {
			functionalities = DAOFactory.getLowFunctionalityDAO()
					.loadAllLowFunctionalities(true);
		} catch (EMFUserError e) {
			SpagoBITracer.major(PamphletsConstants.NAME_MODULE, this.getClass()
					.getName(), "pamphletsDetailHandler", "Error while loading documents tree", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
        //response.setAttribute(objectsSB);
        response.setAttribute(PamphletsConstants.CONFIGURED_DOCUMENT_LIST, confDoc);
		response.setAttribute(PamphletsConstants.ROLE_LIST, roleList);
		response.setAttribute(PamphletsConstants.PUBLISHER_NAME, "PamphletDetail");
		response.setAttribute("PATH_PAMPHLET", pathPamp);
		response.setAttribute("templatefilename", tempFileName);
		response.setAttribute("workflowConfiguration", workConf);
	}
	
	
	
	private void newConfiguredDocumentHandler(SourceBean request, SourceBean response) 
	throws SourceBeanException, EMFUserError {
		Object objPath = request.getAttribute(PamphletsConstants.PATH_OBJECT);
		if(!(objPath instanceof String)){
			throw new EMFUserError(EMFErrorSeverity.ERROR, 102, "component_pamphlets_messages");
		}
		String pathObject = (String)objPath;
		
        String pathPamp = (String)request.getAttribute("PATHPAMPHLET");
		
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
			SpagoBITracer.major(PamphletsConstants.NAME_MODULE, this.getClass().getName(),
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
		response.setAttribute(PamphletsConstants.ROLE_LIST, roleList);
		response.setAttribute("idobj", id);
		response.setAttribute("description", descr);
		response.setAttribute("label", label);
		response.setAttribute("name", name);
		response.setAttribute(PamphletsConstants.PUBLISHER_NAME, "PamphletConfiguredDocumentDetail"); 
		response.setAttribute("PATHPAMPHLET", pathPamp); 
	}
	

	
	private void saveConfiguredDocumentHandler(SourceBean request, SourceBean response) throws Exception {
		// get path pamphlet and id object
		String pathPamp = (String)request.getAttribute("pathpamphlet");
		String idobj = (String)request.getAttribute("idbiobject");
		// get logical name assigned to the configured document
		String logicalName = (String)request.getAttribute("logicalname");
		if( (logicalName==null) || logicalName.trim().equalsIgnoreCase("") ) {
			throw new EMFUserError(EMFErrorSeverity.ERROR, 103, "component_pamphlets_messages");
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
		
		//******************* START ROLE ASSIGNMENT *************************
		//List roles = new ArrayList();
		//confDoc.setRoles(roles);
		//******************* END ROLE ASSIGNMENT *************************
		
		// store the configured document
		IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
		pampDao.addConfiguredDocument(pathPamp, confDoc);
		response.setAttribute(PamphletsConstants.PUBLISHER_NAME, "PamphletsLoopbackPamphletDetail");
		response.setAttribute(PamphletsConstants.PATH_PAMPHLET, pathPamp);
	}

	
	
	private void deleteConfiguredDocumentHandler(SourceBean request, SourceBean response) throws Exception {
		String pathPamp = (String)request.getAttribute(PamphletsConstants.PATH_PAMPHLET);
		String confDocIdent = (String)request.getAttribute("configureddocumentidentifier");
		// delete the configured document
		IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
		pampDao.deleteConfiguredDocument(pathPamp, confDocIdent);
		response.setAttribute(PamphletsConstants.PUBLISHER_NAME, "PamphletsLoopbackPamphletDetail");
		response.setAttribute(PamphletsConstants.PATH_PAMPHLET, pathPamp);
	}
	
	
	private void detailConfiguredDocumentHandler(SourceBean request, SourceBean response) throws Exception {
		String pathPamp = (String)request.getAttribute(PamphletsConstants.PATH_PAMPHLET);
		String confDocIdent = (String)request.getAttribute("configureddocumentidentifier");
		// get configured document
		IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
		ConfiguredBIDocument confDoc = pampDao.getConfigureDocument(pathPamp, confDocIdent);
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
		response.setAttribute(PamphletsConstants.PUBLISHER_NAME, "PamphletConfiguredDocumentDetail"); 
		response.setAttribute("PATHPAMPHLET", pathPamp); 
	}
	
	
	
	
	private void loadTemplatePamphletHandler(SourceBean request, SourceBean response) throws Exception {
		PortletRequest portletRequest = PortletUtilities.getPortletRequest();
		if (portletRequest instanceof ActionRequest) {
			ActionRequest actionRequest = (ActionRequest) portletRequest;
			if (PortletFileUpload.isMultipartContent(actionRequest)) {
				request = PortletUtilities.getServiceRequestFromMultipartPortletRequest(portletRequest);
			}
		}
		String pathPamp = (String)request.getAttribute(PamphletsConstants.PATH_PAMPHLET);
		Object upFileObj = request.getAttribute("UPLOADED_FILE");
		UploadedFile upFile = (UploadedFile)request.getAttribute("UPLOADED_FILE");
		String fileName = upFile.getFileName();
		byte[] fileContent = upFile.getFileContent();
		// store the content of the template
		IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
		pampDao.storePamphletTemplate(pathPamp, fileName, fileContent);
		response.setAttribute(PamphletsConstants.PUBLISHER_NAME, "PamphletsLoopbackPamphletDetail");
		response.setAttribute(PamphletsConstants.PATH_PAMPHLET, pathPamp);
	}
	
	
	
	
	
	private void generateDocumentPartsHandler(SourceBean request, SourceBean response) throws Exception {
		
		RequestContainer reqCont = RequestContainer.getRequestContainer();
		SessionContainer sessCont = reqCont.getSessionContainer();
		SessionContainer permCont = sessCont.getPermanentContainer();
		IEngUserProfile userProfile = (IEngUserProfile)permCont.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String nameUser = userProfile.getUserUniqueIdentifier().toString();
		
		String pathPamp = (String)request.getAttribute(PamphletsConstants.PATH_PAMPHLET);
		PamphletGenerator gen = null;
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
		response.setAttribute(PamphletsConstants.PUBLISHER_NAME, "PamphletsLoopbackPamphletDetail");
		response.setAttribute(PamphletsConstants.PATH_PAMPHLET, pathPamp);
	}
	
	
	
	private void erasePamphletHandler(SourceBean request, SourceBean response) throws Exception {
		String pathPamp = (String)request.getAttribute(PamphletsConstants.PATH_PAMPHLET);
		// delete pamphlet
		IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
		pampDao.deletePamphlet(pathPamp);
		response.setAttribute(PamphletsConstants.PUBLISHER_NAME, "PamphletsLoopbackManagementHome");
	}
	
	
	
	private void saveWorkflowDataHandler(SourceBean request, SourceBean response) throws Exception {
		// get name workflow package and process
		String packageName = (String)request.getAttribute("nameWorkPackage");
		String processName = (String)request.getAttribute("nameWorkProcess");
		String pathPamp = (String)request.getAttribute(PamphletsConstants.PATH_PAMPHLET);
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
		response.setAttribute(PamphletsConstants.PUBLISHER_NAME, "PamphletsLoopbackPamphletDetail");
		response.setAttribute(PamphletsConstants.PATH_PAMPHLET, pathPamp);
	}
	
	
	
}
