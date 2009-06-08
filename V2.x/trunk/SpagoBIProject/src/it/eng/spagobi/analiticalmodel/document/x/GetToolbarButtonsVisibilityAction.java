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

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.util.Iterator;

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
			IEngUserProfile profile = getUserProfile();
			Role virtualRole = null;
			try {
				virtualRole = getVirtualRole(profile);
			} catch (Exception e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot load information about user", e);
			}
			
			try {
				JSONObject visibility = new JSONObject();
				visibility.put("sendMail", virtualRole.isAbleToSendMail());
				visibility.put("saveIntoPersonalFolder", virtualRole.isAbleToSaveIntoPersonalFolder());
				visibility.put("rememberMe", virtualRole.isAbleToSaveRememberMe());
				visibility.put("notes", virtualRole.isAbleToSeeNotes());
				visibility.put("metadata", virtualRole.isAbleToSeeMetadata());
				writeBackToClient( new JSONSuccess( visibility ) );
			} catch (IOException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Impossible to write back the responce to the client", e);
			} catch (JSONException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize objects into a JSON object", e);
			}
		} finally {
			logger.debug("OUT");
		}
	}

	private Role getVirtualRole(IEngUserProfile profile) throws Exception {
		Role virtualRole = new Role("", "");
		Iterator it = profile.getRoles().iterator();
		while (it.hasNext()) {
			String roleName = (String) it.next();
			Role anotherRole = DAOFactory.getRoleDAO().loadByName(roleName);
			if (anotherRole.isAbleToSeeViewpoints()) virtualRole.setIsAbleToSeeSubobjects(true);
			if (anotherRole.isAbleToSeeSnapshots()) virtualRole.setIsAbleToSeeSnapshots(true);
			if (anotherRole.isAbleToSeeViewpoints()) virtualRole.setIsAbleToSeeViewpoints(true);
			if (anotherRole.isAbleToSeeMetadata()) virtualRole.setIsAbleToSeeMetadata(true);
			if (anotherRole.isAbleToSendMail()) virtualRole.setIsAbleToSendMail(true);
			if (anotherRole.isAbleToSeeNotes()) virtualRole.setIsAbleToSeeNotes(true);
			if (anotherRole.isAbleToSaveRememberMe()) virtualRole.setIsAbleToSaveRememberMe(true);
			if (anotherRole.isAbleToSaveIntoPersonalFolder()) virtualRole.setIsAbleToSaveIntoPersonalFolder(true);
		}
		return virtualRole;
	}

}
