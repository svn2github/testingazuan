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
package it.eng.spagobi.qbe.commons.service;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import it.eng.spagobi.utilities.engines.EngineException;
import it.eng.spagobi.utilities.service.IServiceResponse;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JSONResponse implements IServiceResponse {

	String type;
	JSONObject result;
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILURE = "FAILURE";
	public static final String ACKNOWLEDGE = "ACKNOWLEDGE";
	
	JSONResponse() {}
	
	public JSONResponse(String type, JSONObject result) {
		setType( type );
		setResult( result );
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.service.IServiceResponse#getContent()
	 */
	public String getContent() throws IOException {
		JSONObject response = new JSONObject();
		try {
			response.put( "type", getType());
			if( getResult() != null ) {
				response.put( "result", getResult() );
			}
			
		} catch (JSONException e) {
			throw new IOException("Impossible to build JSON response");
		}
		return response.toString();
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.service.IServiceResponse#getName()
	 */
	public String getName() {
		return "response";
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.service.IServiceResponse#getType()
	 */
	public String getType() {		
		return "text/json";
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.service.IServiceResponse#isInline()
	 */
	public boolean isInline() {
		return true;
	}

	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}

	public void setType(String type) {
		this.type = type;
	}

}
