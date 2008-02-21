package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tensegrity.palowebviewer.modules.engine.client.AbstractServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModelListener;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;

public class CubeDimensionsLoader extends AbstractCubeLoader {

	private boolean loading = false;
	private final IPaloServerModelListener serverModelListener = new AbstractServerModelListener() {

		public void onChildArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
			load();
		}
		
	};

	public void load() {
		List list = getDimensionsToLoad();
		if(list.size() > 0) {
			loadDimensions(list);
		}
		else {
			loading = false;
			unsubscribe();
			executeCallback();
		}
	}

	
	protected void loadDimensions(List list) {
		if(!loading) {
			loading  = true;
			subscribe();
			for (Iterator it = list.iterator(); it.hasNext();) {
				XDimension dimension = (XDimension) it.next();
				getServerModel().loadChildren(dimension, IXConsts.TYPE_ELEMENT_NODE);
			}
		}
		
	}

	private void subscribe() {
		getServerModel().addListener(serverModelListener);
	}
	
	private void unsubscribe() {
		getServerModel().removeListener(serverModelListener);
	}

	protected List getDimensionsToLoad() {
		XDimension[] dimensions = getDimensions();
		List result = new ArrayList();
		for (int i = 0; i < dimensions.length; i++) {
			if(dimensions[i].getNodes() == null)
				result.add(dimensions[i]);
		}
		return result;
	}

	protected XDimension[] getDimensions() {
		return getCube().getDimensions();
	}

	public String getDescription() {
		return "CubeDimensionsLoader";
	}

}
