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
/*
 * Created on 21-apr-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.commons.services;

import it.eng.spago.base.Constants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractHttpModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.AdmintoolsConstants;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.UserUtilities;
import it.eng.spagobi.commons.utilities.messages.MessageBuilder;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;
import it.eng.spagobi.wapp.bo.Menu;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class LoginModule extends AbstractHttpModule {

    static Logger logger = Logger.getLogger(LoginModule.class);
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
    
    IEngUserProfile profile = null;
    EMFErrorHandler errorHandler = null;
	
	/**
	 * Service.
	 * 
	 * @param request the request
	 * @param response the response
	 * 
	 * @throws Exception the exception
	 * 
	 * @see it.eng.spago.dispatching.action.AbstractHttpAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
	    logger.debug("IN");
	    
    	ConfigSingleton serverConfig = ConfigSingleton.getInstance();
    	SourceBean validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.ACTIVE");
    	String activeStr = (String) validateSB.getCharacters();
    	boolean activeSoo=false;
    	if (activeStr != null && activeStr.equalsIgnoreCase("true")) {
    		activeSoo=true;
    	}
		RequestContainer reqCont = RequestContainer.getRequestContainer();
		SessionContainer sessCont = reqCont.getSessionContainer();
		SessionContainer permSess = sessCont.getPermanentContainer();
		
			HttpServletRequest servletRequest=getHttpRequest();
		HttpSession httpSession=servletRequest.getSession();
		
		if (request.getAttribute("MESSAGE") != null && ((String)request.getAttribute("MESSAGE")).equalsIgnoreCase("START_LOGIN")){
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "login");
			logger.debug("OUT");
			return;
		}
		errorHandler = getErrorHandler();
		
				// Set BACK URL if present
		String backUrl=(String)request.getAttribute(SpagoBIConstants.BACK_URL);
		
		if (backUrl!=null && !backUrl.equalsIgnoreCase("")){
			//permSess.setAttribute(SpagoBIConstants.BACK_URL, backUrl);
			httpSession.setAttribute(SpagoBIConstants.BACK_URL, backUrl);		
		}
		
		

		String userId=null;
		if (!activeSoo){
			userId = (String)request.getAttribute("userID");
			logger.debug("userID="+userId);
			if (userId == null) {
				logger.error("User identifier not found. Cannot build user profile object");
				throw new SecurityException("User identifier not found.");
			}			
		}else{
			userId=UserUtilities.getUserId(this.getHttpRequest());
		}

			ISecurityServiceSupplier supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
	    	// If SSO is not active, check username and password, i.e. performs the authentication;
	    	// instead, if SSO is active, the authentication mechanism is provided by the SSO itself, so SpagoBI does not make 
	    	// any authentication, just creates the user profile object and puts it into Spago permanent container
	    	if (!activeSoo) {
				String pwd=(String)request.getAttribute("password");       
		        try {
		        	Object ris=supplier.checkAuthentication(userId, pwd);
		        	if (ris==null){
		        		logger.error("pwd uncorrect");
		            	EMFUserError emfu = new EMFUserError(EMFErrorSeverity.ERROR, 501);
		    			errorHandler.addError(emfu); 		    	
		    			return;
		        	}
		        } catch (Exception e) {
		            logger.error("Reading user information... ERROR");
		            throw new SecurityException("Reading user information... ERROR",e);
		        }
	    	}
	        
	        try {
	        	profile=UserUtilities.getUserProfile(userId);
	            if (profile == null){		            	
	            	logger.error("user not created");
	            	EMFUserError emfu = new EMFUserError(EMFErrorSeverity.ERROR, 501);
	    			errorHandler.addError(emfu); 		    	
	    			return;
	            }
	           
	            // put user profile into session
	            permSess.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
	    		// updates locale information on permanent container for Spago messages mechanism
	    		Locale locale = MessageBuilder.getBrowserLocaleFromSpago();
	    		if (locale != null) {
	    			permSess.setAttribute(Constants.USER_LANGUAGE, locale.getLanguage());
	    			permSess.setAttribute(Constants.USER_COUNTRY, locale.getCountry());
	    		}
	        } catch (Exception e) {
	            logger.error("Reading user information... ERROR");
	            throw new SecurityException("Reading user information... ERROR",e);
	        }
	        
			//String username = (String) profile.getUserUniqueIdentifier();
	        String username = (String) ((UserProfile)profile).getUserId();
			if (!UserUtilities.userFunctionalityRootExists(username)) {
			    logger.debug("funcitonality root not yet exists for "+username);	
				//UserUtilities.createUserFunctionalityRoot(profile);
			}
			else{
			    logger.debug("funcitonality root already exists for "+username);					
			}

		getMenuItems(request, response);
		// fill response attributes
		if(userId.equals("chiron")) {
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "chiron");
		} else {
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "home");
		}
		
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


}
