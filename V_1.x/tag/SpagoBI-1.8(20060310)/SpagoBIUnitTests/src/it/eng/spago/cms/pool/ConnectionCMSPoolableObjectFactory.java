package it.eng.spago.cms.pool;

import javax.jcr.Session;
import it.eng.spago.cms.IRepositoryFactory;
import it.eng.spago.cms.exec.CMSConnection;
import it.eng.spago.cms.util.Utils;
import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * Implements method of jackarta commons pool for create 
 * the objects of the pool ( see documentation of jackarta
 * commons pool ) 
 *
 */
public class ConnectionCMSPoolableObjectFactory extends  BasePoolableObjectFactory{
	
	IRepositoryFactory _repo = null;
	String nameRepositoryPool = null;
	String defaultUser = null;
	String defaultPassword = null;
	String defaultWorkspace = null;
	String prefSys = null;
	String prefUsr = null;
	
	/**
	 * Constructor of the class
	 * 
	 * @param repo, repository
	 * @param name, name repository
	 * @param user, user for open Session
	 * @param password, password for open session 
	 * @param workSpace, workspace for open session
	 * @param preSys, application system prefix
	 * @param preUsr, application user prefix
	 */
	protected ConnectionCMSPoolableObjectFactory(IRepositoryFactory repo, String name, 
			  String user, String password, String workSpace, String preSys, String preUsr ) {
		super();
		_repo = repo;
		nameRepositoryPool = name;
		defaultUser = user;
		defaultPassword = password;
		defaultWorkspace = workSpace;
		prefSys = preSys;
		prefUsr = preUsr;
	}
	
	/**
	 * Create a new object of the pool (see documentation of jackarta
     * commons pool )
     * 
     * @return new Object for the pool
	 */
	public Object makeObject() {
		Object obj = null;
		try {
			Session sess = Utils.getSession(_repo, defaultUser, defaultPassword, defaultWorkspace);
			CMSConnection conn = new CMSConnection(sess, nameRepositoryPool, prefSys, prefUsr);
			obj = (Object)conn;
		}
		catch (Exception e) {
			obj = null;
		}
		return obj;
	}
}
