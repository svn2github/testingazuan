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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import it.eng.qbe.query.Query;
import it.eng.qbe.query.serializer.QuerySerializerFactory;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONSuccess;


/**
 * Return queries searching them by id into the catalogue. If for a given id does not
 * exist any query in the catalogue an exception is thrown. If no particular id is specified 
 * all query in the catalogue will be returned.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class GetQueriesAction extends AbstractQbeEngineAction {	
	
	public static final String SERVICE_NAME = "GET_QUERIES_ACTION";
	public String getActionName(){return SERVICE_NAME;}
	
	
	// INPUT PARAMETERS
	public static final String QUERY_IDS = "queries";
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(GetQueriesAction.class);
     
	
	public void service(SourceBean request, SourceBean response)  {				
				
		JSONArray ids;
		Iterator it;
		Query query;
		JSONObject queryJSON;
		JSONArray queriesJSON;
					
		logger.debug("IN");
		
		try {
		
			super.service(request, response);		
			
			ids = getAttributeAsJSONArray( QUERY_IDS );
			logger.debug("Parameter [" + QUERY_IDS + "] is equals to [" + ids + "]");
			
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			
			
			if(ids != null && ids.length() > 0) {
				List list = new ArrayList();
				for(int i = 0; i < ids.length(); i++) {
					list.add(ids.getString(i));
				}
				it = list.iterator();
			} else {
				it = getEngineInstance().getQueryCatalogue().getIds().iterator();
			}
			
			queriesJSON = new JSONArray();
			while(it.hasNext()) {
				String id = (String)it.next();
				query = getEngineInstance().getQueryCatalogue().getQuery(id);
				
				Assert.assertNotNull(query, "A query with id equals to [" + id + "] does not exist in teh catalogue");
				
				try {
					queryJSON = (JSONObject)QuerySerializerFactory.getSerializer("application/json").serialize(query, getEngineInstance().getDatamartModel());
				} catch (Throwable e) {
					throw new SpagoBIEngineServiceException(getActionName(), "An error occurred while serializig query wiyh id equals to [" + id +"]", e);
				}
				
				queriesJSON.put( queryJSON );
			}
			
			try {
				writeBackToClient( new JSONSuccess(queriesJSON) );
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

}
