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
package it.eng.qbe.action;

import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;

import java.util.Iterator;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to handle the modification of selected fields in the Field
 * Selection Tab, like apply operators, distinct selection, change of the alias of a field
 * 
 */
public class UpdateFieldsForSelectAction extends AbstractAction {
	
	/**
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		String className = (String) request.getAttribute("className");
		
		
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(aSessionContainer);
		
		
		ISelectClause selectClause = aWizardObject.getSelectClause();
		ISelectField selectField = null;
		
	
		if (selectClause != null){
			java.util.List l= selectClause.getSelectFields();
			Iterator it = l.iterator();
			String fieldId = null;
			
			
			while (it.hasNext()){
				selectField = (ISelectField)it.next();
			 	fieldId = selectField.getId();
			 	String newFieldName  =(String)request.getAttribute("NEW_FIELD_"+fieldId);
			 	String alias  =(String)request.getAttribute("ALIAS_FOR_"+fieldId);
			 	
			 	if (newFieldName != null){
			 		selectField.setFieldName(newFieldName);
			 	}//end if 	
			 	
			 	if ((alias != null) && (alias.trim().length() > 0)){
			 		selectField.setFieldAlias(alias);
			 	}
			}//end if
		}//end  if
		
		
		String distinct = (String)request.getAttribute("selectDistinct");
		if (distinct.equalsIgnoreCase("true")){
			aWizardObject.setDistinct(true);
		}else{
			aWizardObject.setDistinct(false);
		}
		
		Utils.updateLastUpdateTimeStamp(getRequestContainer());
		
	}
}
