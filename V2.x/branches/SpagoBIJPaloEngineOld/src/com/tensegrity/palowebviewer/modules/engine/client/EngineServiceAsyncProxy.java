package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XViewPath;
import com.tensegrity.palowebviewer.modules.util.client.Assertions;

public class EngineServiceAsyncProxy implements IEngineServiceAsync {

    private final IEngineServiceAsync engineService;

    protected IEngineServiceAsync getEngineService() {
        return engineService;
    }

    public EngineServiceAsyncProxy(IEngineServiceAsync engineService) {
        Assertions.assertNotNull(engineService, "engineService");
        this.engineService = engineService;
    }

    public void authenticate(String login, String password, boolean remember, AsyncCallback callback) {
        engineService.authenticate(login, password, remember, getCallbackProxy("authenticate", callback));
    }

    public void checkExistance(XPath contextPath, String elementId, AsyncCallback callback) {
        engineService.checkExistance(contextPath, elementId, getCallbackProxy("checkExistance", callback));
    }
    
	public void checkExistance(XPath path, AsyncCallback callback) {
		engineService.checkExistance(path, callback);
	}
	
    public void forceReload(AsyncCallback callback) {
        engineService.forceReload(getCallbackProxy("forceReload", callback));
    }

    public void getClientProperties(AsyncCallback callback) {
        engineService.getClientProperties(getCallbackProxy("getClientProperties", callback));
    }

    public void getParentsOf(XPath contextPath, String elementId,	AsyncCallback callback) {
        engineService.getParentsOf(contextPath, elementId, getCallbackProxy("getParentsOf", callback));
    }

    public void isAuthenticated(AsyncCallback callback) {
        engineService.isAuthenticated(getCallbackProxy("isAuthenticated", callback));
    }

    public void loadChildren(XPath path, int type, AsyncCallback callback) {
        engineService.loadChildren(path, type, getCallbackProxy("loadChildren", callback));
    }

    public void loadDefaultView(XPath path, AsyncCallback callback) {
        engineService.loadDefaultView(path, getCallbackProxy("loadDefaultView", callback));
    }

    public void logoff(AsyncCallback callback) {
        engineService.logoff(getCallbackProxy("logoff", callback));
    }

    public void query(XQueryPath[] query, AsyncCallback callback) {
        engineService.query(query, getCallbackProxy("query", callback));
    }

    public void saveView(XViewPath viewPath, AsyncCallback callback) {
        engineService.saveView(viewPath, getCallbackProxy("saveView", callback));
    }

    public void updateData(XPath cubePath, IXPoint point, IResultElement value,	AsyncCallback callback) {
        engineService.updateData(cubePath, point, value, getCallbackProxy("updateData", callback));
    }

	public void loadFavoriteViews(AsyncCallback callback) {
        engineService.loadFavoriteViews(getCallbackProxy("loadFavoriteViews", callback));
	}

	public void loadChild(XPath path, String childId, int type, AsyncCallback callback) {
        engineService.loadChild(path, childId, type, getCallbackProxy("loadChild", callback));
	}

	public void loadChildByName(XPath path, String name, int type, AsyncCallback callback) {
        engineService.loadChildByName(path, name, type, getCallbackProxy("loadChildByName", callback));
	}

    protected AsyncCallback getCallbackProxy(String methodName, AsyncCallback callback) {
        return callback;
    }

    protected static class AsyncCallbackProxy implements AsyncCallback {

        private final AsyncCallback callback;
        
        protected AsyncCallback getCallback() {
        	return callback;
        }

        public AsyncCallbackProxy(AsyncCallback callback) {
            this.callback = callback;
        }

        public void onFailure(Throwable caught) {
        	AsyncCallback callback = getCallback();
            if(callback != null)
                callback.onFailure(caught);
        }

        public void onSuccess(Object result) {
        	AsyncCallback callback = getCallback();
            if(callback != null)
                callback.onSuccess(result);			
        }
    }

}
