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
package it.eng.spagobi.utilities.filters;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.services.common.EnginConf;
import it.eng.spagobi.services.common.SsoServiceInterface;
import it.eng.spagobi.services.common.SsoServiceFactory;
import it.eng.spagobi.services.proxy.SecurityServiceProxy;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

public class SpagoBIAccessFilter implements Filter {

    private static transient Logger logger = Logger.getLogger(SpagoBIAccessFilter.class);

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
	// do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	    ServletException {
	// parameters required for document-to-document drill
	logger.debug("IN");

	// parameters required for auditing
	String auditId = request.getParameter("SPAGOBI_AUDIT_ID");
	logger.debug("auditId:" + auditId);
	if (request instanceof HttpServletRequest) {
	    HttpServletRequest httpRequest = (HttpServletRequest) request;
	    HttpSession session = httpRequest.getSession();
	    String requestUrl = httpRequest.getRequestURL().toString();
	    logger.info("requestUrl:" + requestUrl);

	    String userIdentifier = (String) request.getParameter("userId");
	    String document = (String) request.getParameter("document");
	    logger.info("Filter userIdentifier from request:" + userIdentifier);
	    logger.info("Filter document  from request:" + document);
	    session.setAttribute("document", document);
	    session.setAttribute("isBackend", "false");
	    boolean isBackend = false;

	    if (requestUrl.endsWith("BackEnd")) {

		String passTicket = (String) request.getParameter(SpagoBIConstants.PASS_TICKET);
		if (passTicket != null && passTicket.equalsIgnoreCase(EnginConf.getInstance().getPass())) {
		    // if a request is coming from SpagoBI context
		    isBackend = true;
		    session.setAttribute("isBackend", "true");
		    logger.info("IS a backEnd Request ...");
		} else {
		    logger.warn("PassTicked is NULL in BackEnd call");
		    throw new ServletException();
		}
	    }

	    if (userIdentifier != null && !isBackend)
		userIdentifier = checkUserWithSSO(userIdentifier, session);

	    // If SSO is ACTIVE get the userId from session
	    if (EnginConf.getInstance().isSsoActive() && !isBackend) {
		userIdentifier = getUserWithSSO(session);
		logger.info("UserIdentifier from Session (SSO is Active):" + userIdentifier);
	    }
	    String spagobiContext = request.getParameter(SpagoBIConstants.SBI_CONTEXT);
	    String spagoUrl = request.getParameter(SpagoBIConstants.SBI_HOST);
	    if (spagobiContext != null) {
		logger.debug("spagobiContext:" + spagobiContext);
		session.setAttribute(SpagoBIConstants.SBI_CONTEXT, spagobiContext);
	    } else
		logger.warn("spagobiContext is null!!!!!!");
	    if (spagoUrl != null) {
		logger.debug("spagoUrl:" + spagoUrl);
		session.setAttribute(SpagoBIConstants.SBI_HOST, spagoUrl);
	    } else
		logger.warn("spagoUrl is null!!!!!!");

	    IEngUserProfile profile = null;
	    if (userIdentifier != null && !userIdentifier.equals("scheduler")) {
		try {
		    profile = (IEngUserProfile) session.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		    if (profile == null || !profile.getUserUniqueIdentifier().toString().equals(userIdentifier)) {
			SecurityServiceProxy proxy = new SecurityServiceProxy(userIdentifier, session);
			profile = proxy.getUserProfile();
			session.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
			session.setAttribute("userId", profile.getUserUniqueIdentifier());
		    } else {
			logger.debug("Found user profile in session");
		    }
		} catch (SecurityException e1) {
		    logger.error("SecurityException while reeding user profile!!!", e1);
		    throw new ServletException();
		}
	    }
	    if (userIdentifier != null && userIdentifier.equals("scheduler")) {
		session.setAttribute(IEngUserProfile.ENG_USER_PROFILE, new UserProfile("scheduler"));
	    }

	    if (auditId != null) {
		AuditAccessUtils auditAccessUtils = (AuditAccessUtils) session.getAttribute("SPAGOBI_AUDIT_UTILS");
		if (auditAccessUtils == null) {
		    auditAccessUtils = new AuditAccessUtils(auditId);
		    session.setAttribute("SPAGOBI_AUDIT_UTILS", auditAccessUtils);
		} else {
		    auditAccessUtils.addAuditId(auditId);
		}
	    }
	}
	chain.doFilter(request, response);
	/*
	 * This code doesn't show the original error try {
	 * chain.doFilter(request, response); } catch (Exception e){
	 * logger.error("--------------------------------------------------------------------------------");
	 * logger.error("SpagoBIAccessFilter" + ":doFilter ServletException
	 * !!"); logger.error(" msg: [" + e.getMessage() + "]"); Throwable t =
	 * e.getCause(); logger.error("-----------------------------");
	 * logger.error("ROOT CAUSE:");
	 * logger.error("-----------------------------"); logger.error(" msg: ["
	 * +t.getMessage() + "]"); logger.error(" stacktrace:");
	 * t.printStackTrace(); }
	 * 
	 */
	logger.debug("OUT");
    }

    private String getUserWithSSO(HttpSession session) throws ServletException {
	logger.debug("IN");
	SsoServiceInterface userProxy = SsoServiceFactory.createProxyService();
	String ssoUserIdentifier = userProxy.readUserIdentifier(session);
	logger.debug("OUT. got ssoUserId from IProxyService=" + ssoUserIdentifier);
	return ssoUserIdentifier;
    }

    private String checkUserWithSSO(String userId, HttpSession session) throws ServletException {
	logger.debug("IN");
	if (EnginConf.getInstance().isSsoActive()) {
	    SsoServiceInterface userProxy = SsoServiceFactory.createProxyService();
	    String ssoUserIdentifier = userProxy.readUserIdentifier(session);
	    logger.debug("got ssoUserId from IProxyService=" + ssoUserIdentifier);
	    logger.debug("OU: got userId from IProxyService=" + userId);
	    return ssoUserIdentifier;
	} else {
	    logger.debug("OUT. SSO is inactive...");
	    return userId;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
	// do nothing
    }

}
