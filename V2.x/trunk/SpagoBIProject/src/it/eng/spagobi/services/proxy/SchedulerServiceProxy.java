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
    	    return service.getJobList(ticket);
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
    	    return service.getJobSchedulationList(ticket,jobName, jobGroup);
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
    	    return service.deleteSchedulation(ticket,triggerName, triggerGroup);
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
    	    return service.deleteJob(ticket,jobName, jobGroupName);
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
    	    return service.defineJob(ticket,xmlRequest);
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
    	    return service.getJobDefinition(ticket,jobName, jobGroup);
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
    	    return service.scheduleJob(ticket,xmlRequest);
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
    	    return service.getJobSchedulationDefinition(ticket,triggerName, triggerGroup);
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
    	    return service.existJobDefinition(ticket,jobName, jobGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }

}
