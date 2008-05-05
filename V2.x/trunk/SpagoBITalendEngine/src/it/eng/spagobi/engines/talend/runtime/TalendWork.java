/**

 LICENSE: see COPYING file
  
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
    private HttpSession _session = null;

    private boolean completeWithoutError = false;

    /**
     * Checks if is complete without error.
     * 
     * @return true, if is complete without error
     */
    public boolean isCompleteWithoutError() {
	return completeWithoutError;
    }

    /**
     * Instantiates a new talend work.
     * 
     * @param command the command
     * @param envr the envr
     * @param executableJobDir the executable job dir
     * @param filesToBeDeletedAfterJobExecution the files to be deleted after job execution
     * @param auditAccessUtils the audit access utils
     * @param auditId the audit id
     * @param parameters the parameters
     * @param session the session
     */
    public TalendWork(String command, String[] envr, File executableJobDir, List filesToBeDeletedAfterJobExecution,
	    AuditAccessUtils auditAccessUtils, String auditId, Map parameters, HttpSession session) {
	this._command = command;
	this._executableJobDir = executableJobDir;
	this._envr = envr;
	this._filesToBeDeletedAfterJobExecution = filesToBeDeletedAfterJobExecution;
	this._auditAccessUtils = auditAccessUtils;
	this._auditId = auditId;
	this._parameters = parameters;
	this._session = session;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
	logger.debug("IN");

	String userId = (String) _parameters.get("userId");

	// registering the start execution event
	String startExecutionEventDescription = "${talend.execution.started}<br/>";

	String parametersList = "${talend.execution.parameters}<br/><ul>";
	Set paramKeys = _parameters.keySet();
	Iterator paramKeysIt = paramKeys.iterator();
	logger.debug("--- Request Parameters---");
	while (paramKeysIt.hasNext()) {
	    String key = (String) paramKeysIt.next();
	    
	    if (!key.equalsIgnoreCase("template") && !key.equalsIgnoreCase("biobjectId")
		    && !key.equalsIgnoreCase("cr_manager_url") && !key.equalsIgnoreCase("events_manager_url")
		    && !key.equalsIgnoreCase("user") && !key.equalsIgnoreCase("SPAGOBI_AUDIT_SERVLET")
		    && !key.equalsIgnoreCase("spagobicontext") && !key.equalsIgnoreCase("SPAGOBI_AUDIT_ID")
		    && !key.equalsIgnoreCase("username")) {
		Object valueObj = _parameters.get(key);
		logger.debug(key+"/"+(String)valueObj);
		parametersList += "<li>" + key + " = " + (valueObj != null ? valueObj.toString() : "") + "</li>";
	    }
	}
	logger.debug("--- Request Parameters---");
	parametersList += "</ul>";

	Map startEventParams = new HashMap();
	startEventParams.put(EVENT_TYPE, DOCUMENT_EXECUTION_START);
	// startEventParams.put("biobj-path", params.get(TEMPLATE_PATH));
	startEventParams.put(BIOBJECT_ID, _parameters.get("document"));

	Integer startEventId = null;
	EventServiceProxy eventServiceProxy = new EventServiceProxy(userId, _session);

	try {

	    String startEventParamsStr = getParamsStr(startEventParams);

	    eventServiceProxy.fireEvent(startExecutionEventDescription + parametersList, startEventParamsStr,
		    TALEND_ROLES_HANDLER_CLASS_NAME, TALEND_PRESENTAION_HANDLER_CLASS_NAME);
	    logger.debug("Start Fire Event");

	} catch (Exception e) {
	    logger.error("problems while registering the start process event", e);
	}

	if (_command == null) {
	    logger.error("No command to be executed");
	    return;
	}

	Map endEventParams = new HashMap();
	endEventParams.put(EVENT_TYPE, DOCUMENT_EXECUTION_END);
	// endEventParams.put("biobj-path", params.get(TEMPLATE_PATH));
	endEventParams.put(BIOBJECT_ID, _parameters.get(BIOBJECT_ID));
	if (startEventId != null) {
	    endEventParams.put(START_EVENT_ID, startEventId.toString());
	}

	String endExecutionEventDescription = null;
	BufferedReader input = null;
	try {
	    logger.debug("Java Command:"+_command);
	    logger.debug("Executable Job Dir:"+_executableJobDir);
	    Runtime.getRuntime().exec(_command, _envr, _executableJobDir);

	    endExecutionEventDescription = "${talend.execution.executionOk}<br/>";
	    endEventParams.put("operation-result", "success");

	} catch (Throwable e) {
	    logger.error("Error while executing command " + _command, e);
	    endExecutionEventDescription = "${talend.execution.executionKo}<br/>";
	    endEventParams.put("operation-result", "failure");
	} finally {
	    if (_filesToBeDeletedAfterJobExecution != null && _filesToBeDeletedAfterJobExecution.size() > 0) {
		Iterator it = _filesToBeDeletedAfterJobExecution.iterator();
		while (it.hasNext()) {
		    File aFile = (File) it.next();
		    if (aFile != null && aFile.exists())
			FileUtils.deleteDirectory(aFile);
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

	    String endEventParamsStr = getParamsStr(endEventParams);

	    eventServiceProxy.fireEvent(endExecutionEventDescription + parametersList, endEventParamsStr,
		    TALEND_ROLES_HANDLER_CLASS_NAME, TALEND_PRESENTAION_HANDLER_CLASS_NAME);
	    logger.debug("End fire event");

	} catch (Exception e) {
	    logger.error("problems while registering the end process event", e);
	}
	completeWithoutError = true;

    }

    private String getParamsStr(Map params) {
	logger.debug("IN");
	StringBuffer buffer = new StringBuffer();
	Iterator it = params.keySet().iterator();
	boolean isFirstParameter = true;
	while (it.hasNext()) {
	    String pname = (String) it.next();
	    String pvalue = (String) params.get(pname);
	    if (!isFirstParameter)
		buffer.append("&");
	    else
		isFirstParameter = false;
	    buffer.append(pname + "=" + pvalue);
	}
	logger.debug("parameters: " + buffer.toString());
	logger.debug("OUT");
	return buffer.toString();
    }

    /* (non-Javadoc)
     * @see commonj.work.Work#isDaemon()
     */
    public boolean isDaemon() {
	return false;
    }

    /* (non-Javadoc)
     * @see commonj.work.Work#release()
     */
    public void release() {
	logger.debug("IN");

    }

}
