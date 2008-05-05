/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.qbe.queryinspector.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;

// TODO: Auto-generated Javadoc
/**
 * The Class UpdateFieldsForResumeAction.
 * 
 * @author Scarel Luca
 * 
 * This Action is responsable to handle the modification of the expert query displayed text area in the Resume Query Tab
 */
public class UpdateFieldsForResumeAction extends AbstractQbeEngineAction {
	
	// valid input parameter names
	/** The Constant IP_NEXT_ACTION. */
	public static final String IP_NEXT_ACTION = "NEXT_ACTION";
	
	/** The Constant IP_INEXT_PUBLISHER. */
	public static final String IP_INEXT_PUBLISHER = "NEXT_PUBLISHER";
	
	/** The Constant EXPERT_QUERY_DISPLAYED. */
	public static final String EXPERT_QUERY_DISPLAYED = "expertDisplayedForUpdate";
	
	// valid ouput parameter names
	/** The Constant OP_NEXT_ACTION. */
	public static final String OP_NEXT_ACTION = IP_NEXT_ACTION;
	
	/** The Constant OP_INEXT_PUBLISHER. */
	public static final String OP_INEXT_PUBLISHER = IP_INEXT_PUBLISHER;
		
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws EngineException {
		super.service(request, response);
		
						
		String nextAction = getAttributeAsString(IP_NEXT_ACTION);
		String nextPublisher = getAttributeAsString(IP_INEXT_PUBLISHER);		
		String expertQueryDisplayed = getAttributeAsString(EXPERT_QUERY_DISPLAYED);		
		
		if ((expertQueryDisplayed != null)&&(!expertQueryDisplayed.equalsIgnoreCase(""))){
			getDatamartWizard().setExpertQueryDisplayed(expertQueryDisplayed);
			
			updateLastUpdateTimeStamp();
			setDatamartWizard( getDatamartWizard() );
		}		
		
		try{
			response.setAttribute(OP_NEXT_ACTION, nextAction);		
			response.setAttribute(OP_INEXT_PUBLISHER, nextPublisher);
		}catch(SourceBeanException sbe){
			sbe.printStackTrace();
		}		
	}
}
