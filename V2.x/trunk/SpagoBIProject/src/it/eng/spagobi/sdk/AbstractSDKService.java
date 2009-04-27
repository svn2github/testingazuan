package it.eng.spagobi.sdk;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.utilities.UserUtilities;

import org.apache.axis.MessageContext;
import org.apache.log4j.Logger;
import org.apache.ws.security.handler.WSHandlerConstants;

public class AbstractSDKService {

	static private Logger logger = Logger.getLogger(AbstractSDKService.class);
	
	protected IEngUserProfile getUserProfile() throws Exception {
		logger.debug("IN");
		IEngUserProfile profile = null;
		try {
			MessageContext mc = MessageContext.getCurrentContext();
			profile = (IEngUserProfile) mc.getProperty(IEngUserProfile.ENG_USER_PROFILE);
			if (profile == null) {
				logger.debug("User profile not found.");
				String userIdentifier = (String) mc.getProperty(WSHandlerConstants.USER);
				logger.debug("User identifier found = [" + userIdentifier + "].");
				if (userIdentifier == null) {
					logger.warn("User identifier not found!! cannot build user profile object");
				} else {
					try {
						profile = UserUtilities.getUserProfile(userIdentifier);
						logger.debug("User profile for userId [" + userIdentifier + "] created.");
					} catch (Exception e) {
						logger.error("Exception creating user profile for userId [" + userIdentifier + "]!", e);
						throw new Exception("Cannot create user profile");
					}
				}
				mc.setProperty(IEngUserProfile.ENG_USER_PROFILE, profile);
			} else {
				logger.debug("User profile retrieved.");
			}
		} finally {
			logger.debug("OUT");
		}
		return profile;
	}
	
}
