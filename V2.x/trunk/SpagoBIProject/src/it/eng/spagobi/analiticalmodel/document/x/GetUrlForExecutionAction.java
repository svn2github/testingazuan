/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.document.x;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Zerbetto Davide
 */
public class GetUrlForExecutionAction extends AbstractSpagoBIAction {
	
	public static final String SERVICE_NAME = "GET_URL_FOR_EXECUTION_ACTION";
	
	// logger component
	private static Logger logger = Logger.getLogger(GetUrlForExecutionAction.class);
	
	public void doService() {
		logger.debug("IN");
		ExecutionInstance executionInstance;
		
		try {
			// retrieving execution instance from session, no need to check if user is able to execute the required document
			executionInstance = getContext().getExecutionInstance( ExecutionInstance.class.getName() );
			
			JSONObject executionInstanceJSON = this.getAttributeAsJSONObject( "PARAMETERS" );
			
			executionInstance.refreshParametersValues(executionInstanceJSON, false);
			List errors = null;
			try {
				errors = executionInstance.getParametersErrors();
			} catch (Exception e) {
				throw new SpagoBIServiceException("Impossible to find errors on parameters validation", e);
			}
			if ( errors != null && errors.size() > 0) {
				// there are errors on parameters validation, send them to the client
				JSONArray response = new JSONArray();
				Iterator errorsIt = errors.iterator();
				while (errorsIt.hasNext()) {
					EMFUserError error = (EMFUserError) errorsIt.next();
					response.put(error.getDescription());
				}
				try {
					writeBackToClient( new JSONSuccess( response ) );
				} catch (IOException e) {
					throw new SpagoBIServiceException("Impossible to write back the responce to the client", e);
				}
			} else {
				// there are no errors, we can proceed, so calculate the execution url and send it back to the client
				JSONObject response = new JSONObject();
				String url = executionInstance.getExecutionUrl();
				try {
					response.put("url", url);
					writeBackToClient( new JSONSuccess( response ) );
				} catch (IOException e) {
					throw new SpagoBIServiceException("Impossible to write back the responce to the client", e);
				} catch (JSONException e) {
					throw new SpagoBIServiceException("Impossible to serialize the url [" + url + "] to the client", e);
				}
			}
		} finally {
			logger.debug("OUT");
		}
	}

}
