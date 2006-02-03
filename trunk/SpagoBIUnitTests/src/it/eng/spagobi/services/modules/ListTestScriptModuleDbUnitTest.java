package it.eng.spagobi.services.modules;

import java.util.List;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

public class ListTestScriptModuleDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null, config = null;
	
	private SessionContainer session = null;

	private ListTestScriptModule listTestScriptModule = null;
	
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
		listTestScriptModule = new ListTestScriptModule();
		listTestScriptModule.setRequestContext(defaultRequestContext);
		session = new SessionContainer(true);
		reqContainer.setSessionContainer(session);
		super.setUp();
		
	}

	/**
	 * Test method for 'it.eng.spagobi.services.modules.ListTestQueryModule.getList(SourceBean, SourceBean)'.
	 * Verifies that the correct list is returned from the query execution.
	 */
	public void testGetList() {
		
		ModalitiesValue modVal = new ModalitiesValue();
		String scriptDetXML = "<SCRIPTLOV>\n";
		scriptDetXML += "<SCRIPT>\n";
		scriptDetXML += "StringBuffer buf = new StringBuffer();\n";
		scriptDetXML += "Random rand = new Random();\n";
		scriptDetXML += "buf.append(\"&lt;rows&gt;\");\n";
		int rows = 32;
		int pages = (int) Math.floor(rows/10) +1;
		for (int c = 0; c < rows; c++) {
			scriptDetXML += "buf.append(\"&lt;row storeName=\'STORE" + c + "\'  unitSales=\' \");\n";
			scriptDetXML += "buf.append(rand.nextFloat());\n";
			scriptDetXML += "buf.append(\"\' storeSales=\' \");\n";
			scriptDetXML += "buf.append(rand.nextFloat());\n";
			scriptDetXML += "buf.append(\"\' /&gt;\");\n";
		}
		scriptDetXML += "buf.append(\"&lt;visible-columns&gt;\");\n";
		scriptDetXML += "buf.append(\"storeName, storeSales, unitSales\");\n";
		scriptDetXML += "buf.append(\"&lt;/visible-columns&gt;\");\n";
		scriptDetXML += "buf.append(\"&lt;value-column&gt;\");\n";
		scriptDetXML += "buf.append(\"storeName\");\n";
		scriptDetXML += "buf.append(\"&lt;/value-column&gt;\");\n";
		scriptDetXML += "buf.append(\"&lt;/rows&gt;\");\n";
		scriptDetXML += "return buf;\n";
		scriptDetXML += "</SCRIPT>\n";
		scriptDetXML += "<SINGLE_VALUE>false</SINGLE_VALUE>\n";
		scriptDetXML += "<LIST_VALUES>true</LIST_VALUES>\n";
		scriptDetXML += "</SCRIPTLOV>\n";
		modVal.setLovProvider(scriptDetXML);
		session.setAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT, modVal);
		ListIFace list = null;
		try {
			list=listTestScriptModule.getList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(pages,list.pages());
		assertEquals(rows,list.getPaginator().rows());
		assertNull(response.getAttribute("errorMessage"));
		assertEquals("yes", response.getAttribute("testExecuted"));
		config = (SourceBean) response.getAttribute("CONFIG");
		SourceBean queriesSB = (SourceBean) config.getAttribute("QUERIES");
		assertEquals(0, queriesSB.getContainedAttributes().size());
		SourceBean captionsSB = (SourceBean) config.getAttribute("CAPTIONS");
		assertEquals(0, captionsSB.getContainedAttributes().size());
		SourceBean buttonsSB = (SourceBean) config.getAttribute("BUTTONS");
		assertEquals(0, buttonsSB.getContainedAttributes().size());
		SourceBean columnsSB = (SourceBean) config.getAttribute("COLUMNS");
		List columnsList = columnsSB.getContainedAttributes();
		assertEquals(3, columnsList.size());
		String[] columns = {"storeName", "storeSales", "unitSales"};
		for (int i = 0; i < columnsList.size(); i++) {
			SourceBeanAttribute columnSBA = (SourceBeanAttribute) columnsList.get(i);
			SourceBean columnSB = (SourceBean) columnSBA.getValue();
			assertEquals(columns[i], columnSB.getAttribute("name"));
		}
		
		// case of incorrect query
		try {
			response = new SourceBean("");
		} catch (SourceBeanException e) {
			e.printStackTrace();
		}
		String newLovProvider = scriptDetXML.replaceAll("StringBuffer", "Stringuffer");
		// the script now is not correct
		modVal.setLovProvider(newLovProvider);
		
		try {
			list = listTestScriptModule.getList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0,list.pages());
		assertEquals(0,list.getPaginator().rows());
		assertNotNull(response.getAttribute("errorMessage"));
	}
	
}
