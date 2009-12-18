package it.eng.spagobi.wapp.services;

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

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.AdmintoolsConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.services.LoginModule;
import it.eng.spagobi.commons.utilities.messages.MessageBuilder;
import it.eng.spagobi.wapp.bo.Menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

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

public class ChangeLanguage extends AbstractHttpAction{

	static private Logger logger = Logger.getLogger(ChangeLanguage.class);
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
	
    UserProfile userProfile = null;
	
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
	throws Exception {
		logger.debug("IN");
		RequestContainer reqCont = RequestContainer.getRequestContainer();
		SessionContainer sessCont = reqCont.getSessionContainer();
		SessionContainer permSess = sessCont.getPermanentContainer();

		Locale locale = MessageBuilder.getBrowserLocaleFromSpago();		

		String language=(String)serviceRequest.getAttribute("language_id");
		String country=(String)serviceRequest.getAttribute("country_id");
		logger.debug("language selected: "+language);
		IEngUserProfile profile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		userProfile=null;
		String lang="";
		if (profile  instanceof UserProfile) {
			userProfile = (UserProfile) profile;
		}

		
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		List languages=spagoconfig.getAttributeAsList("SPAGOBI.LANGUAGE_SUPPORTED.LANGUAGE");

		if(language==null){
			logger.error("language not specified");
		}
		else{
			Iterator iter = languages.iterator();
			boolean found=false;
			while (iter.hasNext() && found==false) {
				SourceBean lang_sb = (SourceBean) iter.next();
				String lang_supported = (String) lang_sb.getAttribute("language");
				String country_supported= (String) lang_sb.getAttribute("country");

				if(language.equalsIgnoreCase(lang_supported) && (country==null || country.equalsIgnoreCase(country_supported))){

					locale=new Locale(language,country,"");
					permSess.setAttribute("AF_LANGUAGE", locale.getLanguage());
					permSess.setAttribute("AF_COUNTRY", locale.getCountry());   

					if(userProfile!=null){
						userProfile.setAttributeValue(SpagoBIConstants.LANGUAGE, language);
						userProfile.setAttributeValue(SpagoBIConstants.COUNTRY, country);
						logger.debug("modified profile attribute to "+ lang);
					}
					else{
						logger.error("profile attribute not modified to "+ lang);				
					}
					found=true;
				}
			}
		}

		getMenuItems(serviceRequest, serviceResponse);
		
		serviceResponse.setAttribute("MENU_MODE", "ALL_TOP");
		serviceResponse.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "home");
		logger.debug("OUT");
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
	private void getMenuItems(SourceBean request, SourceBean response) throws EMFUserError {
		try {	
			List lstFinalMenu = new ArrayList();
			// get config
			SourceBean configSingleton = (SourceBean)ConfigSingleton.getInstance();

			//if the user is a final user, the menu is created and putted into the response with other informations like the type of layout,
			//otherwise don't, administrators, developers, testers, behavioral model administrators have they own pre-configured menu
			if (!userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)  // for administrators
					&& !userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV)  // for developers
					&& !userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_TEST)  // for testers
					&& !userProfile.isAbleToExecuteAction(SpagoBIConstants.PARAMETER_MANAGEMENT)){  // for behavioral model administrators
				Collection lstRolesForUser = userProfile.getRoles();
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
			params.put(AdmintoolsConstants.PAGE, LoginModule.MODULE_PAGE);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 500, new Vector(), params);
		}
	}
	
	
	
	
	/**
	 * Check if the menu element in input is already presents into the list
	 * @param lst the list to check
	 * @param menu the element to check
	 * @return true if the element is already presents, false otherwise
	 */
	private boolean containsMenu(List lst, Menu menu){
		if (lst == null)
			return false;
		
		for (int i=0; i<lst.size(); i++){
			Menu tmpMenu = (Menu)lst.get(i);
			if (tmpMenu.getMenuId().intValue() == menu.getMenuId().intValue())
				return true;	
		}
		return false;
	}
    /**
     * Finds the user identifier from http request or from SSO system (by the http request in input).
     * Use the SsoServiceInterface for read the userId in all cases, if SSO is disabled use FakeSsoService.
     * Check spagobi_sso.xml
     * 
     * @param httpRequest The http request
     * @param serviceRequest the service request
     * 
     * @return the current user unique identified
     * 
     * @throws Exception in case the SSO is enabled and the user identifier specified on service request is different from the SSO detected one.
     */
    private static String findUserId(SourceBean serviceRequest, HttpServletRequest httpRequest) throws Exception {
    	logger.debug("IN");
    	String userId = null;
    	try {
    		// Get userid from request
	    	Object requestUserIdObj = serviceRequest.getAttribute("userid");
	    	if (requestUserIdObj != null) userId = requestUserIdObj.toString();    	
    	} finally {
    		logger.debug("OUT: userId = [" + userId + "]");
    	}
    	return userId;
    }	
	
	
	
	
}



