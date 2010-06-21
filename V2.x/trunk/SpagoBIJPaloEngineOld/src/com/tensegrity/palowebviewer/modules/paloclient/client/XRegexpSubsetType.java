package com.tensegrity.palowebviewer.modules.paloclient.client;

public class XRegexpSubsetType extends XSubsetType {

	private static IXSubsetType instance = null;
	
	public static IXSubsetType getInstance(){
		if (instance == null) {
			instance = new XRegexpSubsetType();
		}
		return instance;
	}
	
	public XRegexpSubsetType() {
		super("regexp");
	}

}
