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

import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to instantiate the datamart where
 * 
 * working with qbe
 */
public class ExitSubQueryAction extends AbstractAction {

	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {		
        RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
				
		ISingleDataMartWizardObject mainQuery = Utils.getMainWizardObject(aSessionContainer);
		ISingleDataMartWizardObject subQuery = Utils.getWizardObject(aSessionContainer);
		String fieldId = (String)aSessionContainer.getAttribute(WizardConstants.SUBQUERY_FIELD);
		
		String exitStatus = (String)request.getAttribute("SAVE");
		if(exitStatus != null && exitStatus.equalsIgnoreCase("TRUE")) {
			mainQuery.addSubQueryOnField(fieldId, subQuery);
		}
		
		
		aSessionContainer.delAttribute(WizardConstants.QUERY_MODE);
		aSessionContainer.delAttribute(WizardConstants.SUBQUERY_FIELD);
		
	}
}
