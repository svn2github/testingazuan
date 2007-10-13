package it.eng.spagobi.utilities;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.UserFunctionality;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.ILowFunctionalityDAO;
import it.eng.spagobi.constants.SpagoBIConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class UserUtilities {
	
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
			String username = (String)userProfile.getUserUniqueIdentifier();
			Collection roleStrs = userProfile.getRoles();
			Iterator roleIter = roleStrs.iterator();
			List roles = new ArrayList();
			while(roleIter.hasNext()) {
				String rolename = (String)roleIter.next();
				Role role = DAOFactory.getRoleDAO().loadByName(rolename);
				roles.add(role);
			}
			Role[] rolesArr = new Role[roles.size()];
			rolesArr = (Role[])roles.toArray(rolesArr);

			
			UserFunctionality userFunct = new UserFunctionality();
			userFunct.setCode("ufr_"+username);
			userFunct.setDescription("User Functionality Root");
			userFunct.setName("My Folder");
			userFunct.setPath("/"+username);
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
