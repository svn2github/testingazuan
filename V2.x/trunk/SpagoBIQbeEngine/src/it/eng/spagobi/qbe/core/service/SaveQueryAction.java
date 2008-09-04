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

import java.io.IOException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;

import it.eng.qbe.newquery.Query;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.qbe.commons.service.JSONAcknowledge;
import it.eng.spagobi.qbe.commons.service.JSONSuccess;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.EngineAnalysisMetadata;
import it.eng.spagobi.utilities.engines.EngineException;
import it.eng.spagobi.utilities.strings.StringUtils;

import org.apache.log4j.Logger;
import org.json.JSONException;

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
	

	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(SaveQueryAction.class);
    
	
	public void service(SourceBean request, SourceBean response) throws EngineException {
		
		String queryName = null;		
		String  queryDescritpion  = null;		
		String  queryScope  = null;
		String queryRecords = null;
		String queryFilters = null;
		EngineAnalysisMetadata analysisMetadata = null;
		
		logger.debug("IN");
		
		freezeHttpResponse();
		HttpServletResponse httResponse = getHttpResponse();
		
		
		try {
			super.service(request, response);			
			
			queryName = getAttributeAsString(QUERY_NAME);		
			logger.debug(QUERY_NAME + ": " + queryName);
			queryDescritpion  = getAttributeAsString(QUERY_DESCRIPTION);
			logger.debug(QUERY_DESCRIPTION + ": " + queryDescritpion);
			queryScope  = getAttributeAsString(QUERY_SCOPE);
			logger.debug(QUERY_SCOPE + ": " + queryScope);
			queryRecords = getAttributeAsString(QUERY_RECORDS);
			logger.debug(QUERY_RECORDS + ": " + queryRecords);
			queryFilters = getAttributeAsString(QUERY_FILTERS);
			logger.debug(QUERY_FILTERS + ": " + queryFilters);
			
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			Assert.assertTrue(!StringUtils.isEmpty(queryName), "Input parameter [" + QUERY_NAME + "] cannot be null or empty in oder to execute " + this.getActionName() + " service");		
			Assert.assertTrue(!StringUtils.isEmpty(queryDescritpion), "Input parameter [" + QUERY_DESCRIPTION + "] cannot be null or empty in oder to execute " + this.getActionName() + " service");		
			Assert.assertTrue(!StringUtils.isEmpty(queryScope), "Input parameter [" + QUERY_SCOPE + "] cannot be null or empty in oder to execute " + this.getActionName() + " service");		
			Assert.assertTrue(!StringUtils.isEmpty(queryRecords), "Input parameter [" + QUERY_RECORDS + "] cannot be null or empty in oder to execute " + this.getActionName() + " service");		
			Assert.assertTrue(!StringUtils.isEmpty(queryFilters), "Input parameter [" + QUERY_FILTERS + "] cannot be null or empty in oder to execute " + this.getActionName() + " service");		
		
			analysisMetadata = getEngineInstance().getAnalysisMetadata();
			analysisMetadata.setName( queryName );
			analysisMetadata.setDescription( queryDescritpion );
		
			if( EngineAnalysisMetadata.PUBLIC_SCOPE.equalsIgnoreCase( queryScope ) ) {
				analysisMetadata.setScope( EngineAnalysisMetadata.PUBLIC_SCOPE );
			} else if( EngineAnalysisMetadata.PRIVATE_SCOPE.equalsIgnoreCase( queryScope ) ) {
				analysisMetadata.setScope( EngineAnalysisMetadata.PRIVATE_SCOPE );
			} else {
				Assert.assertUnreachable("Value [" + queryScope + "] is not valid for the input parameter " + QUERY_SCOPE);
			}
			
			Query query = null;
			try {
				query = QueryEncoder.decode(queryRecords, queryFilters, getDatamartModel());
			} catch (JSONException e) {
				throw new EngineException("Impossible to decode query string comming from client", e);
			}
			
			Query queryBkp = getEngineInstance().getQuery();
			getEngineInstance().setQuery(query);
			String result = saveAnalysisState();
			getEngineInstance().setQuery(queryBkp);
				
			try {
				httResponse.getOutputStream().write(result.getBytes());
				httResponse.getOutputStream().flush();
			} catch (IOException e) {
				logger.error(e);
			}	
			/*			
			try {
				writeBackToClient( new JSONAcknowledge() );
			} catch (IOException e) {
				throw new EngineException("Impossible to write back the responce to the client", e);
			}
			*/
			
		} catch(Exception e) {
			QbeEngineException engineException = null;
			
			if(e instanceof QbeEngineException) {
				engineException = (QbeEngineException)e;
			} else {
				engineException = new QbeEngineException("An internal error occurred in " + getActionName() + " service", e);
			}
			
			engineException.setEngineInstance( getEngineInstance() );
						
			throw engineException;
		} finally {
			logger.debug("OUT");
		}
	}
}
