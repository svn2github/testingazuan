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

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to update the content of exepertQuery with the value
 * of the text area input by the user identified by the field expertSelectTA
 * 
 */
public class UpdateExpertAction extends AbstractQbeEngineAction {

	// valid input parameter names
	public static final String EXPERT_STRING = "expertSelectTA";
	
	
	public void service(SourceBean request, SourceBean response) throws EngineException {
		super.service(request, response);
		
		String expertString = getAttributeAsString(EXPERT_STRING); 
				
		getDatamartWizard().setExpertQuerySaved(expertString);
		getDatamartWizard().setExpertQueryDisplayed(expertString);
		
		updateLastUpdateTimeStamp();
		setDatamartWizard( getDatamartWizard() );
		
	}
}
