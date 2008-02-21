package com.tensegrity.palowebviewer.modules.util.client.taskqueue;

/**
 * Interface for the tasks. It it use to split a program in to 
 * smaller tasks, so we can make some breaks in thre thread that interprets 
 * JavaScript on the borwser side and get read of the worning about not responding script. 
 *
 */
public interface ITask {
	
	/**
	 * Returns the name of the task. It is used for the debug puposes.
	 *  And usualy is the name of the implementing class.
	 */
	public String getName();
	
	/**
	 * The method that is called when the time for the task has come.
	 */
	public void execute();

}
