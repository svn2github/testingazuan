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

import it.eng.qbe.catalogue.QueryCatalogue;
import it.eng.qbe.query.serializer.SerializationConstants;
import it.eng.spagobi.qbe.initializer.engine.service.QbeEngineStartAction;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class VersionZeroQbeEngineAnalysisStateLoader implements IQbeEngineAnalysisStateLoader{

	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(QbeEngineStartAction.class);
	
    
	public Object load(String rowData) {
		JSONObject catalogueJSON;
		JSONArray queriesJSON;
		JSONObject queryJSON;
		JSONArray filtersJSON;
		JSONObject filterJSON;
		// just to create well formed id for loaded queries
		QueryCatalogue catalogue;
		
		catalogueJSON = new JSONObject();
		queriesJSON = new JSONArray();
		try {
			queryJSON = new JSONObject(rowData);
			// fix query encoding ...
			catalogue = new QueryCatalogue();
			String queryId = catalogue.getNextValidId();
			queryJSON.put( SerializationConstants.ID,  queryId);
			queryJSON.put( SerializationConstants.NAME, "query_" + queryId );
			queryJSON.put( SerializationConstants.DESCRIPTION, "query_" + queryId );
			queryJSON.put( SerializationConstants.DISTINCT, false );
			queryJSON.put( SerializationConstants.SUBQUERIES, new JSONArray() );
			
			filtersJSON = queryJSON.getJSONArray(SerializationConstants.FILTERS);
			for(int i = 0; i < filtersJSON.length(); i++) {
				filterJSON = filtersJSON.getJSONObject(i);
				filterJSON.put(SerializationConstants.FILTER_IS_FREE, false);
				filterJSON.put(SerializationConstants.FILTER_DEFAULT_VALUE, "");
				filterJSON.put(SerializationConstants.FILTER_LAST_VALUE, "");
			}
			
			
			
			queriesJSON.put(queryJSON);
			catalogueJSON.put("queries", queriesJSON);
		}catch(Throwable t) {
			throw new SpagoBIEngineRuntimeException("Impossible to load from rowData [" + rowData + "]", t);
		}
		
		return catalogueJSON;
	}

}
