package it.eng.spagobi.services.common;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;

/**
 * 
 * Interface for read and validate a proxy ticket
 *
 */
public interface IProxyService {

    void validateTicket(String ticket, String userId,String validateUrl,String validateService) throws SecurityException;
    
    String readTicket(HttpSession session,String filterReceipt) throws IOException;
    
    String readUserId(HttpSession session);
}
