package it.eng.spagobi.security;

import org.apache.log4j.Logger;

import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;

public class InternalSecurityServiceSupplierImpl implements
		ISecurityServiceSupplier {
	
	static private Logger logger = Logger.getLogger(InternalSecurityServiceSupplierImpl.class);

	public SpagoBIUserProfile checkAuthentication(String userId, String psw) {
		// TODO Auto-generated method stub
		return null;
	}

	public SpagoBIUserProfile checkAuthenticationWithToken(String userId,
			String token) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean checkAuthorization(String userId, String function) {
		// TODO Auto-generated method stub
		return false;
	}

	public SpagoBIUserProfile createUserProfile(String userId) {
		logger.debug("IN - userId: " + userId);
		
		SpagoBIUserProfile obj=new SpagoBIUserProfile();
		obj.setUniqueIdentifier(userId);
		obj.setUserId(userId);
		
		logger.debug("OUT");
		return obj;
	}

}
