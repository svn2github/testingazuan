/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.services.actions;

import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.constants.SecurityConstants;
import it.eng.spagobi.security.IUserProfileFactory;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.security.Principal;

import javax.portlet.PortletRequest;

/**
 * This class reads user from portal and traces information for this connected user; after it gets the
 * profile for the principal user and puts it into the permanent container.
 * 
 * @author Zoppello
 */
public class PortletLoginAction extends AbstractAction{

	/**
	 * @see it.eng.spago.dispatching.action.AbstractHttpAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		PortletRequest portletRequest = PortletUtilities.getPortletRequest(); 
		
		String remoteUser = portletRequest.getRemoteUser();
		
		SpagoBITracer.debug(SecurityConstants.NAME_MODULE, this.getClass().getName(),"service()", "USER CONNECTED IS [" + remoteUser+"]");
		
		
		Principal principal = portletRequest.getUserPrincipal();
		
		String engUserProfileFactoryClass =  ((SourceBean)ConfigSingleton.getInstance().getAttribute("SPAGOBI.SECURITY.USER-PROFILE-FACTORY-CLASS")).getCharacters();
		
		IUserProfileFactory engUserProfileFactory = (IUserProfileFactory)Class.forName(engUserProfileFactoryClass).newInstance();
		
		IEngUserProfile userProfile = engUserProfileFactory.createUserProfile(principal);
		
		getRequestContainer().getSessionContainer().getPermanentContainer().setAttribute(IEngUserProfile.ENG_USER_PROFILE, userProfile);
	}

}
