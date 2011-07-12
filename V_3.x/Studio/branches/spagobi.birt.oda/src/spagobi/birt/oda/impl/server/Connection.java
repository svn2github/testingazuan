/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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

package spagobi.birt.oda.impl.server;

import it.eng.spagobi.services.proxy.DataSetServiceProxy;
import it.eng.spagobi.utilities.engines.EngineConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.util.ULocale;

/**
 * Implementation class of IConnection for an ODA runtime driver.
 */
public class Connection implements IConnection
{
	
	boolean isOpen;
	DataSetServiceProxy dataSetServiceProxy = null;
	Map pars;
	
	public static final String CONN_PROP_SERVER_URL = "ServerUrl";
	public static final String CONN_PROP_USER = "Username";
	public static final String CONN_PROP_PASSWORD = "Password";
	
	public static String SBI_BIRT_RUNTIME_IS_RUNTIME = "SBI_BIRT_RUNTIME_IS_RUNTIME";
    public static String SBI_BIRT_RUNTIME_USER_ID = "SBI_BIRT_RUNTIME_USER_ID";
    public static String SBI_BIRT_RUNTIME_SECURE_ATTRS = "SBI_BIRT_RUNTIME_SECURE_ATTRS";
    public static String SBI_BIRT_RUNTIME_SERVICE_URL = "SBI_BIRT_RUNTIME_SERVICE_URL";
    public static String SBI_BIRT_RUNTIME_SERVER_URL = "SBI_BIRT_RUNTIME_SERVER_URL";
    public static String SBI_BIRT_RUNTIME_TOKEN = "SBI_BIRT_RUNTIME_TOKEN";
    public static String SBI_BIRT_RUNTIME_PASS = "SBI_BIRT_RUNTIME_PASS";
    public static String SBI_BIRT_RUNTIME_PARS_MAP = "SBI_BIRT_RUNTIME_PARS_MAP"; 
    
    private Object context = null;
	
	private static Logger logger = LoggerFactory.getLogger(Connection.class);
	
	
	public Connection() {
		isOpen = false;
		dataSetServiceProxy = null;
	}
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException
	{
		logger.trace("IN");
		try {
			logger.debug("Trying to get the DataSetServiceProxy ...");
			dataSetServiceProxy = getDataSetProxy();
			logger.debug("DataSetServiceProxy obtained correctly");
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw (OdaException) new OdaException("Impossible to open connection").initCause(e);
		}
		logger.debug("Data source initialized");
		logger.debug("Connection succesfully opened");
		isOpen = true;    	
		logger.trace("OUT");
 	}
	
	private DataSetServiceProxy getDataSetProxy() {
		if (!isBirtRuntimeContext()) {
			throw new RuntimeException(
					"This method must be invoked in Birt runtime context!!!");
		}
		try {
			HashMap map = (HashMap) context;
			String userId = getUserId();
			String secureAttributes = getSecureAttrs();
			String serviceUrlStr = getServiceUrl();
			String spagoBiServerURL = getSpagoBIServerUrl();
			String token = getToken();
			String pass = getPass();
			pars = getParsMap();
			DataSetServiceProxy proxy = new DataSetServiceProxy(userId, secureAttributes, serviceUrlStr, spagoBiServerURL, token, pass);
			return proxy;
		} catch (Exception e) {
			throw new RuntimeException("Error while getting DataSetServiceProxy from Birt runtime context", e);
		}
	}
	
	private Map getParsMap() {
		try {
		    HashMap map = (HashMap) context;
		    Map pars = (Map) map.get(SBI_BIRT_RUNTIME_PARS_MAP);
		    return pars;
		} catch (Exception e) {
			throw new RuntimeException("Error while getting user id from Birt runtime context", e);
		}
	}
	
	private String getUserId() {
		try {
		    HashMap map = (HashMap) context;
		    String userId = (String) map.get(SBI_BIRT_RUNTIME_USER_ID);
		    return userId;
		} catch (Exception e) {
			throw new RuntimeException("Error while getting user id from Birt runtime context", e);
		}
	}
	
	private String getSecureAttrs() {
		try {
		    HashMap map = (HashMap) context;
		    String secureAttributes = (String) map.get(SBI_BIRT_RUNTIME_SECURE_ATTRS);
		    return secureAttributes;
		} catch (Exception e) {
			throw new RuntimeException("Error while getting user id from Birt runtime context", e);
		}
	}
	
	private String getServiceUrl() {
		try {
		    HashMap map = (HashMap) context;
		    String serviceUrlStr = (String) map.get(SBI_BIRT_RUNTIME_SERVICE_URL);
		    return serviceUrlStr;
		} catch (Exception e) {
			throw new RuntimeException("Error while getting user id from Birt runtime context", e);
		}
	}
	
	private String getSpagoBIServerUrl() {
		try {
		    HashMap map = (HashMap) context;
		    String spagoBiServerURL = (String) map.get(SBI_BIRT_RUNTIME_SERVER_URL);
		    return spagoBiServerURL;
		} catch (Exception e) {
			throw new RuntimeException("Error while getting user id from Birt runtime context", e);
		}
	}
	
	private String getToken() {
		try {
		    HashMap map = (HashMap) context;
		    String token = (String) map.get(SBI_BIRT_RUNTIME_TOKEN);
		    return token;
		} catch (Exception e) {
			throw new RuntimeException("Error while getting user id from Birt runtime context", e);
		}
	}
	
	private String getPass() {
		try {
		    HashMap map = (HashMap) context;
		    String pass = (String) map.get(SBI_BIRT_RUNTIME_PASS);
		    return pass;
		} catch (Exception e) {
			throw new RuntimeException("Error while getting user id from Birt runtime context", e);
		}
	}

	private boolean isBirtRuntimeContext() {
		logger.debug("Entering isBirtRuntimeContext method");
	    if (context != null && context instanceof HashMap) {
	    	HashMap map = (HashMap) context;
	    	String isRuntime = (String) map.get(SBI_BIRT_RUNTIME_IS_RUNTIME);
	    	if(isRuntime!=null && isRuntime.equals("true")){
	    		logger.debug("Ok runtime");
	    		return true;	
	    	}else{
	    		logger.debug("NOT runtime");
	    		return false;
	    	}
	    }
	    return false;
	}
	
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
		this.context = context;
		logger.debug("Driver: start setAppContext");
	    if (context != null && context instanceof HashMap) {
	    	HashMap map = (HashMap) context;
	    	EngineConstants d = null;
	    	Set<Map.Entry> entries = map.entrySet();
	    	Iterator<Map.Entry> it = entries.iterator();
	    	while (it.hasNext()) {
	    		Map.Entry entry = it.next();
	    		Object key = entry.getKey();
	    		Object value = entry.getValue();
	    		logger.debug("Entry key [" + key + "], value [" + value + "]");
	    	}
	    }
	    logger.debug("Driver: end setAppContext");
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close() throws OdaException
	{
	    isOpen = false;
	    dataSetServiceProxy = null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen() throws OdaException
	{
		return isOpen;
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
		return new Query(dataSetServiceProxy, pars);
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
