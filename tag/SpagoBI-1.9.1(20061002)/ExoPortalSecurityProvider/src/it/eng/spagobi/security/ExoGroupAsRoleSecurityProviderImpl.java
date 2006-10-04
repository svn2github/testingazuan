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
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;

/**
 * Implements the IPortalSecurityProvider interface defining method to get the 
 * system and user roles.
 */
public class ExoGroupAsRoleSecurityProviderImpl implements IPortalSecurityProvider {
	
	/** 
	 * Get all the portal roles 
	 * @return List of the portal roles (list of it it.eng.spagobi.bo.Role)
	 */
	public List getRoles() {
		List roles = new ArrayList();
		PortalContainer container = PortalContainer.getInstance();	
		OrganizationService service = (OrganizationService) container.getComponentInstanceOfType(OrganizationService.class);
		try {
			Collection groups = service.getGroupHandler().getAllGroups();
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
	 * @param group Group of the portal
	 * @param orgService OrganizationService of the portal
	 * @param roles List of roles (list of it it.eng.spagobi.bo.Role)
	 */
	private void add(Group group, OrganizationService orgService, List roles){
		Role role = new Role(group.getId(), group.getDescription());
    	roles.add(role);
    	try{
    		Collection children = orgService.getGroupHandler().findGroups(group);
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
	 * @param user Username
	 * @param config The SourceBean configuration
	 * @return List of user roles (list of it.eng.spagobi.bo.Role)
	 */
	public List getUserRoles(String user, SourceBean config) {
		TracerSingleton.log("SPAGOBI", TracerSingleton.DEBUG, 
							"ExoPortalSecurityProviderImpl::getUserRoles:start method");
		List roles = new ArrayList();
		String paramCont = "NAME_PORTAL_APPLICATION";
		TracerSingleton.log("SPAGOBI", TracerSingleton.DEBUG, 
							"ExoPortalSecurityProviderImpl::getUserRoles:use param " + paramCont);
		TracerSingleton.log("SPAGOBI", TracerSingleton.DEBUG, 
				            "ExoPortalSecurityProviderImpl::getUserRoles:config SourceBean in input: " + config);
		SourceBean paramContSB = (SourceBean)config.getAttribute(paramCont);
		TracerSingleton.log("SPAGOBI", TracerSingleton.DEBUG, 
							"ExoPortalSecurityProviderImpl::getUserRoles:param context name Source Bean " +
							"retrived: " + paramContSB);
		String nameCont = (String)paramContSB.getCharacters();
		TracerSingleton.log("SPAGOBI", TracerSingleton.DEBUG, 
				            "ExoPortalSecurityProviderImpl::getUserRoles: use context name " + nameCont);
		RootContainer rootCont = RootContainer.getInstance();
		TracerSingleton.log("SPAGOBI", TracerSingleton.DEBUG, 
	                        "ExoPortalSecurityProviderImpl::getUserRoles: root container retrived: " + rootCont);
		PortalContainer container = rootCont.getPortalContainer(nameCont);
		TracerSingleton.log("SPAGOBI", TracerSingleton.DEBUG, 
                            "ExoPortalSecurityProviderImpl::getUserRoles: portal container retrived: " + container);
		OrganizationService service = 
			(OrganizationService)container.getComponentInstanceOfType(OrganizationService.class);
		TracerSingleton.log("SPAGOBI", TracerSingleton.DEBUG, 
                			"ExoPortalSecurityProviderImpl::getUserRoles: organization service retrived: " + service);
		try {
			Collection groups = service.getGroupHandler().findGroupsOfUser(user);
			Iterator iterGroups = groups.iterator();
			while(iterGroups.hasNext()) {
				Group group = (Group)iterGroups.next();
				add(group, service, roles);
				//String groupid = group.getId();
				//roles.add(groupid);
			}
		} catch (Exception e) {
			SpagoBITracer.critical("UTILITIES",
					               "ExoPortalSecurityProviderImpl",
					               "getUserRoles()",
					               "Error retrieving groups of user "+user, e);
		}
		TracerSingleton.log("SPAGOBI", TracerSingleton.DEBUG, 
							"ExoPortalSecurityProviderImpl::getUserRoles:end method return roles: " + roles);
		return roles;
	}
	
}
