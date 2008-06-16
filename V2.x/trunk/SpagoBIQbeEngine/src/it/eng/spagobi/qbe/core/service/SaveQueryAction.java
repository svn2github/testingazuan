/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.qbe.core.service;

import org.json.JSONException;

import it.eng.qbe.newquery.Query;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.EngineAnalysisMetadata;
import it.eng.spagobi.utilities.engines.EngineException;


// TODO: Auto-generated Javadoc
/**
 * This action is responsible to Persist the current working query represented by
 * the object ISingleDataMartWizardObject in session.
 */
public class SaveQueryAction extends AbstractQbeEngineAction {
	
	// INPUT PARAMETERS
	public static final String QUERY_NAME = "queryName";	
	public static final String QUERY_DESCRIPTION = "queryDescription";
	public static final String QUERY_SCOPE = "queryScope";
	public static final String QUERY_RECORDS = "queryRecords";
	public static final String QUERY_FILTERS = "queryFilters";
	
	
	public void service(SourceBean request, SourceBean response) throws EngineException {
		
		super.service(request, response);
		
		EngineAnalysisMetadata analysisMetadata = null;
	
		String queryName = getAttributeAsString(QUERY_NAME);		
		String  queryDescritpion  = getAttributeAsString(QUERY_DESCRIPTION);		
		String  queryScope  = getAttributeAsString(QUERY_SCOPE);
		String queryRecords = getAttributeAsString(QUERY_RECORDS);
		String queryFilters = getAttributeAsString(QUERY_FILTERS);
		
		analysisMetadata = getEngineInstance().getAnalysisMetadata();
		analysisMetadata.setName( queryName );
		analysisMetadata.setDescription( queryDescritpion );
		if( EngineAnalysisMetadata.PUBLIC_SCOPE.equalsIgnoreCase( queryScope ) ) {
			analysisMetadata.setScope( EngineAnalysisMetadata.PUBLIC_SCOPE );
		} else if( EngineAnalysisMetadata.PRIVATE_SCOPE.equalsIgnoreCase( queryScope ) ) {
			analysisMetadata.setScope( EngineAnalysisMetadata.PRIVATE_SCOPE );
		} else {
			throw new EngineException("Value [" + queryScope + "] is not valid for the input parameter " + QUERY_SCOPE);
		}
		
		Query query = null;
		try {
			query = QueryEncoder.decode(queryRecords, queryFilters, getDatamartModel());
		} catch (JSONException e) {
			throw new EngineException("Impossible to decode query string comming from client", e);
		}
		
		Query queryBkp = getEngineInstance().getQuery();
		getEngineInstance().setQuery(query);
		saveAnalysisState();
		getEngineInstance().setQuery(queryBkp);
	}
}
