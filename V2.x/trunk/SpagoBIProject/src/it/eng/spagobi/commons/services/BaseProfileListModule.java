/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.commons.services;

import javax.servlet.http.HttpServletRequest;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import org.apache.log4j.Logger;

/**
 * @author Zerbetto (davide.zerbetto@eng.it)
 */

public abstract class BaseProfileListModule extends AbstractBasicListModule {

    static Logger logger = Logger.getLogger(BaseProfileListModule.class);

    /* (non-Javadoc)
     * @see it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
     */
    public void service(SourceBean request, SourceBean response) throws Exception {
		logger.debug("IN");
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) this.getRequestContainer().getInternalRequest();
			String userId = GeneralUtilities.findUserId(request, httpRequest);
			logger.debug("User id = " + userId);
			// in case the user is not specified, does nothing
			if (userId == null || userId.trim().equals("")) {
				logger.warn("User identifier not found.");
			} else {
				RequestContainer reqCont = RequestContainer.getRequestContainer();
				SessionContainer sessCont = reqCont.getSessionContainer();
				SessionContainer permSess = sessCont.getPermanentContainer();
				IEngUserProfile profile = (IEngUserProfile) permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
				if (profile == null) {
					logger.debug("User profile not found in session, creating a new one and putting in session....");
					// in case the profile does not exist, creates a new one
					profile = GeneralUtilities.createNewUserProfile(userId);
					permSess.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
				} else {
					// in case the profile is different, creates a new one and overwrites the existing
					if (!profile.getUserUniqueIdentifier().toString().equals(userId)) {
						logger.debug("Different user profile found in session, creating a new one and replacing in session....");
						profile = GeneralUtilities.createNewUserProfile(userId);
						permSess.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
					}
				}
			}
		} finally {
			logger.debug("OUT");
		}
		super.service(request, response);
    }

}
