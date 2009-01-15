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

import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.mappers.SQLMapper;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.tools.dataset.common.datareader.JDBCDataReader;
import it.eng.spagobi.tools.datasource.bo.IDataSource;

import java.sql.Connection;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JDBCDataProxy implements IDataProxy {
	
	IDataSource dataSource;
	String statement;
	
	private static transient Logger logger = Logger.getLogger(JDBCDataProxy.class);
	
	
	public JDBCDataProxy() {
		
	}
			
	public JDBCDataProxy(IDataSource dataSource, String statement) {
		setDataSource(dataSource);
		setStatement(statement);
	}
	
	public JDBCDataProxy(IDataSource dataSource) {
		setDataSource(dataSource);
		setStatement(statement);
	}
	
	public Object load(String statement) throws EMFUserError {
		if(statement != null) {
			setStatement(statement);
		}
		return load();
	}
	
	public Object load() throws EMFUserError {
		
		Object result = null;

		logger.debug("IN");
		
		DataConnection dataConnection = null;
		SQLCommand sqlCommand = null;
		DataResult dataResult = null;
		try {
			Connection conn = dataSource.toSpagoBiDataSource().readConnection(); 
			dataConnection = getDataConnection(conn);
	
			sqlCommand = dataConnection.createSelectCommand( statement );
			dataResult = sqlCommand.execute();
			if(dataResult != null){
				result = (ScrollableDataResult) dataResult.getDataObject();				
			}
		} catch(Exception e){
			logger.error("Error in query Execution",e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "Error in query Execution: check your query and Datasource");
		} finally {
			//Utils.releaseResources(dataConnection, sqlCommand, dataResult);
			logger.debug("OUT");
		}
		
		return result;
	}
	
	private DataConnection getDataConnection(Connection con) throws EMFInternalError {
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
