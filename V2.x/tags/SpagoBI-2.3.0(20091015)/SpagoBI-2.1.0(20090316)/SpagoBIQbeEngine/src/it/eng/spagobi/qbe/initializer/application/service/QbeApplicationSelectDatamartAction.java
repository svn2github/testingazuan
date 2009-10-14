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

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


// TODO: Auto-generated Javadoc
/**
 * The Class QbeApplicationSelectDatamartAction.
 * 
 * @author Andrea Zoppello
 * 
 * This Action is responsible to instantiate the datamart where
 * 
 * working with qbe
 */
public class QbeApplicationSelectDatamartAction extends AbstractAction {

	/**
	 * Builds an empty datamart just to retrive all the datamarts defined and saved
	 * TODO buid a proper class to do this job and build the datamart only once when
	 * all the data are in place.
	 * 
	 * @param request the request
	 * @param response the response
	 * 
	 * @throws Exception the exception
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
			
		/*
		String path = (String)request.getAttribute("PATH");		
		IDataSource dataSource = new HibernateDataSource(path, null, null);
		DataMartModel dataMartModel = new DataMartModel(dataSource);
		getRequestContainer().getSessionContainer().setAttribute("dataMartModel", dataMartModel);
		*/
		
		String dmName = (String)request.getAttribute("PATH");
		response.setAttribute("DM_NAME", dmName);
	}
}
