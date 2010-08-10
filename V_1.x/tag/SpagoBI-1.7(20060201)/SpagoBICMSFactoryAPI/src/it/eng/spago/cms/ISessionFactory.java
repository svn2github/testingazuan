package it.eng.spago.cms;

import it.eng.javax.jcr.Session;

/**
 *
 * Define a method for get a session from a jsr 170 repository
 * 
 */
public interface ISessionFactory {
    
	/**
	 * Create and return a JSR Session
	 * 
	 * @param user, user for create the session
	 * @param password, password for create the session
	 * @param workspace, name of the workspace to connect
	 * @return JSR Session
	 */
	public Session getSession(String user, String password, String workspace);
    
}
