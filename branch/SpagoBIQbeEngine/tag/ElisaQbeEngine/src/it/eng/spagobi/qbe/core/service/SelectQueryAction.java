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

import org.apache.log4j.Logger;
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
 * Set the query with the given query active an return it. 
 * If a query with the given id does not exist return an error
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class SelectQueryAction extends AbstractQbeEngineAction {	
	
	public static final String SERVICE_NAME = "SELECT_QUERY_ACTION";
	public String getActionName(){return SERVICE_NAME;}
	
	
	// INPUT PARAMETERS
	public static final String QUERY_ID = "id";
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(SelectQueryAction.class);
   
    
	
	public void service(SourceBean request, SourceBean response)  {				
				
		String id;
		Query query;
		JSONObject queryJSON;
					
		logger.debug("IN");
		
		try {
		
			super.service(request, response);		
			
			id = getAttributeAsString( QUERY_ID );
			logger.debug("Parameter [" + QUERY_ID + "] is equals to [" + id + "]");
			
			Assert.assertNotNull(id, "Parameter ["+ QUERY_ID +"] cannot be null in order to execute " + this.getActionName());
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			
			query = getEngineInstance().getQueryCatalogue().getQuery(id);
			Assert.assertNotNull(query, "A query with id equals to [" + id + "] does not exist in the catalogue");
			
			getEngineInstance().setActiveQuery(query);
			
			try {
				queryJSON = (JSONObject)QuerySerializerFactory.getSerializer("application/json").serialize(query, getEngineInstance().getDatamartModel());
			} catch (Throwable e) {
				throw new SpagoBIEngineServiceException(getActionName(), "An error occurred while serializig query wiyh id equals to [" + id +"]", e);
			}
			
			try {
				writeBackToClient( new JSONSuccess(queryJSON) );
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
