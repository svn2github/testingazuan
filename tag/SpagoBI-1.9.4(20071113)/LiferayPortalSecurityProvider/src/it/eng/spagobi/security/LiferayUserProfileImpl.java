/**

 Copyright 2005 Engineering Ingegneria Informatica S.p.A.

 This file is part of SpagoBI.

 SpagoBI is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 any later version.

 SpagoBI is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Spago; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

 **/
package it.eng.spagobi.security;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.rmi.RemoteException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleServiceUtil;
import com.liferay.portal.service.UserServiceUtil;

/**
 * Implementation of the Spago IEngUserProfile interface
 */
public class LiferayUserProfileImpl implements IEngUserProfile {

	private String userUniqueIdentifier = null;

	private Map userAttributes = new HashMap();

	private Collection roles = new ArrayList();

	private Map functionalities = new HashMap();

	public final static String NAME = "NAME";
	public final static String SURNAME = "SURNAME";
	public final static String USER_ID = "USER_ID";
	public final static String E_MAIL = "E_MAIL";

	/**
	 * @param userUniqueIdentifier
	 * @param userAttributes
	 * @param roles
	 */
	public LiferayUserProfileImpl(Principal userPrincipal) {

		
		super();

		this.userUniqueIdentifier = userPrincipal.getName();

		SpagoBITracer.debug("UTILITIES", "LiferayUserProfileImpl",
				"LiferayUserProfileImpl()", "INPUT.userUniqueIdentifier="
						+ userUniqueIdentifier);

		this.userAttributes = new HashMap();
		this.roles = new ArrayList();

		try {
			User user = UserServiceUtil.getUserById(userUniqueIdentifier);
			if (user != null) {
				userAttributes.put(USER_ID, user.getUserId());
				userAttributes.put(NAME, user.getFirstName());
				userAttributes.put(SURNAME, user.getLastName());
				userAttributes.put(E_MAIL, user.getEmailAddress());

				SpagoBITracer.debug("UTILITIES", "LiferayUserProfileImpl",
						"LiferayUserProfileImpl()", "user.getUserId()="
								+ user.getUserId());
				SpagoBITracer.debug("UTILITIES", "LiferayUserProfileImpl",
						"LiferayUserProfileImpl()", "user.getFirstName()="
								+ user.getFirstName());
				SpagoBITracer.debug("UTILITIES", "LiferayUserProfileImpl",
						"LiferayUserProfileImpl()", "user.getLastName()="
								+ user.getLastName());
			
				Group gruppo = user.getGroup();
				if (gruppo != null) {
					SpagoBITracer.debug("UTILITIES", "LiferayUserProfileImpl",
							"LiferayUserProfileImpl()", "gruppo.getName()="
									+ gruppo.getName());
					SpagoBITracer.debug("UTILITIES", "LiferayUserProfileImpl",
							"LiferayUserProfileImpl()", "gruppo.getDescription()="
									+ gruppo.getDescription());
	
				}
				List ruoli = RoleServiceUtil.getUserRoles(user.getUserId());
				if (ruoli != null) {
					Iterator iter = ruoli.iterator();
					while (iter.hasNext()) {
						Role ruolo = (Role) iter.next();
						SpagoBITracer.debug("UTILITIES", "LiferayUserProfileImpl",
								"LiferayUserProfileImpl()", "ruolo.getName()="
										+ ruolo.getName());
						SpagoBITracer.debug("UTILITIES", "LiferayUserProfileImpl",
								"LiferayUserProfileImpl()",
								"ruolo.getDescription()=" + ruolo.getDescription());
						SpagoBITracer.debug("UTILITIES", "LiferayUserProfileImpl",
								"LiferayUserProfileImpl()", "ruolo.getRoleId()="
										+ ruolo.getRoleId());
						roles.add(ruolo.getName());
					}
				}
			}

		} catch (SystemException e) {
			SpagoBITracer.critical("UTILITIES", "LiferayUserProfileImpl",
					"LiferayUserProfileImpl()", "SystemException",e);
		} catch (RemoteException e) {
			SpagoBITracer.critical("UTILITIES", "LiferayUserProfileImpl",
					"LiferayUserProfileImpl()", "RemoteException",e);
		} catch (PortalException e) {
			SpagoBITracer.critical("UTILITIES", "LiferayUserProfileImpl",
					"LiferayUserProfileImpl()", "PortalException",e);
		}
		SpagoBITracer.debug("UTILITIES", "LiferayUserProfileImpl",
				"LiferayUserProfileImpl()", "OUT");
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#getUserUniqueIdentifier()
	 */
	public Object getUserUniqueIdentifier() {
		return userUniqueIdentifier;
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#getUserAttribute(java.lang.String)
	 */
	public Object getUserAttribute(String attributeName)
			throws EMFInternalError {
		return userAttributes.get(attributeName);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see it.eng.spago.security.IEngUserProfile#hasRole(java.lang.String)
	 */
	public boolean hasRole(String roleName) throws EMFInternalError {
		return this.roles.contains(roleName);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see it.eng.spago.security.IEngUserProfile#getRoles()
	 */
	public Collection getRoles() throws EMFInternalError {
		return this.roles;
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#getFunctionalities()
	 */
	public Collection getFunctionalities() throws EMFInternalError {
		if (functionalities != null)
			return functionalities.keySet();
		else
			return new ArrayList();
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#isAbleToExecuteAction(java.lang.String)
	 */
	public boolean isAbleToExecuteAction(String arg0) throws EMFInternalError {
		return true;
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#isAbleToExecuteModuleInPage(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean isAbleToExecuteModuleInPage(String arg0, String arg1)
			throws EMFInternalError {
		return true;
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#setApplication(java.lang.String)
	 */
	public void setApplication(String arg0) throws EMFInternalError {
	}

	public Collection getUserAttributeNames() {
		if (userAttributes != null)
			return userAttributes.keySet();
		else
			return new ArrayList();
	}

	public Collection getFunctionalitiesByRole(String arg0)
			throws EMFInternalError {
		return new ArrayList();
	}

}
