package it.eng.test.unittest.action;

import it.eng.qbe.action.DeleteFieldFromOrderByAction;
import it.eng.qbe.wizard.IOrderByClause;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.OrderByFieldSourceBeanImpl;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.test.utilities.CreateInitialSingleDataMartWizardObject;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class DeleteFieldFromOrderByActionTest extends TestCase {

	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private DeleteFieldFromOrderByAction deleteFieldFromOrderByAction = null;
	
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
		deleteFieldFromOrderByAction = new DeleteFieldFromOrderByAction();
		deleteFieldFromOrderByAction.setRequestContext(defaultRequestContext);
	}
	

	public void testService() {
		try {
			request.setAttribute("FIELD_ID","1");
			deleteFieldFromOrderByAction.service(request, response);
			
			IOrderByClause orderByClause = singleDataMartWizardObj.getOrderByClause();
			List fields = orderByClause.getOrderByFields();
			assertEquals(2, fields.size());
			Iterator iter = fields.iterator();
			while(iter.hasNext()){
				OrderByFieldSourceBeanImpl field = (OrderByFieldSourceBeanImpl)iter.next();
				if (field.getId().equalsIgnoreCase("1")) {
					fail("The field has not been deleted");
				} 
			}
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		} 
		
	}
	
}
