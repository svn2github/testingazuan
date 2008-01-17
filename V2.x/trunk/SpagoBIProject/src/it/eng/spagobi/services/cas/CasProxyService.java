package it.eng.spagobi.services.cas;

import it.eng.spagobi.services.common.IProxyService;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.yale.its.tp.cas.client.CASReceipt;
import edu.yale.its.tp.cas.client.ProxyTicketValidator;
import edu.yale.its.tp.cas.client.filter.CASFilter;
import edu.yale.its.tp.cas.proxy.ProxyTicketReceptor;

/**
 * This class contain the specific code of CAS 
 */
public class CasProxyService implements IProxyService {

    static private Logger logger = Logger.getLogger(CasProxyService.class);
    
    /**
     *  @param session HttpSession
     *  @return String
     */
    public String readUserId(HttpSession session){
	String user=(String)session.getAttribute(CASFilter.CAS_FILTER_USER);
	logger.debug("CAS user in Session:"+user);
	return user;
    }
    /**
     * Get a new ticket
     *  @param session HttpSession
     *  @param filterReceipt String
     *  @return String
     */
    public String readTicket(HttpSession session,String filterReceipt) throws IOException{
	    logger.debug("IN");
	    CASReceipt cr = (CASReceipt) session.getAttribute(CASFilter.CAS_FILTER_RECEIPT);
	    logger.debug("Read cr=" + cr);
	    return ProxyTicketReceptor.getProxyTicket(cr.getPgtIou(), filterReceipt);
    }

    /**
     *  This method verify the ticket
     *  @param ticket String, ticket to validate
     *  @param userId String, user id
     *  @param validateUrl String 
     *  @param validateService String 
     *  @return String
     */
    public void validateTicket(String ticket, String userId,String validateUrl,String validateService)throws SecurityException {
	logger.debug("IN");
	try {
	    ProxyTicketValidator pv = null;
	    pv = new ProxyTicketValidator();
	    pv.setCasValidateUrl(validateUrl);
	    pv.setServiceTicket(ticket);
	    pv.setService(validateService);
	    pv.setRenew(false);
	    pv.validate();
	    if (pv.isAuthenticationSuccesful()) {
		String tmpUserId = pv.getUser();
		logger.debug("CAS User:" + tmpUserId);
		if ( !userId.equals(tmpUserId)) {
		    logger.warn("Proxy and application users are not the same !!!!! " + userId + "-"
			    + tmpUserId);
		    throw new SecurityException();
		}
	    } else {
		logger.error("Token NOT VALID");
		throw new SecurityException();
	    }
	} catch (Exception e) {
	    logger.error("Exception", e);
	    throw new SecurityException();
	} finally {
	    logger.debug("OUT");
	}

    }

}
