package com.tensegrity.palowebviewer.server;

public class DefaultRightManager implements IRightManager {

	public IConnectionPool getPool(IConnectionPool initialPool, IUser user) {
		return initialPool;
	}

}
