/**
 * 
 */
package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XViewPath;
import com.tensegrity.palowebviewer.modules.util.client.ICallback;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

class SaveViewCallback implements AsyncCallback {
	
	private final XView view;
	private final PaloServerModelListenerCollection listeners;
	private CubeViewCache cubeViewCache;
	private ICallback successCallback;

    public SaveViewCallback(XView view, PaloServerModelListenerCollection listeners) {
        if(view == null)
            throw new IllegalArgumentException("View can not be null");
        this.view = view;
        this.listeners = listeners;
    }
    
    public void setCubeViewCache(CubeViewCache cubeViewCache) {
    	this.cubeViewCache = cubeViewCache;
    }

    public void send(IEngineServiceAsync service){
        XViewPath path = new XViewPath(view);
        service.saveView(path, this);

    }

    public void onFailure(Throwable caught) {
       Logger.error("fail to save view");
    }
    
    public void onSuccess(Object result) {
    	XView[] views = getViews();
    	//to get id for newly created views
    	String viewId = (String) result;
    	view.setId(viewId);
    	if(views != null) {
    		if(!checkPresence(views)) {
    			insertView();
    		}
    	}
    	else {
    		putToCache();
    	}
    	executeCallback();
    }

	private void executeCallback() {
		if(successCallback != null)
			successCallback.execute();
		
	}

	private void putToCache() {
		cubeViewCache.getView(getCube(), view);
	}

	private XView[] getViews() {
		return getCube().getViews();
	}

	private XCube getCube() {
		return (XCube)view.getParent();
	}

	private void insertView() {
		XCube cube = getCube();
		XView[] views = getViews();
		XView[] newViews = new XView[views.length+1];
		for( int i = 0 ; i < views.length ; i++ ) { 
		    newViews[i] = views[i];
		} 
		newViews[views.length] = view;
		cube.setViews(newViews);
		putToCache();
		listeners.fireChildArrayChanged(cube.getPathArray(), views, IXConsts.TYPE_VIEW);
	}

	private boolean checkPresence(XView[] views) {
		boolean result = false;
        for( int i = 0 ; !result && (i < views.length); i++ ) {
            result = views[i] == view;
        }
		return result;
	}

	public void setSuccessCallback(ICallback successCallback) {
		this.successCallback = successCallback;
	}

}