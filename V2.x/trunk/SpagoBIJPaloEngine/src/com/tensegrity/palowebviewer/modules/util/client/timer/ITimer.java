package com.tensegrity.palowebviewer.modules.util.client.timer;

public interface ITimer {

	public void schedule(int delay);
	
	public void cancel();
	
	public void setTask(ITimerTask task);
	
}
