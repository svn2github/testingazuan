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
package it.eng.spagobi.engines.geo.dataset;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DataSetMetaData {
	
	private Map columns;
	
	public DataSetMetaData() {
		columns = new HashMap();
	}
	
	public Set getColumnNames() {
		if(columns !=  null) {
			return columns.keySet();
		}
		return null;
	}
	
	private Set getColumnNames(String type) {
		Set columnNames = getColumnNames();
		if(columnNames !=  null) {
			Set filteredColumnNames = new HashSet();
			Iterator it = columnNames.iterator();
			while(it.hasNext()) {
				String columnName = (String)it.next();
				if( type.equalsIgnoreCase( getColumnType(columnName))) {
					filteredColumnNames.add( columnName ); 
				}
			}
			return filteredColumnNames;
		}
		return null;
	}
	
	public Set getMeasureColumnNames() {
		return getColumnNames( "measure" );
	}
	
	public String getColumnProperty(String columnName, String propertyName) {
		if(columns !=  null) {
			Properties properties = (Properties)columns.get( columnName );
			if(properties != null) {
				return properties.getProperty( propertyName );
			}
		}
		return null;
	}
	
	public void setColumnProperty(String columnName, String propertyName, String propertyValue) {
		if(columns !=  null) {
			Properties properties = (Properties)columns.get( columnName );
			if(properties != null) {
				properties.setProperty( propertyName, propertyValue );
			}
		}
	}
	
	public void addColumn(String columnName){
		if(columns !=  null) {
			columns.put( columnName, new Properties() );
		}
	}
	
	public String getGeoIdColumnName(String hierarchyName) {
		Set names = getColumnNames();
		if( names != null) {
			Iterator it = names.iterator();
			while( it.hasNext() ) {
				String columnName = (String)it.next();
				if ( isGeoIdColumn(columnName) ) {
					if(  hierarchyName.equalsIgnoreCase( getColumnProperty(columnName, "hierarchy") ) ) {
						return getColumnProperty(columnName, "column_id");
					}
				}
			}
		}
		return null;
	}
	
	public String getLevelName(String hierarchyName) {
		Set names = getColumnNames();
		if( names != null) {
			Iterator it = names.iterator();
			while( it.hasNext() ) {
				String columnName = (String)it.next();
				if ( isGeoIdColumn(columnName) ) {
					if(  hierarchyName.equalsIgnoreCase( getColumnProperty(columnName, "hierarchy") ) ) {
						return getColumnProperty(columnName, "level");
					}
				}
			}
		}
		return null;
	}
	
	public String getColumnType(String columnName) {
		return getColumnProperty(columnName, "type");
	}
	
	public boolean isGeoIdColumn( String columnName ) {
		return "geoid".equalsIgnoreCase( getColumnType( columnName ) );
	}
	
	public String getAggregationFunction(String columnName) {
		return getColumnProperty(columnName, "func");
	}
	
	
}
