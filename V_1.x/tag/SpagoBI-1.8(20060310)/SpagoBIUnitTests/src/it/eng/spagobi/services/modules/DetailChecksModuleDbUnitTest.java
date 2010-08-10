package it.eng.spagobi.services.modules;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Check;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.mockObjects.PortletSessionImplMock;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.Collection;
import java.util.Locale;

public class DetailChecksModuleDbUnitTest extends DBConnectionTestCase {
	
	private SourceBean request = null, response = null;

	private DetailChecksModule checksModule = null;

	protected void setUp() throws Exception {
		try {
			request = new SourceBean("");
			response = new SourceBean("");
		} catch (SourceBeanException e1) {
			e1.printStackTrace();
		}
		RequestContainer reqContainer = new RequestContainer();
		ResponseContainer resContainer = new ResponseContainer();
		DefaultRequestContext defaultRequestContext = new DefaultRequestContext(
				reqContainer, resContainer);
		reqContainer.setServiceRequest(request);
		resContainer.setServiceResponse(response);
		resContainer.setErrorHandler(new EMFErrorHandler());
		PortletRequestImplMock portletRequest = new PortletRequestImplMock();
		PortletSessionImplMock portletSession = new PortletSessionImplMock();
		Locale locale = new Locale("it","IT","");
		portletSession.setAttribute("LOCALE",locale);
		portletRequest.setPortletSession(portletSession);
		reqContainer.setAttribute("PORTLET_REQUEST", portletRequest);
		checksModule = new DetailChecksModule();
		checksModule.setRequestContext(defaultRequestContext);
		super.setUp();
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailChecksModule.getDettaglioCheck(String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method. Verifies that the correct BIObject is attached to the response.
	 */
	public void testGetDettaglioCheck() {
		try {
			request.setAttribute("MESSAGEDET",
					AdmintoolsConstants.DETAIL_SELECT);
			request.setAttribute("id", "42");

			checksModule.service(request, response);
			assertEquals(0, checksModule.getErrorHandler().getErrors().size());
			assertEquals(new Integer(42), ((Check) response
					.getAttribute("checkObj")).getCheckId());
			
			
			// second test on exception event
			
			request.updAttribute("id", "6");
			checksModule.service(request, response);
			Collection errors = checksModule.getErrorHandler().getErrors();
			assertEquals(1, errors.size());
			assertEquals(100, ((EMFUserError)errors.toArray()[0]).getCode());
		} catch (Exception e) {
			// Catching only Exception is becuase the behaviuor is the same for
			// all the types of exception thrown
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailChecksModule.service(SourceBean,
	 * SourceBean)' in case there is no message parameters.
	 */
	public void testNullMessage() {
		try {
			checksModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(101, ((EMFUserError) checksModule.getErrorHandler()
				.getErrors().toArray()[0]).getCode());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailChecksModule.modDettaglioCheck(SourceBean, String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testModDettaglioCheck() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_MOD);
			request.setAttribute("id", "42");
			request.setAttribute("name", "New Check");
			request.setAttribute("description", "new des");
			request.setAttribute("label", "new label");
			request.setAttribute("CheckType_value1", "First value");
			request.setAttribute("CheckType_value2", "Second value");
			request.setAttribute("checkType", "65;NUMERIC");
			
			checksModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, checksModule.getErrorHandler().getErrors().size());
		assertEquals("true", response.getAttribute("loopback"));
		try {
			request.updAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.updAttribute("name", "New check 2");
			request.updAttribute("label", "new label 2");
			checksModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, checksModule.getErrorHandler().getErrors().size());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailChecksModule.newDettaglioCheck(SourceBean)',
	 * passing through the service(SourceBean, SourceBean) method.
	 */
	public void testNewDettaglioCheck() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_NEW);

			checksModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, checksModule.getErrorHandler().getErrors().size());
		assertEquals(AdmintoolsConstants.DETAIL_INS, response
				.getAttribute("modality"));
		assertEquals(Check.class.getName(), response.getAttribute("checkObj")
				.getClass().getName());
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailChecksModule.delDettaglioCheck(SourceBean, String
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testDelDettaglioCheck() {
		Collection errors = null;
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_DEL);
			request.setAttribute("id", "42");

			checksModule.service(request, response);
			errors = checksModule.getErrorHandler().getErrors();
			assertEquals(0, errors.size());
			
			request.updAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_SELECT);
			checksModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		 
		errors = checksModule.getErrorHandler().getErrors();
		assertEquals(1, errors.size());
		assertEquals(100, ((EMFUserError) errors.toArray()[0]).getCode());
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailChecksModule.labelControl(String,
	 * Integer)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testLabelControl() {
		try {
			checksModule.labelControl("label",new Integer(1));
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertTrue(true);
		
		try {
			checksModule.labelControl("label",new Integer(1));
		} catch (EMFUserError e) {
			//e.printStackTrace();
			assertEquals(1029,e.getCode());
		}

		try {
			checksModule.labelControl("Numeric",new Integer(10));
		} catch (EMFUserError e) {
//			e.printStackTrace();
			assertEquals(1029,e.getCode());
		}
		assertTrue(true);
		
		try {
			checksModule.labelControl("Numeric",new Integer(31));
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertTrue(true);
	}

}
