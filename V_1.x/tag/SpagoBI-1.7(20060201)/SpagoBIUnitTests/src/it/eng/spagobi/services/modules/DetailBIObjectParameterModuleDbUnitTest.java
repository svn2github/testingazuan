package it.eng.spagobi.services.modules;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.mockObjects.PortletSessionImplMock;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.Collection;
import java.util.Locale;

public class DetailBIObjectParameterModuleDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null;

	private DetailBIObjectParameterModule biobjparModule = null;

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
		
		biobjparModule = new DetailBIObjectParameterModule();
		biobjparModule.setRequestContext(defaultRequestContext);
		super.setUp();
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailBIObjectParameterModule.getDetailObjPar(String, String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method. Verifies that the correct BIObjectParameter is attached to the response.
	 */
	public void testGetDetailObjPar() {
		try {
			request.setAttribute("MESSAGEDET",
					AdmintoolsConstants.DETAIL_SELECT);
			request.setAttribute("BIOBJ_ID", "14");
			request.setAttribute("PAR_ID", "12");
			biobjparModule.service(request, response);
			assertEquals(0, biobjparModule.getErrorHandler().getErrors().size());
			assertEquals(new Integer(14), ((BIObjectParameter) response
					.getAttribute("OBJECT_PAR")).getBiObjectID());
			
			// second test on exception event
			
			request.updAttribute("PAR_ID", "6");
			biobjparModule.service(request, response);
			Collection errors = biobjparModule.getErrorHandler().getErrors();
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
	 * 'it.eng.spagobi.services.modules.DetailBIObjectParameterModule.service(SourceBean,
	 * SourceBean)' in case there is no message parameters.
	 */
	public void testNullMessage() {
		try {
			biobjparModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(101, ((EMFUserError) biobjparModule.getErrorHandler()
				.getErrors().toArray()[0]).getCode());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailBIObjectParameterModule.modDetailObjPar(SourceBean, String,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testModDetailObjPar() {
		Integer objId= new Integer(14);
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_MOD);
			request.setAttribute("parIdOld", "12");
			request.setAttribute("id", objId.toString());
			request.setAttribute("par_id", "8");
			request.setAttribute("label", "New label");
			request.setAttribute("req_fl", "40");
			request.setAttribute("mod_fl", "41");
			request.setAttribute("view_fl", "42");
			request.setAttribute("mult_fl", "43");
			request.setAttribute("parurl_nm", "new parurl_nm");

			biobjparModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		Integer object_id= (Integer)response.getAttribute("OBJECT_ID");
		assertEquals(objId,object_id);
		String object_path=(String)response.getAttribute("PATH");
		assertEquals(object_path,"/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT-EMPL-01");
		assertEquals(0, biobjparModule.getErrorHandler().getErrors().size());
		assertEquals("true", response.getAttribute("loopback"));
		
		try {
			request.updAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_INS);
			request.updAttribute("id", "14");
			request.updAttribute("par_id", "9");
			biobjparModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, biobjparModule.getErrorHandler().getErrors().size());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailBIObjectParameterModule.newDetailBIObjPar(SourceBean,
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testNewDetailBIObjPar() {
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_NEW);
			request.setAttribute("OBJECT_ID","14");
			biobjparModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0, biobjparModule.getErrorHandler().getErrors().size());
		assertEquals(AdmintoolsConstants.DETAIL_INS, response
				.getAttribute("modality"));
		assertEquals(BIObjectParameter.class.getName(), response.getAttribute("OBJECT_PAR")
				.getClass().getName());
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailBIObjectParameterModule.delDetailObjPar(SourceBean, String
	 * SourceBean)', passing through the service(SourceBean, SourceBean)
	 * method.
	 */
	public void testDelDetailObjPar() {
		Collection errors = null;
		try {
			request.setAttribute("MESSAGEDET", AdmintoolsConstants.DETAIL_DEL);
			request.setAttribute("BIOBJ_ID", "14");
			request.setAttribute("PAR_ID", "12");
			biobjparModule.service(request, response);
			errors = biobjparModule.getErrorHandler().getErrors();
			assertEquals(0, errors.size());
			
			request.updAttribute("PAR_ID", "8");
			biobjparModule.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		 
		errors = biobjparModule.getErrorHandler().getErrors();
		assertEquals(1, errors.size());
		assertEquals(100, ((EMFUserError) errors.toArray()[0]).getCode());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.DetailBIObjectParameterModule.objParControl(SourceBean)',
	 * passing through the service(SourceBean, SourceBean) method.
	 */
	public void testObjParControl() {
		try {
			request.setAttribute("id","10000");
			request.setAttribute("par_id","10000");
			biobjparModule.objParControl(request);
		} catch (EMFUserError e) {
			//e.printStackTrace();
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}
		assertTrue(true);
		
		try {
			request.updAttribute("id","14");
			request.updAttribute("par_id","10000");
			biobjparModule.objParControl(request);
		} catch (EMFUserError e) {
			//e.printStackTrace();
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}
		assertTrue(true);

		try {
			request.updAttribute("id","10000");
			request.updAttribute("par_id","12");
			biobjparModule.objParControl(request);
		} catch (EMFUserError e) {
			//e.printStackTrace();
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}
		assertTrue(true);
		
		try {
			request.updAttribute("id","14");
			request.updAttribute("par_id","12");
			biobjparModule.objParControl(request);
		} catch (EMFUserError e) {
			//e.printStackTrace();
			assertEquals(1026,e.getCode());
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}
	}

}
