package it.eng.spagobi.presentation.publishers;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;

import java.util.Collection;

import junit.framework.TestCase;

public class DetailModalitiesValuePublisherJUnitTest extends TestCase {

	private SourceBean request = null, response = null;
	
	private RequestContainer reqContainer = null;

	private ResponseContainer resContainer = null;
	
	private DetailModalitiesValuePublisher detailModalitiesValuePublisher = null;

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
		detailModalitiesValuePublisher = new DetailModalitiesValuePublisher();
		super.setUp();
	}
	
	/**
	 * Test method for 'it.eng.spagobi.presentation.publishers.DetailModalitiesValuePublisher.getPublisherName(RequestContainer, ResponseContainer)'.
	 * Verifies that the correct Publisher name is returned.
	 */
	public void testGetPublisherName() {
		String publisherName = null;
		SourceBean detailModuleResponse = null;
		SourceBean listTestQueryModule = null;
		SourceBean listTestScriptModule = null;
		String detailModuleName = "DetailModalitiesValueModule";
		String listTestQueryModuleName = "ListTestQueryModule";
		String listTestScriptModuleName = "ListTestScriptModule";
		EMFErrorHandler errorHandler = resContainer.getErrorHandler();
		try {
			
			// only detailModuleResponse with attribute loopback
			detailModuleResponse = new SourceBean(detailModuleName);
			listTestQueryModule = new SourceBean(listTestQueryModuleName);
			listTestScriptModule = new SourceBean(listTestScriptModuleName);
			detailModuleResponse.setAttribute("loopback","");
			response.setAttribute(detailModuleResponse);
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("detailModalitiesValueLoop",publisherName);
			
			detailModuleResponse.delAttribute("loopback");
			// only detailModuleResponse with no attributes
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("detailModalitiesValue",publisherName);
			
			// only detailModuleResponse with attribute testedObject in case of MAN_IN as input type
			detailModuleResponse.setAttribute("testedObject","MAN_IN");
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("detailLovTestResult",publisherName);
			
			detailModuleResponse.delAttribute("testedObject");
			// only detailModuleResponse with attribute testedObject in case of FIX_LOV as input type
			detailModuleResponse.setAttribute("testedObject","FIX_LOV");
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("detailLovTestResult",publisherName);
			
			detailModuleResponse.delAttribute("testedObject");
			// only detailModuleResponse with attribute testedObject in case of QUERY as input type
			detailModuleResponse.setAttribute("testedObject","QUERY");
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("error",publisherName);
			Collection errors = errorHandler.getErrors();
			assertEquals(1, errors.size());
			assertEquals(10, ((EMFUserError)errors.toArray()[0]).getCode());
			
			errorHandler.clear();
			resContainer.setErrorHandler(errorHandler);
			
			// detailModuleResponse with attribute testedObject in case of QUERY as input type and listTestQueryModule with no attributes 
			response.setAttribute(listTestQueryModule);
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("error",publisherName);
			
			// detailModuleResponse with attribute testedObject in case of QUERY as input type and listTestQueryModule with testExecuted attribute
			listTestQueryModule.setAttribute("testExecuted", "yes");
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("detailLovTestResult",publisherName);
			
			response.delAttribute(listTestQueryModuleName);
			// detailModuleResponse with attribute testedObject in case of QUERY as input type and listTestScriptModule
			response.setAttribute(listTestScriptModule);
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("error",publisherName);
			errors = errorHandler.getErrors();
			assertEquals(1, errors.size());
			assertEquals(10, ((EMFUserError)errors.toArray()[0]).getCode());
			
			errorHandler.clear();
			resContainer.setErrorHandler(errorHandler);
			
			// detailModuleResponse with attribute testedObject in case of SCRIPT as input type and listTestScriptModule with testExecuted attribute
			detailModuleResponse.delAttribute("testedObject");
			detailModuleResponse.setAttribute("testedObject", "SCRIPT_LIST_OF_VALUES");
			listTestScriptModule.setAttribute("testExecuted", "yes");
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("detailLovTestResult",publisherName);
			
			// only listTestScriptModule with testExecuted attribute
			response.delAttribute(detailModuleName);
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("detailLovTestResult",publisherName);
			
			// no detailModuleResponse and both listTestScriptModule and listTestQueryModule
			response.setAttribute(listTestQueryModule);
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("error",publisherName);
			errors = errorHandler.getErrors();
			assertEquals(1, errors.size());
			assertEquals(10, ((EMFUserError)errors.toArray()[0]).getCode());
			
			errorHandler.clear();
			resContainer.setErrorHandler(errorHandler);
			
//			detailModuleResponse.setAttribute(SpagoBIConstants.WIZARD,SpagoBIConstants.WIZARD_QUERY);
//			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
//			assertEquals("querySelectionWizard",publisherName);
//			
//			detailModuleResponse.updAttribute(SpagoBIConstants.WIZARD,SpagoBIConstants.WIZARD_FIX_LOV);
//			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
//			assertEquals("lovSelectionWizard",publisherName);
//			
//			detailModuleResponse.updAttribute(SpagoBIConstants.WIZARD,SpagoBIConstants.WIZARD_SCRIPT);
//			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
//			assertEquals("scriptSelectionWizard",publisherName);
			
			// void response
			response.delAttribute(listTestScriptModuleName);
			response.delAttribute(listTestQueryModuleName);
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("error",publisherName);
			errors = resContainer.getErrorHandler().getErrors();
			assertEquals(1, errors.size());
			assertEquals(10, ((EMFUserError) errors.toArray()[0]).getCode());
			
			// error handler not empty
			response.setAttribute(detailModuleResponse);
			publisherName = detailModalitiesValuePublisher.getPublisherName(reqContainer,resContainer);
			assertEquals("error",publisherName);
			
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
	}

}
