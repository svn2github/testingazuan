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

public class SpagoBIAccessFilter implements Filter {

	public void destroy() {
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// parameters required for document-to-document drill
		String username = request.getParameter("username");
		String spagobiContextUrl = request.getParameter("spagobicontext");
		//String spagobiFlowId = request.getParameter("spagobi_flow_id");
		// parameters required for auditing
		String auditId = request.getParameter("SPAGOBI_AUDIT_ID");
		String auditServlet = request.getParameter("SPAGOBI_AUDIT_SERVLET");
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpSession session = httpRequest.getSession();
			if (username != null) session.setAttribute("username", username);
			if (spagobiContextUrl != null) session.setAttribute("spagobicontext", spagobiContextUrl);
			//if (spagobiFlowId != null) session.setAttribute("spagobi_flow_id", spagobiFlowId);
			if (auditId != null && auditServlet != null) {
				AuditAccessUtils auditAccessUtils = (AuditAccessUtils) session.getAttribute("SPAGOBI_AUDIT_UTILS");
				if (auditAccessUtils == null) {
					auditAccessUtils = new AuditAccessUtils(auditId, auditServlet);
					session.setAttribute("SPAGOBI_AUDIT_UTILS", auditAccessUtils);
				} else {
					//if (auditAccessUtils.getAuditId().equals(auditId)) auditAccessUtils.setIsNewExecution(false);
					//else {
						auditAccessUtils.addAuditId(auditId);
						//auditAccessUtils.setIsNewExecution(true);
					//}
				}
			}
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
		// do nothing
	}

}
