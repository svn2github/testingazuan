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
package it.eng.spagobi.engines.talend.runtime;

import it.eng.spagobi.engines.talend.SpagoBITalendEngine;
import it.eng.spagobi.engines.talend.SpagoBITalendEngineConfig;
import it.eng.spagobi.engines.talend.exception.ContextNotFoundException;
import it.eng.spagobi.engines.talend.exception.JobExecutionException;
import it.eng.spagobi.engines.talend.exception.JobNotFoundException;
import it.eng.spagobi.engines.talend.utils.TalendScriptAccessUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUIDGenerator;

/**
 * @author Andrea Gioia
 *
 */
public class JavaJobRunner implements IJobRunner {
private static transient Logger logger = Logger.getLogger(PerlJobRunner.class);
	
	private RuntimeRepository runtimeRepository;
	
	public static final String DEFAULT_CONTEXT = "Default";
	
	
	
	
	JavaJobRunner(RuntimeRepository runtimeRepository) {
		this.runtimeRepository = runtimeRepository;
	}
	
		
    public void run(Job job, Map parameters) throws JobNotFoundException, ContextNotFoundException, JobExecutionException {
    	File contextTempScriptFile = null;
    	File contextFile = null;
    	String tmpDirName = null;
    	File contextFileDir = null;
    	
    	try {
	    	logger.debug("Starting run method with parameters: " +
	    			"project = [" + job.getProject() + "] ; " +
	    			"job name = [" + job.getName() + "] ; " +
	    			"context = [" + job.getContext() + "] ; " +
	    			"base dir = [" + runtimeRepository.getRootDir() + "].");
	
	    	
	    	
	    	File executableJobDir = runtimeRepository.getExecutableJobDir(job);
	    	logger.debug("Job base folder: " + executableJobDir);
	    	
	    	    	

	    	if (!runtimeRepository.containsJob(job)) {	    		
	    		throw new JobNotFoundException("Job executable file " + 
	    				runtimeRepository.getExecutableJobFile(job) + " does not exist.");
	    	}
	    	
	   
	    	
	    	// if the context is not specified, it looks for the defualt context script file;
	    	// if it exists, the context is assumed to be the default.
	    	// Instead, if the context is specified, it looks for the relevant context script file;
	    	// if it does not exist, an error message is returned.
	    	
	    	
	    	if (job.getContext() == null || job.getContext().trim().equals("")) {
	    		logger.debug("Context not specified.");
	    		job.setContext(DEFAULT_CONTEXT);
	    	} 
	    		
	    	File executableJobFile = runtimeRepository.getExecutableJobFile(job);
	    	JarFile jarFile = new JarFile(executableJobFile);
	    	StringBuffer buffer = new StringBuffer();
	    	buffer.append(job.getProject().toLowerCase());
	    	buffer.append("/" + job.getName().toLowerCase());
	    	buffer.append("/contexts");
	    	buffer.append("/" + job.getContext() + ".properties");
	    	ZipEntry ze = jarFile.getEntry(buffer.toString());
	    	if (ze == null) {
		    	throw new ContextNotFoundException("Script context file " + contextFile + " does not exist.");
			}
	    		
	    		
	    	Properties props = new Properties();
	    	props.load( jarFile.getInputStream(ze) );
	    	Iterator it = parameters.keySet().iterator();
	    	while(it.hasNext()) {
	    		String pname = (String)it.next();
	    		String pvalue = (parameters.get(pname) != null)? (String)parameters.get(pname): (String)props.getProperty(pname);
	    		props.setProperty(pname, pvalue);
	    	}
	    		
	    	UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
	    	String uuid = uuidGenerator.generateTimeBasedUUID().toString();
	    	tmpDirName = "tmp" + uuid;
	    	String tmpDirPath = runtimeRepository.getExecutableJobDir(job).getAbsolutePath() + File.separatorChar + tmpDirName;
	    	String contextFileDirPath = tmpDirPath + File.separatorChar + job.getProject().toLowerCase();
	    	contextFileDirPath += File.separatorChar + job.getName().toLowerCase();
	    	contextFileDirPath += File.separatorChar + "contexts";
	    	contextFileDir = new File(contextFileDirPath);
	    	contextFileDir.mkdirs();
	    	contextFile = new File(contextFileDir, job.getContext() + ".properties");
	    	OutputStream out = new FileOutputStream(contextFile);
	    	props.store(out, "Talend Context");
	    	
	
	    	
	    	String classpath = "." + File.separatorChar + tmpDirName + ";" + getCalssPath(job);
	    	String cmd = "java -Xms256M -Xmx1024M -cp " + classpath  + " " + TalendScriptAccessUtils.getExecutableClass(job);
	        
	    	cmd = cmd + " --context=" + job.getContext();
	    		    		    	
	    	logger.debug("Java execution command: " + cmd);
	    	
	    	try { 
				String line;
				Process p = Runtime.getRuntime().exec(cmd, null, executableJobDir);
				BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					logger.debug(line);
					System.out.println(line);
				}
				input.close();
			} catch(Exception e){
				throw new JobExecutionException("Error while executing perl command:", e);
			}
    	} catch (Exception e) {
    		throw new JobExecutionException("Error while executing perl command:", e);
    	} finally {
			if (contextTempScriptFile != null && contextTempScriptFile.exists()) contextTempScriptFile.delete();
			if (contextFile != null && contextFile.exists()) contextFile.delete();
			if (contextFileDir != null && contextFileDir.exists()) contextFileDir.delete();			
    	}
    }
    
    


	public String getCalssPath(Job job) {
    	StringBuffer classpath = new StringBuffer();
    	
    	List libs = TalendScriptAccessUtils.getIncludedLibs(job, runtimeRepository);
    	for(int i = 0; i < libs.size(); i++){
    		File file = (File)libs.get(i);    		
    		classpath.append( (i>0? ";": "") + "../lib/" + file.getName());
    	}
    	
    	classpath.append( (libs.size()>0? ";": "") + TalendScriptAccessUtils.getExecutableFileName(job));
    	
    	System.out.println(classpath);
    	
    	return classpath.toString();
    }
}
