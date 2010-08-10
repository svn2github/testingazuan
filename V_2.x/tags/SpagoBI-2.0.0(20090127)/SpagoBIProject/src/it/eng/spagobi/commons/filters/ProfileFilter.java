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
package it.eng.spagobi.commons.filters;

import it.eng.spago.base.Constants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.utilities.GeneralUtilities;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * @author Zerbetto (davide.zerbetto@eng.it)
 * 
 * This filter tries to build the user profile object, using the user identifier
 * returned by
 * <code>GeneralUtilities.findUserId((HttpServletRequest) request);</code>
 */

public class ProfileFilter implements Filter {

    private static transient Logger logger = Logger.getLogger(ProfileFilter.class);

    public void destroy() {
	// do nothing
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	    ServletException {
	logger.debug("IN");
	try {
	    if (request instanceof HttpServletRequest) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession();
		String userId = GeneralUtilities.findUserId(httpRequest);
		logger.debug("User id = " + userId);
		// in case the user is not specified, does nothing
		if (userId == null || userId.trim().equals("")) {
		    logger.warn("User identifier not found.");
		} else {
		    // looking if a RequestContainer already exists in session
		    RequestContainer requestContainer = (RequestContainer) session
			    .getAttribute(Constants.REQUEST_CONTAINER);
		    if (requestContainer == null) {
			// RequestContainer does not exists yet (maybe it is the
			// first call to Spago)
			// initializing Spago objects (user profile object must
			// be put into PermanentContainer)
			requestContainer = new RequestContainer();
			SessionContainer sessionContainer = new SessionContainer(true);
			requestContainer.setSessionContainer(sessionContainer);
			session.setAttribute(Constants.REQUEST_CONTAINER, requestContainer);
		    }
		    SessionContainer sessionContainer = requestContainer.getSessionContainer();
		    SessionContainer permanentSession = sessionContainer.getPermanentContainer();
		    IEngUserProfile profile = (IEngUserProfile) permanentSession
			    .getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		    if (profile == null) {
			logger.debug("User profile not found in session, creating a new one and putting in session....");
			// in case the profile does not exist, creates a new one
			profile = GeneralUtilities.createNewUserProfile(userId);
			permanentSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
		    } else {
			// in case the profile is different, creates a new one
			// and overwrites the existing
			if (!((UserProfile) profile).getUserUniqueIdentifier().toString().equals(userId)) {
			    logger.debug("Different user profile found in session, creating a new one and replacing in session....");
			    profile = GeneralUtilities.createNewUserProfile(userId);
			    permanentSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
			} else {
			    logger.debug("User profile object for user [" + userId
				    + "] already existing in session, ok");
			}
		    }
		}
	    }
	} catch (Exception e) {
	    logger.error(e);
	} finally {
	    logger.debug("OUT");
	    chain.doFilter(request, response);
	}
    }

    public void init(FilterConfig config) throws ServletException {
	// do nothing
    }

}
