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
package it.eng.spagobi.commons.utilities;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.UserFunctionality;
import it.eng.spagobi.analiticalmodel.functionalitytree.dao.ILowFunctionalityDAO;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.services.PortletLoginAction;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

public class UserUtilities {

    static Logger logger = Logger.getLogger(UserUtilities.class);

    /**
     * Gets the user profile.
     * 
     * @return the user profile
     * 
     * @throws Exception the exception
     */
    public static IEngUserProfile getUserProfile() throws Exception {
	RequestContainer aRequestContainer = RequestContainer.getRequestContainer();
	SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
	SessionContainer permanentSession = aSessionContainer.getPermanentContainer();

	IEngUserProfile userProfile = (IEngUserProfile) permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);

	if (userProfile == null) {

	    String userId = null;
	    PortletRequest portletRequest = PortletUtilities.getPortletRequest();
	    Principal principal = portletRequest.getUserPrincipal();
	    userId = principal.getName();
	    logger.debug("got userId from Principal=" + userId);

	    ISecurityServiceSupplier supplier = SecurityServiceSupplierFactory.createISecurityServiceSupplier();
	    SpagoBIUserProfile user = null;
	    try {
		user = supplier.createUserProfile(userId);
		userProfile = new UserProfile(user);
	    } catch (Exception e) {
		logger.error("Reading user information... ERROR", e);
		throw new SecurityException();
	    }

	    logger.debug("userProfile created.UserID= " + (String) userProfile.getUserUniqueIdentifier());
	    logger.debug("Attributes name of the user profile: " + userProfile.getUserAttributeNames());
	    logger.debug("Functionalities of the user profile: " + userProfile.getFunctionalities());
	    logger.debug("Roles of the user profile: " + userProfile.getRoles());

	    permanentSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, userProfile);

	   // String username = (String) userProfile.getUserUniqueIdentifier();
	    String username = (String) user.getUserId();
	    if (!UserUtilities.userFunctionalityRootExists(username)) {
		UserUtilities.createUserFunctionalityRoot(userProfile);
	    }

	}

	return userProfile;
    }

    /**
     * User functionality root exists.
     * 
     * @param username the username
     * 
     * @return true, if successful
     * 
     * @throws Exception the exception
     */
    public static boolean userFunctionalityRootExists(String username) throws Exception {
	boolean exists = false;
	try {
		logger.debug("****  username checked: " + username);
	    ILowFunctionalityDAO functdao = DAOFactory.getLowFunctionalityDAO();
	    exists = functdao.checkUserRootExists(username);
	} catch (Exception e) {
	    SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, UserUtilities.class.getName(),
		    "userFunctionalityRootExists", "Error while checking user functionality root existence", e);
	    throw new Exception("Unable to check user functionality existence", e);
	}
	return exists;
    }

    /**
     * Creates the user functionality root.
     * 
     * @param userProfile the user profile
     * 
     * @throws Exception the exception
     */
    public static void createUserFunctionalityRoot(IEngUserProfile userProfile) throws Exception {
	try {
	    String username = (String) ((UserProfile)userProfile).getUserId();
	    logger.debug("username: " + username);
	    Collection roleStrs = userProfile.getRoles();
	    Iterator roleIter = roleStrs.iterator();
	    List roles = new ArrayList();
	    while (roleIter.hasNext()) {
		String rolename = (String) roleIter.next();
		Role role = DAOFactory.getRoleDAO().loadByName(rolename);
		roles.add(role);
	    }
	    Role[] rolesArr = new Role[roles.size()];
	    rolesArr = (Role[]) roles.toArray(rolesArr);

	    UserFunctionality userFunct = new UserFunctionality();
	    userFunct.setCode("ufr_" + username);
	    userFunct.setDescription("User Functionality Root");
	    userFunct.setName(username);
	    userFunct.setPath("/" + username);
	    userFunct.setExecRoles(rolesArr);
	    ILowFunctionalityDAO functdao = DAOFactory.getLowFunctionalityDAO();
	    functdao.insertUserFunctionality(userFunct);
	} catch (Exception e) {
	    SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, UserUtilities.class.getName(),
		    "createUserFunctionalityRoot", "Error while creating user functionality root", e);
	    throw new Exception("Unable to create user functionality root", e);
	}
    }

}
