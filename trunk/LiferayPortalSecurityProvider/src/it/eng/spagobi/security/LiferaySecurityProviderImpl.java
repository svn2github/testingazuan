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
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.security.util.csvreader.CSVReader;
import it.eng.spagobi.security.util.csvreader.CSVRow;
import it.eng.spagobi.security.util.csvreader.ICSVReader;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleServiceUtil;
import com.liferay.portal.service.UserServiceUtil;

/**
 * Implements the IPortalSecurityProvider interface defining method to get the
 * system and user roles.
 */
public class LiferaySecurityProviderImpl implements IPortalSecurityProvider {

	/**
	 * Get all the portal roles
	 * 
	 * @return List of the portal roles (list of it.eng.spagobi.bo.Role)
	 */
	public List getRoles() {
		SpagoBITracer.debug("UTILITIES", "LiferaySecurityProviderImpl",
				"getRoles()", "IN");
		ICSVReader reader = new CSVReader();
		List groups = new ArrayList();
		List comuniTmp = reader.readElement("groups.csv");
		Iterator iter = comuniTmp.iterator();
		while (iter.hasNext()) {
			CSVRow row = (CSVRow) iter.next();
			String nome = row.getElement(0);
			String descrizione = row.getElement(1);
			groups.add(new Role(nome, descrizione));
			SpagoBITracer.debug("UTILITIES", "LiferaySecurityProviderImpl",
					"getRoles()", "AGGIUNTO GRUPPO:" + nome);
		}

		SpagoBITracer.debug("UTILITIES", "LiferaySecurityProviderImpl",
				"getRoles()", "OUT");
		return groups;
	}

	/**
	 * Get the list of the user roles. If the user doesn't exist the roles list
	 * is empty
	 * 
	 * @param user
	 *            Username
	 * @param passwd
	 *            Password of the user
	 * @return List of user roles
	 */
	public List getUserRoles(String userName, SourceBean passwd) {
		SpagoBITracer.debug("UTILITIES", "LiferaySecurityProviderImpl",
				"getUserRoles()", "IN");
		ArrayList roles=new ArrayList();
		try {
			User user = UserServiceUtil.getUserById(userName);
			if (user!=null){
				List ruoli = RoleServiceUtil.getUserRoles(user.getUserId());
				if (ruoli != null) {
					Iterator iter = ruoli.iterator();
					while (iter.hasNext()) {
						com.liferay.portal.model.Role ruolo = (com.liferay.portal.model.Role) iter.next();
						SpagoBITracer.debug("UTILITIES", "LiferayUserProfileImpl",
								"LiferayUserProfileImpl()", "ruolo.getName()="
										+ ruolo.getName());
						roles.add(ruolo.getName());
					}
				}
			}

		} catch (SystemException e) {
			SpagoBITracer.critical("UTILITIES", "LiferaySecurityProviderImpl", "getUserRoles()", "SystemException",e);
		} catch (RemoteException e) {
			SpagoBITracer.critical("UTILITIES", "LiferaySecurityProviderImpl", "getUserRoles()", "RemoteException",e);
		} catch (PortalException e) {
			SpagoBITracer.critical("UTILITIES", "LiferaySecurityProviderImpl", "getUserRoles()", "PortalException",e);
		}
		SpagoBITracer.debug("UTILITIES", "LiferaySecurityProviderImpl",
				"getUserRoles()", "OUT");		
		return roles;
	}

	public List getAllProfileAttributesNames() {

		SpagoBITracer.debug("UTILITIES", "LiferaySecurityProviderImpl",
				"getAllProfileAttributesNames()", "IN");

		List toReturn = null;
		try {
			toReturn = SecurityProviderUtilities.getAllProfileAtributesNames();
		} catch (EMFInternalError e) {
			SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		               			   "getAllProfileAttributesNames()",
		               			   "Error while retrieving the list of all profile attributes names", e);
			return new ArrayList();
		}
		SpagoBITracer.debug("UTILITIES", "LiferaySecurityProviderImpl",
				"getAllProfileAttributesNames()", "OUT");		
		return toReturn;		
	}

	/**
	 * Authenticate a user
	 * 
	 * @param userName
	 *            the username
	 * @param password
	 *            bytes of the password, certificate, ...
	 * @return true if the user is autheticated false otherwise
	 */
	public boolean authenticateUser(String userName, byte[] password) {
		// NEVER CALLED BECAUSE AUTHENTICATION IS DONE BY THE PORTAL
		SpagoBITracer.info("UTILITIES", "LiferaySecurityProviderImpl",
				"authenticateUser()", "NOT IMPLEMENTED");
		return false;
	}

}
