package it.eng.spagobi.services.modules;

import it.eng.spago.base.PortletAccess;
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
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.mockObjects.PortletSessionImplMock;
import it.eng.spagobi.security.AnonymousCMSUserProfile;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;
import it.eng.spagobi.utilities.UploadedFile;

import java.util.Collection;
import java.util.Locale;

public class DetailBIObjectModuleDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null;

	private DetailBIObjectModule biobjModule = null;

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
		portletSession.setAttribute("PortalLocale",locale);
		portletRequest.setPortletSession(portletSession);
		reqContainer.setAttribute("PORTLET_REQUEST", portletRequest);
		PortletAccess.setPortletRequest(portletRequest);
		//PortletAccess.setPortalLocale(locale);
		SessionContainer session = new SessionContainer(true);
		reqContainer.setSessionContainer(session);
		SessionContainer permSession = session.getPermanentContainer();
		IEngUserProfile profile = new AnonymousCMSUserProfile();
		permSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
		try {
			request.setAttribute("ACTOR", "Davide");
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}

		biobjModule = new DetailBIObjectModule();
		biobjModule.setRequestContext(defaultRequestContext);
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailBIObjectModule.getDetailObject(SourceBean,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method. Verifies that the correct BIObject is attached to the response.
	 */
	public void testGetDetailObject() {
		try {
			request.setAttribute("MESSAGEDET",
					AdmintoolsConstants.DETAIL_SELECT);
			request
					.setAttribute(
							"PATH",
							"/Functionalities/SystemFunctionalities/Analytical Areas" +
							"/Static Reporting/REPORT_RPT_CUST_01");
			biobjModule.service(request, response);
			assertEquals(0, biobjModule.getErrorHandler().getErrors().size());
			assertEquals(new Integer(13), ((BIObject) response
					.getAttribute("BIObjects")).getId());
			String responseComplete = (String) response.getAttribute(SpagoBIConstants.RESPONSE_COMPLETE);
			assertEquals("true", responseComplete);
			
			// second test on exception event
			
			request.updAttribute("PATH", "new path");
			biobjModule.service(request, response);
			Collection errors = biobjModule.getErrorHandler().getErrors();
			assertEquals(1, errors.size());
			assertEquals(1040, ((EMFUserError)errors.toArray()[0]).getCode());
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
	 * 'it.eng.spagobi.services.modules.DetailBIObjectModule.service(SourceBean,
	 * SourceBean)' in case there is no message parameters.
	 */
	public void testNullMessage() {
		try {
			biobjModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
		assertEquals(101, ((EMFUserError) biobjModule.getErrorHandler()
				.getErrors().toArray()[0]).getCode());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailBIObjectModule.modBIObject(SourceBean, String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testModBIObject() {
		try {
			// modification case
			UploadedFile template = new UploadedFile();
			template.setFileName("Report.temp");
			byte[] content = {'a','f','f','4','g','O','D'};
			template.setFileContent(content);
			SourceBean request = new SourceBean("");
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_MOD);
			request.setAttribute(SpagoBIConstants.ACTOR, "Davide");
			request.setAttribute("UPLOADED_FILE", template);
			request.setAttribute("id", "13");
			request.setAttribute("name", "New BIObject");
			request.setAttribute("description", "new des");
			request.setAttribute("label", "TEMP");
			request.setAttribute("relname", "Rel name");
			request.setAttribute("criptable", "50");
			request.setAttribute("type", "36,type_code");
			request.setAttribute("engine", "9");
			request.setAttribute("state", "37,state_code");
			request
					.setAttribute(
							"PATH",
							"/Functionalities/SystemFunctionalities/Analytical Areas/" +
							"Static Reporting/REPORT_RPT_CUST_01");
			request.setAttribute("versionTemplate", "1.1");
			
			request.setAttribute("objParLabel", "New Parameter");
			request.setAttribute("parurl_nm", "Par");
			request.setAttribute("objParIdOld","-1");
			request.setAttribute("par_id","9");
			request.setAttribute("req_fl","0");
			request.setAttribute("mod_fl","0");
			request.setAttribute("view_fl","0");
			request.setAttribute("mult_fl","0");

			biobjModule.service(request, response);
	
			assertEquals(0, biobjModule.getErrorHandler().getErrors().size());
			assertEquals("true", response.getAttribute(SpagoBIConstants.RESPONSE_COMPLETE));
			
			// error case: puts in request the id of a parameter yet in use
			request.updAttribute("par_id", "8");
			
			biobjModule.service(request, response);
	
			Collection errors = biobjModule.getErrorHandler().getErrors();
			assertEquals(1, errors.size());
			assertEquals(1026, ((EMFUserError)errors.toArray()[0]).getCode());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(true);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailBIObjectModule.newBIObject(SourceBean,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testNewBIObject() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_NEW);
			biobjModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
		assertEquals(0, biobjModule.getErrorHandler().getErrors().size());
		assertEquals(AdmintoolsConstants.DETAIL_INS, response
				.getAttribute("modality"));
		assertEquals(BIObject.class.getName(), response.getAttribute("BIObjects")
				.getClass().getName());
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailBIObjectModule.delDetailObject(SourceBean, String
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testDelDetailObject() {
		Collection errors = null;
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_DEL);
			String path = "/Functionalities/SystemFunctionalities/Analytical Areas"
					+ "/Static Reporting/REPORT_RPT_CUST_01";
			request.setAttribute("PATH", path);

			biobjModule.service(request, response);
			errors = biobjModule.getErrorHandler().getErrors();
			assertEquals(0, errors.size());
			assertEquals(response.getAttribute("loopback"), "true");
			assertEquals(response.getAttribute(ObjectsTreeConstants.PATH_PARENT), path);
			
			request.updAttribute("PATH", "new path");
			biobjModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(true);
		}
		 
		errors = biobjModule.getErrorHandler().getErrors();
		assertEquals(1, errors.size());
		assertEquals(100, ((EMFUserError) errors.toArray()[0]).getCode());
	}


	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailBIObjectModule.validateBIObject(String,
	 * SourceBean, EMFErrorHandler)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testValidateBIObject() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			SourceBean myrequest = new SourceBean("");
			myrequest.setAttribute("label",""); // this fild is mandatory, so the validation must return false
			myrequest.setAttribute("name","Name");
			myrequest.setAttribute("description","Description");
			myrequest.setAttribute("relName","Rel Name");
			myrequest.setAttribute("engine","9");
			myrequest.setAttribute("state","DEV");
			myrequest.setAttribute("PATH_PARENT","Path_Parent");
			RequestContainer reqContainer = RequestContainer.getRequestContainer();
			reqContainer.setServiceRequest(myrequest);
 			biobjModule.validateFields("BIObjectValidation", "PAGE");
			Collection errors = biobjModule.getErrorHandler().getErrors();
			assertEquals(1, errors.size());
			assertEquals(9001, ((EMFUserError)errors.toArray()[0]).getCode());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

}
