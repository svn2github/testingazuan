package com.tensegrity.palowebviewer.server;

import org.palo.api.Connection;

public interface IConnectionFactory {
	
	/**
	 *
	 * creates connection to palo server
	 */
	public Connection createConnection(String host,String service, String login, String password, String provider);
	
	public void initialize(PaloConfiguration cfg);
	
}
