
package it.eng.qbe.action;

import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This action force the composition of the query on the query object (instance of ISingleDataMartWizardObject) 
 * that is in SessionContainer.
 * 
 * After the execution of this action you must obtain the query composed with qbe with ISingleDataMartWizardObject.getFinalQuery()
 */
public class ComposeResumeQueryAction extends AbstractAction {
	
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		try{
			
			RequestContainer aRequestContainer = getRequestContainer();
			SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
			
			ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(aSessionContainer);
			aWizardObject.composeQuery();
			
			Logger.debug(ISingleDataMartWizardObject.class,"LA QUERY FINALE DEL WIZARD "+ aWizardObject.getFinalQuery());
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
