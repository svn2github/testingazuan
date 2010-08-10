package com.tensegrity.palowebviewer.modules.engine.client.usermessage;

import java.util.ArrayList;
import java.util.List;

public class CompositCallback implements IUserCallback {
	
	private final List callbackList = new ArrayList();
	
	public void addCallback(IUserCallback callback) {
		if(callback == null)
			throw new IllegalArgumentException("Callback can not be null.");
		callbackList.add(callback);
	}
	
	public void execute() {
		Object[] callbacks = callbackList.toArray();
		for (int i = 0; i < callbacks.length; i++) {
			IUserCallback callback = (IUserCallback)callbacks[i];
			callback.execute();
		}
	}

}
