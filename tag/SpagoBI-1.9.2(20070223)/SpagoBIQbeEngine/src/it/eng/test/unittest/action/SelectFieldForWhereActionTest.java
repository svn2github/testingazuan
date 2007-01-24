package it.eng.test.unittest.action;

import it.eng.qbe.action.SelectFieldForWhereAction;
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

public class SelectFieldForWhereActionTest extends TestCase {

	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private SelectFieldForWhereAction selectFieldForWhereAction = null;
	
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
		
		selectFieldForWhereAction = new SelectFieldForWhereAction();
		selectFieldForWhereAction.setRequestContext(defaultRequestContext);
	}

	/*
	 * Test method for 'it.eng.qbe.action.SelectFieldForWhereAction.service(SourceBean, SourceBean)'
	 */
	public void testService() {

		try {
			String fieldName = "Cognome";
			request.setAttribute("COMPLETE_FIELD_NAME", fieldName);
			String hibType = "hibernate type";
			request.setAttribute("HIB_TYPE",hibType);
			selectFieldForWhereAction.service(request, response);
			IWhereClause aWhereClause = singleDataMartWizardObj.getWhereClause();
			List whereFields = aWhereClause.getWhereFields();
			assertEquals(4, whereFields.size());
			IWhereField aWhereField = null;
			for (int i = 0; i < whereFields.size(); i++) {
				aWhereField = (IWhereField) whereFields.get(i);
				if (aWhereField.getFieldName().equalsIgnoreCase(fieldName)) break;
			}
			assertEquals("=", aWhereField.getFieldOperator());
			assertEquals("", aWhereField.getFieldValue());
			assertEquals(hibType, aWhereField.getHibernateType());
			assertEquals("AND", aWhereField.getNextBooleanOperator());
			
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
		
	}

}
