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

public class ListTestQueryModuleDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null, config = null;
	
	private SessionContainer session = null;

	private ListTestQueryModule listTestQueryModule = null;
	
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
		listTestQueryModule = new ListTestQueryModule();
		listTestQueryModule.setRequestContext(defaultRequestContext);
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
		String queryDetXML = "<QUERY>";
		queryDetXML += "<CONNECTION>spagobi</CONNECTION>";
		queryDetXML += "<STMT>select label as Etichetta, name as Nome, descr as Descrizione from sbi_engines</STMT>";
		queryDetXML += "<VISIBLE-COLUMNS>Etichetta, Nome, Descrizione</VISIBLE-COLUMNS>";
		queryDetXML += "<VALUE-COLUMN>Nome</VALUE-COLUMN>";
		queryDetXML += "</QUERY>";
		modVal.setLovProvider(queryDetXML);
		session.setAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT, modVal);
		
		ListIFace list = null;
		try {
			list=listTestQueryModule.getList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(1,list.pages());
		assertEquals(7,list.getPaginator().rows());
		assertNull(response.getAttribute("stacktrace"));
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
		String[] columns = {"Etichetta", "Nome", "Descrizione"};
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
		String newLovProvider = queryDetXML.replaceAll("label ", "");
		// now the query statement is not correct
		modVal.setLovProvider(newLovProvider);
		
		try {
			list = listTestQueryModule.getList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(0,list.pages());
		assertEquals(0,list.getPaginator().rows());
		assertNotNull(response.getAttribute("stacktrace"));
	}
}
