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
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;
import it.eng.spagobi.utilities.callbacks.events.EventsAccessUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class JobRunnerThread extends Thread {

	private static transient Logger logger = Logger.getLogger(JobRunnerThread.class);
	
	public static final String TALEND_ROLES_HANDLER_CLASS_NAME = "it.eng.spagobi.drivers.talend.events.handlers.TalendRolesHandler";
	public static final String TALEND_PRESENTAION_HANDLER_CLASS_NAME = "it.eng.spagobi.drivers.talend.events.handlers.TalendEventPresentationHandler";
	
	private String _command = null;
	private File _executableJobDir = null;
	private String[] _envr = null;
	private List _filesToBeDeletedAfterJobExecution = null;
	private AuditAccessUtils _auditAccessUtils = null;
	private String _auditId = null;
	private Map _parameters = null;
	
	public JobRunnerThread(String command, String[] envr, File executableJobDir, 
			List filesToBeDeletedAfterJobExecution, AuditAccessUtils auditAccessUtils, String auditId,
			Map parameters) {
		this._command = command;
		this._executableJobDir = executableJobDir;
		this._envr = envr;
		this._filesToBeDeletedAfterJobExecution = filesToBeDeletedAfterJobExecution;
		this._auditAccessUtils = auditAccessUtils;
		this._auditId = auditId;
		this._parameters = parameters;
	}
	
	public void run() {
		
		String events_manager_url = (String) _parameters.get(EventsAccessUtils.EVENTS_MANAGER_URL);
		EventsAccessUtils eventsAccessUtils = new EventsAccessUtils(events_manager_url);	
		String user = (String) _parameters.get(EventsAccessUtils.USER);
		
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
		startEventParams.put(EventsAccessUtils.EVENT_TYPE, EventsAccessUtils.DOCUMENT_EXECUTION_START);
		//startEventParams.put("biobj-path", params.get(TEMPLATE_PATH));
		startEventParams.put(EventsAccessUtils.BIOBJECT_ID, _parameters.get(EventsAccessUtils.BIOBJECT_ID));
		
		Integer startEventId = null;
		try {
			startEventId = eventsAccessUtils.fireEvent(user, startExecutionEventDescription + parametersList, startEventParams, TALEND_ROLES_HANDLER_CLASS_NAME, TALEND_PRESENTAION_HANDLER_CLASS_NAME);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ":run: problems while registering the start process event", e);
		}
		
		if (_command == null) {
			// AUDIT UPDATE
			if (_auditAccessUtils != null) _auditAccessUtils.updateAudit(_auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "No command to be executed", null);
			logger.error("No command to be executed");
			return;
		}
		
		Map endEventParams = new HashMap();				
		endEventParams.put(EventsAccessUtils.EVENT_TYPE, EventsAccessUtils.DOCUMENT_EXECUTION_END);
		//endEventParams.put("biobj-path", params.get(TEMPLATE_PATH));
		endEventParams.put(EventsAccessUtils.BIOBJECT_ID, _parameters.get(EventsAccessUtils.BIOBJECT_ID));
		endEventParams.put(EventsAccessUtils.START_EVENT_ID, startEventId.toString());
		
		String endExecutionEventDescription = null;
		
    	try { 
			String line;
			Process p = Runtime.getRuntime().exec(_command, _envr, _executableJobDir);
			BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				logger.debug(line);
				//System.out.println(line);
			}
			// AUDIT UPDATE
			if (_auditAccessUtils != null) _auditAccessUtils.updateAudit(_auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_PERFORMED", null, null);
			endExecutionEventDescription = "${talend.execution.executionOk}<br/>";
			endEventParams.put("operation-result", "success");
			
			input.close();
		} catch (Exception e){
			logger.error("Error while executing command " + _command, e);
			endExecutionEventDescription = "${talend.execution.executionKo}<br/>";
			endEventParams.put("operation-result", "failure");
			// AUDIT UPDATE
			if (_auditAccessUtils != null) _auditAccessUtils.updateAudit(_auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "Error while executing process command", null);
		} finally {
			if (_filesToBeDeletedAfterJobExecution != null && _filesToBeDeletedAfterJobExecution.size() > 0) {
				Iterator it = _filesToBeDeletedAfterJobExecution.iterator();
				while (it.hasNext()) {
					File aFile = (File) it.next();
					if (aFile != null && aFile.exists()) FileUtils.deleteDirectory(aFile);
				}
			}
		}
		
		try {	
			eventsAccessUtils.fireEvent(user, endExecutionEventDescription + parametersList, endEventParams, TALEND_ROLES_HANDLER_CLASS_NAME, TALEND_PRESENTAION_HANDLER_CLASS_NAME);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ":run: problems while registering the end process event", e);
		}
		
		
	}
	
}
