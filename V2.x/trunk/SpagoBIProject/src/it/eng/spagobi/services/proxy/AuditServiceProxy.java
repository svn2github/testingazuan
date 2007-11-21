package it.eng.spagobi.services.proxy;

import it.eng.spagobi.services.audit.stub.AuditServiceServiceLocator;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class AuditServiceProxy extends AbstractServiceProxy{


    static private Logger logger = Logger.getLogger(AuditServiceProxy.class);


    public AuditServiceProxy(HttpSession session) {
	super( session);
    }
    public AuditServiceProxy() {
	super();
    }    
    
    public String log(String user,String id,String start,String end,String state,String message,String errorCode){
	logger.debug("IN");
	try {
	    String ticket = "";
	    if (ssoIsActive){
		ticket=readTicket();
	    }
	    AuditServiceServiceLocator locator = new AuditServiceServiceLocator();
	    it.eng.spagobi.services.audit.stub.AuditService service = locator
		    .getAuditService();
	    return service.log( ticket, user, id, start, end, state, message, errorCode);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
}
