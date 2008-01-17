package it.eng.spagobi.services.proxy;

import it.eng.spagobi.services.event.stub.EventServiceServiceLocator;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

/**
 * 
 * proxy of Event service
 *
 */
public final class EventServiceProxy extends AbstractServiceProxy{


    static private Logger logger = Logger.getLogger(EventServiceProxy.class);

    /**
     * use it only in engine context
     * @param user user Id
     * @param session HttpSession
     */
    public EventServiceProxy(String user,HttpSession session) {
	super( user,session);
    }
    
    private EventServiceProxy() {
	super();
    }    

    private it.eng.spagobi.services.event.stub.EventService lookUp() throws SecurityException {
	try {
	    EventServiceServiceLocator locator = new EventServiceServiceLocator();
	    it.eng.spagobi.services.event.stub.EventService service = null;
	    if (serviceUrl!=null ){
		    service = locator.getEventService(serviceUrl);		
	    }else {
		    service = locator.getEventService();		
	    }
	    return service;
	} catch (ServiceException e) {
	    logger.error("Error during service execution", e);
	    throw new SecurityException();
	}
    }
    /**
     * 
     * @param description String
     * @param parameters String
     * @param rolesHandler String
     * @param presentationHandler String
     * @return String
     */
    public String fireEvent(String description,String parameters,String rolesHandler,String presentationHandler){
	logger.debug("IN");
	try {
	    return lookUp().fireEvent( readTicket(), userId, description, parameters, rolesHandler, presentationHandler);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;	
    }

}
