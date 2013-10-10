/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.qbe.services.core;

import it.eng.qbe.query.Query;
import it.eng.qbe.query.serializer.SerializerFactory;
import it.eng.qbe.serializer.SerializationException;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;


public class GetAmbiguousFieldsAction extends AbstractQbeEngineAction {	
	
	private static final long serialVersionUID = -3367673458706691427L;

	// INPUT PARAMETERS
	public static final String QUERY_ID = "id";
	public static final String CATALOGUE = "catalogue";
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(GetAmbiguousFieldsAction.class);
    
	
	public void service(SourceBean request, SourceBean response)  {				
		Query query = null;
		
		Monitor totalTimeMonitor = null;
		Monitor errorHitsMonitor = null;
					

		logger.debug("IN");
		
		try {
		
			super.service(request, response);	
			
			totalTimeMonitor = MonitorFactory.start("QbeEngine.getAmbiguousFieldsAction.totalTime");
						
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
						
			query = getQuery();
			Assert.assertNotNull(query, "Query object not specified");
			if (getEngineInstance().getActiveQuery() == null 
					|| !getEngineInstance().getActiveQuery().getId().equals(query.getId())) {
				logger.debug("Query with id [" + query.getId() + "] is not the current active query. A new statment will be generated");
				getEngineInstance().setActiveQuery(query);
			}

			try {
				writeBackToClient( new JSONSuccess(new JSONArray()) );
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
			
		} catch(Throwable t) {
			errorHitsMonitor = MonitorFactory.start("QbeEngine.errorHits");
			errorHitsMonitor.stop();
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			if(totalTimeMonitor != null) totalTimeMonitor.stop();
			logger.debug("OUT");
		}	
	}
	
	/**
	 * Get the query to be evaluated
	 * @return
	 */
	public Query getQuery() {
		String jsonEncodedCatalogue = getAttributeAsString( CATALOGUE );			
		logger.debug(CATALOGUE + " = [" + jsonEncodedCatalogue + "]");
		if (StringUtilities.isNotEmpty(jsonEncodedCatalogue)) {
			try {
				JSONArray queries = new JSONArray( jsonEncodedCatalogue );
				for (int i = 0; i < queries.length(); i++) {					
					JSONObject queryJSON = queries.getJSONObject(i);
					Query query = deserializeQuery(queryJSON);
					getEngineInstance().getQueryCatalogue().addQuery(query);
					getEngineInstance().resetActiveQuery();
				}
			} catch (Exception e) {
				throw new SpagoBIEngineServiceException(getActionName(), "Cannot deserialize catalogue", e);
			}
		}
		String queryId = getAttributeAsString( QUERY_ID );
		logger.debug("Parameter [" + QUERY_ID + "] is equals to [" + queryId + "]");
		Query query = getEngineInstance().getQueryCatalogue().getQuery(queryId);
		return query;
	}
	
	private Query deserializeQuery(JSONObject queryJSON) throws SerializationException, JSONException {
		return SerializerFactory.getDeserializer("application/json").deserializeQuery(queryJSON.toString(), getEngineInstance().getDataSource());
	}
	
}
