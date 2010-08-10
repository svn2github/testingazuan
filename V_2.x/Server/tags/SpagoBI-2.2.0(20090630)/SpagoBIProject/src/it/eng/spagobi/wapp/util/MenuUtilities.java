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

package it.eng.spagobi.wapp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.constants.AdmintoolsConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.services.LoginModule;
import it.eng.spagobi.wapp.bo.Menu;

public class MenuUtilities {

	Menu parent=null;
	
	static Logger logger = Logger.getLogger(MenuUtilities.class);
	
    public static final String MODULE_PAGE = "LoginPage";
    public static final String DEFAULT_LAYOUT_MODE = "ALL_TOP";
    public static final String LAYOUT_ALL_TOP = "ALL_TOP";
    public static final String LAYOUT_ALL_LEFT = "ALL_LEFT";
    public static final String LAYOUT_TOP_LEFT = "TOP_LEFT";
    public static final String LAYOUT_ADMIN_MENU = "ADMIN_MENU";
    public static final String DEFAULT_EXTRA = "NO";
    public static final String MENU_MODE = "MENU_MODE";
    public static final String MENU_EXTRA = "MENU_EXTRA";
    public static final String LIST_MENU = "LIST_MENU";
	
	
	public static String getMenuPath(Menu menu) {
		try{
		if(menu.getParentId()==null){
			return menu.getName();
		}
		else{
		Menu parent=DAOFactory.getMenuDAO().loadMenuByID(menu.getParentId());		
		return getMenuPath(parent)+" > "+menu.getName();
		}
		}
		catch (Exception e) {
			return "";
		}
	}
	
	
	
	/**
	 * Gets the elements of menu relative by the user logged. It reaches the role from the request and 
	 * asks to the DB all detail
	 * menu information, by calling the method <code>loadMenuByRoleId</code>.
	 *   
	 * @param request The request Source Bean
	 * @param response The response Source Bean
	 * @throws EMFUserError If an exception occurs
	 */   
	public static void getMenuItems(SourceBean request, SourceBean response, IEngUserProfile profile) throws EMFUserError {
		try {	
			List lstFinalMenu = new ArrayList();
			// get config
			SourceBean configSingleton = (SourceBean)ConfigSingleton.getInstance();

			//if the user is a final user, the menu is created and putted into the response with other informations like the type of layout,
			//otherwise don't, administrators, developers, testers, behavioral model administrators have they own pre-configured menu
			if (!profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)  // for administrators
					&& !profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV)  // for developers
					&& !profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_TEST)  // for testers
					&& !profile.isAbleToExecuteAction(SpagoBIConstants.PARAMETER_MANAGEMENT)){  // for behavioral model administrators
				Collection lstRolesForUser = profile.getRoles();
				logger.debug("** Roles for user: " + lstRolesForUser.size());
				Object[] arrRoles = lstRolesForUser.toArray();
				for (int i=0; i< arrRoles.length; i++){
					logger.debug("*** arrRoles[i]): " + arrRoles[i]);
					Role role = (Role)DAOFactory.getRoleDAO().loadByName((String)arrRoles[i]);
					if (role != null){
						List lstMenuItems  = DAOFactory.getMenuRolesDAO().loadMenuByRoleId(role.getId());
						if (lstMenuItems == null)
							logger.debug("Not found menu items for Role " + (String)arrRoles[i] );
						else {
							for(int j=0; j<lstMenuItems.size(); j++){
								Menu tmpObj = (Menu)lstMenuItems.get(j);
								if (!containsMenu(lstFinalMenu, tmpObj)){						
									lstFinalMenu.add((Menu)lstMenuItems.get(j));	
								}
							}
						}
					}
					else
						logger.debug("Role " + (String)arrRoles[i] + " not found on db");
				}
				response.setAttribute(LIST_MENU, lstFinalMenu);
			}	
			logger.debug("List Menu Size " + lstFinalMenu.size());
			//String menuMode = (configSingleton.getAttribute("SPAGOBI.MENU.mode")==null)?DEFAULT_LAYOUT_MODE:(String)configSingleton.getAttribute("SPAGOBI.MENU.mode");
			//response.setAttribute(MENU_MODE, menuMode);
			response.setAttribute(MENU_MODE, DEFAULT_LAYOUT_MODE);
			
		} catch (Exception ex) {
			logger.error("Cannot fill response container" + ex.getLocalizedMessage());	
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE, MODULE_PAGE);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 500, new Vector(), params);
		}
	}
	
	/**
	 * Check if the menu element in input is already presents into the list
	 * @param lst the list to check
	 * @param menu the element to check
	 * @return true if the element is already presents, false otherwise
	 */
	private static boolean containsMenu(List lst, Menu menu){
		if (lst == null)
			return false;
		
		for (int i=0; i<lst.size(); i++){
			Menu tmpMenu = (Menu)lst.get(i);
			if (tmpMenu.getMenuId().intValue() == menu.getMenuId().intValue())
				return true;	
		}
		return false;
	}
	
}
