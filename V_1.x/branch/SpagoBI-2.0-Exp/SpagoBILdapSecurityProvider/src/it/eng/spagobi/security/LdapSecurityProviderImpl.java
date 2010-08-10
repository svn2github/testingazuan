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

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.bo.Role;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.novell.ldap.LDAPException;

/**
 * Implements the IPortalSecurityProvider interface defining method to get the 
 * system and user roles.
 */
public class LdapSecurityProviderImpl implements IPortalSecurityProvider {
	
	
	/** 
	 * Get all the roles 
	 * @return List of the roles (list of it it.eng.spagobi.bo.Role)
	 */
	public List getRoles() {
		List roles=new ArrayList();
		LDAPConnector conn=LdapConnectorFactory.createLDAPConnector();
		List ldapRoles=null;	
		try {
			ldapRoles=conn.getAllGroups();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (LDAPException e) {
			e.printStackTrace();
		}
		
		
		Iterator iter_sb_roles = ldapRoles.iterator();
		while(iter_sb_roles.hasNext()) {
			String roleStr = (String)iter_sb_roles.next();
			Role role = new Role(roleStr, roleStr);
	    	roles.add(role);
		}
		return roles;
	}
	
	/**
	 * Get the list of the user roles. If the user doesn't exist the roles list is empty
	 * @param user Username
	 * @param config The SourceBean configuration
	 * @return List of user roles (list of it.eng.spagobi.bo.Role)
	 */
	public List getUserRoles(String user, SourceBean config) {
		List roles = new ArrayList();
		LDAPConnector conn=LdapConnectorFactory.createLDAPConnector();
		List ldapRoles=null;	
		try {
			ldapRoles=conn.getUserGroup(user);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (LDAPException e) {
			e.printStackTrace();
		}
		Iterator iter_sb_roles = ldapRoles.iterator();
		while(iter_sb_roles.hasNext()) {
			String roleStr = (String)iter_sb_roles.next();
			Role role = new Role(roleStr, roleStr);
	    	roles.add(role);
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
		
		SourceBean configSingleton = (SourceBean)ConfigSingleton.getInstance();
		SourceBean config = (SourceBean)configSingleton.getAttribute("LDAP_AUTHORIZATIONS.CONFIG");
		
		List attrList=config.getAttributeAsList(LDAPConnector.ATTRIBUTES_ID);
		Iterator iterAttr=attrList.iterator();
		while (iterAttr.hasNext()){
			SourceBean tmp=(SourceBean)iterAttr.next();			
			toReturn.add((String)tmp.getAttribute("name"));
		}
		
		return toReturn;
	}

	
	/**
	 * Authenticate a user
	 * @param userName the username
	 * @param password bytes of the password, certificate, ... 
	 * @return true if the user is autheticated false otherwise
	 */
	public boolean authenticateUser(String userName, byte[] password) {
		LDAPConnector conn=LdapConnectorFactory.createLDAPConnector();
		
		try {
			return conn.autenticateUser(userName, new String(password));
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (LDAPException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
