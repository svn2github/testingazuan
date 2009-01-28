package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageQueue;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.UserMessage;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.util.client.ICallback;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class CheckViewExistsCallback implements AsyncCallback {

	private XView view;
	private PaloServerModel serverModel;
	private ICallback successCallback;
	
	
	public CheckViewExistsCallback(PaloServerModel serverModel, XView view) {
		if(view == null) {
			throw new IllegalArgumentException("view can not be null");
		}
		if(serverModel == null) {
			throw new IllegalArgumentException("Server model can not be null");
		}
		this.view = view;
		this.serverModel = serverModel;
	}
	
	public void onFailure(Throwable caught) {
		Logger.error("CheckViewExistsCallback:"+caught);
	}

	public void onSuccess(Object result) {
		Boolean exists = (Boolean)result;
		if(!exists.booleanValue()) {
			Logger.debug("CheckViewExistsCallback: "+view+" does not exist");
			serverModel.saveView(view, successCallback);
		}
		else {
			Logger.debug("CheckViewExistsCallback: "+view+" exists");
			IUserMessageQueue messageQueue = serverModel.getUserMessageQueue();
			String message = "View '"+view.getName()+"' already exists. Can not save";
			messageQueue.pushMessage(UserMessage.createErrorMsg(message));
		}
	}
	
	 public void send(IEngineServiceAsync service){
		 service.checkExistance(view.constructPath(), this);
     }

	public void setSuccessCallback(ICallback successCallback) {
		this.successCallback = successCallback;
	}
	
}
