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
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.ExecutionController;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.Subreport;
import it.eng.spagobi.bo.BIObject.SubObjectDetail;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.ISubreportDAO;
import it.eng.spagobi.bo.lov.ILovDetail;
import it.eng.spagobi.bo.lov.LovDetailFactory;
import it.eng.spagobi.bo.lov.LovResultHandler;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Executes a report, according to four phases; each phase is identified by a message string.
 * <p>
 * 1) Creates the page
 * <p>
 * 2) Selects the role
 * <p>
 * 3) From the field input values loads the object and starts execution
 * <p>
 * 4) See Report/Change the report state
 */
public class ExecuteBIObjectModule extends AbstractModule
{
	EMFErrorHandler errorHandler = null;
	ExecutionController execContr = null;
	RequestContainer requestContainer = null;
	SessionContainer session = null;
	SessionContainer permanentSession = null;
	SessionContainer mainSessCont = null;
	
	public static final String MODULE_PAGE = "ExecuteBIObjectPage";	
	public static final String MESSAGE_EXECUTION = "MESSAGEEXEC";	
	
	
	public void init(SourceBean config) {}
	
	
	/**
	 * Manage all the request in order to exec all the different BIObject execution phases 
	 * @param request	The request source bean
	 * @param response 	The response Source bean
	 * @throws Exception If an Exception occurred
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
			
			debug("service", "start service method");
			String messageExec = (String)request.getAttribute(SpagoBIConstants.MESSAGEDET);
			String executionIdentifier =  (String)request.getAttribute("EXECUTION_IDENTIFIER");
			debug("service", "using message" + messageExec);
			errorHandler = getErrorHandler();
			requestContainer = this.getRequestContainer();
			mainSessCont = requestContainer.getSessionContainer();
			permanentSession = mainSessCont.getPermanentContainer();
			
			if(executionIdentifier!=null) {
				session = (SessionContainer)mainSessCont.getAttribute(executionIdentifier);
				if(session==null) {
					session = new SessionContainer();
					mainSessCont.setAttribute(executionIdentifier, session);
				}
				response.setAttribute("EXECUTION_IDENTIFIER", executionIdentifier);
			} else {
				session = requestContainer.getSessionContainer();
			}
			
			
			debug("service", "errorHanlder, requestContainer, session, permanentSession retrived ");
	        execContr = new ExecutionController(); 
	        
			try{
				if(messageExec == null) {
					EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
					SpagoBITracer.critical(AdmintoolsConstants.NAME_MODULE, "ExecuteBIObjectModule", 
										  "service", "The execution-message parameter is null");
					throw userError;
				}
				
				if(messageExec.equalsIgnoreCase(SpagoBIConstants.EXEC_PHASE_CREATE_PAGE))  {
					pageCreationHandler(request, response);
				} else if(messageExec.equalsIgnoreCase(SpagoBIConstants.EXEC_PHASE_RUN_SUBOJECT)) {
					executionSubObjectHandler(request, response);
				} else if(messageExec.equalsIgnoreCase(SpagoBIConstants.EXEC_PHASE_DELETE_SUBOJECT)) {
					deleteSubObjectHandler(request, response);
				} else if(messageExec.equalsIgnoreCase(SpagoBIConstants.EXEC_PHASE_SELECTED_ROLE)) {
					selectRoleHandler(request, response);	
				} else if(messageExec.equalsIgnoreCase(SpagoBIConstants.EXEC_PHASE_RETURN_FROM_LOOKUP)) {
					lookUpReturnHandler(request, response);
				} else if(messageExec.equalsIgnoreCase(SpagoBIConstants.EXEC_PHASE_RUN))  {
					executionHandler(request, response);
				} else if(messageExec.equalsIgnoreCase(SpagoBIConstants.EXEC_CHANGE_STATE)){
					changeStateHandler(request, response);
				} else if(messageExec.equalsIgnoreCase(SpagoBIConstants.EXEC_SNAPSHOT_MESSAGE)) {
					execSnapshotHandler(request, response);
				} else if(messageExec.equalsIgnoreCase(SpagoBIConstants.ERASE_SNAPSHOT_MESSAGE)) {
					eraseSnapshotHandler(request, response);
				} else {	
			   	    SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
			   	    		            "ExecuteBIObjectMOdule", 
			   	    		            "service", 
			   	    		            "Illegal request of service");
			   		errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 102)); 
			   	}
			
			} catch (EMFUserError e) { 
		    	errorHandler.addError(e); 
		    }
    }


	private void eraseSnapshotHandler(SourceBean request, SourceBean response) throws EMFUserError, SourceBeanException {
		String snapshotPath = (String) request.getAttribute(SpagoBIConstants.SNAPSHOT_PATH);
		IBIObjectCMSDAO objectCMSDAO = DAOFactory.getBIObjectCMSDAO();
		objectCMSDAO.deleteSnapshot(snapshotPath);
        // get object from session
        BIObject obj = (BIObject) session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
        // get from the session the execution role
        String role = (String) session.getAttribute(SpagoBIConstants.ROLE);
        // set data in response
        response.setAttribute(ObjectsTreeConstants.OBJECT_ID , obj.getId().toString());
        response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, 
        		 			  SpagoBIConstants.PUBLISHER_LOOPBACK_AFTER_DEL_SUBOBJECT);
        response.setAttribute(SpagoBIConstants.ROLE, role);
	}


	/**
	 * Manage the parameter page creation preaparing and setting into the response all the 
	 * necessary attributes 
	 * @param request The Spago Request SourceBean
	 * @param response The Spago Response SourceBean
	 */
	private void pageCreationHandler(SourceBean request, SourceBean response) throws Exception {
				
		debug("pageCreationHandler", "start pageCreationHandler method");
		
		// get the current user profile
		IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		debug("pageCreationHandler", "user profile retrived " + profile);
		
		// get the id of the object
		String idStr = (String) request.getAttribute(ObjectsTreeConstants.OBJECT_ID);
		String label = (String) request.getAttribute(ObjectsTreeConstants.OBJECT_LABEL);
		debug("pageCreationHandler", "Request parameters: " +
				"biobject id = '" + idStr + "'; object label = '" + label + "'.");
		
		// get the type of actor 
		String actor = "";
		Object actorObj =  request.getAttribute(SpagoBIConstants.ACTOR);
		if (actorObj != null) {
			actor = (String) actorObj;
			session.setAttribute(SpagoBIConstants.ACTOR, actor);
		} else {
			actor = (String) session.getAttribute(SpagoBIConstants.ACTOR);
		}
		if (actor == null) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
					            "ExecuteBIObjectMOdule", 
					            "service", 
					            "Actor parameter was not found neither in request nor in session!!");
	   		errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, "100")); 
	   		return;
		}
		debug("pageCreationHandler", "using actor " + actor);
		
		Integer id = null;
		if (label != null) {
			debug("pageCreationHandler", "Loading biobject with label = '" + label + "' ...");
			BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectByLabel(label);
			if (obj == null) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
						            "ExecuteBIObjectMOdule", 
						            "pageCreationHandler", 
						            "Object with label = '" + label + "' not found!!");
				Vector v = new Vector();
				v.add(label);
		   		errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, "1074", v)); 
		   		return;
			}
			id = obj.getId();
			/*
			 * in case the object is executed by its label (it means that it is called by a link 
			 * in another document) we must verify that the user can see the document
			*/
			boolean canSee = ObjectsAccessVerifier.canSee(obj, profile);
			if (!canSee) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
			            "ExecuteBIObjectMOdule", 
			            "pageCreationHandler", 
			            "Object with label = '" + label + "' cannot be executed by the user!!");
				Vector v = new Vector();
				v.add(label);
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, "1075"));
				return;
			}
			/*
			 * TODO the actor parameter must be updated
			 */
		} else id = new Integer(idStr);
		debug("pageCreationHandler", "BIObject id = " + id);
		
		// get parameters statically defined in portlet preferences
		String userProvidedParametersStr = (String)session.getAttribute(ObjectsTreeConstants.PARAMETERS);
		debug("pageCreationHandler", "using parameters " + userProvidedParametersStr);
		
		// define the variable for execution role
		String role = "";
       	List correctRoles = null;
       	if ((actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)) || 
       	    (actor.equalsIgnoreCase(SpagoBIConstants.USER_ACTOR)) || 
       		(actor.equalsIgnoreCase(SpagoBIConstants.ADMIN_ACTOR)) )
       		correctRoles = DAOFactory.getBIObjectDAO().getCorrectRolesForExecution(id, profile);
       	else
       		correctRoles = DAOFactory.getBIObjectDAO().getCorrectRolesForExecution(id);
       	debug("pageCreationHandler", "correct roles for execution retrived " + correctRoles);
       	
       	// if correct roles is more than one the user has to select one of them
        // put in the response the right inforamtion for publisher in order to show page role selection
		if( correctRoles.size() > 1 ) {
			response.setAttribute("selectionRoleForExecution", "true");
			response.setAttribute("roles", correctRoles);
			response.setAttribute(ObjectsTreeConstants.OBJECT_ID, id);
			debug("pageCreationHandler", "more than one correct roles for execution, redirect to the" +
				  " role selection page"); 		
			return;
		
		// if there isn't correct role put in the error stack a new error
		} else if(correctRoles.size() < 1) {
			    debug("pageCreationHandler", "no correct role for the object execution");
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
						            "ExecuteBIObjectMOdule", 
						            "service", 
						            "Object cannot be executed by no role of the user");
		   		errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 1006)); 
		   		return;
		
		// the list contains only one role which is the right role
		} else {
			role = (String)correctRoles.get(0);
		}
		debug("pageCreationHandler", "using role " + role);
		
		// NOW THE EXECUTION ROLE IS SELECTED 
		// put in session the execution role
		session.setAttribute(SpagoBIConstants.ROLE, role);
		
				
		// based on the role selected (or the only for the user) load the object and put it in session
		BIObject obj = execContr.prepareBIObjectInSession(session, id, role, userProvidedParametersStr);
		Map paramsDescriptionMap = new HashMap();
		List biparams = obj.getBiObjectParameters(); 
	    Iterator iterParams = biparams.iterator();
	    while(iterParams.hasNext()) {
	    	BIObjectParameter biparam = (BIObjectParameter)iterParams.next();
	        String nameUrl = biparam.getParameterUrlName();
	        paramsDescriptionMap.put(nameUrl, "");	        
	    }
		
		session.setAttribute("PARAMS_DESCRIPTION_MAP", paramsDescriptionMap);
		
		
		
		
		//session.delAttribute(ObjectsTreeConstants.PARAMETERS);
		debug("pageCreationHandler", "object retrived and setted into session");
		
		
		
		
		// get the list of the subObjects
		List subObjects = getSubObjectsList(obj, profile);
		debug("pageCreationHandler", "List subobject loaded: " + subObjects);
		// put in response the list of subobject
		response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
        
		// get the list of biobject snapshot
		List snapshots = getSnapshotList(obj);
		debug("pageCreationHandler", "List snapshot loaded: " + snapshots);
		// put in response the list of snapshot 
		response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);
		
					
		// load the object into the Execution controller				
		ExecutionController controller = new ExecutionController();
		controller.setBiObject(obj);
		
	    // try to get the modality
		boolean isSingleObjExec = false;
		String modality = (String) session.getAttribute(SpagoBIConstants.MODALITY);
	   	if (modality != null && modality.equals(SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY)) 
	   		isSingleObjExec = true;
		
		// if the object can be directly executed (because it hasn't any parameter to be
		// filled by the user) and if the object has no subobject / snapshots saved or it is a 
	   	// single object execution modality then execute it directly without pass through parameters page 	
		if (controller.directExecution() &&  
				((subObjects.size() == 0 && snapshots.size() == 0) || isSingleObjExec)
			) {
			debug("pageCreationHandler", "object hasn't any parameter to fill and no subObjects");
	        controlInputParameters(obj.getBiObjectParameters(), profile, role);
			// if there are some errors into the errorHandler does not execute the BIObject
			if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ExecuteBIObjectPageParameter");
				return;
			}
            execute(obj, null, response);
		}
		if(controller.directExecution()) {
			debug("pageCreationHandler", "object has only subobjects but not parameter to fill");
			response.setAttribute("NO_PARAMETERS", "TRUE");
		}
	}
	
		
	
	/**
	 * Called after the user change state selection to pass the BIObject from a state to another  
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 */
	private void changeStateHandler(SourceBean request, SourceBean response) throws Exception {
		// get the type of actor from the session
		String actor = (String)session.getAttribute(SpagoBIConstants.ACTOR);
		// get new state from the request
		String newState = (String)request.getAttribute("newState");
		// get object from the session
		BIObject obj = (BIObject)session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
		// from the string of the new state extract the id and code of the new state
		StringTokenizer tokenState = new StringTokenizer(newState, ",");
		String stateIdStr = tokenState.nextToken();
		Integer stateId = new Integer(stateIdStr);
		String stateCode = tokenState.nextToken();
		// set into the object the new state id and code
		obj.setStateCode(stateCode);
		obj.setStateID(stateId);
		// call the dao in order to modify the object without versioning the content
		DAOFactory.getBIObjectDAO().modifyBIObjectWithoutVersioning(obj); 
		// set data for correct loopback to the navigation tree
		response.setAttribute("isLoop", "true");
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
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
	
	private Object getAttribute(SourceBean request, String attributeName) {
		Object attribute = null;
		attribute = request.getAttribute(attributeName);
		if(attribute == null) {
			attribute = session.getAttribute(attributeName);
			if(attribute != null) session.delAttribute(attributeName);
		}
		return attribute;
	}
	
	public List getAsList(Object o) {		
		ArrayList list = new ArrayList();
		
		if(o instanceof String) {
			String parameterValueFromLookUp = (String)o;        
			list.add(parameterValueFromLookUp);
		}
		else {
			list.addAll((Collection)o);
		}
		
		return list;
	}
	
	/**
	 * Called after the parameter value lookup selection to continue the execution phase  
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 */
	private void lookUpReturnHandler(SourceBean request, SourceBean response) throws Exception {
		// get the object from the session
		BIObject obj = (BIObject)session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
		// get the parameter name and value from the request
		String parameterNameFromLookUp = (String)request.getAttribute("LOOKUP_PARAMETER_NAME");
		if(parameterNameFromLookUp == null) parameterNameFromLookUp = (String)session.getAttribute("LOOKUP_PARAMETER_NAME");
		
		String returnStatus = (String)getAttribute(request, "RETURN_STATUS");		
		if(returnStatus == null) returnStatus = "OK";
		
		Object lookUpValueObj = getAttribute(request, "LOOKUP_VALUE");
		Object lookUpDescObj  = getAttribute(request, "LOOKUP_DESC");
		
		if(lookUpValueObj != null && !returnStatus.equalsIgnoreCase("ABORT")) {
		
			
			List paramValues = getAsList(lookUpValueObj);
			List paramDescriptions = (lookUpDescObj==null)? paramValues: getAsList(lookUpDescObj);
			
			// Set into the righr object parameter the list value
	        List biparams = obj.getBiObjectParameters(); 
	        Iterator iterParams = biparams.iterator();
	        while(iterParams.hasNext()) {
	        	BIObjectParameter biparam = (BIObjectParameter)iterParams.next();
	        	String nameUrl = biparam.getParameterUrlName();
	        	
	        	if (nameUrl.equalsIgnoreCase(parameterNameFromLookUp)){
	        		biparam.setParameterValues(paramValues);
	        		
	        		// refresh also the description
	        		HashMap paramsDescriptionMap = (HashMap)session.getAttribute("PARAMS_DESCRIPTION_MAP");
	        		String desc = "";
	        		for(int i = 0; i < paramDescriptions.size(); i++) {
	        			desc += (i==0?"":";") + paramDescriptions.get(i).toString();
	        		}
	        		paramsDescriptionMap.put(nameUrl, desc);
	        		session.setAttribute("PARAMS_DESCRIPTION_MAP", paramsDescriptionMap);
	        	}
	        }
		}
        
        // put in session the new object
        session.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
        // get the current user profile
		IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
        // get the list of the subObjects
		List subObjects = getSubObjectsList(obj, profile);
		// get the list of biobject snapshot
		List snapshots = getSnapshotList(obj);
		// put in response the list of subobject names
		response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
		// put in response the list of snapshot 
		response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);
		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME , "ExecuteBIObjectPageParameter");
	}
	
	
	

	/**
	 * Called after the user role selection to continue the execution phase  
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 */
	private void selectRoleHandler(SourceBean request, SourceBean response) throws Exception {
		// get the role selected from request
		String role = (String)request.getAttribute("role");
		session.setAttribute(SpagoBIConstants.ROLE, role);
		// get the id of the object
		String idStr = (String)request.getAttribute(ObjectsTreeConstants.OBJECT_ID);
		Integer id = new Integer(idStr);
		// prepare the object in session
		String userProvidedParametersStr = (String)session.getAttribute(ObjectsTreeConstants.PARAMETERS);	
		session.delAttribute(ObjectsTreeConstants.PARAMETERS);
		BIObject obj = execContr.prepareBIObjectInSession(session, id, role, userProvidedParametersStr);		
        // get the current user profile
		IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		// get the list of the subObjects
		List subObjects = getSubObjectsList(obj, profile);
        // put in response the list of subobject names
		response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
		// get the list of biobject snapshot
		List snapshots = getSnapshotList(obj);
		// put in response the list of snapshot 
		response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);
		// set into the execution controlle the object
		ExecutionController controller = new ExecutionController();
		controller.setBiObject(obj);
		
		Map paramsDescriptionMap = new HashMap();
		List biparams = obj.getBiObjectParameters(); 
	    Iterator iterParams = biparams.iterator();
	    while(iterParams.hasNext()) {
	    	BIObjectParameter biparam = (BIObjectParameter)iterParams.next();
	        String nameUrl = biparam.getParameterUrlName();
	        paramsDescriptionMap.put(nameUrl, "");	        
	    }
		
		session.setAttribute("PARAMS_DESCRIPTION_MAP", paramsDescriptionMap);
		
		// if the object can be directly executed (because it hasn't any parameter to be
		// filled by the user) and if the object has no subobject saved then execute it
		// directly without pass for parameters page 	
		if( controller.directExecution() &&  (subObjects.size() == 0) ) {
	        controlInputParameters(obj.getBiObjectParameters(), profile, role);
			// if there are some errors into the errorHandler does not execute the BIObject
			if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ExecuteBIObjectPageParameter");
				return;
			}
			obj.loadTemplate();
            execute(obj, null, response);
		}
		if(controller.directExecution()) {
			response.setAttribute("NO_PARAMETERS", "TRUE");
		}
	}
	
		
	
	/**
	 * Delete a subObject 
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 */
	private void deleteSubObjectHandler(SourceBean request, SourceBean response) throws Exception {
		// get the current user profile
		IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
        // get object from session
        BIObject obj = (BIObject)session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
        // get name of the subobject
        String subObjName = (String)request.getAttribute("NAME_SUB_OBJECT");
        // get path of the parent object 
        String path = obj.getPath();
        // get cms dao for biobject
        IBIObjectCMSDAO cmsDao = DAOFactory.getBIObjectCMSDAO();
        // delete subobject
        cmsDao.deleteSubObject(path, subObjName);
        // get from the session the execution role
        String role = (String)session.getAttribute(SpagoBIConstants.ROLE);
        // set data in response
        response.setAttribute(ObjectsTreeConstants.OBJECT_ID , obj.getId().toString());
        response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, 
        		 			  SpagoBIConstants.PUBLISHER_LOOPBACK_AFTER_DEL_SUBOBJECT);
        response.setAttribute(SpagoBIConstants.ROLE , role);
	}
	

	/**
	 * Get the list of subObjects of a BIObject for the current user
	 * @param obj BIObject container of the subObjects 
	 * @param profile profile of the user
	 * @return the List of the BIObject's subobjects visible to the current user
	 */
	private List getSubObjectsList(BIObject obj, IEngUserProfile profile) {
		List subObjects = new ArrayList();
		try {
			IBIObjectCMSDAO biObjCmsDAO = DAOFactory.getBIObjectCMSDAO();
			String objectPath =  obj.getPath();
			subObjects =  biObjCmsDAO.getAccessibleSubObjects(objectPath, profile);
		} catch (Exception e) {
			SpagoBITracer.major("SPAGOBI", 
                    			this.getClass().getName(), 
                    			"getSubObjectsList", 
                    			"Error retriving the subObject list", e);
		}
		return subObjects;
	}
	
	
	
	/**
	 * Get the list of BIObject sbapshots 
	 * @param obj BIObject container of the snapshot 
	 * @return the List of the BIObject snapshots 
	 */
	private List getSnapshotList(BIObject obj) {
		List snapshots = new ArrayList();
		try {
			IBIObjectCMSDAO biObjCmsDAO = DAOFactory.getBIObjectCMSDAO();
			String objectPath =  obj.getPath();
			snapshots =  biObjCmsDAO.getSnapshots(objectPath);
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
                    			"getSnapshotList", "Error retriving the snapshot list", e);
		}
		return snapshots;
	}
	
	
	/**
	 * Based on the object type launches the right execution mechanism. For objects executed 
	 * by an external engine instantiates the driver for execution, gets the execution call parameters map,
	 * adds in reponse the map of the parameters. For objects executed by an internal engine, instantiates 
	 * the engine class and launches execution method.
	 * @param obj The BIobject
	 * @param subObj The SubObjectDetail subObject to be executed (in case it is not null)
	 * @param response The response Source Bean
	 */
	private void execute(BIObject obj, SubObjectDetail subObj, SourceBean response) {
		debug("execute", "start execute");
		EMFErrorHandler errorHandler = getErrorHandler();

		// GET ENGINE ASSOCIATED TO THE BIOBJECT
		Engine engine = obj.getEngine();
		
		// GET THE TYPE OF ENGINE (INTERNAL / EXTERNAL) AND THE SUITABLE BIOBJECT TYPES
		Domain engineType = null;
		Domain compatibleBiobjType = null;
		try {
			engineType = DAOFactory.getDomainDAO().loadDomainById(engine.getEngineTypeId());
			compatibleBiobjType = DAOFactory.getDomainDAO().loadDomainById(engine.getBiobjTypeId());
		} catch (EMFUserError error) {
			 SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"execute", 
		 				"Error retrieving document's engine information", error);
			 errorHandler.addError(error);
			 return;
		}
		String compatibleBiobjTypeCd = compatibleBiobjType.getValueCd();
		String biobjTypeCd = obj.getBiObjectTypeCode();
		
		// CHECK IF THE BIOBJECT IS COMPATIBLE WITH THE TYPES SUITABLE FOR THE ENGINE
		if (!compatibleBiobjTypeCd.equalsIgnoreCase(biobjTypeCd)) {
			// the engine document type and the biobject type are not compatible
			 SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"execute", 
		 				"Engine cannot execute input document type: " +
		 				"the engine " + engine.getName() + " can execute '" + compatibleBiobjTypeCd + "' type documents " +
		 						"while the input document is a '" + biobjTypeCd + "'.");
			Vector params = new Vector();
			params.add(engine.getName());
			params.add(compatibleBiobjTypeCd);
			params.add(biobjTypeCd);
			errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 2002, params));
			return;
		}
		
		// GET USER PROFILE
		IEngUserProfile profile = (IEngUserProfile) permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		
		// IF USER CAN'T EXECUTE THE OBJECT RETURN
		if (!canExecute(profile, obj)) return; 
		
		// GET THE EXECUTION ROLE FROM SESSION
		String executionRole = (String)session.getAttribute(SpagoBIConstants.ROLE);
		
		// IF THE ENGINE IS EXTERNAL
		if ("EXT".equalsIgnoreCase(engineType.getValueCd())) {
			try {
				response.setAttribute("EXECUTION", "true");
				response.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
				// instance the driver class
				String driverClassName = obj.getEngine().getDriverName();
				IEngineDriver aEngineDriver = (IEngineDriver)Class.forName(driverClassName).newInstance();
			    // get the map of the parameters
				Map mapPars = null;
				
				
				if(subObj!=null) 
					mapPars = aEngineDriver.getParameterMap(obj, subObj, profile, executionRole);
				else mapPars = aEngineDriver.getParameterMap(obj, profile, executionRole);
				//String type = obj.getBiObjectTypeCode();
				//if(type.equalsIgnoreCase("OLAP")) {
				//    if (subObj != null) mapPars = aEngineDriver.getParameterMap(obj, subObj, profile);
				//    else mapPars = aEngineDriver.getParameterMap(obj, profile);
				//} else {
				//	if (subObj != null) mapPars = aEngineDriver.getParameterMap(obj, subObj);
				//	else mapPars = aEngineDriver.getParameterMap(obj);
				//}
				
			    // set into the reponse the parameters map	
				response.setAttribute(ObjectsTreeConstants.REPORT_CALL_URL, mapPars);
				
			} catch (Exception e) {
				 SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
						 				this.getClass().getName(), 
						 				"execute", 
						 				"Error During object execution", e);
			   	 errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100)); 
			}	
			
		// IF THE ENGINE IS INTERNAL	
		} else {
			
			String className = engine.getClassName();
			debug("execute", "Try instantiating class " + className + " for internal engine " + engine.getName() + "...");
			InternalEngineIFace internalEngine = null;
			// tries to instantiate the class for the internal engine
			try {
				if (className == null && className.trim().equals("")) throw new ClassNotFoundException();
				internalEngine = (InternalEngineIFace) Class.forName(className).newInstance();
			} catch (ClassNotFoundException cnfe) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
			 				this.getClass().getName(), 
			 				"execute", 
			 				"The class ['" + className + "'] for internal engine " + engine.getName() + " was not found.", cnfe);
				Vector params = new Vector();
				params.add(className);
				params.add(engine.getName());
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 2001, params));
				return;
			} catch (Exception e) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"execute", 
		 				"Error while instantiating class " + className, e);
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100));
				return;
			}
			
			debug("execute", "Class " + className + " instantiated successfully. Now engine's execution starts.");
			
			// starts engine's execution
			try {
				if (subObj != null) 
					internalEngine.executeSubObject(this.getRequestContainer(), obj, response, subObj); 
				else internalEngine.execute(this.getRequestContainer(), obj, response);
			} catch (EMFUserError e) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"execute", 
		 				"Error while engine execution", e);
				errorHandler.addError(e);
			} catch (Exception e) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"execute", 
		 				"Error while engine execution", e);
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100));
			}
			
		}

	}
	
	private boolean isSubRptStatusAdmissible(String masterRptStatus, String subRptStatus) {
		if(masterRptStatus.equalsIgnoreCase("DEV")) {
			if(subRptStatus.equalsIgnoreCase("DEV") ||
			   subRptStatus.equalsIgnoreCase("REL")) return true;
			else return false;
		}
		else if(masterRptStatus.equalsIgnoreCase("TEST")) {
			if(subRptStatus.equalsIgnoreCase("TEST") ||
			   subRptStatus.equalsIgnoreCase("REL")) return true;
			else return false;
		}
		else if(masterRptStatus.equalsIgnoreCase("REL")) {
			if(subRptStatus.equalsIgnoreCase("REL")) return true;
			else return false;
		}
		return false;
	}
	
	private boolean isSubRptExecutableByUser(IEngUserProfile profile, BIObject subrptbiobj) {
		String subrptbiobjStatus = subrptbiobj.getStateCode();
		List functionalities = subrptbiobj.getFunctionalities();
		Iterator functionalitiesIt = functionalities.iterator();
		boolean isExecutableByUser = false;
		while (functionalitiesIt.hasNext()) {
			Integer functionalityId = (Integer) functionalitiesIt.next();
			if (ObjectsAccessVerifier.canDev(subrptbiobjStatus, functionalityId, profile)) {
				isExecutableByUser = true;
				break;
			}
			if (ObjectsAccessVerifier.canTest(subrptbiobjStatus, functionalityId, profile)) {
				isExecutableByUser = true;
				break;
			}
			if (ObjectsAccessVerifier.canExec(subrptbiobjStatus, functionalityId, profile)) {
				isExecutableByUser = true;
				break;
			}
		}
		return isExecutableByUser;
	}
	
	private boolean canExecute(IEngUserProfile profile, BIObject biobj) {
		
		Integer masterReportId = biobj.getId();
		String masterReportStatus = biobj.getStateCode();
		
		
		try {
			ISubreportDAO subrptdao = DAOFactory.getSubreportDAO();
			IBIObjectDAO biobjectdao = DAOFactory.getBIObjectDAO();
			
			List subreportList =  subrptdao.loadSubreportsByMasterRptId(masterReportId);
			for(int i = 0; i < subreportList.size(); i++) {
				Subreport subreport = (Subreport)subreportList.get(i);
				BIObject subrptbiobj = biobjectdao.loadBIObjectForDetail(subreport.getSub_rpt_id());
				if(!isSubRptStatusAdmissible(masterReportStatus, subrptbiobj.getStateCode())) {
					errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 1062));
					return false;							
				}
				if(!isSubRptExecutableByUser(profile, subrptbiobj)) {
					errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 1063)); 
					return false;							
				}
			}			
		} catch (EMFUserError e) {
			SpagoBITracer.warning("ENGINES",
					  this.getClass().getName(),
					  "addBISubreports",
					  "Error while reading subreports",
					  e);
			return false;
		}	
		
		return true;
	}
	
	
	/**
	 * Exec a biobject snapshot.
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 */
	private void execSnapshotHandler(SourceBean request, SourceBean response) throws Exception {
		String snapshotPath = (String)request.getAttribute(SpagoBIConstants.SNAPSHOT_PATH);
		// built the url for the content recovering
		String url = GeneralUtilities.getSpagoBiContentRepositoryServlet() + 
		             "?operation=getJcrNodeContent&jcrPath=" + snapshotPath ;
		// set recover url into response
		response.setAttribute(SpagoBIConstants.URL, url);
		// set information for the publisher
		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ViewSnapshotPubJ");
	}
	
	
	/**
	 * Based on the object type lauch the right subobject execution mechanism. For object executed 
	 * by an external engine instances the driver for execution, get the execution call parameters map,
	 * add in reponse the map of the parameters.
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 */
	private void executionSubObjectHandler(SourceBean request, SourceBean response) throws Exception {
        // get object from session
        BIObject obj = (BIObject)session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
        // get name of the subobject
        String subObjName = (String)request.getAttribute("NAME_SUB_OBJECT");
        // get description of the subobject
        String subObjDesc = (String)request.getAttribute("DESCRIPTION_SUB_OBJECT");
        // get visibility of the subobject
        String subObjVis = (String)request.getAttribute("VISIBILITY_SUB_OBJECT");
        // buld a new subObject object
        boolean publicVis = false;
        if(subObjVis.equals("Public"))
        	publicVis = true;
        
        Date now = new Date();
		String lastModifcationDate = DateFormat.getDateInstance().format(now);
	    String creationDate = DateFormat.getDateInstance().format(now);
        
        SubObjectDetail subObj = obj.new SubObjectDetail(subObjName, "", "", subObjDesc, 
        		lastModifcationDate, creationDate, publicVis);
        // load all the parameter value with an empty value 
        List biparams = obj.getBiObjectParameters(); 
        Iterator iterParams = biparams.iterator();
        while(iterParams.hasNext()) {
        	BIObjectParameter biparam = (BIObjectParameter)iterParams.next();
        	List values = biparam.getParameterValues();
        	if((values==null) || (values.size()==0)) {
        		ArrayList paramvalues = new ArrayList();
        		paramvalues.add("");
        		biparam.setParameterValues(paramvalues);
        	}
        }
        
        execute(obj, subObj, response);        
  
	}
	
	/**
	 * Trace a debug message into the log
	 * @param method Name of the method to store into the log
	 * @param message Message to store into the log
	 */
	private void debug(String method, String message) {
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
							"ExecuteBIObjectModule", 
							method, 
        					message);
	}
	
	
	
	
	
	
	
	
	
	/**
	 * get object from session
	 */
	private BIObject getBIObject() {
		return (BIObject)session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
	}
	
	private boolean isMultivalueParameter(BIObjectParameter biparam) {
		return (biparam.getParameterValues() != null && biparam.getParameterValues().size() > 1);
	}
	
	private void refreshParameter(BIObjectParameter biparam, SourceBean request) {
		String nameUrl = biparam.getParameterUrlName();
    	List paramAttrsList = request.getAttributeAsList(nameUrl);
        ArrayList paramvalues = new ArrayList();
        if(paramAttrsList.size()==0) return;        	
        Iterator iterParAttr = paramAttrsList.iterator();
        while(iterParAttr.hasNext()) {
        	String values = (String)iterParAttr.next();
        	String[] value =  values.split(";");
        	for(int i = 0; i < value.length; i++) {
        		if(!value[i].trim().equalsIgnoreCase("")) paramvalues.add(value[i]);
        	}
        	
        }
        if(paramvalues.size() == 0) biparam.setParameterValues(null);
        else biparam.setParameterValues(paramvalues);
	}
	
	
	private Object getLookedUpObjId(SourceBean request) {
		return ( request.getAttribute("LOOKUP_OBJ_PAR_ID") );
	}
	
	private boolean isLookupCall(SourceBean request) {
		return ( getLookedUpObjId(request) != null );
	}
	
	private Integer getLookedUpParameterId(SourceBean request) {
		return ( new Integer(findBIObjParId(getLookedUpObjId(request))) );
	}
	
	public BIObjectParameter getLookedUpParameter(SourceBean request) {
		BIObjectParameter lookedupBIParameter = null;
		
		Integer objParId = getLookedUpParameterId(request);
    	Iterator iterParams = getBIObject().getBiObjectParameters().iterator();
    	while (iterParams.hasNext()) {
    		BIObjectParameter aBIParameter = (BIObjectParameter) iterParams.next();
    		if (aBIParameter.getId().equals(objParId)) {
    			lookedupBIParameter = aBIParameter;
    			break;
    		}
    	}
    	
    	return lookedupBIParameter;
	}
		
	
	public boolean isSingleValue(BIObjectParameter biparam) {
		boolean isSingleValue = false;
    	try {
			LovResultHandler lovResultHandler = new LovResultHandler(biparam.getLovResult());
			if(lovResultHandler.isSingleValue()) isSingleValue = true;
		} catch (SourceBeanException e) {
			// TODO Auto-generated catch block
		}
		return isSingleValue;
	}
	
	
	
	/**
	 * Handles the final execution of the object  
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 */
	private void executionHandler(SourceBean request, SourceBean response) throws Exception {
		
		BIObject obj = getBIObject();
		
		// refresh parameter values
		List biparams = obj.getBiObjectParameters(); 
        Iterator iterParams = biparams.iterator();
        while(iterParams.hasNext()) {
        	BIObjectParameter biparam = (BIObjectParameter)iterParams.next();
        	HashMap paramsDescriptionMap = (HashMap)session.getAttribute("PARAMS_DESCRIPTION_MAP");
        	
        	String pendingDelete = (String)request.getAttribute("PENDING_DELETE");
        	if(pendingDelete != null && !pendingDelete.trim().equals("")) {
        		if(isSingleValue(biparam)) continue;
        		biparam.setParameterValues(null);
        		if(paramsDescriptionMap.get(biparam.getParameterUrlName()) != null)
        				paramsDescriptionMap.put(biparam.getParameterUrlName(), "");            		
        	} else {
        		refreshParameter(biparam, request);
            	String isChanged = (String)request.getAttribute(biparam.getParameterUrlName() + "IsChanged");
            	if(isChanged != null && isChanged.equalsIgnoreCase("true")) {
            		// refresh also the description
            		List values = biparam.getParameterValues();
            		String desc = "";
            		if(values != null) {    
    	        		for(int i = 0; i < values.size(); i++) {
    	        			desc += (i==0?"":";") + values.get(i).toString();
    	        		}
            		}
            		paramsDescriptionMap.put(biparam.getParameterUrlName(), desc);            		
            	}
        	}
        	
        	session.setAttribute("PARAMS_DESCRIPTION_MAP", paramsDescriptionMap);
        }
        
              
        // it is a lookup call
        Object lookupObjParId = request.getAttribute("LOOKUP_OBJ_PAR_ID");
        if ( isLookupCall(request) ) {
        	
        	BIObjectParameter lookupBIParameter = getLookedUpParameter(request);

        	if (lookupBIParameter == null) {
    			SpagoBITracer.major("SPAGOBI", 
            			this.getClass().getName(), 
            			"executionHandler", 
            			"The BIParameter with id = " + getLookedUpParameterId(request).toString() + " does not exist.");
    			throw new EMFUserError(EMFErrorSeverity.ERROR, 1041);
        	}
        	ModalitiesValue modVal = lookupBIParameter.getParameter().getModalityValue();

        	
        	String lookupType = (String)request.getAttribute("LOOKUP_TYPE");
        	if(lookupType == null) lookupType = "LIST";
        	
        	if(lookupType.equalsIgnoreCase("CHECK_LIST")) {        	
        		response.setAttribute("CHECKLIST", "true");
        		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME , "ChecklistLookupPublisher");
        	}
        	else if(lookupType.equalsIgnoreCase("LIST")) {  
        		response.setAttribute("LIST", "true");
            	response.setAttribute(SpagoBIConstants.PUBLISHER_NAME , "LookupPublisher");
        	}
        	else {
        		response.setAttribute("LIST", "true");
            	response.setAttribute(SpagoBIConstants.PUBLISHER_NAME , "LookupPublisher");
        	}
        	        	
        	response.setAttribute("mod_val_id" , modVal.getId().toString());
        	response.setAttribute("LOOKUP_PARAMETER_NAME", lookupBIParameter.getParameterUrlName());
        	response.setAttribute("LOOKUP_PARAMETER_ID", lookupBIParameter.getId().toString());
        	String correlatedParuseId = (String) request.getAttribute("correlatedParuseIdForObjParWithId_" + lookupObjParId);
        	if (correlatedParuseId != null && !correlatedParuseId.equals("")) response.setAttribute("correlated_paruse_id", correlatedParuseId);
        	return;
        }
        
        IEngUserProfile profile = (IEngUserProfile) permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
        String role = (String) session.getAttribute(SpagoBIConstants.ROLE);
        controlInputParameters(biparams, profile, role);
		// if there are some errors into the errorHandler does not execute the BIObject
		if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ExecuteBIObjectPageParameter");
			// get the list of the subObjects
			List subObjects = getSubObjectsList(obj, profile);
	        // put in response the list of subobject names
			response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
			// get the list of biobject snapshot
			List snapshots = getSnapshotList(obj);
			// put in response the list of snapshot 
			response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);
			return;
		}
        // load the template of the object
        obj.loadTemplate();
        // call the execution method        
        execute(obj, null, response);
	}
	

	private void controlInputParameters (List biparams, IEngUserProfile profile, String role) throws Exception {
		if (biparams == null || biparams.size() == 0) return;
        Iterator iterParams = biparams.iterator();
        while(iterParams.hasNext()) {
        	// get biparameter
        	BIObjectParameter biparam = (BIObjectParameter)iterParams.next();
        	// get lov
        	ModalitiesValue lov = biparam.getParameter().getModalityValue();
        	if(lov.getITypeCd().equals("MAN_IN")) {
        		continue;
        	}
    		// get the lov provider detail 
        	String lovProvider = lov.getLovProvider();
        	ILovDetail lovProvDet = LovDetailFactory.getLovFromXML(lovProvider);
        	// get lov result
        	String lovResult = biparam.getLovResult();
        	// get lov result handler
        	LovResultHandler lovResultHandler = new LovResultHandler(lovResult);
        	List values = biparam.getParameterValues();
        	if(values != null) {
        		for(int i = 0; i < values.size(); i++) {
        			String value = values.get(i).toString();
        			if(!lovResultHandler.containsValue(value, lovProvDet.getValueColumnName())) {
        				biparam.setHasValidValues(false);
        				SpagoBITracer.major("SPAGOBI", 
                    			this.getClass().getName(), 
                    			"controlInputParameters", 
                    			"Parameter '" + biparam.getLabel() + "' cannot assume value '" + value + "'" +
                    					" for user '" + profile.getUserUniqueIdentifier().toString() 
                    					+ "' with role '" + role + "'.");
        				List l = new ArrayList();
        				l.add(biparam.getLabel());
        				l.add(value);
        				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 1077, l);
        				errorHandler.addError(userError);
        			}
        		}
        	}
        }
	}
	
}
