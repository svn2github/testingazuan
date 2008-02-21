/**
 * 
 */
package com.tensegrity.palowebviewer.modules.engine.server;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.server.IConnectionPool;
import com.tensegrity.palowebviewer.server.PaloConfiguration;

class LoadChildrenTask extends DbTask {
	
	private XPath path;
	private XObject[] result;
	private PaloConfiguration configuration;
	private int type;

	public LoadChildrenTask() {
	}
	
	public PaloConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(PaloConfiguration configuration) {
		this.configuration = configuration;
	}

	public XObject[] getResult() {
		return result;
	}
	
	public void task() throws InvalidObjectPathException{
		if(path.getPath().length == 1){
			result = initRoot(getConnectioPool(), getConfiguration());
		} else {
			result = getAccessor().loadChildren(getPath(), getType(), getConnection());
		}
	}

	public XPath getPath() {
		return path;
	}

	public void setPath(XPath path) {
		this.path = path;
	}

	protected String getServer() {
		return path.getServer().getName();
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private XObject[] initRoot(IConnectionPool userConnectionPool, PaloConfiguration configuration) {
		XRoot root = new XRoot();
		getAccessor().init(root, userConnectionPool, configuration);
		return root.getServers();
	}

}