/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2011 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.oda.impl;

import it.eng.qbe.conf.QbeCoreSettings;
import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.DataSourceFactory;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.spagobi.meta.oda.TestConnectionFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

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
    
    public static final String DATAMART_DIR_PATH = "D:\\Documenti\\Sviluppo\\servers\\tomcat6spagobi3\\resources\\qbe\\datamarts";
	public static final String DATAMART_NAME = "foodmart";

    
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException {
		
		String datamartName = connProperties.getProperty("datamart_name");
		File qbeDataMartDir = new File(connProperties.getProperty("resource_folder"));
		
		QbeCoreSettings.getInstance().setQbeDataMartDir(qbeDataMartDir);
		
		List<String> dataMartNames = new ArrayList<String>();
		dataMartNames.add( datamartName );
		
		DBConnection connection = new DBConnection();
		connection.setName( datamartName );
		connection.setDialect( connProperties.getProperty("database_dialect") );			
		connection.setJndiName( null );			
		connection.setDriverClass( connProperties.getProperty("database_driver") );			
		connection.setPassword( connProperties.getProperty("database_password") );
		connection.setUrl( connProperties.getProperty("database_url") );
		connection.setUsername( connProperties.getProperty("database_user") );	
		
		datasource = DataSourceFactory.buildDataSource(datamartName, datamartName, dataMartNames, null, connection);
		datasource.setDataMartModelAccessModality(new DataMartModelAccessModality());
	    
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
    
}
