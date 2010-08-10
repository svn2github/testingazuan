package it.eng.spagobi.presentation.publishers;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import junit.framework.TestCase;

public class DynamicForwardPublisherJUnitTest extends TestCase {

	private SourceBean request = null, response = null;
	
	private RequestContainer reqContainer = null;

	private ResponseContainer resContainer = null;
	
	private DynamicForwardPublisher dynamicForwardPublisher = null;

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
		dynamicForwardPublisher = new DynamicForwardPublisher();
		super.setUp();
	}
	
	/**
	 * Test method for 'it.eng.spagobi.presentation.publishers.DynamicForwardPublisher.getPublisherName(RequestContainer, ResponseContainer)'.
	 * Verifies that the correct Publisher name is returned.
	 */
	public void testGetPublisherName() {
		String publisherName = "Pablicher name";
		try {
			request.setAttribute("PUBLISHER_NAME",publisherName);
			String returned = dynamicForwardPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals(publisherName,returned);
			
			request.delAttribute("PUBLISHER_NAME");
			returned = dynamicForwardPublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("SERVICE_ERROR_PUBLISHER",returned);
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
	}

}
