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
package it.eng.spagobi.qbe.querybuilder.orderby.service;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;

import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * This Action is responsible to put a field in Order By  Clause of the object
 * ISingleDataMartWizardObject in session.
 */
public class AddOrderByFieldAction extends AbstractQbeEngineAction {

	// valid input parameter names
	/** The Constant FIELD_NAME. */
	public static final String FIELD_NAME = "COMPLETE_FIELD_NAME";
	
	/** The Constant FIELDS. */
	public static final String FIELDS = "field";
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws EngineException {
		super.service(request, response);	
		
		String fieldName = getAttributeAsString(FIELD_NAME); 
		List fields = request.getAttributeAsList(FIELDS);
		
		if(fieldName != null) {
			getQuery().addOrderByField(fieldName);
		}
		else {			
			getQuery().deleteOrderByClause();
			for(int i = 0; i < fields.size(); i++) {
				fieldName = (String)fields.get(i);
				getQuery().addOrderByField(fieldName);
			}	
		}		
		
		updateLastUpdateTimeStamp();
		setDatamartWizard( getDatamartWizard() );
	}
}
