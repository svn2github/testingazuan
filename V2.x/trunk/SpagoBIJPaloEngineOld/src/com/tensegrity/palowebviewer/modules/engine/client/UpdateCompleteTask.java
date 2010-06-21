package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;

final class UpdateCompleteTask implements ITask {
	
	private final PaloServerModel serverModel;

	public UpdateCompleteTask(PaloServerModel serverModel) {
		this.serverModel = serverModel;
	}

	public void execute() {
		serverModel.stopUpdateHierarchy();
	}

	public String getName() {
		return "UpdateCompleteTask";
	}

}

