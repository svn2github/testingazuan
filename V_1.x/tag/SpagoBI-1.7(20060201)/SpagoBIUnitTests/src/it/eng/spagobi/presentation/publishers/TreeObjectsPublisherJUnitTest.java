package it.eng.spagobi.presentation.publishers;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;

import java.util.Collection;

import junit.framework.TestCase;

public class TreeObjectsPublisherJUnitTest extends TestCase {

	private SourceBean request = null, response = null;
	
	private RequestContainer reqContainer = null;

	private ResponseContainer resContainer = null;
	
	private TreeObjectsPublisher treeObjectsPublisher = null;

	protected void setUp() throws Exception {
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
		resContainer.setErrorHandler(new EMFErrorHandler());
		treeObjectsPublisher = new TreeObjectsPublisher();
		super.setUp();
	}
	
	/**
	 * Test method for 'it.eng.spagobi.presentation.publishers.TreeObjectsPublisher.getPublisherName(RequestContainer, ResponseContainer)'.
	 * Verifies that the correct Publisher name is returned.
	 */
	public void testGetPublisherName() {
		String publisherName = null;
		SourceBean moduleResponse = null;
		String moduleName = "TreeObjectsModule";
		try {
			moduleResponse = new SourceBean(moduleName);
			moduleResponse.setAttribute("ACTOR","Davide");
			response.setAttribute(moduleResponse);
			publisherName = treeObjectsPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("",publisherName);
			
			moduleResponse.updAttribute("ACTOR","DEV_ACTOR");
			publisherName = treeObjectsPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("treeDevObjects",publisherName);
			
			moduleResponse.updAttribute("ACTOR","USER_ACTOR");
			publisherName = treeObjectsPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("treeExecObjects",publisherName);
			
			moduleResponse.updAttribute("ACTOR","TESTER_ACTOR");
			publisherName = treeObjectsPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("treeExecObjects",publisherName);
			
			moduleResponse.updAttribute("ACTOR","ADMIN_ACTOR");
			publisherName = treeObjectsPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("treeAdminObjects",publisherName);
			
			request.setAttribute("OPERATION","operation");
			publisherName = treeObjectsPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("treeAdminObjects",publisherName);
			
			request.updAttribute("OPERATION","FUNCTIONALITIES_OPERATION");
			publisherName = treeObjectsPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("treeFunctionalities",publisherName);
			
			response.delAttribute(moduleName);
			publisherName = treeObjectsPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("error",publisherName);
			Collection errors = resContainer.getErrorHandler().getErrors();
			assertEquals(1, errors.size());
			assertEquals(10, ((EMFUserError)errors.toArray()[0]).getCode());
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
	}

}
