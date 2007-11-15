package it.eng.spagobi.services.security.service;


import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.SecurityService;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import edu.yale.its.tp.cas.client.ProxyTicketValidator;

public class SecurityServiceImpl implements SecurityService {

    private ISecurityServiceSupplier supplier=null;
    private String userId="";
    
    public SecurityServiceImpl(){
	supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
    }
    
    private boolean validateTicket(String ticket) throws SecurityException {
	        try {
	            //String callingService = null;
	            ProxyTicketValidator pv = null;       
	            pv = new ProxyTicketValidator();
	            pv.setCasValidateUrl("https://localhost:8443/exo-cas-2/proxyValidate");
	            pv.setServiceTicket(ticket);
	            pv.setService("https://localhost:8443/SpagoBI/proxyReceptor");
	            pv.setRenew(false);
	            pv.validate();
	            if (pv.isAuthenticationSuccesful()) {
	        	userId = pv.getUser();
	        	/*
	                if (pv.getProxyList() != null && pv.getProxyList().size() > 0) {
	                    callingService = (String)pv.getProxyList().get(0);
	                }
	                */
	                return true;
	            }
	            else {
	                return false;
	            }
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	           throw new SecurityException();
	        }
}
	
    public SpagoBIUserProfile getUserProfile(String token) {
	/*
	try {
	    if (validateTicket(token)){
		return supplier.createUserProfile(userId);
	    }else{
		return null;
	    }
	} catch (SecurityException e) {
	    e.printStackTrace();
	    return null;
	}
	*/
	return supplier.createUserProfile(userId);	
    }

    public boolean isAuthorized(String token, String idFolder, String state) {
	
	/*
	try {
	    if (validateTicket(token)){
		return supplier.createUserProfile(userId);
	    }else{
		return null;
	    }
	} catch (SecurityException e) {
	    e.printStackTrace();
	    return null;
	}
	*/
	SpagoBIUserProfile profile= supplier.createUserProfile(userId);
	UserProfile userProfile=new UserProfile(profile);
	// TODO ... da finire !!!
	return ObjectsAccessVerifier.canExec(new Integer(idFolder), userProfile);
	
    }
    
    public boolean checkAuthorization(String token,String function) {
	
	try {
	    if (validateTicket(token)){
		return supplier.checkAuthorization(userId,function);
	    }else{
		return false;
	    }
	} catch (SecurityException e) {
	    e.printStackTrace();
	    return false;
	}
	
    }
    
    
	
}
