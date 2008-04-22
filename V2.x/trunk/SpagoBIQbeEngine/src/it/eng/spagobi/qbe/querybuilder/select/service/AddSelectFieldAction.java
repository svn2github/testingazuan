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

import it.eng.qbe.model.structure.DataMartField;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;


public class AddSelectFieldAction extends AbstractQbeEngineAction {
	
	public static final String ACTION_NAME = "SELECT_FIELD_FOR_SELECT_ACTION";
	
	// valid input parameter names
	public static final String FIELD_UNIQUE_NAME= "FIELD_UNIQUE_NAME";
	public static final String CLASS_NAME = "CLASS_NAME";
	public static final String FIELD_NAME = "FIELD_NAME";
	public static final String FIELD_LABEL = "FIELD_LABEL";
	public static final String FIELD_HIBTYPE = "FIELD_HIBTYPE";
	public static final String FIELD_HIBSCALE= "FIELD_HIBSCALE";
	public static final String FIELD_HIBPREC= "FIELD_HIBPREC";
	
	
	
	public void service(SourceBean request, SourceBean response) throws EngineException {	
		super.service(request, response);
				
		String fieldUniqueName = getAttributeAsString(FIELD_UNIQUE_NAME);	
		
		String className = getAttributeAsString(CLASS_NAME);		
		String fieldName = getAttributeAsString(FIELD_NAME); 
		String fieldLabel = getAttributeAsString(FIELD_LABEL);
		
		String fieldHibType = getAttributeAsString(FIELD_HIBTYPE);
		String fieldHibScale = getAttributeAsString(FIELD_HIBSCALE);
		String fieldHibPrec= getAttributeAsString(FIELD_HIBPREC);
		
		if(fieldUniqueName != null) {
			DataMartField field = getDatamartModel().getDataMartModelStructure().getField(fieldUniqueName);
			
			fieldName = field.getQueryName();			
			className = field.getParent().getRoot().getType();
			fieldLabel =field.getName();
			fieldHibType = field.getType();
			fieldHibScale = "" + field.getLength();
			fieldHibPrec = "" + field.getPrecision();
		}
		
		
		
				
		getQuery().addSelectField(className, fieldName, fieldLabel, 
				fieldHibType, fieldHibScale, fieldHibPrec);
		
		
		updateLastUpdateTimeStamp();
		setDatamartWizard( getDatamartWizard() );
	}
	
	
}
