package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.XViewLink;

public interface ILoadViewCallback {
	
	public void onViewLoaded(XViewLink link, XView view);

}
