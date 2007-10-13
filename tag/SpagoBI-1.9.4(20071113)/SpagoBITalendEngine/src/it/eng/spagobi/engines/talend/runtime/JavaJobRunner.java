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
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
	
	public static final String DEFAULT_XMS_VALUE = "256";
	public static final String DEFAULT_XMX_VALUE = "1024";
	
	
	
	
	JavaJobRunner(RuntimeRepository runtimeRepository) {
		this.runtimeRepository = runtimeRepository;
	}
	
		
    public void run(Job job, Map parameters, AuditAccessUtils auditAccessUtils, String auditId) throws JobNotFoundException, ContextNotFoundException, JobExecutionException {
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
	    		
	    	/*
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
	    	*/
	    		
	    	Properties context = getContext(job);
	    	if(context == null) {
	    		throw new ContextNotFoundException("Impossible to load context " + job.getContext());
	    	}
	    	/*
	    	Properties props = new Properties();
	    	props.load( jarFile.getInputStream(ze) );
	    	*/
	    	Iterator it = parameters.keySet().iterator();
	    	while(it.hasNext()) {
	    		String pname = (String)it.next();
	    		String pvalue = (parameters.get(pname) != null)? (String)parameters.get(pname): (String)context.getProperty(pname);
	    		context.setProperty(pname, pvalue);
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
	    	context.store(out, "Talend Context");
	    	
	
	    	
	    	String classpath = "." + File.separatorChar + tmpDirName + File.pathSeparator + getClassPath(job);
	    	SpagoBITalendEngineConfig config = SpagoBITalendEngine.getInstance().getConfig();
	    	String cmd = "java";
	    	
	    	String javaInstallDir = config.getJavaInstallDir();
	    	if(config.getJavaInstallDir() != null) {
	    		String javaBinDir = (config.getJavaBinDir() != null? config.getJavaBinDir(): "bin");
	    		String javaCommand = (config.getJavaCommand() != null? config.getJavaCommand(): "java");
	    		cmd = javaInstallDir + File.separatorChar + javaBinDir + File.separatorChar + javaCommand;
	    	}
	    	
	    	String xmsValue = (config.getJavaCommandOption("Xms")!= null? config.getJavaCommandOption("Xms"): DEFAULT_XMS_VALUE);
	    	String xmxValue = (config.getJavaCommandOption("Xmx")!= null? config.getJavaCommandOption("Xmx"): DEFAULT_XMX_VALUE);
	    	cmd += " -Xms" + xmsValue +"M -Xmx" + xmxValue + "M -cp " + classpath  + " " + TalendScriptAccessUtils.getExecutableClass(job);
	        
	    	cmd = cmd + " --context=" + job.getContext();
	    		    		    	
	    	logger.debug("Java execution command: " + cmd);
	    	
	    	List filesToBeDeleted = new ArrayList();
	    	filesToBeDeleted.add(contextFileDir);
	    	JobRunnerThread jrt = new JobRunnerThread(cmd, null, executableJobDir, filesToBeDeleted, 
	    			auditAccessUtils, auditId, parameters);
	    	jrt.start();
	    	
    	} catch (Exception e) {
    		throw new JobExecutionException("Error while preparing java command:", e);
    	}
    }
    
    
    public Properties getContext(Job job){
    	Properties context = null;
    	StringBuffer buffer = null;
    	
    	try {
	    	buffer = new StringBuffer();
	    	buffer.append( runtimeRepository.getExecutableJobDir(job).getAbsolutePath() );
	    	buffer.append("/" +job.getProject().toLowerCase());
	    	buffer.append("/" + job.getName().toLowerCase());
	    	buffer.append("/contexts");
	    	buffer.append("/" + job.getContext() + ".properties");
	    	File contextFile = new File(buffer.toString());
	    	if(contextFile.exists()) {
	    		context = new Properties();
	        	context.load( new FileInputStream(contextFile) );				
	    	} else {
			   	File executableJobFile = runtimeRepository.getExecutableJobFile(job);
			   	JarFile jarFile = new JarFile(executableJobFile);
			   	buffer = new StringBuffer();
			   	buffer.append(job.getProject().toLowerCase());
			   	buffer.append("/" + job.getName().toLowerCase());
			   	buffer.append("/contexts");
			   	buffer.append("/" + job.getContext() + ".properties");
			   	ZipEntry ze = jarFile.getEntry(buffer.toString());
			   	if (ze != null) {
			   		context = new Properties();
			       	context.load( jarFile.getInputStream(ze) );					
				}
	    	}
    	} catch (IOException e) {
			context = null;
			e.printStackTrace();
		}
    	
    	
    	return context;    		
    }

	public String getClassPath(Job job) {
    	StringBuffer classpath = new StringBuffer();
    	
    	List libs = TalendScriptAccessUtils.getIncludedLibs(job, runtimeRepository);
    	for(int i = 0; i < libs.size(); i++){
    		File file = (File)libs.get(i);    		
    		classpath.append( (i>0? File.pathSeparator : "") + "../lib/" + file.getName());
    	}
    	
    	classpath.append( (libs.size()>0? File.pathSeparator : "") + TalendScriptAccessUtils.getExecutableFileName(job));
    	
    	logger.debug(classpath);
    	//System.out.println(classpath);
    	
    	return classpath.toString();
    }
}
