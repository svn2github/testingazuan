package com.tensegrity.palowebviewer.server;

import org.palo.api.Connection;

public interface IConnectionPool {
	
	public IConnectionFactory getConnectionFactory();
	
	public void setConnectionFactrory(IConnectionFactory factory);
	
	/**
	 * @return number servers
	 */
	public int getServerCount();
	
	/**
	 * 
	 * @return names of avaible servers
	 */
	public String[] getServerNames();
	
	/**
	 * 
	 * @return ports of available server
	 */
	public String[] getServerServices();
	
	/**
	 * 
	 * @return provider names of available server
	 */
	public String[] getServerProviders();
	
	
	/**
	 * @param host - server name
	 * @return open connection to appropriate server
	 */
	public Connection getConnection(String server, String service);
	
	public Connection getConnection(String serverId);
	
	
	/**
	 * 
	 * marks connections as NEED_RELOAD. Prior getting connection from pool it will be reloaded.
	 *
	 */
	public void markForceReload();
	
}
