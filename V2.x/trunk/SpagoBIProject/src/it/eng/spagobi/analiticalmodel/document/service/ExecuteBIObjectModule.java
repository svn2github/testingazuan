/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.document.service;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.analiticalmodel.document.bo.SubObject;
import it.eng.spagobi.analiticalmodel.document.bo.Viewpoint;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISubObjectDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISubreportDAO;
import it.eng.spagobi.analiticalmodel.document.dao.IViewpointDAO;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionController;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionManager;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionManager.ExecutionInstance;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovDetailFactory;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovResultHandler;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.bo.Subreport;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.engines.documentcomposition.configuration.DocumentCompositionConfiguration;
import it.eng.spagobi.engines.drivers.IEngineDriver;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * Executes a report, according to four phases; each phase is identified by a
 * message string.
 * <p>
 * 1) Creates the page
 * <p>
 * 2) Selects the role
 * <p>
 * 3) From the field input values loads the object and starts execution
 * <p>
 * 4) See Report/Change the report state
 * 
 * @author Zerbetto
 * @author Fiscato
 * @author Bernabei
 * @author Mark Penningroth (Cincom Systems, Inc.)
 */
public class ExecuteBIObjectModule extends AbstractModule {

	static private Logger logger = Logger
			.getLogger(ExecuteBIObjectModule.class);

	EMFErrorHandler errorHandler = null;
	ExecutionController execContr = null;
	RequestContainer requestContainer = null;
	SessionContainer session = null;
	SessionContainer permanentSession = null;

	public static final String MODULE_PAGE = "ExecuteBIObjectPage";
	public static final String MESSAGE_EXECUTION = "MESSAGEEXEC";
	public static final String SUBMESSAGEDET = "SUBMESSAGEDET";

	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.module.AbstractModule#init(it.eng.spago.base.SourceBean)
	 */
	public void init(SourceBean config) {
	}

