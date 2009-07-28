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
package it.eng.spagobi.qbe.commons.service;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class Version1QbeEngineAnalysisStateLoader extends AbstractQbeEngineAnalysisStateLoader{

	public final static String FROM_VERSION = "1";
    public final static String TO_VERSION = "2";
    
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(Version1QbeEngineAnalysisStateLoader.class);
    	
    public Version1QbeEngineAnalysisStateLoader() {
    	super();
    }
    
    public Version1QbeEngineAnalysisStateLoader(IQbeEngineAnalysisStateLoader loader) {
    	super(loader);
    }
    
	public JSONObject convert(JSONObject data) {
		JSONObject resultJSON;
		JSONObject catalogueJSON;
		JSONArray queriesJSON;
		JSONObject queryJSON;
		JSONArray fieldsJSON;
		JSONArray filtersJSON;
		JSONObject fieldJSON;
		JSONObject filterJSON;
		String fieldUniqueName;
		String operandType;
		
		logger.debug( "IN" );
		try {
			
			logger.debug( "Converting from encoding version [" + FROM_VERSION + "] to encoding version [" + TO_VERSION + "] ..." );
			
			catalogueJSON = data.getJSONObject("catalogue");
			// fix query encoding ...
			queriesJSON = catalogueJSON.getJSONArray("queries");
			for(int i = 0; i < queriesJSON.length(); i++) {
				queryJSON = queriesJSON.getJSONObject(i);
				fieldsJSON = queryJSON.getJSONArray( "fields" );
				for(int j = 0; j < fieldsJSON.length(); j++) {
					fieldJSON = fieldsJSON.getJSONObject(j);
					fieldUniqueName = fieldJSON.getString("id");
					fieldUniqueName = convertFieldUniqueName(fieldUniqueName);	
					fieldJSON.put("id", fieldUniqueName);
				}
				
				filtersJSON = queryJSON.getJSONArray( "filters" );
				for(int j = 0; j < filtersJSON.length(); j++) {
					filterJSON = filtersJSON.getJSONObject(j);
					fieldUniqueName = filterJSON.getString("id");
					fieldUniqueName = convertFieldUniqueName(fieldUniqueName);	
					filterJSON.put("id", fieldUniqueName);
					
					operandType = filterJSON.getString("otype");
					if(operandType.equals("Field Conten")) {
						fieldUniqueName = filterJSON.getString("operand");
						fieldUniqueName = convertFieldUniqueName(fieldUniqueName);
						filterJSON.put("id", fieldUniqueName);
					} else if (operandType.equals("Parent Field Content")) {
						fieldUniqueName = filterJSON.getString("operand");
						String[] chunks = fieldUniqueName.split(" ");
						fieldUniqueName = chunks[1];
						fieldUniqueName = convertFieldUniqueName(fieldUniqueName);
						filterJSON.put("id", chunks[0] + " " + fieldUniqueName);
					}
					
				}
				
			}
			
			resultJSON = new JSONObject();
			resultJSON.put("catalogue", catalogueJSON);
			
			logger.debug( "Conversion from encoding version [" + FROM_VERSION + "] to encoding version [" + TO_VERSION + "] terminated succesfully" );
		}catch(Throwable t) {
			throw new SpagoBIEngineRuntimeException("Impossible to load from rowData [" + data + "]", t);
		} finally {
			logger.debug( "OUT" );
		}
			
		
		return resultJSON;
	}

	private String convertFieldUniqueName(String fieldUniqueName) {
		String result;
		String[] chunks;
		
		logger.debug( "Field unique name to convert [" + fieldUniqueName + "]" );
		
		chunks = fieldUniqueName.split(":");

		result = "";
		for(int i = chunks.length-1; i > 0 ; i--) {
			if(!StringUtilities.isEmpty(chunks[i])) {
				/*
				if(chunks[i].indexOf("(") > 0 ) {
					chunks[i] = chunks[i].substring(0, chunks[i].indexOf("("));
				}
				*/				
				chunks[i] = chunks[i].substring(0, 1).toLowerCase() + chunks[i].substring(1);
			}
			result = StringUtilities.isEmpty(result)? chunks[i]: chunks[i] + ":" + result;
		}
		
		result = chunks[0] + ":" + result;
		
		logger.debug( "Converted field unique name [" + result + "]" );
		
		return result;
	}

}
