package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.engine.client.AbstractServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModelListener;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;


public class XCubeLoader extends AbstractCubeLoader{
	
	private final IPaloServerModelListener serverModelListener = new AbstractServerModelListener () {

		public void onChildArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
			if(cube == path[path.length -1] && type == IXConsts.TYPE_DIMENSION) {
				unsubscribe();
				executeCallback();
			}
		}
		
	};
	private XCube cube;
	
	public void load() {
		cube = getCube();
		if(cube.getDimensions() == null) {
			subscribe();
			getServerModel().loadChildren(cube, IXConsts.TYPE_DIMENSION);
		}
		else {
			executeCallback();
		}
	}

	protected void unsubscribe() {
		getServerModel().removeListener(serverModelListener);
		
	}

	private void subscribe() {
		getServerModel().addListener(serverModelListener);
	}

	public String getDescription() {
		return "XCubeLoader";
	}

}
