/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.engines.console.services;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.engines.console.ConsoleEngineInstance;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utilities.DataSourceUtilities;




/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 */
public class MonitorAction extends AbstractConsoleEngineAction {
	
	
	public static final String SERVICE_NAME = "MONITOR";
	
	// request parameters
	public static String DATASET_LABEL = "ds_label";
	public static String USER_ID = "userId";
	public static String CALLBACK = "callback";
	public static String MESSAGE = "message";
	public static String SCHEMA = "schema";
	public static String STMT = "stmt";

	
	// logger component
	private static Logger logger = Logger.getLogger(MonitorAction.class);
	
	ConsoleEngineInstance consoleEngineInstance;
	
public void service(SourceBean request, SourceBean response) {
		
		String message;
		String user;
		String callback;

		logger.debug("IN");
		
		try {
			super.service(request,response);
			consoleEngineInstance = getConsoleEngineInstance();
					
			DataSourceUtilities utility = new DataSourceUtilities();
			//gets hashmap with all parameters			
			HashMap<String , Object> params = new HashMap <String , Object>();
			params =  utility.getAttributesAsMap(request);
						
			//check for mandatory parameters 
			message  = (String) params.get(MESSAGE);
			logger.debug("Parameter [" + MESSAGE + "] is equals to [" + message + "]");			
			Assert.assertTrue(!StringUtilities.isEmpty( message ), "Parameter [" + MESSAGE + "] cannot be null or empty");
			
			user = getAttributeAsString( USER_ID );
			logger.debug("Parameter [" + USER_ID + "] is equals to [" + user + "]");			
			Assert.assertTrue(!StringUtilities.isEmpty( user ), "Parameter [" + USER_ID + "] cannot be null or empty");
			
			callback = getAttributeAsString( CALLBACK );
			logger.debug("Parameter [" + CALLBACK + "] is equals to [" + callback + "]");
			
			boolean result = utility.executeUpdateQuery(params);
			if ( !result ){
				//something in update goes wrong...				
			}
			
		} catch (Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			logger.debug("OUT");
		}
	}
	
	private void getEmptyResult(String message){
		try {
			JSONArray emptyListJSON = new JSONArray();
			JSONObject results = new JSONObject();
			results.put("message", message);
			results.put("results", emptyListJSON);
			results.put("totalCount", emptyListJSON.length());
			writeBackToClient( new JSONSuccess( results ) ); 
		} catch (IOException e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Impossible to write back the responce to the client", e);
		} catch (JSONException e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize objects into a JSON object", e);
		}
	}
	


}
