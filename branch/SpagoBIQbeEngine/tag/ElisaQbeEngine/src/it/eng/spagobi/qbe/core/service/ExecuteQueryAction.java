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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.eng.qbe.model.HQLStatement;
import it.eng.qbe.model.IStatement;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.SelectField;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.qbe.QbeEngineConfig;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONSuccess;


/**
 * The Class ExecuteQueryAction.
 */
public class ExecuteQueryAction extends AbstractQbeEngineAction {	
	
	// INPUT PARAMETERS
	public static final String LIMIT = "limit";
	public static final String START = "start";
	public static final String QUERY_ID = "id";
	
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(ExecuteQueryAction.class);
    public static transient Logger auditlogger = Logger.getLogger("audit.query");
    
	
	public void service(SourceBean request, SourceBean response)  {				
				
		
		String queryId = null;
		Integer limit = null;
		Integer start = null;
		Integer maxSize = null;
		boolean isMaxResultsLimitBlocking = false;
		
		
		Query query = null;
		IStatement statement = null;
		
		SourceBean queryResponseSourceBean = null;		
		List results = null;
		Integer resultNumber = null;
		JSONObject gridDataFeed = new JSONObject();
					
		logger.debug("IN");
		
		try {
		
			super.service(request, response);		
			
			queryId = getAttributeAsString( QUERY_ID );
			logger.debug("Parameter [" + QUERY_ID + "] is equals to [" + queryId + "]");
			
			start = getAttributeAsInteger( START );	
			logger.debug("Parameter [" + START + "] is equals to [" + start + "]");
			
			limit = getAttributeAsInteger( LIMIT );
			logger.debug("Parameter [" + LIMIT + "] is equals to [" + limit + "]");
						
			maxSize = QbeEngineConfig.getInstance().getResultLimit();			
			logger.debug("Configuration setting  [" + "QBE.QBE-SQL-RESULT-LIMIT.value" + "] is equals to [" + (maxSize != null? maxSize: "none") + "]");
			isMaxResultsLimitBlocking = QbeEngineConfig.getInstance().isMaxResultLimitBlocking();
			logger.debug("Configuration setting  [" + "QBE.QBE-SQL-RESULT-LIMIT.isBlocking" + "] is equals to [" + isMaxResultsLimitBlocking + "]");
			
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			Assert.assertNotNull(queryId, "Parameter [" + QUERY_ID + "] cannot be null in oder to execute " + this.getActionName() + " service");
			
			query = getEngineInstance().getQueryCatalogue().getQuery(queryId);
			Assert.assertNotNull(query, "Query object with id [" + queryId + "] does not exist in the catalogue");
			
			if(getEngineInstance().getActiveQuery() != null && getEngineInstance().getActiveQuery().getId().equals(queryId)) {
				logger.debug("Query with id [" + queryId + "] is the current active query. Previous generated statment will be reused");
				query = getEngineInstance().getActiveQuery();
			} else {
				logger.debug("Query with id [" + queryId + "] is not the current active query. A new statment will be generated");
				getEngineInstance().setActiveQuery(query);
				
			}
			
			statement = getEngineInstance().getStatment();	
			statement.setParameters( getEnv() );
			
			String hqlQuery = statement.getQueryString();
			String sqlQuery = ((HQLStatement)statement).getSqlQueryString();
			logger.debug("Executable query (HQL): [" +  hqlQuery+ "]");
			logger.debug("Executable query (SQL): [" + sqlQuery + "]");
			UserProfile userProfile = (UserProfile)getEnv().get(EngineConstants.ENV_USER_PROFILE);
			auditlogger.info("[" + userProfile.getUserId() + "]:: HQL: " + hqlQuery);
			auditlogger.info("[" + userProfile.getUserId() + "]:: SQL: " + sqlQuery);
			
			
			try {
				logger.debug("Executing query ...");
				queryResponseSourceBean = statement.execute(start, limit, (maxSize == null? -1: maxSize.intValue()), isMaxResultsLimitBlocking);
				Assert.assertNotNull(queryResponseSourceBean, "The sourcebean returned by method executeWithPagination of the class it.eng.qbe.model.XIStatement cannot be null");
			} catch (Exception e) {
				logger.debug("Query execution aborted because of an internal exceptian");
				SpagoBIEngineServiceException exception;
				String message;
				
				message = "An error occurred in " + getActionName() + " service while executing query: [" +  statement.getQueryString() + "]";				
				exception = new SpagoBIEngineServiceException(getActionName(), message, e);
				exception.addHint("Check if the query is properly formed: [" + statement.getQueryString() + "]");
				exception.addHint("Check connection configuration");
				exception.addHint("Check the qbe jar file");
				
				throw exception;
			}
			logger.debug("Query executed succesfully");
			
			results = (List)queryResponseSourceBean.getAttribute("list");
			Assert.assertNotNull(results, "The the attribute [list] of the sourcebean returned by method executeWithPagination of the class it.eng.qbe.model.XIStatement cannot be null");
			logger.debug("Fetched records: " + results.size());
			
			resultNumber = (Integer)queryResponseSourceBean.getAttribute("resultNumber");
			Assert.assertNotNull(resultNumber, "The the attribute [resultNumber] of the sourcebean returned by method executeWithPagination of the class it.eng.qbe.model.XIStatement cannot be null");
			logger.debug("Total records: " + resultNumber);			
			
			boolean overflow = resultNumber >= maxSize;
			if (overflow) {
				logger.warn("Query results number [" + resultNumber + "] exceeds max result limit that is [" + maxSize + "]");
				auditlogger.info("[" + userProfile.getUserId() + "]:: max result limit [" + maxSize + "] exceeded with SQL: " + sqlQuery);
			}
			
			gridDataFeed = buildGridDataFeed(results, resultNumber.intValue());	
			
			try {
				writeBackToClient( new JSONSuccess(gridDataFeed) );
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
			
		} catch(Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			logger.debug("OUT");
		}	
		
		
	}
	
	private JSONObject buildGridDataFeed(List results, int resultNumber) throws JSONException {
		JSONObject gridDataFeed = new JSONObject();
		
		Iterator it = results.iterator();
		Object o = null;
		Object[] row;
		
		JSONObject metadata = new JSONObject();
		JSONArray fields = null;
		JSONArray rows = new JSONArray();
			
		metadata.put("totalProperty", "results");
		metadata.put("root", "rows");
		metadata.put("id", "id");
		gridDataFeed.put("metaData", metadata);
		gridDataFeed.put("results", resultNumber);
		gridDataFeed.put("rows", rows);
		
		int recNo = 0;
		while (it.hasNext()){
			o = it.next();
			
		    if (!(o instanceof Object[])){
		    	row = new Object[1];
		    	row[0] = o == null? "": o.toString();
		    }else{
		    	row = (Object[])o;
		    }
		    
			if(fields == null) {
				fields = new JSONArray();
				fields.put("recNo");
				// Giro le intestazioni di colonne
				Iterator fieldsIterator = getEngineInstance().getActiveQuery().getSelectFields().iterator();
				for (int j=0; j < row.length; j++){ 
					JSONObject field = new JSONObject();
					SelectField f = (SelectField)fieldsIterator.next();
					if(!f.isVisible()) continue;
					String header = f.getAlias();
					if( header != null) field.put("header", header);
					else field.put("header", "Column-" + (j+1));
					
					field.put("name", "column-" + (j+1));						
					
					field.put("dataIndex", "column-" + (j+1));
					fields.put(field);
				}					
				metadata.put("fields", fields);
			}
		    
		    // Giro le colonne
			JSONObject record= null;
			for (int j=0; j < row.length; j++){ 
				if(record == null) {
					record = new JSONObject();
					record.put("id", ++recNo);
				}
				record.put("column-" + (j+1), row[j]!=null?row[j].toString():" ");
			}
			if(record != null) rows.put(record);
		}		
		
		return gridDataFeed;
	}

}
