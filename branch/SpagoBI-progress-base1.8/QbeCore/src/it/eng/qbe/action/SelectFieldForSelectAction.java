
package it.eng.qbe.action;

import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SelectClauseSourceBeanImpl;
import it.eng.qbe.wizard.SelectFieldSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to put a field in Select  Clause of the object ISingleDataMartWizardObject in session
 */
public class SelectFieldForSelectAction extends AbstractAction {
	
	/**
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
	
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		
		
		
		
		String className = (String)request.getAttribute("CLASS_NAME");
		String classAlias = (String)request.getAttribute("ALIAS_CLASS_NAME");
		
		EntityClass ec = new EntityClass(className, classAlias);
		if (!aWizardObject.containEntityClass(ec)){
			aWizardObject.addEntityClass(ec);
		}
		
		String aliasedFieldName = (String)request.getAttribute("COMPLETE_FIELD_NAME"); 
		
		String alias = (String)request.getAttribute("ALIAS_FIELD_NAME");
		
		ISelectClause aSelectClause = aWizardObject.getSelectClause();
		if ( aSelectClause == null){
			aSelectClause = new SelectClauseSourceBeanImpl();
		}
		
		ISelectField aSelectField = new SelectFieldSourceBeanImpl();
		aSelectField.setFieldName(aliasedFieldName);
		aSelectField.setFieldAlias(alias);
		aSelectField.setFieldEntityClass(ec);
		aSelectClause.addSelectField(aSelectField);
		aWizardObject.setSelectClause(aSelectClause);
		
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, aWizardObject);
		
		
	}
}
