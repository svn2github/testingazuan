package com.tensegrity.palowebviewer.modules.engine.server;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;

public class GetParentsOfTask extends DbTask {

	private XPath ctx;
	private String id;
	private XElement[] result;
	
	public GetParentsOfTask(XPath ctx, String id) {
		this.ctx = ctx;
		this.id = id;
	}

	protected String getServer() {
		return ctx.getServer().getName();
	}

	protected void task() throws InvalidObjectPathException {
		result = getAccessor().getParentsOf(ctx, id, getConnection());
	}
	
	public XElement[] getResult(){
		return result;
	}

}
