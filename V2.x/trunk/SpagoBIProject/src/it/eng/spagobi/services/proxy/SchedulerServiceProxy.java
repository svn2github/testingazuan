package it.eng.spagobi.services.proxy;

import it.eng.spagobi.services.scheduler.stub.SchedulerService;
import it.eng.spagobi.services.scheduler.stub.SchedulerServiceServiceLocator;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

public class SchedulerServiceProxy extends AbstractServiceProxy{

    static private Logger logger = Logger.getLogger(SchedulerServiceProxy.class);

    public SchedulerServiceProxy(HttpSession session) {
    	super( session);
    }
    
    public SchedulerServiceProxy() {
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
    public String getJobList() {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    return lookUp().getJobList(ticket);
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
    	    return lookUp().getJobSchedulationList(ticket,jobName, jobGroup);
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
    	    return lookUp().deleteSchedulation(ticket,triggerName, triggerGroup);
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
    	    return lookUp().deleteJob(ticket,jobName, jobGroupName);
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
    	    return lookUp().defineJob(ticket,xmlRequest);
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
    	    return lookUp().getJobDefinition(ticket,jobName, jobGroup);
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
    	    return lookUp().scheduleJob(ticket,xmlRequest);
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
    	    return lookUp().getJobSchedulationDefinition(ticket,triggerName, triggerGroup);
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
    	    return lookUp().existJobDefinition(ticket,jobName, jobGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }

}
