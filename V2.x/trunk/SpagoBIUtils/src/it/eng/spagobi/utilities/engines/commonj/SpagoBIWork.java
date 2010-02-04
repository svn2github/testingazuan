package it.eng.spagobi.utilities.engines.commonj;

import commonj.work.Work;

public class SpagoBIWork implements Work{

	boolean running=false;

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




}
