/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.booklets.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;

/**
 * Controls the Anonymous Workflow User Profile.
 * 
 * @author Zerbetto
 *
 */

public class AnonymousWorkflowProfile implements IEngUserProfile {

	private static String WORKFLOW_USER_NAME = "[SYSTEM - WORKFLOW]";
	
	private String userUniqueIdentifier = WORKFLOW_USER_NAME;
	private Collection roles = null;
	private Map userAttributes = new HashMap();
	
	public AnonymousWorkflowProfile(){
		this.roles = new ArrayList();
		this.userAttributes.put("password", WORKFLOW_USER_NAME);
	}
	
	/**
	 * @see it.eng.spago.security.IEngUserProfile#getUserUniqueIdentifier()
	 */
	public Object getUserUniqueIdentifier() {
		return userUniqueIdentifier;
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#getUserAttribute(java.lang.String)
	 */
	public Object getUserAttribute(String arg0) throws EMFInternalError {
		return this.userAttributes.get(arg0);
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#hasRole(java.lang.String)
	 */
	public boolean hasRole(String arg0) throws EMFInternalError {
		
		return this.roles.contains(arg0);
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#getRoles()
	 */
	public Collection getRoles() throws EMFInternalError {
		return this.roles;
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#getFunctionalities()
	 */
	public Collection getFunctionalities() throws EMFInternalError {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#isAbleToExecuteAction(java.lang.String)
	 */
	public boolean isAbleToExecuteAction(String arg0) throws EMFInternalError {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#isAbleToExecuteModuleInPage(java.lang.String, java.lang.String)
	 */
	public boolean isAbleToExecuteModuleInPage(String arg0, String arg1) throws EMFInternalError {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see it.eng.spago.security.IEngUserProfile#setApplication(java.lang.String)
	 */
	public void setApplication(String arg0) throws EMFInternalError {
		// TODO Auto-generated method stub
		
	}

	public Collection getUserAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getFunctionalitiesByRole(String arg0) throws EMFInternalError {
		// TODO Auto-generated method stub
		return null;
	}

}
