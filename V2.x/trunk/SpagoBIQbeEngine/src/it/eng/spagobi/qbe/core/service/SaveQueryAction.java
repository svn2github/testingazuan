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
package it.eng.spagobi.qbe.core.service;

import it.eng.qbe.conf.QbeEngineConf;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.qbe.commons.service.SpagoBIRequest;
import it.eng.spagobi.services.proxy.ContentServiceProxy;


/**
 * This action is responsible to Persist the current working query represented by
 * the object ISingleDataMartWizardObject in session
 */
public class SaveQueryAction extends AbstractQbeEngineAction {
	
	// valid input parameter names
	public static final String QUERY_ID = "queryId";
	public static final String QUERY_DESCRIPTION = "queryDescritpion";
	public static final String QUERY_VISIBILITY = "visibility";
	
	
	public void service(SourceBean request, SourceBean response) {
		
	
		String queryId = getAttributeAsString(QUERY_ID);		
		String  queryDescritpion  = getAttributeAsString(QUERY_DESCRIPTION);		
		String  visibility  = getAttributeAsString(QUERY_VISIBILITY);
		
		
		
		if ((queryId != null) && (queryId.trim().length() > 0)){
			getQuery().setQueryId(queryId);
		}
		
		if ((queryDescritpion != null) && (queryDescritpion.trim().length() > 0)){
			getDatamartWizard().setDescription(queryDescritpion);
		}
		
		if ((visibility != null) && (visibility.trim().length() > 0)){
			if (visibility.equalsIgnoreCase("public")){
				getDatamartWizard().setVisibility(true);
			}else {
				getDatamartWizard().setVisibility(false);
			}
		}
		
		if ( QbeEngineConf.getInstance().isWebModalityActive() ) {
			SpagoBIRequest spagoBIRequest = getSpagoBIRequest();
			
			if (spagoBIRequest != null){
				getDatamartWizard().setOwner(spagoBIRequest.getUserId());
			}
			
			
		}
		
		getDatamartModel().persistQueryAction( getDatamartWizard() );
		SessionContainer session = getRequestContainer().getSessionContainer();
		
		
		      
		if(getSpagoBIRequest() != null) {
			ContentServiceProxy proxy = new ContentServiceProxy(getUserId(), getHttpSession());
			 
			String result=proxy.saveSubObject(getSpagoBIRequest().getDocumentId(), 
					queryId,queryDescritpion, "" + getDatamartWizard().getVisibility(), queryId);
			
			if (result.toUpperCase().startsWith("KO")) {}
		}
		
		
		String cTM = String.valueOf(System.currentTimeMillis());
		if (isSubqueryModeActive()){
			setAttributeInSession("QBE_START_MODIFY_QUERY_TIMESTAMP", cTM);
			setAttributeInSession("QBE_LAST_UPDATE_TIMESTAMP", cTM);
		}
		
	}
}
