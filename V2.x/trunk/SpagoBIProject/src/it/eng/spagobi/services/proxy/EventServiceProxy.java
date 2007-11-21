package it.eng.spagobi.services.proxy;

import it.eng.spagobi.services.event.stub.EventServiceServiceLocator;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class EventServiceProxy extends AbstractServiceProxy{


    static private Logger logger = Logger.getLogger(EventServiceProxy.class);


    public EventServiceProxy(HttpSession session) {
	super( session);
    }
    public EventServiceProxy() {
	super();
    }    

    public String fireEvent(String user,String description,String parameters,String rolesHandler,String presentationHandler){
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (ssoIsActive){
		ticket=readTicket();
	    }
	    EventServiceServiceLocator locator = new EventServiceServiceLocator();
	    it.eng.spagobi.services.event.stub.EventService service = locator
		    .getEventService();
	    return service.fireEvent( ticket, user, description, parameters, rolesHandler, presentationHandler);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;	
    }

}
