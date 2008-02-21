package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;

public abstract class AbstractCubeLoader extends AbstractLoader {
	
	private XCube cube;
	private IPaloServerModel serverModel;

	protected XCube getCube() {
		return cube;
	}

	public void setCube(XCube cube) {
		this.cube = cube;
	}

	protected IPaloServerModel getServerModel() {
		return serverModel;
	}

	public void setServerModel(IPaloServerModel serverModel) {
		this.serverModel = serverModel;
	}

}
