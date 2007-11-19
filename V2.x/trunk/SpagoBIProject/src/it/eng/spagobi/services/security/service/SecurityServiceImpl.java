package it.eng.spagobi.services.security.service;


import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.engines.drivers.jasperreport.JasperReportDriver;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.SecurityService;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import edu.yale.its.tp.cas.client.ProxyTicketValidator;

/**
 * This class create the user profile and implements the security check
 * @author Bernabei Angelo
 *
 */
public class SecurityServiceImpl implements SecurityService {
    
    static private Logger logger = Logger.getLogger(JasperReportDriver.class);
    private ISecurityServiceSupplier supplier=null;
    private String userId="";
    private String validateUrl =null;
    private String validateService =null;
    private boolean activeSso=false;
    
    public SecurityServiceImpl(){
	logger.debug("IN");
	supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
	ConfigSingleton config = ConfigSingleton.getInstance();
	        SourceBean validateSB = (SourceBean)config.getAttribute("SPAGOBI_SSO.VALIDATE-USER.URL");
	validateUrl = (String)validateSB.getCharacters();
	logger.debug("Read validateUrl="+validateUrl);
	        validateSB = (SourceBean)config.getAttribute("SPAGOBI_SSO.VALIDATE-USER.SERVICE");
	validateService = (String)validateSB.getCharacters();
	logger.debug("Read validateService="+validateService);
        validateSB = (SourceBean)config.getAttribute("SPAGOBI_SSO.ACTIVE");
	String active = (String)validateSB.getCharacters();
	if (active!=null && active.equals("true")) activeSso=true;
	logger.debug("Read activeSso="+activeSso);
    }
    
    /**
     * check the ticket used for verify the user authentication
     * @param ticket
     * @return
     * @throws SecurityException
     */
    private boolean validateTicket(String ticket) throws SecurityException {
		logger.debug("IN");
	
	        try {
	            //String callingService = null;
	            ProxyTicketValidator pv = null;       
	            pv = new ProxyTicketValidator();
	            pv.setCasValidateUrl(validateUrl);
	            pv.setServiceTicket(ticket);
	            pv.setService(validateService);
	            pv.setRenew(false);
	            pv.validate();
	            if (pv.isAuthenticationSuccesful()) {
	        	String tmpUserId = pv.getUser();
	        	if (!userId.equals(tmpUserId)){
	        	    return false;
	        	}
	                return true;
	            }
	            else {
	                return false;
	            }
	        }catch (Exception e) {
	            e.printStackTrace();
	           throw new SecurityException();
	        }finally{
		        logger.debug("OUT");
	        }
	        
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
			return null;
		    }
		} catch (SecurityException e) {
		    e.printStackTrace();
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
			return false;
		    }
		} catch (SecurityException e) {
		    e.printStackTrace();
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
		return false;
	    }
	} catch (SecurityException e) {
	    e.printStackTrace();
	    return false;
	}finally{
	        logger.debug("OUT");
	}
	
    }
    
    
	
}
