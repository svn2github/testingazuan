package it.eng.spagobi.services.modules;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.cms.init.CMSInitializer;
import it.eng.spago.cms.init.CMSManager;
import it.eng.spago.cms.util.CMSRecovery;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.mockObjects.PortletSessionImplMock;
import it.eng.spagobi.security.AnonymousCMSUserProfile;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.Collection;
import java.util.Locale;

public class DetailFunctionalityModuleDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null;

	private DetailFunctionalityModule funcionalityModule = null;

	protected void setUp() throws Exception {
		super.setUp();
		setupContext();
		try {
			CMSManager.getInstance();
		} catch (Exception e) {
			ConfigSingleton configSingleton = ConfigSingleton.getInstance();
			SourceBean contentConfig = (SourceBean) configSingleton
					.getAttribute("CONTENTCONFIGURATION");
			CMSInitializer cmsInit = new CMSInitializer();
			cmsInit.init(contentConfig);
		}
	}

	protected void tearDown() throws Exception{
		super.tearDown();
		CMSManager.destroyInstance();
		CMSRecovery.setupCms();
	}
	
	private void setupContext() {
		try {
			request = new SourceBean("");
			response = new SourceBean("");
		} catch (SourceBeanException e1) {
			e1.printStackTrace();
		}
		RequestContainer reqContainer = new RequestContainer();
		ResponseContainer resContainer = new ResponseContainer();
		reqContainer.setServiceRequest(request);
		resContainer.setServiceResponse(response);
		DefaultRequestContext defaultRequestContext = new DefaultRequestContext(
				reqContainer, resContainer);
		resContainer.setErrorHandler(new EMFErrorHandler());
		RequestContainer.setRequestContainer(reqContainer);
		PortletRequestImplMock portletRequest = new PortletRequestImplMock();
		PortletSessionImplMock portletSession = new PortletSessionImplMock();
		Locale locale = new Locale("it","IT","");
		portletSession.setAttribute("LOCALE",locale);
		portletRequest.setPortletSession(portletSession);
		reqContainer.setAttribute("PORTLET_REQUEST", portletRequest);
		SessionContainer session = new SessionContainer(true);
		reqContainer.setSessionContainer(session);
		SessionContainer permSession = session.getPermanentContainer();
		IEngUserProfile profile = new AnonymousCMSUserProfile();
		permSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
		try {
			request.setAttribute("FUNCTIONALITY_TYPE","LOW_FUNCT");
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}

		funcionalityModule = new DetailFunctionalityModule();
		funcionalityModule.setRequestContext(defaultRequestContext);
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailFunctionalityModule.getDetailFunctionality(SourceBean,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method. Verifies that the correct LowFunctionality is attached to the response.
	 */
	public void testGetDetailFunctionality() {
		try {
			String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting";
			request.setAttribute("MESSAGEDET",
					AdmintoolsConstants.DETAIL_SELECT);
			request.setAttribute("PATH", path);
			funcionalityModule.service(request, response);
			assertEquals(0, funcionalityModule.getErrorHandler().getErrors().size());
			assertEquals(new Integer(7), ((LowFunctionality) response
					.getAttribute("FUNCTIONALITY_OBJ")).getId());
			
			// second test on exception event
			
			request.updAttribute("PATH", "new path");
			funcionalityModule.service(request, response);
			Collection errors = funcionalityModule.getErrorHandler().getErrors();
			assertEquals(1, errors.size());
			assertEquals(100, ((EMFUserError)errors.toArray()[0]).getCode());
		} catch (Exception e) {
			// Catching only Exception is becuase the behaviuor is the same for
			// all the types of exception thrown
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailFuncionalityModule.service(SourceBean,
	 * SourceBean)' in case there is no message parameters.
	 */
	public void testNullMessage() {
		try {
			funcionalityModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
		assertEquals(101, ((EMFUserError) funcionalityModule.getErrorHandler()
				.getErrors().toArray()[0]).getCode());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailFunctionalityModule.modDettaglioFunctionality(SourceBean, String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testModDettaglioFunctionality() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_MOD);
			request.setAttribute("name","TEMP");
			request.setAttribute("description", "new des");
			request.setAttribute("code", "DASH");
			request.setAttribute("test", "3");
			request.setAttribute("test", "4");
			request.setAttribute("test", "7");
			request.setAttribute("development", "7");
			request.setAttribute("development", "14");
			request.setAttribute("development", "15");
			request.setAttribute("execution", "1");
			request.setAttribute("execution", "5");
			request.setAttribute("FUNCTIONALITY_ID", "10");

			funcionalityModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
		assertEquals(0, funcionalityModule.getErrorHandler().getErrors().size());
		assertEquals("true", response.getAttribute("loopback"));
		
		try {
			String pathParent = "/Functionalities/SystemFunctionalities/Analytical Areas";
			request.updAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.setAttribute("PATH_PARENT",pathParent);
			request.updAttribute("code", "TEMP");
			funcionalityModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(true);
		}
		assertEquals(0, funcionalityModule.getErrorHandler().getErrors().size());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailFunctionalityModule.newDettaglioFunctionality(SourceBean, SourceBean)',
	 * passing through the service(SourceBean, SourceBean) method.
	 */
	public void testNewDettaglioFunctionality() {
		try {
			String path = "/Functionalities/SystemFunctionalities/Analytical Areas/TEMP";
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_NEW);
			request.setAttribute("PATH_PARENT", path);
			funcionalityModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
		assertEquals(0, funcionalityModule.getErrorHandler().getErrors().size());
		assertEquals(AdmintoolsConstants.DETAIL_INS, response
				.getAttribute("modality"));
		assertEquals(LowFunctionality.class.getName(), response.getAttribute("FUNCTIONALITY_OBJ")
				.getClass().getName());
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailFunctionalityModule.delDettaglioFunctionality(SourceBean, String
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testDelDettaglioFunctionality() {
		Collection errors = null;
		try {
			String path = "/Functionalities/SystemFunctionalities/Business Departments";
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_DEL);
			request.setAttribute("PATH",path);
			funcionalityModule.service(request, response);
			errors = funcionalityModule.getErrorHandler().getErrors();
			assertEquals(0, errors.size());
			
			request.updAttribute("PATH", "new path");
			funcionalityModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(true);
		}
		 
		errors = funcionalityModule.getErrorHandler().getErrors();
		assertEquals(1, errors.size());
		assertEquals(100, ((EMFUserError) errors.toArray()[0]).getCode());
	}

}
