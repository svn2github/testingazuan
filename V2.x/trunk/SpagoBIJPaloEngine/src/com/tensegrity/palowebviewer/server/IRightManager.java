package com.tensegrity.palowebviewer.server;

public interface IRightManager {
	/**
	 * This method return connection pool for particular user. Returned pool may contain less 
	 * available connections then initial pool
	 * @param initialPool - system-wide connection pool
	 * @param user - user, for wich restrictions are applyed 
	 * @return pool of connections, available for user
	 */
	public IConnectionPool getPool(IConnectionPool initialPool, IUser user);
}
