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
package it.eng.spagobi.qbe.queryinspector.service;

import it.eng.qbe.log.Logger;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to change the execuption mode of the Preview Tab of the ISingleDataMartWizardObject
 * 
 */
public class UpdatePreviewModeAction extends AbstractQbeEngineAction {
	
	// valid input parameter names
	public static final String SOURCE = "SOURCE";
	public static final String EXPERT_DISPLAYED = "EXPERT_DISPLAYED";
	public static final String PREVIEW_MODE = "previewMode";
	
	public void service(SourceBean request, SourceBean response) {
		super.service(request, response);
		
		
		String source = getAttributeAsString(SOURCE);
		String expertQueryDisplayed = getAttributeAsString(EXPERT_DISPLAYED);
		String previewMode = (String)request.getAttribute(PREVIEW_MODE);
		
		if (source.equalsIgnoreCase("RADIO_BUTTON")){
			if (previewMode.equalsIgnoreCase("ExpertMode")){
				
				getDatamartWizard().setUseExpertedVersion(true);
							
			} else {
				
				getDatamartWizard().setUseExpertedVersion(false);
				
			}	
			
			getDatamartWizard().setExpertQueryDisplayed(expertQueryDisplayed);
			
		} else if (source.equalsIgnoreCase("RESUME_LAST_EXPERT_LINK")){
			
			String expertQuerySaved = getDatamartWizard().getExpertQuerySaved();
			
			getDatamartWizard().setExpertQueryDisplayed(expertQuerySaved);
			
			
		} else {
			Logger.debug(UpdatePreviewModeAction.class,"ERRORE in UpdatePreviewModeAction - la source non è prevista");
		}
		
		setDatamartWizard( getDatamartWizard() );	
	}
}
