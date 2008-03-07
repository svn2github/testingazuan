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
package it.eng.spagobi.engines.talend.runtime;

import it.eng.spagobi.engines.talend.utils.FileUtils;
import it.eng.spagobi.services.proxy.EventServiceProxy;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import commonj.work.Work;

public class TalendWork implements Work {

	private static transient Logger logger = Logger.getLogger(TalendWork.class);
	
	public static final String TALEND_ROLES_HANDLER_CLASS_NAME = "it.eng.spagobi.engines.drivers.talend.TalendRolesHandler";
	public static final String TALEND_PRESENTAION_HANDLER_CLASS_NAME = "it.eng.spagobi.engines.drivers.talend.TalendEventPresentationHandler";
	public static final String START_EVENT_ID = "startEventId";
	public static final String BIOBJECT_ID = "biobjectId";
	public static final String USER_NAME = "userName";
	public static final String EVENTS_MANAGER_URL = "events_manager_url";
	public static final String EVENT_TYPE = "event-type";
	public static final String DOCUMENT_EXECUTION_START = "biobj-start-execution";
	public static final String DOCUMENT_EXECUTION_END = "biobj-end-execution";
			
	private String _command = null;
	private File _executableJobDir = null;
	private String[] _envr = null;
	private List _filesToBeDeletedAfterJobExecution = null;
	private AuditAccessUtils _auditAccessUtils = null;
	private String _auditId = null;
	private Map _parameters = null;
	private HttpSession _session=null;
	
	private boolean completeWithoutError=false;
	
	
	
	public boolean isCompleteWithoutError() {
	    return completeWithoutError;
	}

	public TalendWork(String command, String[] envr, File executableJobDir, 
			List filesToBeDeletedAfterJobExecution, AuditAccessUtils auditAccessUtils, String auditId,
			Map parameters,HttpSession session) {
		this._command = command;
		this._executableJobDir = executableJobDir;
		this._envr = envr;
		this._filesToBeDeletedAfterJobExecution = filesToBeDeletedAfterJobExecution;
		this._auditAccessUtils = auditAccessUtils;
		this._auditId = auditId;
		this._parameters = parameters;
		this._session = session;
	}
	
	public void run() {
		
		String events_manager_url = (String) _parameters.get(EVENTS_MANAGER_URL);
		String user = (String) _parameters.get("userName");
		
		String userId=(String) _parameters.get("userId");
		
		
		// registering the start execution event
		String startExecutionEventDescription = "${talend.execution.started}<br/>";
		
		String parametersList = "${talend.execution.parameters}<br/><ul>";
		Set paramKeys = _parameters.keySet();
		Iterator paramKeysIt = paramKeys.iterator();
		while (paramKeysIt.hasNext()) {
			String key = (String) paramKeysIt.next();
			if (!key.equalsIgnoreCase("template") 
					&& !key.equalsIgnoreCase("biobjectId")
					&& !key.equalsIgnoreCase("cr_manager_url")
					&& !key.equalsIgnoreCase("events_manager_url")
					&& !key.equalsIgnoreCase("user")
					&& !key.equalsIgnoreCase("SPAGOBI_AUDIT_SERVLET")
					&& !key.equalsIgnoreCase("spagobicontext")
					&& !key.equalsIgnoreCase("SPAGOBI_AUDIT_ID")
					&& !key.equalsIgnoreCase("username")) {
				Object valueObj = _parameters.get(key);
				parametersList += "<li>" + key + " = " + (valueObj != null ? valueObj.toString() : "") + "</li>";
			}
		}
		parametersList += "</ul>";
		
		Map startEventParams = new HashMap();				
		startEventParams.put(EVENT_TYPE, DOCUMENT_EXECUTION_START);
		//startEventParams.put("biobj-path", params.get(TEMPLATE_PATH));
		startEventParams.put(BIOBJECT_ID, _parameters.get("document"));
		
		Integer startEventId = null;
		EventServiceProxy eventServiceProxy=new EventServiceProxy(userId,_session);

		try {
		
			String startEventParamsStr=getParamsStr(startEventParams);
			
			eventServiceProxy.fireEvent(startExecutionEventDescription + parametersList, startEventParamsStr, TALEND_ROLES_HANDLER_CLASS_NAME, TALEND_PRESENTAION_HANDLER_CLASS_NAME);
		
			//startEventId = eventsAccessUtils.fireEvent(user, startExecutionEventDescription + parametersList, startEventParams, TALEND_ROLES_HANDLER_CLASS_NAME, TALEND_PRESENTAION_HANDLER_CLASS_NAME);
		
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ":run: problems while registering the start process event", e);
		}
		
