package it.eng.spagobi.services.security.service;


import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.services.common.AbstractServiceImpl;
import it.eng.spagobi.services.security.SecurityService;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import org.apache.log4j.Logger;

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
	this.userId=userId;
	if (activeSso){
		try {
		    if (validateTicket(token)){
			return supplier.createUserProfile(userId);
		    }else{
			logger.error("Token NOT VALID");
			return null;
		    }
		} catch (SecurityException e) {
		    logger.error("SecurityException",e);
		    return null;
		}finally{
		    logger.debug("OUT");
		}
	}else{
	        logger.debug("OUT");
		return supplier.createUserProfile(userId);	    
	}
	
    }
/**
 * check if user can access to the folder "idFolder"
 */
    public boolean isAuthorized(String token,String userId, String idFolder, String state) {
        logger.debug("IN");
	this.userId=userId;	
	if (activeSso){
		try {
		    if (validateTicket(token)){
			SpagoBIUserProfile profile= supplier.createUserProfile(userId);
			UserProfile userProfile=new UserProfile(profile);			
			return ObjectsAccessVerifier.canExec(new Integer(idFolder), userProfile);
		    }else{
			logger.error("Token NOT VALID");
			return false;
		    }
		} catch (SecurityException e) {
		    logger.error("SecurityException",e);
		    return false;
		}finally{
		    logger.debug("OUT"); 
		}
	}else {
		SpagoBIUserProfile profile= supplier.createUserProfile(userId);
		UserProfile userProfile=new UserProfile(profile);
	        logger.debug("OUT");
		return ObjectsAccessVerifier.canExec(new Integer(idFolder), userProfile);	    
	}

    }
    
    /**
     * check if the user can execute the function
     */
    public boolean checkAuthorization(String token,String userId,String function) {
        logger.debug("IN");
	this.userId=userId;	
	try {
	    if (validateTicket(token)){
		return supplier.checkAuthorization(userId,function);
	    }else{
		logger.error("Token NOT VALID");
		return false;
	    }
	} catch (SecurityException e) {
	    logger.error("SecurityException",e);
	    return false;
	}finally{
	        logger.debug("OUT");
	}
	
    }
    
    
	
}
