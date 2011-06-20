/**

 SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.querybuilder.ui.shared.result;

import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IFieldMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */

public class DataStoreReader {
	
	public static final String TOTAL_PROPERTY = "results";
	public static final String ROOT = "rows";
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat( "dd/MM/yyyy" );
	private static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
	
	
	/**
	 * Get the columns metadata (i.e column name and data type)
	 * @param dataStore
	 * @return A map of metadata for every column.. Now the keys of the map are title and type
	 * @throws RuntimeException
	 */
	public static List<Map<String,Object>> getColumnMetaData(IDataStore dataStore) throws RuntimeException {

		int resultNumber;
		Object propertyRawValue;
		Map<String,Object> metadata = new HashMap<String,Object>();
		List<Map<String,Object>> headers = new ArrayList<Map<String,Object>>();
		if(dataStore!=null){			
			propertyRawValue = dataStore.getMetaData().getProperty("resultNumber");
				
			resultNumber = ((Integer)propertyRawValue).intValue();
			for(int i = 0; i < dataStore.getMetaData().getFieldCount(); i++) {
				IFieldMetaData fieldMetaData = dataStore.getMetaData().getFieldMeta(i);
					
				propertyRawValue = fieldMetaData.getProperty("visible");
				if(propertyRawValue != null 
						&& (propertyRawValue instanceof Boolean) 
						&& ((Boolean)propertyRawValue).booleanValue() == false) {
					continue;
				}
	
				String fieldHeader = fieldMetaData.getAlias() != null? fieldMetaData.getAlias(): fieldMetaData.getName();
				metadata = new HashMap<String,Object>();
				metadata.put("title", fieldHeader);
				metadata.put("type", dataStore.getMetaData().getFieldType(i));
				headers.add(metadata);
			}
		}
		return headers;
	}
	
	/**
	 * A table with the result
	 * @param dataStore the dattastore
	 * @return
	 * @throws RuntimeException
	 */
	public static int getVisibleColumns(IDataStore dataStore) throws RuntimeException {
		int visibleColumns =0;
		if(dataStore!=null){	
			Object propertyRawValue;
	
			for(int i = 0; i < dataStore.getMetaData().getFieldCount(); i++) {
				IFieldMetaData fieldMetaData = dataStore.getMetaData().getFieldMeta(i);
					
				propertyRawValue = fieldMetaData.getProperty("visible");
				if(propertyRawValue != null && (propertyRawValue instanceof Boolean) 
						&& ((Boolean)propertyRawValue).booleanValue() == false) {
					continue;
				}
	
				visibleColumns++;
			}
		}
		return visibleColumns;
	}
	
	
	/**
	 * A table with the result
	 * @param dataStore the dataStore
	 * @return
	 * @throws RuntimeException
	 */
	public static String[][] getResultList(IDataStore dataStore) throws RuntimeException {
		return getResultList(dataStore, getVisibleColumns(dataStore));
	}
	
	/**
	 * A table with the result
	 * @param dataStore
	 * @param visibleColumns
	 * @return
	 * @throws RuntimeException
	 */
	public static String[][] getResultList(IDataStore dataStore, int visibleColumns) throws RuntimeException {
		IField field;
		IRecord record;
		Object propertyRawValue;
		int rowcount=0,columncount;
		String[][] result = new String[0][0];
		
		if(dataStore!=null){	
		
			propertyRawValue = dataStore.getMetaData().getProperty("resultNumber");
			result = new String[new Long(dataStore.getRecordsCount()).intValue()][visibleColumns];
			String[] resultRecord = new String[visibleColumns];
	
			Iterator records = dataStore.iterator();
			while(records.hasNext()) {
				record = (IRecord)records.next();
				resultRecord = new String[dataStore.getMetaData().getFieldCount()];
	
				columncount=0;
				for(int i = 0; i < dataStore.getMetaData().getFieldCount(); i++) {
	
					IFieldMetaData fieldMetaData = dataStore.getMetaData().getFieldMeta(i);
	
					propertyRawValue = fieldMetaData.getProperty("visible");
					if(propertyRawValue != null && (propertyRawValue instanceof Boolean) && ((Boolean)propertyRawValue).booleanValue() == false) {
						continue;
					}
	
					//field = record.getFieldAt( dataStore.getMetaData().getFieldIndex( fieldMetaData.getName() ) );
					field = record.getFieldAt( i );
	
					String fieldValue = "";
					if(field.getValue() != null && field.getValue() != "") {
						if(Timestamp.class.isAssignableFrom(fieldMetaData.getType())) {
							fieldValue =  TIMESTAMP_FORMATTER.format(  field.getValue() );
						} else if (Date.class.isAssignableFrom(fieldMetaData.getType())) {
							fieldValue =  DATE_FORMATTER.format(  field.getValue() );
						} else {
							fieldValue =  field.getValue().toString();
						}
					}
	
					resultRecord[columncount]=(fieldValue);
					columncount++;
				}
				result[rowcount]=(resultRecord);
				rowcount++;
			}
		}

		return result;
	}
	

	
	public static int getMaxResult(IDataStore dataStore){
		return (Integer)dataStore.getMetaData().getProperty("resultNumber");
	} 

	public static SimpleDateFormat getDateFormatter(){
		return DATE_FORMATTER;
	}
	
	public static SimpleDateFormat getTimestampFormatter(){
		return TIMESTAMP_FORMATTER;
	}
}
