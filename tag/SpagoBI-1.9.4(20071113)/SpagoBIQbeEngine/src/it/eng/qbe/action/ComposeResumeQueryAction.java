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
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This action force the composition of the query on the query object (instance of ISingleDataMartWizardObject) 
 * that is in SessionContainer.
 * 
 * After the execution of this action you must obtain the query composed with qbe with ISingleDataMartWizardObject.getFinalQuery()
 */
public class ComposeResumeQueryAction extends AbstractAction {
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		try{
			
			RequestContainer aRequestContainer = getRequestContainer();
			SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
			DataMartModel dm = (DataMartModel)aSessionContainer.getAttribute("dataMartModel"); 
		    
			ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(aSessionContainer);
			aWizardObject.composeQuery(dm);
			
			Logger.debug(ISingleDataMartWizardObject.class,"LA QUERY FINALE DEL WIZARD "+ aWizardObject.getFinalQuery());
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
