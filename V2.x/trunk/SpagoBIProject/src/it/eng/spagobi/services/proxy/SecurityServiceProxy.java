package it.eng.spagobi.services.proxy;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;
import it.eng.spagobi.services.security.stub.SecurityServiceServiceLocator;

import java.security.Principal;

import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

public class SecurityServiceProxy extends AbstractServiceProxy{

    static private Logger logger = Logger.getLogger(SecurityServiceProxy.class);
    

    public SecurityServiceProxy(String user,HttpSession session){
	super(user,session);
    }    
 
    private SecurityServiceProxy() {
	super();
    }     
    
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
    
    public IEngUserProfile getUserProfile() throws SecurityException{
	logger.debug("IN");
	try {
            SpagoBIUserProfile user= lookUp().getUserProfile(readTicket(),userId);
            return new UserProfile(user);
        } catch (Exception e) {
            logger.error("Error during service execution",e);
            throw new SecurityException();
        }finally{
            logger.debug("IN");
        }
    }
    /*
    public IEngUserProfile getUserProfile(Principal principal) throws SecurityException{
	logger.debug("IN");
	ISecurityServiceSupplier supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
        try {
            SpagoBIUserProfile user= supplier.createUserProfile(principal.getName());
            return new UserProfile(user);
        } catch (Exception e) {
            logger.error("Error during service execution",e);
            throw new SecurityException();
        }finally{
            logger.debug("IN");
        }
    }    
    */
    /**
     * Check if the user is authorized to access the folder
     * @param function
     * @return
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
     * Check if the user can execute the function ( user function )
     * @param function
     * @return
     */
    public boolean checkAuthorization(String function){
	
	return false;
    }
    
    /**
     * Check if the user can execute the function ( user function )
     * @param function
     * @return
     */    
    public boolean checkAuthorization(Principal principal,String function){
	
	return false;
    }    
}
