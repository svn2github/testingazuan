package com.tensegrity.palowebviewer.modules.engine.server;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;

public class ExistanceCheckerTask extends DbTask {

	private XPath path;
	private boolean result = false;
	
	public ExistanceCheckerTask(XPath path){
		this.path = path;
	}
	
	protected String getServer() {
		return path.getServer().getName();
	}

	protected void task(){
		try {
			result = getAccessor().getLastObject(path, getConnection()) != null;
		} catch (InvalidObjectPathException e) {
			result = false;
		}
	}
	
	public boolean getResult(){
		return result;
	}

}
