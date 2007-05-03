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

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.dao.audit.AuditManager;
import it.eng.spagobi.metadata.SbiAudit;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.IOException;
import java.util.Date;

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
		Integer id = new Integer(auditIdStr);
		// getting audit record with the specified id
		AuditManager auditManager = AuditManager.getInstance();
		SbiAudit audit = null;
		try {
			audit = auditManager.loadAudit(id);
		} catch (EMFUserError e) {
			SpagoBITracer.major(AuditManager.MODULE_NAME, getClass().getName(), "service:", "Error while retrieving audit record" +
					"with id = [" + auditIdStr + "]", e);
			exit();
		}
		// modifying audit record
		String executionStartLong = request.getParameter(AuditManager.EXECUTION_START);
		if (executionStartLong != null && !executionStartLong.trim().equals("")) {
			try {
				long startTime = Long.parseLong(executionStartLong);
				Date executionStartTime = new Date(startTime);
				audit.setExecutionStartTime(executionStartTime);
			} catch (NumberFormatException nfe) {
				SpagoBITracer.major(AuditManager.MODULE_NAME, getClass().getName(), "service:", 
						"Execution start time = [" + executionStartLong + "] not correct!", nfe);
			}
		}
		String executionEndLong = request.getParameter(AuditManager.EXECUTION_END);
		if (executionEndLong != null && !executionEndLong.trim().equals("")) {
			try {
				long endTime = Long.parseLong(executionEndLong);
				Date executionEndTime = new Date(endTime);
				audit.setExecutionEndTime(executionEndTime);
			} catch (NumberFormatException nfe) {
				SpagoBITracer.major(AuditManager.MODULE_NAME, getClass().getName(), "service:", 
						"Execution end time = [" + executionStartLong + "] not correct!", nfe);
			}
		}
		String executionState = request.getParameter(AuditManager.EXECUTION_STATE);
		if (executionState != null && !executionState.trim().equals("")) {
			audit.setExecutionState(executionState);
		}
		String errorMessage = request.getParameter(AuditManager.ERROR_MESSAGE);
		if (errorMessage != null && !errorMessage.trim().equals("")) {
			audit.setErrorMessage(errorMessage);
		}
		String errorCode = request.getParameter(AuditManager.ERROR_CODE);
		if (errorCode != null && !errorCode.trim().equals("")) {
			audit.setErrorCode(errorCode);
		}
		// saving modifications
		try {
			auditManager.modifyAudit(audit);
		} catch (EMFUserError e) {
			SpagoBITracer.major(AuditManager.MODULE_NAME, getClass().getName(), "service:", 
					"Error saving audit record", e);
		}
		// exiting
		exit();
	}
	
	private void exit() {
		SpagoBITracer.debug(AuditManager.MODULE_NAME, getClass().getName(), "exit:", "Exiting from audit servlet.");
	}
}
