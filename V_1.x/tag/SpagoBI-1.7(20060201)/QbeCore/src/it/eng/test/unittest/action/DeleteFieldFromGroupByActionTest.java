package it.eng.test.unittest.action;

import java.util.Iterator;
import java.util.List;

import it.eng.qbe.action.DeleteFieldFromGroupByAction;
import it.eng.qbe.wizard.GroupByFieldSourceBeanImpl;
import it.eng.qbe.wizard.IGroupByClause;
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

public class DeleteFieldFromGroupByActionTest extends TestCase {

	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private DeleteFieldFromGroupByAction deleteFieldFromGroupByAction = null;
	
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
		
		deleteFieldFromGroupByAction = new DeleteFieldFromGroupByAction();
		deleteFieldFromGroupByAction.setRequestContext(defaultRequestContext);
	}
	
	/*
	 * Test method for 'it.eng.qbe.action.DeleteFieldFromGroupByAction.service(SourceBean, SourceBean)'
	 */
	public void testService() {
		
		try {
			
			request.setAttribute("FIELD_ID","1");
			deleteFieldFromGroupByAction.service(request, response);
			IGroupByClause groupByClause = singleDataMartWizardObj.getGroupByClause();
			List fields = groupByClause.getGroupByFields();
			
			assertEquals(2, fields.size());
			
			Iterator iter = fields.iterator();
			while(iter.hasNext()){
				GroupByFieldSourceBeanImpl field = (GroupByFieldSourceBeanImpl)iter.next();
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
