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
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.cms.CmsManager;
import it.eng.spago.cms.operations.DeleteOperation;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.validation.coordinator.ValidationCoordinator;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.TemplateVersion;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.dao.TreeObjectsDAO;
import it.eng.spagobi.utilities.PortletUtilities;
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
import javax.portlet.PortletRequest;

import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.validator.GenericValidator;

/**
 * Implements a module which  handles all BI objects management: 
 * has methods for BI Objects load, detail, modify/insertion and deleting operations. 
 * The <code>service</code> method has  a switch for all these operations, differentiated the ones 
 * from the others by a <code>message</code> String.
 * 
 * @author sulis
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
		if (PortletFileUpload.isMultipartContent((ActionRequest)portletRequest)){
			request = PortletUtilities.getServiceRequestFromMultipartPortletRequest(portletRequest);
		}
		
		RequestContainer requestContainer = this.getRequestContainer();
		session = requestContainer.getSessionContainer();
		
		String message = (String) request.getAttribute("MESSAGEDET");
		actor = (String) request.getAttribute(SpagoBIConstants.ACTOR);
		Object loadParametersLookup =  request.getAttribute("loadParametersLookup");
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service","begin of detail BIObject modify/visualization service with message =" +message);
		errorHandler = getErrorHandler();
		
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule", "service", "The message parameter is null");
				throw userError;
			} 
			if (loadParametersLookup != null){
				lookupLoadHandler (request, message, response);
		    } else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.RETURN_FROM_LOOKUP)){
				lookupReturnHandler(request, response);	
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.RETURN_BACK_FROM_LOOKUP)){
				lookupReturnBackHandler(request,response);
			}  else if (message.trim().equalsIgnoreCase(ObjectsTreeConstants.DETAIL_SELECT)) {
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

	private void lookupReturnBackHandler(SourceBean request, SourceBean response) throws SourceBeanException, EMFUserError {

		BIObject obj = (BIObject) session.getAttribute("LookupBIObject");
		BIObjectParameter biObjPar = (BIObjectParameter) session.getAttribute("LookupBIObjectParameter");
		String modality = (String) session.getAttribute("modality");
		actor = (String) session.getAttribute("actor");
		session.delAttribute("LookupBIObject");
		session.delAttribute("LookupBIObjectParameter");
		session.delAttribute("modality");
		session.delAttribute("actor");
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
		fillResponse(response);
		reloadCMSInformation(obj);
		prepareBIObjectDetailPage(response, obj, biObjPar, biObjPar.getParIdOld().toString(), modality, false, false);
		response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
		
	}


	private void lookupReturnHandler(SourceBean request, SourceBean response) throws EMFUserError, SourceBeanException {
		
		BIObject obj = (BIObject) session.getAttribute("LookupBIObject");
		BIObjectParameter biObjPar = (BIObjectParameter) session.getAttribute("LookupBIObjectParameter");
		String modality = (String) session.getAttribute("modality");
		actor = (String) session.getAttribute("actor");
		String newParIdStr = (String) request.getAttribute("PAR_ID");
		Integer newParIdInt = Integer.valueOf(newParIdStr);
		Parameter newParameter = new Parameter();
		newParameter.setId(newParIdInt);
		biObjPar.setParameter(newParameter);
		biObjPar.setParID(newParIdInt);
		session.delAttribute("LookupBIObject");
		session.delAttribute("LookupBIObjectParameter");
		session.delAttribute("modality");
		session.delAttribute("actor");
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
		fillResponse(response);
		reloadCMSInformation(obj);
		prepareBIObjectDetailPage(response, obj, biObjPar, biObjPar.getParIdOld().toString(), modality, false, false);
		response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
				
	}


	private void lookupLoadHandler(SourceBean request, String message, SourceBean response) throws EMFUserError, SourceBeanException {
		
		BIObject obj = recoverBIObjectDetails(request, message);
		String parIdStr = (String) request.getAttribute("par_id");
		Integer parIdInt = null;
		if (parIdStr == null || parIdStr.trim().equals("")) 
			parIdInt = new Integer (-1);
		else parIdInt = new Integer (parIdStr);
		String parIdOldStr = (String) request.getAttribute("objParIdOld");
		Integer parIdOldInt = new Integer(parIdOldStr);
		BIObjectParameter biObjPar = recoverBIObjectParameterDetails(request, parIdInt, obj.getId());
		biObjPar.setParIdOld(parIdOldInt);
		session.setAttribute("LookupBIObject", obj);
		session.setAttribute("LookupBIObjectParameter", biObjPar);
		session.setAttribute("modality", message);
		session.setAttribute("actor",actor);
		response.setAttribute("lookupLoopback", "true");
		
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
			String path = (String) request
					.getAttribute(ObjectsTreeConstants.PATH);
			BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(
					path);
			if (obj == null) {
				SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE,
						"DetailBIObjectModule", "getDetailObject",
						"BIObject with path "+path+" cannot be retrieved.");
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1040);
				errorHandler.addError(error);
				return;
			}
			fillResponse(response);
			prepareBIObjectDetailPage(response, obj, null, "", ObjectsTreeConstants.DETAIL_MOD, true, true);
			response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
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
			String selectedParIdStr = null;
			if (mod.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {
				BIObjectParameter biObjPar = null;
				Object selectedParIdObj = request
						.getAttribute("selected_par_id");
				Object deleteBIObjectParameter = request.getAttribute("deleteBIObjectParameter");
				
				if (selectedParIdObj != null) {
					// it is requested to view another BIObjectParameter than the one visible
					int selectedParId = findBIObjParId(selectedParIdObj);
					selectedParIdStr = new Integer (selectedParId).toString();
					String saveBIObjectParameter = (String) request.getAttribute("saveBIObjectParameter");
					if (saveBIObjectParameter != null && saveBIObjectParameter.equalsIgnoreCase("yes")) {
						// it is requested to save the visible BIObjectParameter
						validateFields("BIObjectParameterValidation", "PAGE");
						String parIdOldStr = (String) request.getAttribute("objParIdOld");
						Integer parIdOldInt = new Integer(parIdOldStr);
						String parIdStr = (String) request.getAttribute("par_id");
						Integer parIdInt = null;
						if (parIdStr == null || parIdStr.trim().equals("")) parIdInt = new Integer(-1);
						else parIdInt = new Integer (parIdStr);
						// If it's a new BIObjectParameter or if the Parameter was changed controls 
						// that the Parameter is not already in use
						if (!parIdInt.equals(parIdOldInt)) objParControl(request, obj.getId(), parIdInt);
						biObjPar = recoverBIObjectParameterDetails(request, parIdInt, obj.getId());
						biObjPar.setParIdOld(parIdOldInt);
						fillResponse(response);
						reloadCMSInformation(obj);
			    		// if there are some errors, exits without writing into DB
			    		if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			    			prepareBIObjectDetailPage(response, obj, biObjPar, parIdOldInt.toString(), ObjectsTreeConstants.DETAIL_MOD, false, false);
			    			response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
							return;
						}
						IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
						if (parIdOldInt.intValue() == -1) {
							// it is requested to insert a new BIObjectParameter
							objParDAO.insertBIObjectParameter(biObjPar);
						} else {
							// it is requested to modify a BIObjectParameter
							objParDAO.modifyBIObjectParameter(biObjPar);
						}
						prepareBIObjectDetailPage(response, obj, null, selectedParIdStr, ObjectsTreeConstants.DETAIL_MOD, false, true);
						response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
						return;
					} else {
						fillResponse(response);
						reloadCMSInformation(obj);
						prepareBIObjectDetailPage(response, obj, null, selectedParIdStr, ObjectsTreeConstants.DETAIL_MOD, false, true);
						response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
		    			// exits without writing into DB
		    			return;
					}
					
				} else if (deleteBIObjectParameter != null) {
					// it is requested to delete the visible BIObjectParameter
					int parId = findBIObjParId(deleteBIObjectParameter);
					IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
					BIObjectParameter objPar = objParDAO.loadBIObjectParameterForDetail(obj.getId(), new Integer(parId) );
					objParDAO.eraseBIObjectParameter(objPar);
					selectedParIdStr = "";
					fillResponse(response);
					reloadCMSInformation(obj);
					prepareBIObjectDetailPage(response, obj, null, selectedParIdStr, ObjectsTreeConstants.DETAIL_MOD, false, true);
					response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
					return;
					
				} else {
					// It is request to save the BIObject with also the visible BIObjectParameter
					String parIdOldStr = (String) request.getAttribute("objParIdOld");
					Integer parIdOldInt = new Integer(parIdOldStr);
					String parIdStr = (String) request.getAttribute("par_id");
					Integer parIdInt = null;
					if (parIdStr == null || parIdStr.trim().equals("")) parIdInt = new Integer (-1);
					else parIdInt = new Integer (parIdStr);
					biObjPar = recoverBIObjectParameterDetails(request, parIdInt, obj.getId());
					// If a new BIParameter was visualized and no fields were inserted, the BIParameter is not validated and saved
					boolean biParameterToBeSaved = true;
					if (GenericValidator.isBlankOrNull(biObjPar.getLabel()) 
							&& GenericValidator.isBlankOrNull(parIdStr)
							&& GenericValidator.isBlankOrNull(biObjPar.getParameterUrlName())
							&& parIdOldInt.intValue() == -1)
						biParameterToBeSaved = false;
					if (biParameterToBeSaved) {
						validateFields("BIObjectParameterValidation", "PAGE");
						// If it's a new BIObjectParameter or if the Parameter
						// was changed, controls that the Parameter is not already in use
						if (!parIdInt.equals(parIdOldInt))
							objParControl(request, obj.getId(), new Integer(
									parIdStr));
					}

					biObjPar.setParIdOld(parIdOldInt);
					if (biParameterToBeSaved) selectedParIdStr = parIdStr;
					else selectedParIdStr = "-1";

					validateFields("BIObjectValidation", "PAGE");
		    		// if there are some errors, exits without writing into DB
					if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
						reloadCMSInformation(obj);
						fillResponse(response);
						prepareBIObjectDetailPage(response, obj, biObjPar, parIdOldInt.toString(), ObjectsTreeConstants.DETAIL_MOD, false, false);
						response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
						return;
					}
					// it is requested to modify the main values of the BIObject
					DAOFactory.getBIObjectDAO().modifyBIObject(obj);
	    			// reloads the BIObject with the updated CMS information
	    			obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(obj.getPath());
	    			
	    			if (biParameterToBeSaved) {
						IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
						if (parIdOldInt.intValue() == -1) {
							// it is requested to insert a new BIObjectParameter
							objParDAO.insertBIObjectParameter(biObjPar);
						} else {
							// it is requested to modify a BIObjectParameter
							objParDAO.modifyBIObjectParameter(biObjPar);
						}
	    			}
				}

    		} else {
    			validateFields("BIObjectValidation", "PAGE");
	    		// if there are some errors, exits without writing into DB
    			selectedParIdStr = "-1";
    			if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
    				obj.setTemplateVersions(new TreeMap());
    				TemplateVersion tv = new TemplateVersion();
    				tv.setVersionName("");
    				obj.setCurrentTemplateVersion(tv);
					fillResponse(response);
					prepareBIObjectDetailPage(response, obj, null, selectedParIdStr, ObjectsTreeConstants.DETAIL_INS, false, false);
					response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
					return;
				}
    			// inserts into DB the new BIObject
    			DAOFactory.getBIObjectDAO().insertBIObject(obj);
    			// reloads the BIObject with the correct Id and empty CMS information
    			obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(obj.getPath());
    		}
			
			Object saveAndGoBack = request.getAttribute("saveAndGoBack");
			if (saveAndGoBack != null) {
				// it is request to save the main BIObject details and to go back
				response.setAttribute("loopback", "true");
			} else {
				// it is requested to save and remain in the BIObject detail page
				fillResponse(response);
				prepareBIObjectDetailPage(response, obj, null, selectedParIdStr, ObjectsTreeConstants.DETAIL_MOD, true, true);
				response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
			}
			
		} catch (Exception ex) {			
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","modBIObject","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
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
		BIObject temp = objDao.loadBIObjectForDetail(obj.getPath());
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
	 * @param biObjPar The BIObjectParameter to be displayed: if it is null the selectedParIdStr will be considered.
	 * @param selectedParIdStr The id of the Parameter relevant to the BIObjectParameter to be displayed.
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
			BIObjectParameter biObjPar, String selectedParIdStr,
			String detail_mod, boolean initialBIObject,
			boolean initialBIObjectParameter) throws SourceBeanException,
			EMFUserError {
		
		List biObjParams = DAOFactory.getBIObjectParameterDAO()
				.loadBIObjectParametersById(obj.getId());
		obj.setBiObjectParameters(biObjParams);
		if (biObjPar == null) {
			if (selectedParIdStr == null || "".equals(selectedParIdStr)) {
				if (biObjParams == null || biObjParams.size() == 0) {
					biObjPar = createNewBIObjectParameter(obj.getId());
					selectedParIdStr = "-1";
				} else {
					biObjPar = (BIObjectParameter) biObjParams.get(0);
					selectedParIdStr = biObjPar.getParID().toString();
				}
			} else if ("-1".equals(selectedParIdStr)) {
				biObjPar = createNewBIObjectParameter(obj.getId());
				selectedParIdStr = "-1";
			} else {
				int selectedParId = Integer.parseInt(selectedParIdStr);
				Iterator it = biObjParams.iterator();
				while (it.hasNext()) {
					biObjPar = (BIObjectParameter) it.next();
					if (biObjPar.getParID().equals(new Integer(selectedParId)))
						break;
				}
			}
			biObjPar.setParIdOld(biObjPar.getParID());
		}
		// Inverts the BIObject template versions:
		//TreeMap templates = obj.getTemplateVersions();
		//if (templates == null) 
		//	templates = new TreeMap();
		//List invertedTemplates = new ArrayList(templates.size());
		//for (int i = 0; i < templates.size(); i++) {
		//	TemplateVersion version = (TemplateVersion) templates.get(templates.size() - 1 - i);
		//	invertedTemplates.add(i, version);
		//}
		//obj.setTemplateVersions(invertedTemplates);
		
		response.setAttribute("selected_par_id", selectedParIdStr);
		response.setAttribute(NAME_ATTR_OBJECT, obj);
		response.setAttribute(NAME_ATTR_OBJECT_PAR, biObjPar);
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
		objParClone.setBiObjectID(biObjPar.getBiObjectID());
		objParClone.setLabel(biObjPar.getLabel());
		objParClone.setModifiable(biObjPar.getModifiable());
		objParClone.setMultivalue(biObjPar.getMultivalue());
		objParClone.setParameter(biObjPar.getParameter());
		objParClone.setParameterUrlName(biObjPar.getParameterUrlName());
		objParClone.setParameterValues(biObjPar.getParameterValues());
		objParClone.setParID(biObjPar.getParID());
		objParClone.setParIdOld(biObjPar.getParIdOld());
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


	private BIObjectParameter recoverBIObjectParameterDetails(SourceBean request, Integer parIdInt, Integer biobjIdInt) {

		String label = (String) request.getAttribute("objParLabel");
	    String parUrlNm = (String)request.getAttribute("parurl_nm");
		String reqFl = (String)request.getAttribute("req_fl");
		Integer reqFlBD = new Integer(reqFl);
		String modFl = (String) request.getAttribute("mod_fl");
		Integer modFlBD = new Integer(modFl);
		String viewFl = (String) request.getAttribute("view_fl");
		Integer viewFlBD = new Integer(viewFl);
		String multFl = (String) request.getAttribute("mult_fl");
		Integer multFlBD = new Integer(multFl);
	   
		BIObjectParameter objPar  = new BIObjectParameter();
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
 
		return objPar;
	}


	private BIObjectParameter createNewBIObjectParameter(Integer objId) {
		BIObjectParameter biObjPar = new BIObjectParameter();
		biObjPar.setBiObjectID(objId);
		biObjPar.setLabel("");
		biObjPar.setModifiable(new Integer(0));
		biObjPar.setMultivalue(new Integer(0));
		biObjPar.setParameter(null);
		biObjPar.setParameterUrlName("");
		biObjPar.setProg(new Integer(0));
		biObjPar.setRequired(new Integer(0));
		biObjPar.setVisible(new Integer(0));
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
		String typeAttr = (String) request.getAttribute("type");
		StringTokenizer tokentype = new StringTokenizer(typeAttr, ",");
		String typeIdStr = tokentype.nextToken();
		Integer typeIdInt = new Integer(typeIdStr);
		String typeCode = tokentype.nextToken();
		String engineIdStr = (String) request.getAttribute("engine");
		Engine engine = null;
		if (engineIdStr == null) {
			List engines = DAOFactory.getEngineDAO().loadAllEngines();
			if (engines.size() > 0) engine = (Engine) engines.get(0);
		} else {
			Integer engineIdInt = new Integer(engineIdStr);
			engine = DAOFactory.getEngineDAO().loadEngineByID(engineIdInt);
		}

		String stateAttr = (String) request.getAttribute("state");
		StringTokenizer tokenState = new StringTokenizer(stateAttr, ",");
		String stateIdStr = tokenState.nextToken();
		Integer stateId = new Integer(stateIdStr);
		String stateCode = tokenState.nextToken();
		
		if (mod.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_INS)) {
			String pathParent = "";
			List parentPaths = request.getAttributeAsList(ObjectsTreeConstants.PATH_PARENT);
			if(parentPaths.size() != 1) {
				HashMap errorParams = new HashMap();
				errorParams.put(AdmintoolsConstants.PAGE, TreeObjectsModule.MODULE_PAGE);
				errorParams.put(SpagoBIConstants.ACTOR, actor);
				EMFUserError error = null;
				if(parentPaths.size()<1) {
					error = new EMFUserError(EMFErrorSeverity.ERROR, 1008, new Vector(), errorParams);
				} else {
					error = new EMFUserError(EMFErrorSeverity.ERROR, 1009, new Vector(), errorParams);
				}
				getErrorHandler().addError(error);
			} else {
				pathParent = (String)parentPaths.get(0);
			}				
			obj.setPath(pathParent + "/" + typeCode + "_" + label);
		} else {
			String pathObj = (String) request.getAttribute(ObjectsTreeConstants.PATH);
			obj.setPath(pathObj);
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
		obj.setEngine(engine);
		obj.setId(id);
		obj.setName(name);
		obj.setLabel(label);
		obj.setRelName(relname);
		obj.setStateCode(stateCode);
		obj.setStateID(stateId);
		
		return obj;
	}


	public int findBIObjParId (Object parIdObj) {
		String parIdStr = "";
		if (parIdObj instanceof String) {
			parIdStr = (String) parIdObj;
		} else if (parIdObj instanceof List) {
			List parIdList = (List) parIdObj;
			Iterator it = parIdList.iterator();
			while (it.hasNext()) {
				Object item = it.next();
				if (item instanceof SourceBean) continue;
				if (item instanceof String) parIdStr = (String) item;
			}
		}
		int parId = Integer.parseInt(parIdStr);
		return parId;
	}
	
	/**
	 * Deletes a BI Object choosed by user from the engines list.
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
			String path = (String)request.getAttribute(ObjectsTreeConstants.PATH);
        	IBIObjectDAO objdao = DAOFactory.getBIObjectDAO();
			obj = objdao.loadBIObjectForDetail(path);
			objdao.eraseBIObject(obj);
		} catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","delDetailObject","Cannot erase object", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute("loopback", "true");
		response.setAttribute(ObjectsTreeConstants.PATH_PARENT, obj.getPath());
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
            obj.setRelName("");
            obj.setStateID(null);
            obj.setStateCode("");
            obj.setBiObjectTypeID(null);
            obj.setBiObjectTypeCode("");
            TemplateVersion curVer = new TemplateVersion();
            curVer.setVersionName("");
            curVer.setDataLoad("");
            obj.setCurrentTemplateVersion(curVer);
            obj.setTemplateVersions(new TreeMap());
            response.setAttribute(NAME_ATTR_OBJECT, obj);
            response.setAttribute(SpagoBIConstants.ACTOR, actor);
            fillResponse(response);
            response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
       
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
	        List states = domaindao.loadListDomainsByType("STATE");
	        List engines =  DAOFactory.getEngineDAO().loadAllEngines();
		    response.setAttribute(NAME_ATTR_LIST_ENGINES, engines);
		    response.setAttribute(NAME_ATTR_LIST_OBJ_TYPES, types);
		    response.setAttribute(NAME_ATTR_LIST_STATES, states);
		    
		    String ATTR_PATH_SYS_FUNCT = "SPAGOBI.CMS_PATHS.SYSTEM_FUNCTIONALITIES_PATH";
            RequestContainer requestContainer = getRequestContainer();
            SessionContainer sessionContainer = requestContainer.getSessionContainer();
            SessionContainer permanentSession = sessionContainer.getPermanentContainer();
            IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		    SourceBean pathSysFunctSB = (SourceBean)ConfigSingleton.getInstance().getAttribute(ATTR_PATH_SYS_FUNCT);
		    String pathSysFunct = pathSysFunctSB.getCharacters();
	        TreeObjectsDAO objDao = new TreeObjectsDAO();
	        SourceBean dataResponseSysFunct = objDao.getXmlTreeObjects(pathSysFunct, profile);
			response.setAttribute(dataResponseSysFunct);
		    
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
			errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 1032));
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
		
//	/**
//	 * It is a particular method to validate a BI object. The SpagoBI validator cannot be used
//	 * directly with BI objects, while some poblems occurred. So a <code>BIObjectValidator</code>
//	 * class has been written and validation is forced directly into the module, with this
//	 * method.
//	 * 
//	 * @param dinValName The dinamyc validation name
//	 * @param request The request Source Bean
//	 * @return True if validation succeeds, false if not
//	 * @throws Exception
//	 */
//	public boolean validateFields(String dinValName, SourceBean request) throws Exception{
//		ConfigSingleton config = ConfigSingleton.getInstance();
//		SourceBean source = (SourceBean)config.getFilteredSourceBeanAttribute("VALIDATIONS.DINAMIC_VALIDATIONS.DINAMIC_VALIDATION", "NAME", dinValName);
//		fillRequestContainer(request, errorHandler);
//		BIObjectValidator val = new BIObjectValidator("detailBIObjectPage","PAGE");
//		RequestContainer req = getRequestContainer();
//		ResponseContainer res = getResponseContainer();
//		SourceBean validatorSB = (SourceBean)source.getAttribute("SERVICE");
//		boolean validate = val.validate(req,res,validatorSB);
//		return validate;
//	}
	
	public boolean validateFields(String businessName, String businessType) throws Exception {
		RequestContainer requestContainer = getRequestContainer();
		ResponseContainer responseContainer = getResponseContainer();
		boolean validate = ValidationCoordinator.validate(businessType, businessName, requestContainer, responseContainer);
		return validate;
	}
	
	public void eraseVersion(SourceBean request, SourceBean response) throws EMFUserError {
		// get path object and name version
		String ver = (String)request.getAttribute(SpagoBIConstants.VERSION);
		String pathObj = (String)request.getAttribute(SpagoBIConstants.PATH);
		String pathVer = pathObj + "/template";
		// get user profile for cms operation
		SessionContainer permSess = getRequestContainer().getSessionContainer().getPermanentContainer();
		IEngUserProfile profile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		try {
            // try to delete the version
			CmsManager manager = new CmsManager();
			DeleteOperation delOp = new DeleteOperation(pathVer, ver);
            manager.execDeleteOperation(delOp);
			// populate response
			BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(pathObj);
			response.setAttribute(SpagoBIConstants.ACTOR, actor);
	        fillResponse(response);
	        prepareBIObjectDetailPage(response, obj, null, "", ObjectsTreeConstants.DETAIL_MOD, false, false);
	        response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
		} catch (Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","eraseVersion","Cannot erase version", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	

	/**
	 * Controls if a BI object parameter is associated or not to a 
	 * parameter already in use by another BI object parameter;
	 * if it is, the BI Object Parameter cannot be inserted/modified.
	 * 
	 * @param request The SourceBean request
	 * @param biobjId The id of the BIObject
	 * @param parId The id of the BIObjectParameter
	 * @throws EMFUserError
	 */
	private void objParControl (SourceBean request, Integer biobjId, Integer parId) throws EMFUserError{
		List list = DAOFactory.getBIObjectParameterDAO().loadBIObjectParametersById(biobjId);
		Iterator i = list.iterator();
		while(i.hasNext()){
			BIObjectParameter BIobjPar = (BIObjectParameter)i.next();
			Parameter param = BIobjPar.getParameter();
			Integer paramId = param.getId();
			if (paramId.equals(parId)){
				HashMap params = new HashMap();
				params.put(AdmintoolsConstants.PAGE, ListBIObjectParametersModule.MODULE_PAGE);
				params.put(AdmintoolsConstants.OBJECT_ID, biobjId);
				String path = (String) request.getAttribute("path");
				params.put(ObjectsTreeConstants.PATH,path);
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1026, new Vector(), params);
				errorHandler.addError(error);
			}
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