package it.eng.spagobi.engines.talend;

import it.eng.spagobi.utilities.messages.EngineMessageBundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.safehaus.uuid.UUIDGenerator;


public class RunProcess {

	private static transient Logger logger = Logger.getLogger(RunProcess.class);
	
	public static final String DEFAULT_CONTEXT = "Default";
	
	private String perlInstallDir = null;
	private String perlBinDir = null;
	private String perlCommand = null;
	private String jobSeparator = null;
	private String wordSeparator = null;
	private String perlExt = null;
	
    public String run(String scriptsBaseDir, String project, String jobName, String context, 
    		Locale locale, Map parameters) {
    
    	File contextTempScriptFile = null;
    	
    	try {
	    	logger.debug("Starting run method with parameters: " +
	    			"project = [" + project + "] ; " +
	    			"job name = [" + jobName + "] ; " +
	    			"context = [" + context + "] ; " +
	    			"base dir = [" + scriptsBaseDir + "].");
	
	    	Properties prop = new Properties();
	    	try {
	    		prop.load(getClass().getResourceAsStream("/talend.properties"));
	    		perlInstallDir = prop.getProperty("perl.install.dir");
	    		perlBinDir = prop.getProperty("perl.bin.dir");
	    		perlCommand = prop.getProperty("perl.command");
		 	    jobSeparator = prop.getProperty("jobSeparator");
		 	    wordSeparator = prop.getProperty("wordSeparator");
		 	    perlExt = prop.getProperty("perlExt");
	    	} catch(Exception e){
	    		logger.error("Error while reading talend.properties file", e);
				String msg = EngineMessageBundle.getMessage("properties.file.error", locale);
	    		return msg;
	    	}
	    	
	    	String baseDirStr = scriptsBaseDir + File.separatorChar + jobName;
	    	logger.debug("Job base folder: " + baseDirStr);
	    	File baseDir = new File(baseDirStr);
	    	if (!baseDir.exists()) {
	    		logger.error("Directory " + baseDirStr + " does not exist.");
				String msg = EngineMessageBundle.getMessage("job.not.existing", locale, new String[]{jobName});
	    		return msg;
	    	}
	    	
	    	String scriptFileName = project + jobSeparator + wordSeparator + jobName + perlExt;
	    	String scriptFilePath = baseDirStr + File.separatorChar + scriptFileName;
	    	File scriptFile = new File(scriptFilePath);
	    	if (!scriptFile.exists()) {
	    		logger.error("Script file " + scriptFilePath + " does not exist.");
				String msg = EngineMessageBundle.getMessage("main.script.not.existing", locale, 
						new String[]{jobName});
	    		return msg;
	    	}
	    	
	    	String cmd = perlInstallDir + File.separatorChar + perlBinDir + File.separatorChar + perlCommand +
				" " + scriptFileName;
	    	
	    	File contextScriptFile = null;
	    	
	    	// if the context is not specified, it looks for the defualt context script file;
	    	// if it exists, the context is assumed to be the default.
	    	// Instead, if the context is specified, it looks for the relevant context script file;
	    	// if it does not exist, an error message is returned.
	    	if (context == null || context.trim().equals("")) {
	    		logger.debug("Context not specified.");
	    		String defaultScriptContextFileName = project + jobSeparator + wordSeparator 
	    											+ jobName + wordSeparator + DEFAULT_CONTEXT + perlExt;
	    		String defaultScriptContextFilePath = baseDirStr + File.separatorChar + 
	    											defaultScriptContextFileName;
	    		File defaultScriptContextFile = new File(defaultScriptContextFilePath);
	    		if (defaultScriptContextFile.exists()) {
	    			logger.debug("Found default script context file.");
	    			context = DEFAULT_CONTEXT;
	    			contextScriptFile = defaultScriptContextFile;
	    		}
	    	} else {
				String scriptContextFileName = project + jobSeparator + wordSeparator 
							+ jobName + wordSeparator + context + perlExt;
				String scriptContextFilePath = baseDirStr + File.separatorChar + scriptContextFileName;
				File scriptContextFile = new File(scriptContextFilePath);
				if (!scriptContextFile.exists()) {
		    		logger.error("Script context file " + scriptContextFilePath + " does not exist.");
					String msg = EngineMessageBundle.getMessage("context.script.not.existing", locale, 
							new String[]{context, jobName});
		    		return msg;
				} else {
					contextScriptFile = scriptContextFile;
				}
	    	}
	    	
	    	// only if the context file exists, we consider document parameter
	    	if (contextScriptFile != null && contextScriptFile.exists()) {
	    		if (parameters.isEmpty()) {
	    			String contextScriptFileName = contextScriptFile.getName();
	    			if (contextScriptFileName.indexOf(File.separatorChar) != -1) {
	    				contextScriptFileName = 
	    					contextScriptFileName.substring(contextScriptFileName.lastIndexOf(File.separatorChar));
	    			}
	    			cmd = cmd + " --context=" + contextScriptFileName;
	    		} else {
	    			//String newContextScriptFileName = contextScriptFile.getName();
					try {
						contextTempScriptFile = createTempContextScriptFile( contextScriptFile, parameters);
						//newContextScriptFileName = contextTempScriptFile.getName();
					} catch (Exception e) {
						logger.error("Error while creating temp context file", e);
						logger.error("Document parameter cannot be considered.");
					}
//	    			if (newContextScriptFileName.indexOf(File.separatorChar) != -1) {
//	    				newContextScriptFileName = 
//	    					newContextScriptFileName.substring(newContextScriptFileName.lastIndexOf(File.separatorChar));
//	    			}
//	    			cmd = cmd + " --context=\"" + newContextScriptFileName + "\"";
					cmd = cmd + " --context=\"" + contextTempScriptFile.getAbsolutePath() + "\"";
	    		}
	    	}
	    	
	    	
	    	//String cmd = perlInstallDir + File.separatorChar + perlBinDir + File.separatorChar + perlCommand +
	    	//	" " + scriptFileName + " --context=" + project + 
	    	//	jobSeparator + wordSeparator + jobName + wordSeparator + context + perlExt;
	    	
	    	logger.debug("Perl execution command: " + cmd);
	    	
			try { 
				String line;
				Process p = Runtime.getRuntime().exec(cmd, null, baseDir);
				BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					logger.debug(line);
				}
				input.close();
			} catch(Exception e){
				logger.error("Error while executing perl command:", e);
				String msg = EngineMessageBundle.getMessage("perl.exectuion.error", locale);
	    		return msg;
			}
	    	
			String msg = EngineMessageBundle.getMessage("etl.process.executed", locale);
			return msg;
			
    	} finally {
    		if (contextTempScriptFile != null && contextTempScriptFile.exists()) contextTempScriptFile.delete();
    	}
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
	    File contextScriptTempFile = new File(tempDirPath + File.separatorChar + tempFileName + perlExt);
		FileOutputStream fos = new FileOutputStream(contextScriptTempFile);
		fos.write(filebuff.toString().getBytes());
		fos.flush();
		fos.close();
		return contextScriptTempFile;
	}
	    
}
