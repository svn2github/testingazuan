package it.eng.spago.cms.init;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.IRepositoryFactory;
import it.eng.spago.cms.operations.SetOperation;
import it.eng.spago.cms.pool.CMSPool;
import it.eng.spago.cms.pool.CMSPoolImpl;
import it.eng.spago.cms.util.Path;
import it.eng.spago.cms.util.Utils;
import it.eng.spago.cms.util.constants.OperationsConstants;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.init.InitializerIFace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Session;


/**
 * 
 * This class initialize the Content Management System 
 * using some configuration parameters.
 * The class is called from Spago Framework during the 
 * Application Start-Up.
 * Create the repository and all objects necessary for using it   
 * 
 */
public class CMSInitializer implements InitializerIFace {
	
	/** 
	 * SourceBean that contains the configuration parameters
	 */
	private SourceBean _config = null;

	
	/**
	 * Create and initialize all the repositories defined
	 * in the configuration SourceBean, the method is called automatically
	 * from Spago Framework at application start up 
	 * if the Spago initializers.xml file is configured 
	 * @param config, SourceBean containing parameters configuration
	 * for each repository to create
	 */
	public void init(SourceBean config) {
    	Utils.debug("CMSInitializer::init: start initialization \n" + config);
    	initialize(config);
    	Utils.debug("CMSInitializer::init: end initialization");
	}
	
	
	/**
	 * Create and initialize all the repositories defined
	 * in the configuration SourceBean
	 */
	public void setUp() {
		String cmsSBname = "CONTENTCONFIGURATION";
		Utils.debug("CMSInitializer::setUp: search for " + cmsSBname + " parameter in Spago configuration ");
		ConfigSingleton config = ConfigSingleton.getInstance();
		SourceBean cmsConfSB = (SourceBean)config.getAttribute(cmsSBname);
		Utils.debug("CMSInitializer::setUp: Cms configuration parameter: " + cmsConfSB);
		Utils.debug("CMSInitializer::setUp: start initialization");
    	initialize(cmsConfSB);
    	Utils.debug("CMSInitializer::setUp: end initialization");
	}
	
	
	
