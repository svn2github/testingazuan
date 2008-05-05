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
package it.eng.spagobi.qbe.querybuilder.where.service;

import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.query.IWhereField;
import it.eng.qbe.wizard.EntityClass;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;

import java.util.Iterator;


// TODO: Auto-generated Javadoc
/**
 * This Action is responsible to handle the modification of where fields in the Field
 * Condition Tab, like the right-value of where modification, the where operator modification.
 */
public class UpdateFieldsForWhereAction extends AbstractQbeEngineAction {

	// valid input parameter names
	
	/** The Constant FIELD_UNIQUE_NAME. */
	public static final String FIELD_UNIQUE_NAME = "FIELD_UNIQUE_NAME";
	
	/** The Constant S_CLASS_NAME. */
	public static final String S_CLASS_NAME = "S_CLASS_NAME";
	
	/** The Constant S_HIB_TYPE. */
	public static final String S_HIB_TYPE = "S_HIB_TYPE";
	
	/** The Constant UPD_COND_MSG. */
	public static final String UPD_COND_MSG = "updCondMsg";
	
	/** The Constant FIELDID. */
	public static final String FIELDID = "FIELDID";
	
	/** The Constant FUPD_COND_JOIN_PARENT. */
	public static final String FUPD_COND_JOIN_PARENT = "fUpdCondJoinParent";
	
	/** The Constant S_COMPLETE_FIELD_NAME. */
	public static final String S_COMPLETE_FIELD_NAME = "S_COMPLETE_FIELD_NAME";
	
	/** The Constant INPUT_SELECTION_TREE. */
	public static final String INPUT_SELECTION_TREE = "SELECTION_TREE";
	
	/** The Constant INPUT_NEXT_ACTION. */
	public static final String INPUT_NEXT_ACTION = "NEXT_ACTION";
	
	/** The Constant INPUT_NEXT_PUBLISHER. */
	public static final String INPUT_NEXT_PUBLISHER = "NEXT_PUBLISHER";
	
	/** The Constant NEXT_BOOLEAN_OPERATOR_FOR_FIELD_PREFIX. */
	public static final String NEXT_BOOLEAN_OPERATOR_FOR_FIELD_PREFIX = "NEXT_BOOLEAN_OPERATOR_FOR_FIELD_";
	
	/** The Constant VALUE_FOR_FIELD_PREFIX. */
	public static final String VALUE_FOR_FIELD_PREFIX = "VALUE_FOR_FIELD_";
	
	/** The Constant OPERATOR_FOR_FIELD_PREFIX. */
	public static final String OPERATOR_FOR_FIELD_PREFIX = "OPERATOR_FOR_FIELD_";
	
	
	
	
	
	// output session parameters
	/** The Constant OUTPUT_SELECTION_TREE. */
	public static final String OUTPUT_SELECTION_TREE = INPUT_SELECTION_TREE;
	
	/** The Constant OUTPUT_NEXT_ACTION. */
	public static final String OUTPUT_NEXT_ACTION = INPUT_NEXT_ACTION;
	
