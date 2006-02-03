package it.eng.spago.cms.sessionfactoryimpl;

import it.eng.javax.jcr.LoginException;
import it.eng.javax.jcr.NoSuchWorkspaceException;
import it.eng.javax.jcr.Repository;
import it.eng.javax.jcr.RepositoryException;
import it.eng.javax.jcr.Session;
import it.eng.javax.jcr.SimpleCredentials;
import it.eng.spago.cms.ISessionFactory;
import it.eng.spago.tracing.TracerSingleton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * 
 * Implements logic for creation of a Jackrabbit repository 
 * and for the creation of JSR Session objects.
 * 
 */
public class JackrabbitJNDIImpl implements ISessionFactory {

	
	private static final String NAME_MODULE = "SPAGOCMS";
	
	/**
	 * JSR Repository
	 */
	protected Repository _repo = null;
	/**
	 * JSR Repository config
	 */
	
	/**
	 * Construct of the class:  build the Jackrabbit repository using the parameter 
	 * values contained into a properties file
	 */
	public JackrabbitJNDIImpl() {
		// get properties
		Properties properties = new Properties();
	    try {
	    	InputStream is = this.getClass().getClassLoader().getResourceAsStream("jackrabbitSessionFactory.properties");
	    	properties.load(is);
	    } catch (IOException e) {
	    	TracerSingleton.log(NAME_MODULE,
								TracerSingleton.CRITICAL,
								"JackrabbitSessionFactoryImpl::init: properties file " +
								"jackrabbitSessionFactory.properties not found", e);
	    	return;
	    }
	    // get name jndi context
	    String rootName = (String)properties.getProperty("jndi_root_name");
	    String objName = (String)properties.getProperty("jndi_obj_name");
        // try to get the repository from jndi
		try{
			InitialContext initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			Object obj = envCtx.lookup("cms/spagobicms");
	    	_repo = (Repository)obj;
	    } catch (Exception e) {
	    	TracerSingleton.log(NAME_MODULE,
					TracerSingleton.CRITICAL,
					"JackRabbitContentRepository::init: jackrabbit repository jndi lookup error", e);
	    }
	}
	
	
	/**
	 * Create and return a JCR Session object ot the repository
	 * 
	 * @param user, userID for connection to the repository
	 * @param password, password for connection to the repository
	 * @param workspace, name of the workspace to connect
	 * 
	 */
	public Session getSession(String user, String password, String workspace) {
		SimpleCredentials credentials = null;
		Session session = null;
		try {
			char [] passChar = null;
			if(password != null) {
				passChar = password.toCharArray();
			}
			credentials = new SimpleCredentials(user, passChar);
			session = _repo.login(credentials, workspace);
		} catch (LoginException le) {
			TracerSingleton.log("SPAGOCMS",
								TracerSingleton.CRITICAL,
								"JackrabbitSessionFactoryImpl::createConnection:login error", le);
		} catch (NoSuchWorkspaceException we) {
			TracerSingleton.log("SPAGOCMS",
								TracerSingleton.CRITICAL,
								"JackrabbitSessionFactoryImpl::createConnection:workspace not correct", we);
		} catch (RepositoryException re){
			TracerSingleton.log("SPAGOCMS",
								TracerSingleton.CRITICAL,
                  				"JackrabbitSessionFactoryImpl::createConnection:repository error", re);  
		}
		return session;
	}
	

}

