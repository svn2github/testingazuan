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

import it.eng.qbe.log.Logger;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereClause;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.WhereClauseSourceBeanImpl;
import it.eng.qbe.wizard.WhereFieldSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;

import java.util.Iterator;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to handle the modification of where fields in the Field
 * Condition Tab, like the right-value of where modification, the where operator modification 
 * 
 */
public class UpdateFieldsForWhereAction extends AbstractAction {
	
	/**
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) {
		
		RequestContainer aRequestContainer = getRequestContainer();
		SessionContainer aSessionContainer = aRequestContainer.getSessionContainer();
		ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(aSessionContainer);
		
		String className = (String) request.getAttribute("S_CLASS_NAME");	
		
		String classPrefix = "a";
		String isJoinWithParentQuery =   (String)request.getAttribute("fUpdCondJoinParent");
		
		if (isJoinWithParentQuery != null && isJoinWithParentQuery.equalsIgnoreCase("TRUE")){
			classPrefix = "a";
		}else{
			if (Utils.isSubQueryModeActive(aSessionContainer)){
				String subQueryFieldId = (String)aSessionContainer.getAttribute(WizardConstants.SUBQUERY_FIELD);
				classPrefix = Utils.getMainWizardObject(aSessionContainer).getSubQueryIdForSubQueryOnField(subQueryFieldId);
			}
		}
		String classAlias = null;
		/*
			if (className.indexOf(".") > 0){
				classAlias = classPrefix + className.substring(className.lastIndexOf(".")+1);
			}else{
				classAlias = classPrefix  + className;
			}
		*/
		
		classAlias = classPrefix  + className.replace(".", "_");
			
		String aliasedFieldName = (String)request.getAttribute("S_COMPLETE_FIELD_NAME");
		String alias = (String)request.getAttribute("S_ALIAS_COMPLETE_FIELD_NAME");
		
		String hibFieldType= (String)request.getAttribute("S_HIB_TYPE");
		String nextAction = (String)request.getAttribute("NEXT_ACTION");
		String nextPublisher = (String)request.getAttribute("NEXT_PUBLISHER");
		String updCondMsg = (String)request.getAttribute("updCondMsg");
		String selectionTree = (String)request.getAttribute("SELECTION_TREE");
		String selectedFieldId = (String)request.getAttribute("FIELDID");
		
		
		try{
			response.setAttribute("NEXT_ACTION", nextAction);		
			response.setAttribute("NEXT_PUBLISHER", nextPublisher);
		}catch(SourceBeanException sbe){
			sbe.printStackTrace();
		}
		
		
		IWhereClause aWhereClause = aWizardObject.getWhereClause();
		
		IWhereField aWhereField = null; 
		
		
		if (aWhereClause != null){
			java.util.List l= aWhereClause.getWhereFields();
			Iterator it = l.iterator();
			String fieldName = null;
			String operatorForField = null;
			String valueForField = null;
			String nextBooleanOperatorForField = null;
			String fieldId = null;
			while (it.hasNext()){
				aWhereField = (IWhereField)it.next();
			 	
				fieldName = aWhereField.getFieldName();
			 	fieldId =  aWhereField.getId();
			 	operatorForField =(String)request.getAttribute("OPERATOR_FOR_FIELD_"+fieldId);
			 	if (operatorForField != null){
			 		aWhereField.setFieldOperator(operatorForField);
			 	}
			 	valueForField =(String)request.getAttribute("VALUE_FOR_FIELD_"+fieldId);
			 	if (valueForField != null){
			 		if(!valueForField.equals(aWhereField.getFieldValue())) {
			 			EntityClass entity = aWhereField.getFieldEntityClassForRightCondition();
			 			aWhereField.setFieldEntityClassForRightCondition(null);
			 		}
			 		aWhereField.setFieldValue(valueForField);
			 	}
			 	
			 	nextBooleanOperatorForField =(String)request.getAttribute("NEXT_BOOLEAN_OPERATOR_FOR_FIELD_"+fieldId);
			 	if (nextBooleanOperatorForField != null){
			 		aWhereField.setNextBooleanOperator(nextBooleanOperatorForField);
			 	}
			 }
		}
		

		
		
		if (updCondMsg.equalsIgnoreCase("UPD_SEL")){
		
			
			// update clause		
			
			EntityClass ec = new EntityClass(className, classAlias);
			if (!aWizardObject.containEntityClass(ec)){
				aWizardObject.addEntityClass(ec);
			}

			
			if ( aWhereClause == null){
				aWhereClause = new WhereClauseSourceBeanImpl();
			}
		
			IWhereField aWhereField2 = new WhereFieldSourceBeanImpl();
			aWhereField2.setFieldName(aliasedFieldName);
			aWhereField2.setFieldOperator("=");
			aWhereField2.setFieldValue("");
			aWhereField2.setHibernateType(hibFieldType);
			aWhereField2.setNextBooleanOperator("AND");
			aWhereField2.setFieldEntityClassForLeftCondition(ec);
			
			// se volessi potrei fare il parsing del campo che ho aggiunto a mano per vedere se ho introdotto un campo e non un valore
			
			aWhereClause.addWhereField(aWhereField2);
			aWizardObject.setWhereClause(aWhereClause);
			
			Utils.updateLastUpdateTimeStamp(getRequestContainer());
			aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, Utils.getMainWizardObject(aSessionContainer));
			
						
		} else if (updCondMsg.equalsIgnoreCase("UPD_SEL_RIGHT")){
			
			EntityClass ec = new EntityClass(className, classAlias);
			
			if (isJoinWithParentQuery != null && isJoinWithParentQuery.equalsIgnoreCase("TRUE")){
				Logger.debug(this.getClass(), "-----");
			}else{	
				if (!aWizardObject.containEntityClass(ec)){
					aWizardObject.addEntityClass(ec);
				}
			}
			
			
			aWhereField = null;
			
			if (aWhereClause != null){
				java.util.List l= aWhereClause.getWhereFields();
				Iterator it = l.iterator();
			    while (it.hasNext()){
					aWhereField = (IWhereField)it.next();
					if (aWhereField.getId().equalsIgnoreCase(selectedFieldId)){
						
						aWhereField.setFieldEntityClassForRightCondition(ec);
						
					}
			    }
			}
			if (isJoinWithParentQuery != null && isJoinWithParentQuery.equalsIgnoreCase("TRUE")){
				// --- 
			}else{
				aWizardObject.purgeNotReferredEntityClasses();
			}
			
			Utils.updateLastUpdateTimeStamp(getRequestContainer());
			aSessionContainer.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, Utils.getMainWizardObject(aSessionContainer));
						
			
		} else if (updCondMsg.equalsIgnoreCase("UPD_TREE_SEL")){
		
			aSessionContainer.setAttribute("SELECTION_TREE",selectionTree);
						
		}
	}
}

