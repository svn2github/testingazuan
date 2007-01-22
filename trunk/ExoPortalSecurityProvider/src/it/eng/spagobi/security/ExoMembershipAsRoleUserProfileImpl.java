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
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class ExoMembershipAsRoleUserProfileImpl implements IEngUserProfile {

	private String userUniqueIdentifier = null;
	private Map userAttributes = null;
	private Collection roles = null;
	private Map functionalities = null;
	
	/**
	 * @param userUniqueIdentifier
	 * @param userAttributes
	 * @param roles
	 */
	public ExoMembershipAsRoleUserProfileImpl(Principal userPrincipal) {
		
		super();
		// set user identifier
		this.userUniqueIdentifier = userPrincipal.getName();
		// create empty attribute map
		userAttributes = new HashMap();
		// create empty functionalities map
		functionalities = new HashMap();
		// create empty list of roles
		this.roles = new ArrayList();
		
		// get istance of the organization service		
		PortalContainer container = PortalContainer.getInstance();	
		SecurityProviderUtilities.debug(this.getClass(), "init", "Portal Container retrived " + container);
		OrganizationService service = (OrganizationService) container.getComponentInstanceOfType(OrganizationService.class);
		SecurityProviderUtilities.debug(this.getClass(), "init", "Organization Service retrived " + service);
        // get filter pattern
		Pattern pattern = SecurityProviderUtilities.getFilterPattern();
		Matcher matcher = null;
		
		
		// fill functionalities and roles		
		try{
			GroupHandler groupHandler = service.getGroupHandler();
			SecurityProviderUtilities.debug(this.getClass(), "init", "Group Handler retrived " + groupHandler);
			MembershipHandler memberHandler = service.getMembershipHandler();
			SecurityProviderUtilities.debug(this.getClass(), "init", "Membership Handler retrived " + memberHandler);
			Collection tmpRoles = groupHandler.findGroupsOfUser(userUniqueIdentifier);
			SecurityProviderUtilities.debug(this.getClass(), "init", "User group collection retrived " + tmpRoles);
			Group group = null;
			Membership membership = null;
			for(Iterator it = tmpRoles.iterator(); it.hasNext();){
				group = (Group) it.next();
				String groupID = group.getId();
				SecurityProviderUtilities.debug(this.getClass(), "init", "Process group " + groupID);
				Collection memberCol = memberHandler.findMembershipsByUserAndGroup(userUniqueIdentifier, group.getId());
				SecurityProviderUtilities.debug(this.getClass(), "init", "User/Role membership collection retrived " + memberCol);
				// fill roles
				Iterator iterMember = memberCol.iterator();
				while(iterMember.hasNext()){
					membership = (Membership)iterMember.next();
					String memberType = membership.getMembershipType();
					matcher = pattern.matcher(memberType);
            		if(!matcher.find()){
            			continue;	
            		}
            		SecurityProviderUtilities.debug(this.getClass(), "init", "Process membership " + memberType);
					if(!this.roles.contains(memberType)) {
						this.roles.add(memberType);
						SecurityProviderUtilities.debug(this.getClass(), "init", "Added role " + memberType);
					}
				}	
				// fill functionalities
				String pathFunct = getFunctPath(groupID);
				if(pathFunct==null){
					continue;
				}
				iterMember = memberCol.iterator();
				while(iterMember.hasNext()){
					membership = (Membership)iterMember.next();
					String memberType = membership.getMembershipType();
					matcher = pattern.matcher(memberType);
            		if(!matcher.find()){
            			continue;	
            		}
					if(!this.functionalities.containsKey(pathFunct)){
						List roles = new ArrayList();
		                roles.add(memberType);
		                this.functionalities.put(pathFunct, roles);
		                SecurityProviderUtilities.debug(this.getClass(), "init", "Added functionality " + pathFunct + " -> " + roles);
					} else {
						List roles = (List)this.functionalities.get(pathFunct);
						if(!roles.contains(memberType)){
							roles.add(memberType);
							functionalities.put(pathFunct, roles);
							SecurityProviderUtilities.debug(this.getClass(), "init", "Added role "+memberType+" to functionality " + pathFunct);
						}
					}
				}
			}
			

			//start load profile attributes 

			userAttributes = SecurityProviderUtilities.getUserProfileAttributes(userUniqueIdentifier, service);
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
                                "<init>", "Attributes load into SpagoBI profile: " + userAttributes);

			// end load profile attributes
			
		} catch(Exception e){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, ExoMembershipAsRoleUserProfileImpl.class.getName(), 
					            "<init>", "Exception ",e);
		}
	}

	
	/**
	 * Generates the functioanlity path starting from froup path and configuration
	 * @param groupId The group path
	 * @return
	 */
	private String getFunctPath(String groupId) {
		String path = null;
		try{
			ConfigSingleton config = ConfigSingleton.getInstance();
			String attribute = "SPAGOBI.SECURITY.FUNCTIONALITIES-LOADER.GROUP-TRANSFORMERS.TRANSFORMER";
			List transSBList = config.getAttributeAsList(attribute);
	        Iterator iterTransSBList = transSBList.iterator();
	        while(iterTransSBList.hasNext()) {
	        	SourceBean transSB = (SourceBean)iterTransSBList.next();
	        	String startWith = (String)transSB.getAttribute("startwith");
	        	String prefix = (String)transSB.getAttribute("prefix");
	        	String dimension = (String)transSB.getAttribute("dimension");
	        	if(groupId.startsWith(startWith)){
	        		groupId = groupId.substring(groupId.indexOf("/") + 1);
	        		groupId = groupId.substring(groupId.indexOf("/") + 1);
	        		groupId = "/" + dimension + "/" + groupId;
	        		path = prefix + groupId;
	        	}
	        }
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, ExoMembershipAsRoleUserProfileImpl.class.getName(), 
		                        "getFunctPath", "Error while generating functionality path",e);
		}
		return path;
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
		return functionalities.keySet();
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
		Collection functs = new ArrayList();
		Set functKeys = functionalities.keySet();
		Iterator iterFunctKey = functKeys.iterator();
		while(iterFunctKey.hasNext()) {
			String functKey = (String)iterFunctKey.next();
			List roleOfFunct = (List)functionalities.get(functKey);
			if(roleOfFunct.contains(arg0)) {
				functs.add(functKey);
			}
		}
		return functs;
	}

}
