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

import it.eng.qbe.action.RecoverClassLoaderAction;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.SpagoBICmsDataMartModelRetriever;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.ExecutionController;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.BIObject.SubObjectDetail;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
	public static final String MODULE_PAGE = "ExecuteBIObjectPage";
	public static final String EXEC_PHASE_SELECTED_ROLE = "EXEC_PHASE_SELECTED_ROLE";
	public static final String EXEC_PHASE_RETURN_FROM_LOOKUP = "EXEC_PHASE_RETURN_FROM_LOOKUP";
	public static final String EXEC_CHANGE_STATE = "EXEC_CHANGE_STATE";
	public static final String MESSAGE_EXECUTION = "MESSAGEEXEC";	
	EMFErrorHandler errorHandler = null;
	ExecutionController execContr = null;
	RequestContainer requestContainer = null;
	SessionContainer session = null;
	SessionContainer permanentSession = null;
	
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
			if(messageExec.equalsIgnoreCase(ObjectsTreeConstants.EXEC_PHASE_CREATE_PAGE))  {
				pageCreationHandler(request, response);
			} else if(messageExec.equalsIgnoreCase("EXEC_SUBOBJECT")) {
				executionSubObjectHandler(request, response);
			} else if(messageExec.equalsIgnoreCase("DELETE_SUBOBJECT")) {
				deleteSubObjectHandler(request, response);
			} else if(messageExec.equalsIgnoreCase(EXEC_PHASE_SELECTED_ROLE)) {
				selectRoleHandler(request, response);	
			} else if(messageExec.equalsIgnoreCase(EXEC_PHASE_RETURN_FROM_LOOKUP)) {
				lookUpReturnHandler(request, response);
			} else if(messageExec.equalsIgnoreCase(ObjectsTreeConstants.EXEC_PHASE_RUN))  {
				executionHandler(request, response);
			} else if(messageExec.equalsIgnoreCase(EXEC_CHANGE_STATE)){
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
		// get the path of the object
		String path = (String)request.getAttribute(ObjectsTreeConstants.PATH);
		debug("pageCreationHandler", "using path " + path);
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
       		correctRoles = DAOFactory.getBIObjectDAO().getCorrectRolesForExecution(path, profile);
       	else
       		correctRoles = DAOFactory.getBIObjectDAO().getCorrectRolesForExecution(path);
       	debug("pageCreationHandler", "correct roles for execution retrived " + correctRoles);
       	// if correct roles is more than one the user has to select one of them
        // put in the response the right inforamtion for publisher in order to show page role selection
		if( correctRoles.size() > 1 ) {
			response.setAttribute("selectionRoleForExecution", "true");
			response.setAttribute("roles", correctRoles);
			response.setAttribute("path", path);
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
		BIObject obj = execContr.prepareBIObjectInSession(session, path, role);
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
            execute(obj, response);
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
        
        Object lookupParameterId = request.getAttribute("LOOKUP_PARAMETER_ID");
        if (lookupParameterId != null) {
        	Integer parameterId = new Integer(findBIObjParId(lookupParameterId));
        	BIObjectParameter lookupBIParameter = null;
        	iterParams = biparams.iterator();
        	while (iterParams.hasNext()) {
        		BIObjectParameter aBIParameter = (BIObjectParameter) iterParams.next();
        		if (aBIParameter.getParID().equals(parameterId)) {
        			lookupBIParameter = aBIParameter;
        			break;
        		}
        	}
        	if (lookupBIParameter == null) {
    			SpagoBITracer.major("SPAGOBI", 
            			this.getClass().getName(), 
            			"executionHandler", 
            			"The BIParameter with Parameter id = " + parameterId.toString() + " and BIObject id = " + obj.getId().toString() + " does not exist.");
    			throw new EMFUserError(EMFErrorSeverity.ERROR, 1041);
        	}
        	ModalitiesValue modVal = lookupBIParameter.getParameter().getModalityValue();
        	// it is a lookup call
        	response.setAttribute(SpagoBIConstants.PUBLISHER_NAME , "LookupPublisher");
        	response.setAttribute("mod_val_id" , modVal.getId().toString());
        	response.setAttribute("LOOKUP_PARAMETER_NAME", lookupBIParameter.getParameterUrlName());
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
        execute(obj, response);
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
		// get the path of the object
		String path = (String)request.getAttribute(ObjectsTreeConstants.PATH);
		// prepare the object in session
		BIObject obj = execContr.prepareBIObjectInSession(session, path, role);
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
            execute(obj, response);
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
        cmsDao.deleteSubObject(path, subObjName, profile);
        // get from the session the execution role
        String role = (String)session.getAttribute(SpagoBIConstants.ROLE);
        // set data in response
        response.setAttribute(SpagoBIConstants.PATH , path);
        response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, 
        		 			  SpagoBIConstants.PUBLISHER_LOOPBACK_AFTER_DEL_SUBOBJECT);
        response.setAttribute(SpagoBIConstants.ROLE , role);
	}
	
	
	
	/*
	private void execQbe(BIObject obj, SourceBean response, String nameQuery) {
		EMFErrorHandler errorHandler = getErrorHandler();
        try{
        	String jndiDataSourceName = "";
        	String dialect = "";
        	List parameters = obj.getBiObjectParameters();
        	Iterator iterPar = parameters.iterator();
        	BIObjectParameter parameter = null;
        	String urlName = null;
        	while(iterPar.hasNext()) {
        		parameter = (BIObjectParameter)iterPar.next();
        		urlName = parameter.getParameterUrlName();
        		if(urlName.equalsIgnoreCase("JNDI_DS")) {
        			jndiDataSourceName = (String)parameter.getParameterValues().get(0);
        		}
        		if(urlName.equalsIgnoreCase("DIALECT")) {
        			dialect = (String)parameter.getParameterValues().get(0);
        		}   
        	}
        	session.setAttribute(SpagoBICmsDataMartModelRetriever.REFRESH_DATAMART, "TRUE");
        	String dmName = obj.getName();
        	String dmDescription = obj.getDescription();
        	String dmLabel = obj.getLabel();
        	String dmPath = obj.getPath();
        	DataMartModel dmModel = new DataMartModel(dmPath, jndiDataSourceName, dialect);
        	dmModel.setName(dmName);
        	dmModel.setDescription(dmDescription);
        	dmModel.setLabel(dmLabel);
        	session.setAttribute("dataMartModel", dmModel);
        	ISingleDataMartWizardObject aWizardObject = null;
        	if(nameQuery==null) {
        		aWizardObject = new SingleDataMartWizardObjectSourceBeanImpl();
        	} else {
        		aWizardObject = dmModel.getQuery(nameQuery);
        		session.setAttribute("QBE_START_MODIFY_QUERY_TIMESTAMP", String.valueOf(System.currentTimeMillis()));
        		session.setAttribute("QBE_LAST_UPDATE_TIMESTAMP", String.valueOf(System.currentTimeMillis()));
        	}
        	session.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, aWizardObject);
        	response.setAttribute("DATAMART_EXECUTION", "true");
        	ClassLoader toRecoverClassLoader = Thread.currentThread().getContextClassLoader(); 
        	Logger.debug(RecoverClassLoaderAction.class, "Saving Class Loader for recovering " + toRecoverClassLoader.toString());
        	if (ApplicationContainer.getInstance().getAttribute("CURRENT_THREAD_CONTEXT_LOADER") == null){
        		ApplicationContainer.getInstance().setAttribute("CURRENT_THREAD_CONTEXT_LOADER", toRecoverClassLoader);
        	}
        	return;
        } catch (Exception e) {
                SpagoBITracer.major("SPAGOBI", 
                                            "ExecuteBIObjectMOdule", 
                                            "execQbe", 
                                            "Cannot exec the subObject", e);
                   errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100)); 
                   return;
        }
	}
	*/
	
	
	
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
			String objectPath = obj.getPath();
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
	 * Based on the object type lauch the right execution mechanism. For object executed 
	 * by an external engine instances the driver for execution, get the execution call parameters map,
	 * add in reponse the map of the parameters.
	 * @param obj The BIobject
	 * @param response The response Source Bean
	 */
	private void execute(BIObject obj, SourceBean response) {
		debug("execute", "start execute");
		EMFErrorHandler errorHandler = getErrorHandler();
		try{
            String type = obj.getBiObjectTypeCode();
			// if object is a datamart, exec an internal logic  
			if(type.equalsIgnoreCase("DATAMART")) {
				execQbe(obj, response, null);
				return;
			}
			// if object is a dashboard, exec an internal logic
			if(type.equalsIgnoreCase("DASH")) {
				execDash(obj, response);
				return;
			}
            // if object is not a datamart
			response.setAttribute("EXECUTION", "true");
			response.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
			// instance the driver class
			String driverClassName = obj.getEngine().getDriverName();
			IEngineDriver aEngineDriver = (IEngineDriver)Class.forName(driverClassName).newInstance();
		    // get the map of the parameters
			Map mapPars = null;
			if(type.equalsIgnoreCase("OLAP")) {
				// get the user profile
				IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			    mapPars = aEngineDriver.getParameterMap(obj, profile);
			} else {
				mapPars = aEngineDriver.getParameterMap(obj);
			}
            // set into the reponse the parameters map	
			response.setAttribute(ObjectsTreeConstants.REPORT_CALL_URL, mapPars);
		} catch (Exception e) {
			 SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
					 				this.getClass().getName(), 
					 				"execute", 
					 				"Error During object execution", e);
		   	 errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100)); 
		}
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
        // different kind of object need different kind of subexecution
        // if the object is a datamart exec the internal logic
        String type = obj.getBiObjectTypeCode();
        if(type.equalsIgnoreCase("DATAMART"))  {
        	execQbe(obj, response, subObjName);
            return;
        } 
        // if the object is not a datamart instances the driver and get the parameter map
        response.setAttribute("EXECUTION", "true");
		response.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
		String driverClassName = obj.getEngine().getDriverName();
		IEngineDriver aEngineDriver = (IEngineDriver)Class.forName(driverClassName).newInstance();
		Map mapPars = null;
		if(type.equalsIgnoreCase("OLAP")) {
			IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		    mapPars = aEngineDriver.getParameterMap(obj, subObj,  profile);
		} else {
			mapPars = aEngineDriver.getParameterMap(obj, subObj);
		}
        // set into the reponse the parameters map	
		response.setAttribute(ObjectsTreeConstants.REPORT_CALL_URL, mapPars);        
	}
	
	
	
	
	
	/**
	 * Exec the SpagoBI internal logic for execution and presentation of the QBE.
	 * The method exec a new composition of the BIObject datamart or a previously saved query of
	 * the BIObject, nased on the value of the last parameter
	 * 
	 * @param obj Datamart BIObject to execute
	 * @param response The Response SourceBean
	 * @param nameQuery Name of the eventual subObject query to execute
	 */
	private void execQbe(BIObject obj, SourceBean response, String nameQuery) {
		EMFErrorHandler errorHandler = getErrorHandler();
		try {
			String jndiDataSourceName = "";
			String dialect = "";
			List parameters = obj.getBiObjectParameters();
			Iterator iterPar = parameters.iterator();
			BIObjectParameter parameter = null;
			String urlName = null;
			while(iterPar.hasNext()) {
				parameter = (BIObjectParameter)iterPar.next();
				urlName = parameter.getParameterUrlName();
			    if(urlName.equalsIgnoreCase("JNDI_DS")) {
				   	jndiDataSourceName = (String)parameter.getParameterValues().get(0);
			    }
			    if(urlName.equalsIgnoreCase("DIALECT")) {
			    	dialect = (String)parameter.getParameterValues().get(0);
			    }   
			}
			session.setAttribute(SpagoBICmsDataMartModelRetriever.REFRESH_DATAMART, "TRUE");
			String dmName = obj.getName();
			String dmDescription = obj.getDescription();
			String dmLabel = obj.getLabel();
			
			String dmPath = obj.getPath();
			DataMartModel dmModel = new DataMartModel(dmPath, jndiDataSourceName, dialect);
			dmModel.setName(dmName);
			dmModel.setDescription(dmDescription);
			dmModel.setLabel(dmLabel);
			//ApplicationContainer application = ApplicationContainer.getInstance();
			//SessionFactory sf = Utils.getSessionFactory(dmModel, application);
			session.setAttribute("dataMartModel", dmModel);
			ISingleDataMartWizardObject aWizardObject = null;
			if(nameQuery==null) {
				aWizardObject = new SingleDataMartWizardObjectSourceBeanImpl();
			} else {
				aWizardObject = dmModel.getQuery(nameQuery);
			}
			
			String cTM = String.valueOf(System.currentTimeMillis());
			session.setAttribute("QBE_START_MODIFY_QUERY_TIMESTAMP", cTM);
			session.setAttribute("QBE_LAST_UPDATE_TIMESTAMP", cTM);
			
			Logger.debug(ExecuteBIObjectModule.class, (String)session.getAttribute("QBE_LAST_UPDATE_TIMESTAMP"));
			session.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, aWizardObject);
			response.setAttribute("DATAMART_EXECUTION", "true");
			
			ClassLoader toRecoverClassLoader = Thread.currentThread().getContextClassLoader(); 
			Logger.debug(RecoverClassLoaderAction.class, "Saving Class Loader for recovering " + toRecoverClassLoader.toString());
			if (ApplicationContainer.getInstance().getAttribute("CURRENT_THREAD_CONTEXT_LOADER") == null){
				ApplicationContainer.getInstance().setAttribute("CURRENT_THREAD_CONTEXT_LOADER", toRecoverClassLoader);
			}
			return;
		} catch (Exception e) {
			SpagoBITracer.major("SPAGOBI", 
					            "ExecuteBIObjectMOdule", 
					            "execQbe", 
					            "Cannot exec the subObject", e);
	   		errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100)); 
	   		return;
		}
	}

	
	
	
	
	/**
	 * Exec the SpagoBI internal logic for execution and presentation of a dashboard.
	 * @param obj Dashboard BIObject to execute
	 * @param response The Response SourceBean
	 */
	private void execDash(BIObject obj, SourceBean response) {
		EMFErrorHandler errorHandler = getErrorHandler();
		try {
			obj.loadTemplate();
			// get the template of the object
			UploadedFile template = obj.getTemplate();
			if(template==null) { 
				SpagoBITracer.major("ExecuteBIObjectModule",
						            this.getClass().getName(),
						            "execDash",
						            "Template biobject null");
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100));
				return;
			}
			// get bytes of template and transform it into a SourceBean
			SourceBean content = null;
			try {
				byte[] contentBytes = template.getFileContent();
				String contentStr = new String(contentBytes);
				content = SourceBean.fromXMLString(contentStr);
			} catch (Exception e) {
				SpagoBITracer.major("ExecuteBIObjectModule",
			            			this.getClass().getName(),
			            			"execDash",
			            			"Error while converting the Template bytes into a SourceBean object");
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100));
				return;
			}
			// get information from the conf SourceBean and pass them into the response
			String displayTitleBar = (String)content.getAttribute("displayTitleBar");
			String movie = (String)content.getAttribute("movie");
			String width = (String)content.getAttribute("DIMENSION.width");
			String height = (String)content.getAttribute("DIMENSION.height");
			String dataurl = (String)content.getAttribute("DATA.url");
			// get all the parameters for data url
			Map dataParameters = new HashMap();
			SourceBean dataSB = (SourceBean)content.getAttribute("DATA");
			List dataAttrsList = dataSB.getContainedSourceBeanAttributes();
			Iterator dataAttrsIter = dataAttrsList.iterator();
			while(dataAttrsIter.hasNext()) {
				SourceBeanAttribute paramSBA = (SourceBeanAttribute)dataAttrsIter.next();
				SourceBean param = (SourceBean)paramSBA.getValue();
				String nameParam = (String)param.getAttribute("name");
				String valueParam = (String)param.getAttribute("value");
				dataParameters.put(nameParam, valueParam);
			}
			// get all the parameters for dash configuration
			Map confParameters = new HashMap();
			SourceBean confSB = (SourceBean)content.getAttribute("CONF");
			List confAttrsList = confSB.getContainedSourceBeanAttributes();
			Iterator confAttrsIter = confAttrsList.iterator();
			while(confAttrsIter.hasNext()) {
				SourceBeanAttribute paramSBA = (SourceBeanAttribute)confAttrsIter.next();
				SourceBean param = (SourceBean)paramSBA.getValue();
				String nameParam = (String)param.getAttribute("name");
				String valueParam = (String)param.getAttribute("value");
				confParameters.put(nameParam, valueParam);
			}
			// create the title
			String title = "";
			title += obj.getName();
			String objDescr = obj.getDescription();
			if( (objDescr!=null) && !objDescr.trim().equals("") ) {
				title += ": " + objDescr;
			}
			 // get the actor
			SessionContainer session = getRequestContainer().getSessionContainer();
			String actor = (String)session.getAttribute(SpagoBIConstants.ACTOR);
			// get the possible state changes
			IDomainDAO domaindao = DAOFactory.getDomainDAO();
			List states = domaindao.loadListDomainsByType("STATE");
		    List possibleStates = new ArrayList();
		    if (actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)){
		    	Iterator it = states.iterator();
		    	 while(it.hasNext()) {
		      		    	Domain state = (Domain)it.next();
		      		    	if (state.getValueCd().equalsIgnoreCase("TEST")){ 
		      					possibleStates.add(state);
		      				}
		      	}  
		    } else if (actor.equalsIgnoreCase(it.eng.spagobi.constants.SpagoBIConstants.TESTER_ACTOR)){
		    	Iterator it = states.iterator();
		    	 while(it.hasNext()) {
		      		    	Domain state = (Domain)it.next();
		      		    	if ((state.getValueCd().equalsIgnoreCase("DEV")) || ((state.getValueCd().equalsIgnoreCase("REL")))) { 
		      					possibleStates.add(state);
		      				}
		      	}  
		    } 
 			// set information into reponse
			response.setAttribute("movie", movie);
			response.setAttribute("dataurl", dataurl);
			response.setAttribute("width", width);
			response.setAttribute("height", height);
			response.setAttribute("displayTitleBar", displayTitleBar);
			response.setAttribute("title", title);
			response.setAttribute("confParameters", confParameters);
			response.setAttribute("dataParameters", dataParameters);
			response.setAttribute("possibleStateChanges", possibleStates);
			// set information for the publisher
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "DASHBOARD");
		} catch (Exception e) {
			SpagoBITracer.major("SPAGOBI", 
					            "ExecuteBIObjectMOdule", 
					            "execDash", 
					            "Cannot exec the dashboard", e);
	   		errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100)); 
	   		return;
		}
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
