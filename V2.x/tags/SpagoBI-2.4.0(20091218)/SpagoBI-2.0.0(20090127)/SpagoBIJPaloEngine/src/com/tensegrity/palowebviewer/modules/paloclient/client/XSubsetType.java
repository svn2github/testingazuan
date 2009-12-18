package com.tensegrity.palowebviewer.modules.paloclient.client;

public class XSubsetType implements IXSubsetType {
	
	private String name;
	
	public XSubsetType(){
		
	}
	
	public XSubsetType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean equals(Object obj) {
		boolean r = false;
		if (obj instanceof IXSubsetType) {
			IXSubsetType type = (IXSubsetType) obj;
			r = name.equals(type.getName());
		}
		return r;
	}

	public int hashCode() {
		return (name != null) ? name.hashCode() : 0;
	}

	
}
