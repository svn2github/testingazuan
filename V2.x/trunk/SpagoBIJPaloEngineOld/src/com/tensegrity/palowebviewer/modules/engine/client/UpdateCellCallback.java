/**
 * 
 */
package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class UpdateCellCallback implements AsyncCallback{
	
	private final XPath cube;
	private final IXPoint point;
	private final IResultElement value;
	private final ICellUpdateCallback callback;
	private IErrorCallback errorCallback;

    public UpdateCellCallback(XPath cube, IXPoint point, IResultElement value, ICellUpdateCallback callback){
        this.cube = cube;
        this.point = point;
        this.value = value;
        this.callback = callback;
    }

    public void onFailure(Throwable caught) {
    	Logger.error(""+caught);
    	if(errorCallback!= null)
    		errorCallback.onError(caught);

    	if(callback != null) {
    		callback.onFinish(cube, point, value, false);
    	}
    }

    public void onSuccess(Object result) {
    	if(callback != null) {
    		callback.onFinish(cube, point, value, true);
    	}
    }

    public void sendCellUpdate(IEngineServiceAsync service){
        service.updateData(cube, point, value, this);
    }

	public void setErrorCallback(IErrorCallback errorCallback) {
		this.errorCallback = errorCallback;
	}
	
}