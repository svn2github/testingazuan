package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XViewPath;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;

public class EngineServiceAsyncLogger extends EngineServiceAsyncProxy {

    public void authenticate(String login, String password, boolean remember, AsyncCallback callback) {
        debug("authenticate( login='"+login+"', password='"+password+"', remember="+remember+")");
        super.authenticate(login, password, remember, callback);
    }

    public void checkExistance(XPath contextPath, String elementName, AsyncCallback callback) {
        debug("checkExistance( context="+contextPath+", elementName='"+elementName+"')");
        super.checkExistance(contextPath, elementName, callback);
    }
    
    public void checkExistance(XPath contextPath, AsyncCallback callback) {
        debug("checkExistance( "+contextPath+")");
        super.checkExistance(contextPath, callback);
    }

    public void forceReload(AsyncCallback callback) {
        debug("forceReload()");
        super.forceReload(callback);
    }

    public void getClientProperties(AsyncCallback callback) {
        super.getClientProperties(callback);
    }

    public void getParentsOf(XPath contextPath, String elementId, AsyncCallback callback) {
        super.getParentsOf(contextPath, elementId, callback);
    }

    public void isAuthenticated(AsyncCallback callback) {
        debug("isAuthenticated()");
        super.isAuthenticated(callback);
    }

    public void loadChildren(XPath path, int type, AsyncCallback callback) {
        debug("loadChildren( path="+path+", type="+XHelper.typeToString(type)+")");
        super.loadChildren(path, type, callback);
    }

    public void loadDefaultView(XPath path, AsyncCallback callback) {
        debug("loadDefaultView( path="+path+")");
        super.loadDefaultView(path, callback);
    }

    public void logoff(AsyncCallback callback) {
        debug("logoff()");
        super.logoff(callback);
    }

    public void query(XQueryPath[] query, AsyncCallback callback) {
        debug("query(query="+query[0]+")");
        super.query(query, callback);
    }

    public void saveView(XViewPath viewPath, AsyncCallback callback) {
        debug("saveView("+viewPath+")");
        super.saveView(viewPath, callback);
    }

    public void updateData(XPath cubePath, IXPoint point, IResultElement value, AsyncCallback callback) {
        debug("updateData(cubePath="+cubePath+", point="+point+", value="+value+")");
        super.updateData(cubePath, point, value, callback);
    }

    public EngineServiceAsyncLogger(IEngineServiceAsync engineService) {
        super(engineService);
    }

    protected AsyncCallback getCallbackProxy(String methodName, AsyncCallback callback) {
        return new AsyncCallbackLogger(callback, methodName);
    }

    protected void debug(String message){
        Logger.debug("[EngineServiceAsync]"+message);
    }

    public String toString() {
        return "EngineServiceAsyncLogger["+getEngineService()+"]";
    }

    private class AsyncCallbackLogger extends AsyncCallbackProxy {

        private final String methodName;
        private final PerformanceTimer sendTimer = new PerformanceTimer("");

        public AsyncCallbackLogger(AsyncCallback callback, String methodName){
            super(callback);
            this.methodName = methodName;
            sendTimer.start();
        }

        public void onFailure(Throwable caught) {
        	sendTimer.stop();
        	debug(methodName+"(): Fail: "+caught);
       		super.onFailure(caught);
        }

        public void onSuccess(Object result) {
        	sendTimer.stop();
        	debug(methodName+"(): OK result="+result);
       		super.onSuccess(result);
        }
        
        private void debug(String msg) {
        	String message = "[" + this.sendTimer.getDuration()+"ms";
        	message += "] "+msg;
        	EngineServiceAsyncLogger.this.debug(message);
        	
        }
    }

}
