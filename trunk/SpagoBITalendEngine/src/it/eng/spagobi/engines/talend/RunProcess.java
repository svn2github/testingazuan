package it.eng.spagobi.engines.talend;

import it.eng.spagobi.engines.talend.messages.TalendMessageBundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;


public class RunProcess {

	private static transient Logger logger = Logger.getLogger(RunProcess.class);
	
    public String run(String scriptsBaseDir, String project, String jobName, String context, Locale locale) {
    
    	logger.debug("Starting run method with parameters: " +
    			"project = [" + project + "] ; " +
    			"job name = [" + jobName + "] ; " +
    			"context = [" + context + "] ; " +
    			"base dir = [" + scriptsBaseDir + "].");
    	
    	String perlInstallDir = null;
    	String perlBinDir = null;
    	String perlCommand = null;
    	String jobSeparator = null;
    	String wordSeparator = null;
    	String perlExt = null;
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
			String msg = TalendMessageBundle.getMessage("properties.file.error", locale);
    		return msg;
    	}
    	
    	String baseDirStr = scriptsBaseDir + File.separatorChar + jobName;
    	logger.debug("Job base folder: " + baseDirStr);
    	File baseDir = new File(baseDirStr);
    	if (!baseDir.exists()) {
    		logger.error("Directory " + baseDirStr + " does not exist.");
			String msg = TalendMessageBundle.getMessage("job.not.existing", locale, new String[]{jobName});
    		return msg;
    	}
    	
    	String cmd = perlInstallDir + File.separatorChar + perlBinDir + File.separatorChar + perlCommand +
    		" " + project + jobSeparator + wordSeparator + jobName + perlExt + " --context=" + project + 
    		jobSeparator + wordSeparator + jobName + wordSeparator + context + perlExt;
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
			String msg = TalendMessageBundle.getMessage("perl.exectuion.error", locale);
    		return msg;
		}
    	
		String msg = TalendMessageBundle.getMessage("etl.process.executed", locale);
		return msg;

    }
	    
}
