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

import it.eng.qbe.query.ISelectField;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;

import java.util.Iterator;


/**
 * This Action is responsible to handle the modification of selected fields in the Field
 * Selection Tab, like apply operators, distinct selection, change of the alias of a field
 * 
 */
public class UpdateFieldsForSelectAction extends AbstractQbeEngineAction {

	// valid input parameter names
	public static final String NEW_FIELD_PREFIX = "NEW_FIELD_";
	public static final String ALIAS_FOR_PREFIX = "ALIAS_FOR_";
	public static final String DISTINCT = "selectDistinct";
	
	
	public void service(SourceBean request, SourceBean response) {
		super.service(request, response);	
		
		boolean distinct = getAttributeAsBoolean(DISTINCT);
		
		ISelectField selectField = null;
		
		Iterator it = getMainQuery().getSelectFieldsIterator();
		String fieldId = null;			
			
		while (it.hasNext()){
			selectField = (ISelectField)it.next();
		 	fieldId = selectField.getId();
		 	String newFieldName  =(String)request.getAttribute(NEW_FIELD_PREFIX + fieldId);
		 	String alias  =(String)request.getAttribute(ALIAS_FOR_PREFIX + fieldId);
		 	
		 	if (newFieldName != null){
		 		selectField.setFieldName(newFieldName);
		 	}	
		 	
		 	if ((alias != null) && (alias.trim().length() > 0)){
		 		selectField.setFieldAlias(alias);
		 	}
		}		
		
		getMainQuery().setDistinct( distinct );
		
		updateLastUpdateTimeStamp();
		
	}
}
