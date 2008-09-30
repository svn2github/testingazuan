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
package it.eng.spagobi.engines.talend.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.engines.talend.SpagoBITalendEngine;
import it.eng.spagobi.engines.talend.SpagoBITalendEngineConfig;
import it.eng.spagobi.engines.talend.runtime.Job;
import it.eng.spagobi.engines.talend.runtime.RuntimeRepository;

/**
 * @author Andrea Gioia
 * @author Luca Andreatta (modification for sub-projects management)
 */
public class TalendScriptAccessUtils {
	
	
	public static String getExecutableClass(Job job) {
		StringBuffer buffer = new StringBuffer();
		
		if(job.isJavaJob()) {
			buffer.append(job.getProject().toLowerCase());
    		buffer.append(".");
    		buffer.append(job.getName().toLowerCase());
    		buffer.append(".");
    		buffer.append(job.getName().substring(0,1).toUpperCase() + job.getName().substring(1));
    	} else{
    		return null;
    	}
		
		
		return buffer.toString();
	}
	

	public static String getExecutableFileName(Job job) {
		StringBuffer buffer = new StringBuffer();
		SpagoBITalendEngineConfig config = SpagoBITalendEngine.getInstance().getConfig();
		if(job.isPerlJob()) {
    		buffer.append(job.getProject());
    		buffer.append(config.getJobSeparator());
    		buffer.append(config.getWordSeparator());
    		buffer.append(job.getName());
    		buffer.append(config.getPerlExt());
    	} else if(job.isJavaJob()) {
    		buffer.append(job.getName().toLowerCase());
    		buffer.append(".jar");    		
    	} else{
    		return null;
    	}
		
		
		return buffer.toString();
	}
	
	public static List getIncludedLibs(Job job, RuntimeRepository runtimeRepository) {
		List libs = new ArrayList();
		if(job.isPerlJob()) {
    		// do nothing
    	} else if(job.isJavaJob()) {
    		File executableJobProjectDir = runtimeRepository.getExecutableJobProjectDir(job);
    		File libsDir = new File(executableJobProjectDir, "lib");
    		File[] files	= libsDir.listFiles();  
    		for(int i  = 0; i < files.length; i++) {
    			libs.add(files[i]);
    		}
    	} else{
    		return null;
    	}
		
		return libs;
	}
	
	/**
	 * Gets a list of jar files of the job to execute and connected sub-jobs.
	 * 
	 * @param job the job to execute
	 * @param runtimeRepository the runtime repository
	 * 
	 * @return the main job libs
	 */
	public static List getMainJobLibs(Job job, RuntimeRepository runtimeRepository) {
		List libs = new ArrayList();
		if(job.isPerlJob()) {
    		// do nothing
    	} else if(job.isJavaJob()) {
    		File executableJobProjectDir = runtimeRepository.getExecutableJobProjectDir(job);
    		File libsDir = new File(executableJobProjectDir, job.getName());
    		File[] files	= libsDir.listFiles();  
    		for(int i  = 0; i < files.length; i++) {
    			if (files[i].getName().endsWith(".jar"))
    				libs.add(files[i]);
    		}
    	} else{
    		return null;
    	}
		
		return libs;
	}
	
	
	///////////////////////////////////////////////////////////////
	// CONTEXT HANDLING METHODS
	///////////////////////////////////////////////////////////////
	
	public static String getContextFileName(Job job) {		
		StringBuffer buffer = new StringBuffer();
		SpagoBITalendEngineConfig config = SpagoBITalendEngine.getInstance().getConfig();
		if(job.isPerlJob()) {
    		buffer.append(job.getProject());
    		buffer.append(config.getJobSeparator());
    		buffer.append(config.getWordSeparator());
    		buffer.append(job.getName());
    		buffer.append(config.getWordSeparator());
    		buffer.append(job.getContext());
    		buffer.append(config.getPerlExt());
    	} else if(job.isJavaJob()) {
    		return null;
    	} else{
    		return null;
    	}
				
		return buffer.toString();
	}
	
	public static File getContextFile(Job job, RuntimeRepository runtimeRepository) {		
		File contextFile = null;
		StringBuffer buffer = new StringBuffer();
		
		SpagoBITalendEngineConfig config = SpagoBITalendEngine.getInstance().getConfig();
		if(job.isPerlJob()) {
    		buffer.append(job.getProject());
    		buffer.append(config.getJobSeparator());
    		buffer.append(config.getWordSeparator());
    		buffer.append(job.getName());
    		buffer.append(config.getWordSeparator());
    		buffer.append(job.getContext());
    		buffer.append(config.getPerlExt());
    		File executableJobDir = runtimeRepository.getExecutableJobDir(job);
    		String contextFileName = buffer.toString();
    		contextFile = new File(executableJobDir, contextFileName);
    	} else if(job.isJavaJob()) {
    		buffer.append(runtimeRepository.getExecutableJobFile(job));
    		buffer.append("!");
    		buffer.append("\\" + job.getProject().toLowerCase());
    		buffer.append("\\" + job.getName().toLowerCase());
    		buffer.append("\\context");
    		buffer.append("\\" + job.getContext() + ".properties");
    		contextFile = new File(buffer.toString());
    	} else{
    		return null;
    	}
				
		return contextFile;
	}
}
