package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;

public class IDChildLoader implements IChildLoader, IChildLoaderCallback {

	private IChildLoaderCallback callback;
	private final String id;
	private final PaloServerModel serverMode;
	private final int type;
	private IInitCallback initCallback = new IInitCallback () {

		public void onSuccess(XObject[] children) {
			onChildrenLoaded(children);
		}

	};
	
	public IDChildLoader(PaloServerModel serverMode, String id, int type) {
		this.serverMode = serverMode;
		this.id = id;
		this.type = type;
	}

	public void setCallback(IChildLoaderCallback callback) {
		this.callback = callback;
	}

	public void onChildLoaded(XObject xObject) {
		XObject[] children = ChildrenGetter.getChildren(xObject, type);
		if(children == null) {
			serverMode.loadChildren(xObject, type, initCallback);
		}
		else {
			onChildrenLoaded(children);
		}
		
	}
	
	protected void callCallback(XObject xObject) {
		if(callback != null) {
			callback.onChildLoaded(xObject);
		}
	}

	public String getId() {
		return id;
	}

	public IPaloServerModel getServerMode() {
		return serverMode;
	}

	private void onChildrenLoaded(XObject[] children) {
		XObject object = XArrays.findById(children, id);
		callCallback(object);
	}

}
