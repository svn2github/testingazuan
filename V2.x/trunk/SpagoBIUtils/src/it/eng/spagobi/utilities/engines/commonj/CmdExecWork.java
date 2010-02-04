package it.eng.spagobi.utilities.engines.commonj;


import java.io.File;
import java.io.IOException;

import sun.java2d.pipe.SpanIterator;

import commonj.work.Work;

public class CmdExecWork extends SpagoBIWork {

	String command;
	String commandEnvironment;
	String[] parameters;

	public boolean isDaemon() {
		// TODO Auto-generated method stub
		return false;
	}

	public void release() {
		super.release();
	}

	public void run() {
		// TODO Auto-generated method stub

	}

	public int execCommand(String cmd, File envFile) throws InterruptedException, IOException {
		int exitValue;
		try {
			Runtime rt = Runtime.getRuntime();
			String[] pars=null;
			if(parameters!=null)pars=parameters;
			Process proc  = rt.exec(cmd, pars, envFile);		
			proc.waitFor();
			exitValue=proc.exitValue();
		} catch (InterruptedException e) {
			throw(e);			
		}
		catch (IOException e) {
			throw(e);			
		}
		return exitValue;

	}


	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCommandEnvironment() {
		return commandEnvironment;
	}

	public void setCommandEnvironment(String commandEnvironment) {
		this.commandEnvironment = commandEnvironment;
	}

	public String[] getParameters() {
		return parameters;
	}

	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}




}
