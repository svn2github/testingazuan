package com.tensegrity.palowebviewer.modules.paloclient.client;

public class XFlatSubsetType extends XSubsetType {

	private static IXSubsetType instance = null;
	
	public static IXSubsetType getInstance(){
		if (instance == null) {
			instance = new XFlatSubsetType();
		}
		return instance;
	}
	
	public XFlatSubsetType() {
		super("flat");
	}


}
