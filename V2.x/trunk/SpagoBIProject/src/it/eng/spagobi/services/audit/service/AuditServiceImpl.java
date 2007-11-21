package it.eng.spagobi.services.audit.service;

import it.eng.spagobi.monitoring.dao.AuditManager;
import it.eng.spagobi.services.common.AbstractServiceImpl;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import org.apache.log4j.Logger;

public class AuditServiceImpl extends AbstractServiceImpl{

    static private Logger logger = Logger.getLogger(AuditServiceImpl.class);
    
    public String log(String token,String user,String id,String start,String end,String state,String message,String errorCode){
	logger.debug("IN");
	if (activeSso) {
	    try {
		if (validateTicket(token)) {
		    return log( user, id, start, end, state, message, errorCode);
		} else {
		    logger.error("Token NOT VALID");
		    return null;
		}
	    } catch (SecurityException e) {
		logger.error("SecurityException", e);
		return null;
	    } finally {
		logger.debug("OUT");
	    }
	} else {
	    logger.debug("OUT");
	    return log( user, id, start, end, state, message, errorCode);
	}

    }
	
	private String log(String user,String id,String start,String end,String state,String message,String errorCode){
	        logger.debug("IN");
		// getting audit record id
		if (id == null) {
		    logger.warn("No operations will be performed");
		    return "";
		}
		logger.debug("Audit id = [" + id + "]");
		Integer auditId = new Integer(id);
		// getting execution start time
		Long startTime = null;
		
		if (start != null && !start.trim().equals("")) {
			try {
				startTime = new Long(start);
			} catch (NumberFormatException nfe) {
				logger.error("Execution start time = [" + start + "] not correct!", nfe);
			}
		}
		// getting execution end time
		Long endTime = null;

		if (end != null && !end.trim().equals("")) {
			try {
				endTime = new Long(end);
			} catch (NumberFormatException nfe) {
				logger.error("Execution end time = [" + end + "] not correct!", nfe);
			}
		}

		// saving modifications
		AuditManager auditManager = AuditManager.getInstance();
		auditManager.updateAudit(auditId, startTime, endTime, state, message, errorCode);
		return "";
	}    
}
