
package it.eng.qbe.action;

import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.GroupByFieldSourceBeanImpl;
import it.eng.qbe.wizard.IGroupByClause;
import it.eng.qbe.wizard.IOrderGroupByField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This action delete the field identified in request with FIELD_ID from Group By Clause
 * of the object ISingleDataMartWizardObject in session
 */
public class DeleteFieldFromGroupByAction extends AbstractAction {
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		
		String fieldId = (String)request.getAttribute("FIELD_ID"); 
				
		
		IGroupByClause aGroupByClause = aWizardObject.getGroupByClause();
		
		IOrderGroupByField aOrderGroupByField = new GroupByFieldSourceBeanImpl();
		
		aOrderGroupByField.setId(fieldId);
		
		aGroupByClause.delGroupByField(aOrderGroupByField);
		
		if (aGroupByClause.getGroupByFields().size() <= 0)
			aWizardObject.setGroupByClause(null);
		
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, aWizardObject);
	
		
	}
}
