package com.tensegrity.palowebviewer.modules.engine.server;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDefaultView;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.server.paloaccessor.PaloAccessor;

public class DefaultViewConstuctorTask extends DbTask implements IXConsts{

	private XPath cubePath;
	private XDefaultView result;
	
	public DefaultViewConstuctorTask(XPath cubePath) {
		this.cubePath = cubePath;
	}

	protected String getServer() {
		return cubePath.getServer().getName();
	}

	protected void task() throws InvalidObjectPathException {
		//skip first two dimensions
		PaloAccessor accessor = getAccessor();
		XDimension[] dimensions = (XDimension[])accessor.loadChildren(cubePath, TYPE_DIMENSION, getConnection());
		XElement[] selElements = getSelectedElements(dimensions);
		result = new XDefaultView(selElements);
	}

	private XElement[] getSelectedElements(XDimension[] dimensions) throws InvalidObjectPathException {
		XElement[] r = new XElement[dimensions.length-2];
		for (int i = 2; i < dimensions.length; i++) {
			XElementNode[] nodes = (XElementNode[])getAccessor().loadChildren(dimensions[i].constructPath(), TYPE_ELEMENT_NODE, getConnection());
			if(nodes.length > 0){
				r[i-2] = nodes[0].getElement();
			}
		}
		return r;
	}

	public XDefaultView getResult() {
		return result;
	}
	

}
