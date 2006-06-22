package it.eng.spago.cms.init;

import it.eng.spago.base.Constants;
import it.eng.spago.cms.pool.CMSPool;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.tracing.TracerSingleton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Mantains the set of connection pool correspondent to the 
 * repositories. Implements methods for retrive an istance of 
 * a pool an for reistry new connection pool  
 * 
 * 
 */
public class CMSManager{

	/**
	 * Set of connection pools.
	 */
	private static HashMap _repositories = new HashMap();
	/**
	 * Name of the default repository
	 */
	private static String defaultRepository = null;

	/**
	 * Registry a new repository pool connection 
	 * 
	 * @param pool, the connection pool to registry
	 * @param name, the name of the repository connect
	 * @throws EMFInternalError
	 */
    protected static synchronized void registerRepository(CMSPool pool, String name) throws EMFInternalError {
    	if (pool == null) {
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.WARNING,
                                "CMSManager::registerRepository: connection pool null");
            throw new EMFInternalError(EMFErrorSeverity.ERROR, "connection pool null");
        }
    	Set keys = _repositories.keySet();
    	Iterator iterKeys = keys.iterator();
	    boolean nameMatch = false;
	    while(iterKeys.hasNext()) {
	      String key = (String)iterKeys.next();
	      if(key.equalsIgnoreCase(name))
	      	 nameMatch = true;
	    }
	    if(nameMatch){
	       TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.WARNING,
                               "CMSManager::registerRepository: name already exists");
           throw new EMFInternalError(EMFErrorSeverity.ERROR, "Repository " + name + " already exists");
	    }
    	_repositories.put(name, pool);
    }
	
    /**
     * Retrive an istance of the connection pool for
     * a specific repository identified by the inut name
     * 
     * @param name, name of the repository connect
     * @return Instance of the repository connection pool
     * @throws EMFInternalError
     */
	public static synchronized CMSPool getInstance(String name) throws EMFInternalError{
	    Set keys = _repositories.keySet();
	    if(!keys.contains(name)) {
	    	CMSInitializer initial = new CMSInitializer();
	    	initial.setUp();
	    	keys = _repositories.keySet();
	    }
	    boolean nameMatch = false;
	    Iterator iterKeys = keys.iterator();
	    while(iterKeys.hasNext()) {
	      String key = (String)iterKeys.next();
	      if(key.equalsIgnoreCase(name))
	      	 nameMatch = true;
	    }
	    if(!nameMatch){
	       TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.WARNING,
                               "ContentRepositoryManager::getInstance: repository name not exists");
           throw new EMFInternalError(EMFErrorSeverity.ERROR, "Repository pool " + name + " non found");
	    }
	    else{
	    	return (CMSPool)_repositories.get(name);
	    }
	}

	
	/**
	 * Check if a pool with a given name is alrady registered
	 * @param name name of the pool
	 * @return boolean, true if the pool name is already registered, false otherwise
	 */
	public static synchronized boolean isRegistered(String name) {
		Set keys = _repositories.keySet();
		return keys.contains(name);
	}
	
	
	
	/**
     * Destroy an istance of the connection pool for
     * a specific repository identified by the inut name
     * 
     * @param name, name of the repository connect
     * @return Instance of the repository connection pool
     * @throws EMFInternalError
     */
	public static synchronized void destroyInstance(String name) throws EMFInternalError{
	    
		if(_repositories.containsKey(name)) {
			CMSPool pool = (CMSPool) _repositories.get(name);
			pool.shutdown();
			_repositories.remove(name);
		} else {
	       TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.WARNING,
                               "ContentRepositoryManager::destroyInstance: repository name not exists");
           throw new EMFInternalError(EMFErrorSeverity.ERROR, "Repository pool " + name + " non found");
	    }
	}
	
	
    /**
     * Retrive an istance of the connection pool for the default
     * repository
     * 
     * @return Instance of the repository connection pool
     * @throws EMFInternalError
     */
	public static synchronized CMSPool getInstance() throws EMFInternalError{
		return getInstance(CMSManager.getDefaultRepository());
	}
	
	
	/**
     * Destroy an istance of the connection pool for the default
     * repository
     * 
     * @return Instance of the repository connection pool
     * @throws EMFInternalError
     */
	public static synchronized void destroyInstance() throws EMFInternalError{
		destroyInstance(CMSManager.getDefaultRepository());
	}
	
	/**
	 * Get the name of the default repository
	 * @return name of the default repository
	 */
	public static String getDefaultRepository() {
		return defaultRepository;
	}

	/**
	 * Set the name of the default repository
	 * @param defaultRepository, name to set
	 */
	protected static void setDefaultRepository(String defaultRepository) {
		CMSManager.defaultRepository = defaultRepository;
	}
}
