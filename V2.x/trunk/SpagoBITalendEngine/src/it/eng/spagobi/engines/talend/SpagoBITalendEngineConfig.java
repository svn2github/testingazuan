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
package it.eng.spagobi.engines.talend;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.services.common.EnginConf;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia
 *
 */
public class SpagoBITalendEngineConfig {
	

	
	File engineRootDir = new File("");
	
	private static transient Logger logger = Logger.getLogger(SpagoBITalendEngineConfig.class);
	
	
	SpagoBITalendEngineConfig() {

	}

		
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
	public File getRuntimeRepositoryRootDir() {
	    
	        SourceBean config = EnginConf.getInstance().getConfig();
	        String dirName = (String)config.getCharacters("RUNTIMEREPOSITORY_ROOT_DIR");
	        File dir = null;
		if( !isAbsolutePath(dirName) )  {
			dirName = engineRootDir.toString() + System.getProperty("file.separator") + dirName;
		}		
		
		if(dirName != null) dir = new File(dirName);
		return dir;
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
	 * Gets the spagobi url.
	 * 
	 * @return the spagobi url
	 */
	public String getSpagobiUrl() {
		String url = null;
        SourceBean config = EnginConf.getInstance().getConfig();
        url= (String)config.getCharacters("spagobi_context_path");
		return url;
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
	 * Gets the perl bin dir.
	 * 
	 * @return the perl bin dir
	 */
	public String getPerlBinDir() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String binDir= (String)config.getCharacters("perl_bin_dir");
		return binDir;
	}

	/**
	 * Gets the perl command.
	 * 
	 * @return the perl command
	 */
	public String getPerlCommand() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String command= (String)config.getCharacters("perl_command");
		return command;
	}

	/**
	 * Gets the perl ext.
	 * 
	 * @return the perl ext
	 */
	public String getPerlExt() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String ext= (String)config.getCharacters("perlExt");
		return ext;
	}

	/**
	 * Gets the perl install dir.
	 * 
	 * @return the perl install dir
	 */
	public String getPerlInstallDir() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String install= (String)config.getCharacters("perl_install_dir");	
		return install;
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

	/**
	 * Gets the engine root dir.
	 * 
	 * @return the engine root dir
	 */
	public File getEngineRootDir() {
		return engineRootDir;
	}

	/**
	 * Sets the engine root dir.
	 * 
	 * @param engineRootDir the new engine root dir
	 */
	public void setEngineRootDir(File engineRootDir) {
		this.engineRootDir = engineRootDir;
	}
}
