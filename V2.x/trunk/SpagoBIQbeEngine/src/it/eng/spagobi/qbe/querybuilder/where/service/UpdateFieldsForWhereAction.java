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

import it.eng.qbe.log.Logger;
import it.eng.qbe.query.IWhereField;
import it.eng.qbe.wizard.EntityClass;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;

import java.util.Iterator;


/**
 * This Action is responsible to handle the modification of where fields in the Field
 * Condition Tab, like the right-value of where modification, the where operator modification 
 * 
 */
public class UpdateFieldsForWhereAction extends AbstractQbeEngineAction {

	// valid input parameter names
	public static final String S_CLASS_NAME = "S_CLASS_NAME";
	public static final String S_HIB_TYPE = "S_HIB_TYPE";
	public static final String UPD_COND_MSG = "updCondMsg";
	public static final String FIELDID = "FIELDID";
	public static final String FUPD_COND_JOIN_PARENT = "fUpdCondJoinParent";
	public static final String S_COMPLETE_FIELD_NAME = "S_COMPLETE_FIELD_NAME";
	public static final String INPUT_SELECTION_TREE = "SELECTION_TREE";
	public static final String INPUT_NEXT_ACTION = "NEXT_ACTION";
	public static final String INPUT_NEXT_PUBLISHER = "NEXT_PUBLISHER";
	public static final String NEXT_BOOLEAN_OPERATOR_FOR_FIELD_PREFIX = "NEXT_BOOLEAN_OPERATOR_FOR_FIELD_";
	public static final String VALUE_FOR_FIELD_PREFIX = "VALUE_FOR_FIELD_";
	public static final String OPERATOR_FOR_FIELD_PREFIX = "OPERATOR_FOR_FIELD_";
	
	
	
	
	
	// output session parameters
	public static final String OUTPUT_SELECTION_TREE = INPUT_SELECTION_TREE;
	public static final String OUTPUT_NEXT_ACTION = INPUT_NEXT_ACTION;
	public static final String OUTPUT_NEXT_PUBLISHER = INPUT_NEXT_PUBLISHER;
	
	
	
	public void service(SourceBean request, SourceBean response) {
		super.service(request, response);
		
		String className = getAttributeAsString(S_CLASS_NAME);	
		String hibFieldType= getAttributeAsString(S_HIB_TYPE);
		String nextAction = getAttributeAsString(INPUT_NEXT_ACTION);
		String nextPublisher = getAttributeAsString(INPUT_NEXT_PUBLISHER);
		String updCondMsg = getAttributeAsString(UPD_COND_MSG);
		String selectionTree = getAttributeAsString(INPUT_SELECTION_TREE);
		String selectedFieldId = getAttributeAsString(FIELDID);
		String isJoinWithParentQuery =   getAttributeAsString(FUPD_COND_JOIN_PARENT);
		String aliasedFieldName = getAttributeAsString(S_COMPLETE_FIELD_NAME);
		
		String classPrefix = "a";
		
		
		if (isJoinWithParentQuery != null && isJoinWithParentQuery.equalsIgnoreCase("TRUE")){
			classPrefix = "a";
		}else{
			if ( isSubqueryModeActive() ){
				classPrefix = getQuery().getSubQueryIdForSubQueryOnField( getSubqueryField() );
			}
		}
		String classAlias = null;
		
		classAlias = classPrefix  + className.replace(".", "_");
			
		
		setAttribute(OUTPUT_NEXT_ACTION, nextAction);		
		setAttribute(OUTPUT_NEXT_PUBLISHER, nextPublisher);
		
		
		
		IWhereField aWhereField = null; 
		Iterator it = getQuery().getWhereFieldsIterator();
		String fieldName = null;
		String operatorForField = null;
		String valueForField = null;
		String nextBooleanOperatorForField = null;
		String fieldId = null;
		while (it.hasNext()){
				aWhereField = (IWhereField)it.next();
			 	
				fieldName = aWhereField.getFieldName();
			 	fieldId =  aWhereField.getId();
			 	operatorForField =getAttributeAsString(OPERATOR_FOR_FIELD_PREFIX + fieldId);
			 	if (operatorForField != null){
			 		aWhereField.setFieldOperator(operatorForField);
			 	}
			 	valueForField = getAttributeAsString(VALUE_FOR_FIELD_PREFIX + fieldId);
			 	if (valueForField != null){
			 		if(!valueForField.equals(aWhereField.getFieldValue())) {
			 			EntityClass entity = aWhereField.getFieldEntityClassForRightCondition();
			 			aWhereField.setFieldEntityClassForRightCondition(null);
			 		}
			 		aWhereField.setFieldValue(valueForField);
			 	}
			 	
			 	nextBooleanOperatorForField = getAttributeAsString(NEXT_BOOLEAN_OPERATOR_FOR_FIELD_PREFIX + fieldId);
			 	if (nextBooleanOperatorForField != null){
			 		aWhereField.setNextBooleanOperator(nextBooleanOperatorForField);
			 	}
		 }
		
		

		
		
		if (updCondMsg.equalsIgnoreCase("UPD_SEL")) {
			
			EntityClass ec = new EntityClass(className, classAlias);
			if (!getQuery().containEntityClass(ec)){
				getQuery().addEntityClass(ec);
			}

			IWhereField whereField = getQuery().addWhereField(aliasedFieldName, hibFieldType);
			whereField.setFieldEntityClassForLeftCondition(ec);
			
			updateLastUpdateTimeStamp();
			setDatamartWizard( getDatamartWizard() );
			
		} else if (updCondMsg.equalsIgnoreCase("UPD_SEL_RIGHT")){
			
			EntityClass ec = new EntityClass(className, classAlias);
			
			if (isJoinWithParentQuery != null && isJoinWithParentQuery.equalsIgnoreCase("TRUE")){
				Logger.debug(this.getClass(), "-----");
			}else{	
				if (!getQuery().containEntityClass(ec)){
					getQuery().addEntityClass(ec);
				}
			}
			
			
			aWhereField = null;
			
			it = getQuery().getWhereFieldsIterator();;
			while (it.hasNext()){
					aWhereField = (IWhereField)it.next();
					if (aWhereField.getId().equalsIgnoreCase(selectedFieldId)){
						
						aWhereField.setFieldEntityClassForRightCondition(ec);
						
				}
			}
			
			if (isJoinWithParentQuery != null && isJoinWithParentQuery.equalsIgnoreCase("TRUE")){
				// --- 
			}else{
				getQuery().purgeNotReferredEntityClasses();
			}
			
			updateLastUpdateTimeStamp();
			setDatamartWizard( getDatamartWizard() );
						
			
		} else if (updCondMsg.equalsIgnoreCase("UPD_TREE_SEL")){
		
			this.setAttributeInSession(OUTPUT_SELECTION_TREE, selectionTree);
						
		}
	}
}

