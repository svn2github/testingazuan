/**
 * 
 */
package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.TaskQueue;

class LoadChildrenCallback implements AsyncCallback{

	private final TaskQueue taskQueue = TaskQueue.getInstance();
	private XObject object;
    private XObject[] path;
    private PerformanceTimer timer;

    private int type;
	private IInitCallback initCallback;
	private final PaloServerModel serverModel;
	private IErrorCallback errorCallback;
	private final LoadingMap loadingMap;
	private List callbackList = new ArrayList();

    public LoadChildrenCallback(PaloServerModel serverModel, XObject object, int type) {
        if(object == null)
            throw new IllegalArgumentException("Object can not be null");
        this.object = object;
        this.type = type;
        this.serverModel = serverModel;
        this.loadingMap = serverModel.getLoadingMap();
		path = object.getPathArray();
    }

    public void onFailure(Throwable caught) {
		loadingMap.setFinishLoading(object, type);
		serverModel.failUpdateHierarchy(caught);
		try {
			executeErrorCallback(caught);
		}
		finally {
			taskQueue.add(new UpdateCompleteTask(serverModel));
		}

    }

	private void executeErrorCallback(Throwable caught) {
		if(errorCallback != null)
			errorCallback.onError(caught);
		for (Iterator it = this.callbackList.iterator(); it.hasNext();) {
			LoadChildrenCallback loader = (LoadChildrenCallback) it.next();
			loader.executeErrorCallback(caught);
		}
	}

    public void onSuccess(Object result) {
    	timer.report();
    	XObject[] children = (XObject[])result;
        setChildren(children);
       
		try {
			executeInitCallback(children, taskQueue);
		}
		finally {
			taskQueue.add(new UpdateCompleteTask(serverModel));
		}
    }

	private void executeInitCallback(XObject[] children, TaskQueue taskQueue) {
		if(initCallback != null)
        	taskQueue.add(new CallInitCallbackTask(initCallback, children));
		for (Iterator it = this.callbackList.iterator(); it.hasNext();) {
			LoadChildrenCallback loader = (LoadChildrenCallback) it.next();
			loader.executeInitCallback(children, taskQueue);
		}
	}
    
	private void setChildren(XObject[] children) {
		ITask task= new SetChildrenTask(serverModel, path, children, type);
		TaskQueue.getInstance().add(task);
	}

    public void sendQuerry(IEngineServiceAsync service) {
    	boolean loading = checkLoading();
        if(!loading) {
        	serverModel.startUpdateHierarchy();
            timer = new PerformanceTimer(this.toString());
            XPath xPath = prepareQuery();
            service.loadChildren(xPath, type, this);
            timer.start();
        }
        else {
        	LoadChildrenCallback loader = loadingMap.getLoader(object, type);
        	loader.addCallback(this);
        }
    }

	private void addCallback(LoadChildrenCallback callback) {
		this.callbackList .add(callback);
	}

	private XPath prepareQuery() {
		loadingMap.setLoading(object, type, this);
		return object.constructPath();
	}

	private boolean checkLoading() {
		return loadingMap.isLoading(object, type);
	}

    public String toString() {
        return "InitObjectCallback["+object+"]";
    }
	public void setErrorCallback(IErrorCallback errorCallback) {
		this.errorCallback = errorCallback;
	}
	public void setInitCallback(IInitCallback initCallback) {
		this.initCallback = initCallback;
	}

}