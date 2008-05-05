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

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;


public class XmlUserProfileFactoryImpl implements ISecurityServiceSupplier {
	
	static private Logger logger = Logger.getLogger(XmlUserProfileFactoryImpl.class);
	

    /* (non-Javadoc)
     * @see it.eng.spagobi.services.security.service.ISecurityServiceSupplier#checkAuthorization(java.lang.String, java.lang.String)
     */
    public boolean checkAuthorization(String userId,String function){
        logger.warn("checkAuthorization NOT implemented");
        return true;
    }
    
	/**
	 * Return an SpagoBIUserProfile implementation starting from the id of the user.
	 * 
	 * @param userId the user id
	 * 
	 * @return The User Profile Interface implementation object
	 */
	
	public SpagoBIUserProfile createUserProfile(String userId){
		 logger.debug("IN - userId: " + userId);
		 
		 SpagoBIUserProfile profile=new SpagoBIUserProfile();
         profile.setUserId(userId);

		// get request container
		RequestContainer reqCont = RequestContainer.getRequestContainer();
		// get user name
		String userName = userId;
		// get config
		SourceBean configSingleton = (SourceBean)ConfigSingleton.getInstance();
		// get roles of the user
		List rolesSB = configSingleton.getFilteredSourceBeanAttributeAsList("AUTHORIZATIONS.RELATIONS.BEHAVIOURS.BEHAVIOUR", "userID", userName);
		List roles = new ArrayList();
		Iterator iterRolesSB = rolesSB.iterator();
		while(iterRolesSB.hasNext()) {
			SourceBean roleSB = (SourceBean)iterRolesSB.next();
			String rolename = (String)roleSB.getAttribute("roleName");
			roles.add(rolename);
		}
		// load all functionalities
		List functs = new ArrayList();
		Iterator iterRoles = roles.iterator();
		while(iterRoles.hasNext()) {
			String functName = (String)iterRoles.next();
			List functsSB = configSingleton.getFilteredSourceBeanAttributeAsList("AUTHORIZATIONS.RELATIONS.PRIVILEDGES.PRIVILEDGE", "roleName", functName);
            Iterator iterFunctsSB =  functsSB.iterator();
            while(iterFunctsSB.hasNext()) {
            	SourceBean functSB = (SourceBean)iterFunctsSB.next();
            	String functionalityName = (String)functSB.getAttribute("functionalityName");
            	if(!functs.contains(functionalityName)){
            		functs.add(functionalityName);
            	}
            }
		}

		//start load profile attributes 
		HashMap userAttributes = new HashMap();
	//	userAttributes = SecurityProviderUtilities.getUserProfileAttributes(userId, service);
		logger.debug("Attributes load into SpagoBI profile: " + userAttributes);
	
		// end load profile attributes
		
		String[] roleStr=new String[roles.size()];
		for (int i=0;i<roles.size();i++){
		    roleStr[i]=(String)roles.get(i);
		}
		String[] functionStr=new String[functs.size()];
		for (int i=0;i<functs.size();i++){
			functionStr[i]=(String)functs.get(i);
		}
		
		profile.setRoles(roleStr);
		profile.setAttributes(userAttributes);
		profile.setFunctions(functionStr);
		
		logger.debug("OUT");
		return profile;
	}
	
}
