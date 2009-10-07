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
package it.eng.qbe.model;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import it.eng.qbe.query.serializer.SerializationException;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IFieldMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.utilities.assertion.Assert;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class DataStoreJSONSerializer {
	
	public static final String TOTAL_PROPERTY = "results";
	public static final String ROOT = "rows";
	
	
	public Object serialize(IDataStore dataStore) throws SerializationException {
		JSONObject  result = null;
		JSONObject metadata;
		IField field;
		JSONArray fieldsMetaDataJSON;		
		JSONObject fieldMetaDataJSON;
		IRecord record;
		JSONObject recordJSON;
		int recNo;
		
		
		JSONArray recordsJSON;
		int resultNumber;
		Object propertyRawValue;
		
		Assert.assertNotNull(dataStore, "Object to be serialized connot be null");
		
		try {
			result = new JSONObject();
			
			metadata = new JSONObject();
				
			metadata.put("totalProperty", TOTAL_PROPERTY);
			metadata.put("root", ROOT);
			metadata.put("id", "id");
			result.put("metaData", metadata);
			
			propertyRawValue = dataStore.getMetaData().getProperty("resultNumber");
			Assert.assertNotNull(propertyRawValue, "DataStore property [resultNumber] cannot be null");
			Assert.assertTrue(propertyRawValue instanceof Integer, "DataStore property [resultNumber] must be of type [Integer]");
			resultNumber = ((Integer)propertyRawValue).intValue();
			Assert.assertTrue(resultNumber >= 0, "DataStore property [resultNumber] cannot be equal to [" + resultNumber + "]. It must be greater or equal to zero");	
			result.put(TOTAL_PROPERTY, resultNumber);
			
			recordsJSON = new JSONArray();
			result.put(ROOT, recordsJSON);
		
			// field's meta
			fieldsMetaDataJSON = new JSONArray();
			fieldsMetaDataJSON.put("recNo"); // counting column
			for(int i = 0; i < dataStore.getMetaData().getFieldCount(); i++) {
				IFieldMetaData fieldMetaData = dataStore.getMetaData().getFieldMeta(i);
				
				propertyRawValue = fieldMetaData.getProperty("visible");
				if(propertyRawValue != null 
						&& (propertyRawValue instanceof Boolean) 
						&& ((Boolean)propertyRawValue).booleanValue() == false) {
					continue;
				}
				
				fieldMetaDataJSON = new JSONObject();
				fieldMetaDataJSON.put("header", fieldMetaData.getName());
				fieldMetaDataJSON.put("name", "column-" + (i+1));						
				fieldMetaDataJSON.put("dataIndex", "column-" + (i+1));
				Boolean calculated = (Boolean)fieldMetaData.getProperty("calculated");
				if(calculated.booleanValue() == true) {
					DataSetVariable variable =  (DataSetVariable)fieldMetaData.getProperty("variable");
					if(variable.getType().equalsIgnoreCase(DataSetVariable.HTML)) {
						fieldMetaDataJSON.put("renderer", "html");
					}
					
				}
				fieldsMetaDataJSON.put(fieldMetaDataJSON);
			}
			metadata.put("fields", fieldsMetaDataJSON);
			
			// records
			recNo = 0;
			Iterator records = dataStore.iterator();
			while(records.hasNext()) {
				record = (IRecord)records.next();
				recordJSON = new JSONObject();
				recordJSON.put("id", ++recNo);
				
				for(int i = 0; i < dataStore.getMetaData().getFieldCount(); i++) {
					IFieldMetaData fieldMetaData = dataStore.getMetaData().getFieldMeta(i);
					
					propertyRawValue = fieldMetaData.getProperty("visible");
					if(propertyRawValue != null 
							&& (propertyRawValue instanceof Boolean) 
							&& ((Boolean)propertyRawValue).booleanValue() == false) {
						continue;
					}
										
					field = record.getFieldAt( dataStore.getMetaData().getFieldIndex( fieldMetaData.getName() ) );
					recordJSON.put("column-" + (i+1), field.getValue()==null? "": field.getValue().toString());
				}
				
				recordsJSON.put(recordJSON);
			}
			
		
			
			
		} catch(Throwable t) {
			throw new SerializationException("An unpredicted error occurred while serializing dataStore", t);
		} finally {
			
		}
		
		return result;
	}
}
