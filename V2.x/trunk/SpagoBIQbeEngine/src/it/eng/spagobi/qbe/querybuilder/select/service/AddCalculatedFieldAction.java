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
package it.eng.spagobi.qbe.querybuilder.select.service;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;


public class AddCalculatedFieldAction extends AbstractQbeEngineAction {
	
	// valid input parameter names
	public static final String CLASS_NAME = "CLASS_NAME";
	public static final String CFIELD_ID = "CFIELD_ID";
	public static final String CFIELD_COMPLETE_NAME = "CFIELD_COMPLETE_NAME";

	
	public void service(SourceBean request, SourceBean response) {	
		super.service(request, response);
		
		String fieldId = getAttributeAsString(CFIELD_ID);
		String fieldNameInQuery = getAttributeAsString(CFIELD_COMPLETE_NAME);
		String classNameInQuery = getAttributeAsString(CLASS_NAME);
		
		
		getMainQuery().addCalculatedField(fieldId, fieldNameInQuery, classNameInQuery,  
				getDatamartModel().getFormula(), getDatamartModel().getDataSource());
		
		
		updateLastUpdateTimeStamp();
		setMainDataMartWizard( getMainDataMartWizard() );
	}
		
	
	
}
