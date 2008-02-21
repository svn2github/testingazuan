/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;

final class NodeStateChangeTask implements ITask {
	private final ICubeTableModel cubeTableModel;
	private final HeaderTreeNode xNode;
	private final HeaderTreeNode yNode;
	private final boolean opened;
	private String description;
	
	

	
	public NodeStateChangeTask(ICubeTableModel model, HeaderTreeNode xNode, HeaderTreeNode yNode, boolean opened) {
		cubeTableModel = model;
		this.xNode = xNode;
		this.yNode = yNode;
		this.opened = opened;
	}

	public void execute() {
		if(opened && cubeTableModel.isModelValid()&& cubeTableModel.isAllowDataLoad()){
			cubeTableModel.reloadData(xNode, yNode);
        }
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		String result = "NodeStateChangeTask";
		if(description != null)
			result += "[" + description + "]";
		return result;
	}
	
}