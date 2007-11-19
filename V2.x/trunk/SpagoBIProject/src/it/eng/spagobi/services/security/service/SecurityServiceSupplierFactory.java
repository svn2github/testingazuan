package it.eng.spagobi.services.security.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;

/**
 * Factory class for the security supplier 
 * @author Bernabei Angelo
 *
 */
public class SecurityServiceSupplierFactory {

    public static ISecurityServiceSupplier createISecurityServiceSupplier(){

	SourceBean configSingleton = (SourceBean)ConfigSingleton.getInstance();
	SourceBean engUserProfileFactorySB = (SourceBean) configSingleton.getAttribute("SPAGOBI.SECURITY.USER-PROFILE-FACTORY-CLASS");
	String engUserProfileFactoryClass = (String) engUserProfileFactorySB.getAttribute("className");
	engUserProfileFactoryClass = engUserProfileFactoryClass.trim(); 
	try {
	    return  (ISecurityServiceSupplier)Class.forName(engUserProfileFactoryClass).newInstance();
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	return null;
    }
    
}
