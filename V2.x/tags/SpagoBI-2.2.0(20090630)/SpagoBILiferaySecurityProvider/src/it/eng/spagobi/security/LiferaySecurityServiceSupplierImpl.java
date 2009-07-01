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
package it.eng.spagobi.security;


import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleServiceUtil;
import com.liferay.portal.service.UserServiceUtil;

/**
 * 
 * @author Angelo Bernabei (angelo.bernabei@eng.it)
 * @author Sven Werlen (sven.werlen@savoirfairelinux.com)
 */
public class LiferaySecurityServiceSupplierImpl implements ISecurityServiceSupplier {


	static private Logger logger = Logger.getLogger(LiferaySecurityServiceSupplierImpl.class);

	/**
	 * Return an SpagoBIUserProfile implementation starting from the id of the
	 * user.
	 * 
	 * @param userId
	 *            the user id
	 * 
	 * @return The User Profile Interface implementation object
	 */

	public SpagoBIUserProfile createUserProfile(String userId) {
		SpagoBIUserProfile profile = new SpagoBIUserProfile();
		profile.setUniqueIdentifier(userId);
		profile.setUserId(userId);

		try {
			User user = UserServiceUtil.getUserById(Integer.parseInt(userId));
			if (user != null) {
				// user attributes
				HashMap<String, String> userAttributes = new HashMap<String, String>();

				userAttributes.put("USER_ID", String.valueOf(user.getUserId()));
				userAttributes.put("NAME", user.getFirstName());
				userAttributes.put("SURNAME", user.getLastName());
				userAttributes.put("E_MAIL", user.getEmailAddress());

				logger.debug("user.getUserId()="+ user.getUserId());
				logger.debug( "user.getFirstName()="+ user.getFirstName());
				logger.debug( "user.getLastName()="+ user.getLastName());

				profile.setAttributes(userAttributes);

				// user roles
				List<com.liferay.portal.model.Role> roles = RoleServiceUtil.getUserRoles(user.getUserId());
				String[] roleNames = new String[roles.size()];
				if (roles != null) {
					Iterator<com.liferay.portal.model.Role> iter = roles.iterator();
					int i = 0;
					while (iter.hasNext()) {
						com.liferay.portal.model.Role role = iter.next();
						logger.debug("ruolo.getName()="+ role.getName());
						logger.debug("ruolo.getDescription()="+ role.getDescription());
						logger.debug("ruolo.getRoleId()=" + role.getRoleId());
						roleNames[i++] = role.getName();
					}
				}
				else {
					logger.warn("THE LIST OF ROLES IS EMPTY, CHECK THE PROFILING CONFIGURATION...");
				} 
				profile.setRoles(roleNames);
			}

		} catch (SystemException e) {
			logger.error( "SystemException", e);
		} catch (PortalException e) {
			logger.error("PortalException", e);
		}
		
		return profile;
	}

	public SpagoBIUserProfile checkAuthentication(String userId, String psw) {
		logger.error("checkAuthentication NOT implemented");
		return null;
	}

	public SpagoBIUserProfile checkAuthenticationWithToken(String userId,
			String token) {
		logger.error("checkAuthenticationWithToken NOT implemented");
		return null;
	}

	public boolean checkAuthorization(String userId, String function) {
		logger.error("checkAuthorization NOT implemented");
		return false;
	}


	


}
