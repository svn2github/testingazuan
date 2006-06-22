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
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Check;
import it.eng.spagobi.bo.ExecutionController;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.hibernate.ParameterUseDAOHibImpl;
import it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.mockObjects.EngUserProfileImplMock;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.mockObjects.PortletSessionImplMock;
import it.eng.spagobi.security.AnonymousCMSUserProfile;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExecuteBIObjectModuleDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null;
	
	private SessionContainer session = null, permSession = null;

	private ExecuteBIObjectModule execBiobjModule = null;

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
		session = new SessionContainer(true);
		reqContainer.setSessionContainer(session);
		permSession = session.getPermanentContainer();
		IEngUserProfile profile = new AnonymousCMSUserProfile();
		permSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
		session.setAttribute("ACTOR", "Davide");

		execBiobjModule = new ExecuteBIObjectModule();
		execBiobjModule.setRequestContext(defaultRequestContext);
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.ExecuteBIObjectModule.service(SourceBean, 
	 * SourceBean)' in case the message parameter is "EXEC_PHASE_CREATE_PAGE".
	 */
	public void testServicePhaseCreatePage() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas"
			+ "/Static Reporting/REPORT_RPT_CUST_01";
		Integer biobjId = new Integer(13);
		try {
			request.setAttribute("PATH",path);
			request.setAttribute(SpagoBIConstants.MESSAGEDET,"EXEC_PHASE_CREATE_PAGE");
			execBiobjModule.service(request,response);
			assertEquals("true",response.getAttribute("selectionRoleForExecution"));
			List roles = (List) response.getAttribute("roles");
			assertEquals(10,roles.size());
			assertEquals(path,response.getAttribute("path"));
			assertEquals("Davide",session.getAttribute("ACTOR"));
			response = new SourceBean("");
			session.setAttribute("ACTOR","DEV_ACTOR");
			EngUserProfileImplMock profile = new EngUserProfileImplMock();
			Collection rolesExpected = new ArrayList();
			rolesExpected.add("/admin");
			profile.setRoles(rolesExpected);
			permSession.delAttribute(IEngUserProfile.ENG_USER_PROFILE);
			permSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE,profile);
			execBiobjModule.service(request,response);
			BIObject biobj = (BIObject) session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
			assertEquals(biobjId, biobj.getId());
			assertEquals("DEV_ACTOR",session.getAttribute("ACTOR"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.ExecuteBIObjectModule.service(SourceBean, 
	 * SourceBean)' in case the message parameter is "EXEC_PHASE_SELECTED_ROLE".
	 */
	public void testServicePhaseSelectedRole() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas"
			+ "/Static Reporting/REPORT_RPT_CUST_01";
		Integer biobjId = new Integer(13);
		try {
			request.setAttribute(SpagoBIConstants.MESSAGEDET,"EXEC_PHASE_SELECTED_ROLE");
			request.setAttribute("PATH",path);
			request.setAttribute("role","/admin");
			execBiobjModule.service(request,response);
			assertEquals("Davide",session.getAttribute("ACTOR"));
			BIObject biobj = (BIObject) session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
			assertEquals(biobjId, biobj.getId());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.ExecuteBIObjectModule.service(SourceBean, 
	 * SourceBean)' in case the message parameter is "EXEC_PHASE_RETURN_FROM_LOOKUP".
	 */
	public void testServicePhaseReturnFromLookup() {
		String paramUrlName = "param_output_format";
		String paramValue = "Txt format";
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas"
			+ "/Static Reporting/REPORT_RPT_CUST_01";
		Integer biobjId = new Integer(13);
		ExecutionController execContr = new ExecutionController();
		String role = "/admin";
		try {
			request.setAttribute(SpagoBIConstants.MESSAGEDET,"EXEC_PHASE_RETURN_FROM_LOOKUP");
			request.setAttribute("LOOKUP_PARAMETER_NAME",paramUrlName);
			request.setAttribute("LOOKUP_VALUE",paramValue);
			execContr.prepareBIObjectInSession(session,path,role);
			execBiobjModule.service(request,response);
			assertEquals("Davide",session.getAttribute("ACTOR"));
			BIObject biobj = (BIObject) session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
			assertEquals(biobjId, biobj.getId());
			List biparams = biobj.getBiObjectParameters();
			Iterator iterParams = biparams.iterator();
			List paramvalues = null;
            while(iterParams.hasNext()) {
            	BIObjectParameter biparam = (BIObjectParameter)iterParams.next();
            	String nameUrl = biparam.getParameterUrlName();
            	if (nameUrl.equalsIgnoreCase(paramUrlName)){
            		paramvalues = biparam.getParameterValues();
            	}
            }
            assertEquals(1, paramvalues.size());
            assertEquals(paramValue,(String) paramvalues.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.ExecuteBIObjectModule.service(SourceBean, 
	 * SourceBean)' in case the message parameter is "EXEC_PHASE_RUN".
	 */
	public void testServicePhaseRun() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas"
			+ "/Static Reporting/REPORT_RPT_CUST_01";
		Integer biobjId = new Integer(13);
		String role = "/admin";
		ExecutionController execContr = new ExecutionController();
		try {
			request.setAttribute(SpagoBIConstants.MESSAGEDET,"EXEC_PHASE_RUN");
			request.setAttribute("param_country","Italy");
			request.setAttribute("param_gender","Male");
			request.setAttribute("param_output_format","Txt_format");
			execContr.prepareBIObjectInSession(session,path,role);
			execBiobjModule.service(request,response);
			assertEquals("Davide",session.getAttribute("ACTOR"));
			assertEquals("true",response.getAttribute("EXECUTION"));
			BIObject biobj = (BIObject) session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
			assertEquals(biobjId, biobj.getId());
			List biparams = biobj.getBiObjectParameters();
			Iterator iterParams = biparams.iterator();
			List paramvalues = null;
            while(iterParams.hasNext()) {
            	BIObjectParameter biparam = (BIObjectParameter)iterParams.next();
            	String nameUrl = biparam.getParameterUrlName();
            	if (nameUrl.equalsIgnoreCase("param_output_format")){
            		paramvalues = biparam.getParameterValues();
            	}
            }
            assertEquals(1, paramvalues.size());
			assertEquals("Txt_format", (String) paramvalues.get(0));
			Map mapPars = (Map) response.getAttribute(ObjectsTreeConstants.REPORT_CALL_URL);
			Map mapParsExpected = new Hashtable();
			mapParsExpected.put("templatePath","/Functionalities/SystemFunctionalities/Analytical Areas/" +
					"Static Reporting/REPORT_RPT_CUST_01/template");
			mapParsExpected.put("spagobiurl","http://null:0/spagobi/ContentRepositoryServlet");
			mapParsExpected.put("param_country","Italy");
			mapParsExpected.put("param_gender","Male");
			mapParsExpected.put("param_output_format","Txt_format");
			mapPars.remove("TOKEN_SIGN");
			mapPars.remove("TOKEN_CLEAR");
			mapPars.remove("IDENTITY");
			assertEquals(mapParsExpected, mapPars);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.ExecuteBIObjectModule.service(SourceBean, 
	 * SourceBean)' in case the message parameter is "EXEC_PHASE_SELECTED_ROLE" and one Role has two ParameterUses
	 * for the same Parameter and so cannot execute the BIObject (and exception occurs).
	 */
	public void testServiceSelectedRoleWithTwoParameterUsesForOneRole() {
		ParameterUseDAOHibImpl paruseDAO = new ParameterUseDAOHibImpl(); 
		ParameterUse paruse = new ParameterUse();
		paruse.setAssociatedChecks(new ArrayList());
		List roles = new ArrayList();
		Role role = null;
		RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
		try {
			role = roleDAO.loadByID(new Integer (1));
		} catch (EMFUserError e1) {
			e1.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		roles.add(role);
		paruse.setAssociatedRoles(roles);
		paruse.setDescription("New Parameter Use for Test");
		paruse.setId(new Integer (14));
		paruse.setIdLov(new Integer(18));
		paruse.setLabel("PARUSE_FOR_TEST");
		paruse.setName("Parameter Use for Test");
		try {
			paruseDAO.insertParameterUse(paruse);
		} catch (EMFUserError e1) {
			e1.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas"
			+ "/Static Reporting/REPORT_RPT_CUST_01";
		String roleName = role.getName();
		try {
			request.setAttribute(SpagoBIConstants.MESSAGEDET,"EXEC_PHASE_SELECTED_ROLE");
			request.setAttribute("PATH",path);
			request.setAttribute("role",roleName);
			execBiobjModule.service(request,response);
			assertEquals(null,response.getAttribute("actor"));
			assertNull(session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR));
			Object[] errors = execBiobjModule.getErrorHandler().getErrors().toArray();
			assertEquals(1,errors.length);
			assertEquals(100,((EMFUserError)errors[0]).getCode());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}


	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.ExecuteBIObjectModule.service(SourceBean, 
	 * SourceBean)' in case the message parameter is "EXEC_CHANGE_STATE".
	 */
	public void testChangeState() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas"
			+ "/Static Reporting/REPORT_RPT_CUST_01";
		Integer biobjId = new Integer(13);
		String role = "/admin";
		String newStateCode = "TEST";
		Integer newStateId = new Integer(56);
		ExecutionController execContr = new ExecutionController();
		try {
			request.setAttribute(SpagoBIConstants.MESSAGEDET,"EXEC_CHANGE_STATE");
			request.setAttribute("newState",newStateId.toString()+","+newStateCode);
			execContr.prepareBIObjectInSession(session,path,role);
			execBiobjModule.service(request,response);
			assertEquals("Davide",response.getAttribute("actor"));
			assertEquals("true",response.getAttribute("isLoop"));
			BIObject biobj = (BIObject) session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
			assertEquals(biobjId, biobj.getId());
			assertEquals(newStateId, biobj.getStateID());
			assertEquals(newStateCode, biobj.getStateCode());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}
	
	/**
	 * Test method for 'it.eng.spagobi.services.modules.ExecuteBIObjectModule.prepareBIObjectInSession(SessionContainer, String, String)'.
	 */
	public void testPrepareBIObjectInSession() {
		SessionContainer session = RequestContainer.getRequestContainer().getSessionContainer();
		Integer biobjId = new Integer(13);
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas"
			+ "/Static Reporting/REPORT_RPT_CUST_01";
		String roleName = "/admin";
		ExecutionController execContr = new ExecutionController();
		try {
			execContr.prepareBIObjectInSession(session,path,roleName);
			BIObject biobj = (BIObject) session.getAttribute(ObjectsTreeConstants.SESSION_OBJ_ATTR);
			assertEquals(biobjId,biobj.getId());
			SourceBean sb = (SourceBean) session.getAttribute("VALIDATE_PAGE_ValidateExecuteBIObjectPage");
			SourceBean maxLenghtValidator = (SourceBean) sb.getFilteredSourceBeanAttribute("VALIDATION.FIELDS.FIELD.VALIDATOR","validatorName","MAXLENGTH");
			assertEquals("20",maxLenghtValidator.getAttribute("arg0"));
			SourceBean alfanumericValidator = (SourceBean) sb.getFilteredSourceBeanAttribute("VALIDATION.FIELDS.FIELD.VALIDATOR","validatorName","ALFANUMERIC");
			assertNull(alfanumericValidator.getAttribute("arg0"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for 'it.eng.spagobi.services.modules.ExecuteBIObjectModule.createValidableFieldSourceBean(BIObjectParameter)'.
	 */
	public void testCreateValidableFieldSourceBean() {
		String urlName = "Url name";
		String label = "label";
		ExecutionController execContr = new ExecutionController();
		try {
			BIObjectParameter biobjparam = new BIObjectParameter();
			biobjparam.setParameterUrlName(urlName);
			biobjparam.setLabel(label);
			Parameter param = new Parameter ();
			param.setMask("dd/mm/yyyy");
			List checks = new ArrayList();
			Check check1 = new Check();
			check1.setValueTypeCd("MANDATORY");
			Check check2 = new Check();
			check2.setValueTypeCd("INTERNET ADDRESS");
			Check check3 = new Check();
			check3.setValueTypeCd("LETTERSTRING");
			Check check4 = new Check();
			check4.setValueTypeCd("ALFANUMERIC");
			Check check5 = new Check();
			check5.setValueTypeCd("EMAIL");
			Check check6 = new Check();
			check6.setValueTypeCd("FISCALCODE");
			Check check7 = new Check();
			check7.setValueTypeCd("DECIMALS");
			check7.setFirstValue("10");
			check7.setSecondValue(",");
			Check check8 = new Check();
			check8.setValueTypeCd("MAXLENGTH");
			check8.setFirstValue("5");
			Check check9 = new Check();
			check9.setValueTypeCd("MINLENGTH");
			check9.setFirstValue("3");
			Check check10 = new Check();
			check10.setValueTypeCd("REGEXP");
			check10.setFirstValue("^([a-zA-Z])*$");
			Check check11 = new Check();
			check11.setValueTypeCd("DATE");
			check11.setFirstValue("yyyy/mm/dd");
			Check check12 = new Check();
			check12.setValueTypeCd("NUMERIC");
			Check check13 = new Check();
			check13.setValueTypeCd("RANGE");
			check13.setFirstValue("0");
			check13.setSecondValue("100");
			Check check14 = new Check();
			check14.setValueTypeCd("RANGE");
			check14.setFirstValue("01/01/2000");
			check14.setSecondValue("01/09/2005");
			Check check15 = new Check();
			check15.setValueTypeCd("RANGE");
			check15.setFirstValue("fro");
			check15.setSecondValue("epa");

			checks.add(check1); 
			checks.add(check2); 
			checks.add(check3); 
			checks.add(check4); 
			checks.add(check5);
			checks.add(check6);
			checks.add(check7);
			checks.add(check8);
			checks.add(check9);
			checks.add(check10);
			checks.add(check11);
			checks.add(check12);
			checks.add(check13);
			checks.add(check14);
			checks.add(check15);
			
			param.setChecks(checks);
			biobjparam.setParameter(param);
			SourceBean sb = execContr.createValidableFieldSourceBean(biobjparam);
			List checksSB = sb.getAttributeAsList("VALIDATOR");
			
			assertEquals(15,checksSB.size());
			
			SourceBean check1SB = (SourceBean) checksSB.get(0);
			assertEquals("MANDATORY",check1SB.getAttribute("validatorName"));
			SourceBean check2SB = (SourceBean) checksSB.get(1);
			assertEquals("URL",check2SB.getAttribute("validatorName"));
			SourceBean check3SB = (SourceBean) checksSB.get(2);
			assertEquals("LETTERSTRING",check3SB.getAttribute("validatorName"));
			SourceBean check4SB = (SourceBean) checksSB.get(3);
			assertEquals("ALFANUMERIC",check4SB.getAttribute("validatorName"));
			SourceBean check5SB = (SourceBean) checksSB.get(4);
			assertEquals("EMAIL",check5SB.getAttribute("validatorName"));
			SourceBean check6SB = (SourceBean) checksSB.get(5);
			assertEquals("FISCALCODE",check6SB.getAttribute("validatorName"));
			SourceBean check7SB = (SourceBean) checksSB.get(6);
			assertEquals("DECIMALS",check7SB.getAttribute("validatorName"));
			assertEquals("10",check7SB.getAttribute("arg0"));
			assertEquals(",",check7SB.getAttribute("arg1"));
			SourceBean check8SB = (SourceBean) checksSB.get(7);
			assertEquals("MAXLENGTH",check8SB.getAttribute("validatorName"));
			assertEquals("5",check8SB.getAttribute("arg0"));
			SourceBean check9SB = (SourceBean) checksSB.get(8);
			assertEquals("MINLENGTH",check9SB.getAttribute("validatorName"));
			assertEquals("3",check9SB.getAttribute("arg0"));
			SourceBean check10SB = (SourceBean) checksSB.get(9);
			assertEquals("REGEXP",check10SB.getAttribute("validatorName"));
			assertEquals("^([a-zA-Z])*$",check10SB.getAttribute("arg0"));
			SourceBean check11SB = (SourceBean) checksSB.get(10);
			assertEquals("DATE",check11SB.getAttribute("validatorName"));
			assertEquals("yyyy/mm/dd",check11SB.getAttribute("arg0"));
			SourceBean check12SB = (SourceBean) checksSB.get(11);
			assertEquals("NUMERIC",check12SB.getAttribute("validatorName"));
			
			param.setType("NUM");
			param.setMask("1234");
			sb = execContr.createValidableFieldSourceBean(biobjparam);
			checksSB = sb.getAttributeAsList("VALIDATOR");
			SourceBean check13SB = (SourceBean) checksSB.get(12);
			assertEquals("NUMERICRANGE",check13SB.getAttribute("validatorName"));
			assertEquals("0",check13SB.getAttribute("arg0"));
			assertEquals("100",check13SB.getAttribute("arg1"));
			assertEquals("1234",check13SB.getAttribute("arg2"));

			param.setType("DATE");
			param.setMask("yyyy-mm-dd");
			sb = execContr.createValidableFieldSourceBean(biobjparam);
			checksSB = sb.getAttributeAsList("VALIDATOR");
			SourceBean check14SB = (SourceBean) checksSB.get(13);
			assertEquals("DATERANGE",check14SB.getAttribute("validatorName"));
			assertEquals("01/01/2000",check14SB.getAttribute("arg0"));
			assertEquals("01/09/2005",check14SB.getAttribute("arg1"));
			assertEquals("yyyy-mm-dd",check14SB.getAttribute("arg2"));
			
			param.setType("STRING");
			param.setMask(null);
			sb = execContr.createValidableFieldSourceBean(biobjparam);
			checksSB = sb.getAttributeAsList("VALIDATOR");
			SourceBean check15SB = (SourceBean) checksSB.get(14);
			assertEquals("STRINGRANGE",check15SB.getAttribute("validatorName"));
			assertEquals("fro",check15SB.getAttribute("arg0"));
			assertEquals("epa",check15SB.getAttribute("arg1"));
			
			assertEquals(0,execBiobjModule.getErrorHandler().getErrors().toArray().length);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

}
