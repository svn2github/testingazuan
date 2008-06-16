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

import it.eng.qbe.model.IStatement;
import it.eng.qbe.model.XIStatement;
import it.eng.qbe.newquery.SelectField;
import it.eng.qbe.query.ISelectField;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;



// TODO: Auto-generated Javadoc
/**
 * The Class ExecuteQueryAction.
 */
public class ExecuteQueryAction extends AbstractQbeEngineAction {
	
	
	/** The Constant LIMIT. */
	public static final String LIMIT = "limit";
	
	/** The Constant START. */
	public static final String START = "start";
	
	/** The Constant MAX_RESULT. */
	public static final int MAX_RESULT = 14000;
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(ExecuteQueryAction.class);
    
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws EngineException  {				
		
		//IStatement statement = null;
		XIStatement xstatement = null;
		Integer limit = null;
		Integer start = null;
		SourceBean queryResponseSourceBean = null;
		//SourceBean xqueryResponseSourceBean = null;
		JSONObject gridDataFeed = new JSONObject();
		
		
			
		logger.debug("IN");
		
		try {
		
			super.service(request, response);		
				
			//statement = getDatamartModel().createStatement( getQuery() );
			xstatement = getDatamartModel().createXStatement( getEngineInstance().getQuery() );
			
			limit = getAttributeAsInteger( LIMIT );
			start = getAttributeAsInteger( START );			
		
			//statement.setMaxResults( MAX_RESULT );
			//statement.setParameters( getDatamartModel().getDataMartProperties() );
			//statement.setParameters( getEnv() );
			xstatement.setParameters( getEnv() );
			
			try {
				logger.debug("Execute Query.");
				//queryResponseSourceBean = statement.executeWithPagination(start, limit, MAX_RESULT);
				queryResponseSourceBean = xstatement.executeWithPagination(start, limit, MAX_RESULT);
			} catch (Exception e) {
				String query = xstatement.getQueryString();
				logger.error("An error occurred while executing query: " + query, e);
				String description = "An error occurred in " + getActionName() + " service while executing query: " + query;
				String str = e.getMessage()!=null? e.getMessage(): e.getClass().getName();
				description += "<br>The root cause of the error is: " + str;
				List hints = new ArrayList();
				hints.add("Check connection configuration");
				hints.add("Check the qbe jar file");
				throw new QbeEngineException("Service error", description, hints, e);
			}
			logger.debug("After executeWithPagination");
				
			List results = (List)queryResponseSourceBean.getAttribute("list");
			Integer resultNumber = (Integer)queryResponseSourceBean.getAttribute("resultNumber");
			logger.debug("ResultNumber="+resultNumber);
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
			gridDataFeed.put("results", resultNumber.intValue());
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
		} catch (Exception e) {
			logger.error("Exception in JASON Creation",e);
			if(e instanceof QbeEngineException) throw (QbeEngineException)e;
			
			String description = "An unpredicted error occurred while executing " + getActionName() + " service.";
			Throwable rootException = e;
			while(rootException.getCause() != null) rootException = rootException.getCause();
			String str = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
			description += "<br>The root cause of the error is: " + str;
			List hints = new ArrayList();
			hints.add("Sorry, there are no hints available right now on how to fix this problem");
			throw new QbeEngineException("Service error", description, hints, e);
		}
		
		freezeHttpResponse();
		HttpServletResponse httpResp = getHttpResponse();		
		try {
			httpResp.getOutputStream().print(gridDataFeed.toString());
			httpResp.getOutputStream().flush();
		} catch (IOException e) {
      logger.error("IOException in Output Flush",e);
		}
	}

}
