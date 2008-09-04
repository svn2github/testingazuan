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

import it.eng.qbe.newquery.Query;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.qbe.commons.service.JSONAcknowledge;
import it.eng.spagobi.qbe.commons.service.JSONFailure;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.EngineException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;


/**
 * The Class RefreshQueryAction.
 */
public class RefreshQueryAction extends AbstractQbeEngineAction {
	
	// INPUT PARAMETERS
	public static final String QUERY = "query";
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(RefreshQueryAction.class);
    
	
	public void service(SourceBean request, SourceBean response) throws EngineException  {				
		
		String jsonEncodedQuery = null;
		Query query = null;
		
		logger.debug("IN");
		
		try {
		
			super.service(request, response);		
			
			jsonEncodedQuery = getAttributeAsString( QUERY );			
			logger.debug(QUERY + " = [" + jsonEncodedQuery + "]");
			
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			Assert.assertNotNull(jsonEncodedQuery, "Input parameter [" + QUERY + "] cannot be null in oder to execute " + this.getActionName() + " service");
			
			
			try {
				query = QueryEncoder.decode( jsonEncodedQuery, getEngineInstance().getDatamartModel() );
			} catch (JSONException e) {
				throw new EngineException("Impossible to syncronize the query with the server. Query passed by the client is malformed", e);
			}
			getEngineInstance().setQuery( query );
			
			try {
				writeBackToClient( new JSONAcknowledge() );
			} catch (IOException e) {
				throw new EngineException("Impossible to write back the responce to the client", e);
			}
		
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
