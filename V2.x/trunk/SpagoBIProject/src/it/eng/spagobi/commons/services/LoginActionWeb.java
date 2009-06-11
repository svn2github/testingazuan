package it.eng.spagobi.commons.services;

import it.eng.spago.base.Constants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
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
import it.eng.spagobi.wapp.util.MenuUtilities;

import java.io.OutputStream;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class LoginActionWeb extends AbstractHttpAction {

	static Logger logger = Logger.getLogger(LoginActionWeb.class);
	IEngUserProfile profile = null;

	
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		logger.debug("IN");

		ServletOutputStream os=getHttpResponse().getOutputStream();
		freezeHttpResponse();

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
					os.print("KO");
					os.flush();
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
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "home");
		}

		
		//login succesfull
		os.print("OK");
		os.flush();
		logger.debug("OUT");		

	}
	}


