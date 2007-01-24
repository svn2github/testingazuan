package it.eng.test.unittest.action;

import it.eng.qbe.action.UpdateFieldsForWhereAction;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereClause;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.test.utilities.CreateInitialSingleDataMartWizardObject;

import java.util.List;

import junit.framework.TestCase;

public class UpdateFieldsForWhereActionTest extends TestCase {

	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private UpdateFieldsForWhereAction updateFieldsForWhereAction = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		try {
			request = new SourceBean("");
			response = new SourceBean("");
		} catch (SourceBeanException e1) {
			e1.printStackTrace();
			fail("Unaspected exception occurred!");
		}
		reqContainer = new RequestContainer();
		resContainer = new ResponseContainer();
		reqContainer.setServiceRequest(request);
		resContainer.setServiceResponse(response);
		session = new SessionContainer(true);
		reqContainer.setSessionContainer(session);
		RequestContainer.setRequestContainer(reqContainer);
		DefaultRequestContext defaultRequestContext = new DefaultRequestContext(
				reqContainer, resContainer);
		resContainer.setErrorHandler(new EMFErrorHandler());
		
		singleDataMartWizardObj = CreateInitialSingleDataMartWizardObject.createInitialSingleDataMartWizardObject();
		
		session.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, singleDataMartWizardObj);
		
		updateFieldsForWhereAction = new UpdateFieldsForWhereAction();
		updateFieldsForWhereAction.setRequestContext(defaultRequestContext);
	}
	
	/*
	 * Test method for 'it.eng.qbe.action.UpdateFieldsForWhereAction.service(SourceBean, SourceBean)'
	 */
	public void testServiceUpdSel() {
		
		try {
			
			String nextAction = "next action";
			request.setAttribute("NEXT_ACTION", nextAction);
			String nextPublisher = "next publisher";
			request.setAttribute("NEXT_PUBLISHER", nextPublisher);
			String className = "class name";
			request.setAttribute("S_CLASS_NAME", className);
			String aliasedFieldName = "aliased field name";
			request.setAttribute("S_COMPLETE_FIELD_NAME", aliasedFieldName);
			String alias = "new alias";
			request.setAttribute("S_ALIAS_COMPLETE_FIELD_NAME", alias);
			String hibFieldType= "hib field type";
			request.setAttribute("S_HIB_TYPE", hibFieldType);
			String updCondMsg = "UPD_SEL";
			request.setAttribute("updCondMsg", updCondMsg);
			String selectionTree = "selection tree";
			request.setAttribute("SELECTION_TREE", selectionTree);
			String selectedFieldId = "selected field id";
			request.setAttribute("FIELDID", selectedFieldId);
			String fieldId = "2";
			String operatorForField = "contains";
			request.setAttribute("OPERATOR_FOR_FIELD_"+fieldId, operatorForField);
			String valueForField = "value for field";
			request.setAttribute("VALUE_FOR_FIELD_"+fieldId, valueForField);
			String nextBooleanOperatorForField = "OR";
			request.setAttribute("NEXT_BOOLEAN_OPERATOR_FOR_FIELD_"+fieldId, nextBooleanOperatorForField);
		 	updateFieldsForWhereAction.service(request, response);
		 	IWhereClause aWhereClause = singleDataMartWizardObj.getWhereClause();
		 	List whereFields = aWhereClause.getWhereFields();
		 	IWhereField aWhereField = null; 
		 	for (int i = 0; i < whereFields.size(); i++) {
		 		aWhereField = (IWhereField) whereFields.get(i);
		 		if (aWhereField.getId().equalsIgnoreCase(fieldId)) break;
		 	}
		 	assertEquals(4, whereFields.size());
			assertEquals(operatorForField, aWhereField.getFieldOperator());
			assertEquals(valueForField, aWhereField.getFieldValue());
			assertEquals(nextBooleanOperatorForField, aWhereField.getNextBooleanOperator());
			
			aWhereField = null;
			for (int i = 0; i < whereFields.size(); i++) {
				aWhereField = (IWhereField) whereFields.get(i);
				if (aWhereField.getFieldName().equalsIgnoreCase(aliasedFieldName)) break;
			}
			
			assertEquals("=", aWhereField.getFieldOperator());
			assertEquals("", aWhereField.getFieldValue());
			assertEquals(hibFieldType, aWhereField.getHibernateType());
			assertEquals("AND", aWhereField.getNextBooleanOperator());
			EntityClass ec = new EntityClass(className, "a" + className);
			assertTrue(singleDataMartWizardObj.containEntityClass(ec));
			assertEquals(ec.getClassName(), aWhereField.getFieldEntityClassForLeftCondition().getClassName());
			assertEquals(ec.getClassAlias(), aWhereField.getFieldEntityClassForLeftCondition().getClassAlias());
			
			assertEquals(nextAction, response.getAttribute("NEXT_ACTION"));		
			assertEquals(nextPublisher, response.getAttribute("NEXT_PUBLISHER"));

		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}

	}
	
	/*
	 * Test method for 'it.eng.qbe.action.UpdateFieldsForWhereAction.service(SourceBean, SourceBean)'
	 */
	public void testServiceUpdSelRight() {
		
		try {
			
			String nextAction = "next action";
			request.setAttribute("NEXT_ACTION", nextAction);
			String nextPublisher = "next publisher";
			request.setAttribute("NEXT_PUBLISHER", nextPublisher);
			String className = "class name";
			request.setAttribute("S_CLASS_NAME", className);
			String aliasedFieldName = "aliased field name";
			request.setAttribute("S_COMPLETE_FIELD_NAME", aliasedFieldName);
			String alias = "new alias";
			request.setAttribute("S_ALIAS_COMPLETE_FIELD_NAME", alias);
			String hibFieldType= "hib field type";
			request.setAttribute("S_HIB_TYPE", hibFieldType);
			String updCondMsg = "UPD_SEL_RIGHT";
			request.setAttribute("updCondMsg", updCondMsg);
			String selectionTree = "selection tree";
			request.setAttribute("SELECTION_TREE", selectionTree);
			String selectedFieldId = "2";
			request.setAttribute("FIELDID", selectedFieldId);
			String fieldId = "2";
			String operatorForField = "contains";
			request.setAttribute("OPERATOR_FOR_FIELD_"+fieldId, operatorForField);
			String valueForField = "value for field";
			request.setAttribute("VALUE_FOR_FIELD_"+fieldId, valueForField);
			String nextBooleanOperatorForField = "OR";
			request.setAttribute("NEXT_BOOLEAN_OPERATOR_FOR_FIELD_"+fieldId, nextBooleanOperatorForField);
		 	updateFieldsForWhereAction.service(request, response);
		 	IWhereClause aWhereClause = singleDataMartWizardObj.getWhereClause();
		 	List whereFields = aWhereClause.getWhereFields();
		 	IWhereField aWhereField = null; 
		 	for (int i = 0; i < whereFields.size(); i++) {
		 		aWhereField = (IWhereField) whereFields.get(i);
		 		if (aWhereField.getId().equalsIgnoreCase(fieldId)) break;
		 	}
		 	assertEquals(3, whereFields.size());
			assertEquals(operatorForField, aWhereField.getFieldOperator());
			assertEquals(valueForField, aWhereField.getFieldValue());
			assertEquals(nextBooleanOperatorForField, aWhereField.getNextBooleanOperator());
			
			EntityClass ec = new EntityClass(className, "a" + className);
			assertTrue(singleDataMartWizardObj.containEntityClass(ec));
			assertEquals(ec.getClassName(), aWhereField.getFieldEntityClassForRightCondition().getClassName());
			assertEquals(ec.getClassAlias(), aWhereField.getFieldEntityClassForRightCondition().getClassAlias());
			
			assertEquals(nextAction, response.getAttribute("NEXT_ACTION"));		
			assertEquals(nextPublisher, response.getAttribute("NEXT_PUBLISHER"));

		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}

	}
	
	/*
	 * Test method for 'it.eng.qbe.action.UpdateFieldsForWhereAction.service(SourceBean, SourceBean)'
	 */
	public void testServiceUpdTreeSel() {
		
		try {
			
			String nextAction = "next action";
			request.setAttribute("NEXT_ACTION", nextAction);
			String nextPublisher = "next publisher";
			request.setAttribute("NEXT_PUBLISHER", nextPublisher);
			String className = "class name";
			request.setAttribute("S_CLASS_NAME", className);
			String aliasedFieldName = "aliased field name";
			request.setAttribute("S_COMPLETE_FIELD_NAME", aliasedFieldName);
			String alias = "new alias";
			request.setAttribute("S_ALIAS_COMPLETE_FIELD_NAME", alias);
			String hibFieldType= "hib field type";
			request.setAttribute("S_HIB_TYPE", hibFieldType);
			String updCondMsg = "UPD_TREE_SEL";
			request.setAttribute("updCondMsg", updCondMsg);
			String selectionTree = "selection tree";
			request.setAttribute("SELECTION_TREE", selectionTree);
			String selectedFieldId = "selected field id";
			request.setAttribute("FIELDID", selectedFieldId);
			String fieldId = "2";
			String operatorForField = "contains";
			request.setAttribute("OPERATOR_FOR_FIELD_"+fieldId, operatorForField);
			String valueForField = "value for field";
			request.setAttribute("VALUE_FOR_FIELD_"+fieldId, valueForField);
			String nextBooleanOperatorForField = "OR";
			request.setAttribute("NEXT_BOOLEAN_OPERATOR_FOR_FIELD_"+fieldId, nextBooleanOperatorForField);
		 	updateFieldsForWhereAction.service(request, response);
		 	IWhereClause aWhereClause = singleDataMartWizardObj.getWhereClause();
		 	List whereFields = aWhereClause.getWhereFields();
		 	IWhereField aWhereField = null; 
		 	for (int i = 0; i < whereFields.size(); i++) {
		 		aWhereField = (IWhereField) whereFields.get(i);
		 		if (aWhereField.getId().equalsIgnoreCase(fieldId)) break;
		 	}
		 	assertEquals(3, whereFields.size());
			assertEquals(operatorForField, aWhereField.getFieldOperator());
			assertEquals(valueForField, aWhereField.getFieldValue());
			assertEquals(nextBooleanOperatorForField, aWhereField.getNextBooleanOperator());
			
			assertEquals(nextAction, response.getAttribute("NEXT_ACTION"));		
			assertEquals(nextPublisher, response.getAttribute("NEXT_PUBLISHER"));
			
			assertEquals(selectionTree, session.getAttribute("SELECTION_TREE"));

		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}

	}

}
