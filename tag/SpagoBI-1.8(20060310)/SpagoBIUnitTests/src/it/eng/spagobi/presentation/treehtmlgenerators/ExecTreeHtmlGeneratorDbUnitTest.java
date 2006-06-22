package it.eng.spagobi.presentation.treehtmlgenerators;

import it.eng.spago.base.PortletAccess;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.cms.init.CMSInitializer;
import it.eng.spago.cms.init.CMSManager;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.httpchannel.AdapterPortlet;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.hibernate.LowFunctionalityDAOHibImpl;
import it.eng.spagobi.bo.dao.hibernate.RoleDAOHibImpl;
import it.eng.spagobi.mockObjects.EngUserProfileImplMock;
import it.eng.spagobi.mockObjects.HttpServletRequestImplMock;
import it.eng.spagobi.mockObjects.PortletPreferencesImplMock;
import it.eng.spagobi.mockObjects.PortletRequestImplMock;
import it.eng.spagobi.mockObjects.RenderRequestImplMock;
import it.eng.spagobi.mockObjects.RenderResponseImplMock;
import it.eng.spagobi.presentation.treehtmlgenerators.util.RecordForExec;
import it.eng.spagobi.presentation.treehtmlgenerators.util.Tree;
import it.eng.spagobi.services.dao.TreeObjectsDAO;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;
import it.eng.spagobi.utilities.PortletUtilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletRequest;

public class ExecTreeHtmlGeneratorDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null;

	private ExecTreeHtmlGenerator execTreeHtmlGenerator = null;
	
	private HttpServletRequest httpRequest = null;
	
	private Tree actualTree = null;
	
	private EngUserProfileImplMock profile = null;
	
	private RequestContainer reqContainer = null;
	
	private ResponseContainer resContainer = null;
	
	private String nameTree = null;
	
	private String rootFolderName = null;
	
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
	}
	
	private void setupContext() {

		try {
			request = new SourceBean("");
			response = new SourceBean("");
		} catch (SourceBeanException e1) {
			e1.printStackTrace();
		}
		reqContainer = new RequestContainer();
		resContainer = new ResponseContainer();
		reqContainer.setServiceRequest(request);
		resContainer.setServiceResponse(response);
		resContainer.setErrorHandler(new EMFErrorHandler());
		RequestContainer.setRequestContainer(reqContainer);
		PortletRequestImplMock portletRequest = new PortletRequestImplMock();
		PortletPreferences portletPreferences = new PortletPreferencesImplMock();
		portletRequest.setPortletPreferences(portletPreferences);
		reqContainer.setAttribute("PORTLET_REQUEST", portletRequest);
		SessionContainer session = new SessionContainer(true);
		reqContainer.setSessionContainer(session);
		SessionContainer permSession = session.getPermanentContainer();
		profile = new EngUserProfileImplMock();
		permSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
		httpRequest = new HttpServletRequestImplMock();	
		RenderResponseImplMock renderResponse = new RenderResponseImplMock();
		RenderRequestImplMock renderRequest = new RenderRequestImplMock();
		renderRequest.setContextPath("ContextPath");
		renderResponse.setUrl("URL");
		PortletSession portletSession = renderRequest.getPortletSession();
		portletSession.setAttribute(AdapterPortlet.REQUEST_CONTAINER_NAME,"MockContainer");
		portletSession.setAttribute("MockContainer",reqContainer);
		Locale locale = new Locale("it","ITALY","");
		portletSession.setAttribute("PortalLocale",locale);
		portletRequest.setPortletSession(portletSession);
		PortletAccess.setPortletRequest(portletRequest);
		httpRequest.setAttribute("javax.portlet.response",renderResponse);
		httpRequest.setAttribute("javax.portlet.request",renderRequest);
		execTreeHtmlGenerator = new ExecTreeHtmlGenerator();
		
		// the tree name and the root folder name are internazionalized
		nameTree = PortletUtilities.getMessage("tree.objectstree.name" ,"messages");
		rootFolderName = PortletUtilities.getMessage("tree.rootfolder.name", "messages");
		
		actualTree = new Tree();
		actualTree.addRecord(new RecordForExec(rootFolderName,0,0,0,0));
		actualTree.addRecord(new RecordForExec("Business Departments",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Analytical Areas",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Static Reporting",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Customer Profile 01",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Employee 01",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Customer profile 02",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Employee pos 02",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Customer Profile 03",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Employee Position 03",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Dimensional Analysis",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Product Cost 01",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Product Analysis 02",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Product analysis 03",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Data Mining Models",0,0,0,0));
		actualTree.addRecord(new RecordForExec("Dashboards",0,0,0,0));
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.presentation.treehtmlgenerators.ExecTreeHtmlGenerator.makeTree(SourceBean,
	 * HttpServletRequest)'. 
	 * Verifies that the "/admin" role sees the correct Objects Tree and has the expected permissions.
	 */
	public void testMakeTreeAdmin() throws Exception {
		Collection roles = new ArrayList();
		roles.add("/admin");
		profile.setRoles(roles);
		String initialPath = "/Functionalities/SystemFunctionalities";
		TreeObjectsDAO treeDAO = new TreeObjectsDAO();
		SourceBean tree = treeDAO.getXmlTreeObjects(initialPath, profile);
		StringBuffer output = execTreeHtmlGenerator.makeTree(tree, httpRequest);
		
		findTreeNodes(output, actualTree);
		
		Tree expectedTree = new Tree();
		expectedTree.addRecord(new RecordForExec(rootFolderName,1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Business Departments",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Analytical Areas",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Static Reporting",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Customer Profile 01",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Employee 01",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Customer profile 02",1,1,1,0));
		expectedTree.addRecord(new RecordForExec("Employee pos 02",1,1,1,0));
		expectedTree.addRecord(new RecordForExec("Customer Profile 03",1,1,0,1));
		expectedTree.addRecord(new RecordForExec("Employee Position 03",1,1,0,1));
		expectedTree.addRecord(new RecordForExec("Dimensional Analysis",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Product Cost 01",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Product Analysis 02",1,1,0,1));
		expectedTree.addRecord(new RecordForExec("Product analysis 03",1,1,1,0));
		expectedTree.addRecord(new RecordForExec("Data Mining Models",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Dashboards",1,0,0,0));
		
		assertEquals(expectedTree,actualTree);
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.presentation.treehtmlgenerators.ExecTreeHtmlGenerator.makeTree(SourceBean,
	 * HttpServletRequest)'. 
	 * Verifies that the "/user" role sees the correct Objects Tree and has the expected permissions.
	 */
	public void testMakeTreeUser() throws Exception {
		Collection roles = new ArrayList();
		roles.add("/user");
		profile.setRoles(roles);
		String initialPath = "/Functionalities/SystemFunctionalities";
		TreeObjectsDAO treeDAO = new TreeObjectsDAO();
		SourceBean tree = treeDAO.getXmlTreeObjects(initialPath,profile);
		StringBuffer output = execTreeHtmlGenerator.makeTree(tree,httpRequest);
	
		findTreeNodes(output,actualTree);
		
		Tree expectedTree = new Tree();
		expectedTree.addRecord(new RecordForExec(rootFolderName,1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Business Departments",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Analytical Areas",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Static Reporting",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Customer Profile 01",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Employee 01",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Customer profile 02",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Employee pos 02",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Customer Profile 03",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Employee Position 03",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Dimensional Analysis",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Product Cost 01",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Product Analysis 02",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Product analysis 03",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Data Mining Models",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Dashboards",1,0,0,0));
		
		assertEquals(expectedTree,actualTree);
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.presentation.treehtmlgenerators.ExecTreeHtmlGenerator.makeTree(SourceBean,
	 * HttpServletRequest)'. 
	 * Verifies that the "/SpagoBiUser" role sees the correct Objects Tree and has the expected permissions.
	 */
	public void testMakeTreeSpagoBiUser() throws Exception {
		Collection roles = new ArrayList();
		roles.add("/SpagoBiUser");
		profile.setRoles(roles);
		String initialPath = "/Functionalities/SystemFunctionalities";
		TreeObjectsDAO treeDAO = new TreeObjectsDAO();
		SourceBean tree = treeDAO.getXmlTreeObjects(initialPath, profile);
		StringBuffer output = execTreeHtmlGenerator.makeTree(tree, httpRequest);

		findTreeNodes(output, actualTree);

		Tree expectedTree = new Tree();
		expectedTree.addRecord(new RecordForExec(rootFolderName,1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Business Departments",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Analytical Areas",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Static Reporting",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Customer Profile 01",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Employee 01",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Customer profile 02",1,1,1,0));
		expectedTree.addRecord(new RecordForExec("Employee pos 02",1,1,1,0));
		expectedTree.addRecord(new RecordForExec("Customer Profile 03",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Employee Position 03",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Dimensional Analysis",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Product Cost 01",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Product Analysis 02",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Product analysis 03",1,1,1,0));
		expectedTree.addRecord(new RecordForExec("Data Mining Models",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Dashboards",1,0,0,0));
		
		assertEquals(expectedTree,actualTree);
		
		LowFunctionalityDAOHibImpl lowfuncDAO = new LowFunctionalityDAOHibImpl();
		LowFunctionality lowfunc = lowfuncDAO.loadLowFunctionalityByPath("/Functionalities/SystemFunctionalities/Analytical Areas/Dimensional Analysis");
		Role[] rolesForTest = new Role[1];
		RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
		rolesForTest[0] = roleDAO.loadByID(new Integer(16)); // /SpagoBiUser role
		lowfunc.setTestRoles(rolesForTest);
		lowfuncDAO.modifyLowFunctionality(lowfunc);
		
		output = execTreeHtmlGenerator.makeTree(tree,httpRequest);
		
		findTreeNodes(output,actualTree);
		
		expectedTree.updRecord(new RecordForExec("Product Analysis 02",1,1,0,1));
		
		assertEquals(expectedTree,actualTree);
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.presentation.treehtmlgenerators.ExecTreeHtmlGenerator.makeTree(SourceBean,
	 * HttpServletRequest)'. 
	 * Verifies that the "/SpagoBiTest" role sees the correct Objects Tree and has the expected permissions.
	 */
	public void testMakeTreeSpagoBiTest() throws Exception {
		Collection roles = new ArrayList();
		roles.add("/SpagoBiTest");
		profile.setRoles(roles);
		String initialPath = "/Functionalities/SystemFunctionalities";
		TreeObjectsDAO treeDAO = new TreeObjectsDAO();
		SourceBean tree = treeDAO.getXmlTreeObjects(initialPath, profile);
		StringBuffer output = execTreeHtmlGenerator.makeTree(tree, httpRequest);

		findTreeNodes(output, actualTree);

		Tree expectedTree = new Tree();
		expectedTree.addRecord(new RecordForExec(rootFolderName,1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Business Departments",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Analytical Areas",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Static Reporting",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Customer Profile 01",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Employee 01",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Customer profile 02",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Employee pos 02",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Customer Profile 03",1,1,0,1));
		expectedTree.addRecord(new RecordForExec("Employee Position 03",1,1,0,1));
		expectedTree.addRecord(new RecordForExec("Dimensional Analysis",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Product Cost 01",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Product Analysis 02",1,1,0,1));
		expectedTree.addRecord(new RecordForExec("Product analysis 03",0,0,0,0));
		expectedTree.addRecord(new RecordForExec("Data Mining Models",1,0,0,0));
		expectedTree.addRecord(new RecordForExec("Dashboards",1,0,0,0));
		
		assertEquals(expectedTree,actualTree);
		
		LowFunctionalityDAOHibImpl lowfuncDAO = new LowFunctionalityDAOHibImpl();
		LowFunctionality lowfunc = lowfuncDAO.loadLowFunctionalityByPath("/Functionalities/SystemFunctionalities/Analytical Areas/Dimensional Analysis");
		Role[] rolesForExec = new Role[1];
		RoleDAOHibImpl roleDAO = new RoleDAOHibImpl();
		rolesForExec[0] = roleDAO.loadByID(new Integer(15)); // /SpagoBiTest role
		lowfunc.setExecRoles(rolesForExec);
		lowfuncDAO.modifyLowFunctionality(lowfunc);
		
		output = execTreeHtmlGenerator.makeTree(tree,httpRequest);

		findTreeNodes(output,actualTree);

		expectedTree.updRecord(new RecordForExec("Product analysis 03",1,1,1,0));
		
		assertEquals(expectedTree,actualTree);
	}
	
	private void findTreeNodes (StringBuffer output,Tree actualTree) throws Exception{
		int index = 0;
		while (index < output.length()){
			index = output.indexOf("treeExecObj.add",index);
			if (index == -1) break;
			int indexEndOfRow = output.indexOf(";",index);
			String row = output.substring(index,indexEndOfRow);
			RecordForExec rec = new RecordForExec(row);
			if (!rec.getName().equalsIgnoreCase(nameTree)) actualTree.updRecord(rec);
			index = indexEndOfRow;
		}
	}

}
