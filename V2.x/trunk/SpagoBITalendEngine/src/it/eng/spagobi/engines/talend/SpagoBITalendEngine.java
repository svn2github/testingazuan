/**

 LICENSE: see COPYING file
  
**/
package it.eng.spagobi.engines.talend;



import java.io.File;

import it.eng.spagobi.engines.talend.runtime.RuntimeRepository;

/**
 * @author Andrea Gioia
 *
 */
public class SpagoBITalendEngine implements Version {
	
	private SpagoBITalendEngineConfig config;
	private RuntimeRepository runtimeRepository;
	
	static private SpagoBITalendEngine instance;
	
	/**
	 * Gets the single instance of SpagoBITalendEngine.
	 * 
	 * @return single instance of SpagoBITalendEngine
	 */
	static public SpagoBITalendEngine getInstance() {
		if(instance == null) instance = new SpagoBITalendEngine();
		return instance;
	}
	
	private SpagoBITalendEngine() { 
		config = new SpagoBITalendEngineConfig();
	}
	
	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public static String getVersion() {
		return MAJOR + "." + MINOR + "." + REVISION;
	}
	    
	/**
	 * Gets the full name.
	 * 
	 * @return the full name
	 */
	public static String getFullName() {
		return ENGINE_NAME + "-" + getVersion();
	}
	    
	/**
	 * Gets the info.
	 * 
	 * @return the info
	 */
	public static String getInfo() {
		return getFullName() + " [ " + WEB +" ]";
	}

	/**
	 * Gets the runtime repository.
	 * 
	 * @return the runtime repository
	 */
	public RuntimeRepository getRuntimeRepository() {
		if(runtimeRepository == null) {
			File rrRootDir = config.getRuntimeRepositoryRootDir();
			runtimeRepository = new RuntimeRepository(rrRootDir);
		}
		return runtimeRepository;
	}

	/**
	 * Gets the config.
	 * 
	 * @return the config
	 */
	public SpagoBITalendEngineConfig getConfig() {
		return config;
	}

	/**
	 * Gets the compliance version.
	 * 
	 * @return the compliance version
	 */
	public static String getComplianceVersion() {
		return CLIENT_COMPLIANCE_VERSION;
	}
	
	
}
