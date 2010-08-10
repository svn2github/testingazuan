package com.tensegrity.palowebviewer.modules.paloclient.client;

import com.google.gwt.user.client.rpc.IsSerializable;


public class XFavoriteNode implements IsSerializable {
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(Object o) {
		boolean result = false;
		if(o instanceof XFavoriteNode){
			String anotherName = ((XFavoriteNode)o).getName();
			result = name == null ? anotherName == null : name.equals(anotherName);
		}
		return result;
	}
	
}
