/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

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

		
	public static boolean isAbsolutePath(String path) {
		if(path == null) return false;
		return (path.startsWith("/") || path.startsWith("\\") || path.charAt(1) == ':');
	}
	
	
	public File getRuntimeRepositoryRootDir() {
	    
	        SourceBean config = EnginConf.getInstance().getConfig();
         	String dirName = (String)config.getAttribute("VIRTUALIZER.runtimeRepository_root_dir");
		File dir = null;
		if( !isAbsolutePath(dirName) )  {
			dirName = engineRootDir.toString() + System.getProperty("file.separator") + dirName;
		}		
		
		if(dirName != null) dir = new File(dirName);
		return dir;
	}
	
	public String getSpagobiTargetFunctionalityLabel() {
		String label = null;
        SourceBean config = EnginConf.getInstance().getConfig();
        label= (String)config.getAttribute("VIRTUALIZER.spagobi_functionality_label");
		return label;
	}
	
	public String getSpagobiUrl() {
		String url = null;
        SourceBean config = EnginConf.getInstance().getConfig();
        url= (String)config.getAttribute("VIRTUALIZER.spagobi_url");
		return url;
	}
	
	public boolean isAutoPublishActive() {
		String autoPublishProp = null;
        SourceBean config = EnginConf.getInstance().getConfig();
        autoPublishProp= (String)config.getAttribute("VIRTUALIZER.spagobi_autopublish");
        if(autoPublishProp != null && autoPublishProp.equalsIgnoreCase("true")) return true;
		return false;
	}
	
	
	// java properties
	public String getJavaInstallDir() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String installDir= (String)config.getAttribute("VIRTUALIZER.java_install_dir");
		return installDir;
	}
	
	public String getJavaBinDir() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String binDir= (String)config.getAttribute("VIRTUALIZER.java_bin_dir");
		return binDir;
	}
	
	public String getJavaCommand() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String command= (String)config.getAttribute("VIRTUALIZER.java_command");	
		return command;
	}
	
	public String getJavaCommandOption(String optionName) {
        SourceBean config = EnginConf.getInstance().getConfig();
        String commandOption= (String)config.getAttribute("VIRTUALIZER.java_command_option_"+optionName);	
		return commandOption;
	}
	
	// perl properties
	public String getJobSeparator() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String jobSeparator= (String)config.getAttribute("VIRTUALIZER.jobSeparator");
		return jobSeparator;
	}

	public String getPerlBinDir() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String binDir= (String)config.getAttribute("VIRTUALIZER.perl_bin_dir");
		return binDir;
	}

	public String getPerlCommand() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String command= (String)config.getAttribute("VIRTUALIZER.perl_command");
		return command;
	}

	public String getPerlExt() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String ext= (String)config.getAttribute("VIRTUALIZER.perlExt");
		return ext;
	}

	public String getPerlInstallDir() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String install= (String)config.getAttribute("VIRTUALIZER.perl_install_dir");	
		return install;
	}

	public String getWordSeparator() {
        SourceBean config = EnginConf.getInstance().getConfig();
        String wordS= (String)config.getAttribute("VIRTUALIZER.wordSeparator");			
		return wordS;
	}

	public File getEngineRootDir() {
		return engineRootDir;
	}

	public void setEngineRootDir(File engineRootDir) {
		this.engineRootDir = engineRootDir;
	}
}
