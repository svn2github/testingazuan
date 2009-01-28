package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;

public interface IObjectUpdaterListener {
	
	public void childrenArrayChanged(XObject[] path, XObject[] oldChildren, int type);

	public void objectRenamed(XObject object);

}
