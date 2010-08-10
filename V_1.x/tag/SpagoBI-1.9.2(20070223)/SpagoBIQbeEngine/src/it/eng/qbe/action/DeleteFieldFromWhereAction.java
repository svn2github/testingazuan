
package it.eng.qbe.action;

import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereClause;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.WhereFieldSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This action delete the field identified in request with FIELD_ID from Where Clause
 * of the object ISingleDataMartWizardObject in session
 */
public class DeleteFieldFromWhereAction extends AbstractAction {
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		
		ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(aSessionContainer);
		
		String fieldId = (String)request.getAttribute("FIELD_ID"); 
		
		
		IWhereClause aWhereClause = aWizardObject.getWhereClause();
		
		IWhereField whereField = new WhereFieldSourceBeanImpl();
		whereField.setId(fieldId);
		
		aWhereClause.delWhereField(whereField);
		
		if (aWhereClause.getWhereFields().size() == 0)
			aWizardObject.delWhereClause();
						
		String prefix = "a";
		 if (Utils.isSubQueryModeActive(aSessionContainer)){
				String subQueryFieldId = (String)aSessionContainer.getAttribute(WizardConstants.SUBQUERY_FIELD);
				prefix = Utils.getMainWizardObject(aSessionContainer).getSubQueryIdForSubQueryOnField(subQueryFieldId);
		 }
		 
	
		 aWizardObject.purgeNotReferredEntityClasses(prefix);
		
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, Utils.getMainWizardObject(aSessionContainer));
		
		
	
		
	}
}
