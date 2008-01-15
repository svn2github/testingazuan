package it.eng.spagobi.services.proxy;

import it.eng.spagobi.services.audit.stub.AuditServiceServiceLocator;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

/**
 * 
 * Proxy of Autit Service
 *
 */
public abstract class AuditServiceProxy extends AbstractServiceProxy{


    static private Logger logger = Logger.getLogger(AuditServiceProxy.class);

    /**
     * 
     * @param user
     * @param session
     */
    public AuditServiceProxy(String user,HttpSession session) {
	super(user, session);
    }
    private AuditServiceProxy() {
	super();
    }    
    
    private it.eng.spagobi.services.audit.stub.AuditService lookUp() throws SecurityException {
	try {
	    AuditServiceServiceLocator locator = new AuditServiceServiceLocator();
	    it.eng.spagobi.services.audit.stub.AuditService service=null;
	    if (serviceUrl!=null ){
		    service = locator.getAuditService(serviceUrl);		
	    }else {
		    service = locator.getAuditService();		
	    }
	    return service;
	} catch (ServiceException e) {
	    logger.error("Error during service execution", e);
	    throw new SecurityException();
	}
    }
    /**
     * 
     * @param id
     * @param start
     * @param end
     * @param state
     * @param message
     * @param errorCode
     * @return
     */
    public String log(String id,String start,String end,String state,String message,String errorCode){
	logger.debug("IN");
	try {
	    return lookUp().log( readTicket(), userId, id, start, end, state, message, errorCode);
	} catch (Exception e) {
	    logger.error("Error during service execution",e);

	}finally{
	    logger.debug("IN");
	}
	return null;
    }
}
