package it.eng.spagobi.services.modules;

import it.eng.spago.base.PortletAccess;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.security.AnonymousCMSUserProfile;
import junit.framework.TestCase;

public class PortletLoginModuleJUnitTest extends TestCase {

	private SourceBean request = null, response = null;
	
	private SessionContainer session = null , permanentSession = null;

	private PortletLoginModule portletLoginModule = null;
	
	protected void setUp() throws Exception {
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
		reqContainer.setAttribute("PORTLET_REQUEST", portletRequest);
		PortletAccess.setPortletRequest(portletRequest);
		session = new SessionContainer(true);
		reqContainer.setSessionContainer(session);
		permanentSession = session.getPermanentContainer();
		try {
			request.setAttribute("ACTOR", "Davide");
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}

		portletLoginModule = new PortletLoginModule();
		portletLoginModule.setRequestContext(defaultRequestContext);
		super.setUp();
	}

	/**
	 * Test method for 'it.eng.spagobi.services.modules.PortletLoginModule.service(SourceBean, SourceBean)'
	 */
	public void testService() {
		try {
			portletLoginModule.service(request,response);
			IEngUserProfile profile = (IEngUserProfile) permanentSession.getAttribute("ENG_USER_PROFILE");
			assertTrue(profile instanceof AnonymousCMSUserProfile);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}

}
