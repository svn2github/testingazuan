package it.eng.test.unittest.action;

import it.eng.qbe.action.SelectFieldForSelectAction;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.test.utilities.CreateInitialSingleDataMartWizardObject;

import java.util.List;

import junit.framework.TestCase;

public class SelectFieldForSelectActionTest extends TestCase {

	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private SelectFieldForSelectAction selectFieldForSelectAction = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		try {
			request = new SourceBean("");
			response = new SourceBean("");
		} catch (SourceBeanException e1) {
			e1.printStackTrace();
			fail("Unaspected exception occurred!");
		}
		reqContainer = new RequestContainer();
		resContainer = new ResponseContainer();
		reqContainer.setServiceRequest(request);
		resContainer.setServiceResponse(response);
		session = new SessionContainer(true);
		reqContainer.setSessionContainer(session);
		RequestContainer.setRequestContainer(reqContainer);
		DefaultRequestContext defaultRequestContext = new DefaultRequestContext(
				reqContainer, resContainer);
		resContainer.setErrorHandler(new EMFErrorHandler());
		
		singleDataMartWizardObj = CreateInitialSingleDataMartWizardObject.createInitialSingleDataMartWizardObject();
		
		session.setAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD, singleDataMartWizardObj);
		
		selectFieldForSelectAction = new SelectFieldForSelectAction();
		selectFieldForSelectAction.setRequestContext(defaultRequestContext);
	}

	/*
	 * Test method for 'it.eng.qbe.action.SelectFieldForSelectAction.service(SourceBean, SourceBean)'
	 */
	public void testService() {

		try {
			String className = "";
			request.setAttribute("CLASS_NAME", className);
			String alias = "alias name";
			request.setAttribute("ALIAS_CLASS_NAME",alias);
			String fieldName = "field name";
			request.setAttribute("COMPLETE_FIELD_NAME",fieldName);
			String aliasFieldName = "alias filed name";
			request.setAttribute("ALIAS_FIELD_NAME",aliasFieldName);
			selectFieldForSelectAction.service(request, response);
			ISelectClause selectClause = singleDataMartWizardObj.getSelectClause();
			List selectFields = selectClause.getSelectFields();
			assertEquals(4, selectFields.size());
			ISelectField aSelectField = null;
			for (int i = 0; i < selectFields.size(); i++) {
				aSelectField = (ISelectField) selectFields.get(i);
				if (aSelectField.getFieldName().equalsIgnoreCase(fieldName)) break;
			}
			EntityClass ec = aSelectField.getFieldEntityClass();
			assertEquals(className, ec.getClassName());
			assertEquals(alias, ec.getClassAlias());
			assertEquals(fieldName, aSelectField.getFieldName());
			assertEquals(aliasFieldName, aSelectField.getFieldAlias());
			
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
		
	}

}
