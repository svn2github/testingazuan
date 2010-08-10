/**
 * 
 */
package com.tensegrity.palowebviewer.modules.engine.server;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XViewPath;

class SaveViewTask extends DbTask {
	
	private XViewPath viewPath;
	private String viewId;

	public void task()  throws InvalidObjectPathException{
		viewId = getAccessor().save(getViewPath(), getConnection());
	}

	public XViewPath getViewPath() {
		return viewPath;
	}

	public void setViewPath(XViewPath viewPath) {
		this.viewPath = viewPath;
	}

	protected String getServer() {
		return viewPath.getCube().getServer().getName();
	}

	public String getId() {
		return viewId;
	}
	
}