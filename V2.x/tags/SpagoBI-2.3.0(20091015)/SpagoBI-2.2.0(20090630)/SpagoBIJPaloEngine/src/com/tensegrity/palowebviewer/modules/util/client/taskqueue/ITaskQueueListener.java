package com.tensegrity.palowebviewer.modules.util.client.taskqueue;

public interface ITaskQueueListener {
	
	public void onTaskAdded(ITask task);
	
	public void onTaskStart(ITask task);
	
	public void onTaskFinished(ITask task);

}
