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
package it.eng.spagobi.services.session.service;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;
import it.eng.spagobi.services.session.exceptions.AuthenticationException;

import org.apache.axis.MessageContext;
import org.apache.axis.session.Session;
import org.apache.log4j.Logger;

public class SessionServiceImpl {
	
	static private Logger logger = Logger.getLogger(SessionServiceImpl.class);

	public void openSession(String userName, String password) throws AuthenticationException {
		logger.debug("IN");
    	ISecurityServiceSupplier supplier = SecurityServiceSupplierFactory.createISecurityServiceSupplier();
    	Object ris= supplier.checkAuthentication(userName, password);
    	if (ris == null) {
    		logger.error("Authentication failed for username [" + userName + "]!");
    		throw new AuthenticationException();
    	}
    	logger.debug("Authentication successful for username [" + userName + "]!");
    	SpagoBIUserProfile user = supplier.createUserProfile(userName);
    	IEngUserProfile profile = new UserProfile(user);
    	logger.debug("User profile for username [" + userName + "] created.");
		MessageContext mc = MessageContext.getCurrentContext();
        Session wsSession =  mc.getSession();
        wsSession.set(IEngUserProfile.ENG_USER_PROFILE, profile);
        logger.debug("User profile for username [" + userName + "] put on session.");
        logger.debug("OUT");
	}
	
	public void openSessionWithToken(String userName, String token) throws AuthenticationException {
		logger.debug("IN");
        logger.debug("OUT");
	}
	
	public void closeSession() {
		logger.debug("IN");
		MessageContext mc = MessageContext.getCurrentContext();
        Session wsSession =  mc.getSession();
        wsSession.remove(IEngUserProfile.ENG_USER_PROFILE);
        wsSession.invalidate();
		logger.debug("OUT");
	}
	
	public static IEngUserProfile getUserProfile() {
		logger.debug("IN");
		MessageContext mc = MessageContext.getCurrentContext();
        Session wsSession =  mc.getSession();
        IEngUserProfile profile = (IEngUserProfile) wsSession.get(IEngUserProfile.ENG_USER_PROFILE);
        logger.debug("OUT");
        return profile;
	}
	
}
