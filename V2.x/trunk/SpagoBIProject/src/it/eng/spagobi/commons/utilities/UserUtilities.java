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
	    try {
		SpagoBIUserProfile user = supplier.createUserProfile(userId);
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

	    String username = (String) userProfile.getUserUniqueIdentifier();
	    if (!UserUtilities.userFunctionalityRootExists(username)) {
		UserUtilities.createUserFunctionalityRoot(userProfile);
	    }

	}

	return userProfile;
    }

    public static boolean userFunctionalityRootExists(String username) throws Exception {
	boolean exists = false;
	try {
	    ILowFunctionalityDAO functdao = DAOFactory.getLowFunctionalityDAO();
	    exists = functdao.checkUserRootExists(username);
	} catch (Exception e) {
	    SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, UserUtilities.class.getName(),
		    "userFunctionalityRootExists", "Error while checking user functionality root existence", e);
	    throw new Exception("Unable to check user functionality existence", e);
	}
	return exists;
    }

    public static void createUserFunctionalityRoot(IEngUserProfile userProfile) throws Exception {
	try {
	    String username = (String) userProfile.getUserUniqueIdentifier();
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
