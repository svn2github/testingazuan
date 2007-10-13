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

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.novell.ldap.LDAPException;


/**
 * Implementation of the IEngUserProfile interface Factory. Defines methods 
 * to get a IEngUserProfile starting from the exo user information 
 */
public class LdapUserProfileFactoryImpl implements IUserProfileFactory {
	
	/**
	 * Return an IEngUserProfile implementation starting from the Principal of the user.
	 * @param principal Principal of the current user
	 * @return The User Profile Interface implementation object
	 */
	public IEngUserProfile createUserProfile(Principal principal){
		IEngUserProfile profile = null;

		RequestContainer reqCont = RequestContainer.getRequestContainer();

		String userName = principal.getName();

		SourceBean configSingleton = (SourceBean)ConfigSingleton.getInstance();

		LDAPConnector conn=LdapConnectorFactory.createLDAPConnector();
		List ldapRoles=null;
		HashMap attributes=null;
		try {
			ldapRoles=conn.getUserGroup(userName);
			attributes=conn.getUserAttributes(userName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (LDAPException e) {
			e.printStackTrace();
		}

		List functs = new ArrayList();
		Iterator iterRoles = ldapRoles.iterator();
		while(iterRoles.hasNext()) {
			String roleName = (String)iterRoles.next();
			List functsSB = configSingleton.getFilteredSourceBeanAttributeAsList("LDAP_AUTHORIZATIONS.RELATIONS.PRIVILEDGES.PRIVILEDGE", "roleName", roleName);
            Iterator iterFunctsSB =  functsSB.iterator();
            while(iterFunctsSB.hasNext()) {
            	SourceBean functSB = (SourceBean)iterFunctsSB.next();
            	String functionalityName = (String)functSB.getAttribute("functionalityName");
            	if(!functs.contains(functionalityName)){
            		functs.add(functionalityName);
            	}
            }
		}

		profile = new SpagoBIUserProfile(userName, functs, ldapRoles, attributes);
		return profile;
	}
	
	
	
	
	
	
	public class SpagoBIUserProfile implements IEngUserProfile {

		String userId = null;
		Collection functionalities = null;
		Collection roles = null;
		Map attributes = null;
		String application = null;
		
		public SpagoBIUserProfile(String userName, Collection functs, Collection roles, Map attrs) {
			this.userId = userName;
			this.functionalities = functs;
			this.roles = roles;
			this.attributes = createMapAttributes(attrs);
		}
		
		private Map createMapAttributes(Map attr){
			Map result=new HashMap();
			
			SourceBean configSingleton = (SourceBean)ConfigSingleton.getInstance();
			SourceBean config = (SourceBean)configSingleton.getAttribute("LDAP_AUTHORIZATIONS.CONFIG");
			List attrList=config.getAttributeAsList(LDAPConnector.ATTRIBUTES_ID);
			Iterator iterAttr=attrList.iterator();
			while (iterAttr.hasNext()){
				SourceBean tmp=(SourceBean)iterAttr.next();
				String key=(String)tmp.getAttribute("name");
				String keyLdap=(String)tmp.getCharacters();
				String value=(String)attr.get(keyLdap);
				if (value!=null && key!=null){
					result.put(key, value);
				}
			}
			return result;
		}
		
		public Collection getFunctionalities() throws EMFInternalError {
			return this.functionalities;
		}

		public Collection getFunctionalitiesByRole(String arg0) throws EMFInternalError {
			return null;
		}

		public Collection getRoles() throws EMFInternalError {
			return this.roles;
		}

		public Object getUserAttribute(String arg0) throws EMFInternalError {
			return attributes.get(arg0);
		}

		public Collection getUserAttributeNames() {
			return attributes.keySet();
		}

		public Object getUserUniqueIdentifier() {
			return userId;
		}

		public boolean hasRole(String arg0) throws EMFInternalError {
			return roles.contains(arg0);
		}

		public boolean isAbleToExecuteAction(String arg0) throws EMFInternalError {
			return true;
		}

		public boolean isAbleToExecuteModuleInPage(String arg0, String arg1) throws EMFInternalError {
			return true;
		}

		public void setApplication(String arg0) throws EMFInternalError {
			this.application = arg0;
		}
	}
	
	
	
	
	
}
