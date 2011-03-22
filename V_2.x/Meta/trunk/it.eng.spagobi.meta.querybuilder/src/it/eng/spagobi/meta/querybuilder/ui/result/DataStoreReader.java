package it.eng.spagobi.meta.querybuilder.ui.result;

import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IFieldMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataStoreReader {

	
	public static final String TOTAL_PROPERTY = "results";
	public static final String ROOT = "rows";
	
	
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat( "dd/MM/yyyy" );
	private static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );

	
	public static List<String> getColumnNames(IDataStore dataStore) throws RuntimeException {

		int resultNumber;
		Object propertyRawValue;

		List<String> headers = new ArrayList<String>();
						
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
				headers.add(fieldHeader);
			}
	
		return headers;
	}
	
	public static String[][] getResultList(IDataStore dataStore) throws RuntimeException {
		IField field;
		IRecord record;
		Object propertyRawValue;
		propertyRawValue = dataStore.getMetaData().getProperty("resultNumber");
		String[][] result = new String[((Integer)propertyRawValue).intValue()][dataStore.getMetaData().getFieldCount()];
		String[] resultRecord = new String[dataStore.getMetaData().getFieldCount()];

						
			int j=0;


			Iterator records = dataStore.iterator();
			while(records.hasNext()) {
				record = (IRecord)records.next();
				resultRecord = new String[dataStore.getMetaData().getFieldCount()];
				for(int i = 0; i < dataStore.getMetaData().getFieldCount(); i++) {
					
					
					IFieldMetaData fieldMetaData = dataStore.getMetaData().getFieldMeta(i);
					
					propertyRawValue = fieldMetaData.getProperty("visible");
					if(propertyRawValue != null 
							&& (propertyRawValue instanceof Boolean) 
							&& ((Boolean)propertyRawValue).booleanValue() == false) {
						continue;
					}
										
					field = record.getFieldAt( dataStore.getMetaData().getFieldIndex( fieldMetaData.getName() ) );
					
					
					
					String fieldValue = "";
					if(field.getValue() != null) {
						if(Timestamp.class.isAssignableFrom(fieldMetaData.getType())) {
							fieldValue =  TIMESTAMP_FORMATTER.format(  field.getValue() );
						} else if (Date.class.isAssignableFrom(fieldMetaData.getType())) {
							fieldValue =  DATE_FORMATTER.format(  field.getValue() );
						} else {
							fieldValue =  field.getValue().toString();
						}
					}
					
					
					resultRecord[i]=(fieldValue);
				}
				result[j]=(resultRecord);
				j++;
			}
			

		
		return result;
	}
}
