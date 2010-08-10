
package it.eng.qbe.action;

import java.util.List;

import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.GroupByClauseSourceBeanImpl;
import it.eng.qbe.wizard.GroupByFieldSourceBeanImpl;
import it.eng.qbe.wizard.IGroupByClause;
import it.eng.qbe.wizard.IOrderByClause;
import it.eng.qbe.wizard.IOrderGroupByField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.OrderByClauseSourceBeanImpl;
import it.eng.qbe.wizard.OrderByFieldSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


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
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		
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
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, aWizardObject);
		
		
	}
}
