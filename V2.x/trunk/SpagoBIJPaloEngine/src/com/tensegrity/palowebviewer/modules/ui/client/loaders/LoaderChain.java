package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import java.util.ArrayList;
import java.util.List;

public class LoaderChain extends AbstractLoader implements ILoaderChain {

	private final List loaders = new ArrayList();
	private final ILoaderCallback loaderCallback = new ILoaderCallback() {

		public void loaded() {
			executeNextLoader();
		}
		
	};
	private int chainIndex;

	public void addLoader(IChainLoader loader) {
		if(loader == null)
			throw new IllegalArgumentException("loader can not be null");
		loaders.add(loader);
		loader.setCallback(loaderCallback);
	}

	public void load() {
		resetChain();
		executeNextLoader();
	}

	protected void executeNextLoader() {
		if(chainIndex < loaders.size()){
			ILoader loader = getLoader(chainIndex);
			chainIndex++;
			loader.load();
		}
		else {
			executeCallback();
		}
	}
	

	private ILoader getLoader(int index) {
		return (ILoader)loaders.get(index);
	}

	protected void resetChain() {
		chainIndex = 0;
	}
	
	public String getDescription() {
		return "LoaderChain";
	}

}
