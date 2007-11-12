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
package it.eng.qbe.action;

import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This action is responsible to recover the Thread Context ClassLoader that was modified dynamically by QBE 
 * before exit QBE
 */
public class RecoverClassLoaderAction extends AbstractAction {
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		try{
			DataMartModel dm = (DataMartModel)getRequestContainer().getSessionContainer().getAttribute("dataMartModel");
			ClassLoader toRecoverClassLoader = (ClassLoader)ApplicationContainer.getInstance().getAttribute("CURRENT_THREAD_CONTEXT_LOADER"); 
			Logger.debug(RecoverClassLoaderAction.class, "Recovering ClassLoader " + toRecoverClassLoader.toString());
			if ( toRecoverClassLoader != null){
				Thread.currentThread().setContextClassLoader(toRecoverClassLoader);
			}
			String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");
			if (qbeMode.equalsIgnoreCase("PORTLET")){
				String actor = (String)getRequestContainer().getSessionContainer().getAttribute("ACTOR");
				response.setAttribute("PAGE", "TreeObjectsPage");
				response.setAttribute("ACTOR", actor);
			}else{
				response.setAttribute("ACTION", "DETAIL_DATA_MART_ACTION");
				response.setAttribute("PATH", dm.getName());
			}
		}catch (SourceBeanException sbe){
			sbe.printStackTrace();
		}
		
	}
}
