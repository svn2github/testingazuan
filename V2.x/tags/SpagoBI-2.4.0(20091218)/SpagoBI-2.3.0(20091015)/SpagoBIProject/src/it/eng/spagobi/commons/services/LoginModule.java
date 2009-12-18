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
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.UserUtilities;
import it.eng.spagobi.commons.utilities.messages.MessageBuilder;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;
import it.eng.spagobi.wapp.services.ChangeTheme;
import it.eng.spagobi.wapp.util.MenuUtilities;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class LoginModule extends AbstractHttpModule {

    static Logger logger = Logger.getLogger(LoginModule.class);

    
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
	    

		String theme_name=(String)request.getAttribute(ChangeTheme.THEME_NAME);
		logger.debug("theme selected: "+theme_name);

		
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
		
		// Set THEME
		if (theme_name!=null && theme_name.length()>0){
			permSess.setAttribute(SpagoBIConstants.THEME, theme_name);
		}
		
		// Set BACK URL if present
		String backUrl=(String)request.getAttribute(SpagoBIConstants.BACK_URL);
		
		if (backUrl!=null && !backUrl.equalsIgnoreCase("")){
			//permSess.setAttribute(SpagoBIConstants.BACK_URL, backUrl);
			httpSession.setAttribute(SpagoBIConstants.BACK_URL, backUrl);		
		}
		
		if (request.getAttribute("MESSAGE") != null && ((String)request.getAttribute("MESSAGE")).equalsIgnoreCase("START_LOGIN")){
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "login");
			logger.debug("OUT");
			return;
		}
		errorHandler = getErrorHandler();
		

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

		MenuUtilities.getMenuItems(request, response, profile);
		// fill response attributes
		if(userId.equals("chiron")) {
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "chiron");
		} else {
			Collection functionalities = profile.getFunctionalities();
			boolean docAdmin = false;
			boolean docDev = false;
			boolean docTest = false;
			if (functionalities!=null && !functionalities.isEmpty()){
				docAdmin = functionalities.contains("DocumentAdministration")|| functionalities.contains("DocumentAdminManagement");
			 	docDev = functionalities.contains("DocumentDevManagement");
			 	docTest = functionalities.contains("DocumentTestManagement");
			}
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "userhome");
		}
		
		logger.debug("OUT");		
	}
	



}
