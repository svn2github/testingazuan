package it.eng.spago.cms;

import it.eng.spago.tracing.TracerSingleton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.jcr.Repository;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.xml.sax.InputSource;

/**
 * Implements logic for creation of a Jackrabbit repository 
 */
public class JackrabbitRepositoryFactoryImpl implements IRepositoryFactory {

	
	private static final String NAME_MODULE = "SPAGOCMS";
	
	/**
	 * JSR Repository
	 */
	protected RepositoryImpl _repo = null;

	
	/**
	 * Construct of the class:  build the Jackrabbit repository using the parameter 
	 * values contained into a properties file
	 */
	public JackrabbitRepositoryFactoryImpl() {
		// get properties
		Properties properties = new Properties();
	    try {
	    	InputStream is = this.getClass().getClassLoader().getResourceAsStream("jackrabbitSessionFactory.properties");
	    	properties.load(is);
	    } catch (IOException e) {
	    	TracerSingleton.log(NAME_MODULE,
								TracerSingleton.CRITICAL,
								"JackrabbitRepositoryFactoryImpl::init: properties file " +
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
	    // crete repository
	    try{
	    	RepositoryConfig repConf = RepositoryConfig.create(repFileConf, pathRepository);
	    	_repo = RepositoryImpl.create(repConf);
	    } catch (Exception e) {
	    	TracerSingleton.log(NAME_MODULE,
					TracerSingleton.CRITICAL,
					"JackRabbitContentRepository::init: jackrabbit repository creation error", e);
	    }
	}
	

	public Repository getRepository() {
		return _repo;
	}
	
	/**
	 * Makes the repository shutdown
	 */
	public void shutdown () {
		if (_repo != null) _repo.shutdown();
	}
	
}






/*
public void init(SourceBean config) throws Exception {
	try
	  {
		// absolute path web app
	    String pathAppl = ConfigSingleton.getRootPath();
		
	    
	    // path repostiory
		String pathRepository = (String) config.getAttribute("REPOSITORYPATH.path");
		// tyep path
		String typePath = (String) config.getAttribute("REPOSITORYPATH.type");
		if(  (pathRepository==null)  || (typePath==null)  ) {
			TracerSingleton.log( Constants.NOME_MODULO, TracerSingleton.CRITICAL,
            "JackRabbitContentRepository::init: missing configuration params");
            throw new Exception("JackRabbitContentRepository::init: missing configuration params");
		}
		
		
		// if path is relative to the web application 
		// build absolute path
		// if path is absloute verify existence otherwise throws exception
		if(typePath.equalsIgnoreCase("relative")) {
			pathRepository = pathAppl + "/" + pathRepository;
		}
		else {
			File exist = new File(pathRepository);
			if(!exist.exists()) {
				TracerSingleton.log( Constants.NOME_MODULO, TracerSingleton.CRITICAL,
                "JackRabbitContentRepository::init: the absolute path "+pathRepository+" doesn't exists");
                throw new Exception("JackRabbitContentRepository::init: the absolute path "+pathRepository+" doesn't exists");
            }
			if(!exist.canWrite()) {
				TracerSingleton.log( Constants.NOME_MODULO, TracerSingleton.CRITICAL,
                "JackRabbitContentRepository::init: write permission denied");
                throw new Exception("JackRabbitContentRepository::init: write permission denied");
			}
		}
		
		
		// path of the configuration path 
	    String pathFileConf = (String) config.getAttribute("CONFIGURATIONFILE.path");
        // type path
		String typePathFileConf = (String) config.getAttribute("CONFIGURATIONFILE.type");
		if(  (pathFileConf==null)  || (typePathFileConf==null)  ) {
			TracerSingleton.log( Constants.NOME_MODULO, TracerSingleton.CRITICAL,
            "JackRabbitContentRepository::init: missing configuration params");
            throw new Exception("JackRabbitContentRepository::init: missing configuration params");
		}
		
		
        // if path is relative to web application build absolute path
		if(typePathFileConf.equalsIgnoreCase("relative")) {
			pathFileConf = pathAppl + "/" + pathFileConf;
		}
		// control if file exists
		File existFileConf = new File(pathFileConf);
		if(!existFileConf.exists()) {
			TracerSingleton.log( Constants.NOME_MODULO, TracerSingleton.CRITICAL,
			"JackRabbitContentRepository::init: il file di configurazione non esiste");
			throw new Exception("JackRabbitContentRepository::init: file di configurazione "+pathFileConf+" non trovato");
		}
		if(!existFileConf.canRead()) {
			TracerSingleton.log( Constants.NOME_MODULO, TracerSingleton.CRITICAL,
            "JackRabbitContentRepository::init: il file di configurazione non pu� essere letto");
            throw new Exception("JackRabbitContentRepository::init: il file di configurazione "+pathFileConf+" non pu� essere letto");
		}
		
					
		
		
		// get inputstream of configuration file
	    File configFile = new File(pathFileConf);
	    FileInputStream configFileIS = new FileInputStream(configFile);
	    InputSource repFileConf = new InputSource(configFileIS);
	    // crete repository
        //_repConf = RepositoryConfig.create(repFileConf, pathRepository);
        //_repo = RepositoryImpl.create(_repConf);
        
    
        
	}
	catch(Exception repE) { 
		TracerSingleton.log( Constants.NOME_MODULO, TracerSingleton.CRITICAL,
        "JackRabbitContentRepository::init: error during repository creation" + repE);
		throw repE;
	}
}
	
*/
