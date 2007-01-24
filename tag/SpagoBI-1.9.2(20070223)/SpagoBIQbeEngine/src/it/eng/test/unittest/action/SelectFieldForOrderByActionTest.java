package it.eng.test.unittest.action;

import it.eng.qbe.action.SelectFieldForOrderByAction;
import it.eng.qbe.wizard.IOrderByClause;
import it.eng.qbe.wizard.IOrderGroupByField;
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

public class SelectFieldForOrderByActionTest extends TestCase {
	
	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private SelectFieldForOrderByAction selectFieldForOrderByAction = null;
	
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
		
		selectFieldForOrderByAction = new SelectFieldForOrderByAction();
		selectFieldForOrderByAction.setRequestContext(defaultRequestContext);
	}

	/*
	 * Test method for 'it.eng.qbe.action.SelectFieldForOrderByAction.service(SourceBean, SourceBean)'
	 */
	public void testService() {

		try {
			String fieldName = "field name";
			request.setAttribute("COMPLETE_FIELD_NAME",fieldName);
			selectFieldForOrderByAction.service(request, response);
			IOrderByClause orderByClause = singleDataMartWizardObj.getOrderByClause();
			List orderByFields = orderByClause.getOrderByFields();
			assertEquals(4, orderByFields.size());
			IOrderGroupByField aOrderGroupByField = null;
			for (int i = 0; i < orderByFields.size(); i++) {
				aOrderGroupByField = (IOrderGroupByField) orderByFields.get(i);
				if (aOrderGroupByField.getFieldName().equalsIgnoreCase(fieldName)) break;
			}
			assertEquals(fieldName, aOrderGroupByField.getFieldName());
			
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
		
	}

}
