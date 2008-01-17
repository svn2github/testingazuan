package it.eng.spagobi.commons.bo;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contain the information about the user
 */
public class UserProfile implements IEngUserProfile {
    
        private static String WORKFLOW_USER_NAME = "[SYSTEM - WORKFLOW]";
        private static String SCHEDULER_USER_NAME = "scheduler";
    
	private String userUniqueIdentifier = null;
	private Map userAttributes = null;
	private Collection roles = null;
	private Collection functionalities = null;


	/**
	 * 
	 * @param profile SpagoBIUserProfile
	 */
    public UserProfile(SpagoBIUserProfile profile){
	this.userUniqueIdentifier=profile.getUserId();
	roles=new ArrayList();
	if (profile.getRoles()!=null) {
	    int l=profile.getRoles().length;
		for (int i=0;i<l;i++){
		    roles.add(profile.getRoles()[i]);
		}
	        
	}
	functionalities=new ArrayList();
	if (profile.getFunctions()!=null) {
	    int l=profile.getFunctions().length;
		for (int i=0;i<l;i++){
		    functionalities.add(profile.getFunctions()[i]);
		}
	        
	}	
	userAttributes=profile.getAttributes();
    }
    /**
     * 
     * @param user String
     */
    public UserProfile(String user){
	this.userUniqueIdentifier=user;
    }  
    
    /**
     * Usato solo nel workflow
     * @param user
     * @param psw
     */
    public static final UserProfile createWorkFlowUserProfile(){
	UserProfile profile=new UserProfile("[SYSTEM - WORKFLOW]");
	profile.roles = new ArrayList();
	profile.userAttributes = new HashMap();
	profile.userAttributes.put("password", WORKFLOW_USER_NAME);
	return profile;
    } 
    
    /**
     * Usato solo per lanciare i job
     * @param user
     * @param psw
     */    
    public static final UserProfile createSchedulerUserProfile(){
	UserProfile profile=new UserProfile(SCHEDULER_USER_NAME);
	profile.roles = new ArrayList();
	profile.userAttributes = new HashMap();
	return profile;
    }    

    /**
     * 
     * @param userid String
     * @return
     */
    public static boolean isSchedulerUser(String userid){
	return SCHEDULER_USER_NAME.equals(userid);
    }

	
    public Collection getFunctionalities() throws EMFInternalError {
	return functionalities;
    }

    public Collection getFunctionalitiesByRole(String arg0)
	    throws EMFInternalError {
	return new ArrayList();

    }

    public Collection getRoles() throws EMFInternalError {
	return this.roles;
    }

    public Object getUserAttribute(String attributeName) throws EMFInternalError {
	return userAttributes.get(attributeName);
    }

    public Collection getUserAttributeNames() {
	return userAttributes.keySet();
    }

    public Object getUserUniqueIdentifier() {
	return userUniqueIdentifier;
    }

    public boolean hasRole(String roleName) throws EMFInternalError {
	return this.roles.contains(roleName);
    }

    public boolean isAbleToExecuteAction(String arg0) throws EMFInternalError {
	if (this.functionalities.contains(arg0)) return true;
	else return false;
    }

    public boolean isAbleToExecuteModuleInPage(String arg0, String arg1)
	    throws EMFInternalError {
	return true;
    }

    public void setApplication(String arg0) throws EMFInternalError {
    }
    
    public void setFunctionalities(Collection functs) {
		this.functionalities=functs;
    }
    
    public void setAttributes(Map attrs) {
		this.userAttributes=attrs;
    }
	
    public void setRoles(Collection rols) {
		this.roles=rols;
   }    
}
