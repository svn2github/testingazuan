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
package it.eng.spagobi.tools.dataset.common.transformer;


import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.FieldMetadata;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStoreMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.datastore.Record;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class PivotDataSetTransformer extends AbstractDataStoreTransformer {
	
	String pivotFieldName; // pivotColumn
	String valueFieldName; // pivotValue
	String groupFieldName; // pivotRow
	
	private static transient Logger logger = Logger.getLogger(PivotingTransformer.class);
    

	public PivotDataSetTransformer(String pivotFieldName, String valueFieldName, String groupFieldName) {
		this.setPivotFieldName(pivotFieldName);
		this.setValueFieldName(valueFieldName);
		this.setGroupFieldName(groupFieldName);
	}
	
	void transformDataSetMetaData(IDataStore dataStore) {
		IDataStoreMetaData dataStoreMeta;
		int pivotFieldIndex;
		int valueFieldIndex;
		int groupFieldIndex;
		
		dataStoreMeta = dataStore.getMetaData();
		pivotFieldIndex = dataStoreMeta.getFieldIndex( getPivotFieldName() );
		valueFieldIndex = dataStoreMeta.getFieldIndex( getValueFieldName() );
		groupFieldIndex = dataStoreMeta.getFieldIndex( getGroupFieldName() );
			
		Iterator it = dataStore.iterator();
		while(it.hasNext()) {
			IRecord record = (IRecord)it.next();
			
			List fields = record.getFields();
			for(int j = 0; j < fields.size(); j++) {		
				IField field = (IField)fields.get(j);
				
				if(j == pivotFieldIndex) {
					
				} else if (j == valueFieldIndex) {
					
				} else if (j == groupFieldIndex) {
					
				} else {
					
				}				
			}		
		}
		
	}

	void transformDataSetRecords(IDataStore dataStore) {
		
	}

	public String getPivotFieldName() {
		return pivotFieldName;
	}

	public void setPivotFieldName(String pivotFieldName) {
		this.pivotFieldName = pivotFieldName;
	}

	public String getValueFieldName() {
		return valueFieldName;
	}

	public void setValueFieldName(String valueFieldName) {
		this.valueFieldName = valueFieldName;
	}

	public String getGroupFieldName() {
		return groupFieldName;
	}

	public void setGroupFieldName(String groupFieldName) {
		this.groupFieldName = groupFieldName;
	}

	
}
