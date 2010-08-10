/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.qbe.action;

import it.eng.qbe.log.Logger;
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
		ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(aSessionContainer);
		
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
		
		
		aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, Utils.getMainWizardObject(aSessionContainer));
			
	}
}
