/**

Copyright (c) 2005-2008, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE

**/
package it.eng.spagobi.security;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.novell.ldap.LDAPException;

/**
 * Implementation of the IEngUserProfile interface Factory. Defines methods to
 * get a IEngUserProfile starting from the exo user information
 */
public class LdapUserProfileFactoryImpl implements ISecurityServiceSupplier {

    static private Logger logger = Logger.getLogger(LdapUserProfileFactoryImpl.class);

    /* (non-Javadoc)
     * @see it.eng.spagobi.services.security.service.ISecurityServiceSupplier#checkAuthorization(java.lang.String, java.lang.String)
     */
    public boolean checkAuthorization(String userId, String function) {
	logger.warn("checkAuthorization NOT implemented");
	return true;
    }

    /**
     * Return an IEngUserProfile implementation starting from the Principal of
     * the user.
     * 
     * @param userId the user id
     * 
     * @return The User Profile Interface implementation object
     */
    public SpagoBIUserProfile createUserProfile(String userId) {
	SpagoBIUserProfile profile = null;

	RequestContainer reqCont = RequestContainer.getRequestContainer();

	SourceBean configSingleton = (SourceBean) ConfigSingleton.getInstance();

	LDAPConnector conn = LdapConnectorFactory.createLDAPConnector();
	List ldapRoles = null;
	HashMap attributes = null;
	try {
	    ldapRoles = conn.getUserGroup(userId);
	    attributes = conn.getUserAttributes(userId);
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (LDAPException e) {
	    e.printStackTrace();
	}

	List functs = new ArrayList();
	Iterator iterRoles = ldapRoles.iterator();
	while (iterRoles.hasNext()) {
	    String roleName = (String) iterRoles.next();
	    List functsSB = configSingleton.getFilteredSourceBeanAttributeAsList(
		    "LDAP_AUTHORIZATIONS.RELATIONS.PRIVILEDGES.PRIVILEDGE", "roleName", roleName);
	    Iterator iterFunctsSB = functsSB.iterator();
	    while (iterFunctsSB.hasNext()) {
		SourceBean functSB = (SourceBean) iterFunctsSB.next();
		String functionalityName = (String) functSB.getAttribute("functionalityName");
		if (!functs.contains(functionalityName)) {
		    functs.add(functionalityName);
		}
	    }
	}

	//profile = new SpagoBIUserProfile(attributes, functs, ldapRoles, userId);
	return profile;
    }

    /*
     * public class SpagoBIUserProfile implements IEngUserProfile {
     * 
     * String userId = null; Collection functionalities = null; Collection roles =
     * null; Map attributes = null; String application = null;
     * 
     * public SpagoBIUserProfile(String userName, Collection functs, Collection
     * roles, Map attrs) { this.userId = userName; this.functionalities =
     * functs; this.roles = roles; this.attributes = createMapAttributes(attrs); }
     * 
     * private Map createMapAttributes(Map attr){ Map result=new HashMap();
     * 
     * SourceBean configSingleton = (SourceBean)ConfigSingleton.getInstance();
     * SourceBean config =
     * (SourceBean)configSingleton.getAttribute("LDAP_AUTHORIZATIONS.CONFIG");
     * List attrList=config.getAttributeAsList(LDAPConnector.ATTRIBUTES_ID);
     * Iterator iterAttr=attrList.iterator(); while (iterAttr.hasNext()){
     * SourceBean tmp=(SourceBean)iterAttr.next(); String
     * key=(String)tmp.getAttribute("name"); String
     * keyLdap=(String)tmp.getCharacters(); String
     * value=(String)attr.get(keyLdap); if (value!=null && key!=null){
     * result.put(key, value); } } return result; }
     * 
     * public Collection getFunctionalities() throws EMFInternalError { return
     * this.functionalities; }
     * 
     * public Collection getFunctionalitiesByRole(String arg0) throws
     * EMFInternalError { return null; }
     * 
     * public Collection getRoles() throws EMFInternalError { return this.roles; }
     * 
     * public Object getUserAttribute(String arg0) throws EMFInternalError {
     * return attributes.get(arg0); }
     * 
     * public Collection getUserAttributeNames() { return attributes.keySet(); }
     * 
     * public Object getUserUniqueIdentifier() { return userId; }
     * 
     * public boolean hasRole(String arg0) throws EMFInternalError { return
     * roles.contains(arg0); }
     * 
     * public boolean isAbleToExecuteAction(String arg0) throws EMFInternalError {
     * return true; }
     * 
     * public boolean isAbleToExecuteModuleInPage(String arg0, String arg1)
     * throws EMFInternalError { return true; }
     * 
     * public void setApplication(String arg0) throws EMFInternalError {
     * this.application = arg0; } }
     */

}
