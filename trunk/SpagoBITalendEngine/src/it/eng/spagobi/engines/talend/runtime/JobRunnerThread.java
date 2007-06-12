package it.eng.spagobi.engines.talend.runtime;

import it.eng.spagobi.engines.talend.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class JobRunnerThread extends Thread {

	private static transient Logger logger = Logger.getLogger(JobRunnerThread.class);
	
	private String _command = null;
	private File _executableJobDir = null;
	private String[] _envr = null;
	private List _filesToBeDeletedAfterJobExecution = null;
	
	public JobRunnerThread(String command, String[] envr, File executableJobDir, 
			List filesToBeDeletedAfterJobExecution) {
		this._command = command;
		this._executableJobDir = executableJobDir;
		this._envr = envr;
		this._filesToBeDeletedAfterJobExecution = filesToBeDeletedAfterJobExecution;
	}
	
	public void run() {
		if (_command == null) {
			logger.error("No command to be executed");
			return;
		}
    	try { 
			String line;
			Process p = Runtime.getRuntime().exec(_command, _envr, _executableJobDir);
			BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				logger.debug(line);
				//System.out.println(line);
			}
			input.close();
		} catch (Exception e){
			logger.error("Error while executing command " + _command, e);
		} finally {
			if (_filesToBeDeletedAfterJobExecution != null && _filesToBeDeletedAfterJobExecution.size() > 0) {
				Iterator it = _filesToBeDeletedAfterJobExecution.iterator();
				while (it.hasNext()) {
					File aFile = (File) it.next();
					if (aFile != null && aFile.exists()) FileUtils.deleteDirectory(aFile);
				}
			}
		}
		
	}
	
}
