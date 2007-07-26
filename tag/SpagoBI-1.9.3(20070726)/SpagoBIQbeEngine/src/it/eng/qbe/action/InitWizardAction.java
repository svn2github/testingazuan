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

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Zoppello
 * 
 * This is the starting action for QBE it provide to initialize the ISingleDataMartWizardObject
 * provided with qbe and to put in the session
 * 
 * If QBE is asked to run with an exisisting composed query identified in request with queryId
 * the object ISingleDataMartWizardObject is initialized with this query data, otherwise a new object is
 * created.
 */
public class InitWizardAction extends AbstractAction {
	
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		
		if (aWizardObject != null){
			aSessionContainer.delAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		}
		
		
		String queryId = (String)request.getAttribute("queryId");
		
		DataMartModel dataMart = null;
		if (queryId == null){
			aWizardObject = new SingleDataMartWizardObjectSourceBeanImpl();
		} else {
			dataMart = (DataMartModel)aSessionContainer.getAttribute("dataMartModel");
			aWizardObject = dataMart.getQuery(queryId);
		}
		
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, aWizardObject);
		
		
		
	}
}
