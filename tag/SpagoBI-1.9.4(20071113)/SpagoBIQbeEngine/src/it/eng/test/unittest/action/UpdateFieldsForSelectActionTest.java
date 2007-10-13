/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.test.unittest.action;

import it.eng.qbe.action.UpdateFieldsForSelectAction;
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

public class UpdateFieldsForSelectActionTest extends TestCase {

	private SourceBean request = null, response = null;
	private RequestContainer reqContainer = null;
	private ResponseContainer resContainer = null;
	private SessionContainer session = null;
	private ISingleDataMartWizardObject singleDataMartWizardObj = null;
	private UpdateFieldsForSelectAction updateFieldsForSelectAction = null;
	
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
		
		updateFieldsForSelectAction = new UpdateFieldsForSelectAction();
		updateFieldsForSelectAction.setRequestContext(defaultRequestContext);
	}
	
	/*
	 * Test method for 'it.eng.qbe.action.UpdateFieldsForSelectAction.service(SourceBean, SourceBean)'
	 */
	public void testService() {

		try {
			String className = "class name";
			request.setAttribute("className", className);
			String distinct = "true";
			request.setAttribute("selectDistinct", distinct);	
		 	String newFieldName = "new field name";
		 	String fieldId = "2";
		 	request.setAttribute("NEW_FIELD_"+fieldId, newFieldName);
		 	String alias = "new alias";
		 	request.setAttribute("ALIAS_FOR_"+fieldId, alias);
			updateFieldsForSelectAction.service(request, response);
			ISelectClause selectClause = singleDataMartWizardObj.getSelectClause();
			List selectFields = selectClause.getSelectFields();
			ISelectField selectField = null;
			for (int i = 0; i < selectFields.size(); i++) {
				selectField = (ISelectField) selectFields.get(i);
				if (selectField.getId().equals(fieldId)) break;
			}
			assertEquals(newFieldName, selectField.getFieldName());
			assertEquals(alias, selectField.getFieldAlias());
			assertTrue(singleDataMartWizardObj.getDistinct());
			
		} catch (SourceBeanException e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
		
	}

}
