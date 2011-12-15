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

package it.eng.spagobi.meta.editor.multi;



import it.eng.spagobi.meta.model.physical.PhysicalModel;

import java.sql.Connection;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IManagedConnection;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.datatools.connectivity.sqm.core.connection.ConnectionInfo;
import org.eclipse.datatools.modelbase.sql.schema.Catalog;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.common.util.EList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DSEBridge {
	String defaultCatalog, defaultSchema,modelName; 
	PhysicalModel model;
	java.sql.Connection connection;
	
	private String connectionDriver;
	private String connectionUrl;
	private String connectionUsername;
	private String connectionPassword;
	private String connectionDatabaseName;

	private static Logger logger = LoggerFactory.getLogger(DSEBridge.class);

	public DSEBridge() {}
	
	public IConnectionProfile[] getConnectionProfiles() {
		IConnectionProfile[] connectionProfiles;
		connectionProfiles = ProfileManager.getInstance().getProfiles();
		
		return connectionProfiles;
	}
	
	public IConnectionProfile getConnectionProfile(String connectionName) {
		return ProfileManager.getInstance().getProfileByName(connectionName);
	}
	
	
	public Connection connect(String connectionName) {
		return connect( getConnectionProfile(connectionName) );
	}
	
	//connect to a specified connection profile and return a JDBC connection
	public Connection connect(IConnectionProfile connectionProfile) {
		Connection jdbcCon = null;
		IStatus status = connectionProfile.connect();
		Properties prop = connectionProfile.getBaseProperties();
		connectionDriver = prop.getProperty("org.eclipse.datatools.connectivity.db.driverClass");
		logger.debug("Connection driver is [{}]",connectionDriver);		
		connectionUrl = prop.getProperty("org.eclipse.datatools.connectivity.db.URL");
		logger.debug("Connection Url is [{}]",connectionUrl);
		connectionUsername = prop.getProperty("org.eclipse.datatools.connectivity.db.username");
		logger.debug("Connection Username is [{}]",connectionUsername);
		connectionPassword = prop.getProperty("org.eclipse.datatools.connectivity.db.password");
		logger.debug("Connection password is [{}]",connectionPassword);
		connectionDatabaseName = prop.getProperty("org.eclipse.datatools.connectivity.db.databaseName");
		logger.debug("Connection Database name is [{}]",connectionDatabaseName);
		
		if (status.getCode()== IStatus.OK) 
		{
		     IManagedConnection managedConnection = ((IConnectionProfile)connectionProfile).getManagedConnection ("java.sql.Connection");
		     if (managedConnection != null) {			
		    	jdbcCon = (java.sql.Connection) managedConnection.getConnection().getRawConnection();
		     }
		     
		     if (jdbcCon != null) {
		    	 return jdbcCon;
		     }
		    	 
		}
		return null;	
	}

	//return the database model object which can be used to extract schemas, tables, ect
	public Database get_dbModel(IConnectionProfile cp)
	{
		IManagedConnection managedConnection = ((IConnectionProfile)cp).getManagedConnection(
		"org.eclipse.datatools.connectivity.sqm.core.connection.ConnectionInfo");
		if (managedConnection != null) {
			ConnectionInfo connectionInfo = (ConnectionInfo) managedConnection.getConnection().getRawConnection();
			if (connectionInfo != null) {
				Database database = connectionInfo.getSharedDatabase();
				return database;
			}
		}
		return null;
	}
	
	//return the schemas from a Database object
	public EList<Schema> get_schemas(Database db)
	{
		EList<Schema> schemas = db.getSchemas();
		return schemas;
	}
	
	//return the catalogs from a Database object
	public EList<Catalog> get_catalogs(Database db)
	{
		EList<Catalog> catalogs = db.getCatalogs();
		return catalogs;
	}
	
	//return the tables from a schema object
	public EList<Table> get_tables(Schema sch)
	{
		EList<Table> tables = sch.getTables();
		return tables;
	}
	
	//return the tables from a schema object
	public EList<Column> get_columns(Table tab)
	{
		EList<Column> columns = tab.getColumns();
		return columns;
	}
	
	/**
	 * @return the connectionDriver
	 */
	public String getConnectionDriver() {
		return connectionDriver;
	}
	
	/**
	 * @return the connectionUrl
	 */
	public String getConnectionUrl() {
		return connectionUrl;
	}

	/**
	 * @return the connectionUsername
	 */
	public String getConnectionUsername() {
		return connectionUsername;
	}

	/**
	 * @return the connectionPassword
	 */
	public String getConnectionPassword() {
		return connectionPassword;
	}
	
	/**
	 * @return the connectionDatabaseName
	 */
	public String getConnectionDatabaseName() {
		return connectionDatabaseName;
	}
}
