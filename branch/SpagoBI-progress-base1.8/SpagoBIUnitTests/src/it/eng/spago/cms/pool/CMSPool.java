package it.eng.spago.cms.pool;

import it.eng.spago.cms.exec.CMSConnection;
import it.eng.spago.error.EMFInternalError;

/**
 * 
 * Interface that define methods for repositories 
 * connection pool
 */
public interface CMSPool {

	/**
	 * Get a connection to the repository
	 * 
	 * @return Connection to the repository
	 * @throws EMFInternalError
	 */
	public CMSConnection getConnection() throws EMFInternalError;
	
	/**
	 * Close a connection of the repository
	 * 
	 * @param con, connection to close 
	 * @throws EMFInternalError
	 */
	public void closeConnection( CMSConnection con ) throws EMFInternalError;
	
	/**
	 * Make the repository instance shutdown
	 * 
	 * @throws EMFInternalError
	 */
	public void shutdown() throws EMFInternalError;
	
}
