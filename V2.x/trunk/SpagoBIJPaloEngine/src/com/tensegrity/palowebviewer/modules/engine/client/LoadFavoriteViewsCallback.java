package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.XFolder;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class LoadFavoriteViewsCallback implements AsyncCallback {

	private final PaloServerModel model;

	public LoadFavoriteViewsCallback(PaloServerModel model) {
		if(model == null) {
			throw new IllegalArgumentException("Model can not be null.");
		}
		this.model = model;
	}

	public void onFailure(Throwable caught) {
		Logger.error("LoadFavoriteViewsCallback:"+caught);
	}

	public void onSuccess(Object result) {
		XFolder rootFolder = (XFolder)result;
		Logger.debug("LoadFavoriteViewsCallback: loaded");
		model.setFavoriteViews(rootFolder);
		
	}

	public void send(IEngineServiceAsync engineService) {
		engineService.loadFavoriteViews(this);
	}

}
