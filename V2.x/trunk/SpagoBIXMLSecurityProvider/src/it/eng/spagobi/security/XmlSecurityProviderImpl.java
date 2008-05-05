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

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.commons.bo.Role;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class XmlSecurityProviderImpl implements ISecurityInfoProvider {
	
	/**
	 * Get all the roles.
	 * 
	 * @return List of the roles (list of it it.eng.spagobi.bo.Role)
	 */
	public List getRoles() {
		List roles = new ArrayList();
		ConfigSingleton config = ConfigSingleton.getInstance();
		List sb_roles = config.getAttributeAsList("AUTHORIZATIONS.ENTITIES.ROLES.ROLE");
		Iterator iter_sb_roles = sb_roles.iterator();
		while(iter_sb_roles.hasNext()) {
			SourceBean roleSB = (SourceBean)iter_sb_roles.next();
			String roleName = (String)roleSB.getAttribute("roleName");
			String roleDescription = (String)roleSB.getAttribute("description");
			Role role = new Role(roleName, roleDescription);
	    	roles.add(role);
		}
		return roles;
	}
	
	/**
	 * Get the list of the user roles. If the user doesn't exist the roles list is empty
	 * 
	 * @param user Username
	 * @param config The SourceBean configuration
	 * 
	 * @return List of user roles (list of it.eng.spagobi.bo.Role)
	 */
	public List getUserRoles(String user, SourceBean config) {
		List roles = new ArrayList();
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		List sb_behavs = spagoconfig.getFilteredSourceBeanAttributeAsList("AUTHORIZATIONS.RELATIONS.BEHAVIOURS.BEHAVIOUR", "userID", user);
		Iterator iter_sb_behavs = sb_behavs.iterator();
		while(iter_sb_behavs.hasNext()) {
			SourceBean behavSB = (SourceBean)iter_sb_behavs.next();
			String roleName = (String)behavSB.getAttribute("roleName");
			SourceBean roleSB = (SourceBean)spagoconfig.getFilteredSourceBeanAttribute("AUTHORIZATIONS.ENTITIES.ROLES.ROLE", "roleName", roleName);
			if(roleSB!=null) {
				String roleDescription = (String)roleSB.getAttribute("description");
				Role role = new Role(roleName, roleDescription);
		    	roles.add(role);
			}
		}
		return roles;
	}

	/**
	 * Gets the list of names of all attributes of all profiles .
	 * 
	 * @return the list of names of all attributes of all profiles defined
	 */
	public List getAllProfileAttributesNames() {
		List toReturn = new ArrayList();
		return toReturn;
	}

	
	/**
	 * Authenticate a user.
	 * 
	 * @param userName the username
	 * @param password bytes of the password, certificate, ...
	 * 
	 * @return true if the user is autheticated false otherwise
	 */
	public boolean authenticateUser(String userName, byte[] password) {
		// get config
		SourceBean configSingleton = (SourceBean)ConfigSingleton.getInstance();
		// authenticate
		SourceBean userSB = (SourceBean)configSingleton.getFilteredSourceBeanAttribute("AUTHORIZATIONS.ENTITIES.USERS.USER", "userID", userName);
		if(userSB==null) {
			return false;
		} else {
			String storedPwd = (String)userSB.getAttribute("password");
			String requestPwd = new String(password);
			if(storedPwd.equals(requestPwd)){
				return true;
			} else {
				return false;
			}
		}
	}
	
}
