package com.tensegrity.palowebviewer.modules.ui.client.loaders;

public interface ILoader {
	
	public void load();
	
	public void setCallback(ILoaderCallback callback);

}
