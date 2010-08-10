package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.util.client.Logger;

public abstract class AbstractLoader implements IChainLoader {

	private ILoaderCallback callback;

	public void setCallback(ILoaderCallback callback) {
		this.callback = callback;
	}

	public void loaded() {
		load();
	}
	
	private void logLoaded() {
		String message = getDescription()+": loaded.";
		Logger.debug(message);
	}

	protected void executeCallback() {
		logLoaded();
		if(callback != null) {
			callback.loaded();
		}
	}
	
	public abstract String getDescription();
	
}