	/** The Constant OUTPUT_NEXT_PUBLISHER. */
	public static final String OUTPUT_NEXT_PUBLISHER = INPUT_NEXT_PUBLISHER;
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws EngineException {
		super.service(request, response);
		
		String fieldUniqueName = getAttributeAsString(FIELD_UNIQUE_NAME);	
		String className = getAttributeAsString(S_CLASS_NAME);	
		String hibFieldType= getAttributeAsString(S_HIB_TYPE);
		String nextAction = getAttributeAsString(INPUT_NEXT_ACTION);
		String nextPublisher = getAttributeAsString(INPUT_NEXT_PUBLISHER);
		String updCondMsg = getAttributeAsString(UPD_COND_MSG);
		String selectionTree = getAttributeAsString(INPUT_SELECTION_TREE);
		String selectedFieldId = getAttributeAsString(FIELDID);
		boolean isJoinWithParentQuery =   getAttributeAsBoolean(FUPD_COND_JOIN_PARENT);;
		String aliasedFieldName = getAttributeAsString(S_COMPLETE_FIELD_NAME);
		
		String classPrefix = "a";
		
		
		if ( isJoinWithParentQuery ){
			classPrefix = "a";
		}else{
			if ( isSubqueryModeActive() ){
				classPrefix = getQuery().getSubQueryIdForSubQueryOnField( getSubqueryField() );
			}
		}
		
			
		
		setAttribute(OUTPUT_NEXT_ACTION, nextAction);		
		setAttribute(OUTPUT_NEXT_PUBLISHER, nextPublisher);
		
		
		
		IWhereField whereField = null; 
		Iterator it = getQuery().getWhereFieldsIterator();
		String fieldName = null;
		String operatorForField = null;
		String valueForField = null;
		String nextBooleanOperatorForField = null;
		String fieldId = null;
		while (it.hasNext()){
				whereField = (IWhereField)it.next();
			 	
				fieldName = whereField.getFieldName();
			 	fieldId =  whereField.getId();
			 	operatorForField =getAttributeAsString(OPERATOR_FOR_FIELD_PREFIX + fieldId);
			 	if (operatorForField != null){
			 		whereField.setFieldOperator(operatorForField);
			 	}
			 	valueForField = getAttributeAsString(VALUE_FOR_FIELD_PREFIX + fieldId);
			 	if (valueForField != null){
			 		if(!valueForField.equals(whereField.getFieldValue())) {
			 			EntityClass entity = whereField.getFieldEntityClassForRightCondition();
			 			whereField.setFieldEntityClassForRightCondition(null);
			 		}
			 		whereField.setFieldValue(valueForField);
			 	}
			 	
			 	nextBooleanOperatorForField = getAttributeAsString(NEXT_BOOLEAN_OPERATOR_FOR_FIELD_PREFIX + fieldId);
			 	if (nextBooleanOperatorForField != null){
			 		whereField.setNextBooleanOperator(nextBooleanOperatorForField);
			 	}
		 }
		
		
		// === new invocation type ====================================================================
		if(fieldUniqueName != null && !fieldUniqueName.equalsIgnoreCase("")) { 
			DataMartField field = this.getDatamartModel().getDataMartModelStructure().getField(fieldUniqueName);
			className = field.getParent().getRoot().getType();
			
			
			String aliasedEntityName;
			String subQueryPrefix = null;
		   	if ( getQuery().isSubqueryModeActive() && !isJoinWithParentQuery ){
					String subQueryFieldId = getQuery().getSubqueryFieldId();
					subQueryFieldId = getSubqueryField(); // deprecated: use only the above initialization
					
					subQueryPrefix =  getQuery().getSubQueryIdForSubQueryOnField(subQueryFieldId);
		   	}
		   	
			if (subQueryPrefix == null){
				subQueryPrefix = "a";
			}
			
			if (field.getParent().getRoot().getType().indexOf(".") > 0){
				aliasedEntityName = subQueryPrefix + field.getParent().getRoot().getType().replace(".", "_");
			}else{
				aliasedEntityName = subQueryPrefix + field.getParent().getRoot().getType();
			}
			
			aliasedFieldName = aliasedEntityName + "." + field.getQueryName();
			
			hibFieldType = field.getType();
		} 
		
		// === new invocation type ====================================================================
		
		String classAlias = classPrefix  + className.replace(".", "_");
		EntityClass ec = new EntityClass(className, classAlias);		
		
		if (updCondMsg.equalsIgnoreCase("UPD_SEL")) {			
			
			
			if (!getQuery().containEntityClass(ec)){
				getQuery().addEntityClass(ec);
			}

			whereField = getQuery().addWhereField(aliasedFieldName, hibFieldType);
			whereField.setFieldEntityClassForLeftCondition(ec);
			
			updateLastUpdateTimeStamp();
			setDatamartWizard( getDatamartWizard() );
			
		} else if (updCondMsg.equalsIgnoreCase("UPD_SEL_RIGHT")) {			
			
			if ( !isJoinWithParentQuery ) {
				if (!getQuery().containEntityClass(ec)){
					getQuery().addEntityClass(ec);
				}
			}
			
			
			it = getQuery().getWhereFieldsIterator();;
			while (it.hasNext()) {
				whereField = (IWhereField)it.next();
				if (whereField.getId().equalsIgnoreCase(selectedFieldId)) {						
					whereField.setFieldEntityClassForRightCondition(ec);						
				}
			}
			
			if ( !isJoinWithParentQuery ) {
				getQuery().purgeNotReferredEntityClasses();
			}
			
			whereField.setFieldValue(aliasedFieldName);
			
			updateLastUpdateTimeStamp();
			setDatamartWizard( getDatamartWizard() );
						
			
		} else if (updCondMsg.equalsIgnoreCase("UPD_TREE_SEL")){
		
			this.setAttributeInSession(OUTPUT_SELECTION_TREE, selectionTree);
						
		}
	}
}

