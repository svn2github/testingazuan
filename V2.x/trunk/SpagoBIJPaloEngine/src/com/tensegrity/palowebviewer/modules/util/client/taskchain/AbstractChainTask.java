package com.tensegrity.palowebviewer.modules.util.client.taskchain;

import com.tensegrity.palowebviewer.modules.util.client.Logger;

public abstract class AbstractChainTask implements IChainTask {

	private IChainTask task;

	public abstract void execute();

	public IChainTask getNextTask() {
		return task;
	}

	public void setNextTask(IChainTask task) {
		this.task = task;
	}
	
	protected void executeNextTask() {
		Logger.debug(getDescription()+": finished");
		if(task != null){
			Logger.debug("execute next " + task.getDescription());
			task.execute();
		}
	}

}
