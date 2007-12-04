/**
 * Copyright (c) 2005, Engineering Ingegneria Informatica s.p.a.
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

 */
package it.eng.spagobi.utilities.filters;

import it.eng.spago.security.IEngUserProfile;
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

public class SpagoBIAccessFilter implements Filter {

    private static transient Logger logger = Logger.getLogger(SpagoBIAccessFilter.class);
    
	public void destroy() {
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// parameters required for document-to-document drill

		String spagobiContextUrl = request.getParameter("spagobicontext");
		// parameters required for auditing
		String auditId = request.getParameter("SPAGOBI_AUDIT_ID");
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpSession session = httpRequest.getSession();		    
			// USER PROFILE
			String userId = (String) request.getParameter("userId");
			logger.info("Filter USER_ID:"+ userId);
			IEngUserProfile profile = null;
			try {
			    profile = (IEngUserProfile) session.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			    if (profile == null) {
				SecurityServiceProxy proxy = new SecurityServiceProxy();
				profile = proxy.getUserProfile(userId);
				session.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
			    }
			} catch (SecurityException e1) {
			    logger.error("SecurityException", e1);
			    throw new ServletException();
			}

			if (spagobiContextUrl != null) session.setAttribute("spagobicontext", spagobiContextUrl);
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
	}

	public void init(FilterConfig config) throws ServletException {
		// do nothing
	}

}
