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
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.mockObjects.PortletPreferencesImplMock;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.security.AnonymousCMSUserProfile;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.ArrayList;
import java.util.List;

public class TreeObjectsModuleDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null;
	
	private SessionContainer session = null, permSession = null;

	private TreeObjectsModule treeObjectModule = null;

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
	
	protected void setupContext() throws Exception {
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
		PortletPreferencesImplMock portletPref = new PortletPreferencesImplMock();
		portletPref.setValue("MODALITY","");
		portletRequest.setPortletPreferences(portletPref);
		reqContainer.setAttribute("PORTLET_REQUEST", portletRequest);
		PortletAccess.setPortletRequest(portletRequest);
		session = new SessionContainer(true);
		reqContainer.setSessionContainer(session);
		permSession = session.getPermanentContainer();
		IEngUserProfile profile = new AnonymousCMSUserProfile();
		permSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
		request.setAttribute("actor","Davide");
		
		treeObjectModule = new TreeObjectsModule();
		treeObjectModule.setRequestContext(defaultRequestContext);
		super.setUp();
	}
	
	/**
	 * Test method for 'it.eng.spagobi.services.modules.TreeObjectsModule.service(SourceBean, SourceBean)'.
	 * Verifies that the correct functionalities tree is returned.
	 */
	public void testService() {
		try {
			treeObjectModule.service(request,response);
			assertEquals("Davide",response.getAttribute("actor"));
			List mainList = response.getFilteredSourceBeanAttributeAsList("SYSTEMFUNCTIONALITIES.SYSTEMFUNCTIONALITY","codeType","LOW_FUNCT");
			assertEquals(2,mainList.size());
			List expectedMainFunctions = new ArrayList();
			expectedMainFunctions.add("Business Departments");
			expectedMainFunctions.add("Analytical Areas");
			List subList = null;
			List actualMainFunctions = new ArrayList();
			for (int i = 0; i < mainList.size(); i++) {
				SourceBean sb = (SourceBean) mainList.get(i);
				actualMainFunctions.add((String) sb.getAttribute("name"));
				if ("Analytical Areas".equals(sb.getAttribute("name")))
					subList = sb.getAttributeAsList("SYSTEMFUNCTIONALITY");
			}
			assertTrue(expectedMainFunctions.containsAll(actualMainFunctions));
			assertEquals(4,subList.size());
			List expectedSubFunctions = new ArrayList();
			expectedSubFunctions.add("Static Reporting");
			expectedSubFunctions.add("Dimensional Analysis");
			expectedSubFunctions.add("Data Mining Models");
			expectedSubFunctions.add("Dashboards");
			List actualSubFunctions = new ArrayList();
			for (int i = 0; i < mainList.size(); i++) {
				SourceBean sb = (SourceBean) subList.get(i);
				actualSubFunctions.add((String) sb.getAttribute("name"));
			}
			assertTrue(expectedSubFunctions.containsAll(actualSubFunctions));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		
	}

}
