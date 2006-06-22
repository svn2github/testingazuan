package it.eng.spagobi.bo.dao.hibernate.dbunit;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.cms.CmsManager;
import it.eng.spago.cms.CmsNode;
import it.eng.spago.cms.exceptions.BuildOperationException;
import it.eng.spago.cms.exceptions.OperationExecutionException;
import it.eng.spago.cms.init.CMSInitializer;
import it.eng.spago.cms.init.CMSManager;
import it.eng.spago.cms.operations.GetOperation;
import it.eng.spago.cms.util.CMSRecovery;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.hibernate.LowFunctionalityDAOHibImpl;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.mockObjects.PortletSessionImplMock;
import it.eng.spagobi.security.AnonymousCMSUserProfile;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LowFunctionalityDAOHibImplDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null;

	private LowFunctionalityDAOHibImpl lowfuncDAO = null;
	
	private IEngUserProfile profile = new AnonymousCMSUserProfile();

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
		permSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);

		lowfuncDAO = new LowFunctionalityDAOHibImpl();
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.LowFunctionalityDAOHibImpl.loadLowFunctionalityByID(Integer)'
	 * Loads a LowFunctionality by id and verifies if it is loaded correctly.
	 */
	public void testLoadLowFunctionalityByID() {
		Integer lowfuncId = new Integer(7);
		LowFunctionality lowfunc = null;
		try {
			lowfunc = lowfuncDAO.loadLowFunctionalityByID(lowfuncId);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
		assertEquals(lowfuncId,lowfunc.getId());
		assertEquals("REPORT",lowfunc.getCode());
		assertEquals("LOW_FUNCT",lowfunc.getCodType());
		assertEquals("Static Reporting",lowfunc.getDescription());
		List expectedDevRoleIds = new ArrayList();
		expectedDevRoleIds.add(new Integer(1));
		expectedDevRoleIds.add(new Integer(14));
		List expectedExecRoleIds = new ArrayList();
		expectedExecRoleIds.add(new Integer(1));
		expectedExecRoleIds.add(new Integer(16));
		List expectedTestRoleIds = new ArrayList();
		expectedTestRoleIds.add(new Integer(1));
		expectedTestRoleIds.add(new Integer(15));
		Role[] devRoles = lowfunc.getDevRoles();
		Role[] execRoles = lowfunc.getExecRoles();
		Role[] testRoles = lowfunc.getTestRoles();
		assertEquals(expectedDevRoleIds.size(),lowfunc.getDevRoles().length);
		assertEquals(expectedExecRoleIds.size(),lowfunc.getExecRoles().length);
		assertEquals(expectedTestRoleIds.size(),lowfunc.getTestRoles().length);
		for (int i = 0; i < expectedDevRoleIds.size() ; i++){
			assertTrue(expectedDevRoleIds.contains(devRoles[i].getId()));
		}
		for (int i = 0; i < expectedExecRoleIds.size() ; i++){
			assertTrue(expectedExecRoleIds.contains(execRoles[i].getId()));
		}
		for (int i = 0; i < expectedTestRoleIds.size() ; i++){
			assertTrue(expectedTestRoleIds.contains(testRoles[i].getId()));
		}
		assertEquals("Static Reporting",lowfunc.getName());
		assertEquals("/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting",lowfunc.getPath());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.LowFunctionalityDAOHibImpl.loadLowFunctionalityByPath(String)'
	 * Loads a LowFunctionality by path and verifies if it is loaded correctly.
	 */
	public void testLoadLowFunctionalityByPath() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting";
		LowFunctionality lowfunc = null;
		try {
			lowfunc = lowfuncDAO.loadLowFunctionalityByPath(path);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
		assertEquals(path,lowfunc.getPath());
		assertEquals("REPORT",lowfunc.getCode());
		assertEquals("LOW_FUNCT",lowfunc.getCodType());
		assertEquals("Static Reporting",lowfunc.getDescription());
		List expectedDevRoleIds = new ArrayList();
		expectedDevRoleIds.add(new Integer(1));
		expectedDevRoleIds.add(new Integer(14));
		List expectedExecRoleIds = new ArrayList();
		expectedExecRoleIds.add(new Integer(1));
		expectedExecRoleIds.add(new Integer(16));
		List expectedTestRoleIds = new ArrayList();
		expectedTestRoleIds.add(new Integer(1));
		expectedTestRoleIds.add(new Integer(15));
		Role[] devRoles = lowfunc.getDevRoles();
		Role[] execRoles = lowfunc.getExecRoles();
		Role[] testRoles = lowfunc.getTestRoles();
		assertEquals(expectedDevRoleIds.size(),lowfunc.getDevRoles().length);
		assertEquals(expectedExecRoleIds.size(),lowfunc.getExecRoles().length);
		assertEquals(expectedTestRoleIds.size(),lowfunc.getTestRoles().length);
		for (int i = 0; i < expectedDevRoleIds.size() ; i++){
			assertTrue(expectedDevRoleIds.contains(devRoles[i].getId()));
		}
		for (int i = 0; i < expectedExecRoleIds.size() ; i++){
			assertTrue(expectedExecRoleIds.contains(execRoles[i].getId()));
		}
		for (int i = 0; i < expectedTestRoleIds.size() ; i++){
			assertTrue(expectedTestRoleIds.contains(testRoles[i].getId()));
		}
		assertEquals("Static Reporting",lowfunc.getName());
		assertEquals(7,lowfunc.getId().intValue());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.LowFunctionalityDAOHibImpl.modifyLowFunctionality(LowFunctionality)'.
	 * Modifies a LowFunctionality with data loaded by another LowFunctionality
	 * (but changing path and code) and then verifies if it is modified as
	 * request by reloading the modified LowFunctionality.
	 */
	public void testModifyLowFunctionality() {
		Integer lowfuncIdToLoad = new Integer(9);
		LowFunctionality lowfunc = null;
		LowFunctionality reloaded = null;
		Integer lowfuncIdToModify = new Integer(10);
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/TEMP";
		try {
			lowfunc = lowfuncDAO.loadLowFunctionalityByID(lowfuncIdToLoad);
			lowfunc.setCode("TEMP");
			lowfunc.setPath(path);
			lowfunc.setId(lowfuncIdToModify);
			lowfuncDAO.modifyLowFunctionality(lowfunc);
			reloaded = lowfuncDAO.loadLowFunctionalityByPath(path);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
		assertEquals(lowfunc.getCode(), reloaded.getCode());
		assertEquals(lowfunc.getCodType(), reloaded.getCodType());
		assertEquals(lowfunc.getDescription(), reloaded.getDescription());
		Role[] expectedDevRoles = lowfunc.getDevRoles();
		Role[] expectedExecRoles = lowfunc.getExecRoles();
		Role[] expectedTestRoles = lowfunc.getTestRoles();
		List expectedDevRoleIds = new ArrayList();
		for (int i = 0; i < expectedDevRoles.length; i++)
			expectedDevRoleIds.add(expectedDevRoles[i].getId());
		List expectedExecRoleIds = new ArrayList();
		for (int i = 0; i < expectedExecRoles.length; i++)
			expectedExecRoleIds.add(expectedExecRoles[i].getId());
		List expectedTestRoleIds = new ArrayList();
		for (int i = 0; i < expectedTestRoles.length; i++)
			expectedTestRoleIds.add(expectedTestRoles[i].getId());
		Role[] devRoles = reloaded.getDevRoles();
		Role[] execRoles = reloaded.getExecRoles();
		Role[] testRoles = reloaded.getTestRoles();
		assertEquals(expectedDevRoleIds.size(), lowfunc.getDevRoles().length);
		assertEquals(expectedExecRoleIds.size(), lowfunc.getExecRoles().length);
		assertEquals(expectedTestRoleIds.size(), lowfunc.getTestRoles().length);
		for (int i = 0; i < expectedDevRoleIds.size(); i++) {
			assertTrue(expectedDevRoleIds.contains(devRoles[i].getId()));
		}
		for (int i = 0; i < expectedExecRoleIds.size(); i++) {
			assertTrue(expectedExecRoleIds.contains(execRoles[i].getId()));
		}
		for (int i = 0; i < expectedTestRoleIds.size(); i++) {
			assertTrue(expectedTestRoleIds.contains(testRoles[i].getId()));
		}
		assertEquals(lowfunc.getName(), reloaded.getName());
		assertEquals(path, reloaded.getPath());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.LowFunctionalityDAOHibImpl.insertLowFunctionality(LowFunctionality,
	 * IEngUserProfile)'. 
	 * Loads a LowFunctionality, changes some fields of that
	 * LowFunctionality and inserts the modified LowFunctionality; then verifies
	 * that it is correctly inserted by reloading it.
	 */
	public void testInsertLowFunctionality() {
		Integer lowfuncId = new Integer(10);
		LowFunctionality lowfunc = null;
		LowFunctionality reloaded = null;
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/TEMP";
		try {
			lowfunc = lowfuncDAO.loadLowFunctionalityByID(lowfuncId);
			lowfunc.setCode("TEMP");
			lowfunc.setPath(path);
			lowfunc.setCodType("PORTAL");
			lowfuncDAO.insertLowFunctionality(lowfunc, profile);
			reloaded = lowfuncDAO.loadLowFunctionalityByPath(path);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(true);
		}
		assertEquals(lowfunc.getCode(), reloaded.getCode());
		assertEquals(lowfunc.getCodType(), reloaded.getCodType());
		assertEquals(lowfunc.getDescription(), reloaded.getDescription());
		Role[] expectedDevRoles = lowfunc.getDevRoles();
		Role[] expectedExecRoles = lowfunc.getExecRoles();
		Role[] expectedTestRoles = lowfunc.getTestRoles();
		List expectedDevRoleIds = new ArrayList();
		for (int i = 0; i < expectedDevRoles.length; i++)
			expectedDevRoleIds.add(expectedDevRoles[i].getId());
		List expectedExecRoleIds = new ArrayList();
		for (int i = 0; i < expectedExecRoles.length; i++)
			expectedExecRoleIds.add(expectedExecRoles[i].getId());
		List expectedTestRoleIds = new ArrayList();
		for (int i = 0; i < expectedTestRoles.length; i++)
			expectedTestRoleIds.add(expectedTestRoles[i].getId());
		Role[] devRoles = reloaded.getDevRoles();
		Role[] execRoles = reloaded.getExecRoles();
		Role[] testRoles = reloaded.getTestRoles();
		assertEquals(expectedDevRoleIds.size(), lowfunc.getDevRoles().length);
		assertEquals(expectedExecRoleIds.size(), lowfunc.getExecRoles().length);
		assertEquals(expectedTestRoleIds.size(), lowfunc.getTestRoles().length);
		for (int i = 0; i < expectedDevRoleIds.size(); i++) {
			assertTrue(expectedDevRoleIds.contains(devRoles[i].getId()));
		}
		for (int i = 0; i < expectedExecRoleIds.size(); i++) {
			assertTrue(expectedExecRoleIds.contains(execRoles[i].getId()));
		}
		for (int i = 0; i < expectedTestRoleIds.size(); i++) {
			assertTrue(expectedTestRoleIds.contains(testRoles[i].getId()));
		}
		assertEquals(lowfunc.getName(), reloaded.getName());
		assertEquals(path, reloaded.getPath());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.LowFunctionalityDAOHibImpl.eraseLowFunctionality(LowFunctionality,
	 * IEngUserProfile)'. 
	 * Erases a LowFunctionality and then tries unsuccessfully to load the same.
	 */
	public void testEraseLowFunctionality() {
		Integer lowfuncId = new Integer (5);
		LowFunctionality lowfunc = null;
		try{
			lowfunc = lowfuncDAO.loadLowFunctionalityByID(lowfuncId);
			lowfuncDAO.eraseLowFunctionality(lowfunc, profile);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(true);
		}
		
		try {
			lowfunc = lowfuncDAO.loadLowFunctionalityByID(lowfuncId);
			fail();
		} catch (EMFUserError e) {
			assertEquals(100,e.getCode());
		}
		
		for (int i = 6; i <= 10; i++){
			lowfuncId = new Integer (6);
			try {
				lowfunc = lowfuncDAO.loadLowFunctionalityByID(lowfuncId);
				lowfuncDAO.eraseLowFunctionality(lowfunc, profile);
			} catch (EMFUserError e) {
				assertEquals(1000,e.getCode());
			} finally {
				CMSRecovery.setCmsModified(true);
			}
		}
	}

	/**
	 * Test method for 'it.eng.spagobi.bo.dao.hibernate.LowFunctionalityDAOHibImpl.existByCode(String)'.
	 * Verifies that LowFunctionalities exist by code as expected.
	 */
	public void testExistByCode() {
		String[] codes = {"FUNC","TYPE","REPORT","OLAP","DATA_MINING","DASH"};
		Integer id = null;
		try{
			String code = null;
			for (int i=0; i<codes.length; i++){
				code = codes[i];
				id = lowfuncDAO.existByCode(code);
				assertEquals(i+5,id.intValue());
			}
			
			code = "TEMP";
			id = lowfuncDAO.existByCode(code);
			assertNull(id);
			
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
		
		
	}

	/**
	 * Test method for 'it.eng.spagobi.bo.dao.hibernate.LowFunctionalityDAOHibImpl.hasChild(String)'.
	 * Verifies that LowFunctionalities have childs as expected.
	 */
	public void testHasChild() {
		String[] paths = {
				"/Functionalities/SystemFunctionalities/Business Departments",
				"/Functionalities/SystemFunctionalities/Analytical Areas",
				"/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting",
				"/Functionalities/SystemFunctionalities/Analytical Areas/Dimensional Analysis",
				"/Functionalities/SystemFunctionalities/Analytical Areas/Data Mining Models",
				"/Functionalities/SystemFunctionalities/Analytical Areas/Dashboards"};
		try {
			for (int i = 0; i < paths.length; i++) {
				String path = paths[i];
				if (i == 1 || i == 2 || i==3) assertTrue(lowfuncDAO.hasChild(path));
				else assertFalse(lowfuncDAO.hasChild(path));
			}
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}
	
	/**
	 * Integrity test between DB and CMS on a insert operation. Tries to insert
	 * into DB a LowFunctionality that violates a constraint (code already existing)
	 * and sees if it is stored in CMS. Then tries also to insert into CMS a
	 * LowFunctionality that violates a constraint (node parent does not exist) and sees
	 * if it is stored in DB.
	 */
	public void testIntegrityDbCmsOnInsert(){
		
		// Part 1: tries to insert into DB a Low Functionality that violates a
		// constraint (code already existing) and sees if it is stored in CMS
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Temp";
		Integer lowfuncId = new Integer (7);
		LowFunctionality lowfunc = null;
		try{
			lowfunc = lowfuncDAO.loadLowFunctionalityByID(lowfuncId);
			lowfunc.setPath(path);
			lowfunc.setDescription("New Low Functionality");
	
			lowfuncDAO.insertLowFunctionality(lowfunc, profile);
			fail("Unexpected exception occurred!");
		} catch (EMFUserError e) {
			assertEquals(100,e.getCode());
		} finally {
			CMSRecovery.setCmsModified(false);
		}
		
//		CMSConnection connection = null;
		try {
//			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
//			connection = CMSManager.getInstance().getConnection();
//			GetOperation get = OperationBuilder.buildGetOperation();
//			get.setPath(path);
//			ElementDescriptor desc = executor.getObject(connection, get, profile, true);
//			SourceBean descSB = desc.getDescriptor();
//			assertNull(descSB);
			GetOperation getOp = new GetOperation();
			getOp.setPath(path);
			getOp.setRetriveContentInformation("true");
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			CmsManager manager = new CmsManager();
			CmsNode cmsnode = manager.execGetOperation(getOp);
			assertNull(cmsnode);
		} catch (BuildOperationException e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} catch (OperationExecutionException e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		
		// Part 2: tries to insert into CMS a Low Functionality that violates a
		// constraint (node parent does not exist) and sees if it is stored into DB
		
		path = "/Functionalities/SystemFunctionalities/Temp/Static Reporting ";
		
		try{
			lowfunc = lowfuncDAO.loadLowFunctionalityByID(lowfuncId);
			lowfunc.setCode("TEMP");
			lowfunc.setPath(path);
			lowfunc.setDescription("New Low Functionality");
	
			lowfuncDAO.insertLowFunctionality(lowfunc, profile);
			fail();
		} catch (EMFUserError e) {
			assertEquals(100,e.getCode());
		} finally {
			CMSRecovery.setCmsModified(false);
		}
		
		try {
			LowFunctionality reloaded = lowfuncDAO.loadLowFunctionalityByPath(path);
			assertNull(reloaded);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}
	
	/**
	 * Integrity test between DB and CMS on a delete operation. Tries to delete
	 * from DB a LowFunctionality with wrong id and sees if CMS is effected. Then tries
	 * also to delete from CMS a LowFunctionality with wrong path and sees if DB is
	 * effected.
	 */
	public void testIntegrityDbCmsOnDelete() {

		// Part 1: tries to delete from DB a Low Functionality with wrong id and sees if CMS is effected
		String path = "/Functionalities/SystemFunctionalities/Business Departments";

		LowFunctionality lowfunc = new LowFunctionality();
		Integer lowfuncId = new Integer(300);
		lowfunc.setId(lowfuncId);
		lowfunc.setPath(path);

		try {
			lowfuncDAO.eraseLowFunctionality(lowfunc,profile);

			fail();
		} catch (EMFUserError e) {
			assertEquals(100, e.getCode());
		} finally {
			CMSRecovery.setCmsModified(false);
		}

//		CMSConnection connection = null;
		try {
//			OperationExecutor executor = OperationExecutorManager.getOperationExecutor();
//			connection = CMSManager.getInstance().getConnection();
//			GetOperation get = OperationBuilder.buildGetOperation();
//			get.setPath(path);
//			get.setRetriveContentInformation("true");
//			get.setRetrivePropertiesInformation("true");
//			get.setRetriveVersionsInformation("true");
//			ElementDescriptor desc = executor.getObject(connection, get, profile, true);
//			SourceBean descSB = desc.getDescriptor();
//			assertNotNull(descSB);
//			assertEquals("Business Departments",descSB.getAttribute("name"));
//			assertEquals("container",descSB.getAttribute("type"));
			GetOperation getOp = new GetOperation();
			getOp.setPath(path);
			getOp.setRetriveContentInformation("true");
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			CmsManager manager = new CmsManager();
			CmsNode cmsnode = manager.execGetOperation(getOp);
			assertEquals("Business Departments",cmsnode.getName());
			assertEquals("container",cmsnode.getType());
		} catch (BuildOperationException e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} catch (OperationExecutionException e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		// Part 2: tries to delete from CMS a biobject with wrong path and sees if DB is effected
		path = "/Functionalities/SystemFunctionalities/Temp";

		lowfuncId = new Integer(5);
		lowfunc.setId(lowfuncId);
		lowfunc.setPath(path);

		try {
			lowfuncDAO.eraseLowFunctionality(lowfunc,profile);

			fail();
		} catch (EMFUserError e) {
			assertEquals(1003, e.getCode());
		} finally {
			CMSRecovery.setCmsModified(false);
		}

		try {
			lowfunc = lowfuncDAO.loadLowFunctionalityByID(lowfuncId);
			assertEquals("Business Departments",lowfunc.getName());
			assertEquals("FUNC",lowfunc.getCode());
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}
}
