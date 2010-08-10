package it.eng.spagobi.services.modules;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.dao.hibernate.ParameterDAOHibImpl;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.mockObjects.PortletSessionImplMock;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class DetailParameterModuleDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null;

	private DetailParameterModule parameterModule = null;

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
		RequestContainer.setRequestContainer(reqContainer);
		PortletRequestImplMock portletRequest = new PortletRequestImplMock();
		PortletSessionImplMock portletSession = new PortletSessionImplMock();
		Locale locale = new Locale("it","IT","");
		portletSession.setAttribute("PortalLocale",locale);
		portletRequest.setPortletSession(portletSession);
		//PortletAccess.setPortalLocale(locale);
		reqContainer.setAttribute("PORTLET_REQUEST", portletRequest);
		SessionContainer session = new SessionContainer(true);
		reqContainer.setSessionContainer(session);
		parameterModule = new DetailParameterModule();
		parameterModule.setRequestContext(defaultRequestContext);
		super.setUp();
	}
	

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailParameterModule.getDetailParameter(String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method. Verifies that the correct Parameter is attached to the response.
	 */
	public void testGetDetailParameter() {
		try {
			request.setAttribute("MESSAGEDET",
					AdmintoolsConstants.DETAIL_SELECT);
			request.setAttribute("id", "8");

			parameterModule.service(request, response);
			assertEquals(0, parameterModule.getErrorHandler().getErrors().size());
			assertEquals(new Integer(8), ((Parameter) response
					.getAttribute("parametersObj")).getId());
			
			// second test on exception event
			
			request.updAttribute("id", "6");
			parameterModule.service(request, response);
			Collection errors = parameterModule.getErrorHandler().getErrors();
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
	 * 'it.eng.spagobi.services.modules.DetailParameterModule.modDetailParameter(SourceBean, String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */	
	public void testModDetailParameter() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_MOD);
			request.setAttribute("id", "8");
			request.setAttribute("name", "New parameter");
			request.setAttribute("description", "new des");
			request.setAttribute("label", "new label");
			request.setAttribute("length","400");
			request.setAttribute("mask", "new mask");
			request.setAttribute("type", "new type");
			request.setAttribute("modality", "STRING,49");
			Integer useId= new Integer(18);
			request.setAttribute("useId", useId.toString());
			request.setAttribute("paruseCheckId", "40");
			request.setAttribute("paruseExtRoleId", "1");
			request.setAttribute("paruseLovId", "22");
			request.setAttribute("paruseDescription", "New Modalities");
			request.setAttribute("paruseLabel", "New Modalities");
			request.setAttribute("paruseName", "New Modalities");
			request.setAttribute("saveAndGoBack", "yes");
			parameterModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, parameterModule.getErrorHandler().getErrors().size());
		assertEquals("true", response.getAttribute("loopback"));
		
		try {
			request.updAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.updAttribute("label","new label 2");
			parameterModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, parameterModule.getErrorHandler().getErrors().size());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailParameterModule.newDetailParameter(SourceBean)',
	 * passing through the service(SourceBean, SourceBean) method.
	 */
	public void testNewDetailParameter() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_NEW);

			parameterModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, parameterModule.getErrorHandler().getErrors().size());
		assertEquals(AdmintoolsConstants.DETAIL_INS, response
				.getAttribute("modality"));
		assertEquals(Parameter.class.getName(), response.getAttribute("parametersObj")
				.getClass().getName());
	}
	
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailParameterModule.delDetailParameter(SourceBean, String
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testDelDetailParameter() {
		Collection errors = null;
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.setAttribute("id", "0");
			request.setAttribute("name", "New parameter");
			request.setAttribute("description", "new des");
			request.setAttribute("label", "new label");
			request.setAttribute("length","400");
			request.setAttribute("mask", "new mask");
			request.setAttribute("type", "new type");
			request.setAttribute("modality", "DAVIDE,36");
			parameterModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		ParameterDAOHibImpl parameterDAO= new ParameterDAOHibImpl();
		Integer id=null;
		try {
			List list= parameterDAO.loadAllParameters();
			Parameter parameter=(Parameter) list.get(list.size()-1);
			id=parameter.getId();
		} catch (EMFUserError e1) {
			e1.printStackTrace();
		}
		errors = parameterModule.getErrorHandler().getErrors();
		assertEquals(0, errors.size());
		
		try {
			request.updAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_DEL);
			request.updAttribute("id", id.toString());

			parameterModule.service(request, response);
			errors = parameterModule.getErrorHandler().getErrors();
			assertEquals(0, errors.size());
			
			request.updAttribute("id", "8");
			parameterModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		 
		errors = parameterModule.getErrorHandler().getErrors();
		assertEquals(1, errors.size());
		assertEquals(1017, ((EMFUserError) errors.toArray()[0]).getCode());
	}
	
}
