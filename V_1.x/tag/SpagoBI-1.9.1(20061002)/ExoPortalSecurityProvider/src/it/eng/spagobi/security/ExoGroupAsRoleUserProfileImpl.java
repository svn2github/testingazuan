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

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.OrganizationService;

/**
 * Implementation of the Spago IEngUserProfile interface
 */
public class ExoGroupAsRoleUserProfileImpl implements IEngUserProfile {

	private String userUniqueIdentifier = null;
	private Map userAttributes = null;
	private Collection roles = null;
	private SecurityProviderUtilities util = new SecurityProviderUtilities();
	
	/**
	 * @param userUniqueIdentifier
	 * @param userAttributes
	 * @param roles
	 */
	public ExoGroupAsRoleUserProfileImpl(Principal userPrincipal) {
		super();
		this.userUniqueIdentifier = userPrincipal.getName();
		userAttributes = new HashMap();
		PortalContainer container = PortalContainer.getInstance();		
		OrganizationService service = (OrganizationService) container.getComponentInstanceOfType(OrganizationService.class);
		this.roles = new ArrayList();
		try{
			Collection tmpRoles = service.getGroupHandler().findGroupsOfUser(userUniqueIdentifier); 
			GroupHandler groupHandler = service.getGroupHandler();
			util.debug(this.getClass(), "init", "Group Handler retrived " + groupHandler);
			MembershipHandler memberHandler = service.getMembershipHandler();
			util.debug(this.getClass(), "init", "Membership Handler retrived " + memberHandler);
			Group group = null;
			Matcher matcher = null;
			for (Iterator it = tmpRoles.iterator(); it.hasNext();){
				group = (Group) it.next();
				String groupID = group.getId();
				Pattern pattern = util.getFilterPattern();
				matcher = pattern.matcher(groupID);
				if(!matcher.find()){
        			continue;	
        		}
				this.roles.add(group.getId());
			}
			//load profile attributes for all users
			userAttributes = SecurityProviderUtilities.getAllSharedProfileAttributes();
			SourceBean loadUserProfileAttrsSB = (SourceBean) ConfigSingleton.getInstance().getAttribute("EXO_PORTAL_SECURITY.PROFILE_ATTRIBUTES.LOAD-USER-PROFILE-ATTRIBUTES");
			String loadUserProfileAttrs = loadUserProfileAttrsSB.getCharacters();
			HashMap predefinedProfileAttributes = new HashMap();
			if (loadUserProfileAttrs != null && loadUserProfileAttrs.trim().toUpperCase().equals("YES")) {
				SpagoBITracer.info(SpagoBIConstants.NAME_MODULE, ExoGroupAsRoleUserProfileImpl.class.getName(), "<init>",
						"Trying to load predefined user attributes for user with unique identifer '" + this.userUniqueIdentifier +"'.");
				predefinedProfileAttributes = SecurityProviderUtilities.getPredefinedProfileAttributes(userUniqueIdentifier);
			} else {
				SpagoBITracer.info(SpagoBIConstants.NAME_MODULE, ExoGroupAsRoleUserProfileImpl.class.getName(), "<init>",
						"Predefined user attributes for user with unique identifer '" + this.userUniqueIdentifier +"' will not be loaded.");
			}
			//add the predefined attributes for the current user (already existing attributes are overwritten)
			userAttributes.putAll(predefinedProfileAttributes);
		} catch(Exception e){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, ExoGroupAsRoleUserProfileImpl.class.getName(), "<init>", "Exception ",e);
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

	public Collection getUserAttributeNames() {
		return userAttributes.keySet();
	}

	public Collection getFunctionalitiesByRole(String arg0) throws EMFInternalError {
		Collection col = new ArrayList();
		return col;
	}

}
