package it.eng.spagobi.services.modules;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.mockObjects.PortletSessionImplMock;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.Collection;
import java.util.Locale;

public class DetailModalitiesModuleDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null;

	private DetailModalitiesModule modalitiesModule = null;

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
		modalitiesModule = new DetailModalitiesModule();
		modalitiesModule.setRequestContext(defaultRequestContext);
		super.setUp();
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesModule.getDetailModalities(String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method. Verifies that the correct BIObject is attached to the response.
	 */
	public void testGetDetailModalities() {
		Integer useId= new Integer(13);
		try {
			request.setAttribute("MESSAGEDET",
					AdmintoolsConstants.DETAIL_SELECT);
			request.setAttribute("useId", useId.toString());

			modalitiesModule.service(request, response);
			assertEquals(0, modalitiesModule.getErrorHandler().getErrors()
					.size());
			assertEquals(useId, ((ParameterUse) response
					.getAttribute("modalitiesObj")).getUseID());

			// second test on exception event

			request.updAttribute("useId", "6");
			modalitiesModule.service(request, response);
			Collection errors = modalitiesModule.getErrorHandler().getErrors();
			assertEquals(1, errors.size());
			assertEquals(100, ((EMFUserError) errors.toArray()[0]).getCode());
		} catch (Exception e) {
			// Catching only Exception is becuase the behaviuor is the same for
			// all the types of exception thrown
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesModule.service(SourceBean,
	 * SourceBean)' in case there is no message parameters.
	 */
	public void testNullMessage() {
		try {
			modalitiesModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(101, ((EMFUserError) modalitiesModule.getErrorHandler()
				.getErrors().toArray()[0]).getCode());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesModule.modDetailModalities(SourceBean, String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testModDetailModalities() {
		Integer useId= new Integer(13);
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_MOD);
			request.setAttribute("useId", useId.toString());
			request.setAttribute("par_id", "8");
			request.setAttribute("idCheck", "40");
			request.setAttribute("idExtRole", "1");
			request.setAttribute("idLov", "22");
			request.setAttribute("description", "New Modalities");
			request.setAttribute("label", "New Modalities");
			request.setAttribute("name", "New Modalities");

			modalitiesModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, modalitiesModule.getErrorHandler().getErrors().size());
		assertEquals("true", response.getAttribute("loopback"));

		try {
			request.updAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.updAttribute("name", "New Modalities 2");
			modalitiesModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, modalitiesModule.getErrorHandler().getErrors().size());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesModule.nameControl(SourceBean,
	 * String)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testNameControl() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			//request.setAttribute("useId", "13");
			request.setAttribute("par_id", "14");
			request.setAttribute("idCheck", "30");
			request.setAttribute("idExtRole", "7");
			request.setAttribute("idLov", "21");
			request.setAttribute("description", "New Modalities");
			request.setAttribute("label", "OUT_FREE");
			request.setAttribute("name", "Output Format ");

			modalitiesModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(1, modalitiesModule.getErrorHandler().getErrors().size());
		assertEquals(1025, ((EMFUserError) modalitiesModule.getErrorHandler()
				.getErrors().toArray()[0]).getCode());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesModule.newDetailModalities(String, SourceBean)',
	 * passing through the service(SourceBean, SourceBean) method.
	 */
	public void testNewDetailModalities() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_NEW);
			request.setAttribute("PAR_ID", "7");
			modalitiesModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, modalitiesModule.getErrorHandler().getErrors().size());
		assertEquals(AdmintoolsConstants.DETAIL_INS, response
				.getAttribute("modality"));
		assertEquals(ParameterUse.class.getName(), response.getAttribute(
				"modalitiesObj").getClass().getName());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesModule.delDetailModalities(SourceBean, String
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testDelDetailModalities() {
		Collection errors = null;
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_DEL);
			request.setAttribute("useId", "13");

			modalitiesModule.service(request, response);
			errors = modalitiesModule.getErrorHandler().getErrors();
			assertEquals(0, errors.size());

			request.updAttribute("useId", "8");
			modalitiesModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		errors = modalitiesModule.getErrorHandler().getErrors();
		assertEquals(1, errors.size());
		assertEquals(100, ((EMFUserError) errors.toArray()[0]).getCode());
	}

}
