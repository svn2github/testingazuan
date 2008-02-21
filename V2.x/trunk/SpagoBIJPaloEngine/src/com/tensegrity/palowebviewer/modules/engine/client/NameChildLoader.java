package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class NameChildLoader implements IChildLoader, IChildLoaderCallback {

	private IChildLoaderCallback callback;
	private final String name;
	private final PaloServerModel serverMode;
	private final int type;
	private IInitCallback initCallback = new IInitCallback () {

		public void onSuccess(XObject[] children) {
			onChildrenLoaded(children);
		}

	};
	
	public NameChildLoader(PaloServerModel serverMode, String name, int type) {
		this.serverMode = serverMode;
		this.name = name;
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
		return name;
	}

	public IPaloServerModel getServerMode() {
		return serverMode;
	}

	private void onChildrenLoaded(XObject[] children) {
		XObject object = XArrays.findByName(children, name);
		if(object == null) {
			Logger.debug("Object with name '"+name+"' was not found");
		}
		else {
			callCallback(object);
		}
	}

}
