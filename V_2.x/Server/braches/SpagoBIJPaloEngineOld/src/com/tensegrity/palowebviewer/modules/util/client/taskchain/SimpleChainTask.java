package com.tensegrity.palowebviewer.modules.util.client.taskchain;

import com.tensegrity.palowebviewer.modules.util.client.Logger;

public abstract class SimpleChainTask extends AbstractChainTask {

	protected abstract void run(); 
	
	public void execute() {
		Logger.debug(getDescription()+": start");
		run();
		executeNextTask();
	}

}
