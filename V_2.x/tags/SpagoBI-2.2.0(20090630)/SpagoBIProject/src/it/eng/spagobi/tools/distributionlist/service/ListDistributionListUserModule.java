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
package it.eng.spagobi.tools.distributionlist.service;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.services.DelegatedHibernateConnectionListService;
import it.eng.spagobi.commons.utilities.UserUtilities;
import it.eng.spagobi.services.common.SsoServiceInterface;

import java.util.Collection;
import java.util.Iterator;

/**
 * Loads the Distributionlist List
 */
public class ListDistributionListUserModule extends AbstractBasicListModule{
	
	public static final String MODULE_PAGE = "ListDistributionListUserPage";

	/**
	 * Gets the list.
	 * 
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 * 
	 * @return ListIFace
	 * 
	 * @throws Exception the exception
	 */
	
	public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
	    
	        RequestContainer aRequestContainer = RequestContainer.getRequestContainer();
	        SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
	        SessionContainer permanentSession = aSessionContainer.getPermanentContainer();
	        /*
		IEngUserProfile userProfile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		*/
	    	IEngUserProfile userProfile =UserUtilities.getUserProfile();
		String userId="";
		if (userProfile!=null) userId=(String)((UserProfile)userProfile).getUserId();
		//sets the userid as input parameter for the query fo statements.xml
		aSessionContainer.setAttribute(SsoServiceInterface.USER_ID ,userId);
		

		Collection c = userProfile.getRoles();
		Iterator i = c.iterator();
		int j = 0;
		while (i.hasNext()){
			String roles = (String)i.next();
			aSessionContainer.setAttribute("role"+j,roles);
			j++ ;
		}
			while (j<6){
				String s= "/";
				aSessionContainer.setAttribute("role"+j,s);
				j++ ;
			}
		
		return DelegatedHibernateConnectionListService.getList(this, request, response);
	} 


}
