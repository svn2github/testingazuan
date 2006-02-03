package it.eng.test.unittest.action;

import it.eng.qbe.action.UpdateFieldsForSelectAction;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
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

public class UpdateFieldsForSelectActionTest extends TestCase {

	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private UpdateFieldsForSelectAction updateFieldsForSelectAction = null;
	
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
		
		updateFieldsForSelectAction = new UpdateFieldsForSelectAction();
		updateFieldsForSelectAction.setRequestContext(defaultRequestContext);
	}
	
	/*
	 * Test method for 'it.eng.qbe.action.UpdateFieldsForSelectAction.service(SourceBean, SourceBean)'
	 */
	public void testService() {

		try {
			String className = "class name";
			request.setAttribute("className", className);
			String distinct = "true";
			request.setAttribute("selectDistinct", distinct);	
		 	String newFieldName = "new field name";
		 	String fieldId = "2";
		 	request.setAttribute("NEW_FIELD_"+fieldId, newFieldName);
		 	String alias = "new alias";
		 	request.setAttribute("ALIAS_FOR_"+fieldId, alias);
			updateFieldsForSelectAction.service(request, response);
			ISelectClause selectClause = singleDataMartWizardObj.getSelectClause();
			List selectFields = selectClause.getSelectFields();
			ISelectField selectField = null;
			for (int i = 0; i < selectFields.size(); i++) {
				selectField = (ISelectField) selectFields.get(i);
				if (selectField.getId().equals(fieldId)) break;
			}
			assertEquals(newFieldName, selectField.getFieldName());
			assertEquals(alias, selectField.getFieldAlias());
			assertTrue(singleDataMartWizardObj.getDistinct());
			
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
		
	}

}
