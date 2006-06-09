
package it.eng.qbe.action;

import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereClause;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.SelectFieldSourceBeanImpl;
import it.eng.qbe.wizard.WhereFieldSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This action move down of one position the field identified in request with FIELD_ID in the Where Clause  
 * of the object ISingleDataMartWizardObject in session
 */
public class MoveDownWhereAction extends AbstractAction {
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		
		String fieldId = (String)request.getAttribute("FIELD_ID"); 
		
		
		IWhereClause aWhereClause = aWizardObject.getWhereClause();
		
		IWhereField whereField = new WhereFieldSourceBeanImpl();
		whereField.setId(fieldId);
		
		aWhereClause.moveDown(whereField);
		
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, aWizardObject);
	
		
	}
}
