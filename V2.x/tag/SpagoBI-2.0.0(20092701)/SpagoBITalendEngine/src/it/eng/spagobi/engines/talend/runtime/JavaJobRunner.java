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
package it.eng.spagobi.engines.talend.runtime;

import it.eng.spagobi.engines.talend.TalendEngine;
import it.eng.spagobi.engines.talend.TalendEngineConfig;
import it.eng.spagobi.engines.talend.exception.ContextNotFoundException;
import it.eng.spagobi.engines.talend.exception.JobExecutionException;
import it.eng.spagobi.engines.talend.exception.JobNotFoundException;
import it.eng.spagobi.engines.talend.utils.TalendScriptAccessUtils;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;
import it.eng.spagobi.utilities.file.FileUtils;
import it.eng.spagobi.utilities.file.IFileTransformer;
import it.eng.spagobi.utilities.threadmanager.WorkManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUIDGenerator;

/**
 * @author Andrea Gioia
 *
 */
public class JavaJobRunner implements IJobRunner {
private static transient Logger logger = Logger.getLogger(JavaJobRunner.class);
	
	private RuntimeRepository runtimeRepository;
	
	public static final String DEFAULT_CONTEXT = "Default";
	
	public static final String DEFAULT_XMS_VALUE = "256";
	public static final String DEFAULT_XMX_VALUE = "1024";
	
	
	
	JavaJobRunner(RuntimeRepository runtimeRepository) {
		this.runtimeRepository = runtimeRepository;
	}
	
	
		
    /* (non-Javadoc)
     * @see it.eng.spagobi.engines.talend.runtime.IJobRunner#run(it.eng.spagobi.engines.talend.runtime.Job, java.util.Map, it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils, java.lang.String)
     */
    public void run(Job job, Map parameters) 
    	throws JobNotFoundException, ContextNotFoundException, JobExecutionException {
    	
    	File contextFile = null;
    	//String tmpDirName = null;
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
	    	
	    	File tempDir = getTempDir(job);
	   		String tmpDirName = tempDir.getName();
	    	initContexts(job, parameters, tempDir);
	    	//String tmpDirName = instantiateContext(job, parameters);
	    	
	    	String classpath = "." + File.separatorChar + tmpDirName + File.pathSeparator 
	    			+ TalendScriptAccessUtils.getClassPath(job, runtimeRepository); //getClassPath(job);
	    	
	    	TalendEngineConfig config = TalendEngineConfig.getInstance();
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
	    	
	    	WorkManager wm = new WorkManager();
	    	TalendWork jrt = new TalendWork(cmd, null, executableJobDir, filesToBeDeleted, 
    			parameters);
	    	TalendWorkListener listener=new TalendWorkListener();
	    	wm.run(jrt, listener);
	    	// davide sparire usa run e destroy oppure fai classe statica che lancia comando 	Process p = Runtime.getRuntime().exec(_command, _envr, _executableJobDir);
	    	/*JobRunnerThread jrt = new JobRunnerThread(cmd, null, executableJobDir, filesToBeDeleted, 
	    			auditAccessUtils, auditId, parameters,session);
	    	jrt.start();*/
	    	/*
	    	JobRunnerFacilities jrt = new JobRunnerFacilities(cmd, null, executableJobDir, filesToBeDeleted, 
	    			auditAccessUtils, auditId, parameters,session);
	    	jrt.executeJob();
	    	*/
	    	
    	} catch (Exception e) {
    		throw new JobExecutionException("Error while preparing java command:", e);
    	}
    }
    
    public void initContexts(Job job, Map parameters, File destDir) {
    	File contextsBaseDir = TalendScriptAccessUtils.getContextsBaseDir(job, runtimeRepository);
    	
    	
    	
    	FileUtils.doForEach(new File(contextsBaseDir, job.getProject().toLowerCase()), new IFileTransformer() {
    		Job job;
    		Map parameters;
    		File destDir;
    		File srcDir; 
    		
    		public IFileTransformer init(Job job, Map parameters, File srcDir, File destDir) {
    			this.job = job;
    			this.parameters = parameters;
    			this.destDir = destDir;
    			this.srcDir = srcDir;
    			return this;
    		}

			public boolean transform(File file) {
				String str1 = file.getName();
				String str2 = job.getContext() + ".properties";
				
				if(file.getName().equalsIgnoreCase(job.getContext() + ".properties")) {
					try {
					
						Properties context = new Properties();
						FileInputStream in = new FileInputStream(file);
						
			        	context.load( in );
			        	Iterator it = parameters.keySet().iterator();
			        	while(it.hasNext()) {
			        		String pname = (String)it.next();
			        		Object o = parameters.get(pname);
			        		
			        		if(o instanceof String) {
			        			String pvalue = (String)parameters.get(pname);
			        			context.setProperty(pname, pvalue);
			        		}		        		
			        	}
			        	File destFile = null;
			        	
			        	String pathToContext = file.getAbsolutePath().substring(srcDir.getAbsolutePath().length());
			        	destFile = new File(destDir, pathToContext);
			        	destFile.getParentFile().mkdirs();
			        	OutputStream out = new FileOutputStream(destFile);
			        	context.store(out, "Talend Context");
			        	
			        	in.close();
			        	out.flush();
			        	out.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return false;
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
				return true;
				
			}    		
    	}.init(job, parameters, contextsBaseDir, destDir) );
  
    }
    
    public File getTempDir(Job job) {
    	UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
    	String uuid = uuidGenerator.generateTimeBasedUUID().toString();
    	String tmpDirName = "tmp" + uuid;
    	String tmpDirPath = runtimeRepository.getExecutableJobDir(job).getAbsolutePath() + File.separatorChar + tmpDirName;
    	return new File(tmpDirPath);
    }
    
    
    
    public String instantiateContext(Job job, Map parameters) throws ContextNotFoundException, IOException {
    	
    	File contextFile;
    	File contextFileDir;
    	String tmpDirName;
    	
    	
    	// if the context is not specified, it looks for the defualt context script file;
    	// if it exists, the context is assumed to be the default.
    	// Instead, if the context is specified, it looks for the relevant context script file;
    	// if it does not exist, an error message is returned.
    	if (job.getContext() == null || job.getContext().trim().equals("")) {
    		logger.debug("Context not specified.");
    		job.setContext(DEFAULT_CONTEXT);
    	} 
    		
    	Properties context = getContext(job);
    	if(context == null) {
    		throw new ContextNotFoundException("Impossible to load context " + job.getContext());
    	}
    	
    	Iterator it = parameters.keySet().iterator();
    	while(it.hasNext()) {
    		String pname = (String)it.next();
    		Object o = parameters.get(pname);
    		
    		if(o instanceof String) {
    			String pvalue = (String)parameters.get(pname);
    			context.setProperty(pname, pvalue);
    		}
    		
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
    	out.flush();
    	out.close();
    	
    	return tmpDirName;
    }
    
    /**
     * Gets the context.
     * 
     * @param job the job
     * 
     * @return the context
     */
    public Properties getContext(Job job){
    	Properties context = null;
    	StringBuffer buffer = null;
    	
    	try {
	    	buffer = new StringBuffer();
	    	buffer.append( runtimeRepository.getExecutableJobDir(job).getAbsolutePath() );
	    	buffer.append("/" +job.getProject().toLowerCase());
	    	buffer.append("/" + job.getName().toLowerCase() + "_" + job.getVersion().replace('.', '_'));
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

	
}
