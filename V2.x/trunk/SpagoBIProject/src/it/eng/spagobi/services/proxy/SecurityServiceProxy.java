package it.eng.spagobi.services.proxy;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpSession;



import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;
import it.eng.spagobi.services.security.stub.SecurityServiceServiceLocator;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import edu.yale.its.tp.cas.client.CASReceipt;
import edu.yale.its.tp.cas.client.filter.CASFilter;
import edu.yale.its.tp.cas.proxy.ProxyTicketReceptor;

public class SecurityServiceProxy {

    private HttpSession session=null;
    
    private String readTicket() throws IOException{
	CASReceipt cr = (CASReceipt)session.getAttribute(CASFilter.CAS_FILTER_RECEIPT);
        return ProxyTicketReceptor.getProxyTicket(cr.getPgtIou(), "https://localhost:8443/SpagoBI/proxyReceptor");
        
    }
    
    public SecurityServiceProxy(){
	
    }
    public SecurityServiceProxy(HttpSession session){
	this.session=session;
    }    
    
    
    public IEngUserProfile getUserProfile(String userId) throws SecurityException{
	try {
            //String ticket=readTicket();
	    String ticket="";
            SecurityServiceServiceLocator locator=new SecurityServiceServiceLocator();
            it.eng.spagobi.services.security.stub.SecurityService servizio= locator.getSecurityService();
            SpagoBIUserProfile user= servizio.getUserProfile(ticket,userId);
            return new UserProfile(user);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new SecurityException();
        }
    }
    
    public IEngUserProfile getUserProfile(Principal principal) throws SecurityException{
	ISecurityServiceSupplier supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
        try {
            SpagoBIUserProfile user= supplier.createUserProfile(principal.getName());
            return new UserProfile(user);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new SecurityException();
        }
    }    
    
    /**
     * Check if the user is authorized to access the folder
     * @param function
     * @return
     */    
    public boolean isAuthorized(String userId,String folderId,String mode) {
        try {
            //String ticket=readTicket();
	    String ticket="";
            SecurityServiceServiceLocator locator=new SecurityServiceServiceLocator();
            it.eng.spagobi.services.security.stub.SecurityService servizio= locator.getSecurityService();
            return servizio.isAuthorized(ticket,userId,folderId, mode);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
	return false;
    } 
    

    /**
     * Check if the user can execute the function ( user function )
     * @param function
     * @return
     */
    public boolean checkAuthorization(String function){
	
	return true;
    }
    
    /**
     * Check if the user can execute the function ( user function )
     * @param function
     * @return
     */    
    public boolean checkAuthorization(Principal principal,String function){
	
	return true;
    }    
}
