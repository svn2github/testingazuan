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
package it.eng.spagobi.wapp.services;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.container.ContextManager;
import it.eng.spagobi.container.SpagoBISessionContainer;
import it.eng.spagobi.container.strategy.LightNavigatorContextRetrieverStrategy;
import it.eng.spagobi.wapp.bo.Menu;

public class BeforeExecutionAction extends AbstractHttpAction{

	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
			throws Exception {
		
		String menuId=(String)serviceRequest.getAttribute("MENU_ID");
		RequestContainer requestContainer=RequestContainer.getRequestContainer();
		SessionContainer sessionContainer=requestContainer.getSessionContainer();
		ContextManager contextManager = new ContextManager(new SpagoBISessionContainer(sessionContainer), 
				new LightNavigatorContextRetrieverStrategy(serviceRequest));
		
		if(menuId!=null){
		
			Menu menu=DAOFactory.getMenuDAO().loadMenuByID(Integer.valueOf(menuId));
			boolean hideBar=menu.isHideExecBar();
			if(hideBar) contextManager.set("TOOLBAR_VISIBLE", Boolean.valueOf(false));
			serviceResponse.setAttribute("objectid", menu.getObjId().toString());
			//serviceResponse.setAttribute("menuid",menuId);
		}
	}

}
