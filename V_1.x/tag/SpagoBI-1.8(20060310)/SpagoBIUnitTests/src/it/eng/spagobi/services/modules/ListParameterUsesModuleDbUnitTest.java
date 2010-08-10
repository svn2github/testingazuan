package it.eng.spagobi.services.modules;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spagobi.test.dbunit.utility.DBConnectionTestCase;

public class ListParameterUsesModuleDbUnitTest extends DBConnectionTestCase {

	private SourceBean request = null, response = null, config = null;

	private ListParameterUsesModule listParameterUsesModule = null;

	private String className="it.eng.spagobi.services.modules.ListParameterUsesModule";
	
	protected void setUp() throws Exception {
		try {
			request = new SourceBean("");
			response = new SourceBean("");
			config = new SourceBean("");
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
		listParameterUsesModule = new ListParameterUsesModule();
		listParameterUsesModule.setRequestContext(defaultRequestContext);
		super.setUp();
		
		ConfigSingleton configSingleton = ConfigSingleton.getInstance();
		SourceBean moduleConfig = (SourceBean) configSingleton.getFilteredSourceBeanAttribute("MODULES.MODULE","class",className);
		config = (SourceBean) moduleConfig.getAttribute("CONFIG");
		listParameterUsesModule.init(config);
	}

	
	/**
	 * Test method for 'it.eng.spagobi.services.modules.ListParametersModule.getList(SourceBean, SourceBean)'.
	 * Verifies that the correct list of ParameterUses with given Domain is returned.
	 */
	public void testGetList() {
		ListIFace list=null;
		Integer domainId = new Integer(14);
		try {
			request.setAttribute("ID_DOMAIN",domainId);
			list=listParameterUsesModule.getList(request,response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occurred!");
		}
		assertEquals(1,list.pages());
		assertEquals(3,list.getPaginator().rows());
	}

}