	/**
	 * Manage all the request in order to exec all the different BIObject
	 * execution phases.
	 * 
	 * @param request The request source bean
	 * @param response The response Source bean
	 * 
	 * @throws Exception If an Exception occurred
	 */
	public void service(SourceBean request, SourceBean response)
			throws Exception {
		logger.debug("IN");
		String messageExec = (String) request
				.getAttribute(SpagoBIConstants.MESSAGEDET);
		logger.debug("using message" + messageExec);
		String subMessageExec = (String) request.getAttribute(SUBMESSAGEDET);
		logger.debug("using sub-message" + subMessageExec);
		// if submessage is valorized it gives the value to message
		if (subMessageExec != null && !subMessageExec.equals(""))
			messageExec = subMessageExec;
		errorHandler = getErrorHandler();
		requestContainer = this.getRequestContainer();
		session = requestContainer.getSessionContainer();
		permanentSession = session.getPermanentContainer();
		logger
				.debug("errorHanlder, requestContainer, session, permanentSession retrived ");
		execContr = new ExecutionController();

		try {
			if (messageExec == null) {
				EMFUserError userError = new EMFUserError(
						EMFErrorSeverity.ERROR, 101);
				logger.error("The execution-message parameter is null");
				throw userError;
			}

			if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.EXEC_PHASE_CREATE_PAGE)) {
				pageCreationHandler(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.EXEC_PHASE_RUN_SUBOJECT)) {
				executionSubObjectHandler(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.EXEC_PHASE_DELETE_SUBOJECT)) {
				deleteSubObjectHandler(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.EXEC_PHASE_SELECTED_ROLE)) {
				selectRoleHandler(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.EXEC_PHASE_RETURN_FROM_LOOKUP)) {
				lookUpReturnHandler(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.EXEC_PHASE_RUN)) {
				executionHandler(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.EXEC_CHANGE_STATE)) {
				changeStateHandler(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.EXEC_SNAPSHOT_MESSAGE)) {
				execSnapshotHandler(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.ERASE_SNAPSHOT_MESSAGE)) {
				eraseSnapshotHandler(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.VIEWPOINT_SAVE)) {
				saveViewPoint(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.VIEWPOINT_ERASE)) {
				eraseViewpoint(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.VIEWPOINT_EXEC)) {
				execViewpoint(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.VIEWPOINT_VIEW)) {
				viewViewpoint(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.EXEC_CROSS_NAVIGATION)) {
				executeCrossNavigationHandler(request, response);
			} else if (messageExec
					.equalsIgnoreCase(SpagoBIConstants.RECOVER_EXECUTION_FROM_CROSS_NAVIGATION)) {
				recoverExecutionFromCrossNavigationHandler(request, response);
			} else {
				logger.error("Illegal request of service");
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
						102));
			}
		} catch (EMFUserError e) {
			errorHandler.addError(e);
		} finally {
			logger.debug("OUT");
		}
	}

	private void recoverExecutionFromCrossNavigationHandler(SourceBean request, SourceBean response)
		throws Exception {
		logger.debug("IN");
		try {
			// recovers required execution details
			String executionFlowId = (String) request.getAttribute("EXECUTION_FLOW_ID");
			String executionId = (String) request.getAttribute("EXECUTION_ID");
			session.setAttribute("EXECUTION_FLOW_ID", executionFlowId);
			ExecutionManager executionManager = (ExecutionManager) session.getAttribute(ObjectsTreeConstants.EXECUTION_MANAGER);
			if (executionManager == null) {
				throw new Exception("Execution Manager not found. Cannot recover execution details.");
			}
			ExecutionInstance instance = executionManager.recoverExecution(executionFlowId, executionId);
			BIObject obj = instance.getBIObject();
			// set biobject in session
			setBIObject(obj);
	    	String executionRole = instance.getExecutionRole();
	    	// sets role for new execution on request
	    	request.setAttribute("spagobi_execution_role", executionRole);
	    	// sets the flag in order to skip snapshots/viewpoints/parameters/subobjects page
	    	request.setAttribute(SpagoBIConstants.IGNORE_SUB_NODES, "true");
	    	// deletes parameters of previous execution
	    	session.delAttribute(ObjectsTreeConstants.PARAMETERS);
	    	// creates parameters string for new execution
	    	String documentParameters = "";
			List parameters = obj.getBiObjectParameters();
			Iterator parametersIt = parameters.iterator();
			while (parametersIt.hasNext()) {
				BIObjectParameter parameter = (BIObjectParameter) parametersIt.next();
				String parurlname = parameter.getParameterUrlName();
				List parValues = parameter.getParameterValues();
				if (parValues == null || parValues.size() == 0) continue;
				if (parValues.size() == 1) {
					documentParameters += "&" + parurlname + "=" + parValues.get(0).toString();
				} else {
					documentParameters += "&" + parurlname + "=";
					for (int i = 0; i < parValues.size(); i++) {
						String aParValue = parValues.get(i).toString();
						documentParameters += aParValue;
						if (i < parValues.size() - 1) documentParameters += ";";
					}
				}
			}
	    	if (documentParameters.startsWith("&")) {
	    		documentParameters = documentParameters.substring(1);
	    	}
	    	session.setAttribute(ObjectsTreeConstants.PARAMETERS, documentParameters);
	    	// starts new execution
	    	pageCreationHandler(request, response);
		} finally {
			logger.debug("OUT");
		}
	}
	
	private void executeCrossNavigationHandler(SourceBean request, SourceBean response)
		throws Exception {
		logger.debug("IN");
		try {
			// registers the current execution in the ExecutionManager
			String executionFlowId = (String) request.getAttribute("EXECUTION_FLOW_ID");
			String sourceExecutionId = (String) request.getAttribute("SOURCE_EXECUTION_ID");
			session.setAttribute("EXECUTION_FLOW_ID", executionFlowId);
			ExecutionManager executionManager = (ExecutionManager) session.getAttribute(ObjectsTreeConstants.EXECUTION_MANAGER);
			if (executionManager == null) {
				executionManager = new ExecutionManager();
				session.setAttribute(ObjectsTreeConstants.EXECUTION_MANAGER, executionManager);
			}
			BIObject obj = getBIObject();
			String executionRole = (String) session.getAttribute(SpagoBIConstants.ROLE);
			executionManager.registerExecution(executionFlowId, sourceExecutionId, obj, executionRole);
			// starts new execution 
			request.setAttribute(SpagoBIConstants.IGNORE_SUB_NODES, "true");
			pageCreationHandler(request, response);
		} finally {
			logger.debug("OUT");
		}
	}
	
	private void eraseSnapshotHandler(SourceBean request, SourceBean response)
			throws EMFUserError, SourceBeanException {
		logger.debug("IN");
		ISnapshotDAO snapdao = DAOFactory.getSnapshotDAO();
		String snapshotIdStr = (String) request
				.getAttribute(SpagoBIConstants.SNAPSHOT_ID);
		Integer snapId = new Integer(snapshotIdStr);
		snapdao.deleteSnapshot(snapId);
		// get object from session
		BIObject obj = (BIObject) session
				.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
		// get from the session the execution role
		String role = (String) session.getAttribute(SpagoBIConstants.ROLE);
		// set data in response
		response.setAttribute(ObjectsTreeConstants.OBJECT_ID, obj.getId()
				.toString());
		response.setAttribute(SpagoBIConstants.ROLE, role);
		logger.debug("OUT");
	}

	/**
	 * Manage the parameter page creation preaparing and setting into the
	 * response all the necessary attributes
	 * 
	 * @param request
	 *            The Spago Request SourceBean
	 * @param response
	 *            The Spago Response SourceBean
	 */
	private void pageCreationHandler(SourceBean request, SourceBean response)
			throws Exception {

		logger.debug("IN");

		// get the current user profile
		IEngUserProfile profile = (IEngUserProfile) permanentSession
				.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		logger.debug("user profile retrived " + profile);

		// get the id of the object
		String idStr = (String) request
				.getAttribute(ObjectsTreeConstants.OBJECT_ID);
		String label = (String) request
				.getAttribute(ObjectsTreeConstants.OBJECT_LABEL);
		logger.debug("Request parameters: " + "biobject id = '" + idStr
				+ "'; object label = '" + label + "'.");
		BIObject obj = getBIObject();
		if (idStr == null && label == null) {
			if (obj == null) {
				logger
						.error("The object id and label are not set and no objects are in session");
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
						"100"));
				return;
			} else {
				logger.debug("Object retrieved from session");
			}
		}

		Integer id = null;
		if (label != null) {
			logger.debug("Loading biobject with label = '" + label + "' ...");
			obj = DAOFactory.getBIObjectDAO().loadBIObjectByLabel(label);
			if (obj == null) {
				logger.error("Object with label = '" + label + "' not found!!");
				Vector v = new Vector();
				v.add(label);
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
						"1074", v));
				return;
			}
			id = obj.getId();

		} else if (idStr != null) {
			logger.debug("Loading biobject with id = '" + idStr + "' ...");
			id = new Integer(idStr);
			obj = DAOFactory.getBIObjectDAO().loadBIObjectById(id);
		} else if (obj != null) {
			id = obj.getId();
		}

		logger.debug("BIObject id = " + id);

		boolean canSee = ObjectsAccessVerifier.canSee(obj, profile);
		if (!canSee) {
			logger.error("Object with label = '" + obj.getLabel()
					+ "' cannot be executed by the user!!");
			Vector v = new Vector();
			v.add(obj.getLabel());
			errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
					"1075", v, null));
			return;
		}

		// get parameters statically defined in portlet preferences
		String userProvidedParametersStr = (String) request.getAttribute(ObjectsTreeConstants.PARAMETERS);
		if (userProvidedParametersStr == null)
			userProvidedParametersStr = (String) session.getAttribute(ObjectsTreeConstants.PARAMETERS);
		
		//String userProvidedParametersStr = (String) session.getAttribute(ObjectsTreeConstants.PARAMETERS);
		logger.debug("using parameters " + userProvidedParametersStr);

		// define the variable for execution role
		String role = (String) request.getAttribute("spagobi_execution_role");
		List correctRoles = null;

		if (profile
				.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV)
				|| profile
						.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_USER)
				|| profile
						.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN))
			correctRoles = DAOFactory.getBIObjectDAO()
					.getCorrectRolesForExecution(id, profile);
		else
			correctRoles = DAOFactory.getBIObjectDAO()
					.getCorrectRolesForExecution(id);
		logger.debug("correct roles for execution retrived " + correctRoles);

		if (role != null) {
			// if the role is specified
			if (!correctRoles.contains(role)) {

				logger.warn("Role [" + role
						+ "] is not a correct role for execution");
				Vector v = new Vector();
				v.add(role);
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
						1078, v, null));
				return;
			}
		} else {
			// if correct roles is more than one the user has to select one of
			// them
			// put in the response the right inforamtion for publisher in order
			// to show page role selection
			if (correctRoles.size() > 1) {
				response.setAttribute("selectionRoleForExecution", "true");
				response.setAttribute("roles", correctRoles);
				response.setAttribute(ObjectsTreeConstants.OBJECT_ID, id);
				logger
						.debug("more than one correct roles for execution, redirect to the"
								+ " role selection page");
				return;

				// if there isn't correct role put in the error stack a new
				// error
			} else if (correctRoles.size() < 1) {

				logger.warn("Object cannot be executed by no role of the user");
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
						1006));
				return;

				// the list contains only one role which is the right role
			} else {
				role = (String) correctRoles.get(0);
			}
		}
		logger.debug("using role " + role);

		// NOW THE EXECUTION ROLE IS SELECTED
		// put in session the execution role
		session.setAttribute(SpagoBIConstants.ROLE, role);

		// based on the role selected (or the only for the user) load the object
		// and put it in session
		obj = execContr.prepareBIObjectInSession(session, id, role,
				userProvidedParametersStr);
		Map paramsDescriptionMap = new HashMap();
		List biparams = obj.getBiObjectParameters();
		Iterator iterParams = biparams.iterator();
		while (iterParams.hasNext()) {
			BIObjectParameter biparam = (BIObjectParameter) iterParams.next();
			String nameUrl = biparam.getParameterUrlName();
			paramsDescriptionMap.put(nameUrl, "");
		}

		session.setAttribute("PARAMS_DESCRIPTION_MAP", paramsDescriptionMap);

		// session.delAttribute(ObjectsTreeConstants.PARAMETERS);
		logger.debug("object retrived and setted into session");

		// get the list of the subObjects
		List subObjects = getSubObjectsList(obj, profile);
		logger.debug("List subobject loaded: " + subObjects);
		// put in response the list of subobject
		response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);

		// get the list of biobject snapshot
		List snapshots = getSnapshotList(obj);
		logger.debug("List snapshot loaded: " + snapshots);
		// put in response the list of snapshot
		response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);

		// get the list of viewpoints
		List viewpoints = getViewpointList(obj);
		logger.debug("List viewpoint loaded: " + viewpoints);
		// put in response the list of viewpoint
		response.setAttribute(SpagoBIConstants.VIEWPOINT_LIST, viewpoints);

		// load the object into the Execution controller
		ExecutionController controller = new ExecutionController();
		// check parameters values 
		controlInputParameters(obj.getBiObjectParameters(), profile, role);
		controller.setBiObject(obj);

		// finds if it is requested to ignore sub-nodes
		// (subobjects/snapshots/viewpoints)
		String ignoreSubNodesStr = (String) session
				.getAttribute(SpagoBIConstants.IGNORE_SUB_NODES);
		if (ignoreSubNodesStr == null) {
			ignoreSubNodesStr = (String) request.getAttribute(SpagoBIConstants.IGNORE_SUB_NODES);
		}
		boolean ignoreSubNodes = false;
		if (ignoreSubNodesStr != null
				&& ignoreSubNodesStr.trim().equalsIgnoreCase("true")) {
			ignoreSubNodes = true;
		}

		// subObj is not null if specified on preferences
		SubObject subObj = getRequiredSubObject(obj, subObjects, profile);

		// (if the object can be directly executed (because it hasn't any
		// parameter to be
		// filled by the user) and if the object has no subobject / snapshots /
		// viewpoints saved
		// or the request esplicitely asks to ignore subnodes) or (a valid
		// subobject
		// is specified by preferences)
		// then execute it directly without pass through parameters page
		if ((controller.directExecution() && ((subObjects.size() == 0
				&& snapshots.size() == 0 && viewpoints.size() == 0) || ignoreSubNodes))
				|| subObj != null) {
			logger
					.debug("object hasn't any parameter to fill and no subObjects");
			if (subObj == null)
				controlInputParameters(obj.getBiObjectParameters(), profile,
						role);
			// if there are some errors into the errorHandler does not execute
			// the BIObject
			if (!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
						"ExecuteBIObjectPageParameter");
				return;
			}
			execute(obj, subObj, null, response);
		}
		if (controller.directExecution()) {
			logger
					.debug("object has only subobjects but not parameter to fill");
			response.setAttribute("NO_PARAMETERS", "TRUE");
		}
		logger.debug("OUT");
	}

	/**
	 * Find the subobject with the name specified by the attribute
	 * "LABEL_SUB_OBJECT" on SessionContainer. If such a subobject exists and
	 * the user can execute it, then it is returned; if it doesn't exist, null
	 * is returned; if it exists but the user is not able to execute it, an
	 * error is added into the error handler and null is returned.
	 * 
	 * 
	 * @param obj
	 *            The BIObject being executed
	 * @param subObjects
	 *            The list of all the document suobjects
	 * @param profile
	 *            The user profile
	 * @return the subobject to be executed if it exists and the user can
	 *         execute it
	 */
	// MPenningroth 25-JAN-2008
	// Handle new LABEL_SUB_OBJECT Preference
	private SubObject getRequiredSubObject(BIObject obj, List subObjects,
			IEngUserProfile profile) {
		logger.debug("IN");
		SubObject subObj = null;
		if (subObjects.size() > 0) {
			String subObjectName = (String) session
					.getAttribute("LABEL_SUB_OBJECT");
			if (subObjectName != null) {
				Iterator iterSubs = subObjects.iterator();
				while (iterSubs.hasNext() && subObj == null) {
					SubObject sd = (SubObject) iterSubs.next();
					if (sd.getName().equals(subObjectName.trim())) {
						subObj = sd;
					}
				}
				// TODO - Error case if not found?
			}
		}
		if (subObj != null) {
			if (!subObj.getIsPublic().booleanValue()
					&& !subObj.getOwner().equals(
							profile.getUserUniqueIdentifier())) {
				List l = new ArrayList();
				l.add(subObj.getName());
				l.add(obj.getName());
				EMFUserError userError = new EMFUserError(
						EMFErrorSeverity.ERROR, 1079, l);
				errorHandler.addError(userError);
				return null;
			}
		}
		logger.debug("OUT");
		return subObj;
	}

	/**
	 * Called after the user change state selection to pass the BIObject from a
	 * state to another
	 * 
	 * @param request
	 *            The request SourceBean
	 * @param response
	 *            The response SourceBean
	 */
	private void changeStateHandler(SourceBean request, SourceBean response)
			throws Exception {
		logger.debug("IN");
		// get the type of actor from the session
		// String actor = (String)session.getAttribute(SpagoBIConstants.ACTOR);
		// get new state from the request
		String newState = (String) request.getAttribute("newState");
		// get object from the session
		BIObject obj = (BIObject) session
				.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
		// from the string of the new state extract the id and code of the new
		// state
		StringTokenizer tokenState = new StringTokenizer(newState, ",");
		String stateIdStr = tokenState.nextToken();
		Integer stateId = new Integer(stateIdStr);
		String stateCode = tokenState.nextToken();
		// set into the object the new state id and code
		obj.setStateCode(stateCode);
		obj.setStateID(stateId);
		// call the dao in order to modify the object without versioning the
		// content
		DAOFactory.getBIObjectDAO().modifyBIObject(obj);
		// set data for correct loopback to the navigation tree
		response.setAttribute("isLoop", "true");
		logger.debug("OUT");
	}

	/**
	 * Find bi obj par id.
	 * 
	 * @param parIdObj the par id obj
	 * 
	 * @return the int
	 */
	public int findBIObjParId(Object parIdObj) {
		logger.debug("IN");
		String parIdStr = "";
		if (parIdObj instanceof String) {
			parIdStr = (String) parIdObj;
		} else if (parIdObj instanceof List) {
			List parIdList = (List) parIdObj;
			Iterator it = parIdList.iterator();
			while (it.hasNext()) {
				Object item = it.next();
				if (item instanceof SourceBean)
					continue;
				if (item instanceof String)
					parIdStr = (String) item;
			}
		}
		int parId = Integer.parseInt(parIdStr);
		logger.debug("OUT");
		return parId;
	}

	private Object getAttribute(SourceBean request, String attributeName) {
		logger.debug("IN");
		Object attribute = null;
		attribute = request.getAttribute(attributeName);
		if (attribute == null) {
			attribute = session.getAttribute(attributeName);
			if (attribute != null)
				session.delAttribute(attributeName);
		}
		logger.debug("OUT");
		return attribute;
	}

	/**
	 * Gets the as list.
	 * 
	 * @param o the o
	 * 
	 * @return the as list
	 */
	public List getAsList(Object o) {
		logger.debug("IN");
		ArrayList list = new ArrayList();

		if (o instanceof String) {
			String parameterValueFromLookUp = (String) o;
			list.add(parameterValueFromLookUp);
		} else {
			list.addAll((Collection) o);
		}
		logger.debug("OUT");
		return list;
	}

	/**
	 * Called after the parameter value lookup selection to continue the
	 * execution phase
	 * 
	 * @param request
	 *            The request SourceBean
	 * @param response
	 *            The response SourceBean
	 */
	private void lookUpReturnHandler(SourceBean request, SourceBean response)
			throws Exception {
		logger.debug("IN");
		// get the object from the session
		BIObject obj = (BIObject) session
				.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
		// get the parameter name and value from the request
		String parameterNameFromLookUp = (String) request
				.getAttribute("LOOKUP_PARAMETER_NAME");
		if (parameterNameFromLookUp == null)
			parameterNameFromLookUp = (String) session
					.getAttribute("LOOKUP_PARAMETER_NAME");

		String returnStatus = (String) getAttribute(request, "RETURN_STATUS");
		if (returnStatus == null)
			returnStatus = "OK";

		Object lookUpValueObj = getAttribute(request, "LOOKUP_VALUE");
		Object lookUpDescObj = getAttribute(request, "LOOKUP_DESC");

		if (lookUpValueObj != null && !returnStatus.equalsIgnoreCase("ABORT")) {

			List paramValues = getAsList(lookUpValueObj);
			List paramDescriptions = (lookUpDescObj == null) ? paramValues
					: getAsList(lookUpDescObj);

			// Set into the righr object parameter the list value
			List biparams = obj.getBiObjectParameters();
			Iterator iterParams = biparams.iterator();
			while (iterParams.hasNext()) {
				BIObjectParameter biparam = (BIObjectParameter) iterParams
						.next();
				String nameUrl = biparam.getParameterUrlName();

				if (nameUrl.equalsIgnoreCase(parameterNameFromLookUp)) {
					biparam.setParameterValues(paramValues);

					// refresh also the description
					HashMap paramsDescriptionMap = (HashMap) session
							.getAttribute("PARAMS_DESCRIPTION_MAP");
					String desc = "";
					for (int i = 0; i < paramDescriptions.size(); i++) {
						desc += (i == 0 ? "" : ";")
								+ paramDescriptions.get(i).toString();
					}
					paramsDescriptionMap.put(nameUrl, desc);
					session.setAttribute("PARAMS_DESCRIPTION_MAP",
							paramsDescriptionMap);
				}
			}
		}

		// put in session the new object
		session.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
		// get the current user profile
		IEngUserProfile profile = (IEngUserProfile) permanentSession
				.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		// get the list of the subObjects
		List subObjects = getSubObjectsList(obj, profile);
		// get the list of biobject snapshot
		List snapshots = getSnapshotList(obj);
		// get the list of viewpoints
		List viewpoints = getViewpointList(obj);
		// put in response the list of subobject names
		response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
		// put in response the list of snapshot
		response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);
		// put in response the list of viewpoints
		response.setAttribute(SpagoBIConstants.VIEWPOINT_LIST, viewpoints);
		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
				"ExecuteBIObjectPageParameter");
		logger.debug("OUT");
	}

	/**
	 * Called after the user role selection to continue the execution phase
	 * 
	 * @param request
	 *            The request SourceBean
	 * @param response
	 *            The response SourceBean
	 */
	private void selectRoleHandler(SourceBean request, SourceBean response)
			throws Exception {
		logger.debug("IN");
		// get the role selected from request
		String role = (String) request.getAttribute("role");
		session.setAttribute(SpagoBIConstants.ROLE, role);
		// get the id of the object
		String idStr = (String) request
				.getAttribute(ObjectsTreeConstants.OBJECT_ID);
		Integer id = new Integer(idStr);
		// prepare the object in session
		String userProvidedParametersStr = (String) session
				.getAttribute(ObjectsTreeConstants.PARAMETERS);
		// session.delAttribute(ObjectsTreeConstants.PARAMETERS);
		BIObject obj = execContr.prepareBIObjectInSession(session, id, role,
				userProvidedParametersStr);
		// get the current user profile
		IEngUserProfile profile = (IEngUserProfile) permanentSession
				.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		// get the list of the subObjects
		List subObjects = getSubObjectsList(obj, profile);
		// put in response the list of subobject names
		response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
		// get the list of biobject snapshot
		List snapshots = getSnapshotList(obj);
		// put in response the list of snapshot
		response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);
		// get the list of viewpoint
		List viewpoints = getViewpointList(obj);
		// put in response the list of viewpoint
		response.setAttribute(SpagoBIConstants.VIEWPOINT_LIST, viewpoints);

		// set into the execution controlle the object
		ExecutionController controller = new ExecutionController();
		controller.setBiObject(obj);

		Map paramsDescriptionMap = new HashMap();
		List biparams = obj.getBiObjectParameters();
		Iterator iterParams = biparams.iterator();
		while (iterParams.hasNext()) {
			BIObjectParameter biparam = (BIObjectParameter) iterParams.next();
			String nameUrl = biparam.getParameterUrlName();
			paramsDescriptionMap.put(nameUrl, "");
		}

		session.setAttribute("PARAMS_DESCRIPTION_MAP", paramsDescriptionMap);

		// subObj is not null if specified on preferences
		SubObject subObj = getRequiredSubObject(obj, subObjects, profile);

		// (if the object can be directly executed (because it hasn't any
		// parameter to be
		// filled by the user) and if the object has no subobject saved) or (a
		// valid subobject
		// is specified by preferences) then execute it directly without pass
		// for parameters page
		if ((controller.directExecution() && subObjects.size() == 0)
				|| subObj != null) {
			if (subObj == null)
				controlInputParameters(obj.getBiObjectParameters(), profile,
						role);
			// if there are some errors into the errorHandler does not execute
			// the BIObject
			if (!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
						"ExecuteBIObjectPageParameter");
				return;
			}
			execute(obj, subObj, null, response);
		}
		if (controller.directExecution()) {
			response.setAttribute("NO_PARAMETERS", "TRUE");
		}
		logger.debug("OUT");
	}

	/**
	 * Delete a subObject
	 * 
	 * @param request
	 *            The request SourceBean
	 * @param response
	 *            The response SourceBean
	 */
	private void deleteSubObjectHandler(SourceBean request, SourceBean response)
			throws Exception {
		logger.debug("IN");
		// get the current user profile
		IEngUserProfile profile = (IEngUserProfile) permanentSession
				.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		// get object from session
		BIObject obj = (BIObject) session
				.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
		// get id of the subobject
		String subObjIdStr = (String) request
				.getAttribute(SpagoBIConstants.SUBOBJECT_ID);
		Integer subObjId = new Integer(subObjIdStr);
		// get dao for suboject
		ISubObjectDAO subobjdao = DAOFactory.getSubObjectDAO();
		// delete subobject
		subobjdao.deleteSubObject(subObjId);
		// get from the session the execution role
		String role = (String) session.getAttribute(SpagoBIConstants.ROLE);
		// set data in response
		response.setAttribute(ObjectsTreeConstants.OBJECT_ID, obj.getId()
				.toString());
		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
				SpagoBIConstants.PUBLISHER_LOOPBACK_AFTER_DEL_SUBOBJECT);
		response.setAttribute(SpagoBIConstants.ROLE, role);
		logger.debug("OUT");
	}

	/**
	 * Get the list of subObjects of a BIObject for the current user
	 * 
	 * @param obj
	 *            BIObject container of the subObjects
	 * @param profile
	 *            profile of the user
	 * @return the List of the BIObject's subobjects visible to the current user
	 */
	private List getSubObjectsList(BIObject obj, IEngUserProfile profile) {
		logger.debug("IN");
		List subObjects = new ArrayList();
		try {
			ISubObjectDAO subobjdao = DAOFactory.getSubObjectDAO();
			subObjects = subobjdao
					.getAccessibleSubObjects(obj.getId(), profile);
		} catch (Exception e) {
			logger.error("Error retriving the subObject list", e);
		}
		logger.debug("OUT");
		return subObjects;
	}

	/**
	 * Get the list of BIObject sbapshots
	 * 
	 * @param obj
	 *            BIObject container of the snapshot
	 * @return the List of the BIObject snapshots
	 */
	private List getSnapshotList(BIObject obj) {
		logger.debug("IN");
		List snapshots = new ArrayList();
		try {
			ISnapshotDAO snapdao = DAOFactory.getSnapshotDAO();
			snapshots = snapdao.getSnapshots(obj.getId());
		} catch (Exception e) {
			logger.error("Error retriving the snapshot list", e);
		}
		logger.debug("OUT");
		return snapshots;
	}

	/**
	 * Based on the object type launches the right execution mechanism. For
	 * objects executed by an external engine instantiates the driver for
	 * execution, gets the execution call parameters map, adds in reponse the
	 * map of the parameters. For objects executed by an internal engine,
	 * instantiates the engine class and launches execution method.
	 * 
	 * @param obj
	 *            The BIobject
	 * @param subObj
	 *            The SubObjectDetail subObject to be executed (in case it is
	 *            not null)
	 * @param response
	 *            The response Source Bean
	 */
	private void execute(BIObject obj, SubObject subObj, String[] vpParameters,
			SourceBean response) {
		logger.debug("IN");

		// identity string for object execution
		UUIDGenerator uuidGen = UUIDGenerator.getInstance();
		UUID uuid = uuidGen.generateTimeBasedUUID();
		String executionId = uuid.toString();
		executionId = executionId.replaceAll("-", "");

		EMFErrorHandler errorHandler = getErrorHandler();

		// GET ENGINE ASSOCIATED TO THE BIOBJECT
		Engine engine = obj.getEngine();

		// GET THE TYPE OF ENGINE (INTERNAL / EXTERNAL) AND THE SUITABLE
		// BIOBJECT TYPES
		Domain engineType = null;
		Domain compatibleBiobjType = null;
		try {
			engineType = DAOFactory.getDomainDAO().loadDomainById(
					engine.getEngineTypeId());
			compatibleBiobjType = DAOFactory.getDomainDAO().loadDomainById(
					engine.getBiobjTypeId());
		} catch (EMFUserError error) {
			logger.error("Error retrieving document's engine information",
					error);
			errorHandler.addError(error);
			return;
		}
		String compatibleBiobjTypeCd = compatibleBiobjType.getValueCd();
		String biobjTypeCd = obj.getBiObjectTypeCode();

		// CHECK IF THE BIOBJECT IS COMPATIBLE WITH THE TYPES SUITABLE FOR THE
		// ENGINE
		if (!compatibleBiobjTypeCd.equalsIgnoreCase(biobjTypeCd)) {
			// the engine document type and the biobject type are not compatible
			logger.warn("Engine cannot execute input document type: "
					+ "the engine " + engine.getName() + " can execute '"
					+ compatibleBiobjTypeCd + "' type documents "
					+ "while the input document is a '" + biobjTypeCd + "'.");
			Vector params = new Vector();
			params.add(engine.getName());
			params.add(compatibleBiobjTypeCd);
			params.add(biobjTypeCd);
			errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
					2002, params));
			return;
		}

		// GET USER PROFILE
		IEngUserProfile profile = (IEngUserProfile) permanentSession
				.getAttribute(IEngUserProfile.ENG_USER_PROFILE);

		// IF USER CAN'T EXECUTE THE OBJECT RETURN
		if (!canExecute(profile, obj))
			return;

		// GET THE EXECUTION ROLE FROM SESSION
		String executionRole = (String) session
				.getAttribute(SpagoBIConstants.ROLE);

		// IF THE ENGINE IS EXTERNAL
		if ("EXT".equalsIgnoreCase(engineType.getValueCd())) {
			try {
				response.setAttribute("EXECUTION", "true");
				response.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR,
						obj);
				// instance the driver class
				String driverClassName = obj.getEngine().getDriverName();
				IEngineDriver aEngineDriver = (IEngineDriver) Class.forName(
						driverClassName).newInstance();
				// get the map of the parameters
				Map mapPars = null;

				if (subObj != null)
					mapPars = aEngineDriver.getParameterMap(obj, subObj,
							profile, executionRole);
				else
					mapPars = aEngineDriver.getParameterMap(obj, profile,
							executionRole);

				// adding or sobstituting parameters for viewpoint
				if (vpParameters != null) {
					for (int i = 0; i < vpParameters.length; i++) {
						String param = (String) vpParameters[i];
						String name = param.substring(0, param.indexOf("="));
						String value = param.substring(param.indexOf("=") + 1);
						if (mapPars.get(name) != null) {
							mapPars.remove(name);
							mapPars.put(name, value);
						} else
							mapPars.put(name, value);
					}
				}
			
				//GET DOC CONFIG FOR DOCUMENT COMPOSITION
				if (session.getAttribute("docConfig") != null)
						mapPars.put("docConfig", (DocumentCompositionConfiguration) session.getAttribute("docConfig"));
			
				// set into the reponse the parameters map
				response.setAttribute(ObjectsTreeConstants.REPORT_CALL_URL,
						mapPars);
				if (subObj != null) {
					response.setAttribute(SpagoBIConstants.SUBOBJECT, subObj);
				}
				// set into the reponse the execution and flow ids
				response.setAttribute("spagobi_execution_id", executionId);
				// response.setAttribute("FLOW_ID", flowId);

			} catch (Exception e) {
				logger.error("Error During object execution", e);
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
						100));
			}

			// IF THE ENGINE IS INTERNAL
		} else {

			String className = engine.getClassName();
			logger.debug("Try instantiating class " + className
					+ " for internal engine " + engine.getName() + "...");
			InternalEngineIFace internalEngine = null;
			// tries to instantiate the class for the internal engine
			try {
				if (className == null && className.trim().equals(""))
					throw new ClassNotFoundException();
				internalEngine = (InternalEngineIFace) Class.forName(className)
						.newInstance();
			} catch (ClassNotFoundException cnfe) {
				logger.error("The class ['" + className
						+ "'] for internal engine " + engine.getName()
						+ " was not found.", cnfe);
				Vector params = new Vector();
				params.add(className);
				params.add(engine.getName());
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
						2001, params));
				return;
			} catch (Exception e) {
				logger.error("Error while instantiating class " + className, e);
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
						100));
				return;
			}

			logger
					.debug("Class "
							+ className
							+ " instantiated successfully. Now engine's execution starts.");

			// starts engine's execution
			try {
				
				if (subObj != null)
					internalEngine.executeSubObject(this.getRequestContainer(),
							obj, response, subObj);
				else
					internalEngine.execute(this.getRequestContainer(), obj,
							response);
			} catch (EMFUserError e) {
				logger.error("Error while engine execution", e);
				errorHandler.addError(e);
			} catch (Exception e) {
				logger.error("Error while engine execution", e);
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
						100));
			}

		}
		logger.debug("OUT");
	}

	private boolean isSubRptStatusAdmissible(String masterRptStatus,
			String subRptStatus) {

		if (masterRptStatus.equalsIgnoreCase("DEV")) {
			if (subRptStatus.equalsIgnoreCase("DEV")
					|| subRptStatus.equalsIgnoreCase("REL"))
				return true;
			else
				return false;
		} else if (masterRptStatus.equalsIgnoreCase("TEST")) {
			if (subRptStatus.equalsIgnoreCase("TEST")
					|| subRptStatus.equalsIgnoreCase("REL"))
				return true;
			else
				return false;
		} else if (masterRptStatus.equalsIgnoreCase("REL")) {
			if (subRptStatus.equalsIgnoreCase("REL"))
				return true;
			else
				return false;
		}
		return false;
	}

	private boolean isSubRptExecutableByUser(IEngUserProfile profile,
			BIObject subrptbiobj) {
		logger.debug("IN");
		String subrptbiobjStatus = subrptbiobj.getStateCode();
		List functionalities = subrptbiobj.getFunctionalities();
		Iterator functionalitiesIt = functionalities.iterator();
		boolean isExecutableByUser = false;
		while (functionalitiesIt.hasNext()) {
			Integer functionalityId = (Integer) functionalitiesIt.next();
			if (ObjectsAccessVerifier.canDev(subrptbiobjStatus,
					functionalityId, profile)) {
				isExecutableByUser = true;
				break;
			}
			if (ObjectsAccessVerifier.canTest(subrptbiobjStatus,
					functionalityId, profile)) {
				isExecutableByUser = true;
				break;
			}
			if (ObjectsAccessVerifier.canExec(subrptbiobjStatus,
					functionalityId, profile)) {
				isExecutableByUser = true;
				break;
			}
		}
		logger.debug("OUT");
		return isExecutableByUser;
	}

	private boolean canExecute(IEngUserProfile profile, BIObject biobj) {
		logger.debug("IN");
		Integer masterReportId = biobj.getId();
		String masterReportStatus = biobj.getStateCode();

		try {
			ISubreportDAO subrptdao = DAOFactory.getSubreportDAO();
			IBIObjectDAO biobjectdao = DAOFactory.getBIObjectDAO();

			List subreportList = subrptdao
					.loadSubreportsByMasterRptId(masterReportId);
			for (int i = 0; i < subreportList.size(); i++) {
				Subreport subreport = (Subreport) subreportList.get(i);
				BIObject subrptbiobj = biobjectdao
						.loadBIObjectForDetail(subreport.getSub_rpt_id());
				if (!isSubRptStatusAdmissible(masterReportStatus, subrptbiobj
						.getStateCode())) {
					errorHandler.addError(new EMFUserError(
							EMFErrorSeverity.ERROR, 1062));
					return false;
				}
				if (!isSubRptExecutableByUser(profile, subrptbiobj)) {
					errorHandler.addError(new EMFUserError(
							EMFErrorSeverity.ERROR, 1063));
					return false;
				}
			}
		} catch (EMFUserError e) {
			logger.error("Error while reading subreports", e);
			return false;
		} finally {
			logger.debug("OUT");
		}

		return true;
	}

	/**
	 * Exec a biobject snapshot.
	 * 
	 * @param request
	 *            The request SourceBean
	 * @param response
	 *            The response SourceBean
	 */
	private void execSnapshotHandler(SourceBean request, SourceBean response)
			throws Exception {
		logger.debug("IN");
		// if single object modality, object is in request
		BIObject obj = (BIObject) request.getAttribute(SpagoBIConstants.OBJECT);
		if (obj == null) {
			// normal execution: object is in session
			obj = getBIObject();
		}
		SessionContainer sessionContainer = this.getRequestContainer().getSessionContainer();
		IEngUserProfile profile = (IEngUserProfile) sessionContainer.getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		boolean canSee = ObjectsAccessVerifier.canSee(obj, profile);
		if (!canSee) {
			logger.error("Object with label = '" + obj.getLabel()
					+ "' cannot be executed by the user!!");
			Vector v = new Vector();
			v.add(obj.getLabel());
			errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
					"1075", v, null));
			return;
		}
		
		String role = (String) sessionContainer.getAttribute(SpagoBIConstants.ROLE);
		if (role == null) {
			// define the variable for execution role
			List correctRoles = null;
			if (profile
					.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV)
					|| profile
							.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_USER)
					|| profile
							.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN))
				correctRoles = DAOFactory.getBIObjectDAO()
						.getCorrectRolesForExecution(obj.getId(), profile);
			else
				correctRoles = DAOFactory.getBIObjectDAO()
						.getCorrectRolesForExecution(obj.getId());
			logger.debug("correct roles for execution retrived " + correctRoles);

			if (role != null) {
				// if the role is specified
				if (!correctRoles.contains(role)) {

					logger.warn("Role [" + role
							+ "] is not a correct role for execution");
					Vector v = new Vector();
					v.add(role);
					errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
							1078, v, null));
					return;
				}
			} else {
				// if correct roles is more than one the user has to select one of
				// them
				// put in the response the right inforamtion for publisher in order
				// to show page role selection
				if (correctRoles.size() > 1) {
					response.setAttribute("selectionRoleForExecution", "true");
					response.setAttribute("roles", correctRoles);
					response.setAttribute(ObjectsTreeConstants.OBJECT_ID, obj.getId());
					logger
							.debug("more than one correct roles for execution, redirect to the"
									+ " role selection page");
					return;

					// if there isn't correct role put in the error stack a new
					// error
				} else if (correctRoles.size() < 1) {

					logger.warn("Object cannot be executed by no role of the user");
					errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
							1006));
					return;

					// the list contains only one role which is the right role
				} else {
					role = (String) correctRoles.get(0);
				}
			}
			logger.debug("using role " + role);

			// NOW THE EXECUTION ROLE IS SELECTED
			// put in session the execution role
			session.setAttribute(SpagoBIConstants.ROLE, role);
		}
		// propagates the object and the snapshot
		response.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
		String snapIdStr = (String) request.getAttribute(SpagoBIConstants.SNAPSHOT_ID);
		Snapshot snap = DAOFactory.getSnapshotDAO().loadSnapshot(new Integer(snapIdStr));
		response.setAttribute(SpagoBIConstants.SNAPSHOT, snap);
		// set information for the publisher
		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ViewSnapshotPubJ");
		logger.debug("OUT");
	}

	/**
	 * Based on the object type lauch the right subobject execution mechanism.
	 * For object executed by an external engine instances the driver for
	 * execution, get the execution call parameters map, add in reponse the map
	 * of the parameters.
	 * 
	 * @param request
	 *            The request SourceBean
	 * @param response
	 *            The response SourceBean
	 */
	private void executionSubObjectHandler(SourceBean request,
			SourceBean response) throws Exception {
		logger.debug("IN");
		// get object from session
		BIObject obj = (BIObject) session
				.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
		// get id of the subobject
		String subObjIdStr = (String) request
				.getAttribute(SpagoBIConstants.SUBOBJECT_ID);
		Integer subObjId = new Integer(subObjIdStr);
		ISubObjectDAO subdao = DAOFactory.getSubObjectDAO();
		SubObject subObj = subdao.getSubObject(subObjId);
		// load all the parameter value with an empty value
		List biparams = obj.getBiObjectParameters();
		Iterator iterParams = biparams.iterator();
		while (iterParams.hasNext()) {
			BIObjectParameter biparam = (BIObjectParameter) iterParams.next();
			List values = biparam.getParameterValues();
			if ((values == null) || (values.size() == 0)) {
				ArrayList paramvalues = new ArrayList();
				paramvalues.add("");
				biparam.setParameterValues(paramvalues);
			}
		}
		// execution
		execute(obj, subObj, null, response);
		logger.debug("OUT");
	}

	/**
	 * get object from session
	 */
	private BIObject getBIObject() {
		return (BIObject) session
				.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
	}

	/**
	 * set object in session
	 */
	private void setBIObject(BIObject obj) {
		session.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
	}
	
	private boolean isMultivalueParameter(BIObjectParameter biparam) {
		return (biparam.getParameterValues() != null && biparam
				.getParameterValues().size() > 1);
	}

	private void refreshParameter(BIObjectParameter biparam, SourceBean request) {
		logger.debug("IN");
		String nameUrl = biparam.getParameterUrlName();
		List paramAttrsList = request.getAttributeAsList(nameUrl);
		ArrayList paramvalues = new ArrayList();
		if (paramAttrsList.size() == 0)
			return;
		Iterator iterParAttr = paramAttrsList.iterator();
		while (iterParAttr.hasNext()) {
			String values = (String) iterParAttr.next();
			String[] value = values.split(";");
			for (int i = 0; i < value.length; i++) {
				if (!value[i].trim().equalsIgnoreCase(""))
					paramvalues.add(value[i]);
			}

		}
		if (paramvalues.size() == 0)
			biparam.setParameterValues(null);
		else
			biparam.setParameterValues(paramvalues);
		logger.debug("OUT");
	}

	private Object getLookedUpObjId(SourceBean request) {
		return (request.getAttribute("LOOKUP_OBJ_PAR_ID"));
	}

	private boolean isLookupCall(SourceBean request) {
		return (getLookedUpObjId(request) != null);
	}

	private Object getRefreshCorrelationObj(SourceBean request) {
		return (request.getAttribute("REFRESH_CORRELATION"));
	}

	private boolean isRefreshCorrelationCall(SourceBean request) {
		return (getRefreshCorrelationObj(request) != null);
	}

	private Integer getLookedUpParameterId(SourceBean request) {
		return (new Integer(findBIObjParId(getLookedUpObjId(request))));
	}

	/**
	 * Gets the looked up parameter.
	 * 
	 * @param request the request
	 * 
	 * @return the looked up parameter
	 */
	public BIObjectParameter getLookedUpParameter(SourceBean request) {
		logger.debug("IN");
		BIObjectParameter lookedupBIParameter = null;

		Integer objParId = getLookedUpParameterId(request);
		Iterator iterParams = getBIObject().getBiObjectParameters().iterator();
		while (iterParams.hasNext()) {
			BIObjectParameter aBIParameter = (BIObjectParameter) iterParams
					.next();
			if (aBIParameter.getId().equals(objParId)) {
				lookedupBIParameter = aBIParameter;
				break;
			}
		}
		logger.debug("OUT");
		return lookedupBIParameter;
	}

	/**
	 * Checks if is single value.
	 * 
	 * @param biparam the biparam
	 * 
	 * @return true, if is single value
	 */
	public boolean isSingleValue(BIObjectParameter biparam) {
		logger.debug("IN");
		boolean isSingleValue = false;
		try {
			LovResultHandler lovResultHandler = new LovResultHandler(biparam
					.getLovResult());
			if (lovResultHandler.isSingleValue())
				isSingleValue = true;
		} catch (SourceBeanException e) {
			logger.error("SourceBeanException", e);
		}
		logger.debug("OUT");
		return isSingleValue;
	}

	/**
	 * Handles the final execution of the object
	 * 
	 * @param request
	 *            The request SourceBean
	 * @param response
	 *            The response SourceBean
	 */
	private void executionHandler(SourceBean request, SourceBean response)
			throws Exception {
		logger.debug("IN");
		BIObject obj = getBIObject();

		// refresh parameter values
		List biparams = obj.getBiObjectParameters();
		Iterator iterParams = biparams.iterator();
		HashMap paramsDescriptionMap = (HashMap) session.getAttribute("PARAMS_DESCRIPTION_MAP");
		while (iterParams.hasNext()) {
			BIObjectParameter biparam = (BIObjectParameter) iterParams.next();

			String pendingDelete = (String) request
					.getAttribute("PENDING_DELETE");
			if (pendingDelete != null && !pendingDelete.trim().equals("")) {
				if (isSingleValue(biparam))
					continue;
				biparam.setParameterValues(null);
				if (paramsDescriptionMap.get(biparam.getParameterUrlName()) != null)
					paramsDescriptionMap.put(biparam.getParameterUrlName(), "");
			} else {
				refreshParameter(biparam, request);
//				String isChanged = (String) request.getAttribute(biparam
//						.getParameterUrlName()
//						+ "IsChanged");
//				if (isChanged != null && isChanged.equalsIgnoreCase("true")) {
					// refresh also the description
//					List values = biparam.getParameterValues();
//					String desc = "";
//					if (values != null) {
//						for (int i = 0; i < values.size(); i++) {
//							desc += (i == 0 ? "" : ";")
//									+ values.get(i).toString();
//						}
//					}
//					paramsDescriptionMap.put(biparam.getParameterUrlName(),
//							desc);
//				}
			}

//			session.setAttribute("PARAMS_DESCRIPTION_MAP", paramsDescriptionMap);
		}

		// it is a lookup call
		Object lookupObjParId = request.getAttribute("LOOKUP_OBJ_PAR_ID");
		if (isLookupCall(request)) {

			BIObjectParameter lookupBIParameter = getLookedUpParameter(request);

			if (lookupBIParameter == null) {
				logger.error("The BIParameter with id = "
						+ getLookedUpParameterId(request).toString()
						+ " does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1041);
			}
			ModalitiesValue modVal = lookupBIParameter.getParameter()
					.getModalityValue();

			String lookupType = (String) request.getAttribute("LOOKUP_TYPE");
			if (lookupType == null)
				lookupType = "LIST";

			if (lookupType.equalsIgnoreCase("CHECK_LIST")) {
				response.setAttribute("CHECKLIST", "true");
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
						"ChecklistLookupPublisher");
			} else if (lookupType.equalsIgnoreCase("LIST")) {
				response.setAttribute("LIST", "true");
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
						"LookupPublisher");
			} else {
				response.setAttribute("LIST", "true");
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
						"LookupPublisher");
			}

			response.setAttribute("mod_val_id", modVal.getId().toString());
			response.setAttribute("LOOKUP_PARAMETER_NAME", lookupBIParameter
					.getParameterUrlName());
			response.setAttribute("LOOKUP_PARAMETER_ID", lookupBIParameter
					.getId().toString());
			String correlatedParuseId = (String) request
					.getAttribute("correlatedParuseIdForObjParWithId_"
							+ lookupObjParId);
			if (correlatedParuseId != null && !correlatedParuseId.equals(""))
				response.setAttribute("correlated_paruse_id",
						correlatedParuseId);
			return;
		}

		IEngUserProfile profile = (IEngUserProfile) permanentSession
		.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		String role = (String) session.getAttribute(SpagoBIConstants.ROLE);
		controlInputParameters(biparams, profile, role);

		if (isRefreshCorrelationCall(request)) {
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
					"ExecuteBIObjectPageParameter");
