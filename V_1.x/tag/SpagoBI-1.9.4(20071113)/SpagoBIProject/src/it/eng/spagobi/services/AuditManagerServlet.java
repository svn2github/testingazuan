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
package it.eng.spagobi.services;

import it.eng.spagobi.bo.dao.audit.AuditManager;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet used by SpagoBI external engines to modify the audit information (execution start time, 
 * end time, errors ...) 
 * 
 * @author Zerbetto
 *
 */
public class AuditManagerServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		SpagoBITracer.debug(AuditManager.MODULE_NAME, getClass().getName(), "service:", "Start processing request ...");
		// getting audit record id
		String auditIdStr = request.getParameter(AuditManager.AUDIT_ID);
		if (auditIdStr == null) {
			SpagoBITracer.warning(AuditManager.MODULE_NAME, getClass().getName(), "service:", "Audit record id not specified! " +
					"No operations will be performed");
			exit();
		}
		SpagoBITracer.debug(AuditManager.MODULE_NAME, getClass().getName(), "service:", "Audit id = [" + auditIdStr + "]");
		Integer auditId = new Integer(auditIdStr);
		// getting execution start time
		Long startTime = null;
		String executionStartLong = request.getParameter(AuditManager.EXECUTION_START);
		if (executionStartLong != null && !executionStartLong.trim().equals("")) {
			try {
				startTime = new Long(executionStartLong);
			} catch (NumberFormatException nfe) {
				SpagoBITracer.major(AuditManager.MODULE_NAME, getClass().getName(), "service:", 
						"Execution start time = [" + executionStartLong + "] not correct!", nfe);
			}
		}
		// getting execution end time
		Long endTime = null;
		String executionEndLong = request.getParameter(AuditManager.EXECUTION_END);
		if (executionEndLong != null && !executionEndLong.trim().equals("")) {
			try {
				endTime = new Long(executionEndLong);
			} catch (NumberFormatException nfe) {
				SpagoBITracer.major(AuditManager.MODULE_NAME, getClass().getName(), "service:", 
						"Execution end time = [" + executionStartLong + "] not correct!", nfe);
			}
		}
		// getting execution state
		String executionState = request.getParameter(AuditManager.EXECUTION_STATE);
		// getting error message
		String errorMessage = request.getParameter(AuditManager.ERROR_MESSAGE);
		// getting error code
		String errorCode = request.getParameter(AuditManager.ERROR_CODE);
		// saving modifications
		AuditManager auditManager = AuditManager.getInstance();
		auditManager.updateAudit(auditId, startTime, endTime, executionState, errorMessage, errorCode);
		// exiting
		exit();
	}
	
	private void exit() {
		SpagoBITracer.debug(AuditManager.MODULE_NAME, getClass().getName(), "exit:", "Exiting from audit servlet.");
	}
}
