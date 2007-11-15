package it.eng.spagobi.commons.bo;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserProfile implements IEngUserProfile {

    
	private String userUniqueIdentifier = null;
	private Map userAttributes = null;
	private Collection roles = null;
	private Collection functionalities = null;
	
    public UserProfile(SpagoBIUserProfile profile){
	this.userUniqueIdentifier=profile.getUserId();
	roles=new ArrayList();
	if (profile.getRoles()!=null) {
	    int l=profile.getRoles().length;
		for (int i=0;i<l;i++){
		    roles.add(profile.getRoles()[i]);
		}
	        
	}
	userAttributes=profile.getAttributes();
    }
    
    public UserProfile(String user){
	this.userUniqueIdentifier=user;
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
	return true;
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
