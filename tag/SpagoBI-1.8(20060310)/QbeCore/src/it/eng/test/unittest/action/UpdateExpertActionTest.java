package it.eng.test.unittest.action;

import it.eng.qbe.action.UpdateExpertAction;
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
import junit.framework.TestCase;

public class UpdateExpertActionTest extends TestCase {

	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private UpdateExpertAction updateExpertAction = null;
	
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
		
		updateExpertAction = new UpdateExpertAction();
		updateExpertAction.setRequestContext(defaultRequestContext);
	}

	/*
	 * Test method for 'it.eng.qbe.action.UpdateExpertAction.service(SourceBean, SourceBean)'
	 */
	public void testService() {

		try {
			String expertString = "EXPERT STRING";
			request.setAttribute("expertSelectTA", expertString); 
			updateExpertAction.service(request, response);
			assertEquals(expertString, singleDataMartWizardObj.getExpertQuerySaved());
			assertEquals(expertString, singleDataMartWizardObj.getExpertQueryDisplayed());
			
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
		
	}

}
