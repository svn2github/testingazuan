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
package it.eng.spagobi.utilities.sql;

import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.mappers.SQLMapper;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.datasource.service.DataSourceSupplier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


public class DataSourceUtilities {
	private static transient Logger logger = Logger.getLogger(DataSourceUtilities.class);
	/**
	 * This method, based on the data sources table, gets a 
	 * database connection and return it  
	 * @return the database connection
	 */
	public Connection getConnection(String dsLabel) {
		//calls implementation for gets data source object
		DataSourceSupplier supplierDS = new DataSourceSupplier();		
		SpagoBiDataSource ds = supplierDS.getDataSourceByLabel(dsLabel);
		//  get sql connection
		String jndi = ds.getJndiName();
		if (jndi != null && !jndi.equals("")) {
			return getConnectionFromJndiDS(ds);
		} else {
			return getDirectConnection(ds);
		}
	}
	
	/**
	 * Get the connection from JNDI
	 * @param connectionConfig SpagoBiDataSource describing data connection
	 * @return Connection to database
	 */
	private Connection getConnectionFromJndiDS(SpagoBiDataSource connectionConfig) {
		Connection connection = null;
		Context ctx ;		
		String resName = connectionConfig.getJndiName();		
		try {
			ctx = new InitialContext();								
			DataSource ds = (DataSource) ctx.lookup(resName);		
			connection = ds.getConnection();
		} catch (NamingException ne) {
			logger.error("JNDI error", ne);
		} catch (SQLException sqle) {
			logger.error("Cannot retrive connection", sqle);
		}
		return connection;
	}
	
	/**
	 * Get the connection using jdbc 
	 * @param connectionConfig SpagoBiDataSource describing data connection
	 * @return Connection to database
	 */
	private Connection getDirectConnection(SpagoBiDataSource connectionConfig) {
		Connection connection = null;
		try {
			String driverName = connectionConfig.getDriver();
			Class.forName(driverName);
			String url = connectionConfig.getUrl();
			String username = connectionConfig.getUser();
			String password = connectionConfig.getPassword();
			connection = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			logger.error("Driver not found", e);
		} catch (SQLException e) {
			logger.error("Cannot retrive connection", e);
		}
		return connection;
	}	
	
	/**
	 * Creates a ago DataConnection object starting from a sql connection 
	 * @param con Connection to the export database
	 * @return The Spago DataConnection Object
	 * @throws EMFInternalError
	 */
	public DataConnection getDataConnection(Connection con) throws EMFInternalError {
		DataConnection dataCon = null;
		try {
			Class mapperClass = Class.forName("it.eng.spago.dbaccess.sql.mappers.OracleSQLMapper");
			SQLMapper sqlMapper = (SQLMapper)mapperClass.newInstance();
			dataCon = new DataConnection(con, "2.1", sqlMapper);
		} catch(Exception e) {
			logger.error("Error while getting Data Source " + e);
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "cannot build spago DataConnection object");
		}
		return dataCon;
	}
	
	

}
