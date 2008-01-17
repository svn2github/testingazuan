package it.eng.spagobi.services.proxy;

import it.eng.spagobi.services.scheduler.stub.SchedulerService;
import it.eng.spagobi.services.scheduler.stub.SchedulerServiceServiceLocator;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

/**
 * 
 * Scheduler Service
 *
 */
public final class SchedulerServiceProxy extends AbstractServiceProxy{

    static private Logger logger = Logger.getLogger(SchedulerServiceProxy.class);

    /**
     * Use this in engine context only.
     * @param user user id
     * @param session HttpSession
     */
    public SchedulerServiceProxy(String user,HttpSession session) {
    	super(user, session);
    }

    private  SchedulerServiceProxy() {
    	super();
    }    

    
    private SchedulerService lookUp() throws SecurityException {
	try {
    	    SchedulerServiceServiceLocator locator = new SchedulerServiceServiceLocator();
    	    SchedulerService service = null;
	    if (serviceUrl!=null ){
		    service = locator.getSchedulerService(serviceUrl);		
	    }else {
		    service = locator.getSchedulerService();		
	    } 
	    return service;
	} catch (ServiceException e) {
	    logger.error("Error during service execution", e);
	    throw new SecurityException();
	}
    }   
    /**
     * Return all jobs
     * @return String
     */
    public String getJobList() {
    	logger.debug("IN");
    	try {
    	    return lookUp().getJobList(readTicket(),userId);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    /**
     * 
     * @param jobName job name
     * @param jobGroup job group
     * @return String
     */
    public String getJobSchedulationList(String jobName, String jobGroup) {
    	logger.debug("IN");
    	try {
    	    return lookUp().getJobSchedulationList(readTicket(),userId,jobName, jobGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    /**
     * 
     * @param triggerName String
     * @param triggerGroup String
     * @return String
     */
    public String deleteSchedulation(String triggerName, String triggerGroup) {
    	logger.debug("IN");
    	try {
    	    return lookUp().deleteSchedulation(readTicket(),userId,triggerName, triggerGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    /**
     * 
     * @param jobName String
     * @param jobGroupName String
     * @return String
     */
    public String deleteJob(String jobName, String jobGroupName) {
    	logger.debug("IN");
    	try {
    	    return lookUp().deleteJob(readTicket(),userId,jobName, jobGroupName);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    /**
     * 
     * @param xmlRequest  String
     * @return String
     */
    public String defineJob(String xmlRequest) {
    	logger.debug("IN");
    	try {
    	    return lookUp().defineJob(readTicket(),userId,xmlRequest);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    /**
     * 
     * @param jobName String
     * @param jobGroup String
     * @return String
     */
    public String getJobDefinition(String jobName, String jobGroup) {
    	logger.debug("IN");
    	try {
    	    return lookUp().getJobDefinition(readTicket(),userId,jobName, jobGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    /**
     * 
     * @param xmlRequest String
     * @return String
     */
    public String scheduleJob(String xmlRequest) {
    	logger.debug("IN");
    	try {
    	    return lookUp().scheduleJob(readTicket(),userId,xmlRequest);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    /**
     * 
     * @param triggerName String
     * @param triggerGroup String
     * @return String
     */
    public String getJobSchedulationDefinition(String triggerName, String triggerGroup) {
    	logger.debug("IN");
    	try {
    	    return lookUp().getJobSchedulationDefinition(readTicket(),userId,triggerName, triggerGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    /**
     * 
     * @param jobName String
     * @param jobGroup String
     * @return String
     */
    public String existJobDefinition(String jobName, String jobGroup) {
    	logger.debug("IN");
    	try {
    	    return lookUp().existJobDefinition(readTicket(),userId,jobName, jobGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }

}
