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

    /**
     * 
     * @param ticket String
     * @param userId String
     * @param validateUrl String
     * @param validateService String
     * @throws SecurityException String
     */
    void validateTicket(String ticket, String userId,String validateUrl,String validateService) throws SecurityException;
    /**
     * 
     * @param session Http Session
     * @param filterReceipt String
     * @return String
     * @throws IOException
     */
    String readTicket(HttpSession session,String filterReceipt) throws IOException;
    /**
     * 
     * @param session Http Session
     * @return
     */
    String readUserId(HttpSession session);
}
