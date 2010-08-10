package it.eng.spagobi.mockObjects;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;

import java.util.Collection;
import java.util.HashMap;

public class EngUserProfileImplMock implements IEngUserProfile {

	private Collection roles = null;
	private String userUniqueIdentifier = null;
	private HashMap attributes; 
	
	public EngUserProfileImplMock() {
		attributes = new HashMap();
	}

	public Object getUserUniqueIdentifier() {
		return userUniqueIdentifier;
	}

	public Object getUserAttribute(String attributeName)
			throws EMFInternalError {
		if (attributeName != null) return attributes.get(attributeName);
		return null;
	}

	public boolean hasRole(String roleName) throws EMFInternalError {
		if (roles.contains(roleName)) return true;
		return false;
	}

	public Collection getRoles() throws EMFInternalError {
		return roles;
	}

	public Collection getFunctionalities() throws EMFInternalError {
		return null;
	}

	public boolean isAbleToExecuteAction(String actionName)
			throws EMFInternalError {
		return false;
	}

	public boolean isAbleToExecuteModuleInPage(String pageName,
			String moduleName) throws EMFInternalError {
		return false;
	}

	public void setApplication(String applicationName) throws EMFInternalError {
	}

	public void setRoles(Collection roles) {
		this.roles = roles;
	}

	public void setUserAttribute(String attrName, Object attr) {
		if (attrName != null && attr != null) attributes.put(attrName, attr);
	}
}
