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

import it.eng.qbe.conf.QbeEngineConf;
import it.eng.qbe.model.XIStatement;
import it.eng.qbe.newquery.SelectField;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;

import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * The Class ExecuteQueryAction.
 */
public class ExecuteQueryAction extends AbstractQbeEngineAction {	
	
	// INPUT PARAMETERS
	public static final String LIMIT = "limit";
	public static final String START = "start";
	
	/** The Constant MAX_RESULT. */
	//public static final int MAX_RESULT = 14000;
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(ExecuteQueryAction.class);
    
	
	public void service(SourceBean request, SourceBean response)  {				
				
		XIStatement statement = null;
		Integer limit = null;
		Integer start = null;
		Integer maxSize = null;
		SourceBean queryResponseSourceBean = null;		
		List results = null;
		Integer resultNumber = null;
		JSONObject gridDataFeed = new JSONObject();
					
		logger.debug("IN");
		
		try {
		
			super.service(request, response);		
			
			limit = getAttributeAsInteger( LIMIT );
			logger.debug(LIMIT + ": " + limit);
			start = getAttributeAsInteger( START );	
			logger.debug(START + ": " + start);
			maxSize = QbeEngineConf.getInstance().getResultLimit();			
			logger.debug("max results: " + (maxSize != null? maxSize: "none") );
			
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			Assert.assertNotNull(getEngineInstance().getQuery(), "Query object cannot be null in oder to execute " + this.getActionName() + " service");
			Assert.assertTrue(getEngineInstance().getQuery().isEmpty() == false, "Query object cannot be empty in oder to execute " + this.getActionName() + " service");
			
			statement = getDatamartModel().createXStatement( getEngineInstance().getQuery() );	
			//logger.debug("Parametric query: [" + statement.getQueryString() + "]");
			statement.setParameters( getEnv() );
			logger.debug("Executable query: [" + statement.getQueryString() + "]");
			
			try {
				logger.debug("Executing query ...");
				queryResponseSourceBean = statement.executeWithPagination(start, limit, (maxSize == null? -1: maxSize.intValue()));
				Assert.assertNotNull(queryResponseSourceBean, "The the sourcebean returned by method executeWithPagination of the class it.eng.qbe.model.XIStatement cannot be null");
			} catch (Exception e) {
				logger.debug("Query execution aborted because of an internal exceptian");
				SpagoBIEngineServiceException exception;
				String message;
				String query;
				
				query = statement.getQueryString();
				message = "An error occurred in " + getActionName() + " service while executing query: [" +  query + "]";				
				exception = new SpagoBIEngineServiceException(getActionName(), message, e);
				exception.addHint("Check if the query is properly formed: [" + query + "]");
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
			// no resources need to be released
		}	
		
		logger.debug("OUT");
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
			logger.debug("Reading result.");	
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
				Iterator fieldsIterator = getEngineInstance().getQuery().getSelectFields().iterator();
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
