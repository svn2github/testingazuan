package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.XViewLink;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;

public class ViewLoader implements IChildLoaderCallback {
	
	private final String viewId;
	private final PaloServerModel model;
	private ILoadViewCallback callback;
	private final XViewLink viewLink;
	private final IChildLoadCallback loadCallback = new IChildLoadCallback() {

		public void onChildLoaded(XObject object) {
			XView view = (XView)object;
			onViewLoaded(view);			
		}
		
	};

	public ViewLoader(PaloServerModel model, XViewLink link) {
		this.model = model;
		this.viewId = link.getViewId();
		this.viewLink = link;
	}

	public void onChildLoaded(XObject xObject) {
		XCube cube = (XCube)xObject;
		XView[] views = cube.getViews();
		if(views == null) {
			model.loadView(cube, viewId, loadCallback);
		}
		else {
			XView view = (XView)XArrays.findById(views,viewId);
			onViewLoaded(view);
		}
	}

	private void onViewLoaded(XView view) {
		if(callback != null) {
			callback.onViewLoaded(viewLink, view);
		}
	}

	public void setCallback(ILoadViewCallback callback) {
		this.callback = callback;
	}

}
