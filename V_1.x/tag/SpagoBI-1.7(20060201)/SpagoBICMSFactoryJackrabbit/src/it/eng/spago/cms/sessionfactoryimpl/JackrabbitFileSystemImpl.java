package it.eng.spago.cms.sessionfactoryimpl;

import it.eng.javax.jcr.LoginException;
import it.eng.javax.jcr.NoSuchWorkspaceException;
import it.eng.javax.jcr.Repository;
import it.eng.javax.jcr.RepositoryException;
import it.eng.javax.jcr.Session;
import it.eng.javax.jcr.SimpleCredentials;
import it.eng.spago.cms.ISessionFactory;
import it.eng.spago.tracing.TracerSingleton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.xml.sax.InputSource;

/**
 * 
 * Implements logic for creation of a Jackrabbit repository 
 * and for the creation of JSR Session objects.
 * 
 */
public class JackrabbitFileSystemImpl implements ISessionFactory {

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
	public JackrabbitFileSystemImpl() {
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
	    // get pathj repository
	    String pathRepository = (String)properties.getProperty("repository_path");
	    // control if path repository exist and it's possible to write
	    File pathRepFile = new File(pathRepository);
		if(!pathRepFile.exists()) {
			TracerSingleton.log(NAME_MODULE,
								TracerSingleton.CRITICAL,
								"JackRabbitContentRepository::init: the absolute path "+pathRepository+
								" doesn't exists");
        }
		if(!pathRepFile.canWrite()) {
			TracerSingleton.log(NAME_MODULE, 
								TracerSingleton.CRITICAL,
            					"JackRabbitContentRepository::init: write permission denied");
		}
		// get the name of the jackrabbit configuration file
		String nameConfFile = (String)properties.getProperty("name_configuration_file");
		// get the input stream of the configuration file
		InputStream configFileIS = null;
		try{
			configFileIS = this.getClass().getClassLoader().getResourceAsStream(nameConfFile);
		} catch (Exception e) {
			TracerSingleton.log(NAME_MODULE,
								TracerSingleton.CRITICAL,
								"JackRabbitContentRepository::init: jackrabbit configuration file " +
								"reading error", e);
		}
	    InputSource repFileConf = new InputSource(configFileIS);
		try{
	    	RepositoryConfig repConf = RepositoryConfig.create(repFileConf, pathRepository);
	    	_repo = RepositoryImpl.create(repConf);
	    } catch (Exception e) {
	    	TracerSingleton.log(NAME_MODULE,
					TracerSingleton.CRITICAL,
					"JackRabbitContentRepository::init: jackrabbit repository creation error", e);
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
