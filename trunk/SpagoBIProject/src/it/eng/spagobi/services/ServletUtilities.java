package it.eng.spagobi.services;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.security.IPortalSecurityProvider;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.List;

public class ServletUtilities {

   	/**
   	 * Get the portal roles assigned to a user, if the user doesn't exist the role list is empty 
   	 * 
   	 * @param username username of the user
   	 * @param password password of the user
   	 * @return the user's roles list
   	 */    
    public static List getRoles(String username, String password) {
    	List roles = new ArrayList();
    	ConfigSingleton config = ConfigSingleton.getInstance();
    	SourceBean securityconfSB = (SourceBean)config.getAttribute("SPAGOBI.SECURITY.PORTAL-SECURITY-CLASS");
    	String portalSecurityProviderClass = (String) securityconfSB.getAttribute("className");
    	IPortalSecurityProvider portalSecurityProvider = null;
    	try {
    		Class portSecClass = Class.forName(portalSecurityProviderClass);
    		portalSecurityProvider = (IPortalSecurityProvider)portSecClass.newInstance();
    	} catch (Exception e) {
    		SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, ServletUtilities.class.getName(), 
					   "getRoles", "Error while instantiating the portal security class", e);
    		return roles;
    	}
    	SourceBean portSecClassConfig = (SourceBean) securityconfSB.getAttribute("CONFIG");
    	roles = portalSecurityProvider.getUserRoles(username, portSecClassConfig);
    	return roles;
	}
}
