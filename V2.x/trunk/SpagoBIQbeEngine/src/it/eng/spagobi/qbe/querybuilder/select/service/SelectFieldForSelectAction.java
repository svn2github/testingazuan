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

import it.eng.qbe.javascript.QbeJsTreeNodeId;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;


public class SelectFieldForSelectAction extends AbstractQbeEngineAction {
	
	// valid input parameter names
	public static final String CLASS_NAME = "CLASS_NAME";
	public static final String FIELD_NAME = "FIELD_NAME";
	public static final String FIELD_LABEL = "FIELD_LABEL";
	public static final String FIELD_HIBTYPE = "FIELD_HIBTYPE";
	public static final String FIELD_HIBSCALE= "FIELD_HIBSCALE";
	public static final String FIELD_HIBPREC= "FIELD_HIBPREC";

	
	public void service(SourceBean request, SourceBean response) {	
				
		String className = (String)request.getAttribute(CLASS_NAME);		
		String fieldName = (String)request.getAttribute(FIELD_NAME); 
		String fieldLabel = (String)request.getAttribute(FIELD_LABEL);
		
		String fieldHibType = (String)request.getAttribute(FIELD_HIBTYPE);
		String fieldHibScale = (String)request.getAttribute(FIELD_HIBSCALE);
		String fieldHibPrec= (String)request.getAttribute(FIELD_HIBPREC);
		
		
		
		String subQueryPrefix = null;
		if (isSubqueryModeActive()){
			subQueryPrefix = getMainDataMartWizard().getSubQueryIdForSubQueryOnField( getSubqueryField() );
		}
		
		if(className != null && fieldName != null) {		
			
			QbeJsTreeNodeId nodeId = new QbeJsTreeNodeId(className, fieldName);
			if (isSubqueryModeActive())
				nodeId.setClassPrefix(subQueryPrefix);
			
			getActiveQuery().addSelectField(nodeId.getClassName(), 
							nodeId.getClassAlias(), 
							nodeId.getFieldAlias(), 
							fieldLabel, 
							className + "." + fieldName,
							fieldHibType,
							fieldHibScale,
							fieldHibPrec);
		} 
			
		
		updateLastUpdateTimeStamp();
		setMainDataMartWizard( getMainDataMartWizard() );		
	}
}