	/**
	 * Create and initialize all the repositories defined
	 * in the configuration SourceBean
	 * @param config, SourceBean containing parameters configuration
	 * for each repository to create
	 */
    private void initialize(SourceBean config) {
    	Utils.debug("CMSInitializer::initialize:start method");
        _config = config;
        if(config == null) {
        	Utils.critical("CMSInitializer::initialize:configuration sourceBean null \n");
            Utils.warning("CMSInitializer::initialize:each time a new connection will be required the" +
            		      " system will try to initialize again the repository\n");
            return;
        }
        // get List of sourceBean configuration descriptor for all repository 
        List repositories = config.getAttributeAsList("CONTENTREPOSITORY");
        Utils.debug("CMSInitializer::initialize:list repositories" + repositories);
        // check repositories configuration
        try {
        	checkRepsConf(config, repositories);
        } catch (Exception e) {
        	Utils.critical("CMSInitializer::initialize:repositories initialization failed " + e + " \n");
        	return;
        }
        // for each repository configured   
        List repositoriesRegistered = new ArrayList();
        for (int i = 0; i < repositories.size(); i++) {
            //try {
            	IRepositoryFactory repositoryFactory = null;
            	Session session = null;
            	CMSPool pool  = null;   
            	SourceBean repositoryBean = (SourceBean)repositories.get(i);
        		String repositoryName = (String)repositoryBean.getAttribute("NAME");
        		if(CMSManager.isRegistered(repositoryName)){
        			Utils.warning("CMSInitializer::initialize:repository " + repositoryName + " already registered \n");
        			continue;
        		}
        		Utils.debug("CMSInitializer::initialize:start initialization for repository " + repositoryName);
        		Utils.debug("CMSInitializer::initialize:start creation of the repository factory");
        		try{
        			repositoryFactory = createRepositoryFactory(repositoryName, repositoryBean, 
        														repositoriesRegistered);
        		} catch (Exception e) {
        			Utils.critical("CMSInitializer::initialize:error during the creation " +
        					       "of the IRepositoryFactory implementation for " + repositoryName +
        					       "\n" + e + " \n");
                	continue;
        		}
        		Utils.debug("CMSInitializer::initialize:repository factory created: " + repositoryFactory + "\n");
        		Object userForConnObj = repositoryBean.getAttribute("CONNECTIONDATA.user");
        		Utils.debug("CMSInitializer::initialize:using user " + userForConnObj + " \n");
        		Object passwordForConObj = repositoryBean.getAttribute("CONNECTIONDATA.password");
        		Utils.debug("CMSInitializer::initialize:using password " + passwordForConObj + " \n");
        		Object workspaceForConnObj = (String)repositoryBean.getAttribute("CONNECTIONDATA.workspace");
        		Utils.debug("CMSInitializer::initialize:using workspace " + passwordForConObj + " \n");
        		String userForConn = userForConnObj.toString();
        		String passwordForConn = passwordForConObj.toString();
        		String workspaceForConn = workspaceForConnObj.toString();
                session = Utils.getSession(repositoryFactory, userForConn, passwordForConn, workspaceForConn);
                if(session==null) {
                	Utils.critical("CMSInitializer::initialize:repository session null: " +
 					               "cannot register namespaces \n");
                	continue;
                }
                Utils.debug("CMSInitializer::initialize:session for namespace registration obtained \n");
                Object sysPrefixObj = repositoryBean.getAttribute("NAMESPACES.SYSTEMNAMESPACE.prefix");
                Utils.debug("CMSInitializer::initialize:using system namespace prefix " + sysPrefixObj + " \n");
                Object sysUriObj = repositoryBean.getAttribute("NAMESPACES.SYSTEMNAMESPACE.uri");
                Utils.debug("CMSInitializer::initialize:using system namespace uri " + sysUriObj + " \n");
                Object userPrefixObj = repositoryBean.getAttribute("NAMESPACES.USERNAMESPACE.prefix");
                Utils.debug("CMSInitializer::initialize:using user namespace prefix " + userPrefixObj + " \n");
                Object userUriObj = repositoryBean.getAttribute("NAMESPACES.USERNAMESPACE.uri");
                Utils.debug("CMSInitializer::initialize:using user namespace uri " + userUriObj + " \n");
        		String sysPrefix = sysPrefixObj.toString();
        		String userPrefix = userPrefixObj.toString();
        		String sysUri = sysUriObj.toString();
        		String userUri = userUriObj.toString();
        		Utils.debug("CMSInitializer::initialize:start namespaces registration \n");
            	try{
            		registerNamespaces(repositoryBean, session, sysPrefix, userPrefix, sysUri, userUri);
            	} catch (Exception e) {
            		Utils.critical("CMSInitializer::initialize:error during the namespaces " +
 					               "registration for " + repositoryName +
 					               " repository \n" + e + " \n");
         	        continue;
            	}
            	Utils.debug("CMSInitializer::initialize:end namespaces registration \n");
            	Utils.debug("CMSInitializer::initialize:start creation connection pool \n");
                try{
                	SourceBean configPool = (SourceBean)repositoryBean.getAttribute("POOLCONFIGURATION");
                    pool  = new CMSPoolImpl(repositoryFactory, configPool, repositoryName, 
                     		                 userForConn, passwordForConn, workspaceForConn, 
                     		                 sysPrefix, userPrefix);
                } catch(Exception e) {
                	Utils.critical("CMSInitializer::initialize:cannot create connection pool for " +
                			       " repository " + repositoryName + "\n" + e); 
                	continue;
                }
                Utils.debug("CMSInitializer::initialize:end creation connection pool \n");
                Utils.debug("CMSInitializer::initialize:start registration connection pool \n");
                try {
                	CMSManager.registerRepository(pool, repositoryName);
                	repositoriesRegistered.add(repositoryName);
                }
                catch(Exception e) {
                	Utils.critical("CMSInitializer::initialize:cannot register connection pool for " +
                			       " repository " + repositoryName + "\n" + e); 
                	continue;
                }
                Utils.debug("CMSInitializer::initialize:end registration connection pool \n");
                Utils.debug("CMSInitializer::initialize:start creation initial structure \n");
                session = Utils.getSession(repositoryFactory, userForConn, passwordForConn, workspaceForConn);
                Utils.debug("CMSInitializer::initialize:session for initial structure creation obtained:"+session+" \n");
                try{
                	createInitialStructure(repositoryBean, session, sysPrefix); 
                } catch (Exception e) {
                	Utils.critical("CMSInitializer::initialize:error during the creation of the  " +
				                   " inititial structure for " + repositoryName +
				                   " repository \n" + e + " \n");
                	continue;
                }
                Utils.debug("CMSInitializer::initialize:end creation initial structure \n");
        }// for repositories
        Utils.debug("CMSInitializer::initialize:end method");
    }


    
    
    
    /**
     * return the SourceBean which contains the configuration
     * of the repository.
     */
	public SourceBean getConfig() {
		return _config;
	}

	
	
	
	
