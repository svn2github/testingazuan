package it.eng.spagobi.utilities.engines.commonj;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import sun.java2d.pipe.SpanIterator;

import commonj.work.Work;

public class CmdExecWork extends SpagoBIWork {

	String command;
	String commandEnvironment;

	/** parameters  passed to command*/
	Vector<String> cmdParameters;
	/** to be added to classpath*/
	Vector<String> classpathParameters;



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


	/** this method executes command followed by command parameters taken from template
	 *  and by sbi parameters
	 *  and add classpath variables followed by -cp
	 * 
	 * @param cmd
	 * @param envFile
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */

	public int execCommand(String cmd, File envFile) throws InterruptedException, IOException {

		File directoryExec = new File(commandEnvironment);

		// build command
		String commandToLaunch = command;

		// add -cp 
		if(classpathParameters.size()>0){
			command += " -cp ";
			for (Iterator iterator = classpathParameters.iterator(); iterator.hasNext();) {
				String add = (String) iterator.next();
				command += add;
				if(iterator.hasNext()){
					command += ";";
				}
			}
		}

		command += " ";

		// add command parameters
		for (Iterator iterator = cmdParameters.iterator(); iterator.hasNext();) {
			String par = (String) iterator.next();			
			command += par + " ";
		}


		// select sbi driver from MAP and add values!
		for (Iterator iterator = analyticalParameters.iterator(); iterator.hasNext();) {
			String url= (String) iterator.next();
			if (sbiParameters.get(url) != null){
				Object value = sbiParameters.get(url);
				command += " " + url + "=" + value +" "; 
			}
		}


		ProcessBuilder pb = new ProcessBuilder(command);
		Map<String, String> env = pb.environment();

		//env.put("Var1", "myValue");
		//env.remove("OTHERVAR");
		//env.put("VAR2", env.get("VAR1") + "suffix");

		pb.directory(envFile);

		//Process p = pb.start();

		Process p = Runtime.getRuntime().exec(command, null, envFile);
		
		int exitValue = p.exitValue();
		//p.exitValue();

		return exitValue;

	}


	/*	

	public int execCommand(String cmd, File envFile) throws InterruptedException, IOException {
		int exitValue;
		try {
			Runtime rt = Runtime.getRuntime();
			String[] pars=null;
			if(cmdParameters!=null)pars=cmdParameters;
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

	 */

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

	public Vector<String> getCmdParameters() {
		return cmdParameters;
	}

	public void setCmdParameters(Vector<String> cmdParameters) {
		this.cmdParameters = cmdParameters;
	}

	public Vector<String> getClasspathParameters() {
		return classpathParameters;
	}

	public void setClasspathParameters(Vector<String> classpathParameters) {
		this.classpathParameters = classpathParameters;
	}




}
