
package it.eng.qbe.action;

import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This action is responsible to put the contents of the query composed automatically
 * in the expert query contents
 */
public class AlignExpertAction extends AbstractAction {
	
		
	/** 
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		try{
			
			RequestContainer aRequestContainer = getRequestContainer();
			SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
			ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
			aWizardObject.composeQuery();
			aWizardObject.setExpertQueryDisplayed(aWizardObject.getFinalQuery());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
