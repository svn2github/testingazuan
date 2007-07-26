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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUIDGenerator;


public class PerlJobRunner implements IJobRunner {

	private static transient Logger logger = Logger.getLogger(PerlJobRunner.class);
	
	private RuntimeRepository runtimeRepository;
	
	public static final String DEFAULT_CONTEXT = "Default";
	
	
	
	
	PerlJobRunner(RuntimeRepository runtimeRepository) {
		this.runtimeRepository = runtimeRepository;
	}
	
		
    public void run(Job job, Map parameters, AuditAccessUtils auditAccessUtils, String auditId) throws JobNotFoundException, ContextNotFoundException, JobExecutionException {
    
    	File contextTempScriptFile = null;
    	
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
    	
    	SpagoBITalendEngineConfig config = SpagoBITalendEngine.getInstance().getConfig();
    	String cmd = config.getPerlInstallDir() + File.separatorChar + config.getPerlBinDir()+ 
    		File.separatorChar + config.getPerlCommand() + " " + TalendScriptAccessUtils.getExecutableFileName(job);
    	
    		    	
    	// if the context is not specified, it looks for the defualt context script file;
    	// if it exists, the context is assumed to be the default.
    	// Instead, if the context is specified, it looks for the relevant context script file;
    	// if it does not exist, an error message is returned.
    	File contextFile = null;
    	
    	if (job.getContext() == null || job.getContext().trim().equals("")) {
    		logger.debug("Context not specified.");
    		job.setContext(DEFAULT_CONTEXT);
    		String contextFileName = TalendScriptAccessUtils.getContextFileName(job);
    		File defaultContextFile = new File(executableJobDir, contextFileName);
    		if (defaultContextFile.exists()) {
    			logger.debug("Found default script context file.");
    			contextFile = defaultContextFile;
    		}
    	} else {
    		String contextFileName = TalendScriptAccessUtils.getContextFileName(job);
    		contextFile = new File(executableJobDir, contextFileName);
    		if (!contextFile.exists()) {
	    		throw new ContextNotFoundException("Script context file " + contextFile + " does not exist.");
			}
    	}
    	
    	
		
    	// only if the context file exists, we consider document parameter
    	if (contextFile != null) {
    		if (parameters.isEmpty()) {
    			String contextFileName = contextFile.getName();
    			if (contextFileName.indexOf(File.separatorChar) != -1) {
    				contextFileName = 
    					contextFileName.substring(contextFileName.lastIndexOf(File.separatorChar));
    			}
    			cmd = cmd + " --context=" + contextFileName;
    		} else {
    			//String newContextScriptFileName = contextScriptFile.getName();
				try {
					contextTempScriptFile = createTempContextScriptFile( contextFile, parameters);
					//newContextScriptFileName = contextTempScriptFile.getName();
				} catch (Exception e) {
					logger.error("Error while creating temp context file", e);
					logger.error("Document parameter cannot be considered.");
				}
				cmd = cmd + " --context=\"" + contextTempScriptFile.getAbsolutePath() + "\"";
    		}
    	}
    	
    	
    	logger.debug("Perl execution command: " + cmd);
    	
    	List filesToBeDeleted = new ArrayList();
    	filesToBeDeleted.add(contextTempScriptFile);
    	JobRunnerThread jrt = new JobRunnerThread(cmd, null, executableJobDir, filesToBeDeleted, 
    			auditAccessUtils, auditId, parameters);
    	jrt.start();
			
    }

	private File createTempContextScriptFile(File contextScriptFile, Map parameters) throws Exception {
		FileReader reader = new FileReader(contextScriptFile);
		StringBuffer filebuff = new StringBuffer();
		char[] buffer = new char[1024];
		int len;
		while ((len = reader.read(buffer)) >= 0) {
			filebuff.append(buffer, 0, len);
		}
		reader.close();
		Set entries = parameters.entrySet();
		Iterator it = entries.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String parameterName = entry.getKey().toString();
			Object parameterValueObj = entry.getValue(); 
			logger.debug("Found parameter '" + parameterName + "' with value '" + parameterValueObj + "'.");
			if (parameterValueObj == null || parameterValueObj.toString().trim().equals("")) {
				logger.debug("Parameter '" + parameterName + "' has no value.");
				continue;
			}
			String parameterValue = parameterValueObj.toString();
			int index = filebuff.indexOf("$_context{" + parameterName + "}");
			if (index < 0) {
				logger.error("Parameter '" + parameterName + "' not found in context script file.");
				continue;
			} else {
				int startIndex = filebuff.indexOf("=", index) + 1;
				int endIndex = filebuff.indexOf(";", startIndex);
				filebuff.replace(startIndex, endIndex, parameterValue);
			}
		}
	    String contextScriptFilePath = contextScriptFile.getAbsolutePath();
	    String contextScriptDirPath = contextScriptFilePath.substring(0, 
	    		contextScriptFilePath.lastIndexOf(File.separatorChar));
	    String tempDirPath = contextScriptDirPath + File.separatorChar 
	    						+ ".." + File.separatorChar + ".." + File.separatorChar + "temp";
	    File tempDir = new File(tempDirPath);
	    if (!tempDir.exists()) tempDir.mkdir();
	    else {
	    	if (!tempDir.isDirectory()) tempDir.delete();
	    }
	    UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
	    String tempFileName = uuidGenerator.generateTimeBasedUUID().toString();
	    SpagoBITalendEngineConfig config = SpagoBITalendEngine.getInstance().getConfig();
	    File contextScriptTempFile = new File(tempDirPath + File.separatorChar + tempFileName + config.getPerlExt());
		FileOutputStream fos = new FileOutputStream(contextScriptTempFile);
		fos.write(filebuff.toString().getBytes());
		fos.flush();
		fos.close();
		return contextScriptTempFile;
	}
	    
}
