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
import it.eng.qbe.wizard.GroupByClauseSourceBeanImpl;
import it.eng.qbe.wizard.GroupByFieldSourceBeanImpl;
import it.eng.qbe.wizard.IGroupByClause;
import it.eng.qbe.wizard.IOrderGroupByField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;

import java.util.List;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to put a field in Group By Clause of the object ISingleDataMartWizardObject in session
 * 
 */
public class SelectFieldForGroupByAction extends AbstractAction {
	
	
	/**
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
	
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(aSessionContainer);
		
		String aliasedFieldName = (String)request.getAttribute("COMPLETE_FIELD_NAME"); 
		if(aliasedFieldName != null) {
			IGroupByClause aGroupByClause = aWizardObject.getGroupByClause();
			if ( aGroupByClause == null){
				aGroupByClause = new GroupByClauseSourceBeanImpl();
			}
			
			IOrderGroupByField aGroupByField = new GroupByFieldSourceBeanImpl();
			aGroupByField.setFieldName(aliasedFieldName);
			aGroupByClause.addGroupByField(aGroupByField);
			aWizardObject.setGroupByClause(aGroupByClause);
		}
		else {
			List list = request.getAttributeAsList("field");
			aWizardObject.setGroupByClause(null);
			for(int i = 0; i < list.size(); i++) {
				aliasedFieldName = (String)list.get(i);
				IGroupByClause aGroupByClause = aWizardObject.getGroupByClause();
				if ( aGroupByClause == null){
					aGroupByClause = new GroupByClauseSourceBeanImpl();
				}
				
				IOrderGroupByField aGroupByField = new GroupByFieldSourceBeanImpl();
				aGroupByField.setFieldName(aliasedFieldName);
				aGroupByClause.addGroupByField(aGroupByField);
				aWizardObject.setGroupByClause(aGroupByClause);
			}	
		}
		
		
		
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, Utils.getMainWizardObject(aSessionContainer));
		
		
	}
}
