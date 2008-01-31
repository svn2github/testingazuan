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
package it.eng.spagobi.qbe.querybuilder.where.service;

import it.eng.qbe.query.IWhereField;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;

import java.util.Iterator;


/**
 * This action move down of one position the field identified in request with FIELD_ID in the Where Clause  
 * of the object ISingleDataMartWizardObject in session
 */
public class UpdateBracketsInWhereAction extends AbstractQbeEngineAction {
	
	// valid input parameter names
	public static final String FIELD_ID = "FIELD_ID";
	public static final String SIDE = "SIDE";
	public static final String ACTION = "ACTION";
	
	public void service(SourceBean request, SourceBean response) {
		super.service(request, response);
		
		String fieldId = getAttributeAsString(FIELD_ID); 
		String side = getAttributeAsString(SIDE); 
		String action = getAttributeAsString(ACTION); 
		
		if(fieldId == null || side == null || action == null) return;
		
		
		IWhereField whereField = null;
		for(Iterator it =getQuery().getWhereFieldsIterator(); it.hasNext(); ) {
			IWhereField field = (IWhereField)it.next();
			if(field.getId().equalsIgnoreCase(fieldId)) {
				whereField = field;
				break;
			}
		}
		
		
		if(whereField == null) return;
		
		if(side.equalsIgnoreCase("LEFT")) {
			if(action.equalsIgnoreCase("ADD")) {
				whereField.setLeftBracketsNum(whereField.getLeftBracketsNum()+1);
			} else if (action.equalsIgnoreCase("REMOVE")) {
				whereField.setLeftBracketsNum(whereField.getLeftBracketsNum()-1);				
			}
		} else if (side.equalsIgnoreCase("RIGHT")) {
			if(action.equalsIgnoreCase("ADD")) {
				whereField.setRightBracketsNum(whereField.getRightBracketsNum()+1);
			} else if (action.equalsIgnoreCase("REMOVE")) {
				whereField.setRightBracketsNum(whereField.getRightBracketsNum()-1);				
			}
		}
						
		
		updateLastUpdateTimeStamp();
		setDatamartWizard( getDatamartWizard() );
	}
}
