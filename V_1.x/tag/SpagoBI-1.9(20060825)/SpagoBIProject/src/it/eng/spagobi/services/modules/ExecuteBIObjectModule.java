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
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.Subreport;
import it.eng.spagobi.bo.BIObject.SubObjectDetail;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.ISubreportDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.events.EventsManager;
import it.eng.spagobi.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
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
	
	public static final String MODULE_PAGE = "ExecuteBIObjectPage";	
	public static final String MESSAGE_EXECUTION = "MESSAGEEXEC";	
	
	
	public void init(SourceBean config) {}
	
	
	/**
	 * Manage all the request in order to exec all the different BIObject execution phases 
	 * @param request	The request source bean
	 * @param response 	The response Source bean
	 * @throws Exception If an Exception occurred
	 */
	public void service(SourceBean request, SourceBean response) throws Exception 
	{
		debug("service", "start service method");
		String messageExec = (String)request.getAttribute(SpagoBIConstants.MESSAGEDET);
		debug("service", "using message" + messageExec);
		errorHandler = getErrorHandler();
		requestContainer = this.getRequestContainer();
		session = requestContainer.getSessionContainer();
		permanentSession = session.getPermanentContainer();
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
			} else {	
		   	    SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
		   	    		            "ExecuteBIObjectMOdule", 
		   	    		            "service", 
		   	    		            "Illegal request of service");
		   		errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 102)); 
		   	}
	      } 
	    catch (EMFUserError e) { 
	    	errorHandler.addError(e); 
	    }
    }


	/**
	 * Manage the parameter page creation preaparing and setting into the response all the 
	 * necessary attributes 
	 * @param request The Spago Request SourceBean
	 * @param response The Spago Response SourceBean
	 */
	private void pageCreationHandler(SourceBean request, SourceBean response) throws Exception {
				
		debug("pageCreationHandler", "start pageCreationHandler method");
		
		// get the id of the object
		String idStr = (String) request.getAttribute(ObjectsTreeConstants.OBJECT_ID);
		Integer id = new Integer(idStr);
		debug("pageCreationHandler", "BIObject id = " + id);
		
		// get parameters statically defined in portlet config file
		String userProvidedParametersStr = (String)session.getAttribute(ObjectsTreeConstants.PARAMETERS);
		debug("pageCreationHandler", "using parameters " + userProvidedParametersStr);
		
		// get the current user profile
		IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		debug("pageCreationHandler", "user profile retrived " + profile);
		
		// get the type of actor 
		String actor = "";
		Object actorObj =  request.getAttribute(SpagoBIConstants.ACTOR);
		if (actorObj != null) {
			actor = (String) actorObj;
			session.setAttribute(SpagoBIConstants.ACTOR, actor);
		} else {
			actor = (String) session.getAttribute(SpagoBIConstants.ACTOR);
		}
		debug("pageCreationHandler", "using actor " + actor);
		
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
		session.delAttribute(ObjectsTreeConstants.PARAMETERS);
		debug("pageCreationHandler", "object retrived and setted into session");
		
		
		
		
		// get the list of the subObjects
		List subObjects = getSubObjectsList(obj, profile);
		debug("pageCreationHandler", "List subobject loaded: " + subObjects);
        
		// put in response the list of subobject names
		response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
				
		// load the object into the Execution controller				
		ExecutionController controller = new ExecutionController();
		controller.setBiObject(obj);
		
		// if the object can be directly executed (because it hasn't any parameter to be
		// filled by the user) and if the object has no subobject saved then execute it
		// directly without pass for parameters page 	
		if( controller.directExecution() &&  (subObjects.size() == 0) ) {
			debug("pageCreationHandler", "object hasn't any parameter to fill and no subObjects");
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
	
	
	
	/**
	 * Handles the final execution of the object  
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 */
	private void executionHandler(SourceBean request, SourceBean response) throws Exception {
		// get object from the session
		BIObject obj = (BIObject) session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
        // for each parameter of the object control if in the request are
		// present one or more values and put them into the parameter
		List biparams = obj.getBiObjectParameters(); 
        Iterator iterParams = biparams.iterator();
        while(iterParams.hasNext()) {
        	BIObjectParameter biparam = (BIObjectParameter)iterParams.next();
        	String nameUrl = biparam.getParameterUrlName();
        	List paramAttrsList = request.getAttributeAsList(nameUrl);
            ArrayList paramvalues = new ArrayList();
            if(paramAttrsList.size()==0)
            	continue;
            Iterator iterParAttr = paramAttrsList.iterator();
            while(iterParAttr.hasNext()) {
            	String value = (String)iterParAttr.next();
            	paramvalues.add(value);
            }
            biparam.setParameterValues(paramvalues);
        }
        
        Object lookupObjParId = request.getAttribute("LOOKUP_OBJ_PAR_ID");
        if (lookupObjParId != null) {
        	Integer objParId = new Integer(findBIObjParId(lookupObjParId));
        	BIObjectParameter lookupBIParameter = null;
        	iterParams = biparams.iterator();
        	while (iterParams.hasNext()) {
        		BIObjectParameter aBIParameter = (BIObjectParameter) iterParams.next();
        		if (aBIParameter.getId().equals(objParId)) {
        			lookupBIParameter = aBIParameter;
        			break;
        		}
        	}
        	if (lookupBIParameter == null) {
    			SpagoBITracer.major("SPAGOBI", 
            			this.getClass().getName(), 
            			"executionHandler", 
            			"The BIParameter with id = " + objParId.toString() + " does not exist.");
    			throw new EMFUserError(EMFErrorSeverity.ERROR, 1041);
        	}
        	ModalitiesValue modVal = lookupBIParameter.getParameter().getModalityValue();
        	// it is a lookup call
        	response.setAttribute(SpagoBIConstants.PUBLISHER_NAME , "LookupPublisher");
        	response.setAttribute("mod_val_id" , modVal.getId().toString());
        	response.setAttribute("LOOKUP_PARAMETER_NAME", lookupBIParameter.getParameterUrlName());
        	response.setAttribute("LOOKUP_PARAMETER_ID", lookupBIParameter.getId().toString());
        	String correlatedParuseId = (String) request.getAttribute("correlatedParuseIdForObjParWithId_" + lookupObjParId);
        	if (correlatedParuseId != null && !correlatedParuseId.equals("")) response.setAttribute("correlated_paruse_id", correlatedParuseId);
        	return;
        }
        
        errorHandler = getErrorHandler();
		// if there are some errors into the errorHandler does not execute the BIObject
		if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			String role = (String) session.getAttribute(SpagoBIConstants.ROLE);
			response.setAttribute(SpagoBIConstants.ROLE, role);
			IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			// get the list of the subObjects
			List subObjects = getSubObjectsList(obj, profile);
	        // put in response the list of subobject names
			response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ExecuteBIObjectPageParameter");
		}
        // load the template of the object
        obj.loadTemplate();
        // call the execution method        
        execute(obj, null, response);
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
	 * Called after the parameter value lookup selection to continue the execution phase  
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 */
	private void lookUpReturnHandler(SourceBean request, SourceBean response) throws Exception {
		// get the object from the session
		BIObject obj = (BIObject)session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
		// get the parameter name and value from the request
		String parameterNameFromLookUp = (String)request.getAttribute("LOOKUP_PARAMETER_NAME");
		String parameterValueFromLookUp = (String)request.getAttribute("LOOKUP_VALUE");
        // Create a List that will contains the value returned 
		ArrayList paramvalues = new ArrayList();
		paramvalues.add(parameterValueFromLookUp);
		// Set into the righr object parameter the list value
        List biparams = obj.getBiObjectParameters(); 
        Iterator iterParams = biparams.iterator();
        while(iterParams.hasNext()) {
        	BIObjectParameter biparam = (BIObjectParameter)iterParams.next();
        	String nameUrl = biparam.getParameterUrlName();
        	
        	if (nameUrl.equalsIgnoreCase(parameterNameFromLookUp)){
        		biparam.setParameterValues(paramvalues);
        	}//if (nameUrl.equalsIgnoreCase(parameterNameFromLookUp)){
        }// while(iterParams.hasNext()) {
        // put in session the new object
        session.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
        // get the current user profile
		IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
        // get the list of the subObjects
		List subObjects = getSubObjectsList(obj, profile);
        // put in response the list of subobject names
		response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
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
		// set into the execution controlle the object
		ExecutionController controller = new ExecutionController();
		controller.setBiObject(obj);
		// if the object can be directly executed (because it hasn't any parameter to be
		// filled by the user) and if the object has no subobject saved then execute it
		// directly without pass for parameters page 	
		if( controller.directExecution() &&  (subObjects.size() == 0) ) {
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
		Engine engine = obj.getEngine();
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
		
		IEngUserProfile profile = (IEngUserProfile) permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		if (!canExecute(profile, obj)) return; 

		
		if ("EXT".equalsIgnoreCase(engineType.getValueCd())) {
			
			try {
				
				response.setAttribute("EXECUTION", "true");
				response.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
				// instance the driver class
				String driverClassName = obj.getEngine().getDriverName();
				IEngineDriver aEngineDriver = (IEngineDriver)Class.forName(driverClassName).newInstance();
			    // get the map of the parameters
				Map mapPars = null;
				String type = obj.getBiObjectTypeCode();
				if(type.equalsIgnoreCase("OLAP")) {
				    if (subObj != null) mapPars = aEngineDriver.getParameterMap(obj, subObj, profile);
				    else mapPars = aEngineDriver.getParameterMap(obj, profile);
				} else {
					if (subObj != null) mapPars = aEngineDriver.getParameterMap(obj, subObj);
					else mapPars = aEngineDriver.getParameterMap(obj);
				}
				
//				if(type.equalsIgnoreCase("OLAP")) {
//					// get the user profile
//					IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
//				    if (subObj != null) mapPars = aEngineDriver.getParameterMap(obj, subObj, profile);
//				    else mapPars = aEngineDriver.getParameterMap(obj, profile);
//				} else if(type.equalsIgnoreCase("REPORT") && aEngineDriver.getClass().getName().equalsIgnoreCase("it.eng.spagobi.drivers.jasperreport.JasperReportDriver")) {
//					String actor = (String)session.getAttribute(SpagoBIConstants.ACTOR);
//					String objState = obj.getStateCode();
//					if(!canExecute(obj)) 
//						 errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 1062)); 
//					if (subObj != null) mapPars = aEngineDriver.getParameterMap(obj, subObj);
//					else mapPars = aEngineDriver.getParameterMap(obj);
//				} else {
//					if (subObj != null) mapPars = aEngineDriver.getParameterMap(obj, subObj);
//					else mapPars = aEngineDriver.getParameterMap(obj);
//				}
				
				// callback event id
			    String user = (String)profile.getUserUniqueIdentifier();
				Integer id =  EventsManager.getInstance().registerEvent(user);
			    mapPars.put("event", id.toString());
			    mapPars.put("user", user);
			    
				// set into the reponse the parameters map	
				response.setAttribute(ObjectsTreeConstants.REPORT_CALL_URL, mapPars);
			
			} catch (Exception e) {
				 SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
						 				this.getClass().getName(), 
						 				"execute", 
						 				"Error During object execution", e);
			   	 errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100)); 
			}	
			
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
        SubObjectDetail subObj = obj.new SubObjectDetail(subObjName, "", "", subObjDesc, publicVis);
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
	
}
