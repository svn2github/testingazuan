/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2008 Engineering Ingegneria Informatica S.p.A.

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
/*
 * Created on 21-apr-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.commons.services;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.utilities.PortletUtilities;
import it.eng.spagobi.commons.utilities.UserUtilities;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

import java.security.Principal;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

/**
 * This class read user from portal and defines login operations.
 * 
 * @author Zoppello
 */
public class PortletLoginModule extends AbstractModule {

    static Logger logger = Logger.getLogger(PortletLoginModule.class);
	
	/**
	 * Service.
	 * 
	 * @param request the request
	 * @param response the response
	 * 
	 * @throws Exception the exception
	 * 
	 * @see it.eng.spago.dispatching.action.AbstractHttpAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
	        logger.debug("IN");
		PortletRequest portletRequest = PortletUtilities.getPortletRequest(); 
		Principal principal = portletRequest.getUserPrincipal();		
		IEngUserProfile profile = null;
		ISecurityServiceSupplier supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
	        try {
	            SpagoBIUserProfile user= supplier.createUserProfile(principal.getName());
	            profile=new UserProfile(user);
	        } catch (Exception e) {
	            logger.error("Reading user information... ERROR",e);
	            throw new SecurityException();
	        }
		
		logger.debug("userProfile created " + profile);
		logger.debug("Attributes name of the user profile: "+ profile.getUserAttributeNames());
		logger.debug("Functionalities of the user profile: "+ profile.getFunctionalities());
		logger.debug("Roles of the user profile: "+ profile.getRoles());	

		// put user profile into spago session container
		RequestContainer reqCont = getRequestContainer();
		SessionContainer sessionCont = reqCont.getSessionContainer();
		SessionContainer permSession = sessionCont.getPermanentContainer();
		permSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
		
		
		String username = (String)profile.getUserUniqueIdentifier();
		if(!UserUtilities.userFunctionalityRootExists(username)) {
			UserUtilities.createUserFunctionalityRoot(profile);
		}
		
		logger.debug("OUT");
	}

}
