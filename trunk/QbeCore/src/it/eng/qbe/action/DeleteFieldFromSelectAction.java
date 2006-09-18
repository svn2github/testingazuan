
package it.eng.qbe.action;

import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.GroupByFieldSourceBeanImpl;
import it.eng.qbe.wizard.IGroupByClause;
import it.eng.qbe.wizard.IOrderByClause;
import it.eng.qbe.wizard.IOrderGroupByField;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.OrderByFieldSourceBeanImpl;
import it.eng.qbe.wizard.SelectFieldSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;

import java.util.List;


/**
 * @author Andrea Zoppello
 * 
 * This action delete the field identified in request with FIELD_ID from Select Clause
 * of the object ISingleDataMartWizardObject in session
 */
public class DeleteFieldFromSelectAction extends AbstractAction {
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
	
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		
		ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(aSessionContainer);
		
		String fieldId = (String)request.getAttribute("FIELD_ID"); 
		
		
		ISelectClause aSelectClause = aWizardObject.getSelectClause();
		
		ISelectField selectField = new SelectFieldSourceBeanImpl();
		selectField.setId(fieldId);
		
		List selectFields = aSelectClause.getSelectFields();
		
		ISelectField tmpSelectField = null;
		boolean found = false;
		for (int i=0; i < selectFields.size(); i++){
			tmpSelectField = (ISelectField)selectFields.get(i);
			if (tmpSelectField.getId().equalsIgnoreCase(selectField.getId())){
				found = true;
				break;
			}
		}
		
		if ((tmpSelectField != null) && found){
		
			//Devo rimuovere il campo anche nelle eventuali condizioni di Order By
			
			IOrderByClause orderByClause = aWizardObject.getOrderByClause();
			if (orderByClause != null){
				List orderByFields = orderByClause.getOrderByFields();
				IOrderGroupByField aOrderGroupByField = null;
				IOrderGroupByField toEliminate = null;
				
				for (int i=0; i < orderByFields.size(); i++){
					aOrderGroupByField = (IOrderGroupByField)orderByFields.get(i);
					if (aOrderGroupByField.getFieldName().equalsIgnoreCase(tmpSelectField.getFieldName())){
						toEliminate = new OrderByFieldSourceBeanImpl();
						toEliminate.setId(aOrderGroupByField.getId());
					}
				}
				
				if (toEliminate != null){
					orderByClause.delOrderByField(toEliminate);
				}
				if (orderByClause.getOrderByFields().size() == 0)
					aWizardObject.delOrderByClause();
			}
		
		
		
			//Devo rimuovere il campo anche nelle eventuali condizioni di Order By
		
			IGroupByClause groupByClause = aWizardObject.getGroupByClause();
			if (groupByClause != null){
				List groupByFields = groupByClause.getGroupByFields();
				IOrderGroupByField aOrderGroupByField = null;
				IOrderGroupByField toEliminate = null;
				for (int i=0; i < groupByFields.size(); i++){
					aOrderGroupByField = (IOrderGroupByField)groupByFields.get(i);
					if (aOrderGroupByField.getFieldName().equalsIgnoreCase(tmpSelectField.getFieldName())){
						toEliminate = new GroupByFieldSourceBeanImpl();
						toEliminate.setId(aOrderGroupByField.getId());
					}
				}
				
				if (toEliminate != null){
					groupByClause.delGroupByField(toEliminate);
				}
				
				if (groupByClause.getGroupByFields().size() == 0)
					aWizardObject.delGroupByClause();
			}
		
		
			aSelectClause.delSelectField(selectField);
			if (aSelectClause.getSelectFields().size() == 0)
				aWizardObject.delSelectClause();
		}
		
		aWizardObject.purgeNotReferredEntityClasses();
		
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, Utils.getMainWizardObject(aSessionContainer));
	
		
	}
}
