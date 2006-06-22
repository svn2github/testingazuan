package it.eng.spagobi.services.modules;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.LovDetail;
import it.eng.spagobi.bo.LovDetailList;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.dao.hibernate.LovDAOHibImpl;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.mockObjects.EngUserProfileImplMock;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.mockObjects.PortletSessionImplMock;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DetailModalitiesValueModuleDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null;

	private SessionContainer sessionCont = null;

	private DetailModalitiesValueModule modvalModule = null;

	protected void setUp() throws Exception {
		try {
			request = new SourceBean("");
			response = new SourceBean("");
		} catch (SourceBeanException e1) {
			e1.printStackTrace();
		}
		RequestContainer reqContainer = new RequestContainer();
		sessionCont = new SessionContainer(true);
		reqContainer.setSessionContainer(sessionCont);
		ResponseContainer resContainer = new ResponseContainer();
		DefaultRequestContext defaultRequestContext = new DefaultRequestContext(
				reqContainer, resContainer);
		reqContainer.setServiceRequest(request);
		resContainer.setServiceResponse(response);
		resContainer.setErrorHandler(new EMFErrorHandler());
		PortletRequestImplMock portletRequest = new PortletRequestImplMock();
		PortletSessionImplMock portletSession = new PortletSessionImplMock();
		Locale locale = new Locale("it","IT","");
		portletSession.setAttribute("PortalLocale",locale);
		//PortletAccess.setPortalLocale(locale);
		portletRequest.setPortletSession(portletSession);
		reqContainer.setAttribute("PORTLET_REQUEST", portletRequest);
		modvalModule = new DetailModalitiesValueModule();
		modvalModule.setRequestContext(defaultRequestContext);		
		EngUserProfileImplMock profile = new EngUserProfileImplMock();
		profile.setUserAttribute("PROFILE_ATTRIBUTES", new HashMap());
		SessionContainer permSession = sessionCont.getPermanentContainer();
		permSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
		super.setUp();
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesValueModule.getDetailModValue(String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method. Verifies that the correct ModalitiesValue is attached to the response.
	 */
	public void testGetDetailModValue() {
		Integer id = new Integer(21);
		try {
			request.setAttribute("MESSAGEDET",
					AdmintoolsConstants.DETAIL_SELECT);
			request.setAttribute("id", id.toString());

			modvalModule.service(request, response);
			assertEquals(0, modvalModule.getErrorHandler().getErrors().size());
			assertEquals(id, ((ModalitiesValue) response
					.getAttribute("MODALITY_VALUE_OBJ")).getId());

			// second test on exception event

			request.updAttribute("id", "6");
			modvalModule.service(request, response);
			Collection errors = modvalModule.getErrorHandler().getErrors();
			assertEquals(1, errors.size());
			assertEquals(1019, ((EMFUserError) errors.toArray()[0]).getCode());
		} catch (Exception e) {
			// Catching only Exception is becuase the behaviuor is the same for
			// all the types of exception thrown
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesValueModule.service(SourceBean,
	 * SourceBean)' in case there is no message parameter.
	 */
	public void testNullMessage() {
		try {
			modvalModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(1, modvalModule.getErrorHandler().getErrors().size());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesValueModule.modDetailModValue(SourceBean, String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method. Verifies that a ModalitiesValue is correctly modified or inserted.
	 */
	public void testModDettaglioModalitiesValue() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_MOD);
			request.setAttribute("id", "18");
			request.setAttribute("description", "Description");
			request.setAttribute("name", "New Lov");
			request.setAttribute("label", "New Label");
			request.setAttribute("input_type", "SCRIPT,38");
			request.setAttribute("script", "'6'");
			request.setAttribute("numberout", "single");
			modvalModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, modvalModule.getErrorHandler().getErrors().size());
		assertEquals("true", response.getAttribute("loopback"));

		try {
			response.delAttribute("loopback");
			request.updAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.updAttribute("label", "New Label 2");
			request.updAttribute("name", "New Name 2");
			modvalModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, modvalModule.getErrorHandler().getErrors().size());
		assertEquals("true", response.getAttribute("loopback"));
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesValueModule.newDetailModValue(SourceBean)',
	 * passing through the service(SourceBean, SourceBean) method.
	 */
	public void testNewDettaglioModalitiesValue() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_NEW);
			modvalModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, modvalModule.getErrorHandler().getErrors().size());
		assertEquals(AdmintoolsConstants.DETAIL_INS, response
				.getAttribute("modality"));
		assertEquals(ModalitiesValue.class.getName(), response.getAttribute(
				"MODALITY_VALUE_OBJ").getClass().getName());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesValueModule.delDetailModValue(SourceBean, String
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testDelDettaglioModalitiesValue() {
		Collection errors = null;
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.setAttribute("id", "18");
			request.setAttribute("description", "Description");
			request.setAttribute("name", "New Lov");
			request.setAttribute("label", "New Label");
			request.setAttribute("input_type", "MAN_IN,37");
			modvalModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, modvalModule.getErrorHandler().getErrors().size());
		assertEquals("true", response.getAttribute("loopback"));

		LovDAOHibImpl lovDAO = new LovDAOHibImpl();
		ModalitiesValue modval = null;
		try {
			List list = lovDAO.loadAllModalitiesValue();
			modval = (ModalitiesValue) list.get(list.size() - 1);
		} catch (EMFUserError e1) {
			e1.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		try {
			request.updAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_DEL);
			request.updAttribute("id", modval.getId().toString());
			modvalModule.service(request, response);
			errors = modvalModule.getErrorHandler().getErrors();
			assertEquals(0, errors.size());

			request.updAttribute("id", "8");
			modvalModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		errors = modvalModule.getErrorHandler().getErrors();
		assertEquals(1, errors.size());
		assertEquals(1020, ((EMFUserError) errors.toArray()[0]).getCode());
	}


	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesValueModule.modDetailModValue(SourceBean,
	 * String, SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method, in case the user is going to insert a query. 
	 */
	public void testSaveQueryWizardValues() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.setAttribute("id", "7");
			request.setAttribute("description", "Description");
			request.setAttribute("name", "New Lov");
			request.setAttribute("label", "Label");
			request.setAttribute("input_type", "QUERY,36");
			request.setAttribute("connName", "Connection Name");
			request.setAttribute("visColumns", "Visible Columns");
			request.setAttribute("valuecolumns", "Value Columns");
			request.setAttribute("queryDef", "Query Definition");
			modvalModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, modvalModule.getErrorHandler().getErrors().size());
		assertEquals("true", response.getAttribute("loopback"));
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesValueModule.modDetailModValue(SourceBean, 
	 * String, SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method, in case the user is going to insert a script.
	 */
	public void testSaveScriptWizardValues() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.setAttribute("id", "7");
			request.setAttribute("description", "Description");
			request.setAttribute("name", "Mock Lov");
			request.setAttribute("label", "Label");
			request.setAttribute("input_type", "SCRIPT,38");
			request.setAttribute("script", "'5'");
			request.setAttribute("numberout", "single");
			modvalModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, modvalModule.getErrorHandler().getErrors().size());
		assertEquals("true", response.getAttribute("loopback"));
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesValueModule.modDetailModValue(SourceBean, 
	 * String, SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method, in case the user is going to insert a new item in the Fix Lov.
	 */
	public void testInsertNewFixLovItem() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.setAttribute("id", "7");
			request.setAttribute("description", "Description");
			request.setAttribute("name", "New Lov");
			request.setAttribute("label", "Label");
			request.setAttribute("input_type", "FIX_LOV,39");
			request.setAttribute("insertFixLovItem","");
			request.setAttribute("nameOfFixedLovItemNew", "Padova");
			request.setAttribute("valueOfFixedLovItemNew", "Veneto");
			modvalModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, modvalModule.getErrorHandler().getErrors().size());
		ModalitiesValue modVal = (ModalitiesValue) response.getAttribute("MODALITY_VALUE_OBJ");
		String lovProvider = modVal.getLovProvider();
		LovDetailList lovDetList = null;
		try {
			lovDetList = LovDetailList.fromXML(lovProvider);
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}
		List lovs = lovDetList.getLovs();
		assertEquals(1,lovs.size());
		LovDetail lovDetail = (LovDetail) lovs.get(0);
		assertEquals("Padova", lovDetail.getName());
		assertEquals("Veneto", lovDetail.getDescription());
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailModalitiesValueModule.modDetailModValue(SourceBean, 
	 * String, SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method, in case the user is going to delete an item in the Fix Lov.
	 */
	public void testDelfixlov() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.setAttribute("id", "7");
			request.setAttribute("description", "Description");
			request.setAttribute("name", "New Lov");
			request.setAttribute("label", "Label");
			request.setAttribute("input_type", "FIX_LOV,39");
			request.setAttribute("insertFixLovItem","");
			request.setAttribute("nameOfFixedListItem", "Padova");
			request.setAttribute("valueOfFixedListItem", "Veneto");			
			request.setAttribute("indexOfFixedLovItemToDelete", "0");
			modvalModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, modvalModule.getErrorHandler().getErrors().size());
		ModalitiesValue modVal = (ModalitiesValue) response.getAttribute("MODALITY_VALUE_OBJ");
		String lovProvider = modVal.getLovProvider();
		LovDetailList lovDetList = null;
		try {
			lovDetList = LovDetailList.fromXML(lovProvider);
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}
		List lovs = lovDetList.getLovs();
		assertEquals(0,lovs.size());
	}

}
