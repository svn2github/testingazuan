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

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.newquery.Query;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.Constants;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.qbe.commons.service.JSONAcknowledge;
import it.eng.spagobi.qbe.commons.service.JSONFailure;
import it.eng.spagobi.utilities.engines.EngineException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;



// TODO: Auto-generated Javadoc
/**
 * The Class CreateViewAction.
 * 
 * @author Andrea Gioia
 */
public class CreateViewAction extends AbstractQbeEngineAction {
	
	// INPUT PARAMETERS
	public static final String VIEW_NAME = "viewName";	
	public static final String QUERY = "query";
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(CreateViewAction.class);
    
	
    
	public void service(SourceBean request, SourceBean response) throws EngineException  {				
		String viewName = null;
		String jsonEncodedQuery = null;
		Query query = null;
		
		logger.debug("IN");
		
		try {
		
			super.service(request, response);		
		
			viewName = getAttributeAsString(VIEW_NAME);
			
			/*
			jsonEncodedQuery = getAttributeAsString( QUERY );			
			logger.debug(QUERY + " = [" + jsonEncodedQuery + "]");
			
			try {
				query = QueryEncoder.decode( jsonEncodedQuery, getDatamartModel() );
			} catch (JSONException e) {
				throw new EngineException("Impossible to syncronize the query with the server. Query passed by the client is malformed", e);
			}
			*/
			
			getEngineInstance().getDatamartModel().addView(viewName, getEngineInstance().getQuery());
			
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
			
			
			
			
			try {
			
			getHttpResponse().getWriter().write("OK");	
		} catch (Throwable t) {
			try {
				getHttpResponse().getWriter().write("KO" + t.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t.printStackTrace();
		}
		
			
	}
}
