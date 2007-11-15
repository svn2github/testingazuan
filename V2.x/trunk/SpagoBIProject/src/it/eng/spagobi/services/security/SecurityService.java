package it.eng.spagobi.services.security;


import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;


/**
 * This is the SecurityService interfaces
 * @author Bernabei Angelo
 *
 */
public interface SecurityService {

    	/**
    	 * return the user profile informations
    	 * @param token
    	 * @return
    	 */
        SpagoBIUserProfile getUserProfile(String token);
	
	/**
	 * Check if the user can access to the path
	 * @param token
	 * @param idFolder ( object tree )
	 * @param mode
	 * @return
	 */
	boolean isAuthorized(String token,String idFolder,String mode);
	
	/**
	 * check if the user can access to this function 
	 * @param token
	 * @param function
	 * @return
	 */
	boolean checkAuthorization(String token,String function);	
}
