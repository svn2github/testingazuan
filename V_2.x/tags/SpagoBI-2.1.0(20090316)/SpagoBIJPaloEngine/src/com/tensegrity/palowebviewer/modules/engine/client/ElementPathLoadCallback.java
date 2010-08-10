/**
 * 
 */
package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

final class ElementPathLoadCallback implements AsyncCallback {

	private XObject root;
	private XElement element;
	private IElementPathCallback callback;

	public void onFailure(Throwable caught) {
		Logger.error("ElementPathLoadCallback fail:"+caught);
	}

	public void onSuccess(Object result) {
		XElement[] path = (XElement[])result;
		callback.onSuccess(path);
	}

	public void setRoot(XObject hierarchyRoot) {
		if(hierarchyRoot == null)
			throw new IllegalArgumentException("Root can not be null");
		this.root = hierarchyRoot;
	}

	public void setElement(XElement element) {
		if(element == null)
			throw new IllegalArgumentException("element can not be null");
		this.element = element;
	}

	public void setCallback(IElementPathCallback callback) {
		if(callback == null)
			throw new IllegalArgumentException("Callback can not be null");
		this.callback = callback;
	}

	public void sendQuerry(IEngineServiceAsync engineService) {
		String elementId = element.getId();
		XPath contextPath = root.constructPath();
		engineService.getParentsOf(contextPath, elementId, this);
	}
	
}