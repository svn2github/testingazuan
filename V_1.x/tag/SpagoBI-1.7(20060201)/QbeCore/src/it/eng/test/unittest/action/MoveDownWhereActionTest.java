package it.eng.test.unittest.action;

import it.eng.qbe.action.MoveDownWhereAction;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereClause;
import it.eng.qbe.wizard.WhereFieldSourceBeanImpl;
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

public class MoveDownWhereActionTest extends TestCase {
	
	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private MoveDownWhereAction moveDownWhereAction = null;
	
	
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
		moveDownWhereAction = new MoveDownWhereAction();
		moveDownWhereAction.setRequestContext(defaultRequestContext);
	}
	

	public void testService() {
		try {
			request.setAttribute("FIELD_ID","1");
			moveDownWhereAction.service(request, response);
			ISingleDataMartWizardObject singleDataMartWizardObj = 
				(ISingleDataMartWizardObject)session.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
			IWhereClause whereClause = singleDataMartWizardObj.getWhereClause();
			List fields = whereClause.getWhereFields();
			WhereFieldSourceBeanImpl field1 = (WhereFieldSourceBeanImpl)fields.get(0);
			String id1 = field1.getId();	
            assertEquals("2", id1);
            WhereFieldSourceBeanImpl field2 = (WhereFieldSourceBeanImpl)fields.get(1);
			String id2 = field2.getId();	
            assertEquals("1", id2);
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		} 
		

	}
	
}
