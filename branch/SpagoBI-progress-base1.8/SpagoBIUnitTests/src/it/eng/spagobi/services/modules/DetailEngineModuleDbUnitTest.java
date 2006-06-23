package it.eng.spagobi.services.modules;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.mockObjects.PortletSessionImplMock;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.Collection;
import java.util.Locale;

/**
 * Is this the case to control NumberFormatException in ours modules?<br>
 * If <code>request.getAttribute("MESSAGEDET")</code> is not equal to any of
 * <b>AdmintoolsConstants</b> the service method of the module do nothing and
 * doesn't alert.
 * 
 */

public class DetailEngineModuleDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null;

	private DetailEngineModule engineModule = null;

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
		engineModule = new DetailEngineModule();
		engineModule.setRequestContext(defaultRequestContext);
		super.setUp();
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailEngineModule.getDettaglioEngine(String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method. Verifies that the correct Engine is attached to the response.
	 */
	public void testGetDettaglioEngine() {
		try {
			request.setAttribute("MESSAGEDET",
					AdmintoolsConstants.DETAIL_SELECT);
			request.setAttribute("id", "7");

			engineModule.service(request, response);
			assertEquals(0, engineModule.getErrorHandler().getErrors().size());
			assertEquals(new Integer(7), ((Engine) response
					.getAttribute("engineObj")).getId());
			
			
			// second test on exception event
			
			request.updAttribute("id", "6");
			engineModule.service(request, response);
			Collection errors = engineModule.getErrorHandler().getErrors();
			assertEquals(1, errors.size());
			assertEquals(1014, ((EMFUserError)errors.toArray()[0]).getCode());
		} catch (Exception e) {
			// Catching only Exception is becuase the behaviuor is the same for
			// all the types of exception thrown
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailEngineModule.service(SourceBean,
	 * SourceBean)' in case there is no message parameters.
	 */
	public void testNullMessage() {
		try {
			engineModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(101, ((EMFUserError) engineModule.getErrorHandler()
				.getErrors().toArray()[0]).getCode());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailEngineModule.modDettaglioEngine(SourceBean, String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testModDettaglioEngine() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_MOD);
			request.setAttribute("id", "7");
			request.setAttribute("name", "New Engine");
			request.setAttribute("description", "new des");
			request.setAttribute("label", "new label");
			request.setAttribute("url", "http//new/url");
			request.setAttribute("secondaryUrl", "http://new/sec/url");
			request.setAttribute("dirUpload", "new/upload/path");
			request.setAttribute("dirUsable", "new/usable/path");
			request.setAttribute("driverName", "NewDriver");
			request.setAttribute("criptable", "0");

			engineModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, engineModule.getErrorHandler().getErrors().size());
		assertEquals("true", response.getAttribute("loopback"));
		try {
			request.updAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.updAttribute("label", "new label 2");
			engineModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, engineModule.getErrorHandler().getErrors().size());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailEngineModule.newDettaglioEngine(SourceBean)',
	 * passing through the service(SourceBean, SourceBean) method.
	 */
	public void testNewDettaglioEngine() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_NEW);

			engineModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, engineModule.getErrorHandler().getErrors().size());
		assertEquals(AdmintoolsConstants.DETAIL_INS, response
				.getAttribute("modality"));
		assertEquals(Engine.class.getName(), response.getAttribute("engineObj")
				.getClass().getName());
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailEngineModule.delDettaglioEngine(SourceBean, String
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testDelDettaglioEngine() {
		Collection errors = null;
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_DEL);
			request.setAttribute("id", "7");

			engineModule.service(request, response);
			errors = engineModule.getErrorHandler().getErrors();
			assertEquals(0, errors.size());
			
			request.updAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_SELECT);
			engineModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		 
		errors = engineModule.getErrorHandler().getErrors();
		assertEquals(1, errors.size());
		assertEquals(1014, ((EMFUserError) errors.toArray()[0]).getCode());
	}
}
