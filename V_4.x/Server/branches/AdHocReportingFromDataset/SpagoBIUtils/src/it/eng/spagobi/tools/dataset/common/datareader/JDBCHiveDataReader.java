/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.tools.dataset.common.datareader;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.tools.dataset.common.datastore.DataStore;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.datastore.Record;
import it.eng.spagobi.tools.dataset.common.metadata.FieldMetadata;
import it.eng.spagobi.tools.dataset.common.metadata.MetaData;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * @author Monica Franceschini (monica.franceschini@eng.it)
 */
public class JDBCHiveDataReader extends AbstractDataReader {

	private static transient Logger logger = Logger.getLogger(JDBCHiveDataReader.class);
    
	
	public JDBCHiveDataReader() { }
	
	public boolean isOffsetSupported() {return true;}	
	public boolean isFetchSizeSupported() {return true;}	
	public boolean isMaxResultsSupported() {return true;}
    
    public IDataStore read(Object data) throws EMFUserError, EMFInternalError {
    	DataStore dataStore = null;
		MetaData dataStoreMeta;
    	int columnCount;
    	int columnIndex;
		ResultSet rs;

    	FieldMetadata fieldMeta;
    	
		logger.debug("IN");

		rs = (ResultSet) data;  

		
		dataStore = new DataStore();
		dataStoreMeta = new MetaData();
		dataStore.setMetaData(dataStoreMeta);

		try {				
			logger.debug("Reading data ...");
    		if(getOffset() > 0) {
    			logger.debug("Offset is equal to [" + getOffset() + "]");
    			int i=0;
     			while(i != getOffset()){
     				rs.next();
     				i++;
     			}
    			
    		} else {
    			logger.debug("Offset not set");
    		}
    		
    		long maxRecToParse = Long.MAX_VALUE;
    		if(getFetchSize() > 0) {
    			maxRecToParse = getFetchSize();
    			logger.debug("FetchSize is equal to [" + maxRecToParse + "]");
    		} else {
    			logger.debug("FetchSize not set");
    		}
    		int recCount = 0;
    		int resultNumber = 0;
    		while ((recCount < maxRecToParse) && rs.next()) {
    			IRecord record = new Record(dataStore);
            	logger.debug("Reading metadata ...");
            	columnCount = rs.getMetaData().getColumnCount();
            	
        		for(columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            		fieldMeta = new FieldMetadata();
            		
    				Object columnValue = rs.getObject(columnIndex);
    				IField field = new Field( columnValue );

    				record.appendField( field );					
					
            		String fieldName = rs.getMetaData().getColumnLabel(columnIndex);
            		
            		logger.debug("Field [" + columnIndex + "] name is equal to [" + fieldName + "]");
            		if(dataStore.getMetaData().getFieldIndex(fieldName) == -1){
                		fieldMeta.setName( fieldName );
                		fieldMeta.setType(String.class);
                		dataStore.getMetaData().addFiedMeta(fieldMeta);
            		}
            	}    
        		
    			dataStore.appendRecord(record);
    			recCount++;
    		}
    		logger.debug("Readed [" + recCount+ "] records");
    		logger.debug("Data readed succcesfully");
    		
    		if (this.isCalculateResultNumberEnabled()) {
    			logger.debug("Calculation of result set number is enabled");
    			resultNumber = getResultNumber(rs, maxRecToParse, recCount);
    			dataStore.getMetaData().setProperty("resultNumber", new Integer(resultNumber));
    		} else {
    			logger.debug("Calculation of result set number is NOT enabled");
    		}

				
		} catch (SQLException e) {
			logger.error("An unexpected error occured while reading resultset", e);
		}finally {
    		logger.debug("OUT");
    	}
		
		return dataStore;
    }
    
	private int getResultNumber(ResultSet rs, long maxRecToParse, int recCount)
			throws SQLException {
		logger.debug("IN");
		
		int toReturn =recCount;
		
		logger.debug("resultset type [" + rs.getType() + "] (" + (rs.getType()  == ResultSet.TYPE_FORWARD_ONLY) + ")");
		if (rs.getType()  == ResultSet.TYPE_FORWARD_ONLY) {

			int recordsCount = 0;
			if (recCount < maxRecToParse) {
				// records read where less then max records to read, therefore the resultset has been completely read
				recordsCount = getOffset() + recCount;
			} else {
				//recordsCount = rs.getRow();
				while (rs.next()) {
					recordsCount++;
					// do nothing, just scroll result set
				}
			}

			toReturn = recordsCount;
		}
		
		logger.debug("Reading total record numeber is equal to [" + toReturn + "]");
		logger.debug("OUT " + toReturn);
		return toReturn;
	}

	
}

