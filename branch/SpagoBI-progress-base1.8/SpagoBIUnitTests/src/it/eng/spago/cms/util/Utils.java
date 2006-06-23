package it.eng.spago.cms.util;

import it.eng.spago.cms.IRepositoryFactory;
import it.eng.spago.cms.constants.Constants;
import it.eng.spago.tracing.TracerSingleton;

import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

public class Utils {

	/**
	 * Create and return a JCR Session object from the repository
	 * 
	 * @param user, userID for connection to the repository
	 * @param password, password for connection to the repository
	 * @param workspace, name of the workspace to connect
	 * 
	 */
	public static Session getSession(IRepositoryFactory repFactory, String user, String password, String workspace) {
		SimpleCredentials credentials = null;
		Session session = null;
		try {
			Repository _repo = repFactory.getRepository();
			char [] passChar = null;
			if(password != null) {
				passChar = password.toCharArray();
			}
			credentials = new SimpleCredentials(user, passChar);
			session = _repo.login(credentials, workspace);
		} catch (LoginException le) {
			TracerSingleton.log("SPAGOCMS",
								TracerSingleton.CRITICAL,
								"Utils::getSession:login error", le);
		} catch (NoSuchWorkspaceException we) {
			TracerSingleton.log("SPAGOCMS",
								TracerSingleton.CRITICAL,
								"Utils::getSession:workspace not correct", we);
		} catch (RepositoryException re){
			TracerSingleton.log("SPAGOCMS",
								TracerSingleton.CRITICAL,
                  				"Utils::getSession:repository error", re);  
		} catch (Exception e){
			TracerSingleton.log("SPAGOCMS",
					TracerSingleton.CRITICAL,
      				"Utils::getSession: error while creating session", e);  
		}
		return session;
	}
	
	
	/**
	 * Log a debug message
	 * @param message Message to log
	 */
	public static void debug(String message) {
		TracerSingleton.log(Constants.NAME_MODULE, 
							TracerSingleton.DEBUG, 
						    message);
	}
	
	/**
	 * Log a major message
	 * @param message Message to log
	 */
	public static void major(String message) {
		TracerSingleton.log(Constants.NAME_MODULE, 
				            TracerSingleton.MAJOR, 
							message);
	}
	
	
	/**
	 * Log a critical message
	 * @param message Message to log
	 */
	public static void critical(String message) {
		TracerSingleton.log(Constants.NAME_MODULE, 
				            TracerSingleton.CRITICAL, 
							message);
	}
	
	
	/**
	 * Log a warning message
	 * @param message Message to log
	 */
	public static void warning(String message) {
		TracerSingleton.log(Constants.NAME_MODULE, 
				            TracerSingleton.WARNING, 
							message);
	}
	
}
