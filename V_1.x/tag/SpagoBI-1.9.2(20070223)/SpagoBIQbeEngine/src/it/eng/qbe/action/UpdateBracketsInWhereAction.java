
package it.eng.qbe.action;

import java.util.Iterator;
import java.util.List;

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
 * This action move down of one position the field identified in request with FIELD_ID in the Where Clause  
 * of the object ISingleDataMartWizardObject in session
 */
public class UpdateBracketsInWhereAction extends AbstractAction {
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(aSessionContainer);
		
		String fieldId = (String)request.getAttribute("FIELD_ID"); 
		String side = (String)request.getAttribute("SIDE"); 
		String action = (String)request.getAttribute("ACTION"); 
		
		if(fieldId == null || side == null || action == null) return;
		
		IWhereClause aWhereClause = aWizardObject.getWhereClause();
		IWhereField whereField = null;
		if(aWhereClause != null) {
			List fileds = aWhereClause.getWhereFields();
			for(Iterator it = fileds.iterator(); it.hasNext(); ) {
				IWhereField field = (IWhereField)it.next();
				if(field.getId().equalsIgnoreCase(fieldId)) {
					whereField = field;
					break;
				}
			}
		}
		
		if(whereField == null) return;
		
		if(side.equalsIgnoreCase("LEFT")) {
			if(action.equalsIgnoreCase("ADD")) {
				whereField.setLeftBracketsNum(whereField.getLeftBracketsNum()+1);
			} else if (action.equalsIgnoreCase("REMOVE")) {
				whereField.setLeftBracketsNum(whereField.getLeftBracketsNum()-1);				
			}
		} else if (side.equalsIgnoreCase("RIGHT")) {
			if(action.equalsIgnoreCase("ADD")) {
				whereField.setRightBracketsNum(whereField.getRightBracketsNum()+1);
			} else if (action.equalsIgnoreCase("REMOVE")) {
				whereField.setRightBracketsNum(whereField.getRightBracketsNum()-1);				
			}
		}
						
		
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, Utils.getMainWizardObject(aSessionContainer));
	
		
	}
}
