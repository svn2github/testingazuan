
package it.eng.qbe.action;

import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;

/**
 * @author Scarel Luca
 * 
 * This Action is responsable to handle the modification of the expert query displayed text area in the Resume Query Tab 
 * 
 */
public class UpdateFieldsForResumeAction extends AbstractAction {
	
	/**
	 * 
	 */
	
	public void service(SourceBean request, SourceBean response) {
		
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(aSessionContainer);
		
						
		String nextAction = (String)request.getAttribute("NEXT_ACTION");
		String nextPublisher = (String)request.getAttribute("NEXT_PUBLISHER");
		
		String expertQueryDisplayed = (String)request.getAttribute("expertDisplayedForUpdate");
		
		
		try{
			response.setAttribute("NEXT_ACTION", nextAction);		
			response.setAttribute("NEXT_PUBLISHER", nextPublisher);
		}catch(SourceBeanException sbe){
			sbe.printStackTrace();
		}
		
		
		if ((expertQueryDisplayed != null)&&(!expertQueryDisplayed.equalsIgnoreCase(""))){
			aWizardObject.setExpertQueryDisplayed(expertQueryDisplayed);
			
			Utils.updateLastUpdateTimeStamp(getRequestContainer());
			aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, Utils.getMainWizardObject(aSessionContainer));		
		}		
		
		
	}
}
