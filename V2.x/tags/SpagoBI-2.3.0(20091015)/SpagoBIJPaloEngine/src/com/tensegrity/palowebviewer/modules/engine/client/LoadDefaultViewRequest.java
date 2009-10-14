package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDefaultView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class LoadDefaultViewRequest implements AsyncCallback {
	
	
	private final XCube cube;
	private final PaloServerModelListenerCollection listners;

	public LoadDefaultViewRequest(XCube cube, PaloServerModelListenerCollection listeners) {
		this.cube = cube;
		this.listners = listeners;
	}

	public void onFailure(Throwable caught) {
		Logger.error(caught+"");
	}
	
	public void sendRequest(IEngineServiceAsync engineService) {
		XPath path = cube.constructPath();
		engineService.loadDefaultView(path, this);
	}

	public void onSuccess(Object result) {
		XDefaultView view = (XDefaultView) result;
		cube.setDefaultView(view);
		listners.fireDefaultViewLoaded(cube);
	}

}
