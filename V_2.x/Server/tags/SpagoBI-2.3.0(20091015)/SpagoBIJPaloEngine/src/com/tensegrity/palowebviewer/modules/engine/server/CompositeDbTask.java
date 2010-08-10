/**
 * 
 */
package com.tensegrity.palowebviewer.modules.engine.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.palo.api.Connection;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;

class CompositeDbTask extends DbTask {
	
	private final List taskList = new ArrayList();
	
	public CompositeDbTask() {
	}
	
	public CompositeDbTask(List tasks) {
		for (Iterator iter = tasks.iterator(); iter.hasNext();) {
			DbTask task = (DbTask) iter.next();
			if(task != null)
				this.addTask(task);
		}
	}
	
	public void addTask(DbTask task) {
		if(task == null)
			throw new IllegalArgumentException("Task can not be null.");
		task.setParent(this);
		taskList.add(task);

	}
	
	public int getTaskCount() {
		return taskList.size();
	}
	
	public DbTask getTask(int i) {
		return (DbTask)taskList.get(i);
	}
	
	public CompositeDbTask(DbTask[] tasks, Connection conn) {
		this(Arrays.asList(tasks));
	}

	protected void task() throws InvalidObjectPathException {
		int size = getTaskCount();
		for(int i = 0; i < size; i++) {
			DbTask task = getTask(i);
			task.execute();
		}
	}

	protected String getServer() {
		return null;
	}
	
}