package it.eng.spagobi.utilities.engines.commonj;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

public class CmdExecWork extends SpagoBIWork {

	String command;
	String commandEnvironment;

	/** parameters  passed to command*/
	Vector<String> cmdParameters;
	/** to be added to classpath*/
	Vector<String> classpathParameters;
	/** Logger */
	static private Logger logger = Logger.getLogger(CmdExecWork.class);
	/** the process aunche*/
	Process process = null;

	public boolean isDaemon() {
		// TODO Auto-generated method stub
		return false;
	}

	public void release() {
		logger.debug("IN");
		super.release();
		if(process != null){
			process.destroy();
		}
		logger.debug("OUT");
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

	public int execCommand() throws InterruptedException, IOException {
		logger.debug("IN");
		File directoryExec = null;
		if(commandEnvironment != null) {
			directoryExec = new File(commandEnvironment);
		}
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
				command += url + "=" + value +" "; 
			}
		}


//		ProcessBuilder pb = new ProcessBuilder(command);
//		pb.directory(envFile);
//		Map<String, String> env = pb.environment();

		//env.put("Var1", "myValue");
		//env.remove("OTHERVAR");
		//env.put("VAR2", env.get("VAR1") + "suffix");


//		Process p = pb.start();
		Process process = null;
		if(isRunning()){
			logger.debug("launch command "+command);
			process = Runtime.getRuntime().exec(command, null, directoryExec);
		}
		else{
			logger.warn("Command not launched cause work has been stopper");
		}
		//int exitValue = p.exitValue();
		//p.exitValue();
		//p.wait(1000);

//		try{
//		p.waitFor();
//		}catch(InterruptedException ie){
//		p.destroy();
//		}

		logger.debug("OUT");
		return 0;

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
