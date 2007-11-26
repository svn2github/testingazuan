package it.eng.spagobi.services.proxy;

import it.eng.spagobi.services.scheduler.stub.SchedulerService;
import it.eng.spagobi.services.scheduler.stub.SchedulerServiceServiceLocator;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class SchedulerServiceProxy extends AbstractServiceProxy{

    static private Logger logger = Logger.getLogger(SchedulerServiceProxy.class);

    public SchedulerServiceProxy(HttpSession session) {
    	super( session);
    }
    
    public SchedulerServiceProxy() {
    	super();
    }    

    
    public String getJobList() {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    SchedulerServiceServiceLocator locator = new SchedulerServiceServiceLocator();
    	    SchedulerService service = locator.getSchedulerService();
    	    return service.getJobList();
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    public String getJobSchedulationList(String jobName, String jobGroup) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    SchedulerServiceServiceLocator locator = new SchedulerServiceServiceLocator();
    	    SchedulerService service = locator.getSchedulerService();
    	    return service.getJobSchedulationList(jobName, jobGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    
    public String deleteSchedulation(String triggerName, String triggerGroup) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    SchedulerServiceServiceLocator locator = new SchedulerServiceServiceLocator();
    	    SchedulerService service = locator.getSchedulerService();
    	    return service.deleteSchedulation(triggerName, triggerGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    
    public String deleteJob(String jobName, String jobGroupName) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    SchedulerServiceServiceLocator locator = new SchedulerServiceServiceLocator();
    	    SchedulerService service = locator.getSchedulerService();
    	    return service.deleteJob(jobName, jobGroupName);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    

    public String defineJob(String xmlRequest) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    SchedulerServiceServiceLocator locator = new SchedulerServiceServiceLocator();
    	    SchedulerService service = locator.getSchedulerService();
    	    return service.defineJob(xmlRequest);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    
    public String getJobDefinition(String jobName, String jobGroup) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    SchedulerServiceServiceLocator locator = new SchedulerServiceServiceLocator();
    	    SchedulerService service = locator.getSchedulerService();
    	    return service.getJobDefinition(jobName, jobGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    
    public String scheduleJob(String xmlRequest) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    SchedulerServiceServiceLocator locator = new SchedulerServiceServiceLocator();
    	    SchedulerService service = locator.getSchedulerService();
    	    return service.scheduleJob(xmlRequest);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    
    public String getJobSchedulationDefinition(String triggerName, String triggerGroup) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    SchedulerServiceServiceLocator locator = new SchedulerServiceServiceLocator();
    	    SchedulerService service = locator.getSchedulerService();
    	    return service.getJobSchedulationDefinition(triggerName, triggerGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    
    public String existJobDefinition(String jobName, String jobGroup) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    SchedulerServiceServiceLocator locator = new SchedulerServiceServiceLocator();
    	    SchedulerService service = locator.getSchedulerService();
    	    return service.existJobDefinition(jobName, jobGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }

}
