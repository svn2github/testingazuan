package it.eng.spagobi.presentation.publishers;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import junit.framework.TestCase;

public class ListLovsPublisherJUnitTest extends TestCase {
	
	private SourceBean request = null, response = null;
	
	private RequestContainer reqContainer = null;

	private ResponseContainer resContainer = null;
	
	private ListLovsPublisher listLovsPublisher = null;

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
		listLovsPublisher = new ListLovsPublisher();
		super.setUp();
	}
	
	/**
	 * Test method for 'it.eng.spagobi.presentation.publishers.ListLovsPublisher.getPublisherName(RequestContainer, ResponseContainer)'.
	 * Verifies that the correct Publisher name is returned.
	 */
	public void testGetPublisherName() {
		String returned = listLovsPublisher.getPublisherName(reqContainer,resContainer);
		assertEquals("listLovs",returned);

		EMFErrorHandler errorHandler = resContainer.getErrorHandler();
		errorHandler.addError(new EMFUserError(EMFErrorSeverity.WARNING,99999));
		returned = listLovsPublisher.getPublisherName(reqContainer,resContainer);
		assertEquals("listLovs",returned);
		
		errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,99999));
		returned = listLovsPublisher.getPublisherName(reqContainer,resContainer);
		assertEquals("error",returned);
	}
	
}
