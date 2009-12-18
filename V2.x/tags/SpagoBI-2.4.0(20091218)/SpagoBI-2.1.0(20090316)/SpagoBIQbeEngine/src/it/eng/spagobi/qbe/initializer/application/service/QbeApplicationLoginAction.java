/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.qbe.initializer.application.service;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spago.error.EMFAbstractError;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.security.GenericLoginHandler;


// TODO: Auto-generated Javadoc
/**
 * The Class QbeApplicationLoginAction.
 * 
 * @author Andrea Zoppello
 * 
 * This action is responsible to put the contents of the query composed automatically
 * in the expert query contents
 */
public class QbeApplicationLoginAction extends AbstractAction {
	
		
	/**
	 * Service.
	 * 
	 * @param request the request
	 * @param response the response
	 * 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		EMFErrorHandler errorHandler = getErrorHandler();
		SessionContainer session = getRequestContainer().getSessionContainer();
		
		try{
			
			RequestContainer aRequestContainer = getRequestContainer();
			
			GenericLoginHandler handler = new GenericLoginHandler();
			handler.service(aRequestContainer, request, response);
			session.setAttribute("AUTHENTICATED", "TRUE");
			
		}catch(EMFAbstractError e){
			
			session.setAttribute("AUTHENTICATED", "FALSE");
		}
		
	}
}
