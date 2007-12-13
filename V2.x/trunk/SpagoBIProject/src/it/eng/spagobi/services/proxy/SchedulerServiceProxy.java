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
    public String getJobList(String user) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    return lookUp().getJobList(ticket,user);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    public String getJobSchedulationList(String user,String jobName, String jobGroup) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    return lookUp().getJobSchedulationList(ticket,user,jobName, jobGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    
    public String deleteSchedulation(String user,String triggerName, String triggerGroup) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    return lookUp().deleteSchedulation(ticket,user,triggerName, triggerGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    
    public String deleteJob(String user,String jobName, String jobGroupName) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    return lookUp().deleteJob(ticket,user,jobName, jobGroupName);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    

    public String defineJob(String user,String xmlRequest) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    return lookUp().defineJob(ticket,user,xmlRequest);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    
    public String getJobDefinition(String user,String jobName, String jobGroup) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    return lookUp().getJobDefinition(ticket,user,jobName, jobGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    
    public String scheduleJob(String user,String xmlRequest) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    return lookUp().scheduleJob(ticket,user,xmlRequest);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    
    public String getJobSchedulationDefinition(String user,String triggerName, String triggerGroup) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    return lookUp().getJobSchedulationDefinition(ticket,user,triggerName, triggerGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }
    
    
    public String existJobDefinition(String user,String jobName, String jobGroup) {
    	logger.debug("IN");
    	try {
    	    String ticket = "";
    	    if (ssoIsActive){
    	    	ticket=readTicket();
    	    }
    	    return lookUp().existJobDefinition(ticket,user,jobName, jobGroup);
    	} catch (Exception e) {
    	    logger.error("Error during service execution",e);
    	}finally{
    	    logger.debug("OUT");
    	}
    	return null;	
    }

}
