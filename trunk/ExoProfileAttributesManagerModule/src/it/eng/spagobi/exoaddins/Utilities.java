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
package it.eng.spagobi.exoaddins;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.security.IPortalSecurityProvider;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.exoplatform.services.organization.User;

public class Utilities {

	public static List getExoUserFiltered(List allUser) {
		List filteredUser = new ArrayList();
		try {
			// recover spago configuration
			ConfigSingleton conf = ConfigSingleton.getInstance();
			if(conf==null) throw new Exception("Configuration not Found");
			// create instance of the portal security class
			SourceBean secClassSB = (SourceBean)conf.getAttribute("SPAGOBI.SECURITY.PORTAL-SECURITY-CLASS");
			if(secClassSB==null) throw new Exception("SPAGOBI.SECURITY.PORTAL-SECURITY-CLASS tag not Found");
			SourceBean secClassConfigSB = (SourceBean)secClassSB.getAttribute("CONFIG");
			if(secClassConfigSB==null) throw new Exception("SPAGOBI.SECURITY.PORTAL-SECURITY-CLASS.CONFIG tag not Found");
        	String portalSecurityProviderClass = (String) secClassSB.getAttribute("className");
        	if(portalSecurityProviderClass==null) throw new Exception("Attribute className of the SPAGOBI.SECURITY.PORTAL-SECURITY-CLASS tag not Found");
        	portalSecurityProviderClass = portalSecurityProviderClass.trim();
        	Class secProvClass = Class.forName(portalSecurityProviderClass);
        	IPortalSecurityProvider portalSecurityProvider = (IPortalSecurityProvider)secProvClass.newInstance();
            // create the pattern for the role name filter
			SourceBean secFilterSB = (SourceBean)conf.getAttribute("SPAGOBI.SECURITY.ROLE-NAME-PATTERN-FILTER");
			if(secFilterSB==null) throw new Exception("SPAGOBI.SECURITY.ROLE-NAME-PATTERN-FILTER tag not Found");
			String rolePatternFilter = secFilterSB.getCharacters();
	        if(rolePatternFilter==null) throw new Exception("Role filter regular expression not found");
			Pattern pattern = Pattern.compile(rolePatternFilter);
	        Matcher matcher = null;
			// for each user checks if at least one of his roles is suitable for the filter
			Iterator iterUser = allUser.iterator();
			Iterator iterRoles = null;
			while(iterUser.hasNext()) {
				User user = (User)iterUser.next();
				String userName = user.getUserName();
				boolean allowed  = false;
				List roles = portalSecurityProvider.getUserRoles(userName, secClassConfigSB);
				iterRoles = roles.iterator();
				while(iterRoles.hasNext()) {
					Role role = (Role)iterRoles.next();
					String rolename = role.getName();
					matcher = pattern.matcher(rolename);
					if(matcher.find()){
						allowed  = true;	
						break;
					}
				}
				/*
				groups = groupHandler.findGroupsOfUser(userName);
				iterGroup = groups.iterator();
				while(iterGroup.hasNext()) {
					Group group = (Group)iterGroup.next();
					String groupID = group.getId();
					matcher = pattern.matcher(groupID);
					if(matcher.find()){
						allowed  = true;	
						break;
					}
				}
				*/
				if(allowed) {
					filteredUser.add(user);
				}
			}
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, Utilities.class.getName(), 
		                        "getExoUserFiltered", "Error while filter exo user list ", e);
			filteredUser = new ArrayList();
		}
		return filteredUser;
		
	}
	
}
