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

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Zerbetto Davide
 *
 */
public class GetToolbarButtonsVisibilityAction extends AbstractSpagoBIAction {
	
	public static final String SERVICE_NAME = "GET_TOOLBAR_BUTTONS_VISIBILITY_ACTION";
	
	// logger component
	private static Logger logger = Logger.getLogger(GetToolbarButtonsVisibilityAction.class);
	
	public void doService() {
		logger.debug("IN");
		try {
			// retrieving execution instance from session, no need to check if user is able to execute the current document
			ExecutionInstance executionInstance = getContext().getExecutionInstance( ExecutionInstance.class.getName() );
			BIObject obj = executionInstance.getBIObject();
			IEngUserProfile profile = getUserProfile();
			try {
				JSONObject visibility = new JSONObject();
				
				boolean isExecutingSnapshot = executionInstance.getSnapshot() != null;
				
				// if executing a snapshot, refresh button makes no sense
				boolean refresh = !isExecutingSnapshot;
				visibility.put("refresh", refresh);
				
				boolean sendMail = profile.isAbleToExecuteAction(SpagoBIConstants.SEND_MAIL_FUNCTIONALITY) && !isExecutingSnapshot  && obj.getBiObjectTypeCode().equals("REPORT");
				visibility.put("sendMail", sendMail);
				
				boolean saveIntoPersonalFolder = profile.isAbleToExecuteAction(SpagoBIConstants.SAVE_INTO_FOLDER_FUNCTIONALITY) && !isExecutingSnapshot;
				visibility.put("saveIntoPersonalFolder", saveIntoPersonalFolder);
				
				boolean rememberMe = profile.isAbleToExecuteAction(SpagoBIConstants.SAVE_REMEMBER_ME_FUNCTIONALITY) && !isExecutingSnapshot;
				visibility.put("rememberMe", rememberMe);
				
				boolean notes = profile.isAbleToExecuteAction(SpagoBIConstants.SEE_NOTES_FUNCTIONALITY) && !isExecutingSnapshot;
				visibility.put("notes", notes);
				
				boolean metadata = profile.isAbleToExecuteAction(SpagoBIConstants.SEE_METADATA_FUNCTIONALITY) && !isExecutingSnapshot;
				visibility.put("metadata", metadata);
				
				writeBackToClient( new JSONSuccess( visibility ) );
			} catch (IOException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Impossible to write back the responce to the client", e);
			} catch (JSONException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize objects into a JSON object", e);
			} catch (EMFInternalError e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Error retriving user information", e);
			}
		} finally {
			logger.debug("OUT");
		}
	}

}
