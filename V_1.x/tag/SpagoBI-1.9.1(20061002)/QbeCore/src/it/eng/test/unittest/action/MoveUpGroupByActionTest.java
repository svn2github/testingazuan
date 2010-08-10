package it.eng.test.unittest.action;

import it.eng.qbe.action.MoveUpGroupByAction;
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

public class MoveUpGroupByActionTest extends TestCase {

	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private MoveUpGroupByAction moveUpGroupByAction = null;
	
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
		
		moveUpGroupByAction = new MoveUpGroupByAction();
		moveUpGroupByAction.setRequestContext(defaultRequestContext);
	}
	
	/*
	 * Test method for 'it.eng.qbe.action.MoveUpGroupByAction.service(SourceBean, SourceBean)'
	 */
	public void testService() {
		
		try {
			String fieldId = "3";
			request.setAttribute("FIELD_ID", fieldId);
			moveUpGroupByAction.service(request, response);
			IGroupByClause groupByClause = singleDataMartWizardObj.getGroupByClause();
			List groupByFields = groupByClause.getGroupByFields();
			assertEquals(3, groupByFields.size());
			IOrderGroupByField field1 = (IOrderGroupByField) groupByFields.get(0);
			assertEquals("1", field1.getId());
			IOrderGroupByField field2 = (IOrderGroupByField) groupByFields.get(1);
			assertEquals("3", field2.getId());
			IOrderGroupByField field3 = (IOrderGroupByField) groupByFields.get(2);
			assertEquals("2", field3.getId());
			
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}

	}

}
