/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.qbe.statement.sql;

import it.eng.qbe.datasource.dataset.DataSetDataSource;
import it.eng.qbe.statement.AbstractQbeDataSet;
import it.eng.spagobi.services.dataset.bo.SpagoBiDataSet;
import it.eng.spagobi.tools.dataset.bo.JDBCDataSet;
import it.eng.spagobi.tools.datasource.bo.IDataSource;

import org.apache.log4j.Logger;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 */

public class SQLDataSet extends AbstractQbeDataSet {


	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(SQLDataSet.class);
    
	
	public SQLDataSet(SQLStatement statement) {
		super(statement);
	}
	
	
	public void loadData(int offset, int fetchSize, int maxResults) {

		DataSetDataSource ds = (DataSetDataSource)statement.getDataSource();
		String statementStr = statement.getQueryString();
		
		//SpagoBiDataSet dataSetConfig = new SpagoBiDataSet();
		//dataSetConfig.setDataSource( ds.getSpagoBiDataSource() );
		//dataSetConfig.setQuery(statementStr);
		JDBCDataSet dataset = new JDBCDataSet();
		dataset.setDataSource(ds.getDataSourceForReading());
		dataset.setQuery(statementStr);
		dataset.loadData(offset, fetchSize, maxResults);

		dataStore = dataset.getDataStore();
		dataStore.getMetaData();
		
				
		if(hasDataStoreTransformer()) {
			getDataStoreTransformer().transform(dataStore);
		}

	
	}
	
	public void setDataSource(IDataSource dataSource) {
		// TODO Auto-generated method stub
		
	}

	
	

}