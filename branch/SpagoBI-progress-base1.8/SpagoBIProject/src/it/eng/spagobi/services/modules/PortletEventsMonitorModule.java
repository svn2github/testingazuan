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
package it.eng.spagobi.services.modules;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.events.EventsManager;
import it.eng.spagobi.security.IUserProfileFactory;
import it.eng.spagobi.utilities.PortletUtilities;

import java.security.Principal;
import java.util.List;

import javax.portlet.PortletRequest;

/**
 * This class shows events' notification log 
 * 
 * @author Gioia
 *
 */			 
public class PortletEventsMonitorModule extends AbstractModule{
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		System.out.println(">>> ExecuteBIObjectModule <<<");
		
		PortletRequest portletRequest = PortletUtilities.getPortletRequest(); 
		Principal principal = portletRequest.getUserPrincipal();
		String engUserProfileFactoryClass =  ((SourceBean)ConfigSingleton.getInstance().getAttribute("SPAGOBI.SECURITY.USER-PROFILE-FACTORY-CLASS")).getCharacters();
		IUserProfileFactory engUserProfileFactory = (IUserProfileFactory)Class.forName(engUserProfileFactoryClass).newInstance();
		IEngUserProfile userProfile = engUserProfileFactory.createUserProfile(portletRequest, principal);
		String user = (String)userProfile.getUserUniqueIdentifier();
		
		EventsManager eventsManager = EventsManager.getInstance();		
		List firedEventsList = eventsManager.getFiredEvents(user);
		response.setAttribute("firedEventsList", firedEventsList);	
	}
}
