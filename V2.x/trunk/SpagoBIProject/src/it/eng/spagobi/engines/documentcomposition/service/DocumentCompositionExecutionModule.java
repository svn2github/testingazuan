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
/*
 * Created on 21-apr-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.engines.documentcomposition.service;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 *
 */

public class DocumentCompositionExecutionModule extends AbstractModule {
	static private Logger logger = Logger.getLogger(DocumentCompositionExecutionModule.class);
	
	/**
	 * Service.
	 * 
	 * @param request the request
	 * @param response the response
	 * 
	 * @throws Exception the exception
	 * 
	 * @see it.eng.spago.dispatching.action.AbstractHttpAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		// get spago containers
		RequestContainer reqContainer = getRequestContainer();
		SessionContainer sessionContainer = reqContainer.getSessionContainer();
		
		// identity string for object execution
	    UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
	    UUID uuid = uuidGen.generateTimeBasedUUID();
	    String executionId = uuid.toString();
	    executionId = executionId.replaceAll("-", "");
	    
		String username = (String) request.getAttribute("USERNAME");

		IEngUserProfile profile = null;
		ISecurityServiceSupplier supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
        try {
            SpagoBIUserProfile user= supplier.createUserProfile(username);
            profile=new UserProfile(user);
        } catch (Exception e) {
            throw new SecurityException();
        }
		// get the user name from request if available
		if (profile == null) {
			logger.error("User profile is null!!");
			throw new Exception("User profile is null!!");
		} else {
			sessionContainer.getPermanentContainer().setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
			logger.debug("User profile was retrieved from HttpSession and put on PermanentContainer.");
		}

		
		BIObject obj = null;
		String documentParameters = "";
		String executionRole = null;
		String message = (String) request.getAttribute("MESSAGEDET");
		if (message != null && message.equals("LOOPBACK_AFTER_EXEC_DOC")){
			String objId = (String)request.getAttribute("OBJECT_ID");
	        // set into the response the right information for loopback
	        if (objId != null) response.setAttribute(ObjectsTreeConstants.OBJECT_ID, objId);
	    	// set into the reponse the publisher name for object execution
	        //response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, SpagoBIConstants.PUBLISHER_LOOPBACK_SINGLE_OBJECT_EXEC);
	        response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "OUTPUT_DOCUMENT_COMPOSITION");
		}
		else{
			//clearSession(sessionContainer);

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
				logger.error("Object with label = '" + obj.getLabel() + "' cannot be executed by the user!!");
				Vector v = new Vector();
				v.add(obj.getLabel());
				this.getErrorHandler().addError(new EMFUserError(EMFErrorSeverity.ERROR, "1075"));
				return;
			}
		
			
	        if (executionRole != null)  {
	        	response.setAttribute(SpagoBIConstants.ROLE, executionRole);
			}
			// put in session execution modality
	        sessionContainer.setAttribute(SpagoBIConstants.MODALITY, SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY);
	        // set into the response the right information for loopback
	        //response.setAttribute(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
	        response.setAttribute("OBJECT_ID", obj.getId().toString());
	        response.setAttribute("MESSAGEDET", "EXEC_PHASE_CREATE_PAGE");
	    	// set into the reponse the publisher name for object execution
	        // response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, SpagoBIConstants.PUBLISHER_LOOPBACK_SINGLE_OBJECT_EXEC);
	        // response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "loopbackCompositeDocumentExecution");
	        //response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "ExecuteBIObjectPageExecution");
	        // set into the reponse the execution id	
			response.setAttribute("spagobi_execution_id", executionId);
	        // if the parameters is set put it into the session
	        if (documentParameters != null && !documentParameters.trim().equals(""))  {
	           	sessionContainer.setAttribute(SpagoBIConstants.PARAMETERS, documentParameters);
			}
		}
	}

}
