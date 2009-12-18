package com.tensegrity.palowebviewer.modules.ui.client.loaders;

public interface ILoaderChain extends IChainLoader {
	
	public void addLoader(IChainLoader loader);
	
}
