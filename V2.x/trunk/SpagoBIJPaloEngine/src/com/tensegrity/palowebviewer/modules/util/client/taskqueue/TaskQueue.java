package com.tensegrity.palowebviewer.modules.util.client.taskqueue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;
import com.tensegrity.palowebviewer.modules.util.client.timer.GWTTimer;
import com.tensegrity.palowebviewer.modules.util.client.timer.ITimer;
import com.tensegrity.palowebviewer.modules.util.client.timer.ITimerTask;

public class TaskQueue {
	
	private static TaskQueue instance;
	private static final int TASK_DELAY = 1;
	
	public static TaskQueue getInstance() {
		if (instance == null) {
			initialize(new GWTTimer());
		}
		return instance;
	}
	
	public static void initialize(ITimer timer) {
		if(instance == null)
			instance = new TaskQueue(timer, TASK_DELAY);
	}
	
	
	private int delay;
	private final List taskList = new ArrayList();
	private final List listenerList = new ArrayList();
	private final ITimer timer;
	private final ITimerTask timerTask = new ITimerTask(){

		public void run() {
			executeNextTask();
		}

				
	};
	
	protected TaskQueue (ITimer timer, int delay) {
		if(timer == null)
			throw new IllegalArgumentException("Timer can not be null.");
		this.delay = delay;
		this.timer = timer;
		this.timer.setTask(timerTask);
	}
	
	protected ITask getNextTask() {
		ITask result = (ITask)taskList.remove(0);
		if(!hasTasks())
			timer.cancel();
		return result;
	}
	
	public boolean hasTasks() {
		return !taskList.isEmpty();
	}

	public int getDelay() {
		return delay;
	}
	
	public void add(ITask task) {
		if(task == null)
			throw new IllegalArgumentException("Task can not be null.");
		taskList.add(task);
		fireTaskAdded(task);
		if(taskList.size() == 1)
			timer.schedule(getDelay());
	}
	
	public void addListener(ITaskQueueListener listener) {
		if(listener == null)
			throw new IllegalArgumentException("Listener can not be null.");
		listenerList.add(listener);
	}
	
	public void removeListener(ITaskQueueListener listener) {
		listenerList.remove(listener);
	}
	
	protected void executeNextTask() {
		ITask task = getNextTask();
		fireTaskStart(task);
		PerformanceTimer timer = new PerformanceTimer("Task("+task.getName()+")");
		try {
			timer.start();
			task.execute();
			timer.report();
		}
		catch(RuntimeException re){
			re.printStackTrace();
			timer.report("fail: "+re);
			Logger.warn("Exception while task execution: "+re);
		}
		finally { 
			fireTaskFinished(task);
		}
	}
	
	protected void fireTaskStart(ITask task) {
		List list = getListenerList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			ITaskQueueListener listener = (ITaskQueueListener) it.next();
			try {
				listener.onTaskStart(task);
			}
			catch(RuntimeException re) {
				Logger.warn("Exception while dispatching events: "+re);
			}
		}
	}
	
	protected void fireTaskAdded(ITask task) {
		List list = getListenerList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			ITaskQueueListener listener = (ITaskQueueListener) it.next();
			try {
				listener.onTaskAdded(task);
			}
			catch(RuntimeException re) {
				Logger.warn("Exception while dispatching events: "+re);
			}
		}
	}
	
	protected void fireTaskFinished(ITask task) {
		List list = getListenerList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			ITaskQueueListener listener = (ITaskQueueListener) it.next();
			try {
				listener.onTaskFinished(task);
			}
			catch(RuntimeException re) {
				Logger.warn("Exception while dispatching events: "+re);
			}
		}
	}

	private List getListenerList() {
		return new ArrayList(listenerList);
	}


	
	
}
