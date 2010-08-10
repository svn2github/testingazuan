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

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spagobi.services.commons.DelegatedHibernateConnectionListService;
import it.eng.spagobi.utilities.SpagoBITracer;

/**
 * Loads the parameters lookup list
 * 
 * @author Zerbetto
 */

public class ListLookupParametersModule extends AbstractBasicListModule {
	
	public static final String MODULE_PAGE = "ParametersLookupPage";
	/**
	 * Class Constructor
	 *
	 */
	public ListLookupParametersModule() {
		super();
	} 
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		String message = (String) request.getAttribute("MESSAGEDET");		
		if(message != null && message.equalsIgnoreCase("EXIT_FROM_MODULE")) {
			String returnState = (String) request.getAttribute("RETURN_STATE");
			response.setAttribute("PUBLISHER_NAME",  "ReturnBackPublisher");			
			RequestContainer requestContainer = this.getRequestContainer();	
			SessionContainer session = requestContainer.getSessionContainer();
			session.setAttribute("RETURN_FROM_MODULE", "ListLookupParametersModule");
			session.setAttribute("RETURN_STATUS", returnState);
			if(returnState.equalsIgnoreCase("SELECT"))
				session.setAttribute("PAR_ID", request.getAttribute("PAR_ID"));	
			
			return;
		}
				
		super.service(request, response); 
		
		response.setAttribute("PUBLISHER_NAME",  "ParametersLookupPublisher");
		SpagoBITracer.debug(MODULE_PAGE, "ListLookupParametersModule","service",
				"PUBLISHER_NAME = " + "ParametersLookupPublisher");
	}
	
	/**
	 * Gets the list
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 * @return ListIFace 
	 */
	public ListIFace getList(SourceBean request, SourceBean response) throws Exception {
		return DelegatedHibernateConnectionListService.getList(this, request, response);
	}
	
}
