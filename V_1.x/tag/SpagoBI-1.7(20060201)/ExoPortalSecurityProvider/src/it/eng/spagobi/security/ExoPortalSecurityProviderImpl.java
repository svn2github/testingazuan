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

import it.eng.spagobi.bo.Role;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.database.DatabaseService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;

/**
 * Implements the IPortalSecurityProvider interface defining method to get the 
 * system and user roles.
 */
public class ExoPortalSecurityProviderImpl implements IPortalSecurityProvider {
	
	/** 
	 * Get all the portal roles 
	 * @return List of the portal roles
	 */
	public List getRoles() {
		List roles = new ArrayList();
		PortalContainer container = PortalContainer.getInstance();	
		OrganizationService service = (OrganizationService) container.getComponentInstanceOfType(OrganizationService.class);
		try {
			Collection groups = service.findGroups(null);
		    Iterator iter = groups.iterator();
		    Group group = null;
		    Role role = null;
		    while(iter.hasNext()) {
		    	group = (Group)iter.next();
		    	SpagoBITracer.debug("UTILITIES",
		    			            "ExoPortalSecurityProviderImpl",
		    			            "getRoles()",
		    			            "Find a Role With Name [" + group.getGroupName() +"]");
		    	add(group, service, roles);
		    }
		} catch (Exception e) {
			SpagoBITracer.critical("UTILITIES",
					               "ExoPortalSecurityProviderImpl",
					               "getRoles()",
					               "Exception ", e);
		}
		return roles;
	}
	
	
	/**
	 * Add the current group(role) and it's child to the roles list
	 * 
	 * @param group Group of the portal
	 * @param orgService OrganizationService of the portal
	 * @param roles List of roles
	 */
	private void add(Group group, OrganizationService orgService, List roles){
		Role role = new Role(group.getId(), group.getDescription());
    	roles.add(role);
    	try{
    		Collection children = orgService.findGroups(group);
    		if ((children == null) || (children.size() == 0)){
    			// End recursion
    			return;
    		}else{
    			Iterator it = children.iterator();
    			while (it.hasNext()){
    				add((Group)it.next(), orgService, roles);
    			}
    		}
    	}catch(Exception e){
    		SpagoBITracer.critical("UTILITIES",
    				               "ExoPortalSecurityProviderImpl",
    				               "getRoles()",
    				               "Exception when retrieving child of group "+group.getId(), e);
    	}
	}

	/**
	 * Get the list of the user roles. If the user doesn't exist the roles list is empty
	 * 
	 * @param user Username
	 * @param passwd Password of the user
	 * @return List of user roles
	 */
	public List getUserRoles(String user, String passwd) {
		List roles = new ArrayList();
		RootContainer root = RootContainer.getInstance();
		PortalContainer portalCont = root.getPortalContainer("portal");
		PortalContainer.setInstance(portalCont);
		OrganizationService service = 
			(OrganizationService)portalCont.getComponentInstanceOfType(OrganizationService.class);
		try {
			Collection groups = service.findGroupsOfUser(user);
			Iterator iterGroups = groups.iterator();
			while(iterGroups.hasNext()) {
				Group group = (Group)iterGroups.next();
				String groupid = group.getId();
				roles.add(groupid);
			}
		} catch (Exception e) {
			SpagoBITracer.critical("UTILITIES",
					               "ExoPortalSecurityProviderImpl",
					               "getUserRoles()",
					               "Error retrieving groups of user "+user, e);
		}
		return roles;
	}
	
}
