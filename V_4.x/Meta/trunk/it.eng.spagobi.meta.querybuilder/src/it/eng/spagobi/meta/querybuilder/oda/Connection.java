/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.oda;

import it.eng.qbe.datasource.ConnectionDescriptor;
import it.eng.qbe.datasource.IDataSource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.util.ULocale;

/**
 * Implementation class of IConnection for an ODA runtime driver.
 * 
 * @authors  Andrea Gioia (andrea.gioia@eng.it)
 */
public class Connection implements IConnection 
{
	IDataSource datasource = null;
	private boolean m_isOpen = false;
	private static Logger logger = LoggerFactory.getLogger(Connection.class);

   
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException {
		
		logger.debug("Opening connection");
		datasource = (IDataSource)connProperties.get("data_source");
		if(datasource==null){
			logger.debug("The datasource is null.. Initializing the data source");
			Map<String, Object> dataSourceProperties = new HashMap<String, Object>();
			
			String modelName = connProperties.getProperty("datamart_name");
			List<String> modelNames = new ArrayList<String>();
			modelNames.add( modelName );
		
			ConnectionDescriptor connection = new ConnectionDescriptor();
			connection.setName( modelName );
			connection.setDialect( connProperties.getProperty("database_dialect") );			
			connection.setJndiName( null );			
			connection.setDriverClass( connProperties.getProperty("database_driver") );			
			connection.setPassword( connProperties.getProperty("database_password") );
			connection.setUrl( connProperties.getProperty("database_url") );
			connection.setUsername( connProperties.getProperty("database_user") );	
	
			dataSourceProperties.put("connection", connection);
			dataSourceProperties.put("dblinkMap", new HashMap());
			
			String resourceFolderName = connProperties.getProperty("resource_folder");
			File resourceFolder = new File(resourceFolderName);
			
			datasource = OdaStructureBuilder.getDataSourceSingleModel(modelNames, dataSourceProperties, new File(resourceFolder, "datamart.jar"));
			logger.debug("Data source initialized");
		}
		logger.debug("Connection opened");
		m_isOpen = true;        
 	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException {
	    // do nothing; assumes no support for pass-through context
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close() throws OdaException {
		datasource = null;
	    m_isOpen = false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen() throws OdaException {
		return m_isOpen;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMetaData(java.lang.String)
	 */
	public IDataSetMetaData getMetaData( String dataSetType ) throws OdaException
	{
	    // assumes that this driver supports only one type of data set,
        // ignores the specified dataSetType
		return new DataSetMetaData( this );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang.String)
	 */
	public IQuery newQuery( String dataSetType ) throws OdaException
	{
        // assumes that this driver supports only one type of data set,
        // ignores the specified dataSetType
		logger.debug("Connection, create new Query");
		return new Query(datasource);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMaxQueries()
	 */
	public int getMaxQueries() throws OdaException
	{
		return 0;	// no limit
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#commit()
	 */
	public void commit() throws OdaException
	{
	    // do nothing; assumes no transaction support needed
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#rollback()
	 */
	public void rollback() throws OdaException
	{
        // do nothing; assumes no transaction support needed
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IConnection#setLocale(com.ibm.icu.util.ULocale)
     */
    public void setLocale( ULocale locale ) throws OdaException
    {
        // do nothing; assumes no locale support
    }

	public IDataSource getDatasource() {
		return datasource;
	}
    
    
}
