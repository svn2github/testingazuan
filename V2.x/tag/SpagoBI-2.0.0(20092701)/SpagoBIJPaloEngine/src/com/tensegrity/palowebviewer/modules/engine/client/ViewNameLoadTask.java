package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class ViewNameLoadTask implements AsyncCallback{

	private final String name;
	private final XCube cube;
	private IChildLoadCallback callback;
	private CubeViewCache cubeViewCache;

	public ViewNameLoadTask(XCube cube, String name) {
		this.cube = cube;
		this.name = name;
	}
	
	public void onFailure(Throwable caught) {
		Logger.error("ChildLoadTask fail:"+caught);
	}

	public void onSuccess(Object result) {
		XView view = (XView)result;
		if(view != null) {
			view = cubeViewCache.getView(cube, view);
			if(callback != null)
				callback.onChildLoaded(view);
		}
	}
	
	public void setCallback(IChildLoadCallback callback) {
		this.callback = callback;
	}
	
	public void sendQuerry(IEngineServiceAsync engineService) {
		XPath path = cube.constructPath();
		engineService.loadChildByName(path, name, IXConsts.TYPE_VIEW, this);
	}

	public void setCubeViewCache(CubeViewCache cubeViewCache) {
		this.cubeViewCache = cubeViewCache;
	}

}
