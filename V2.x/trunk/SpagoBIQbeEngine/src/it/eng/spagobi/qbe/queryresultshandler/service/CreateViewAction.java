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
package it.eng.spagobi.qbe.queryresultshandler.service;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.Constants;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;

import java.io.IOException;



// TODO: Auto-generated Javadoc
/**
 * The Class CreateViewAction.
 * 
 * @author Andrea Gioia
 */
public class CreateViewAction extends AbstractQbeEngineAction {
	
	/**
	 * Gets the session container.
	 * 
	 * @return the session container
	 */
	private SessionContainer getSessionContainer() {
		return getRequestContainer().getSessionContainer();
	}
	
	/**
	 * Gets the data mart wizard.
	 * 
	 * @return the data mart wizard
	 */
	private ISingleDataMartWizardObject getDataMartWizard() {
		return getDatamartWizard();
	}
	
	/**
	 * Gets the data mart model.
	 * 
	 * @return the data mart model
	 */
	private DataMartModel getDataMartModel() {
		return (DataMartModel)getSessionContainer().getAttribute("dataMartModel");
	}
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response)  {				
		String viewName = (String)request.getAttribute("VIEW_NAME");
		
		getResponseContainer().setAttribute(Constants.HTTP_RESPONSE_FREEZED, Boolean.TRUE);
		
		try {
			getDataMartModel().addView(viewName, getDataMartWizard());
			getHttpResponse().getWriter().write("OK");	
		} catch (Throwable t) {
			try {
				getHttpResponse().getWriter().write("KO" + t.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t.printStackTrace();
		}
		
			
	}
}
