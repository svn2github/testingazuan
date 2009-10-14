package com.tensegrity.palowebviewer.modules.engine.server;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;

public class ElementExistanceCheckerTask extends DbTask {

	private XPath ctxPath;
	private String elementId;
	private boolean result = false;
	
	public ElementExistanceCheckerTask(XPath path, String elementId){
		this.ctxPath = path;
		this.elementId = elementId;
	}
	
	protected String getServer() {
		return ctxPath.getServer().getName();
	}

	protected void task() throws InvalidObjectPathException {
		result = getAccessor().checkExistance(ctxPath, elementId, getConnection());
	}
	
	public boolean getResult(){
		return result;
	}

}
