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
package it.eng.spagobi.utilities.callbacks.audit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class AuditAccessUtils {

	private List _auditIds;
	//private String _auditId;
	private String _auditServlet;
	//private boolean _isNewExecution = true;
	
	public AuditAccessUtils (String auditId, String auditServlet) {
		_auditIds = new ArrayList();
		_auditIds.add(auditId);
		//_auditId = auditId;
		_auditServlet = auditServlet;
	}
	
	public List getAuditIds() {
		return _auditIds;
	}

	public void addAuditId(String auditId) {
		_auditIds.add(auditId);
	}

	public String getAuditServlet() {
		return _auditServlet;
	}

	public void setAuditServlet(String servlet) {
		_auditServlet = servlet;
	}

//	public boolean isNewExecution() {
//		return _isNewExecution;
//	}
//
//	public void setIsNewExecution(boolean isNewExecution) {
//		_isNewExecution = isNewExecution;
//	}
	
	/**
	 * Updates the audit record with the id specified using the constructor or by the setAuditId method.
	 * It makes an http call to the servlet specified using the constructor or by the setAuditServlet method.
	 * If the current execution is not a new one (examples: page refresh, portlet rendering) nothing is updated.
	 * 
	 * @param auditId The id of the audit record to be modified
	 * @param startTime The start time
	 * @param endTime The end time
	 * @param executionState The execution state
	 * @param errorMessage The error message
	 * @param errorCode The error code
	 */
	public void updateAudit(String auditId, Long startTime, Long endTime, String executionState, 
			String errorMessage, String errorCode) {
		//if (!_isNewExecution) return;
		if (auditId != null && !_auditIds.contains(auditId)) return;
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(_auditServlet);
	    NameValuePair[] parameters = {
	    		new NameValuePair("SPAGOBI_AUDIT_ID", auditId), 
	    		new NameValuePair("SPAGOBI_AUDIT_EXECUTION_START", startTime != null ? startTime.toString() : ""), 
	    		new NameValuePair("SPAGOBI_AUDIT_EXECUTION_END", endTime != null ? endTime.toString() : ""),				
	    		new NameValuePair("SPAGOBI_AUDIT_EXECUTION_STATE", executionState != null ? executionState : ""),
	    		new NameValuePair("SPAGOBI_AUDIT_ERROR_MESSAGE", errorMessage != null ? errorMessage : ""),
	    		new NameValuePair("SPAGOBI_AUDIT_ERROR_CODE", errorCode != null ? errorCode : "")
	    };
	    
	    // Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(parameters);
        try {
            // Execute the method.        	
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
            }
        } catch (IOException e) {
            System.err.println("Failed to get response body.");
            e.printStackTrace();
        } finally {
            // Release the connection.
          	httppost.releaseConnection();
        }
	}
	
}
