/**

 LICENSE: see COPYING file
  
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
 *
 */
public class TalendScriptAccessUtils {
	
	
	/**
	 * Gets the executable class.
	 * 
	 * @param job the job
	 * 
	 * @return the executable class
	 */
	public static String getExecutableClass(Job job) {
		StringBuffer buffer = new StringBuffer();
		
		if(job.isJavaJob()) {
			buffer.append(job.getProject().toLowerCase());
    		buffer.append(".");
    		buffer.append(job.getName().toLowerCase());
    		buffer.append(".");
    		//not more Java convention but original name
    		buffer.append(job.getName().substring(0,1) + job.getName().substring(1));
    		//buffer.append(job.getName().substring(0,1).toUpperCase() + job.getName().substring(1));
    	} else{
    		return null;
    	}
		
		
		return buffer.toString();
	}
	

	/**
	 * Gets the executable file name.
	 * 
	 * @param job the job
	 * 
	 * @return the executable file name
	 */
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
	
	/**
	 * Gets the included libs.
	 * 
	 * @param job the job
	 * @param runtimeRepository the runtime repository
	 * 
	 * @return the included libs
	 */
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
	
	
	
	
	///////////////////////////////////////////////////////////////
	// CONTEXT HANDLING METHODS
	///////////////////////////////////////////////////////////////
	
	/**
	 * Gets the context file name.
	 * 
	 * @param job the job
	 * 
	 * @return the context file name
	 */
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
	
	/**
	 * Gets the context file.
	 * 
	 * @param job the job
	 * @param runtimeRepository the runtime repository
	 * 
	 * @return the context file
	 */
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
