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

import it.eng.spagobi.engines.talend.services.JobRunService;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia
 *
 */
public class SpagoBITalendEngineConfig {
	
	Properties rrProps = new Properties();
	Properties perlProps = new Properties();
	Properties javaProps = new Properties();
	
	private static transient Logger logger = Logger.getLogger(SpagoBITalendEngineConfig.class);
	
	
	SpagoBITalendEngineConfig() {
		try {
			rrProps.load(getClass().getResourceAsStream("/talend.properties"));
    	} catch(Exception e){
    		logger.warn("Error while reading talend.properties file", e);
    	}
		
		try {
    		perlProps.load(getClass().getResourceAsStream("/talend.perl.properties"));
    	} catch(Exception e){
    		logger.warn("Error while reading talend.perl.properties file", e);
    	}
    	
    	try {
    		javaProps.load(getClass().getResourceAsStream("/talend.java.properties"));
    	} catch(Exception e){
    		logger.warn("Error while reading talend.java.properties file", e);
    	}
	}

	// rr properties
	
	public File getRuntimeRepositoryRootDir() {
		File dir = null;
		String dirName = rrProps.getProperty("runtimeRepository.rootDir");
		if(dirName != null) dir = new File(dirName);
		return dir;
	}
	
	
	// perl properties
	public String getJobSeparator() {
		return perlProps.getProperty("jobSeparator");
	}

	public String getPerlBinDir() {
		return perlProps.getProperty("perl.bin.dir");
	}

	public String getPerlCommand() {
		return perlProps.getProperty("perl.command");
	}

	public String getPerlExt() {
		return perlProps.getProperty("perlExt");
	}

	public String getPerlInstallDir() {
		return perlProps.getProperty("perl.install.dir");
	}

	public String getWordSeparator() {
		return perlProps.getProperty("wordSeparator");
	}
}
