/**
 * 
 */
package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XResult;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;

public final class XQueryCallback implements AsyncCallback {

    private final XQueryPath[] queries;
    private final IQueryCallback callback;
    private PerformanceTimer timer;
    private IErrorCallback errorCallback;

    public XQueryCallback(XQueryPath[] queries, IQueryCallback callback) {
        if(queries == null)
            throw new IllegalArgumentException("Query can not be null");
        if(callback == null)
            throw new IllegalArgumentException("Callback can not be null");
        this.queries = queries;
        this.callback = callback;
    }

    public void onFailure(Throwable caught) {
    	Logger.error("XQueryCallback error " + caught);
    	caught.printStackTrace();
    	if(errorCallback != null)
    		errorCallback.onError(caught);
    }

    public void onSuccess(Object result) {
        timer.report();
        XResult[] xresult = (XResult[])result;
        callback.onSuccess(xresult);
    }

    public void sendQuerry(IEngineServiceAsync service) {
    	Logger.debug("quering : " + this.toString());
        timer = new PerformanceTimer(this.toString());
        PerformanceTimer sendTimer = new PerformanceTimer("XQueryPath::querry send time ");
        timer.start();
        sendTimer.start();
        service.query(queries, this);
        sendTimer.report();
    }

    public String toString() {
    	String r = "XQueryCallback[";
    	for (int i = 0; i < queries.length; i++) {
			r += "query " + queries[i] + "\n";
		}
    	r += "]";
        return r;
    }

	public void setErrorCallback(IErrorCallback errorCallback) {
		this.errorCallback = errorCallback;
	}

}