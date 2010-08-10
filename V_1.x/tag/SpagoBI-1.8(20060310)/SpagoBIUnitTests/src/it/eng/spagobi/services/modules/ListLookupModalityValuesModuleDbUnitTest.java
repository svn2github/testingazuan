package it.eng.spagobi.services.modules;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IModalitiesValueDAO;
import it.eng.spagobi.mockObjects.EngUserProfileImplMock;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

import java.util.HashMap;
import java.util.List;

public class ListLookupModalityValuesModuleDbUnitTest extends
		DBConnectionTestCase {

	private SourceBean request = null, response = null;
	
	private SessionContainer sessionCont = null;

	private ListLookupModalityValuesModule listLookupModalityValuesModule = null;

	protected void setUp() throws Exception {
		try {
			request = new SourceBean("");
			response = new SourceBean("");
		} catch (SourceBeanException e1) {
			e1.printStackTrace();
		}
		RequestContainer reqContainer = new RequestContainer();
		ResponseContainer resContainer = new ResponseContainer();
		sessionCont = new SessionContainer(true);
		reqContainer.setSessionContainer(sessionCont);
		DefaultRequestContext defaultRequestContext = new DefaultRequestContext(
				reqContainer, resContainer);
		reqContainer.setServiceRequest(request);
		resContainer.setServiceResponse(response);
		resContainer.setErrorHandler(new EMFErrorHandler());
		listLookupModalityValuesModule = new ListLookupModalityValuesModule();
		listLookupModalityValuesModule.setRequestContext(defaultRequestContext);
		EngUserProfileImplMock profile = new EngUserProfileImplMock();
		profile.setUserAttribute("PROFILE_ATTRIBUTES", new HashMap());
		SessionContainer permSession = sessionCont.getPermanentContainer();
		permSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE, profile);
		super.setUp();	
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.services.modules.ListLookupModalityValuesModule.getList(SourceBean,
	 * SourceBean)'. 
	 * Inserts a QUERY type ModalitiesValue into DB and make the
	 * getList method perform the given query, then checks the correctness of the
	 * list returned, also with filters.
	 */
	public void testGetListQuery() {
		ListIFace list = null;
		try {
			ModalitiesValue modVal = new ModalitiesValue();
			modVal.setDescription("Descr Test Statement");
			modVal.setITypeCd("QUERY");
			modVal.setITypeId("36");
			modVal.setLabel("TEST STM");
			modVal.setName("Test Statement");
			modVal.setLovProvider("<QUERY><CONNECTION>spagobi</CONNECTION>"
							+ "<STMT>select name from sbi_engines</STMT>"
							+ "<VALUE-COLUMN>name</VALUE-COLUMN>"
							+ "<VISIBLE-COLUMNS>engine_id, name</VISIBLE-COLUMNS>"
							+ "</QUERY>");
			IModalitiesValueDAO aModalitiesValueDAO = DAOFactory
					.getModalitiesValueDAO();
			aModalitiesValueDAO.insertModalitiesValue(modVal);
			List lovList=aModalitiesValueDAO.loadAllModalitiesValue();
			ModalitiesValue reload=(ModalitiesValue) lovList.get(lovList.size()-1);
			String idModVal = reload.getId().toString();
			
			request.setAttribute("mod_val_id", idModVal);
			request.setAttribute("INPUT_TYPE", "QUERY");
			request.setAttribute("LOOKUP_PARAMETER_NAME",
					"Lookup parameter name");
			request.setAttribute("ACTOR", "Davide");
			list = listLookupModalityValuesModule.getList(request, response);
			assertEquals(1, list.pages());
			assertEquals(7, list.getPaginator().rows());
			
			response = new SourceBean("");
			request.setAttribute("valueFilter", "j");
			request.setAttribute("columnFilter", "name");
			request.setAttribute("typeFilter", "start");
			list = listLookupModalityValuesModule.getList(request, response);
			assertEquals(1, list.pages());
			assertEquals(4, list.getPaginator().rows());
			
			response = new SourceBean("");
			request.updAttribute("valueFilter", "a");
			list = listLookupModalityValuesModule.getList(request, response);
			assertEquals(0, list.pages());
			assertEquals(0, list.getPaginator().rows());
			
			response = new SourceBean("");
			request.updAttribute("valueFilter", "dev");
			request.updAttribute("typeFilter", "end");
			list = listLookupModalityValuesModule.getList(request, response);
			assertEquals(1, list.pages());
			assertEquals(5, list.getPaginator().rows());
			
			response = new SourceBean("");
			request.updAttribute("valueFilter", "Report");
			request.updAttribute("typeFilter", "contain");
			list = listLookupModalityValuesModule.getList(request, response);
			assertEquals(1, list.pages());
			assertEquals(3, list.getPaginator().rows());
			
			response = new SourceBean("");
			request.updAttribute("valueFilter", "Custom Oracle Engine DEV");
			request.updAttribute("typeFilter", "equal");
			list = listLookupModalityValuesModule.getList(request, response);
			assertEquals(1, list.pages());
			assertEquals(1, list.getPaginator().rows());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
	}
	
}
