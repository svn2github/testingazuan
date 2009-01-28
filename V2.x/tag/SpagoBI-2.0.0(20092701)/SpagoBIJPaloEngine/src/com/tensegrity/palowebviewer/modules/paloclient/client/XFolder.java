package com.tensegrity.palowebviewer.modules.paloclient.client;

import java.util.ArrayList;
import java.util.List;

public class XFolder extends XFavoriteNode {
	
	/**
	 *@gwt.typeArgs <com.tensegrity.palowebviewer.modules.paloclient.client.XFavoriteNode>
	 */
	private List childList = new ArrayList();
	private boolean connection;
	
	public boolean isConnection() {
		return connection;
	}
	
	public void setConnnection(boolean value) {
		connection = value;
	}
	
	public int getChildCount() {
		return childList.size();
	}
	
	public XFavoriteNode getChild(int i) {
		return (XFavoriteNode)childList.get(i);
	}
	
	public void addChild(XFavoriteNode child) {
		if(child == null)
			throw new IllegalArgumentException("Child can not be null.");
		childList.add(child);
	}
	
	public String toString() {
		String result = "XFolder[" + getName();
		if(isConnection()){
			result += "(connection)";
		}
		result += "]";
		return result;
	}
	
	public boolean equals(Object o) {
		boolean result = false;
		if(o instanceof XFolder) {
			XFolder folder = (XFolder)o;
			result = connection == folder.isConnection() && super.equals(folder);
		}
		return result;
	}

}
