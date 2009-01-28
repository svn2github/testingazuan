package com.tensegrity.palowebviewer.modules.paloclient.client;

public class XManualHierarchySubsetType extends XSubsetType {

	private static IXSubsetType instance = null;
	
	public static IXSubsetType getInstance(){
		if (instance == null) {
			instance = new XManualHierarchySubsetType();
		}
		return instance;
	}

	public XManualHierarchySubsetType() {
		super("manual-hierarchy");
	}

}
