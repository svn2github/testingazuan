/*
 *************************************************************************
 * Copyright (c) 2008 <<Your Company Name here>>
 *  
 *************************************************************************
 */

package spagobi.birt.oda.impl;

import it.eng.spagobi.sdk.proxy.DataSetsSDKServiceProxy;
import it.eng.spagobi.tools.datasource.bo.IDataSource;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

import spagobi.birt.oda.impl.util.ProxyDataRetriever;
import spagobi.birt.oda.impl.util.SpagoBIServerConnectionDefinition;

import com.ibm.icu.util.ULocale;

/**
 * Implementation class of IConnection for an ODA runtime driver.
 */
public class Connection implements IConnection
{
	DataSetsSDKServiceProxy datasource = null;
	private boolean m_isOpen = false;
	private static Logger logger = Logger.getLogger(Connection.class);
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException
	{
		logger.debug("Opening connection");

		logger.debug("The datasource is null.. Initializing the data source");
		String serverUrl = connProperties.getProperty("ServerUrl");
		String username = connProperties.getProperty("Username");
		String password = connProperties.getProperty("Password");
		

		
		DataSetsSDKServiceProxy proxy = getDataSetsSDKServiceProxy(serverUrl, username, password);
		
		datasource = proxy;

		logger.debug("Data source initialized");

		logger.debug("Connection opened");
		m_isOpen = true;    
	    
 	}
	private static DataSetsSDKServiceProxy getDataSetsSDKServiceProxy(String serverUrl, String username, String password) {
		
		Properties props = System.getProperties();
		props.put("http.proxyHost", "proxy.eng.it");
		props.put("http.proxyPort", 3128);
		
		DataSetsSDKServiceProxy proxy = new DataSetsSDKServiceProxy(username, password);

		if (serverUrl != null && !serverUrl.endsWith("/")) {
			serverUrl += "/";
		}
		proxy.setEndpoint(serverUrl + "sdk/DataSetsSDKService");
		new ProxyDataRetriever().initProxyData(proxy, serverUrl);
		
		
		return proxy;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
	    // do nothing; assumes no support for pass-through context
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close() throws OdaException
	{
        // TODO replace with data source specific implementation
	    m_isOpen = false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen() throws OdaException
	{
        // TODO Auto-generated method stub
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
	@Override
	public void setLocale(ULocale arg0) throws OdaException {
		// TODO Auto-generated method stub
		
	}
    
}
