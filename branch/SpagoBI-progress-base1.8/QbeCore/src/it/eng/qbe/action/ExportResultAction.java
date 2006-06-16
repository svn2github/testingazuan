
package it.eng.qbe.action;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Gioia
 */
public class ExportResultAction extends AbstractAction {
	
	
	public void service(SourceBean request, SourceBean response) throws Exception{

		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		DataMartModel dataMartModel  = (DataMartModel)aSessionContainer.getAttribute("dataMartModel");
				
		
		//executeExpertQuery(aWizardObject, response, aSessionContainer);			
		//executeFinalQuery(aWizardObject, response, aSessionContainer);		
				
	}	
}
