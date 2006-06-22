
package it.eng.qbe.action;

import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;

import java.util.Iterator;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to handle the modification of selected fields in the Field
 * Selection Tab, like apply operators, distinct selection, change of the alias of a field
 * 
 */
public class UpdateFieldsForSelectAction extends AbstractAction {
	
	/**
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		String className = (String) request.getAttribute("className");
		
		
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		
		
		ISelectClause selectClause = aWizardObject.getSelectClause();
		ISelectField selectField = null;
		
	
		if (selectClause != null){
			java.util.List l= selectClause.getSelectFields();
			Iterator it = l.iterator();
			String fieldId = null;
			
			
			while (it.hasNext()){
				selectField = (ISelectField)it.next();
			 	fieldId = selectField.getId();
			 	String newFieldName  =(String)request.getAttribute("NEW_FIELD_"+fieldId);
			 	String alias  =(String)request.getAttribute("ALIAS_FOR_"+fieldId);
			 	
			 	if (newFieldName != null){
			 		selectField.setFieldName(newFieldName);
			 	}//end if 	
			 	
			 	if ((alias != null) && (alias.trim().length() > 0)){
			 		selectField.setFieldAlias(alias);
			 	}
			}//end if
		}//end  if
		
		
		String distinct = (String)request.getAttribute("selectDistinct");
		if (distinct.equalsIgnoreCase("true")){
			aWizardObject.setDistinct(true);
		}else{
			aWizardObject.setDistinct(false);
		}
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		
	}
}
