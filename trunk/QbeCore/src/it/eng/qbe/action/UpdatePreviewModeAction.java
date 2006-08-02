
package it.eng.qbe.action;

import it.eng.qbe.utility.Logger;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to change the execuption mode of the Preview Tab of the ISingleDataMartWizardObject
 * 
 */
public class UpdatePreviewModeAction extends AbstractAction {
	
	/**
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)aSessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		
		String source = (String)request.getAttribute("SOURCE");
		String expertQueryDisplayed = (String)request.getAttribute("EXPERT_DISPLAYED");
		
		if (source.equalsIgnoreCase("RADIO_BUTTON")){
			String previewMode = (String)request.getAttribute("previewMode"); 
			
			if (previewMode.equalsIgnoreCase("ExpertMode")){
				
				aWizardObject.setUseExpertedVersion(true);
							
			}else{
				
				aWizardObject.setUseExpertedVersion(false);
				
			}	
			
			aWizardObject.setExpertQueryDisplayed(expertQueryDisplayed);
			
		} else if (source.equalsIgnoreCase("RESUME_LAST_EXPERT_LINK")){
			
			String expertQuerySaved = aWizardObject.getExpertQuerySaved();
			
			aWizardObject.setExpertQueryDisplayed(expertQuerySaved);
			
			
		} else Logger.debug(UpdatePreviewModeAction.class,"ERRORE in UpdatePreviewModeAction - la source non è prevista");
		
		
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, aWizardObject);
			
	}
}
