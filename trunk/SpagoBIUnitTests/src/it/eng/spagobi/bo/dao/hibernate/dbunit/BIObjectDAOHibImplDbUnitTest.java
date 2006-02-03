package it.eng.spagobi.bo.dao.hibernate.dbunit;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.cms.init.CMSInitializer;
import it.eng.spago.cms.init.CMSManager;
import it.eng.spago.cms.util.CMSRecovery;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.TemplateVersion;
import it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl;
import it.eng.spagobi.bo.dao.hibernate.ParameterUseDAOHibImpl;
import it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.mockObjects.EngUserProfileImplMock;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.mockObjects.PortletSessionImplMock;
import it.eng.spagobi.security.AnonymousCMSUserProfile;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;
import it.eng.spagobi.utilities.UploadedFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BIObjectDAOHibImplDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null;

	private BIObjectDAOHibImpl biobjectDAO = null;

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

	protected void tearDown() throws Exception {
		super.tearDown();
		CMSRecovery.setupCms();
		CMSManager.destroyInstance();
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
		IEngUserProfile profile = new AnonymousCMSUserProfile();
		permSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);

		biobjectDAO = new BIObjectDAOHibImpl();
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl.loadBIObjectForExecutionByPathAndRole(String,
	 * String)'.
	 * Loads a BIObject by path and role and verifies if it is loaded
	 * correctly.
	 */
	public void testLoadBIObjectForExecutionByPathAndRole() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT_CUST_01";
		String role = "/user";
		BIObject biobj = null;
		try {
			biobj = biobjectDAO.loadBIObjectForExecutionByPathAndRole(path,
					role);

			Integer biobjIdExpected = new Integer(13);
			assertEquals(biobjIdExpected, biobj.getId());
			assertEquals("REPORT", biobj.getBiObjectTypeCode());
			assertEquals(new Integer(40), biobj.getBiObjectTypeID());
			assertEquals("Report about Customer Profiles", biobj
					.getDescription());
			assertEquals(new Integer(0), biobj.getEncrypt());
			assertEquals(new Integer(9), biobj.getEngine().getId());
			assertEquals("RPT-CUST-01", biobj.getLabel());
			assertEquals("Customer Profile 01", biobj.getName());
			assertEquals(null, biobj.getNameCurrentTemplateVersion());
			assertEquals(path, biobj.getPath());
			assertEquals("", biobj.getRelName());
			assertEquals("DEV", biobj.getStateCode());
			assertEquals(new Integer(55), biobj.getStateID());
			assertEquals(null, biobj.getTemplate());
			List biobjParameters = biobj.getBiObjectParameters();
			assertEquals(3, biobjParameters.size());
			int[] parametersIdsExpected = { 8, 10, 14 };
			for (int i = 0; i < biobjParameters.size(); i++) {
				BIObjectParameter biobjParameter = (BIObjectParameter) biobjParameters
						.get(i);
				assertEquals(parametersIdsExpected[i], biobjParameter
						.getParID().intValue());
				assertEquals(biobjIdExpected, biobjParameter.getBiObjectID());
			}
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl.loadBIObjectForDetail(Integer)'.
	 * Loads a BIObject by id and verifies if it is loaded correctly.
	 */
	public void testLoadBIObjectForDetailInteger() {
		Integer biobjectId = new Integer(12);
		BIObject biobj = null;
		try {
			biobj = biobjectDAO.loadBIObjectForDetail(biobjectId);

			assertEquals(biobjectId, biobj.getId());
			assertEquals(null, biobj.getBiObjectParameters());
			assertEquals("OLAP", biobj.getBiObjectTypeCode());
			assertEquals(new Integer(41), biobj.getBiObjectTypeID());
			assertEquals(null, biobj.getCurrentTemplateVersion());
			assertEquals("Product Cost and Sales Olap Analysis", biobj
					.getDescription());
			assertEquals(new Integer(0), biobj.getEncrypt());
			assertEquals(new Integer(8), biobj.getEngine().getId());
			assertEquals("OLAP-PROD-COS-01", biobj.getLabel());
			assertEquals("Product Cost 01", biobj.getName());
			assertEquals(null, biobj.getNameCurrentTemplateVersion());
			assertEquals(
					"/Functionalities/SystemFunctionalities/Analytical Areas/Dimensional Analysis/OLAP_OLAP-PROD-COS-01",
					biobj.getPath());
			assertEquals("", biobj.getRelName());
			assertEquals("DEV", biobj.getStateCode());
			assertEquals(new Integer(55), biobj.getStateID());
			assertEquals(null, biobj.getTemplate());
			assertEquals(null, biobj.getTemplateVersions());
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl.loadBIObjectForDetail(String)'.
	 * Loads a BIObject by path and verifies if it is loaded correctly.
	 */
	public void testLoadBIObjectForDetailString() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT_CUST_01";
		BIObject biobj = null;
		try {
			biobj = biobjectDAO.loadBIObjectForDetail(path);

			assertEquals(new Integer(13), biobj.getId());
			assertEquals(null, biobj.getBiObjectParameters());
			assertEquals("REPORT", biobj.getBiObjectTypeCode());
			assertEquals(new Integer(40), biobj.getBiObjectTypeID());
			assertEquals("Report about Customer Profiles", biobj
					.getDescription());
			assertEquals(new Integer(0), biobj.getEncrypt());
			assertEquals(new Integer(9), biobj.getEngine().getId());
			assertEquals("RPT-CUST-01", biobj.getLabel());
			assertEquals("Customer Profile 01", biobj.getName());
			assertEquals(null, biobj.getNameCurrentTemplateVersion());
			assertEquals(path, biobj.getPath());
			assertEquals("", biobj.getRelName());
			assertEquals("DEV", biobj.getStateCode());
			assertEquals(new Integer(55), biobj.getStateID());
			assertEquals(null, biobj.getTemplate());

			TemplateVersion currentTemplateVersion = biobj
					.getCurrentTemplateVersion();
			assertEquals("Thu Jul 21 17:07:48 CEST 2005",
					currentTemplateVersion.getDataLoad());
			assertEquals("customerProfilesList.jrxml", currentTemplateVersion
					.getNameFileTemplate());
			assertEquals("1.2", currentTemplateVersion.getVersionName());
			String[] templateFileNames = {
					"customerProfiles.jrxml",
					"C:\\Andrea\\SpagoBI Scratch\\reports\\customerProfilesOK.jrxml",
					"customerProfilesList.jrxml", "E3396_2005_07_RF.TXT" };
			String[] templateVersionNames = { "1.0", "1.1", "1.2", "1.3" };
			String[] templateDatas = { "Wed Jul 20 11:51:02 CEST 2005",
					"Wed Jul 20 18:40:32 CEST 2005",
					"Thu Jul 21 17:07:48 CEST 2005",
					"Wed Aug 03 17:56:22 CEST 2005" };
			List templateVersions = biobj.getTemplateVersions();
			assertEquals(4, templateVersions.size());
			for (int i = 0; i < templateVersions.size(); i++) {
				TemplateVersion tv = (TemplateVersion) templateVersions.get(i);
				assertEquals(templateFileNames[i], tv.getNameFileTemplate());
				assertEquals(templateVersionNames[i], tv.getVersionName());
				assertEquals(templateDatas[i], tv.getDataLoad());
			}
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl.gatherCMSInformation(BIObject)'.
	 * Loads the CMS information of a BIObject and verifies if the information is loaded correctly.
	 */
	public void testGatherCMSInformation() {
		BIObject biobj = new BIObject();
		biobj
				.setPath("/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT_CUST_01");
		try {
			biobjectDAO.gatherCMSInformation(biobj);

			TemplateVersion currentTemplateVersion = biobj
					.getCurrentTemplateVersion();
			assertEquals("Thu Jul 21 17:07:48 CEST 2005",
					currentTemplateVersion.getDataLoad());
			assertEquals("customerProfilesList.jrxml", currentTemplateVersion
					.getNameFileTemplate());
			assertEquals("1.2", currentTemplateVersion.getVersionName());
			String[] templateFileNames = {
					"customerProfiles.jrxml",
					"C:\\Andrea\\SpagoBI Scratch\\reports\\customerProfilesOK.jrxml",
					"customerProfilesList.jrxml", "E3396_2005_07_RF.TXT" };
			String[] templateVersionNames = { "1.0", "1.1", "1.2", "1.3" };
			String[] templateDatas = { "Wed Jul 20 11:51:02 CEST 2005",
					"Wed Jul 20 18:40:32 CEST 2005",
					"Thu Jul 21 17:07:48 CEST 2005",
					"Wed Aug 03 17:56:22 CEST 2005" };
			List templateVersions = biobj.getTemplateVersions();
			assertEquals(4, templateVersions.size());
			for (int i = 0; i < templateVersions.size(); i++) {
				TemplateVersion tv = (TemplateVersion) templateVersions.get(i);
				assertEquals(templateFileNames[i], tv.getNameFileTemplate());
				assertEquals(templateVersionNames[i], tv.getVersionName());
				assertEquals(templateDatas[i], tv.getDataLoad());
			}
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl.loadBIObjectForTree(String)'.
	 * Load a BIObject and verifies if it is loaded correctly.
	 */
	public void testLoadBIObjectForTree() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT_CUST_01";
		BIObject biobj = null;
		try {
			biobj = biobjectDAO.loadBIObjectForTree(path);

			assertEquals(new Integer(13), biobj.getId());
			assertEquals(null, biobj.getBiObjectParameters());
			assertEquals("REPORT", biobj.getBiObjectTypeCode());
			assertEquals(new Integer(40), biobj.getBiObjectTypeID());
			assertEquals("Report about Customer Profiles", biobj
					.getDescription());
			assertEquals(new Integer(0), biobj.getEncrypt());
			assertEquals(new Integer(9), biobj.getEngine().getId());
			assertEquals("RPT-CUST-01", biobj.getLabel());
			assertEquals("Customer Profile 01", biobj.getName());
			assertEquals(null, biobj.getNameCurrentTemplateVersion());
			assertEquals(path, biobj.getPath());
			assertEquals("", biobj.getRelName());
			assertEquals("DEV", biobj.getStateCode());
			assertEquals(new Integer(55), biobj.getStateID());
			assertEquals(null, biobj.getTemplate());

			TemplateVersion currentTemplateVersion = biobj
					.getCurrentTemplateVersion();
			assertEquals(null, currentTemplateVersion);
			List templateVersions = biobj.getTemplateVersions();
			assertEquals(null, templateVersions);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl.modifyBIObject(BIObject)'.
	 * Modifies a BIObject with data loaded by another BIObject (but changing
	 * path and label) and then verifies if it is modified as request by
	 * reloading the modified BIObject.
	 */
	public void testModifyBIObject() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT_CUST_01";
		String newPath = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT-EMPL-01";
		BIObject biobj = null;
		BIObject modBiobj = null;
		String newLabel = "TEMP";
		Integer idBiobjToModify = new Integer(14);
		try {
			biobj = biobjectDAO.loadBIObjectForDetail(path);
			biobjectDAO.fillBIObjectTemplate(biobj);
			biobj.setId(idBiobjToModify);
			biobj.setPath(newPath);
			biobj.setLabel(newLabel);
			UploadedFile template = biobj.getTemplate();
			byte[] content = template.getFileContent();

			biobjectDAO.modifyBIObject(biobj);

			modBiobj = biobjectDAO.loadBIObjectForDetail(newPath);
			biobjectDAO.fillBIObjectTemplate(modBiobj);
			biobj = biobjectDAO.loadBIObjectForDetail(path);
			biobjectDAO.fillBIObjectTemplate(biobj);
			assertEquals(newLabel, modBiobj.getLabel());
			assertEquals(idBiobjToModify, modBiobj.getId());
			assertEquals(biobj.getTemplate().getFileName(), modBiobj
					.getTemplate().getFileName());
			byte[] actualContent = modBiobj.getTemplate().getFileContent();
			for (int i = 0; i < actualContent.length; i++)
				assertEquals(content[i], actualContent[i]);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(true);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl.modifyBIObjectWithoutVersioning(BIObject)'.
	 * Modifies without versioning a BIObject with data loaded by another
	 * BIObject (but changing path and label) and then verifies if it is
	 * modified as request by reloading the modified BIObject and the initial
	 * BIObject.
	 */
	public void testModifyBIObjectWithoutVersioning() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT_CUST_01";
		String newPath = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT-EMPL-01";
		BIObject biobj = null;
		BIObject modBiobj = null;
		String newLabel = "TEMP";
		Integer idBiobjToModify = new Integer(14);
		try {
			biobj = biobjectDAO.loadBIObjectForDetail(path);
			biobj.setId(idBiobjToModify);
			biobj.setPath(newPath);
			biobj.setLabel(newLabel);

			biobjectDAO.modifyBIObjectWithoutVersioning(biobj);

			modBiobj = biobjectDAO.loadBIObjectForDetail(newPath);
			biobj = biobjectDAO.loadBIObjectForDetail(path);
			assertEquals(newLabel, modBiobj.getLabel());
			assertEquals(idBiobjToModify, modBiobj.getId());
			assertEquals(biobj.getNameCurrentTemplateVersion(), modBiobj
					.getNameCurrentTemplateVersion());
			assertEquals(biobj.getRelName(), modBiobj.getRelName());
			assertEquals(biobj.getStateCode(), modBiobj.getStateCode());
			assertEquals(newPath, modBiobj.getPath());
			assertEquals(biobj.getBiObjectTypeCode(), modBiobj
					.getBiObjectTypeCode());
			assertEquals(biobj.getDescription(), modBiobj.getDescription());
			assertEquals(biobj.getName(), modBiobj.getName());
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl.insertBIObject(BIObject)'.
	 * Loads a BIObject, changes the CMS information and other fields of that
	 * BIObject and inserts the modified BIObject; then verifies that it is correctly inserted by reloading it.
	 */
	public void testInsertBIObject() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_TEMP_01";

		UploadedFile template = new UploadedFile();
		template.setFileName("Report.temp");
		byte[] content = { 'a', 'f', 'f', '4', 'g', 'O', 'D' };
		template.setFileContent(content);

		TemplateVersion current = new TemplateVersion();
		current.setDataLoad(new Date().toString());
		current.setNameFileTemplate("Report.temp");
		current.setVersionName("23.1");
		List versions = new ArrayList();
		versions.add(current);

		BIObject biobj = null;
		BIObject reloaded = null;

		Integer biobjId = new Integer(13);
		try {
			biobj = biobjectDAO.loadBIObjectForDetail(biobjId);
			biobj.setPath(path);
			biobj.setTemplate(template);
			biobj.setLabel("Report temp");
			biobj.setName("Report temp for test");
			biobj.setCurrentTemplateVersion(current);
			biobj.setNameCurrentTemplateVersion("15.1");
			biobj.setTemplateVersions(versions);

			biobjectDAO.insertBIObject(biobj);

			biobj.setTemplate(template);
			reloaded = biobjectDAO.loadBIObjectForDetail(path);
			biobjectDAO.fillBIObjectTemplate(reloaded);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(true);
		}
		assertEquals(biobj.getLabel(), reloaded.getLabel());
		assertEquals(null, reloaded.getNameCurrentTemplateVersion()); // ??????
		assertEquals(biobj.getRelName(), reloaded.getRelName());
		assertEquals(biobj.getStateCode(), reloaded.getStateCode());
		assertEquals(biobj.getPath(), reloaded.getPath());
		assertEquals(biobj.getBiObjectTypeCode(), reloaded
				.getBiObjectTypeCode());
		assertEquals(biobj.getDescription(), reloaded.getDescription());
		assertEquals(biobj.getName(), reloaded.getName());
		assertEquals(biobj.getTemplate().getFileName(), reloaded.getTemplate()
				.getFileName());
		byte[] actualContent = reloaded.getTemplate().getFileContent();
		for (int i = 0; i < actualContent.length; i++)
			assertEquals(content[i], actualContent[i]);
		List actualVersions = biobj.getTemplateVersions();
		assertEquals(versions.size(), actualVersions.size());
		TemplateVersion version = biobj.getCurrentTemplateVersion();
		assertEquals(current.getDataLoad(), version.getDataLoad());
		assertEquals(current.getNameFileTemplate(), version
				.getNameFileTemplate());
		assertEquals(current.getVersionName(), version.getVersionName());
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl.eraseBIObject(BIObject)'.
	 * Erases a BIObject and then tries unsuccessfully to load the same.
	 */
	public void testEraseBIObject() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT_CUST_01";
		Integer biobjId = new Integer(13);
		BIObject biobj = new BIObject();
		biobj.setId(biobjId);
		biobj.setPath(path);
		try {
			biobjectDAO.eraseBIObject(biobj);
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(true);
		}

		try {
			biobjectDAO.loadBIObjectForDetail(biobjId);
		} catch (EMFUserError e) {
			assertEquals(100, e.getCode());
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl.fillBIObjectTemplate(BIObject)'.
	 * Loads the template of a BIObject and verifies if the
	 * template is loaded correctly.
	 */
	public void testFillBIObjectTemplate() {
		try {
			String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT_CUST_01";
			BIObject biobj = new BIObject();
			biobj.setPath(path);
			biobjectDAO.fillBIObjectTemplate(biobj);
			UploadedFile template = biobj.getTemplate();
			assertEquals("customerProfilesList.jrxml", template.getFileName());
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl.getCorrectRolesForExecution(String,
	 * IEngUserProfile)'.
	 * Loads the correct roles for the execution of a BIObject
	 * and verifies if they are correctly loaded in relation to the profile
	 * passes as input.
	 */
	public void testGetCorrectRolesForExecutionStringIEngUserProfile() {
		String path = null;
		EngUserProfileImplMock profile = new EngUserProfileImplMock();
		Collection rolesExpected = new ArrayList();
		rolesExpected.add("/admin");
		rolesExpected.add("/guest");
		rolesExpected.add("/portal");
		rolesExpected.add("/portal/admin");
		rolesExpected.add("/portal/share");
		rolesExpected.add("/portal/site");
		rolesExpected.add("/user");
		rolesExpected.add("/SpagoBiDev");
		rolesExpected.add("/SpagoBiTest");
		rolesExpected.add("/SpagoBiUser");
		profile.setRoles(rolesExpected);
		List roles = null;
		try {
			// the BIObject referred by the following path can be executed by
			// all roles
			path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT_CUST_01";

			roles = biobjectDAO.getCorrectRolesForExecution(path, profile);

			assertEquals(rolesExpected.size(), roles.size());
			assertTrue(rolesExpected.containsAll(roles));

			// the BIObject referred by the following path can be executed by
			// all roles
			path = "/Functionalities/SystemFunctionalities/Analytical Areas/Dimensional Analysis/OLAP_OLAP-PROD-COS-01";

			roles = biobjectDAO.getCorrectRolesForExecution(path, profile);

			assertEquals(rolesExpected.size(), roles.size());
			assertTrue(rolesExpected.containsAll(roles));

			// the BIObject referred by the following path can be executed by
			// all roles
			path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT-EMPL-01";

			roles = biobjectDAO.getCorrectRolesForExecution(path, profile);

			assertEquals(rolesExpected.size(), roles.size());
			assertTrue(rolesExpected.containsAll(roles));

			// adds some non-existing roles and verifies that they are not
			// returned
			Collection nonExistingRoles = new ArrayList();
			nonExistingRoles.add("nonExistingRole1");
			nonExistingRoles.add("nonExistingRole2");
			rolesExpected.addAll(nonExistingRoles);

			roles = biobjectDAO.getCorrectRolesForExecution(path, profile);

			rolesExpected.removeAll(nonExistingRoles);
			assertEquals(rolesExpected.size(), roles.size());
			assertTrue(rolesExpected.containsAll(roles));

			// modifies the ParameterUse with id = 23 and checks if the correct
			// roles are returned
			ParameterUseDAOHibImpl paruseDAO = new ParameterUseDAOHibImpl();
			Integer paruseID = new Integer(23);
			ParameterUse paruse = paruseDAO.loadByUseID(paruseID);
			paruseDAO.fillRolesForParUse(paruse);
			List associatedRoles = new ArrayList();
			RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
			associatedRoles.add(roleDAO.loadByID(new Integer(2))); // /guest
																	// role
			associatedRoles.add(roleDAO.loadByID(new Integer(4))); // /portal/admin
																	// role
			associatedRoles.add(roleDAO.loadByID(new Integer(14))); // /SpagoBiDev
																	// role
			associatedRoles.add(roleDAO.loadByID(new Integer(15))); // /SpagoBiTest
																	// role
			paruse.setAssociatedRoles(associatedRoles);
			paruseDAO.modifyParameterUse(paruse);
			roles = biobjectDAO.getCorrectRolesForExecution(path, profile);
			assertEquals(associatedRoles.size(), roles.size());
			for (int i = 0; i < associatedRoles.size(); i++) {
				Role role = (Role) associatedRoles.get(i);
				assertTrue(roles.contains(role.getName()));
			}

			// modifies the profile's roles and checks if the correct roles are
			// returned
			rolesExpected.remove("/guest");
			roles = biobjectDAO.getCorrectRolesForExecution(path, profile);
			associatedRoles.remove(0);
			assertEquals(associatedRoles.size(), roles.size());
			for (int i = 0; i < associatedRoles.size(); i++) {
				Role role = (Role) associatedRoles.get(i);
				assertTrue(roles.contains(role.getName()));
			}

		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		} finally {
			CMSRecovery.setCmsModified(false);
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.bo.dao.hibernate.BIObjectDAOHibImpl.getCorrectRolesForExecution(String)'.
	 * Loads the correct roles for the execution of a BIObject and verifies if
	 * they are correctly loaded.
	 */
	public void testGetCorrectRolesForExecutionString() {
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT_CUST_01";
		List roles = null;
		try {
			roles = biobjectDAO.getCorrectRolesForExecution(path);

			assertEquals(10, roles.size());
			String[] roleNamesExpected = { "/admin", "/guest", "/portal",
					"/portal/admin", "/portal/share", "/portal/site", "/user",
					"/SpagoBiDev", "/SpagoBiTest", "/SpagoBiUser" };
			for (int i = 0; i < roles.size(); i++) {
				assertEquals(roleNamesExpected[i], roles.get(i).toString());
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
	 * into DB a biobject that violates a constraint (label already existing)
	 * and sees if it is stored in CMS. Then tries also to insert into CMS a
	 * biobject that violates a constraint (parent path non existing) and sees
	 * if it is stored in DB.
	 */
	public void testIntegrityDbCmsOnInsert() {

		// Part 1: tries to insert into DB a biobject that violates a
		// constraint (label already existing) and sees if it is stored in CMS
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_TEMP_01";

		UploadedFile template = new UploadedFile();
		template.setFileName("Report.temp");
		byte[] content = { 'a', 'f', 'f', '4', 'g', 'O', 'D' };
		template.setFileContent(content);

		TemplateVersion current = new TemplateVersion();
		current.setDataLoad(new Date().toString());
		current.setNameFileTemplate("Report.temp");
		current.setVersionName("23.1");
		List versions = new ArrayList();
		versions.add(current);

		BIObject biobj = null;

		Integer biobjId = new Integer(13);
		try {
			biobj = biobjectDAO.loadBIObjectForDetail(biobjId);
			biobj.setPath(path);
			biobj.setTemplate(template);
			biobj.setName("Report temp for test");
			biobj.setCurrentTemplateVersion(current);
			biobj.setNameCurrentTemplateVersion("15.1");
			biobj.setTemplateVersions(versions);
			biobjectDAO.insertBIObject(biobj);

			fail();
		} catch (EMFUserError e) {
			assertEquals(100, e.getCode());
		} finally {
			CMSRecovery.setCmsModified(false);
		}

		try {
			biobjectDAO.gatherCMSInformation(biobj);
			assertEquals("", biobj.getCurrentTemplateVersion().getVersionName());
			assertNull(biobj.getCurrentTemplateVersion().getNameFileTemplate());
			assertNull(biobj.getCurrentTemplateVersion().getDataLoad());
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		// Part 2: tries to insert into CMS a biobject that violates a
		// constraint (parent path non existing) and sees if it is stored in DB
		path = "/Functionalities/SystemFunctionalities/Analytical Areas/Temp/REPORT_TEMP_01";

		try {
			biobj = biobjectDAO.loadBIObjectForDetail(biobjId);
			biobj.setPath(path);
			biobj.setLabel("RPT_TEMP_01");
			biobj.setTemplate(template);
			biobj.setName("Report temp for test");
			biobj.setCurrentTemplateVersion(current);
			biobj.setNameCurrentTemplateVersion("15.1");
			biobj.setTemplateVersions(versions);
			biobjectDAO.insertBIObject(biobj);

			fail();
		} catch (EMFUserError e) {
			assertEquals(100, e.getCode());
		} finally {
			CMSRecovery.setCmsModified(false);
		}

		try {
			Session aSession = null;
			Transaction tx = null;
			aSession = biobjectDAO.getSession();
			tx = aSession.beginTransaction();
			path = path.toUpperCase();
			String hql = " from SbiObjects where upper(path) = '" + path + "'";
			Query hqlQuery = aSession.createQuery(hql);
			SbiObjects hibObject = (SbiObjects) hqlQuery.uniqueResult();

			assertNull(hibObject);

			tx.rollback();
			aSession.close();
		} catch (HibernateException he) {
			he.printStackTrace();
			fail("Unexpected exception occurred!");
		}

	}

	/**
	 * Integrity test between DB and CMS on a delete operation. Tries to delete
	 * from DB a biobject with wrong id and sees if CMS is effected. Then tries
	 * also to delete from CMS a biobject with wrong path and sees if DB is
	 * effected.
	 */
	public void testIntegrityDbCmsOnDelete() {

		// Part 1: tries to delete from DB a biobject with wrong id and sees if
		// CMS is effected
		String path = "/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT_CUST_01";

		BIObject biobj = new BIObject();
		Integer biobjId = new Integer(300);
		biobj.setId(biobjId);
		biobj.setPath(path);

		try {
			biobjectDAO.eraseBIObject(biobj);

			fail();
		} catch (EMFUserError e) {
			assertEquals(100, e.getCode());
		} finally {
			CMSRecovery.setCmsModified(false);
		}

		try {
			biobj = biobjectDAO.loadBIObjectForDetail(path);
			assertEquals("1.2", biobj.getCurrentTemplateVersion()
					.getVersionName());
			assertEquals("customerProfilesList.jrxml", biobj
					.getCurrentTemplateVersion().getNameFileTemplate());
			assertEquals("Thu Jul 21 17:07:48 CEST 2005", biobj
					.getCurrentTemplateVersion().getDataLoad());
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}

		// Part 2: tries to delete from CMS a biobject with wrong path and sees
		// if DB is effected
		path = "/Functionalities/SystemFunctionalities/Analytical Areas/Temp/REPORT_RPT_CUST_01";

		biobj = new BIObject();
		biobjId = new Integer(13);
		biobj.setId(biobjId);
		biobj.setPath(path);

		try {
			biobjectDAO.eraseBIObject(biobj);

			fail();
		} catch (EMFUserError e) {
			assertEquals(100, e.getCode());
		} finally {
			CMSRecovery.setCmsModified(false);
		}

		try {
			biobj = biobjectDAO.loadBIObjectForDetail(biobjId);
			assertEquals(
					"/Functionalities/SystemFunctionalities/Analytical Areas/Static Reporting/REPORT_RPT_CUST_01",
					biobj.getPath());
		} catch (EMFUserError e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}
}
