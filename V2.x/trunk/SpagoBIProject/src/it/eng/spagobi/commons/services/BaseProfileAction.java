/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.commons.services;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.services.common.SsoServiceInterface;
import it.eng.spagobi.services.common.SsoServiceFactory;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

import org.apache.log4j.Logger;

/**
 * @author Chiara Chiarelli
 */

public abstract class BaseProfileAction extends AbstractHttpAction {

    static Logger logger = Logger.getLogger(BaseProfileAction.class);

    public void service(SourceBean request, SourceBean response) throws Exception {
	logger.debug("IN");
	IEngUserProfile profile = null;
	// Get userid from request
	String requestUserId = request.getAttribute("userid").toString();
	String sessionUserId = "";
	String userId = "";
	// Check if SSO is active
	ConfigSingleton serverConfig = ConfigSingleton.getInstance();
	SourceBean validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.ACTIVE");
	String active = (String) validateSB.getCharacters();

	// If SSO is active gets userid in session
	if (active != null && active.equals("true")) {
	    SsoServiceInterface ssoProxy = SsoServiceFactory.createProxyService();
	    sessionUserId = ssoProxy.readUserId(this.getHttpRequest().getSession());
	    if (sessionUserId != null) {
		// if userid in session different from userid in request throws
		// exception
		if (requestUserId != null && !requestUserId.equals(sessionUserId)) {
		    logger.error("The user can not execute this module.requestUserId=" + requestUserId
			    + "/sessionUserId=" + sessionUserId);
		    throw new SecurityException("Invalid userid");
		}
	    } else {
		logger.error("The user can not execute this module");
		throw new SecurityException("Invalid userid");
	    }
	}

	RequestContainer reqCont = RequestContainer.getRequestContainer();
	SessionContainer sessCont = reqCont.getSessionContainer();
	SessionContainer permSess = sessCont.getPermanentContainer();
	profile = (IEngUserProfile) permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	if (profile == null) {
	    if (sessionUserId != null) {
		userId = sessionUserId;
	    } else {
		userId = requestUserId;
	    }
	    ISecurityServiceSupplier supplier = SecurityServiceSupplierFactory.createISecurityServiceSupplier();
	    try {
		SpagoBIUserProfile user = supplier.createUserProfile(userId);
		profile = new UserProfile(user);
		// put user profile into session
		permSess.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
	    } catch (Exception e) {
		logger.error("Reading user information... ERROR", e);
		throw new SecurityException();
	    }

	}

	logger.debug("OUT");
    }

}
