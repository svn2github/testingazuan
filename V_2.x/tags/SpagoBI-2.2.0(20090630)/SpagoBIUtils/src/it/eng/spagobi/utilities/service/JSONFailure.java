/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.utilities.service;

import org.json.JSONException;
import org.json.JSONObject;

import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JSONFailure extends JSONResponse {

	public JSONFailure(SpagoBIEngineServiceException exception, String callback) {
		super(JSONResponse.FAILURE, createResponseContent(exception), callback );
	}
	
	public JSONFailure(SpagoBIServiceException exception, String callback) {
		super(JSONResponse.FAILURE, createResponseContent(exception), callback );
	}
	
	public JSONFailure(SpagoBIEngineServiceException exception) {
		super(JSONResponse.FAILURE, createResponseContent(exception) );
	}
	
	public JSONFailure(SpagoBIServiceException exception) {
		super(JSONResponse.FAILURE, createResponseContent(exception) );
	}

	private static JSONObject createResponseContent(SpagoBIEngineServiceException exception) {
		JSONObject content = new JSONObject();
		
		try {
			content.put("cause", exception.getCause());			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return content;
	}
	
	private static JSONObject createResponseContent(SpagoBIServiceException exception) {
		JSONObject content = new JSONObject();
		
		try {
			content.put("serviceName", exception.getServiceName());
			content.put("message", exception.getMessage());
			content.put("localizedMessage", exception.getLocalizedMessage());
			content.put("stacktrace", exception.getStackTraceDump());			
			content.put("cause", exception.getCause());			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return content;
	}

}
