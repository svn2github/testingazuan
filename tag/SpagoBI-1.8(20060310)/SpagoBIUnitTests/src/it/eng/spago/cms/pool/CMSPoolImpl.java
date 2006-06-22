package it.eng.spago.cms.pool;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.IRepositoryFactory;
import it.eng.spago.cms.exec.CMSConnection;
import it.eng.spago.cms.util.Utils;
import it.eng.spago.error.EMFInternalError;

import org.apache.commons.pool.impl.GenericObjectPool;


/**
 * 
 * Implements methos for create connection pool, get a 
 * connection anc close it 
 * 
 */
public class CMSPoolImpl implements CMSPool {

	/**
	 * Factory of object contains in the pool (See 
	 * jackarta common pool)
	 */ 
	ConnectionCMSPoolableObjectFactory factoryCon = null;
	/**
	 * Pool configuration (See jackarta common pool)
	 */
	GenericObjectPool.Config confPool = null;
	/**
	 * Connection pool (See jackarta common pool)
	 */
	GenericObjectPool pool = null;
	/**
	 * Repository instance
	 */
	private IRepositoryFactory _repo = null;
	
	/**
	 * Create the connection pool an the initial connection
	 * istance
	 * 
	 * @param repo, repository 
	 * @param confPoolSB, configuration of th pool
	 * @param name, name of the repository
	 * @param user user for open jcr session
	 * @param pass, password for open jcr session
	 * @param work, workspace for open jcr session
	 * @param pSys, application system namespace prefix
	 * @param pUsr, application user namespace prefix 
	 * @throws EMFInternalError
	 */
	public CMSPoolImpl(IRepositoryFactory repo, SourceBean confPoolSB, String name, 
			                         String user, String pass, String work, 
									 String pSys, String pUsr) throws EMFInternalError {
		_repo = repo;
		Utils.debug("CMSPoolImpl::CMSPoolImpl:start constructor\n");
		factoryCon = new ConnectionCMSPoolableObjectFactory(repo, name, user, pass, work, pSys, pUsr);
		confPool = new GenericObjectPool.Config();
		String maxActiveStr = (String)confPoolSB.getAttribute("maxActive");
		Utils.debug("CMSPoolImpl::CMSPoolImpl:using maxActive: "+maxActiveStr+"\n");
		String maxIdleStr = (String)confPoolSB.getAttribute("maxIdle");
		Utils.debug("CMSPoolImpl::CMSPoolImpl:using maxIdle: "+maxIdleStr+"\n");
		String minIdleStr = (String)confPoolSB.getAttribute("minIdle");
		Utils.debug("CMSPoolImpl::CMSPoolImpl:using minIdle: "+minIdleStr+"\n");
		String initialConStr = (String)confPoolSB.getAttribute("initialConnection");
		Utils.debug("CMSPoolImpl::CMSPoolImpl:using initialConnection: "+initialConStr+"\n");
		String waitStr = (String)confPoolSB.getAttribute("wait");
		Utils.debug("CMSPoolImpl::CMSPoolImpl:using wait: "+waitStr+"\n");
		int maxActive =  Integer.parseInt(  (String)confPoolSB.getAttribute("maxActive")  );
		int maxIdle =  Integer.parseInt(  (String)confPoolSB.getAttribute("maxIdle") );
		int minIdle = Integer.parseInt(  (String)confPoolSB.getAttribute("minIdle")  );
		int initial =  Integer.parseInt(  (String)confPoolSB.getAttribute("initialConnection")   );
		long wait = Long.parseLong(  (String)confPoolSB.getAttribute("wait")   );
		confPool.maxActive = maxActive;
		confPool.maxIdle = maxIdle;
		confPool.minIdle = minIdle;
		confPool.maxWait = wait;
		confPool.testOnBorrow = false;
		confPool.testOnReturn = false;
		confPool.testWhileIdle = false;
		confPool.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
		pool = new GenericObjectPool(factoryCon, confPool);
		Utils.debug("CMSPoolImpl::CMSPoolImpl:connection pool created: "+pool+" \n");
		try {
			for(int i=0; i<initial; i++){
			    pool.addObject();
			    Utils.debug("CMSPoolImpl::CMSPoolImpl:add connection "+i+" to connection pool \n");
		    }
		}
		catch (Exception e) {
			Utils.critical("CMSPoolImpl::CMSPoolImpl:error during the creation of the  " +
	                       "CMSPool \n" + e + " \n");
		}
		Utils.debug("CMSPoolImpl::CMSPoolImpl:end constructor\n");
	}
	
	
	/**
	 * Get a connection from the pool
	 * 
	 * @return Connection to the repository
	 */
	public CMSConnection getConnection() throws EMFInternalError {
		CMSConnection conn = null;
		try{
			conn = (CMSConnection)pool.borrowObject();
			conn.setClose(false);
		}
		catch(Exception e) {
		   conn = null; 
		}
		return conn;
	}
	
   
	/**
	 * Close a connecion of the repository
	 * 
	 * @param connection to close 
	 */
    public void closeConnection(CMSConnection con) throws EMFInternalError {
    	try{
			pool.returnObject(con); 
		}
		catch(Exception e) {
			Utils.major("CMSPoolImpl::closeConnection:cannot close connection \n" + e + " \n");
		}
    }

	/**
	 * Makes the repository shutdown
	 */
	public void shutdown() {
		if (_repo != null) _repo.shutdown();
	}
	
	
	
}