//			// get the list of the subObjects
//			List subObjects = getSubObjectsList(obj, profile);
//			// put in response the list of subobject names
//			response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
//			// get the list of biobject snapshot
//			List snapshots = getSnapshotList(obj);
//			// put in response the list of snapshot
//			response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);
//			// get the list of viewpoint
//			List viewpoints = getViewpointList(obj);
//			// put in response the list of viewpoint
//			response.setAttribute(SpagoBIConstants.VIEWPOINT_LIST, viewpoints);

			return;
		}

		// if there are some errors into the errorHandler does not execute the
		// BIObject
		if (!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
					"ExecuteBIObjectPageParameter");
//			// get the list of the subObjects
//			List subObjects = getSubObjectsList(obj, profile);
//			// put in response the list of subobject names
//			response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
//			// get the list of biobject snapshot
//			List snapshots = getSnapshotList(obj);
//			// put in response the list of snapshot
//			response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);
//			// get the list of viewpoint
//			List viewpoints = getViewpointList(obj);
//			// put in response the list of viewpoints
//			response.setAttribute(SpagoBIConstants.VIEWPOINT_LIST, viewpoints);

			return;
		}
		// call the execution method
		execute(obj, null, null, response);
		logger.debug("OUT");
	}

	private void controlInputParameters(List biparams, IEngUserProfile profile,
			String role) throws Exception {
		logger.debug("IN");
		if (biparams == null || biparams.size() == 0)
			return;
		Iterator iterParams = biparams.iterator();
		while (iterParams.hasNext()) {
			// get biparameter
			BIObjectParameter biparam = (BIObjectParameter) iterParams.next();
			// get lov
			ModalitiesValue lov = biparam.getParameter().getModalityValue();
			if (lov.getITypeCd().equals("MAN_IN")) {
				continue;
			}
			String parameterValuesDescription = "";
			// get the lov provider detail
			String lovProvider = lov.getLovProvider();
			ILovDetail lovProvDet = LovDetailFactory.getLovFromXML(lovProvider);
			// get lov result
			String lovResult = biparam.getLovResult();
			// get lov result handler
			LovResultHandler lovResultHandler = new LovResultHandler(lovResult);
			List values = biparam.getParameterValues();
			if (values != null && values.size()>0) {
				for (int i = 0; i < values.size(); i++) {
					String value = values.get(i).toString();
					if (!value.equals("") && !lovResultHandler.containsValue(value, lovProvDet
							.getValueColumnName())) {
						biparam.setHasValidValues(false);
						logger.warn("Parameter '" + biparam.getLabel()
								+ "' cannot assume value '" + value + "'"
								+ " for user '"
								+ profile.getUserUniqueIdentifier().toString()
								+ "' with role '" + role + "'.");
						List l = new ArrayList();
						l.add(biparam.getLabel());
						l.add(value);
						EMFUserError userError = new EMFUserError(
								EMFErrorSeverity.ERROR, 1077, l);
						errorHandler.addError(userError);
					} else {
						biparam.setHasValidValues(true);
						parameterValuesDescription += lovResultHandler.getValueDescription(value, 
								lovProvDet.getValueColumnName(), lovProvDet.getDescriptionColumnName());
						if (i < values.size() - 1) parameterValuesDescription += "; ";
					}
				}
			}
			biparam.setParameterValuesDescription(parameterValuesDescription);
		}
		logger.debug("OUT");
	}

	/**
	 * Get the list ofviewpoints
	 * 
	 * @param obj
	 *            BIObject container of the viewpoint
	 * @return the List of the viewpoints
	 */
	private List getViewpointList(BIObject obj) {
		logger.debug("IN");
		List viewpoints = new ArrayList();
		try {
			IViewpointDAO biVPDAO = DAOFactory.getViewpointDAO();
			IEngUserProfile profile = (IEngUserProfile) permanentSession
					.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			viewpoints = biVPDAO.loadAccessibleViewpointsByObjId(obj.getId(), profile);
		} catch (Exception e) {
			logger.error("Error retriving the viewpoint list", e);
		} finally {
			logger.debug("OUT");
		}
		return viewpoints;
	}

	/**
	 * Save a viewpoint.
	 * 
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 * 
	 * @throws Exception the exception
	 */
	public void saveViewPoint(SourceBean request, SourceBean response)
			throws Exception {
		logger.debug("IN");
		BIObject obj = getBIObject();
		// get from the session the execution role
		String role = (String) session.getAttribute(SpagoBIConstants.ROLE);
		// get the current user profile
		IEngUserProfile profile = (IEngUserProfile) permanentSession
				.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		// get the list of the subObjects
		List subObjects = getSubObjectsList(obj, profile);
		// get the list of biobject snapshot
		List snapshots = getSnapshotList(obj);
		String nameVP = (String) request.getAttribute("tmp_nameVP");
		String descVP = (String) request.getAttribute("tmp_descVP");
		String scopeVP = (String) request.getAttribute("tmp_scopeVP");
		String ownerVP = (String) profile.getUserUniqueIdentifier();
		String contentVP = "";

		// gets parameter's values and creates a string of values
		List parameters = obj.getBiObjectParameters();
		// Map paramsDescriptionMap = new HashMap();
		Iterator iterParams = parameters.iterator();
		while (iterParams.hasNext()) {
			BIObjectParameter biparam = (BIObjectParameter) iterParams.next();
			
			String labelUrl = biparam.getParameterUrlName();
			String value = (request.getAttribute(labelUrl) == null) ? ""
					: (String) request.getAttribute(labelUrl);
			// defines the string of parameters to save into db
			contentVP = contentVP + labelUrl + "%3D" + value + "%26";
			List paramValues = getAsList(value);
			biparam.setParameterValues(paramValues);
		}
		
		controlInputParameters(obj.getBiObjectParameters(), profile,
				role);
		// if there are some errors into the errorHandler does not save the viewpoint
		if (!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
					"ExecuteBIObjectPageParameter");
			return;
		}
		
		if (contentVP != null && contentVP.endsWith("%26")) {
			contentVP = contentVP.substring(0, contentVP.length() - 3);
		}

		IViewpointDAO biViewpointDAO = DAOFactory.getViewpointDAO();
		// check if a viewpoint with the same name yet exists
		Viewpoint tmpVP = biViewpointDAO.loadViewpointByName(nameVP);
		if (tmpVP != null) {
			errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,
					6002, null));
			// put in session the new object
			session.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);
			// set into the response the right information for loopback
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
					"ExecuteBIObjectPageParameter");
			response.setAttribute(SpagoBIConstants.ROLE, role);
			// put in response the list of subobject names
			response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
			// put in response the list of snapshot
			response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);
			// get the list of viewpoint
			List viewpoints = getViewpointList(obj);
			// put in response the list of viewpoint
			response.setAttribute(SpagoBIConstants.VIEWPOINT_LIST, viewpoints);
			logger.debug("OUT");
			return;
		}
		Viewpoint aViewpoint = new Viewpoint();
		aViewpoint.setBiobjId(obj.getId());
		aViewpoint.setVpName(nameVP);
		aViewpoint.setVpOwner(ownerVP);
		aViewpoint.setVpDesc(descVP);
		aViewpoint.setVpScope(scopeVP);
		aViewpoint.setVpValueParams(contentVP);
		aViewpoint.setVpCreationDate(new Timestamp(System.currentTimeMillis()));
		biViewpointDAO.insertViewpoint(aViewpoint);

		// set data in response
		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
				"ExecuteBIObjectPageParameter");
		response.setAttribute(SpagoBIConstants.ROLE, role);
		// put in session the new object
		session.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);

		// put in response the list of subobject names
		response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
		// put in response the list of snapshot
		response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);
		// get the list of viewpoint
		List viewpoints = getViewpointList(obj);
		// put in response the list of viewpoint
		response.setAttribute(SpagoBIConstants.VIEWPOINT_LIST, viewpoints);
		logger.debug("OUT");
		return;
	}

	/**
	 * Delete a viewpoint.
	 * 
	 * @param request
	 *            The request SourceBean
	 * @param response
	 *            The response SourceBean
	 */
	private void eraseViewpoint(SourceBean request, SourceBean response)
			throws EMFUserError, SourceBeanException {
		logger.debug("IN");
		String id = (String) request.getAttribute("vpId");

		IViewpointDAO VPDAO = DAOFactory.getViewpointDAO();
		VPDAO.eraseViewpoint(new Integer(id));
		// get object from session
		BIObject obj = (BIObject) session
				.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
		// get from the session the execution role
		String role = (String) session.getAttribute(SpagoBIConstants.ROLE);
		// set data in response
		response.setAttribute(ObjectsTreeConstants.OBJECT_ID, obj.getId()
				.toString());
		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
				SpagoBIConstants.PUBLISHER_LOOPBACK_AFTER_DEL_SUBOBJECT);
		response.setAttribute(SpagoBIConstants.ROLE, role);
		logger.debug("OUT");
	}

	/**
	 * Exec a viewpoint.
	 * 
	 * @param request
	 *            The request SourceBean
	 * @param response
	 *            The response SourceBean
	 */
	private void execViewpoint(SourceBean request, SourceBean response)
			throws Exception {
		logger.debug("IN");
		// get object from session
		BIObject obj = getBIObject();
		String role = (String) session.getAttribute(SpagoBIConstants.ROLE);
		// built the url for the content recovering
		String content = (request.getAttribute("content") == null) ? ""
				: (String) request.getAttribute("content");
		content = content.replace("%26", "&");
		content = content.replace("%3D", "=");
		obj = execContr.prepareBIObjectInSession(session, obj.getId(), role,
				content);
		
		// load the object into the Execution controller
		ExecutionController controller = new ExecutionController();
		controller.setBiObject(obj);
		
		// if the object can be directly executed (because it hasn't any
		// parameter to be
		// filled by the user) then execute it directly without pass through
		// parameters page
		if (controller.directExecution()) {
			// get the current user profile
			IEngUserProfile profile = (IEngUserProfile) permanentSession
					.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			controlInputParameters(obj.getBiObjectParameters(), profile, role);
			// if there are some errors into the errorHandler does not execute
			// the BIObject
			if (!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
						"ExecuteBIObjectPageParameter");
				return;
			}
			execute(obj, null, null, response);
			response.setAttribute("NO_PARAMETERS", "TRUE");
		}
		logger.debug("OUT");
	}

	/**
	 * Gets viewpoint's parameters and view theme.
	 * 
	 * @param request
	 *            The request SourceBean
	 * @param response
	 *            The response SourceBean
	 */
	private void viewViewpoint(SourceBean request, SourceBean response)
			throws Exception {
		logger.debug("OUT");
		String id = (String) request.getAttribute("vpId");

		IViewpointDAO VPDAO = DAOFactory.getViewpointDAO();
		Viewpoint vp = VPDAO.loadViewpointByID(new Integer(id));

		BIObject obj = getBIObject();
		// get from the session the execution role
		String role = (String) session.getAttribute(SpagoBIConstants.ROLE);
		// get the current user profile
		IEngUserProfile profile = (IEngUserProfile) permanentSession
				.getAttribute(IEngUserProfile.ENG_USER_PROFILE);

		// gets parameter's values and creates a string of values
		List parameters = obj.getBiObjectParameters();
		Iterator iterParams = parameters.iterator();

		String[] vpParameters = vp.getVpValueParams().split("%26");
		while (iterParams.hasNext()) {
			BIObjectParameter biparam = (BIObjectParameter) iterParams.next();
			String labelUrl = biparam.getParameterUrlName();
			String value = "";
			for (int i = 0; i < vpParameters.length; i++) {
				if ((vpParameters[i]).substring(0,
						vpParameters[i].indexOf("%3D")).equalsIgnoreCase(
						labelUrl)) {
					value = vpParameters[i].substring(vpParameters[i]
							.indexOf("%3D") + 3);
					List paramValues = getAsList(value);
					biparam.setParameterValues(paramValues);
					// refresh also the description
					HashMap paramsDescriptionMap = (HashMap) session
							.getAttribute("PARAMS_DESCRIPTION_MAP");
					paramsDescriptionMap.put(labelUrl, value);
					session.setAttribute("PARAMS_DESCRIPTION_MAP",
							paramsDescriptionMap);

					break;
				}
			}
		}
		controlInputParameters(parameters, profile, role);
		
		// put in session the new object
		session.setAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR, obj);

		// get the list of the subObjects
		List subObjects = getSubObjectsList(obj, profile);
		// put in response the list of subobject names
		response.setAttribute(SpagoBIConstants.SUBOBJECT_LIST, subObjects);
		// get the list of biobject snapshot
		List snapshots = getSnapshotList(obj);
		// put in response the list of snapshot
		response.setAttribute(SpagoBIConstants.SNAPSHOT_LIST, snapshots);
		// get the list of viewpoint
		List viewpoints = getViewpointList(obj);
		// put in response the list of viewpoint
		response.setAttribute(SpagoBIConstants.VIEWPOINT_LIST, viewpoints);
		// set into the response the right information for loopback
		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,
				"ExecuteBIObjectPageParameter");
		response.setAttribute(SpagoBIConstants.ROLE, role);
		logger.debug("OUT");
		return;

	}
}
