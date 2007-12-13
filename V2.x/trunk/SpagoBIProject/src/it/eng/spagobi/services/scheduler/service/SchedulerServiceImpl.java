package it.eng.spagobi.services.scheduler.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.services.common.AbstractServiceImpl;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import org.safehaus.uuid.UUIDGenerator;


public class SchedulerServiceImpl extends AbstractServiceImpl{

    static private Logger logger = Logger.getLogger(SchedulerServiceImpl.class);
    private SchedulerServiceSupplier supplier=new SchedulerServiceSupplier();
    
	public String getJobList(String token,String user){
		logger.debug("IN");
		if (activeSso) {
		    try {
			if (validateTicket(token,user)) {
			    return supplier.getJobList();
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
		    return supplier.getJobList( );
		}	    
	}
	
	public String getJobSchedulationList(String token,String user,String jobName, String jobGroup){
		logger.debug("IN");
		if (activeSso) {
		    try {
			if (validateTicket(token,user)) {
			    return supplier.getJobSchedulationList(jobName,jobGroup);
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
		    return supplier.getJobSchedulationList(jobName,jobGroup );
		}	    
	}
	
	public String deleteSchedulation(String token,String user,String triggerName, String triggerGroup){
		logger.debug("IN");
		if (activeSso) {
		    try {
			if (validateTicket(token,user)) {
			    return supplier.deleteSchedulation(triggerName,triggerGroup);
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
		    return supplier.deleteSchedulation(triggerName,triggerGroup);
		}	    
	}
	
	public String deleteJob(String token,String user,String jobName, String jobGroupName){
		logger.debug("IN");
		if (activeSso) {
		    try {
			if (validateTicket(token,user)) {
			    return supplier.deleteJob(jobName,jobGroupName);
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
		    return supplier.deleteJob(jobName,jobGroupName);
		}	    
	}
	
	public String defineJob(String token,String user,String xmlRequest){
		logger.debug("IN");
		if (activeSso) {
		    try {
			if (validateTicket(token,user)) {
			    return supplier.defineJob(xmlRequest);
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
		    return supplier.defineJob(xmlRequest);
		}	    
	}
	
	public String getJobDefinition(String token,String user,String jobName, String jobGroup){
		logger.debug("IN");
		if (activeSso) {
		    try {
			if (validateTicket(token,user)) {
			    return supplier.getJobDefinition(jobName,jobGroup);
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
		    return supplier.getJobDefinition(jobName,jobGroup);
		}	    
	}
	
	public String scheduleJob(String token,String user,String xmlRequest){
		logger.debug("IN");
		if (activeSso) {
		    try {
			if (validateTicket(token,user)) {
			    return supplier.scheduleJob(xmlRequest);
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
		    return supplier.scheduleJob(xmlRequest);
		}	    
	}
	
	public String getJobSchedulationDefinition(String token,String user,String triggerName, String triggerGroup){
		logger.debug("IN");
		if (activeSso) {
		    try {
			if (validateTicket(token,user)) {
			    return supplier.getJobSchedulationDefinition(triggerName,triggerGroup);
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
		    return supplier.getJobSchedulationDefinition(triggerName,triggerGroup);
		}	    
	}
	
	public String existJobDefinition(String token,String user,String jobName, String jobGroup){
		logger.debug("IN");
		if (activeSso) {
		    try {
			if (validateTicket(token,user)) {
			    return supplier.existJobDefinition(jobName,jobGroup);
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
		    return supplier.existJobDefinition(jobName,jobGroup);
		}    
	}
	
}
