/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datareader;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.tools.dataset.common.datastore.DataStore;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.FieldMetadata;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.datastore.Record;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class JDBCStandardDataReader implements IDataReader {

	private static transient Logger logger = Logger.getLogger(JDBCStandardDataReader.class);
    
	public JDBCStandardDataReader() {
		
	}

    
    public IDataStore read(Object data) throws EMFUserError, EMFInternalError {
    	DataStore dataStore;
    	DataStoreMetaData dataStoreMeta;
    	FieldMetadata fieldMeta;
    	
    	logger.debug("IN");
    	
    	dataStore = null;
    	
    	try {
    		ResultSet rs = (ResultSet)data;
    		int columnCount = rs.getMetaData().getColumnCount();
    		int columnIndex;
    		
    		dataStore = new DataStore();
        	dataStoreMeta = new DataStoreMetaData();
        	
        	
    		for(columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
        		fieldMeta = new FieldMetadata();
        		fieldMeta.setName( rs.getMetaData().getColumnLabel(columnIndex) );
        		dataStoreMeta.addFiedMeta(fieldMeta);
        	}    
    		dataStore.setMetaData(dataStoreMeta);
    		
    		while (rs.next()) {
    			IRecord record = new Record(dataStore);
    			for(columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
    				Object columnValue = rs.getObject(columnIndex);
    				IField field = new Field( columnValue );
					if(columnValue != null) {
						dataStoreMeta.getFieldMeta(columnIndex-1).setType( columnValue.getClass() );
					}
					record.appendField( field );
    			}
    			dataStore.appendRecord(record);
    		}
    	} catch (SQLException e) {
			e.printStackTrace();
		} finally {
    		logger.debug("OUT");
    	}
    	
    	return dataStore;
    }
}
