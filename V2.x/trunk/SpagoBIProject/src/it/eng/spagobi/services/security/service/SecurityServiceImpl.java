package it.eng.spagobi.services.security.service;


import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.services.common.AbstractServiceImpl;
import it.eng.spagobi.services.security.SecurityService;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import org.apache.log4j.Logger;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * This class create the user profile and implements the security check
 * @author Bernabei Angelo
 *
 */
public class SecurityServiceImpl extends AbstractServiceImpl implements SecurityService {
    
    static private Logger logger = Logger.getLogger(SecurityServiceImpl.class);
    private ISecurityServiceSupplier supplier=null;

    
    public SecurityServiceImpl(){
	super();
	logger.debug("IN");
	supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
    }
    

	
/**
 * User profile creation 
 */
public SpagoBIUserProfile getUserProfile(String token,String userId) {
        logger.debug("IN");
        Monitor monitor =MonitorFactory.start("spagobi.service.security.getUserProfile");
	try {
	    validateTicket(token, userId);
	    return supplier.createUserProfile(userId);
	} catch (SecurityException e) {
	    logger.error("SecurityException", e);
	    return null;
	} finally {
	    monitor.stop();
	    logger.debug("OUT");
	}	

	
    }
/**
 * check if user can access to the folder "idFolder"
 */
    public boolean isAuthorized(String token,String userId, String idFolder, String state) {
        logger.debug("IN");
        Monitor monitor =MonitorFactory.start("spagobi.service.security.isAuthorized");
	try {
	        validateTicket(token, userId);
		SpagoBIUserProfile profile= supplier.createUserProfile(userId);
		UserProfile userProfile=new UserProfile(profile);			
		return ObjectsAccessVerifier.canExec(new Integer(idFolder), userProfile);
	} catch (SecurityException e) {
	    logger.error("SecurityException", e);
	    return false;
	} finally {
	    monitor.stop();	    
	    logger.debug("OUT");
	}	


    }
    
    /**
     * check if the user can execute the function
     */
    public boolean checkAuthorization(String token,String userId,String function) {
        logger.debug("IN");
        Monitor monitor =MonitorFactory.start("spagobi.service.security.checkAuthorization");	
	try {
	    validateTicket(token,userId);
            return supplier.checkAuthorization(userId,function);
	} catch (SecurityException e) {
	    logger.error("SecurityException",e);
	    return false;
	}finally{
	    monitor.stop();	    
	    logger.debug("OUT");
	}
	
    }
    
    
	
}