	/**
	 * Verify the repositories configuration and register the default repository
	 * @param config Configuration SourceBean of the repositories
	 * @param repositories List of the configured repositories
	 */
	private void checkRepsConf(SourceBean config, List repositories) throws Exception {
		Utils.debug("CMSInitializer::checkRepsConf:start method");
        // control if at least one repository is configured
        if(repositories.size() == 0) {
        	Utils.critical("CMSInitializer::checkRepsConf:cannot find any configured repository \n");
        	throw new Exception("Cannot find any configured repository");
        } 
        // get the default repository name
        String defaultRep = "";
        Object objDefRep = config.getAttribute("DEFAULTREPOSITORY.name");
        if(objDefRep != null) {
        	defaultRep = objDefRep.toString().trim();
        }
        else {
        	Utils.critical("CMSInitializer::checkRepsConf:default repository not found\n");
        	throw new Exception("Default repository not found");
        }
        Utils.debug("CMSInitializer::checkRepsConf:use "+defaultRep+" as default repository\n");
        // control if default repository is one of the configured repositories
        boolean find = false;
        for(int i=0; i<repositories.size(); i++) {
        	Object objRepBean = repositories.get(i);
        	if((objRepBean == null)  ||  !(objRepBean instanceof SourceBean) ) {
        		continue;
        	}
        	SourceBean repositoryBean = (SourceBean)objRepBean;
        	Object objRepName = repositoryBean.getAttribute("NAME");
        	if(objRepName == null) {
        		continue;
        	}
        	String repositoryName = objRepName.toString().trim();
        	if(defaultRep.equals(repositoryName)) {
        		find = true; 
        	}
        }
        if(!find){
        	Utils.critical("CMSInitializer::checkRepsConf: default repository not configured \n");
        	throw new Exception("Default repository not configured");
        }
        // registry the name of default repository
        CMSManager.setDefaultRepository(defaultRep);
        Utils.debug("CMSInitializer::checkRepsConf:default repository \n" + defaultRep);
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Create and return an implementation of the IRepositoryFacotry interface
	 * @param repositoryBean Configuration SourceBean of the repository
	 * @param registeredRepos List of registered repositories
	 * @return Implementation of IRepositoryFactory interface
	 */
	private IRepositoryFactory createRepositoryFactory(String repositoryName,
			                                           SourceBean repositoryBean, 
													   List registeredRepos) throws Exception {
		IRepositoryFactory repFactory = null;
		Utils.debug("CMSInitializer::createRepositoryFactory:start method");
		String factoryClass = (String)repositoryBean.getAttribute("CLASS");
		Utils.debug("CMSInitializer::createRepositoryFactory:start initialization for " + factoryClass);

        if(registeredRepos.contains(repositoryName)) {
        	Utils.major("CMSInitializer::createRepositoryFactory:repository name already in use : "+repositoryName+"\n");
            throw new Exception("Repository name already in use : "+repositoryName);	
        }
        try{
        	repFactory = (IRepositoryFactory)Class.forName(factoryClass).newInstance();
        }
        catch(Exception e) {
        	Utils.major("CMSInitializer::createRepositoryFactory:error create new instance of class " + factoryClass +
        			    " for repository "+repositoryName+"\n");
        	throw new Exception("error create new instance of class " + factoryClass +
    			                 " for repository "+repositoryName+"\n");
        }
        Utils.debug("CMSInitializer::createRepositoryFactory:return repository factory " + repFactory);
        return repFactory;
	}
	


	
	
	
	/**
	 * Register namespaces for spagocms system property and for user property
	 * @param repositoryBean Repository Configuration SourceBean
	 * @param session Repository Session
	 * @param sysPrefix  namespace prefix for spagocms system properties
	 * @param userPrefix namespace uri for spagocms system properties
	 * @param sysUri namespace prefix for spagocms user properties
	 * @param userUri namespace uri for spagocms user properties
	 */
	private void registerNamespaces(SourceBean repositoryBean, Session session,
			                        String sysPrefix, String userPrefix,
			                        String sysUri, String userUri) throws Exception {
		Utils.debug("CMSInitializer::registerNamespaces: start method\n");
		String [] prefxs = session.getWorkspace().getNamespaceRegistry().getPrefixes();
        List prefxsList = Arrays.asList(prefxs);
        Utils.debug("CMSInitializer::registerNamespaces:prefixs already registered: " + prefxsList + "\n");
        boolean notRegistrySysPre = true;
        boolean notRegistryUsrPre = true;
	    for(int j=0; j<prefxs.length; j++) {
	       	if(prefxs[j].equals(sysPrefix)) {
	       		notRegistrySysPre = false;
	        }
	        if(prefxs[j].equals(userPrefix)) {
	        	notRegistryUsrPre = false;
	        }
	    }
	    if(notRegistrySysPre) {
	    	session.getWorkspace().getNamespaceRegistry().registerNamespace(sysPrefix, sysUri);
	    	Utils.debug("CMSInitializer::registerNamespaces: user namespace registered: "+sysPrefix+":"+sysUri+"\n");
	    }
	    if(notRegistryUsrPre) {
	        session.getWorkspace().getNamespaceRegistry().registerNamespace(userPrefix, userUri);
	        Utils.debug("CMSInitializer::registerNamespaces: user namespace registered: "+userPrefix+":"+userUri+"\n");
	    }
	    session.logout();
	    Utils.debug("CMSInitializer::registerNamespaces: end method\n");
	}
	
	
	
	
	
	
	/**
	 * Create the initial structure for the repository
	 * @param repositoryBean Repository Configuration SourceBean
	 * @param session Repository Session
	 * @param sysPrefix namespace prefix for spagocms system properties
	 */
	private void createInitialStructure(SourceBean repositoryBean, Session session, String sysPrefix) throws Exception {
		// creation of the initial structure
        // ATTENTION: the following code assume that into the configuraion file the nodes to create 
        // are defined in gerarchy order. So for each path only the last element has to be created.
        // Example: /node1/node2/node3 this path assume that only the node3 not exist.
        // In future realeses this behaviour will be modified in ordet to add more flexibility.
        try {
        	Utils.debug("CMSInitializer::createInitialStructure: start method\n");
        	Node rootNode = session.getRootNode();
        	Utils.debug("CMSInitializer::createInitialStructure:root node retrived: "+rootNode+"\n");
        	List nodesInitStruct = repositoryBean.getAttributeAsList("INITIALSTRUCTURE.NODE");
        	Utils.debug("CMSInitializer::createInitialStructure:sourceBean initial structured " +
        			    "retrived "+nodesInitStruct+"\n");
        	Iterator iterNIS = nodesInitStruct.iterator();
            String pathStr = "";
            while(iterNIS.hasNext()) {
            	try{
            		SourceBean pathSB = (SourceBean)iterNIS.next();
            		Utils.debug("CMSInitializer::createInitialStructure:path element sourceBean "+pathSB+"\n");
            		pathStr = (String)pathSB.getAttribute("path");
            		Utils.debug("CMSInitializer::createInitialStructure:path element "+pathStr+"\n");
            		Path pathObj =  Path.create(pathStr);
            		try{
            			rootNode.getNode(pathObj.getRootRelativePathStr());
            			Utils.warning("CMSInitializer::createInitialStructure:path element "+pathStr+" already exists\n");
            			continue;
            		} catch (Exception e) {
            			Utils.debug("CMSInitializer::createInitialStructure:path element "+pathStr+"\n");
            		}
            		Path PathAncestorObj = pathObj.getPathAncestor(1);
            		String nameNewNode = pathObj.getNameLastElement();
            		Node parentNode = rootNode;
            		if(!PathAncestorObj.getAbsPathStr().equals("/")) {
            			parentNode = rootNode.getNode(PathAncestorObj.getRootRelativePathStr());
            		}
        			Node newNode = parentNode.addNode(nameNewNode, "nt:unstructured");
        			newNode.addMixin("mix:versionable");
        			newNode.setProperty(sysPrefix + ":"+OperationsConstants.TYPE, SetOperation.TYPE_CONTAINER);
        			parentNode.save();
        			Utils.debug("CMSInitializer::createInitialStructure:path element "+pathStr+"\n");
            	}  catch (Exception e) {
                	Utils.critical("CMSInitializer::createInitialStructure: cannot create a path of the  " +
                                   "initial structure: " + pathStr + " \n " + e);
                    continue;
                }
            	Utils.debug("CMSInitializer::createInitialStructure: end method\n");
            }
        } catch (Exception e) {
       	 	if(session != null) {
       	 		session.logout();
       	 	}
       	 	Utils.critical("CMSInitializer::createInitialStructure: cannot create " +
       	 				   "the initial structure \n " + e + "\n");
            throw e;
        }
        Utils.debug("CMSInitializer::createInitialStructure: end method\n");
	}
	
	
	
}
