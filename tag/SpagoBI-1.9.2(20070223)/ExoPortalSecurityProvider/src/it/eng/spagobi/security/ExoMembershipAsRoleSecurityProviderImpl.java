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
import it.eng.spago.error.EMFInternalError;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.MembershipTypeHandler;
import org.exoplatform.services.organization.OrganizationService;

/**
 * Implements the IPortalSecurityProvider interface defining method to get the 
 * system and user roles.
 */
public class ExoMembershipAsRoleSecurityProviderImpl implements IPortalSecurityProvider {
	
	/** 
	 * Get all the portal roles 
	 * @return List of the portal roles (list of it.eng.spagobi.bo.Role)
	 */
	public List getRoles() {
		List roles = new ArrayList();
		PortalContainer container = PortalContainer.getInstance();
		SecurityProviderUtilities.debug(this.getClass(), "getRoles", "Portal Container retrived " + container);
		OrganizationService service = (OrganizationService)container.getComponentInstanceOfType(OrganizationService.class);
		SecurityProviderUtilities.debug(this.getClass(), "getRoles", "Organization Service retrived " + service);
		try{
			GroupHandler groupHandler = service.getGroupHandler();
			SecurityProviderUtilities.debug(this.getClass(), "getRoles", "Group Handler retrived " + groupHandler);
			MembershipHandler membershipHandler = service.getMembershipHandler();
			SecurityProviderUtilities.debug(this.getClass(), "getRoles", "Membership Handler retrived " + membershipHandler);
			MembershipTypeHandler membershipTypeHandler = service.getMembershipTypeHandler();
			SecurityProviderUtilities.debug(this.getClass(), "getRoles", "MembershipType Handler retrived " + membershipTypeHandler);
			Collection groups = service.getGroupHandler().getAllGroups();
			SecurityProviderUtilities.debug(this.getClass(), "getRoles", "Group collection retrived " + groups);
			roles = getRolesFromMembership(groups, membershipHandler, membershipTypeHandler);
			SecurityProviderUtilities.debug(this.getClass(), "getRoles", "roles retrived " + roles);	
		} catch (Exception e) {
			SpagoBITracer.critical("SPAGOBI(ExoSecurityProvider)",
					               this.getClass().getName(),
					               "getRoles()",
					               "Exception ", e);
		}
		return roles;
	}
	
	
	/**
	 * Gets the list of role names starting from a collection of group. 
	 * @param groups The collection of exo groups
	 * @param membershipHandler the handler of the membership relation
	 * @param membershipTypeHandler the handler of membership type
	 * @return A list of roles extract from the group collection (list of it.eng.spagobi.bo.Role)
	 */
	private List getRolesFromMembership(Collection groups, MembershipHandler membershipHandler, 
			                            MembershipTypeHandler membershipTypeHandler) {
		List rolesNames = new ArrayList();
		List roles = new ArrayList();
		Iterator iter = groups.iterator();
	    Group group = null;
	    Role role = null;
	    Membership member = null;
	    MembershipType membershipType = null;
	    while(iter.hasNext()) {
	    	group = (Group)iter.next();
	    	SecurityProviderUtilities.debug(this.getClass(), "getRolesFromMembership", "Start Process group " + group.getId());
	    	Collection memebers = null;
	    	try{
	    		memebers = membershipHandler.findMembershipsByGroup(group);
	    	} catch(Exception e) {
	    		SpagoBITracer.critical("SPAGOBI(ExoSecurityProvider)",
			               			   this.getClass().getName(),
			                           "getRolesFromMembership()",
			                           "Error while recovering membership of the group", e);
	    		continue;
	    	}
	    	SecurityProviderUtilities.debug(this.getClass(), "getRolesFromMembership", "Memberships of the gropu retrived " + memebers);
	    	Iterator iterMem = memebers.iterator();
	    	while(iterMem.hasNext()) {
	    		member = (Membership)iterMem.next();
	    		String memberType = member.getMembershipType();
	    		if(!rolesNames.contains(memberType)) {
	    			rolesNames.add(memberType);
	    			try{
	    				membershipType = membershipTypeHandler.findMembershipType(memberType);
	    			} catch(Exception e) {
	    	    		SpagoBITracer.critical("SPAGOBI(ExoSecurityProvider)",
	    	    							   this.getClass().getName(),
	    	    							   "getRolesFromMembership()",
	    	    							   "Error while recovering membershipType of the membership", e);
	    	    		continue;
	    			}
	    			String membDescr = membershipType.getDescription();
	    			role = new Role(memberType, membDescr);
	    			roles.add(role);
	    			SecurityProviderUtilities.debug(this.getClass(), "getRolesFromMembership", "Role " + memberType + " added" );
	    		} else {
	    			SecurityProviderUtilities.debug(this.getClass(), "getRolesFromMembership", "Role " + memberType + " already present");
	    		}
	    	}
	    }
	    return roles;
	}
	
	

	/**
	 * Get the list of the user roles. If the user doesn't exist the roles list is empty
	 * @param user Username
	 * @param config The SourceBean configuration
	 * @return List of user roles (list of it.eng.spagobi.bo.Role)
	 */
	public List getUserRoles(String user, SourceBean config) {
		SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "getUserRoles:start method");
		List roles = new ArrayList();
		String paramCont = "NAME_PORTAL_APPLICATION";
		SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "use param " + paramCont);
		SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "config SourceBean in input: " + config);
		SourceBean paramContSB = (SourceBean)config.getAttribute(paramCont);
		SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "param context name Source Bean retrived: " + paramContSB);
		String nameCont = (String)paramContSB.getCharacters();
		SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "use context name " + nameCont);
		RootContainer rootCont = RootContainer.getInstance();
		SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "root container retrived: " + rootCont);
		PortalContainer container = rootCont.getPortalContainer(nameCont);
		SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "portal container retrived: " + container);
		OrganizationService service = (OrganizationService)container.getComponentInstanceOfType(OrganizationService.class);
		SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "organization service retrived: " + service);
		try{
			GroupHandler groupHandler = service.getGroupHandler();
			SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "Group Handler retrived " + groupHandler);
			MembershipHandler membershipHandler = service.getMembershipHandler();
			SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "Membership Handler retrived " + membershipHandler);
			MembershipTypeHandler membershipTypeHandler = service.getMembershipTypeHandler();
			SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "MembershipType Handler retrived " + membershipTypeHandler);
			Collection groups = groupHandler.findGroupsOfUser(user);
			SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "Group collection retrived " + groups);
			roles = getRolesFromMembership(groups, membershipHandler, membershipTypeHandler);
			SecurityProviderUtilities.debug(this.getClass(), "getUserRoles", "roles retrived " + roles);		
		} catch (Exception e) {
			SpagoBITracer.critical("SPAGOBI(ExoSecurityProvider)",
					               this.getClass().getName(),
					               "getUserRoles(String, String)",
					               "Error retrieving groups of user "+user, e);
		}
		SecurityProviderUtilities.debug(this.getClass(), "getRoles", "roles returned " + roles);		
		return roles;
	}
	
	public List getAllProfileAttributesNames() {
		List toReturn = null;
		try {
			toReturn = SecurityProviderUtilities.getAllProfileAtributesNames();
		} catch (EMFInternalError e) {
			SpagoBITracer.critical("SPAGOBI(ExoSecurityProvider)",
		               this.getClass().getName(),
		               "getAllProfileAttributesNames()",
		               "Error retrieving the list of all profile attributes names", e);
			return new ArrayList();
		}
		return toReturn;
	}
	
}
