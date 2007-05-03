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
		// parameters required for auditing
		String auditId = request.getParameter("SPAGOBI_AUDIT_ID");
		String auditServlet = request.getParameter("SPAGOBI_AUDIT_SERVLET");
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpSession session = httpRequest.getSession();
			if (username != null) session.setAttribute("username", username);
			if (spagobiContextUrl != null) session.setAttribute("spagobicontext", spagobiContextUrl);
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
