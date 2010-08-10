
package it.eng.qbe.action;

import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereClause;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.WhereClauseSourceBeanImpl;
import it.eng.qbe.wizard.WhereFieldSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to put a field in Where Clause of the object ISingleDataMartWizardObject in session
 * 
 */
public class SelectFieldForWhereAction extends AbstractAction {
	
	/**
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
	
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		
		
		String aliasedFieldName = (String)request.getAttribute("COMPLETE_FIELD_NAME"); 
		String hibFieldType= (String)request.getAttribute("HIB_TYPE"); 
		
		IWhereClause aWhereClause = aWizardObject.getWhereClause();
		if ( aWhereClause == null){
			aWhereClause = new WhereClauseSourceBeanImpl();
		}
		
		IWhereField aWhereField = new WhereFieldSourceBeanImpl();
		aWhereField.setFieldName(aliasedFieldName);
		aWhereField.setFieldOperator("=");
		aWhereField.setFieldValue("");
		aWhereField.setHibernateType(hibFieldType);
		aWhereField.setNextBooleanOperator("AND");
		aWhereClause.addWhereField(aWhereField);
		
		aWizardObject.setWhereClause(aWhereClause);
		
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, aWizardObject);
		
		
	}
}
