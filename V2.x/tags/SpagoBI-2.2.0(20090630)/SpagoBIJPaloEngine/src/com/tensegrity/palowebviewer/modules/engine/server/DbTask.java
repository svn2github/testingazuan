/**
 * 
 */
package com.tensegrity.palowebviewer.modules.engine.server;

import org.palo.api.Connection;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.server.IConnectionPool;
import com.tensegrity.palowebviewer.server.paloaccessor.PaloAccessor;

abstract class DbTask{
	
	private Connection conn;
	private IConnectionPool connectioPool;
	private DbTask parent;
	private PaloAccessor accessor;

	
	public DbTask(){

	}
	
	public Connection getConnection() {
		if(conn == null){
			conn = openConnection(getServer());
		}
		return conn;
	}
	
	private Connection openConnection(String serverId){
		IConnectionPool pool = getConnectioPool();
		Connection connection = pool.getConnection(serverId);
		return connection;
	}
	
	public void setConnection(Connection value) {
		this.conn = value;
	}
	
	protected abstract String getServer();
	
	protected abstract void task() throws InvalidObjectPathException;
	
	protected void initTask() {
	}
	
	protected void finalizeTask() {
		if(conn != null)
			conn.disconnect();			
	}
	
	protected void setParent(DbTask task) {
		parent = task;
	}
	
	protected DbTask getParent() {
		return parent;
	}
	
	public void execute() throws InvalidObjectPathException{
		try{
			initTask();
			task();
		}
		finally{
			finalizeTask();
		}
	}

	public IConnectionPool getConnectioPool() {
		IConnectionPool result = this.connectioPool;
		if(result == null) {
			result = getParentConnectionPool(result);
		}
		return result;
	}

	private IConnectionPool getParentConnectionPool(IConnectionPool result) {
		DbTask parent = getParent();
		if(parent != null)
			result = parent.getConnectioPool();
		return result;
	}

	public void setConnectioPool(IConnectionPool connectioPool) {
		this.connectioPool = connectioPool;
	}

	public PaloAccessor getAccessor() {
		if (accessor == null) {
			accessor = new PaloAccessor();
			
		}

		return accessor;
	}
	
	
}