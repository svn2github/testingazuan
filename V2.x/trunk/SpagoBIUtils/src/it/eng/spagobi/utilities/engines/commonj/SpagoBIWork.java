package it.eng.spagobi.utilities.engines.commonj;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import commonj.work.Work;

public class SpagoBIWork implements Work{

	String pid;

	boolean running=false;

	Map sbiParameters=new HashMap();

	// names of those in sbiParameters that are analytical drivers
	Vector<String> analyticalParameters;

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



	public Vector<String> getAnalyticalParameters() {
		return analyticalParameters;
	}


	public void setAnalyticalParameters(Vector<String> analyticalParameters) {
		this.analyticalParameters = analyticalParameters;
	}


	public String getPid() {
		return pid;
	}


	public void setPid(String pid) {
		this.pid = pid;
	}



}
