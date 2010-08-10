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
import it.eng.spago.cms.CmsManager;
import it.eng.spago.cms.operations.DeleteOperation;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.validation.EMFValidationError;
import it.eng.spago.validation.coordinator.ValidationCoordinator;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.ObjParuse;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.TemplateVersion;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.bo.dao.IObjParuseDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.commons.AbstractBasicCheckListModule;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SessionMonitor;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.validator.GenericValidator;

/**
 * Implements a module which  handles all BI objects management: 
 * has methods for BI Objects load, detail, modify/insertion and deleting operations. 
 * The <code>service</code> method has  a switch for all these operations, differentiated the ones 
 * from the others by a <code>message</code> String.
 */

public class DetailBIObjectModule extends AbstractModule {
		
	public final static String MODULE_PAGE = "DetailBIObjectPage";
	public final static String NAME_ATTR_OBJECT = "BIObjects";
	public final static String NAME_ATTR_LIST_OBJ_TYPES = "types";
	public final static String NAME_ATTR_LIST_ENGINES = "engines";
	public final static String NAME_ATTR_LIST_STATES = "states";		
	public final static String NAME_ATTR_OBJECT_PAR = "OBJECT_PAR";
	private String actor = null;
	private EMFErrorHandler errorHandler = null;
	protected IEngUserProfile profile;
	protected String initialPath = null;
	
	SessionContainer session = null;
	
