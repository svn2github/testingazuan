package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;

public interface ICellUpdateCallback {
	
	public void onFinish(XPath cube, IXPoint point, IResultElement value, boolean success);
	
}
