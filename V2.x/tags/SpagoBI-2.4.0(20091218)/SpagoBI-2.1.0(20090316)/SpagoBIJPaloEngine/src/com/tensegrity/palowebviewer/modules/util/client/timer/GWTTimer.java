package com.tensegrity.palowebviewer.modules.util.client.timer;

import com.google.gwt.user.client.Timer;

public class GWTTimer extends Timer implements ITimer {
	
	private ITimerTask task;

	public void setTask(ITimerTask task) {
		this.task = task;
	}

	public void run() {
		if(task != null)
			task.run();
	}
	
	public void schedule(int value) {
		scheduleRepeating(value);
	}
	
	

}
