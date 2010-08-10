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
/*
 * Created on 21-apr-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.services.modules;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.managers.ExecutionManager;
import it.eng.spagobi.managers.ExecutionManager.ExecutionInstance;
import it.eng.spagobi.security.IUserProfileFactory;
import it.eng.spagobi.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class DirectExecutionModule extends AbstractModule {

	/**
	 * @see it.eng.spago.dispatching.action.AbstractHttpAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		// get spago containers
		RequestContainer reqContainer = getRequestContainer();
		SessionContainer sessionContainer = reqContainer.getSessionContainer();
		BIObject obj = null;
		String documentParameters = "";
		String executionRole = null;
		String operation = (String) request.getAttribute("OPERATION");
		if (operation != null && operation.equalsIgnoreCase("REFRESH")) {
			obj = (BIObject) sessionContainer.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
			documentParameters = (String) sessionContainer.getAttribute(SpagoBIConstants.PARAMETERS);
		} else if (operation != null && operation.equalsIgnoreCase("RECOVER_EXECUTION_FROM_DRILL_FLOW")) {
			String executionId = (String) request.getAttribute("spagobi_execution_id");
			String flowId = (String) request.getAttribute("spagobi_flow_id");
			if (executionId != null && flowId != null) {
				response.setAttribute("spagobi_execution_id", executionId);
				response.setAttribute("spagobi_flow_id", flowId);
				response.setAttribute(SpagoBIConstants.IGNORE_SUB_NODES, "true");
				ExecutionManager executionManager = ExecutionManager.getInstance();
				ExecutionInstance instance = executionManager.recoverExecution(flowId, executionId);
				obj = instance.getBIObject();
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
		    	executionRole = instance.getExecutionRole();
			}
		} else {
			clearSession(sessionContainer);
			// get the user name from request if available
			
			/*
			IEngUserProfile profile = null;
			String username = (String) request.getAttribute("USERNAME");
			if (username != null && !username.trim().equals("")) {
				// if the user name is specified, create a user profile for it
				LoginModule mod = new LoginModule();
				Principal principal = mod.new SpagoBIPrincipal(username);
				// create instance of the user profile factory interface
				SourceBean configSingleton = (SourceBean)ConfigSingleton.getInstance();
				SourceBean engUserProfileFactorySB = (SourceBean) configSingleton.getAttribute("SPAGOBI.SECURITY.USER-PROFILE-FACTORY-CLASS");
				String engUserProfileFactoryClass = (String) engUserProfileFactorySB.getAttribute("className");
				engUserProfileFactoryClass = engUserProfileFactoryClass.trim(); 
				IUserProfileFactory engUserProfileFactory = (IUserProfileFactory)Class.forName(engUserProfileFactoryClass).newInstance();
				profile = engUserProfileFactory.createUserProfile(principal);
				sessionContainer.getPermanentContainer().setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
			} else {
				// if the user name is not specified, try to find the user profile in session
				profile = (IEngUserProfile) sessionContainer.getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
				if (profile == null) {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
							"DirectExecutionModule", "service", "User profile is null!!");
					throw new Exception("User profile is null!!");
				}
			}
			*/
			
			IEngUserProfile profile = (IEngUserProfile) sessionContainer.getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			if (profile == null) {
				SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
						"DirectExecutionModule", "service", "User profile retrieved from PermanentContainer is null!!");
				Object internalRequest = this.getRequestContainer().getInternalRequest();
				if (internalRequest != null && internalRequest instanceof HttpServletRequest) {
					HttpServletRequest servletRequest = (HttpServletRequest) internalRequest;
					HttpSession httpSession = servletRequest.getSession();
					profile = (IEngUserProfile) httpSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
				}
				
				if (profile == null) {
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
							"DirectExecutionModule", "service", "User profile is null!!");
					throw new Exception("User profile is null!!");
				} else {
					sessionContainer.getPermanentContainer().setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
					SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
							"DirectExecutionModule", "service", 
							"User profile was retrieved from HttpSession and put on PermanentContainer.");
				}
			}
			
			// get attribute from request
			String documentLabel = (String)request.getAttribute("DOCUMENT_LABEL");
			documentParameters = (String)request.getAttribute("DOCUMENT_PARAMETERS");
			// load biobject
			obj = DAOFactory.getBIObjectDAO().loadBIObjectByLabel(documentLabel);
			/*
	 		* we must verify that the user can see the document
			*/
			boolean canSee = ObjectsAccessVerifier.canSee(obj, profile);
			if (!canSee) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
			            "DirectExecutionModule", 
			            "service", 
			            "Object with label = '" + obj.getLabel() + "' cannot be executed by the user!!");
				Vector v = new Vector();
				v.add(obj.getLabel());
				this.getErrorHandler().addError(new EMFUserError(EMFErrorSeverity.ERROR, "1075"));
				return;
			}
		}
		
        if (executionRole != null)  {
        	response.setAttribute("spagobi_execution_role", executionRole);
		}
		// put in session execution modality
        sessionContainer.setAttribute(SpagoBIConstants.MODALITY, SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY);
        // set into the response the right information for loopback
        response.setAttribute(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
        response.setAttribute(ObjectsTreeConstants.OBJECT_ID, obj.getId().toString());
    	// set into the reponse the publisher name for object execution
        response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, SpagoBIConstants.PUBLISHER_LOOPBACK_SINGLE_OBJECT_EXEC);
        // if the parameters is set put it into the session
        if (documentParameters != null && !documentParameters.trim().equals(""))  {
           	sessionContainer.setAttribute(SpagoBIConstants.PARAMETERS, documentParameters);
		}
        
	}
	
	private void clearSession (SessionContainer session) {
		session.delAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
		session.delAttribute(SpagoBIConstants.PARAMETERS);
		session.delAttribute(SpagoBIConstants.ROLE);
		session.delAttribute("VALIDATE_PAGE_VALIDATEEXECUTEBIOBJECTPAGE");
		// session parameters for check list
		session.delAttribute("CHK_LIST_INITIALIZED");
		session.delAttribute("LOOKUP_PARAMETER_NAME");
		session.delAttribute("LOOKUP_PARAMETER_ID");
		session.delAttribute("mod_val_id");
		session.delAttribute("correlated_paruse_id");
		session.delAttribute("CHECKEDOBJECTS");
		session.delAttribute("PAGE_NUMBER");
		session.delAttribute("RETURN_VALUES");
		session.delAttribute("RETURN_FROM_MODULE");
		session.delAttribute("RETURN_STATUS");
		session.delAttribute("LOOKUP_VALUE");
		session.delAttribute("LOOKUP_DESC");
		session.delAttribute("PARAMS_DESCRIPTION_MAP");
	}

}
