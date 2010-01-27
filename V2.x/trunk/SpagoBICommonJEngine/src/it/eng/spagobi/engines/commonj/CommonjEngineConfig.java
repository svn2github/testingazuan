/**
Copyright (c) 2005-2008, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
**/
package it.eng.spagobi.engines.commonj;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.services.common.EnginConf;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.file.FileUtils;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia
 *
 */
public class CommonjEngineConfig {
	
	private EnginConf engineConfig;
	
	private static CommonjEngineConfig instance;
	
	
	public static String COMMONJ_REPOSITORY_ROOT_DIR = "commonjRepository_root_dir";
	
	private static transient Logger logger = Logger.getLogger(CommonjEngineConfig.class);

	
	public static CommonjEngineConfig getInstance() {
		if(instance == null) {
			instance =  new CommonjEngineConfig();
		}
		
		return instance;
	}
	
	
	private CommonjEngineConfig() {
		setEngineConfig( EnginConf.getInstance() );
	}
	
	public SourceBean getConfigSourceBean() {
		return getEngineConfig().getConfig();
	}
	
	
	// core settings
	
	/**
	 * Checks if is absolute path.
	 * 
	 * @param path the path
	 * 
	 * @return true, if is absolute path
	 */
	public static boolean isAbsolutePath(String path) {
		if(path == null) return false;
		return (path.startsWith("/") || path.startsWith("\\") || path.charAt(1) == ':');
	}
	
	
	/**
	 * Gets the runtime repository root dir.
	 * 
	 * @return the runtime repository root dir
	 */
	public File getWorksRepositoryRootDir() {
	    
		String property = getProperty( COMMONJ_REPOSITORY_ROOT_DIR);
		
		SourceBean config = EnginConf.getInstance().getConfig();
	        
	        File dir = null;
		if( !isAbsolutePath(property) )  {
			property = getEngineResourcePath() + System.getProperty("file.separator") + property;
		}		
		
		if(property != null) dir = new File(property);
		
		return dir;
	}
	
	
	
	// engine settings
	
	public String getEngineResourcePath() {
		String path = null;
		if(getEngineConfig().getResourcePath() != null) {
			path = getEngineConfig().getResourcePath() + System.getProperty("file.separator") + "commonj";
		} else {
			path = ConfigSingleton.getRootPath() + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "commonj";
		}
		
		return path;
	}
	
	
	
	
	// utils 
	
	private String getProperty(String propertName) {
		String propertyValue = null;		
		SourceBean sourceBeanConf;
		
		Assert.assertNotNull( getConfigSourceBean(), "Impossible to parse engine-config.xml file");
		
		sourceBeanConf = (SourceBean) getConfigSourceBean().getAttribute( propertName);
		if(sourceBeanConf != null) {
			propertyValue  = (String) sourceBeanConf.getCharacters();
			logger.debug("Configuration attribute [" + propertName + "] is equals to: [" + propertyValue + "]");
		}
		
		return propertyValue;		
	}
	
	
	
	
	
	
	

	
	
	
	
	/**
	 * Gets the spagobi target functionality label.
	 * 
	 * @return the spagobi target functionality label
	 */
	public String getSpagobiTargetFunctionalityLabel() {
		String label = null;
        SourceBean config = EnginConf.getInstance().getConfig();
        label= (String)config.getCharacters("spagobi_functionality_label");
		return label;
	}
	

	
	/**
	 * Checks if is auto publish active.
	 * 
	 * @return true, if is auto publish active
	 */
	public boolean isAutoPublishActive() {
		String autoPublishProp = null;
        SourceBean config = EnginConf.getInstance().getConfig();
        autoPublishProp= (String)config.getCharacters("spagobi_autopublish");
        if(autoPublishProp != null && autoPublishProp.equalsIgnoreCase("true")) return true;
		return false;
	}
	
	
	// java properties
	/**
	 * Gets the java install dir.
	 * 
	 * @return the java install dir
	 */
	public String getJavaInstallDir() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String installDir= (String)config.getCharacters("java_install_dir");
		return installDir;
	}
	
	/**
	 * Gets the java bin dir.
	 * 
	 * @return the java bin dir
	 */
	public String getJavaBinDir() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String binDir= (String)config.getCharacters("java_bin_dir");
		return binDir;
	}
	
	/**
	 * Gets the java command.
	 * 
	 * @return the java command
	 */
	public String getJavaCommand() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String command= (String)config.getCharacters("java_command");	
		return command;
	}
	
	/**
	 * Gets the java command option.
	 * 
	 * @param optionName the option name
	 * 
	 * @return the java command option
	 */
	public String getJavaCommandOption(String optionName) {
        SourceBean config = EnginConf.getInstance().getConfig();
        String commandOption= (String)config.getCharacters("java_command_option_"+optionName);	
		return commandOption;
	}
	
	// perl properties
	/**
	 * Gets the job separator.
	 * 
	 * @return the job separator
	 */
	public String getJobSeparator() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String jobSeparator= (String)config.getCharacters("jobSeparator");
		return jobSeparator;
	}


	/**
	 * Gets the word separator.
	 * 
	 * @return the word separator
	 */
	public String getWordSeparator() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String wordS= (String)config.getCharacters("wordSeparator");			
		return wordS;
	}


	public EnginConf getEngineConfig() {
		return engineConfig;
	}

	public void setEngineConfig(EnginConf engineConfig) {
		this.engineConfig = engineConfig;
	}
}
