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
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

public class DirectExecutionModule extends AbstractModule {

	/**
	 * @see it.eng.spago.dispatching.action.AbstractHttpAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		try {
			// get spago containers
			RequestContainer reqContainer = getRequestContainer();
			SessionContainer mainSessionContainer = reqContainer.getSessionContainer();
			String executionIdentifier = (String)request.getAttribute("EXECUTION_IDENTIFIER");
			// check if is a refresh operation
			String refresh = (String)request.getAttribute("REFRESH");
			// if is a refresh get the session container otherwise create a new one
			SessionContainer sessionContainer = null;
			if(refresh!=null) {
				sessionContainer = (SessionContainer)mainSessionContainer.getAttribute(executionIdentifier);
				// get the object
				BIObject biobj = (BIObject)sessionContainer.getAttribute("SESSION_OBJ_ATTR");
				// set into the response the right information for loopback
		        response.setAttribute(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
		        response.setAttribute(ObjectsTreeConstants.OBJECT_ID, biobj.getId().toString());
		    	// set into the reponse the publisher name for object execution
		        response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, SpagoBIConstants.PUBLISHER_LOOPBACK_SINGLE_OBJECT_EXEC);
			} else {
				sessionContainer = new SessionContainer();
				// get attribute from request
				String documentLabel = (String)request.getAttribute("DOCUMENT_LABEL");
				String documentParameters = (String)request.getAttribute("DOCUMENT_PARAMETERS");
				// load biobject
				BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectByLabel(documentLabel);
				// put in session execution modality
		        sessionContainer.setAttribute(SpagoBIConstants.MODALITY, SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY);
		        // set into the response the right information for loopback
		        response.setAttribute(SpagoBIConstants.ACTOR, SpagoBIConstants.USER_ACTOR);
		        response.setAttribute(ObjectsTreeConstants.OBJECT_ID, obj.getId().toString());
		    	// set into the reponse the publisher name for object execution
		        response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, SpagoBIConstants.PUBLISHER_LOOPBACK_SINGLE_OBJECT_EXEC);
		        // if the parameters is set put it into the session
		        if(documentParameters != null && !documentParameters.trim().equals(""))  {
		           	sessionContainer.setAttribute(SpagoBIConstants.PARAMETERS, documentParameters);
		        }
		        mainSessionContainer.setAttribute(executionIdentifier, sessionContainer);
			}
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "service", "error while launching application", e);
			EMFUserError emfue = new EMFUserError(EMFErrorSeverity.ERROR, 100);
			throw emfue;
		}
	}

}
