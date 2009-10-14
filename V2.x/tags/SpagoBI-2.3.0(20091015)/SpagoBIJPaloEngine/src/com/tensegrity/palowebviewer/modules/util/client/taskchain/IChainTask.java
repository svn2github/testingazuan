package com.tensegrity.palowebviewer.modules.util.client.taskchain;

public interface IChainTask {
	
	public void execute();
	
	public IChainTask getNextTask();
	
	public void setNextTask(IChainTask task);
	
	public String getDescription();

}
