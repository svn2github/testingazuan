package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.util.client.taskchain.SimpleChainTask;

public class RebuildHeaderTask extends SimpleChainTask{
	
	private final AxisDimensionList dimList;

	public RebuildHeaderTask(AxisDimensionList dimList) {
		this.dimList = dimList;
	}

	protected void run() {
		dimList.rebuildHeader();
	}
	
	public String getDescription() {
		return "RebuildHeaderTask";
	}

}
