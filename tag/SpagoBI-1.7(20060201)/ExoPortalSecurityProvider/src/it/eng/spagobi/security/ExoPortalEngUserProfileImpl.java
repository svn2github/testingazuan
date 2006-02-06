/**

Copyright 2005 Engineering Ingegneria Informatica S.p.A.

This file is part of SpagoBI.

SpagoBI is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
any later version.

SpagoBI is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Spago; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

**/
package it.eng.spagobi.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.constants.SecurityConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

/**
 * Implementation of the Spago IEngUserProfile interface
 */
public class ExoPortalEngUserProfileImpl implements IEngUserProfile {

	private String userUniqueIdentifier = null;
	private Map userAttributes = null;
	private Collection roles = null;
	
	/**
	 * @param userUniqueIdentifier
	 * @param userAttributes
	 * @param roles
	 */
	public ExoPortalEngUserProfileImpl(Principal userPrincipal) {
		super();
		this.userUniqueIdentifier = userPrincipal.getName();
		PortalContainer container = PortalContainer.getInstance();		
		OrganizationService service = (OrganizationService) container.getComponentInstanceOfType(OrganizationService.class);
		this.roles = new ArrayList();
		try{
			Collection tmpRoles = service.findGroupsOfUser(userUniqueIdentifier); 
			Group group = null;
			for (Iterator it = tmpRoles.iterator(); it.hasNext();){
				group = (Group)it.next();
				this.roles.add(group.getId());
			}
		}catch(Exception e){
			SpagoBITracer.major(SecurityConstants.NAME_MODULE, ExoPortalEngUserProfileImpl.class.getName(), "<init>", "Exception ",e);
		}
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
	public Object getUserAttribute(String attributeName) throws EMFInternalError {
		return userAttributes.get(attributeName);
	}

	/** (non-Javadoc)
	 * @see it.eng.spago.security.IEngUserProfile#hasRole(java.lang.String)
	 */
	public boolean hasRole(String roleName) throws EMFInternalError {
		return this.roles.contains(roleName);
	}

	/** (non-Javadoc)
	 * @see it.eng.spago.security.IEngUserProfile#getRoles()
	 */
	public Collection getRoles() throws EMFInternalError {
		return this.roles;
	}

	/** 
	 * @see it.eng.spago.security.IEngUserProfile#getFunctionalities()
	 */
	public Collection getFunctionalities() throws EMFInternalError {
		return new ArrayList();
	}

	/** 
	 * @see it.eng.spago.security.IEngUserProfile#isAbleToExecuteAction(java.lang.String)
	 */
	public boolean isAbleToExecuteAction(String arg0) throws EMFInternalError {
		return true;
	}

	/** 
	 * @see it.eng.spago.security.IEngUserProfile#isAbleToExecuteModuleInPage(java.lang.String, java.lang.String)
	 */
	public boolean isAbleToExecuteModuleInPage(String arg0, String arg1) throws EMFInternalError {
		return true;
	}

	/** 
	 * @see it.eng.spago.security.IEngUserProfile#setApplication(java.lang.String)
	 */
	public void setApplication(String arg0) throws EMFInternalError {	
	}

}
