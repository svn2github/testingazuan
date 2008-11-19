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
package it.eng.spagobi.hotlink.service;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.SubObject;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.services.BaseProfileAction;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class SaveRememberMeAction extends BaseProfileAction {

	private static final long serialVersionUID = 1L;
	static private Logger logger = Logger.getLogger(SaveRememberMeAction.class);
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.commons.services.BaseProfileAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
			throws Exception {
		
		//Check of the userId in order to keep performing the request
		super.service(serviceRequest, serviceResponse);
		
		logger.debug("IN");
		String message = null;
		freezeHttpResponse();
		HttpServletResponse httResponse = getHttpResponse();
		try {
			SessionContainer permSession = this.getRequestContainer().getSessionContainer().getPermanentContainer();
			UserProfile profile = (UserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			String userId = profile.getUserId().toString();
			String name = (String) serviceRequest.getAttribute("name");
			String description = (String) serviceRequest.getAttribute("description");
			String docIdStr = (String) serviceRequest.getAttribute(SpagoBIConstants.OBJECT_ID);
			Integer docId = new Integer(docIdStr);
			
			// check is user is able to execute the required document
			List correctRoles = ObjectsAccessVerifier.getCorrectRolesForExecution(docId, profile);
			// if no roles are suitable for execution, throws an exception
			if (correctRoles == null || correctRoles.size() == 0) {
				logger.error("No correct roles for execution for user [" + userId + "] and document with id = " + docId + "!!!!");
				throw new Exception("No correct roles for execution for user [" + userId + "] and document with id = " + docId + "!!!!");
			}
			
			// check if user is able to save RememberMe
			boolean canSaveRememberMe = false;
			Iterator it = correctRoles.iterator();
			while (it.hasNext()) {
				String roleName = (String) it.next();
				Role role = DAOFactory.getRoleDAO().loadByName(roleName);
				if (role.isAbleToSaveRememberMe()) {
					canSaveRememberMe = true;
					break;
				}
			}
			// if no roles are suitable for saving RememberMe, throws an exception
			if (!canSaveRememberMe) {
				logger.error("Current user [" + userId + "] is not able to save remember me");
				throw new Exception("Current user [" + userId + "] is not able to save remember me");
			}
			 
			String subobjectIdStr = (String) serviceRequest.getAttribute("subobject_id");
			Integer subobjectId = null;
			if (subobjectIdStr != null && !subobjectIdStr.trim().equals("")) {
				subobjectId = new Integer(subobjectIdStr);
				// check if user is able to see the required subobject
				SubObject subobject = DAOFactory.getSubObjectDAO().getSubObject(subobjectId);
				if (!subobject.getIsPublic() && !subobject.getOwner().equals(userId)) {
					logger.error("Current user [" + userId + "] CANNOT execute subobject with id = " + subobjectId + " of document with id = " + docId + "!!!!");
					throw new Exception("Current user [" + userId + "] CANNOT execute subobject with id = " + subobjectId + " of document with id = " + docId + "!!!!");
				}
			}
			String parameters = (String) serviceRequest.getAttribute("parameters");
			boolean inserted = DAOFactory.getRememberMeDAO().saveRememberMe(name, description, docId, subobjectId, userId, parameters);
			if (inserted) {
				message = "sbi.rememberme.saveOk";
			} else {
				message = "sbi.rememberme.alreadyExisting";
			}
		} catch (Exception e) {
			logger.debug("Error while saving remember me: " + e);
			message = "sbi.rememberme.errorWhileSaving";
		} finally {
			httResponse.getOutputStream().write(message.getBytes());
			httResponse.getOutputStream().flush();
			logger.debug("OUT");
		}
	}
	
}
