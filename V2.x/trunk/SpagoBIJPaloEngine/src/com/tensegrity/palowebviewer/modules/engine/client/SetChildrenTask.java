package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;

class SetChildrenTask implements ITask {
	private final XObject[] path;
	private final XObject[] children;
	private final int type;
	private final PaloServerModel serverModel;
	private final LoadingMap loadingMap;
	
	public SetChildrenTask(PaloServerModel serverModel, XObject[] path, XObject[] children, int type) {
		this.path = path;
		this.children = children;
		this.type = type;
		this.serverModel = serverModel;
		this.loadingMap = serverModel.getLoadingMap();
	}

	public void execute() {
		try {
			serverModel.setObject(path, children, type);
		}
		finally {
			if(path.length >0) {
				XObject object = path[path.length-1];
				loadingMap.setFinishLoading(object, type);
			}
		}
	}
	
	public String getName() {
		return "SetChildrenTask";
	}
}
