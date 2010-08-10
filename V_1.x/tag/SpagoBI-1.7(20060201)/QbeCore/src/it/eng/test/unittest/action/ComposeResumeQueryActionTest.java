package it.eng.test.unittest.action;

import it.eng.qbe.action.ComposeResumeQueryAction;
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


public class ComposeResumeQueryActionTest extends TestCase {
	
	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private ComposeResumeQueryAction composeResumeQueryAction = null;
	
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
		DefaultRequestContext defaultRequestContext = new DefaultRequestContext(reqContainer, resContainer);
		resContainer.setErrorHandler(new EMFErrorHandler());
		singleDataMartWizardObj = CreateInitialSingleDataMartWizardObject.createInitialSingleDataMartWizardObject();
		session.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, singleDataMartWizardObj);
		composeResumeQueryAction = new ComposeResumeQueryAction();
		composeResumeQueryAction.setRequestContext(defaultRequestContext);
	}
	

	public void testService() {
		try {
			composeResumeQueryAction.service(request, response);
			singleDataMartWizardObj = (ISingleDataMartWizardObject)session.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
			String queryFinal =  singleDataMartWizardObj.getFinalQuery();
			assertNotNull(queryFinal);
		} catch (Exception e) {
			fail("Unaspected exception occurred!");
			e.printStackTrace();
		} finally {
			
		}
		

	}	
}
