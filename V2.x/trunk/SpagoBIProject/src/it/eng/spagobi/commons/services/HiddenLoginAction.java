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
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.dispatching.module.AbstractHttpModule;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.services.common.IProxyService;
import it.eng.spagobi.services.common.IProxyServiceFactory;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class HiddenLoginAction extends AbstractHttpAction {

    static Logger logger = Logger.getLogger(HiddenLoginAction.class);

    /**
     * @see it.eng.spago.dispatching.action.AbstractHttpAction#service(it.eng.spago.base.SourceBean,
     *      it.eng.spago.base.SourceBean)
     */
    public void service(SourceBean request, SourceBean response) throws Exception {
	logger.debug("IN");
	freezeHttpResponse();
	HttpServletRequest httpReq = getHttpRequest();
	HttpServletResponse httpResp = getHttpResponse();
	HttpSession session = httpReq.getSession();

	
	RequestContainer reqCont = RequestContainer.getRequestContainer();
	SessionContainer sessCont = reqCont.getSessionContainer();
	SessionContainer permSess = sessCont.getPermanentContainer();

	String userId = null;
	ConfigSingleton config = ConfigSingleton.getInstance();
	SourceBean validateSB = (SourceBean) config.getAttribute("SPAGOBI_SSO.ACTIVE");
	String active = (String) validateSB.getCharacters();
	logger.debug("Active SSO:" + active);
	if (active != null && active.equals("true")) {
	    IProxyService proxy = IProxyServiceFactory.createProxyService();
	    userId = proxy.readUserId(session);
	    logger.debug("got userId from IProxyService=" + userId);
	} else {
	    userId = httpReq.getParameter("userId");
	    logger.debug("got userId from Request=" + userId);
	}	
	try {

	    IEngUserProfile profile = (IEngUserProfile) permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);

	    if (profile == null) {
		logger.debug("Profile in sessione is null...");

		ISecurityServiceSupplier supplier = SecurityServiceSupplierFactory.createISecurityServiceSupplier();
		SpagoBIUserProfile user = supplier.createUserProfile(userId);
		profile = new UserProfile(user);
		permSess.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
	    } else if (!userId.equalsIgnoreCase((String)profile.getUserUniqueIdentifier())){
		logger.debug("Change the user profile ...");
		permSess.delAttribute(IEngUserProfile.ENG_USER_PROFILE);
		ISecurityServiceSupplier supplier = SecurityServiceSupplierFactory.createISecurityServiceSupplier();
		SpagoBIUserProfile user = supplier.createUserProfile(userId);
		profile = new UserProfile(user);
		permSess.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);		
	    }
	    byte[] content = "".getBytes();
	    httpResp.setContentLength(content.length);
	    httpResp.getOutputStream().write(content);
	    httpResp.getOutputStream().flush();

	} catch (Exception e) {
	    logger.error("Reading user information... ERROR", e);
	    throw new SecurityException();
	} finally {
	    logger.debug("OUT");
	}
    }

}
