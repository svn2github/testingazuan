/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.security;

import java.util.List;
import org.apache.log4j.Logger;


public class UPRSSecurityInfoProviderImpl implements ISecurityInfoProvider {
	
    static private Logger logger = Logger.getLogger(UPRSSecurityInfoProviderImpl.class);
    
    
	/**
	 * Get all the roles from db.
	 * 
	 * @return List of the roles (list of it it.eng.spagobi.bo.Role)
	 */
	public List getRoles() {
	    logger.debug("IN");
		List roles = SecurityProviderUtilities.getAllRoles();	
		logger.debug("OUT");
		return roles;
	}
	


	/**
	 * Gets the list of names of all attributes of all profiles from db.
	 * 
	 * @return the list of names of all attributes of all profiles defined
	 */
	public List getAllProfileAttributesNames() {
		 logger.debug("IN");
		List attributes = SecurityProviderUtilities.getAllProfileAttributesNames();	
		logger.debug("OUT");
		return attributes;
	}
	
}
