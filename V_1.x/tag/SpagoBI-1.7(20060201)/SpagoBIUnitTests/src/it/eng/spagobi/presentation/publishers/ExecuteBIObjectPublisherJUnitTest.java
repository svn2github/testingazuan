package it.eng.spagobi.presentation.publishers;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;

import java.util.Collection;

import junit.framework.TestCase;

public class ExecuteBIObjectPublisherJUnitTest extends TestCase {

	private SourceBean request = null, response = null;
	
	private RequestContainer reqContainer = null;

	private ResponseContainer resContainer = null;
	
	private ExecuteBIObjectPublisher executeBIObjectPublisher = null;

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
		executeBIObjectPublisher = new ExecuteBIObjectPublisher();
		super.setUp();
	}
	
	
	/**
	 * Test method for 'it.eng.spagobi.presentation.publishers.ExecuteBIObjectPublisher.getPublisherName(RequestContainer, ResponseContainer)'.
	 * Verifies that the correct Publisher name is returned.
	 */
	public void testGetPublisherName() {
		String publisherName = null;
		SourceBean moduleResponse = null;
		String moduleName = "ExecuteBIObjectModule";
		try {
			moduleResponse = new SourceBean(moduleName);
			moduleResponse.setAttribute("isLoop","");
			moduleResponse.setAttribute("Actor","Davide");
			response.setAttribute(moduleResponse);
			publisherName = executeBIObjectPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("LoopTree",publisherName);
			assertNull(moduleResponse.getAttribute("isLoop"));
			
			publisherName = executeBIObjectPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("ExecuteBIObjectPageParameter",publisherName);
			
			moduleResponse.setAttribute("selectionRoleForExecution","selectionRoleForExecution");
			publisherName = executeBIObjectPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("ExecuteBIObjectSelectRole",publisherName);
			
			moduleResponse.setAttribute("EXECUTION","EXECUTION");
			publisherName = executeBIObjectPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("ExecuteBIObjectPageExecution",publisherName);
			
			response.delAttribute(moduleName);
			publisherName = executeBIObjectPublisher.getPublisherName(reqContainer,resContainer);
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