	public void init(SourceBean config) {
	}
	
	
	/**
	 * Reads the operation asked by the user and calls the insertion, modify, detail and 
	 * deletion methods.
	 * 
	 * @param request The Source Bean containing all request parameters
	 * @param response The Source Bean containing all response parameters
	 * @throws exception If an exception occurs
	 * 
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		PortletRequest portletRequest = PortletUtilities.getPortletRequest();
		if (portletRequest instanceof ActionRequest) {
			ActionRequest actionRequest = (ActionRequest) portletRequest;
			if (PortletFileUpload.isMultipartContent(actionRequest)) {
				request = PortletUtilities.getServiceRequestFromMultipartPortletRequest(portletRequest);
			}
		}
		
		PortletPreferences prefs = portletRequest.getPreferences();
		String modality = (String) prefs.getValue(BIObjectsModule.MODALITY, "");
		initialPath = null;
		if (modality != null && modality.equalsIgnoreCase(BIObjectsModule.FILTER_TREE)) {
			initialPath = (String) prefs.getValue(TreeObjectsModule.PATH_SUBTREE, "");
		}
		
		RequestContainer requestContainer = this.getRequestContainer();		
		session = requestContainer.getSessionContainer();
		SessionContainer permanentSession = session.getPermanentContainer();
		profile = (IEngUserProfile) permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		
		// get attributes from request		
		String message = (String) request.getAttribute("MESSAGEDET");
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service"," MESSAGEDET = " + message);
				
		actor = (String) request.getAttribute(SpagoBIConstants.ACTOR);
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service"," ACTOR = " + actor);
		
		// get attribute from session
		String moduleName = (String)session.getAttribute("RETURN_FROM_MODULE");
		if(moduleName != null) { // TODO clear session with a proper method of returning module
			if(moduleName.equalsIgnoreCase("ListLookupParametersModule")) {
				String returnState = (String)session.getAttribute("RETURN_STATUS");
				if(returnState.equalsIgnoreCase("SELECT"))
					lookupReturnHandler(request, response);	
				else
					lookupReturnBackHandler(request,response);
				session.delAttribute("RETURN_STATUS");
				session.delAttribute("RETURN_FROM_MODULE");
				return; // force refresh
				// TODO force refresh in a standard way with a generic methods
			}
			else if(moduleName.equalsIgnoreCase("CheckLinksModule")) {
				SessionMonitor.printSession(session);
				AbstractBasicCheckListModule.clearSession(session, moduleName);
				session.delAttribute(SpagoBIConstants.ACTOR);
				SessionMonitor.printSession(session);
			} else if (moduleName.equalsIgnoreCase("ListObjParuseModule")) {
				lookupReturnBackHandler(request,response);
				session.delAttribute("RETURN_FROM_MODULE");
				return;
			}			
		}
		
		// these attributes, if defined, represent events triggered by one 
		// of the submit buttons present in the main form 
		boolean parametersLookupButtonClicked =  request.getAttribute("loadParametersLookup") != null;
		boolean linksLookupButtonClicked =  request.getAttribute("loadLinksLookup") != null;
		boolean dependenciesButtonClicked =  request.getAttribute("goToDependenciesPage") != null;
		
		errorHandler = getErrorHandler();
		
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule", "service", "The message parameter is null");
				throw userError;
			} 
						
			// check for events first...
			if (parametersLookupButtonClicked){
				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service","loadParametersLookup != null");
				startParametersLookupHandler (request, message, response);
			} else if(linksLookupButtonClicked){
				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service","editSubreports != null");
				startLinksLookupHandler(request, message, response);
			} else if (dependenciesButtonClicked) {
				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service","goToDependenciesPage != null");
				startDependenciesLookupHandler(request, message, response);
		    } // ...then check for other service request types 			
			 else if (message.trim().equalsIgnoreCase(ObjectsTreeConstants.DETAIL_SELECT)) {
				getDetailObject(request, response);
			} else if (message.trim().equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {
				modBIObject(request, ObjectsTreeConstants.DETAIL_MOD, response);
			} else if (message.trim().equalsIgnoreCase(ObjectsTreeConstants.DETAIL_NEW)) {
				newBIObject(request, response);
			} else if (message.trim().equalsIgnoreCase(ObjectsTreeConstants.DETAIL_INS)) {
				modBIObject(request, ObjectsTreeConstants.DETAIL_INS, response);
			} else if (message.trim().equalsIgnoreCase(ObjectsTreeConstants.DETAIL_DEL)) {
				delDetailObject(request, ObjectsTreeConstants.DETAIL_DEL, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.ERASE_VERSION)) {
				eraseVersion(request, response);
			} else if (message.trim().equalsIgnoreCase("EXIT_FROM_DETAIL")){
				exitFromDetail(request, response);
			}
		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	}

	private void setLoopbackContext(SourceBean request, String message) throws EMFUserError{
		BIObject obj = recoverBIObjectDetails(request, message);
		BIObjectParameter biObjPar = recoverBIObjectParameterDetails(request, obj.getId());
		session.setAttribute("LookupBIObject", obj);
		session.setAttribute("LookupBIObjectParameter", biObjPar);
		session.setAttribute("modality", message);
		session.setAttribute("modalityBkp", message);
		session.setAttribute("actor",actor);
	}
	
	private void delateLoopbackContext() {
		session.delAttribute("LookupBIObject");
		session.delAttribute("LookupBIObjectParameter");
		session.delAttribute("modality");
		session.delAttribute("modalityBkp");
		session.delAttribute("actor");
	}
	
	private Integer getBIObjectIdFromLoopbackContext() {
		Integer id = null;
		BIObject obj = (BIObject)session.getAttribute("LookupBIObject");
		if(obj != null) id = obj.getId();
		return id;
	}
	
		
	private void startParametersLookupHandler(SourceBean request, String message, SourceBean response) throws EMFUserError, SourceBeanException {
		setLoopbackContext(request, message);		
		response.setAttribute("parametersLookup", "true");		
	}
	
	private void startLinksLookupHandler(SourceBean request, String message, SourceBean response) throws EMFUserError, SourceBeanException {
		modBIObject(request, ObjectsTreeConstants.DETAIL_MOD, response);
		String idStr = (String) request.getAttribute("id");
		session.setAttribute("SUBJECT_ID", idStr);
		session.setAttribute(SpagoBIConstants.ACTOR, actor);
		response.setAttribute("linksLookup", "true");		
	}	

	private void startDependenciesLookupHandler(SourceBean request, String message, SourceBean response) throws Exception {
		fillRequestContainer(request, errorHandler);
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
		BIObject obj = recoverBIObjectDetails(request, message);
		BIObjectParameter biObjPar = recoverBIObjectParameterDetails(request, obj.getId());
		String saveBIObjectParameter = (String) request.getAttribute("saveBIObjectParameter");
		if (saveBIObjectParameter != null && saveBIObjectParameter.equalsIgnoreCase("yes")) {
			// it is requested to save the visible BIObjectParameter
			ValidationCoordinator.validate("PAGE", "BIObjectParameterValidation", this);
			// If it's a new BIObjectParameter or if the Parameter was changed controls 
			// that the BIObjectParameter url name is not already in use
			urlNameControl(obj.getId(), biObjPar);
			verifyForDependencies(biObjPar);
			
			// if there are some validation errors into the errorHandler does not write into DB
			Collection errors = errorHandler.getErrors();
			if (errors != null && errors.size() > 0) {
				Iterator iterator = errors.iterator();
				while (iterator.hasNext()) {
					Object error = iterator.next();
					if (error instanceof EMFValidationError) {
						fillResponse(response);
						reloadCMSInformation(obj);
						prepareBIObjectDetailPage(response, obj, biObjPar, biObjPar.getId().toString(), 
								ObjectsTreeConstants.DETAIL_MOD, false, false);
						return;
					}
				}
			}
			DAOFactory.getBIObjectParameterDAO().modifyBIObjectParameter(biObjPar);
		} else {
			biObjPar = DAOFactory.getBIObjectParameterDAO().loadForDetailByObjParId(biObjPar.getId());
		}
		// refresh of the initial_BIObjectParameter in session
		BIObjectParameter biObjParClone = clone(biObjPar);
		session.setAttribute("initial_BIObjectParameter", biObjParClone);
		// set lookup objects
		session.setAttribute("LookupBIObject", obj);
		session.setAttribute("LookupBIObjectParameter", biObjPar);
		session.setAttribute("modality", message);
		session.setAttribute("modalityBkp", message);
		session.setAttribute("actor",actor);		
		response.setAttribute("dependenciesLookup", "true");
	}
	
	private void lookupReturnBackHandler(SourceBean request, SourceBean response) throws SourceBeanException, EMFUserError {

		BIObject obj = (BIObject) session.getAttribute("LookupBIObject");
		BIObjectParameter biObjPar = (BIObjectParameter) session.getAttribute("LookupBIObjectParameter");
		String modality = (String) session.getAttribute("modality");
		if(modality == null) modality = (String)session.getAttribute("modalityBkp");
		
		actor = (String) session.getAttribute("actor");
		session.delAttribute("LookupBIObject");
		session.delAttribute("LookupBIObjectParameter");
		session.delAttribute("modality");
		session.delAttribute("modalityBkp");
		session.delAttribute("actor");
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
		fillResponse(response);
		reloadCMSInformation(obj);
		prepareBIObjectDetailPage(response, obj, biObjPar, biObjPar.getId().toString(), modality, false, false);
		
	}


	private void lookupReturnHandler(SourceBean request, SourceBean response) throws EMFUserError, SourceBeanException {
		
		BIObject obj = (BIObject) session.getAttribute("LookupBIObject");
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReturnHandler",
				" BIObject = " + obj);
		
		BIObjectParameter biObjPar = (BIObjectParameter) session.getAttribute("LookupBIObjectParameter");
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReturnHandler",
				" BIObjectParameter = " + biObjPar);
		
		String modality = (String) session.getAttribute("modality");
		if(modality == null) modality = (String)session.getAttribute("modalityBkp");
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReturnHandler",
				" modality = " + modality);
		
		
		actor = (String) session.getAttribute("actor");
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReturnHandler",
				" actor = " + actor);
		
		
		String newParIdStr = (String) session.getAttribute("PAR_ID");
		Integer newParIdInt = Integer.valueOf(newParIdStr);
		Parameter newParameter = new Parameter();
		newParameter.setId(newParIdInt);
		biObjPar.setParameter(newParameter);
		biObjPar.setParID(newParIdInt);
		
		delateLoopbackContext();
		
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
		fillResponse(response);
		reloadCMSInformation(obj);
		prepareBIObjectDetailPage(response, obj, biObjPar, biObjPar.getId().toString(), modality, false, false);
		session.delAttribute("PAR_ID");
	}

	/**
	 * Gets the detail of a BI object  choosed by the user from the 
	 * BI objects list. It reaches the key from the request and asks to the DB all detail
	 * BI objects information, by calling the method <code>loadBIObjectForDetail</code>.
	 *   
	 * @param request The request Source Bean
	 * @param response The response Source Bean
	 * @throws EMFUserError If an exception occurs
	 */
	private void getDetailObject(SourceBean request, SourceBean response)
			throws EMFUserError {
		try {
			response.setAttribute(SpagoBIConstants.ACTOR, actor);
			String idStr = (String) request
					.getAttribute(ObjectsTreeConstants.OBJECT_ID);
			Integer id = new Integer(idStr);
			BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(
					id);
			if (obj == null) {
				SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE,
						"DetailBIObjectModule", "getDetailObject",
						"BIObject with id "+id+" cannot be retrieved.");
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1040);
				errorHandler.addError(error);
				return;
			}
			Object selectedObjParIdObj = request.getAttribute("selected_obj_par_id");
			String selectedObjParIdStr = "";
			if (selectedObjParIdObj != null) {
				int selectedObjParId = findBIObjParId(selectedObjParIdObj);
				selectedObjParIdStr = new Integer(selectedObjParId).toString();
			}
			fillResponse(response);
			prepareBIObjectDetailPage(response, obj, null, selectedObjParIdStr, ObjectsTreeConstants.DETAIL_MOD, true, true);
		} catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE,
					"DetailBIObjectModule", "getDetailObject",
					"Cannot fill response container", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	/**
	 * Inserts/Modifies the detail of a BI Object according to the user request.
	 * When a BI Object is modified, the <code>modifyBIObject</code> method is
	 * called; when a new BI Object is added, the <code>insertBIObject</code>method
	 * is called. These two cases are differentiated by the <code>mod</code>
	 * String input value .
	 * 
	 * @param request
	 *            The request information contained in a SourceBean Object
	 * @param mod
	 *            A request string used to differentiate insert/modify
	 *            operations
	 * @param response
	 *            The response SourceBean
	 * @throws EMFUserError
	 *             If an exception occurs
	 * @throws SourceBeanException
	 *             If a SourceBean exception occurs
	 */
	private void modBIObject(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {

		try {
			// as the request is a multipart request, the fillRequestContainer popolates correctly the service request
			fillRequestContainer(request, errorHandler);
			BIObject obj = recoverBIObjectDetails(request, mod);
			response.setAttribute(SpagoBIConstants.ACTOR, actor);
			String selectedObjParIdStr = null;
			if (mod.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {
				BIObjectParameter biObjPar = null;
				Object selectedObjParIdObj = request
						.getAttribute("selected_obj_par_id");
				Object deleteBIObjectParameter = request.getAttribute("deleteBIObjectParameter");
				
				if (selectedObjParIdObj != null) {
					// it is requested to view another BIObjectParameter than the one visible
					int selectedObjParId = findBIObjParId(selectedObjParIdObj);
					selectedObjParIdStr = new Integer (selectedObjParId).toString();
					String saveBIObjectParameter = (String) request.getAttribute("saveBIObjectParameter");
					if (saveBIObjectParameter != null && saveBIObjectParameter.equalsIgnoreCase("yes")) {
						// it is requested to save the visible BIObjectParameter
						ValidationCoordinator.validate("PAGE", "BIObjectParameterValidation", this);
						biObjPar = recoverBIObjectParameterDetails(request, obj.getId());
						// If it's a new BIObjectParameter or if the Parameter was changed controls 
						// that the BIObjectParameter url name is not already in use
						urlNameControl(obj.getId(), biObjPar);
						fillResponse(response);
						reloadCMSInformation(obj);
						verifyForDependencies(biObjPar);
						
						// if there are some validation errors into the errorHandler does not write into DB
						Collection errors = errorHandler.getErrors();
						if (errors != null && errors.size() > 0) {
							Iterator iterator = errors.iterator();
							while (iterator.hasNext()) {
								Object error = iterator.next();
								if (error instanceof EMFValidationError) {
									prepareBIObjectDetailPage(response, obj, biObjPar, biObjPar.getId().toString(), 
											ObjectsTreeConstants.DETAIL_MOD, false, false);
									return;
								}
							}
						}

						IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
						if (biObjPar.getId().intValue() == -1) {
							// it is requested to insert a new BIObjectParameter
							objParDAO.insertBIObjectParameter(biObjPar);
						} else {
							// it is requested to modify a BIObjectParameter
							objParDAO.modifyBIObjectParameter(biObjPar);
						}
						prepareBIObjectDetailPage(response, obj, null, selectedObjParIdStr, ObjectsTreeConstants.DETAIL_MOD, false, true);
						return;
					} else {
						fillResponse(response);
						reloadCMSInformation(obj);
						prepareBIObjectDetailPage(response, obj, null, selectedObjParIdStr, ObjectsTreeConstants.DETAIL_MOD, false, true);
		    			// exits without writing into DB
		    			return;
					}
					
				} else if (deleteBIObjectParameter != null) {
					// it is requested to delete the visible BIObjectParameter
					int objParId = findBIObjParId(deleteBIObjectParameter);
					Integer objParIdInt = new Integer(objParId);
					checkForDependancies(objParIdInt);
					fillResponse(response);
					reloadCMSInformation(obj);
					
					// if there are some validation errors into the errorHandler does not write into DB
					Collection errors = errorHandler.getErrors();
					if (errors != null && errors.size() > 0) {
						Iterator iterator = errors.iterator();
						while (iterator.hasNext()) {
							Object error = iterator.next();
							if (error instanceof EMFValidationError) {
								prepareBIObjectDetailPage(response, obj, biObjPar, objParIdInt.toString(), 
										ObjectsTreeConstants.DETAIL_MOD, false, false);
								return;
							}
						}
					}

					IObjParuseDAO objParuseDAO = DAOFactory.getObjParuseDAO();
					// deletes all the ObjParuse objects associated to this BIObjectParameter 
					List objParuses = objParuseDAO.loadObjParuses(new Integer(objParId));
					if (objParuses != null && objParuses.size() > 0) {
						Iterator it = objParuses.iterator();
						while (it.hasNext()) {
							ObjParuse objParuse = (ObjParuse) it.next();
							objParuseDAO.eraseObjParuse(objParuse);
						}
					}
					// then deletes the BIObjectParameter
					IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
					BIObjectParameter objPar = objParDAO.loadForDetailByObjParId(new Integer(objParId));
					objParDAO.eraseBIObjectParameter(objPar);
					selectedObjParIdStr = "";
					prepareBIObjectDetailPage(response, obj, null, selectedObjParIdStr, ObjectsTreeConstants.DETAIL_MOD, false, true);
					return;
					
				} else {
					// It is request to save the BIObject with also the visible BIObjectParameter
					biObjPar = recoverBIObjectParameterDetails(request, obj.getId());
					// If a new BIParameter was visualized and no fields were inserted, the BIParameter is not validated and saved
					boolean biParameterToBeSaved = true;
					if (GenericValidator.isBlankOrNull(biObjPar.getLabel()) 
							&& biObjPar.getId().intValue() == -1 
							&& GenericValidator.isBlankOrNull(biObjPar.getParameterUrlName())
							&& biObjPar.getParID().intValue() == -1)
						biParameterToBeSaved = false;
					if (biParameterToBeSaved) {
						ValidationCoordinator.validate("PAGE", "BIObjectParameterValidation", this);
						// If it's a new BIObjectParameter or if the Parameter was changed controls 
						// that the BIObjectParameter url name is not already in use
						urlNameControl(obj.getId(), biObjPar);
					}

					ValidationCoordinator.validate("PAGE", "BIObjectValidation", this);
					verifyForDependencies(biObjPar);
					
					// if there are some validation errors into the errorHandler does not write into DB
					Collection errors = errorHandler.getErrors();
					if (errors != null && errors.size() > 0) {
						Iterator iterator = errors.iterator();
						while (iterator.hasNext()) {
							Object error = iterator.next();
							if (error instanceof EMFValidationError) {
								reloadCMSInformation(obj);
								fillResponse(response);
								prepareBIObjectDetailPage(response, obj, biObjPar, biObjPar.getId().toString(), 
										ObjectsTreeConstants.DETAIL_MOD, false, false);
								return;
							}
						}
					}
					
					// it is requested to modify the main values of the BIObject
					DAOFactory.getBIObjectDAO().modifyBIObject(obj);
	    			// reloads the BIObject with the updated CMS information
	    			obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(obj.getId());
	    			
	    			if (biParameterToBeSaved) {
						IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
						if (biObjPar.getId().intValue() == -1) {
							// it is requested to insert a new BIObjectParameter
							objParDAO.insertBIObjectParameter(biObjPar);
							// reload the BIObjectParameter with the given url name
							biObjPar = reloadBIObjectParameter(obj.getId(), biObjPar.getParameterUrlName());
						} else {
							// it is requested to modify a BIObjectParameter
							objParDAO.modifyBIObjectParameter(biObjPar);
						}
						selectedObjParIdStr = biObjPar.getId().toString();
	    			} else selectedObjParIdStr = "-1";
				}

    		} else {
    			ValidationCoordinator.validate("PAGE", "BIObjectValidation", this);
    			selectedObjParIdStr = "-1";
    			
				// if there are some validation errors into the errorHandler does not write into DB
				Collection errors = errorHandler.getErrors();
				if (errors != null && errors.size() > 0) {
					Iterator iterator = errors.iterator();
					while (iterator.hasNext()) {
						Object error = iterator.next();
						if (error instanceof EMFValidationError) {
		    				obj.setTemplateVersions(new TreeMap());
		    				TemplateVersion tv = new TemplateVersion();
		    				tv.setVersionName("");
		    				obj.setCurrentTemplateVersion(tv);
							fillResponse(response);
							prepareBIObjectDetailPage(response, obj, null, selectedObjParIdStr, 
									ObjectsTreeConstants.DETAIL_INS, false, false);
							return;
						}
					}
				}
    			
    			// inserts into DB the new BIObject
    			DAOFactory.getBIObjectDAO().insertBIObject(obj);
    			// reloads the BIObject with the correct Id and empty CMS information
    			obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(obj.getId());
    		}
			
			Object saveAndGoBack = request.getAttribute("saveAndGoBack");
			if (saveAndGoBack != null) {
				// it is request to save the main BIObject details and to go back
				response.setAttribute("loopback", "true");
			} else {
				// it is requested to save and remain in the BIObject detail page
				fillResponse(response);
				prepareBIObjectDetailPage(response, obj, null, selectedObjParIdStr, ObjectsTreeConstants.DETAIL_MOD, true, true);
			}

		} catch (EMFUserError error) {			
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","modBIObject","Cannot fill response container", error  );
			throw error;
		} catch (Exception ex) {			
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","modBIObject","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	/**
	 * Controls if there are some BIObjectParameter objects that depend by the BIObjectParameter object
	 * at input, given its id.
	 * 
	 * @param objParFatherId The id of the BIObjectParameter object to check
	 * @throws EMFUserError
	 */
	private void checkForDependancies(Integer objParFatherId) throws EMFUserError {
		IObjParuseDAO objParuseDAO = DAOFactory.getObjParuseDAO();
		List objParametersCorrelated = objParuseDAO.getDependencies(objParFatherId);
		if (objParametersCorrelated != null && objParametersCorrelated.size() > 0) {
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE,
					DetailBIObjectModule.MODULE_PAGE);
			Vector v = new Vector();
			v.add(objParametersCorrelated.toString());
			EMFValidationError error = new EMFValidationError(EMFErrorSeverity.ERROR, 1049,
					v, params);
			errorHandler.addError(error);
		}
	}

	/**
	 * Before modifing a BIObjectParameter (not inserting), this method must be invoked in order to verify that the BIObjectParameter
	 * stored into db (to be modified as per the BIObjectParameter in input) has dependencies associated; if it is the case,
	 * verifies that the associated Parameter was not changed. In case of changed Parameter adds a EMFValidationError into the error handler.
	 * 
	 * @param objPar The BIObjectParameter to verify
	 * @throws EMFUserError 
	 */
	private void verifyForDependencies (BIObjectParameter objPar) throws EMFUserError {
		Integer objParId = objPar.getId();
		if (objParId == null || objParId.intValue() == -1) {
			// it means that the BIObjectParameter in input must be inserted, not modified
			return;
		}
		// Controls that, if the are some dependencies for the BIObjectParameter, the associated parameter was not changed
		IObjParuseDAO objParuseDAO = DAOFactory.getObjParuseDAO();
		List correlations = objParuseDAO.loadObjParuses(objParId);
		if (correlations != null && correlations.size() > 0) {
			IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
			BIObjectParameter initialObjPar = objParDAO.loadForDetailByObjParId(objParId);
			if (initialObjPar.getParID().intValue() != objPar.getParID().intValue()) {
				// the ParameterUse was changed to manual input or the lov id was changed
				HashMap params = new HashMap();
				params.put(AdmintoolsConstants.PAGE, "DetailBIObjectPage");
				Vector vector = new Vector();
				EMFValidationError error = new EMFValidationError(EMFErrorSeverity.ERROR, 1061, vector, params);
				errorHandler.addError(error);
				return;
			}
		}
	}

	/**
	 * Controls that the BIObjectParameter url name is not in use by another BIObjectParameter
	 * 
	 * @param objId The id of the document
	 * @param biObjPar The BIObjectParameter to control before inserting/modifying
	 */
	private void urlNameControl(Integer objId, BIObjectParameter biObjPar) {
		if (objId == null || objId.intValue() < 0 || biObjPar == null || biObjPar.getParameterUrlName() == null) 
			return;
		try {
			IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
			List paruses = objParDAO.loadBIObjectParametersById(objId);
			Iterator it = paruses.iterator();
			while (it.hasNext()) {
				BIObjectParameter aBIObjectParameter = (BIObjectParameter) it.next();
				if (aBIObjectParameter.getParameterUrlName().equals(biObjPar.getParameterUrlName()) 
						&& !aBIObjectParameter.getId().equals(biObjPar.getId())) {
					HashMap params = new HashMap();
					params.put(AdmintoolsConstants.PAGE,
							DetailBIObjectModule.MODULE_PAGE);
					EMFValidationError error = new EMFValidationError(EMFErrorSeverity.ERROR, 1046,
							new Vector(), params);
					errorHandler.addError(error);
				}
			}
		} catch (EMFUserError e) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailBIObjectModule","urlNameControl","Error while url name control", e);
		}
		
	}
	
	private BIObjectParameter reloadBIObjectParameter(Integer objId, String objParUrlName) throws EMFInternalError, EMFUserError {
		if (objId == null || objId.intValue() < 0 || objParUrlName == null || objParUrlName.trim().equals(""))
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "Invalid input data for method reloadBIObjectParameter in DetailBIObjectModule");
		BIObjectParameter objPar = null;
		try {
			IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
			List paruses = objParDAO.loadBIObjectParametersById(objId);
			Iterator it = paruses.iterator();
			while (it.hasNext()) {
				BIObjectParameter aBIObjectParameter = (BIObjectParameter) it.next();
				if (aBIObjectParameter.getParameterUrlName().equals(objParUrlName)) {
					objPar = aBIObjectParameter;
					break;
				}
			}
		} catch (EMFUserError e) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailBIObjectModule","reloadBIObjectParameter","Cannot reload BIObjectParameter", e);
		}
		if (objPar == null) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailBIObjectModule","reloadBIObjectParameter","BIObjectParameter with url name '"+ objParUrlName +"' not found.");
			objPar = createNewBIObjectParameter(objId);
		}
		return objPar;
	}

	/**
	 * Reloads the CMS information of a BIObject: the versioned templates.
	 * Changes the current version template if it is required but without 
	 * writing on CMS.
	 * Other details remain unchanged.
	 * 
	 * @param obj THe BIObject that will be filled with its own CMS information.
	 * @throws EMFUserError
	 */
	private void reloadCMSInformation(BIObject obj) throws EMFUserError {
		IBIObjectDAO objDao = DAOFactory.getBIObjectDAO();
		BIObject temp = objDao.loadBIObjectForDetail(obj.getId());
		TreeMap templates = temp.getTemplateVersions();
		TemplateVersion currentVersion = temp.getCurrentTemplateVersion();
		obj.setTemplateVersions(templates);
		String requiredCurrVerName = obj.getNameCurrentTemplateVersion();
		if (requiredCurrVerName != null && !requiredCurrVerName.equalsIgnoreCase(currentVersion.getVersionName())) {
			// it is requested to change the current version template
			Collection values = templates.values();
			Iterator it = values.iterator();
			while (it.hasNext()) {
				TemplateVersion template = (TemplateVersion) it.next();
				if (requiredCurrVerName.equals(template.getVersionName())) {
					obj.setCurrentTemplateVersion(template);
					break;
				}
			}
		} else obj.setCurrentTemplateVersion(currentVersion);
		
		if (obj.getCurrentTemplateVersion() == null) {
			// the required version template was not found
			obj.setCurrentTemplateVersion(currentVersion);
			SpagoBITracer.warning(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","reloadCMSInformation","The required version template was not found");
		}
	}


	/**
	 * Fills the response SourceBean with the elements that will be displayed in the BIObject detail page: 
	 * the BIObject itself and the required BIObjectParameter.
	 * 
	 * @param response The response SourceBean to be filled
	 * @param obj The BIObject to be displayed
	 * @param biObjPar The BIObjectParameter to be displayed: if it is null the selectedObjParIdStr will be considered.
	 * @param selectedObjParIdStr The id of the BIObjectParameter to be displayed.
	 * 			If it is blank or null the first BIObjectParameter will be diplayed but in case the BIObject 
	 * 			has no BIObjectParameter a new empty BIObjectParameter will be displayed.
	 * 			If it is "-1" a new empty BIObjectParameter will be displayed.
	 * @param detail_mod The modality
	 * @param initialBIObject Boolean: if true the BIObject to be visualized is the initial BIObject and 
	 * 			a clone will be put in session.
	 * @param initialBIObjectParameter Boolean: if true the BIObjectParameter to be visualized is the initial BIObjectParameter and 
	 * 			a clone will be put in session.
	 * @throws SourceBeanException
	 * @throws EMFUserError
	 */
	private void prepareBIObjectDetailPage(SourceBean response, BIObject obj,
			BIObjectParameter biObjPar, String selectedObjParIdStr,
			String detail_mod, boolean initialBIObject,
			boolean initialBIObjectParameter) throws SourceBeanException,
			EMFUserError {
		
		List biObjParams = DAOFactory.getBIObjectParameterDAO()
				.loadBIObjectParametersById(obj.getId());
		obj.setBiObjectParameters(biObjParams);
		if (biObjPar == null) {
			if (selectedObjParIdStr == null || "".equals(selectedObjParIdStr)) {
				if (biObjParams == null || biObjParams.size() == 0) {
					biObjPar = createNewBIObjectParameter(obj.getId());
					selectedObjParIdStr = "-1";
				} else {
					biObjPar = (BIObjectParameter) biObjParams.get(0);
					selectedObjParIdStr = biObjPar.getId().toString();
				}
			} else if ("-1".equals(selectedObjParIdStr)) {
				biObjPar = createNewBIObjectParameter(obj.getId());
				selectedObjParIdStr = "-1";
			} else {
				int selectedObjParId = Integer.parseInt(selectedObjParIdStr);
				Iterator it = biObjParams.iterator();
				while (it.hasNext()) {
					biObjPar = (BIObjectParameter) it.next();
					if (biObjPar.getId().equals(new Integer(selectedObjParId)))
						break;
				}
			}
		}

		response.setAttribute("selected_obj_par_id", selectedObjParIdStr);
		response.setAttribute(NAME_ATTR_OBJECT, obj);
		response.setAttribute(NAME_ATTR_OBJECT_PAR, biObjPar);
		
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule", "prepareBIObjectDetailPage", "XXXXXXXXXX " + detail_mod);
		
		response.setAttribute(ObjectsTreeConstants.MODALITY, detail_mod);
		
		if (initialBIObject) {
			BIObject objClone = clone(obj);
			session.setAttribute("initial_BIObject", objClone);
		}

		if (initialBIObjectParameter) {
			BIObjectParameter biObjParClone = clone(biObjPar);
			session.setAttribute("initial_BIObjectParameter", biObjParClone);
		}
		
	}


	private BIObjectParameter clone (BIObjectParameter biObjPar) {
		
		if (biObjPar == null) return null;
		
		BIObjectParameter objParClone = new BIObjectParameter();
		objParClone.setId(biObjPar.getId());
		objParClone.setBiObjectID(biObjPar.getBiObjectID());
		objParClone.setLabel(biObjPar.getLabel());
		objParClone.setModifiable(biObjPar.getModifiable());
		objParClone.setMultivalue(biObjPar.getMultivalue());
		objParClone.setParameter(biObjPar.getParameter());
		objParClone.setParameterUrlName(biObjPar.getParameterUrlName());
		objParClone.setParameterValues(biObjPar.getParameterValues());
		objParClone.setParID(biObjPar.getParID());
		objParClone.setProg(biObjPar.getProg());
		objParClone.setRequired(biObjPar.getRequired());
		objParClone.setVisible(biObjPar.getVisible());
		return objParClone;
	}


	private BIObject clone (BIObject obj) {
		
		if (obj == null) return null;
		
		BIObject objClone = new BIObject();
		objClone.setBiObjectTypeCode(obj.getBiObjectTypeCode());
		objClone.setBiObjectTypeID(obj.getBiObjectTypeID());
		objClone.setCurrentTemplateVersion(obj.getCurrentTemplateVersion());
		objClone.setDescription(obj.getDescription());
		objClone.setEncrypt(obj.getEncrypt());
		objClone.setVisible(obj.getVisible());
		objClone.setEngine(obj.getEngine());
		objClone.setId(obj.getId());
		objClone.setLabel(obj.getLabel());
		objClone.setName(obj.getName());
		objClone.setNameCurrentTemplateVersion(obj.getNameCurrentTemplateVersion());
		objClone.setPath(obj.getPath());
		objClone.setRelName(obj.getRelName());
		objClone.setStateCode(obj.getStateCode());
		objClone.setStateID(obj.getStateID());
		objClone.setTemplate(obj.getTemplate());
		
		return objClone;
	}


	private BIObjectParameter recoverBIObjectParameterDetails(SourceBean request, Integer biobjIdInt) {

		String idStr = (String) request.getAttribute("objParId");
		Integer idInt = null;
		if (idStr == null || idStr.trim().equals("")) idInt = new Integer (-1);
		else idInt = new Integer(idStr);
		String parIdStr = (String) request.getAttribute("par_id");
		Integer parIdInt = null;
		if (parIdStr == null || parIdStr.trim().equals("")) 
			parIdInt = new Integer (-1);
		else parIdInt = new Integer (parIdStr);
		String label = (String) request.getAttribute("objParLabel");
	    String parUrlNm = (String)request.getAttribute("parurl_nm");
		String priorityStr = (String)request.getAttribute("priority");
		Integer priority = new Integer(priorityStr);
		String reqFl = (String)request.getAttribute("req_fl");
		Integer reqFlBD = new Integer(reqFl);
		String modFl = (String) request.getAttribute("mod_fl");
		Integer modFlBD = new Integer(modFl);
		String viewFl = (String) request.getAttribute("view_fl");
		Integer viewFlBD = new Integer(viewFl);
		String multFl = (String) request.getAttribute("mult_fl");
		Integer multFlBD = new Integer(multFl);
	   
		BIObjectParameter objPar  = new BIObjectParameter();
		objPar.setId(idInt);
		objPar.setBiObjectID(biobjIdInt);
		objPar.setParID(parIdInt);
        Parameter par = new Parameter();
        par.setId(parIdInt);
        objPar.setParameter(par);
        objPar.setLabel(label);
        objPar.setParameterUrlName(parUrlNm);
        objPar.setRequired(reqFlBD);
        objPar.setModifiable(modFlBD);
        objPar.setVisible(viewFlBD);
        objPar.setMultivalue(multFlBD);
        objPar.setPriority(priority);
 
		return objPar;
	}


	private BIObjectParameter createNewBIObjectParameter(Integer objId) throws EMFUserError {
		BIObjectParameter biObjPar = new BIObjectParameter();
		biObjPar.setId(new Integer(-1));
		biObjPar.setParID(new Integer(-1));
		biObjPar.setBiObjectID(objId);
		biObjPar.setLabel("");
		biObjPar.setModifiable(new Integer(0));
		biObjPar.setMultivalue(new Integer(0));
		biObjPar.setParameter(null);
		biObjPar.setParameterUrlName("");
		biObjPar.setProg(new Integer(0));
		biObjPar.setRequired(new Integer(0));
		biObjPar.setVisible(new Integer(0));
		int objParsNumber = 0;
		IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
		List objPars = objParDAO.loadBIObjectParametersById(objId);
		if (objPars != null) objParsNumber = objPars.size();
		biObjPar.setPriority(new Integer(objParsNumber + 1));
		return biObjPar;
	}


	private BIObject recoverBIObjectDetails(SourceBean request, String mod) throws EMFUserError {
		
		BIObject obj = new BIObject();
		
		UploadedFile uploaded = (UploadedFile) request
				.getAttribute("UPLOADED_FILE");
		String idStr = (String) request.getAttribute("id");
		Integer id = new Integer(idStr);
		String name = (String) request.getAttribute("name");
		String label = (String) request.getAttribute("label");
		String description = (String) request.getAttribute("description");
		String relname = (String) request.getAttribute("relname");
		String criptableStr = (String) request.getAttribute("criptable");
		Integer encrypt = new Integer(criptableStr);
		String visibleStr = (String) request.getAttribute("visible");
		Integer visible = new Integer(visibleStr);
		String path = (String) request.getAttribute("path");
		
		String typeAttr = (String) request.getAttribute("type");
		StringTokenizer tokentype = new StringTokenizer(typeAttr, ",");
		String typeIdStr = tokentype.nextToken();
		Integer typeIdInt = new Integer(typeIdStr);
		String typeCode = tokentype.nextToken();
		String engineIdStr = (String) request.getAttribute("engine");
		Engine engine = null;
		if (engineIdStr == null || engineIdStr.equals("")) {
			List engines = DAOFactory.getEngineDAO().loadAllEnginesForBIObjectType(typeCode);
			if (engines.size() == 0) {
				HashMap errorParams = new HashMap();
				errorParams.put(AdmintoolsConstants.PAGE, MODULE_PAGE);
				Domain domain = DAOFactory.getDomainDAO().loadDomainById(typeIdInt);
				Vector vector = new Vector();
				vector.add(domain.getValueName());
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1064, vector, errorParams);
			}
			engine = (Engine) engines.get(0);
		} else {
			Integer engineIdInt = new Integer(engineIdStr);
			engine = DAOFactory.getEngineDAO().loadEngineByID(engineIdInt);
		}

		String stateAttr = (String) request.getAttribute("state");
		StringTokenizer tokenState = new StringTokenizer(stateAttr, ",");
		String stateIdStr = tokenState.nextToken();
		Integer stateId = new Integer(stateIdStr);
		String stateCode = tokenState.nextToken();
		
		List functionalities = new ArrayList();
		List functionalitiesStr = request.getAttributeAsList(ObjectsTreeConstants.FUNCT_ID);
		if (functionalitiesStr.size() == 0) {
			HashMap errorParams = new HashMap();
			errorParams.put(AdmintoolsConstants.PAGE, MODULE_PAGE);
			errorParams.put(SpagoBIConstants.ACTOR, actor);
			EMFValidationError error = null;
			error = new EMFValidationError(EMFErrorSeverity.ERROR, 1008, new Vector(), errorParams);
			getErrorHandler().addError(error);
		} else {
			for (Iterator it = functionalitiesStr.iterator(); it.hasNext(); ) {
				String functIdStr = (String) it.next();
				Integer functId = new Integer (functIdStr);
				functionalities.add(functId);
			}
		}
		
		// label control
		BIObject aBIObject = DAOFactory.getBIObjectDAO().loadBIObjectByLabel(label);
		if (aBIObject != null && !aBIObject.getId().equals(id)) {
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE,
					DetailBIObjectModule.MODULE_PAGE);
			EMFValidationError error = new EMFValidationError(EMFErrorSeverity.ERROR,
					1056, new Vector(), params);
			getErrorHandler().addError(error);
		}
		
		// in case the user is not an administrator, folders where the user is not a developer must remain
		if (!SpagoBIConstants.ADMIN_ACTOR.equalsIgnoreCase(actor) && mod.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {
			IBIObjectDAO objDAO = DAOFactory.getBIObjectDAO();
			BIObject prevObj = objDAO.loadBIObjectById(id);
			List prevFuncsId = prevObj.getFunctionalities();
			for (Iterator it = prevFuncsId.iterator(); it.hasNext(); ) {
				Integer funcId = (Integer) it.next();
				if (!ObjectsAccessVerifier.canDev(stateCode, funcId, profile)) {
					functionalities.add(funcId);
				}
			}
		}
		// in case the user is a local administrator, folders where he cannot admin must remain
		if (SpagoBIConstants.ADMIN_ACTOR.equalsIgnoreCase(actor) 
				&& initialPath != null && !initialPath.trim().equals("")  
				&& mod.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {
			IBIObjectDAO objDAO = DAOFactory.getBIObjectDAO();
			BIObject prevObj = objDAO.loadBIObjectById(id);
			List functionalitiesId = prevObj.getFunctionalities();
			Iterator it = functionalitiesId.iterator();
			while (it.hasNext()) {
				Integer folderId = (Integer) it.next();
				LowFunctionality folder = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByID(folderId, false);
				String folderPath = folder.getPath();
				if (!folderPath.equalsIgnoreCase(initialPath) && !folderPath.startsWith(initialPath + "/")) {
					functionalities.add(folderId);
				}
			}
		}
		
		// if the user is a global administrator, he can see all functionalities
		
		obj.setFunctionalities(functionalities);

		if (mod.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {
			String nameCurTempVer = (String) request.getAttribute("versionTemplate");
			if (nameCurTempVer != null) {
				obj.setNameCurrentTemplateVersion(nameCurTempVer);
			}
		}
		
		obj.setTemplate(uploaded);
		obj.setBiObjectTypeCode(typeCode);
		obj.setBiObjectTypeID(typeIdInt);
		obj.setDescription(description);
		obj.setEncrypt(encrypt);
		obj.setVisible(visible);
		obj.setEngine(engine);
		obj.setId(id);
		obj.setName(name);
		obj.setLabel(label);
		obj.setRelName(relname);
		obj.setStateCode(stateCode);
		obj.setStateID(stateId);
		obj.setPath(path);
		
		return obj;
	}


	public int findBIObjParId (Object objParIdObj) {
		String objParIdStr = "";
		if (objParIdObj instanceof String) {
			objParIdStr = (String) objParIdObj;
		} else if (objParIdObj instanceof List) {
			List objParIdList = (List) objParIdObj;
			Iterator it = objParIdList.iterator();
			while (it.hasNext()) {
				Object item = it.next();
				if (item instanceof SourceBean) continue;
				if (item instanceof String) objParIdStr = (String) item;
			}
		}
		int objParId = Integer.parseInt(objParIdStr);
		return objParId;
	}
	
	/**
	 * Deletes a BI Object choosed by user. If the folder id is specified, it deletes only the instance 
	 * of the object in that folder. If the folder id is not specified: if the user is an administrator 
	 * the object is deleted from all the folders, else it is deleted from the folder on which the user 
	 * is a developer.
	 * 
	 * @param request	The request SourceBean
	 * @param mod	A request string used to differentiate delete operation
	 * @param response	The response SourceBean
	 * @throws EMFUserError	If an Exception occurs
	 * @throws SourceBeanException If a SourceBean Exception occurs
	 */
	private void delDetailObject(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {
		BIObject obj = null;
		try {
			String idObjStr = (String) request.getAttribute(ObjectsTreeConstants.OBJECT_ID);
			Integer idObj = new Integer(idObjStr);
			IBIObjectDAO objdao = DAOFactory.getBIObjectDAO();
			obj = objdao.loadBIObjectById(idObj);
			String idFunctStr = (String) request.getAttribute(ObjectsTreeConstants.FUNCT_ID);
			if (idFunctStr != null) {
				Integer idFunct = new Integer(idFunctStr);
				if (SpagoBIConstants.ADMIN_ACTOR.equals(actor)) {
					// deletes the document from the specified folder, no matter the permissions
					objdao.eraseBIObject(obj, idFunct);
				} else {
					// deletes the document from the specified folder if the profile is a developer for that folder
					if (ObjectsAccessVerifier.canDev(obj.getStateCode(), idFunct, profile)) {
						objdao.eraseBIObject(obj, idFunct);
					}
				}
			} else {
				if (SpagoBIConstants.ADMIN_ACTOR.equals(actor)) {
					if (initialPath != null && !initialPath.trim().equals("")) {
						// in case of local administrator, deletes the document in the folders where he can admin
						List funcsId = obj.getFunctionalities();
						for (Iterator it = funcsId.iterator(); it.hasNext(); ) {
							Integer idFunct = (Integer) it.next();
							LowFunctionality folder = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByID(idFunct, false);
							String folderPath = folder.getPath();
							if (folderPath.equalsIgnoreCase(initialPath) || folderPath.startsWith(initialPath + "/")) {
								objdao.eraseBIObject(obj, idFunct);
							}
						}
					} else {
						// deletes the document from all the folders, no matter the permissions
						objdao.eraseBIObject(obj, null);
					}
				} else {
					// deletes the document from all the folders on which the profile is a developer
					List funcsId = obj.getFunctionalities();
					for (Iterator it = funcsId.iterator(); it.hasNext(); ) {
						Integer idFunct = (Integer) it.next();
						if (ObjectsAccessVerifier.canDev(obj.getStateCode(), idFunct, profile)) {
							objdao.eraseBIObject(obj, idFunct);
						}
					}
				}
			}
		} catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","delDetailObject","Cannot erase object", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute("loopback", "true");
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
	}
	
	/**
	 * Instantiates a new <code>BIObject<code> object when a new BI object insertion 
	 * is required, in order to prepare the page for the insertion.
	 * 
	 * @param response The response SourceBean
	 * @throws EMFUserError If an Exception occurred
	 */
	private void newBIObject(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			response.setAttribute(ObjectsTreeConstants.MODALITY, ObjectsTreeConstants.DETAIL_INS);
            BIObject obj = new BIObject();
            obj.setId(new Integer(0));
            obj.setEngine(null);
            obj.setDescription("");
            obj.setLabel("");
            obj.setName("");
            obj.setEncrypt(new Integer(0));
            obj.setVisible(new Integer(1));
            obj.setRelName("");
            obj.setStateID(null);
            obj.setStateCode("");
            obj.setBiObjectTypeID(null);
            obj.setBiObjectTypeCode("");
            List functionalitites = new ArrayList();
            obj.setFunctionalities(functionalitites);
            TemplateVersion curVer = new TemplateVersion();
            curVer.setVersionName("");
            curVer.setDataLoad("");
            obj.setCurrentTemplateVersion(curVer);
            obj.setTemplateVersions(new TreeMap());
            response.setAttribute(NAME_ATTR_OBJECT, obj);
            response.setAttribute(SpagoBIConstants.ACTOR, actor);
            fillResponse(response);       
		} catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","newBIObject","Cannot prepare page for the insertion", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	/**
	 * Fills the response SourceBean with some needed BI Objects information.
	 * 
	 * @param response The SourceBean to fill
	 */
	private void fillResponse(SourceBean response) {
		try {
			IDomainDAO domaindao = DAOFactory.getDomainDAO();
	        List types = domaindao.loadListDomainsByType("BIOBJ_TYPE");
	        // if booklet module is not installed remove from types the booklet type
	        if(!GeneralUtilities.isModuleInstalled("booklets")){
		        Iterator iterdom = types.iterator();
	  		    while(iterdom.hasNext()) {
	  		    	Domain type = (Domain)iterdom.next();
	  		    	if(type.getValueCd().equalsIgnoreCase("BOOKLET")) {
	  		    		types.remove(type);
	  		    		break;
	  		    	}
	  		    }
	        }
	        // load list of states and engines
	        List states = domaindao.loadListDomainsByType("STATE");
	        List engines =  DAOFactory.getEngineDAO().loadAllEngines();
		    response.setAttribute(NAME_ATTR_LIST_ENGINES, engines);
		    response.setAttribute(NAME_ATTR_LIST_OBJ_TYPES, types);
		    response.setAttribute(NAME_ATTR_LIST_STATES, states);
			List functionalities = new ArrayList();
			try {
				if (initialPath != null && !initialPath.trim().equals("")) {
					functionalities = DAOFactory.getLowFunctionalityDAO().loadSubLowFunctionalities(initialPath, false);
					//response.setAttribute(SpagoBIConstants.MODALITY, SpagoBIConstants.FILTER_TREE_MODALITY);
					response.setAttribute(TreeObjectsModule.PATH_SUBTREE, initialPath);
				} else {
					functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(false);
					//response.setAttribute(SpagoBIConstants.MODALITY, SpagoBIConstants.ENTIRE_TREE_MODALITY);
				}
			} catch (EMFUserError e) {
				SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
						"DetailBIObjectsMOdule", 
						"fillResponse", 
						"Error loading functionalities", e);
			}
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
		    
		} catch (Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","fillResponse","Cannot fill the response ", e  );
		}
	}
	
	/**
	 * Fills the request container object with some BIObject and BIObjectParameter information contained into
	 * the request Source Bean (they are all attributes). It is useful for validation process.
	 * 
	 * @param request The request Source Bean 
	 * @throws SourceBeanException If any exception occurred
	 */
	public void fillRequestContainer (SourceBean request, EMFErrorHandler errorHandler) throws Exception{
		RequestContainer req = getRequestContainer();
		String label = (String)request.getAttribute("label");
		String name = (String)request.getAttribute("name");
		String description = (String)request.getAttribute("description");
		String relName = (String)request.getAttribute("relName");
		String engine = (String)request.getAttribute("engine");
		String state = (String)request.getAttribute("state");
		String path = "";
		String objParLabel = (String)request.getAttribute("objParLabel");
		String parurl_nm = (String)request.getAttribute("parurl_nm");
		String par_Id = (String)request.getAttribute("par_Id");
		String req_fl = (String)request.getAttribute("req_fl");
		String mod_fl = (String) request.getAttribute("mod_fl");
		String view_fl = (String) request.getAttribute("view_fl");
		String mult_fl = (String) request.getAttribute("mult_fl");
		Object pathParentObj = request.getAttribute("PATH_PARENT");
		if( (pathParentObj != null) && (!(pathParentObj instanceof String))) {
			errorHandler.addError(new EMFValidationError(EMFErrorSeverity.ERROR, 1032));
		}else {
			String pathParent = (String)pathParentObj;
			if(pathParent != null){
				path = pathParent;
			}
		}
		SourceBean _serviceRequest = req.getServiceRequest();
		_serviceRequest.setAttribute("label",label);
		_serviceRequest.setAttribute("description",description);
		_serviceRequest.setAttribute("name",name);
		_serviceRequest.setAttribute("relName",relName);
		if (engine == null) {
			List engines = DAOFactory.getEngineDAO().loadAllEngines();
			if (engines.size() > 0) {
				engine = ((Engine) engines.get(0)).getId().toString();
			}
		}
		_serviceRequest.setAttribute("engine", engine);
		_serviceRequest.setAttribute("state", state);
		_serviceRequest.setAttribute("path", path);
		_serviceRequest.setAttribute("objParLabel", objParLabel == null ? "" : objParLabel);
		_serviceRequest.setAttribute("parurl_nm", parurl_nm == null ? "" : parurl_nm);
		_serviceRequest.setAttribute("par_Id", par_Id == null ? "" : par_Id);
		_serviceRequest.setAttribute("req_fl", req_fl == null ? "" : req_fl);
		_serviceRequest.setAttribute("mod_fl", mod_fl == null ? "" : mod_fl);
		_serviceRequest.setAttribute("view_fl", view_fl == null ? "" : view_fl);
		_serviceRequest.setAttribute("mult_fl", mult_fl == null ? "" : mult_fl);
	}
	
	public void eraseVersion(SourceBean request, SourceBean response) throws EMFUserError {
		// get object' id and name version
		String ver = (String)request.getAttribute(SpagoBIConstants.VERSION);
		String idStr = (String)request.getAttribute(ObjectsTreeConstants.OBJECT_ID);
		Integer id = new Integer (idStr);
		// get user profile for cms operation
		//SessionContainer permSess = getRequestContainer().getSessionContainer().getPermanentContainer();
		//IEngUserProfile profile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		try {
			BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(id);
//			ConfigSingleton config = ConfigSingleton.getInstance();
//			SourceBean biobjectsPathSB = (SourceBean) config.getAttribute(SpagoBIConstants.CMS_BIOBJECTS_PATH);
//			String biobjectsPath = (String) biobjectsPathSB.getAttribute("path");
//			String pathVer = biobjectsPath + "/" + obj.getUuid() + "/template";
			String pathVer = obj.getPath() + "/template";
			// try to delete the version
			CmsManager manager = new CmsManager();
			DeleteOperation delOp = new DeleteOperation(pathVer, ver);
            manager.execDeleteOperation(delOp);
            // populate response
            obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(id);
			response.setAttribute(SpagoBIConstants.ACTOR, actor);
	        fillResponse(response);
	        prepareBIObjectDetailPage(response, obj, null, "", ObjectsTreeConstants.DETAIL_MOD, false, false);
		} catch (Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","eraseVersion","Cannot erase version", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	/**
	 * Clean the SessionContainer from no more useful objects.
	 * 
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 * @throws SourceBeanException
	 */
	private void exitFromDetail (SourceBean request, SourceBean response) throws SourceBeanException {
		session.delAttribute("initial_BIObject");
		session.delAttribute("initial_BIObjectParameter");
		session.delAttribute("modality");
		session.delAttribute("actor");
		response.setAttribute("loopback", "true");
	}
	
}
