package it.eng.spagobi.utilities.engines.commonj;

import java.util.HashMap;
import java.util.Map;

import commonj.work.Work;

public class SpagoBIWork implements Work{

	boolean running=false;

	Map sbiParameters=new HashMap();
	
	public boolean isDaemon() {
		// TODO Auto-generated method stub
		return false;
	}


	public void release() {

		running=false;
	}

	public void run() {
		running=true;		
	}


	public boolean isRunning() {
		return running;
	}


	public void setRunning(boolean running) {
		this.running = running;
	}


	public Map getSbiParameters() {
		return sbiParameters;
	}


	public void setSbiParameters(Map sbiParameters) {
		this.sbiParameters = sbiParameters;
	}




}
