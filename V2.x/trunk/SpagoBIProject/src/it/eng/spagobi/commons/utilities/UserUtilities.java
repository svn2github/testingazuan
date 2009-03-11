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
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.UserFunctionality;
import it.eng.spagobi.analiticalmodel.functionalitytree.dao.ILowFunctionalityDAO;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.services.common.SsoServiceFactory;
import it.eng.spagobi.services.common.SsoServiceInterface;
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
import javax.servlet.http.HttpServletRequest;

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
		user.setFunctions(readFunctionality(user.getRoles()));
		userProfile = new UserProfile(user);
	    } catch (Exception e) {
	    	logger.error("An error occured while retrieving user profile for user[" + userId +"]");
	    	throw new SecurityException("An error occured while retrieving user profile for user[" + userId +"]", e);
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
    
    public static String getUserId(HttpServletRequest req){
    	logger.debug("IN");
    	SsoServiceInterface userProxy = SsoServiceFactory.createProxyService();
		String userId = userProxy.readUserIdentifier(req);
	    logger.debug("OUT,userId:"+userId);
	    return userId;
    }
    
    public static IEngUserProfile getUserProfile(HttpServletRequest req) throws Exception {
    	logger.debug("IN");
    	SsoServiceInterface userProxy = SsoServiceFactory.createProxyService();
		String userId = userProxy.readUserIdentifier(req);
		
	    ISecurityServiceSupplier supplier = SecurityServiceSupplierFactory.createISecurityServiceSupplier();
	    try {
		SpagoBIUserProfile user = supplier.createUserProfile(userId);
		user.setFunctions(readFunctionality(user.getRoles()));
		return new UserProfile(user);
	    } catch (Exception e) {
	    	logger.error("Exception while creating user profile");
			throw new SecurityException("Exception while creating user profile", e);
	    }finally{
	    	logger.debug("OUT");
	    }
    }
    public static IEngUserProfile getUserProfile(String userId) throws Exception {
    	logger.debug("IN");
	
	    ISecurityServiceSupplier supplier = SecurityServiceSupplierFactory.createISecurityServiceSupplier();
	    try {
		SpagoBIUserProfile user = supplier.createUserProfile(userId);
		user.setFunctions(readFunctionality(user.getRoles()));
		return new UserProfile(user);
	    } catch (Exception e) {
	    	logger.error("Exception while creating user profile");
			throw new SecurityException("Exception while creating user profile", e);
	    }finally{
	    	logger.debug("OUT");
	    }
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


    public static String[] readFunctionality(String[] roles) {
	logger.debug("IN");
	try {
	    it.eng.spagobi.commons.dao.IUserFunctionalityDAO dao = DAOFactory.getUserFunctionalityDAO();
	    String[] functionalities = dao.readUserFunctionality(roles);
	    logger.debug("Functionalities retrieved: " + functionalities == null ? "" : functionalities.toString());
	    if (isAbleToSaveSubObjects(roles)) {
		logger.debug("Adding save subobject functionality...");
		String[] newFunctionalities = new String[functionalities.length + 1];
		for (int i = 0; i < functionalities.length; i++) {
		    newFunctionalities[i] = functionalities[i];
		}
		newFunctionalities[newFunctionalities.length - 1] = SpagoBIConstants.SAVE_SUBOBJECT_FUNCTIONALITY;
		functionalities = newFunctionalities;
	    }
	    return functionalities;
	} catch (EMFUserError e) {
	    logger.error("EMFUserError", e);
	} catch (Exception e) {
	    logger.error("Exception", e);
	} finally {
	    logger.debug("OUT");
	}
	return null;
    }

    private static boolean isAbleToSaveSubObjects(String[] roles) {
	logger.debug("IN");
	boolean isAbleToSaveSubObjects = false;
	try {
	    if (roles != null && roles.length > 0) {
		for (int i = 0; i < roles.length; i++) {
		    String roleName = roles[i];
		    Role role = DAOFactory.getRoleDAO().loadByName(roleName);
		    if (role.isAbleToSaveSubobjects()) {
			logger.debug("User has role " + roleName + " that is able to save subobjects.");
			isAbleToSaveSubObjects = true;
			break;
		    }
		}
	    }
	} catch (EMFUserError e) {
	    logger.error(e);
	} finally {
	    logger.debug("OUT");
	}
	return isAbleToSaveSubObjects;
    }
    

}
