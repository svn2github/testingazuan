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
package it.eng.spagobi.qbe.querybuilder.groupby.service;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;


// TODO: Auto-generated Javadoc
/**
 * This action delete the field identified in request with FIELD_ID from Group By Clause
 * of the object ISingleDataMartWizardObject in session.
 */
public class DeleteGroupByFieldAction extends AbstractQbeEngineAction {

	// valid input parameter names
	/** The Constant FIELD_ID. */
	public static final String FIELD_ID = "FIELD_ID";
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws EngineException {
		super.service(request, response);	
		
		String fieldId = getAttributeAsString(FIELD_ID);
		
		getQuery().deleteGroupByField(fieldId);
		
		updateLastUpdateTimeStamp();
		setDatamartWizard( getDatamartWizard() );
	}
}