		if (_command == null) {
			// AUDIT UPDATE
			//if (_auditAccessUtils != null) _auditAccessUtils.updateAudit(_auditId, null, new Long(System.currentTimeMillis()), 
					//"EXECUTION_FAILED", "No command to be executed", null);
			logger.error("No command to be executed");
			return;
		}
		
		Map endEventParams = new HashMap();				
		endEventParams.put(EVENT_TYPE, DOCUMENT_EXECUTION_END);
		//endEventParams.put("biobj-path", params.get(TEMPLATE_PATH));
		endEventParams.put(BIOBJECT_ID, _parameters.get(BIOBJECT_ID));
		if (startEventId != null) {
			endEventParams.put(START_EVENT_ID, startEventId.toString());
		}
		
		String endExecutionEventDescription = null;
		BufferedReader input = null;
    	try { 
			String line;
			Process p = Runtime.getRuntime().exec(_command, _envr, _executableJobDir);
			
		//davide sparire
			input = new BufferedReader (new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				logger.debug(line);
				//System.out.println(line);
			}

			endExecutionEventDescription = "${talend.execution.executionOk}<br/>";
			endEventParams.put("operation-result", "success");
			
		} catch (Exception e){
			logger.error("Error while executing command " + _command, e);
			endExecutionEventDescription = "${talend.execution.executionKo}<br/>";
			endEventParams.put("operation-result", "failure");
			// AUDIT UPDATE
			//if (_auditAccessUtils != null) _auditAccessUtils.updateAudit(_auditId, null, new Long(System.currentTimeMillis()), 
				//	"EXECUTION_FAILED", "Error while executing process command", null);
		} finally {
			if (_filesToBeDeletedAfterJobExecution != null && _filesToBeDeletedAfterJobExecution.size() > 0) {
				Iterator it = _filesToBeDeletedAfterJobExecution.iterator();
				while (it.hasNext()) {
					File aFile = (File) it.next();
					if (aFile != null && aFile.exists()) FileUtils.deleteDirectory(aFile);
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {	
			
			String endEventParamsStr=getParamsStr(endEventParams);
			
			eventServiceProxy.fireEvent(endExecutionEventDescription + parametersList, endEventParamsStr, TALEND_ROLES_HANDLER_CLASS_NAME, TALEND_PRESENTAION_HANDLER_CLASS_NAME);

		} catch (Exception e) {
			logger.error(this.getClass().getName() + ":run: problems while registering the end process event", e);
		}
		completeWithoutError=true;
		
	}
	
	private String getParamsStr(Map params) {
		logger.debug("IN");
		StringBuffer buffer = new StringBuffer();
		Iterator it = params.keySet().iterator();
		boolean isFirstParameter = true;
		while(it.hasNext()) {
			String pname = (String)it.next();
			String pvalue = (String)params.get(pname);
			if(!isFirstParameter) buffer.append("&");
			else isFirstParameter = false;
			buffer.append(pname + "=" + pvalue);
		}
		logger.debug("parameters: "+buffer.toString());
		logger.debug("OUT");
		return buffer.toString();
	}


	public boolean isDaemon() {
	    return false;
	}


	public void release() {
	    logger.debug("IN");
	    
	}
	
}
