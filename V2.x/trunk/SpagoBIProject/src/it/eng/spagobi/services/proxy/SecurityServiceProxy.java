package it.eng.spagobi.services.proxy;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.stub.SecurityServiceServiceLocator;

import java.security.Principal;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

/**
 * 
 * Security Service Proxy. 
 * Use in engine component only
 *
 */
public final class SecurityServiceProxy extends AbstractServiceProxy{

    static private Logger logger = Logger.getLogger(SecurityServiceProxy.class);
    
/**
 * Use this constructor.
 * @param user user ID
 * @param session HttpSession
 */
    public SecurityServiceProxy(String user,HttpSession session){
	super(user,session);
    }    
 
    /**
     * Don't use it.
     */
    private SecurityServiceProxy() {
	super();
    }     
    /**
     * 
     * @return Object used
     * @throws SecurityException catch this if exist error
     */
    private it.eng.spagobi.services.security.stub.SecurityService lookUp() throws SecurityException {
	try {
	    SecurityServiceServiceLocator locator = new SecurityServiceServiceLocator();
	    it.eng.spagobi.services.security.stub.SecurityService service = null;
	    if (serviceUrl != null) {
		service = locator.getSecurityService(serviceUrl);
	    } else {
		service = locator.getSecurityService();
	    }
	    return service;
	} catch (ServiceException e) {
	    logger.error("Error during service execution", e);
	    throw new SecurityException();
	}
    }
    /**
     * 
     * @return IEngUserProfile with user profile
     * @throws SecurityException if the process has generated an error
     */
    public IEngUserProfile getUserProfile() throws SecurityException{
	logger.debug("IN");
	try {
            SpagoBIUserProfile user= lookUp().getUserProfile(readTicket(),userId);
            return new UserProfile(user);
        } catch (Exception e) {
            logger.error("Error during service execution",e);
            throw new SecurityException();
        }finally{
            logger.debug("OUT");
        }
    }

    /**
     * Check if the user is authorized to access the folder.
     * @param folderId folder id
     * @param mode mode
     * @return true/false
     */    
    public boolean isAuthorized(String folderId,String mode) {
	logger.debug("IN");
        try {
            return lookUp().isAuthorized(readTicket(),userId,folderId, mode);
        } catch (Exception e) {
            logger.error("Error during service execution",e);
        }finally{
            logger.debug("IN");
        }
	return false;
    } 
    

    /**
     * Check if the user can execute the function ( user function ).
     * @param function function id
     * @return true/false
     */
    public boolean checkAuthorization(String function){
	
	return false;
    }
    
    /**
     * Check if the user can execute the function ( user function ).
     * @param function function
     * @param principal user principal
     * @return true / false
     */    
    public boolean checkAuthorization(Principal principal,String function){
	
	return false;
    }    
}
