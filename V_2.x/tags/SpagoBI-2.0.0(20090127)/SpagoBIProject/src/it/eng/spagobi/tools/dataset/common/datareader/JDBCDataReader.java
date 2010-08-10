/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datareader;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.tools.dataset.common.datastore.DataStore;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.FieldMetadata;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IFieldMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.datastore.Record;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class JDBCDataReader implements IDataReader {

	private static transient Logger logger = Logger.getLogger(JDBCDataReader.class);
    
	public JDBCDataReader() {
		
	}

    
    public IDataStore read(Object data) throws EMFUserError, EMFInternalError {
	logger.debug("IN");
    	DataStore dataStore;
    	DataStoreMetaData dataStoreMeta;
    	FieldMetadata fieldMeta;
    	
    	ScrollableDataResult scrollableDataResult;
    	List columnsNames;
    	SourceBean resultSB;
    	
    	scrollableDataResult = (ScrollableDataResult)data;
    	
    	dataStore = new DataStore();
    	dataStoreMeta = new DataStoreMetaData();
    		
    	columnsNames = Arrays.asList(scrollableDataResult.getColumnNames());
    	for(int i = 0; i < columnsNames.size(); i++) {
    		fieldMeta = new FieldMetadata();
    		fieldMeta.setName( (String)columnsNames.get(i) );
    		dataStoreMeta.addFiedMeta(fieldMeta);
    	}    	
		dataStore.setMetaData(dataStoreMeta);
		
		
		resultSB = scrollableDataResult.getSourceBean();
		if( resultSB != null) {
			
				
			List rows = resultSB.getAttributeAsList("ROW");
			Iterator rowIterator = rows.iterator(); 
			while(rowIterator.hasNext()) {										
				SourceBean rowSB = (SourceBean) rowIterator.next();
				IRecord record = new Record(dataStore);
				
				for(int i = 0; i < dataStoreMeta.getFieldCount(); i++) {
					IFieldMetaData fieldMetaData = dataStoreMeta.getFieldMeta(i);
					Object value = rowSB.getAttribute( dataStoreMeta.getFieldName(i) );
					//SourceBeanAttribute columnSB = (SourceBeanAttribute) columns.get(i);						
					IField field = new Field( value );
					if(value != null) {
						dataStoreMeta.getFieldMeta(i).setType( value.getClass() );
					}
					record.appendField( field );
				}
				dataStore.appendRecord(record);
			}
			
		}
		logger.debug("OUT");
		return dataStore;
    }
}
