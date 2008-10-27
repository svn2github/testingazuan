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

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.newquery.Query;
import it.eng.spagobi.qbe.core.service.QueryEncoder;
import it.eng.spagobi.utilities.engines.EngineAnalysisState;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeEngineAnalysisState  extends EngineAnalysisState {
	
	// property name
	public static final String QUERY = "QUERY";
	public static final String DATAMART_MODEL = "DATAMART_MODEL";
	
	
	public QbeEngineAnalysisState( DataMartModel datamartModel ) {
		super( );
		setDatamartModel( datamartModel );
	}

	public void load(byte[] rowData) throws SpagoBIEngineException {
		String str = null;
			
		str = new String( rowData );
		Query query = null;
		try {
			query = QueryEncoder.decode( str, getDatamartModel() );
		} catch (JSONException e) {
			throw new SpagoBIEngineException("Impossible to load analysis state from row-data", e);
		}
		setProperty( QUERY, query );
	}

	public byte[] store() throws SpagoBIEngineException {
		String rowData = null;
				
		try {
			rowData = QueryEncoder.encode( getQuery(), getDatamartModel() );
		} catch (JSONException e) {
			throw new SpagoBIEngineException("Impossible to store analysis state from query object", e);
		}
		
		return rowData.getBytes();
	}

	public Query getQuery() {
		return (Query)getProperty( QUERY );
	}

	public void setQuery(Query query) {
		setProperty( QUERY, query );
	}

	public DataMartModel getDatamartModel() {
		return (DataMartModel)getProperty( DATAMART_MODEL );
	}

	public void setDatamartModel(DataMartModel datamartModel) {
		setProperty( DATAMART_MODEL, datamartModel );
	}
	
	
}
