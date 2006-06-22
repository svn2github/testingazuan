package it.eng.spago.cms.exec;

import javax.jcr.Session;

import it.eng.spago.cms.init.CMSManager;


/**
 * 
 * Class of the connection to the repository
 * 
 */
public class CMSConnection {
    
	/**
	 * Spago CMS namespace prefix for system properties
	 */
	private String prefixPropertySystem;
	/**
	 * Spago CMS namespace prefix for user properties
	 */
	private String prefixPropertyUser;
	/**
	 * JCR Session
	 */
	private Session session;
	/**
	 * flag for close / open state
	 */
	private boolean close = false;
	/**
	 * Name of the repository connect
	 */
	private String nameRepositoryPool = null;
	

	/**
	 * Constructor of the connection
	 * 
	 * @param sess, jcr session
	 * @param nameRep, name of the repository connect
	 * @param preSys, prefix for system properties
	 * @param preUsr, prefix for user properties
	 */
	public CMSConnection(Session sess, String nameRep, String preSys, String preUsr) {
		prefixPropertySystem = preSys;
		prefixPropertyUser = preUsr;
		nameRepositoryPool = nameRep;
		session = sess;
	}
		
	/**
	 * close the connection
	 *
	 */
	public void close() {
	    try {
	    	CMSManager.getInstance(nameRepositoryPool).closeConnection(this);
		    close = true;
	    }
	    catch (Exception e) {
	    	
	    }
	}
	
	/**
	 * Verify if the connection is close
	 * 
	 * @return boolean, true if connection is close false otherwise
	 */
	public boolean isClose() {
		return close;
	}
	/**
	 * Get the jcr session
	 * @return JCR Session
	 */
	public Session getSession() {
		return session;
	}
	/**
	 * Get the name of the repository connect
	 * @return Name of the repository connect
	 */
	public String getNameRepositoryPool() {
		return nameRepositoryPool;
	}
	/**
	 * Get the prefix used for system propertyes
	 * @return Spago CMS system properties prefix
	 */
	public String getPrefixPropertySystem() {
		return prefixPropertySystem;
	}
	/**
	 * Get the prefix used for user properties
	 * @return Spago CMS user properties prefix
	 */
	public String getPrefixPropertyUser() {
		return prefixPropertyUser;
	}
	/**
	 * Set the close / open state of the connection
	 * @param close state to set
	 */
	public void setClose(boolean close) {
		this.close = close;
	}
}
