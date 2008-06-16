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

import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.newquery.Query;
import it.eng.qbe.query.IOrderByField;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.query.IWhereField;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.qbe.commons.service.JSONAcknowledge;
import it.eng.spagobi.qbe.commons.service.JSONFailure;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.EngineException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
			
			try {
				query = QueryEncoder.decode( jsonEncodedQuery, getDatamartModel() );
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
			if(e instanceof QbeEngineException) throw (QbeEngineException)e;
			
			String description = "An unpredicted error occurred while executing " + getActionName() + " service.";
			Throwable rootException = e;
			while(rootException.getCause() != null) rootException = rootException.getCause();
			String str = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
			description += "<br>The root cause of the error is: " + str;
			List hints = new ArrayList();
			hints.add("Sorry, there are no hints available right now on how to fix this problem");
			QbeEngineException engineException = new QbeEngineException("Service error", description, hints, e);
			try {
				writeBackToClient( new JSONFailure( engineException ) );
			} catch (IOException ioe) {
				throw new EngineException("Impossible to write back the responce to the client", e);
			}
			throw engineException;
		} finally {
			logger.debug("OUT");
		}
	}

}
