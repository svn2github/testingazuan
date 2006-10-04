package it.eng.test.unittest.action;

import it.eng.qbe.action.SelectFieldForGroupByAction;
import it.eng.qbe.wizard.IGroupByClause;
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

public class SelectFieldForGroupByActionTest extends TestCase {
	
	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private SelectFieldForGroupByAction selectFieldForGroupByAction = null;
	
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
		
		selectFieldForGroupByAction = new SelectFieldForGroupByAction();
		selectFieldForGroupByAction.setRequestContext(defaultRequestContext);
	}

	/*
	 * Test method for 'it.eng.qbe.action.SelectFieldForGroupByAction.service(SourceBean, SourceBean)'
	 */
	public void testService() {

		try {
			String fieldName = "field name";
			request.setAttribute("COMPLETE_FIELD_NAME",fieldName);
			selectFieldForGroupByAction.service(request, response);
			IGroupByClause groupByClause = singleDataMartWizardObj.getGroupByClause();
			List groupByFields = groupByClause.getGroupByFields();
			assertEquals(4, groupByFields.size());
			IOrderGroupByField aGroupByField = null;
			for (int i = 0; i < groupByFields.size(); i++) {
				aGroupByField = (IOrderGroupByField) groupByFields.get(i);
				if (aGroupByField.getFieldName().equalsIgnoreCase(fieldName)) break;
			}
			assertEquals(fieldName, aGroupByField.getFieldName());
			
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
	}

}
