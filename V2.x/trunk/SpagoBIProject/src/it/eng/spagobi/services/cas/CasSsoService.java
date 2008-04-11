package it.eng.spagobi.services.cas;

import it.eng.spagobi.services.common.SsoServiceInterface;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import java.io.IOException;

import javax.portlet.PortletSession;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.yale.its.tp.cas.client.CASReceipt;
import edu.yale.its.tp.cas.client.ProxyTicketValidator;
import edu.yale.its.tp.cas.client.filter.CASFilter;
import edu.yale.its.tp.cas.proxy.ProxyTicketReceptor;

/**
 * This class contain the specific code of CAS 
 */
public class CasSsoService implements SsoServiceInterface {

    static private Logger logger = Logger.getLogger(CasSsoService.class);
    
    /**
     *  @param session HttpSession
     *  @return String
     */
    public String readUserId(HttpSession session){
	String user=(String)session.getAttribute(CASFilter.CAS_FILTER_USER);
	logger.debug("CAS user in HttpSession:"+user);
	return user;
    }
    
    /**
     *  @param session PortletSession
     *  @return String
     */
    public String readUserId(PortletSession session){
	String user=(String)session.getAttribute(CASFilter.CAS_FILTER_USER);
	logger.debug("CAS user in PortletSession:"+user);
	return user;
    }
    
    /**
     * Get a new ticket
     *  @param session HttpSession
     *  @param filterReceipt String
     *  @return String
     */
    public String readTicket(HttpSession session,String filterReceipt) throws IOException{
	    logger.debug("IN.filterReceipt="+filterReceipt);
	    CASReceipt cr = (CASReceipt) session.getAttribute(CASFilter.CAS_FILTER_RECEIPT);
	    logger.debug("Read cr=" + cr);
	    if (cr==null) logger.warn("CASReceipt in session is NULL");
	    String ticket=ProxyTicketReceptor.getProxyTicket(cr.getPgtIou(), filterReceipt);
	    logger.debug("OUT.ticket="+ticket);
	    return ticket;
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
		if (  userId==null || !userId.equals(tmpUserId)) {
		    logger.warn("Proxy and application users are not the same !!!!! " + userId + "-"
			    + tmpUserId);
		    throw new SecurityException();
		}
	    } else {
		logger.error("Token NOT VALID");
		throw new SecurityException();
	    }
	} catch (Throwable e) {
	    logger.error("Exception", e);
	    throw new SecurityException();
	} finally {
	    logger.debug("OUT");
	}

    }

}
