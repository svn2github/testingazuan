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
package it.eng.spagobi.tools.dataset.common.dataproxy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.tools.dataset.common.datareader.IDataReader;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.datasource.bo.IDataSource;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * 
 * TODO: rename JDBCDataProxy to JDBCSpagoDataProxy and use this proxy as the default one for any JDBC dataset
 *
 */
public class JDBCStandardDataProxy extends AbstractDataProxy {
	
	IDataSource dataSource;
	String statement;
	String schema;
	
	private static transient Logger logger = Logger.getLogger(JDBCStandardDataProxy.class);
	
	
	public JDBCStandardDataProxy() {
		
	}
			
	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public JDBCStandardDataProxy(IDataSource dataSource, String statement) {
		setDataSource(dataSource);
		setStatement(statement);
	}
	
	public JDBCStandardDataProxy(IDataSource dataSource) {
		setDataSource(dataSource);
		setStatement(statement);
	}
	
	public IDataStore load(String statement, IDataReader dataReader) throws EMFUserError {
		if(statement != null) {
			setStatement(statement);
		}
		return load(dataReader);
	}
	
	public IDataStore load(IDataReader dataReader) throws EMFUserError {
		
		IDataStore dataStore;
		Connection connection;
		Statement stmt;
		ResultSet rs;
		
		logger.debug("IN");
		
		connection = null;
		try {
			connection = getDataSource().getConnection( getSchema() );
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rs = null;
        try {
			rs = stmt.executeQuery( getStatement() );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dataStore = null;
		try {
			dataStore = dataReader.read( rs );
		} catch (EMFInternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dataStore;
	}

	/*
	public IDataStore load(IDataReader dataReader) throws EMFUserError {
		
		IDataStore dataStore = null;
		Object result = null;
		logger.debug("IN");
		
		DataConnection dataConnection = null;
		SQLCommand sqlCommand = null;
		DataResult dataResult = null;
		try {
			Connection conn = dataSource.toSpagoBiDataSource().readConnection(schema); 
			dataConnection = getDataConnection(conn);
			sqlCommand = dataConnection.createSelectCommand( statement );
			dataResult = sqlCommand.execute();
			if(dataResult != null){
				result = (ScrollableDataResult) dataResult.getDataObject();				
			}
			dataStore = dataReader.read( result );
		} catch(Exception e){
			logger.error("Error in query Execution",e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 9221);
		} finally {
			Utils.releaseResources(dataConnection, sqlCommand, dataResult);
			logger.debug("OUT");
		}
		
		return dataStore;
	}
	*/
	
	

	public IDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(IDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
}
