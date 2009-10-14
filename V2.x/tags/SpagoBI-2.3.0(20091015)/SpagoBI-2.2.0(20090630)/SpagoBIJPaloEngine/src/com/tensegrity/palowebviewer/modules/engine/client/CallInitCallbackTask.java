package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;

/**
 * Task to call {@link IInitCallback}.
 */
public class CallInitCallbackTask implements ITask {
	
	private final IInitCallback callback;
	private final XObject[] children;

	public CallInitCallbackTask(IInitCallback callback, XObject[] children) {
		this.callback = callback;
		this.children = children;
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute() {
		callback.onSuccess(children);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "CallInitCallbackTask";
	}


}
