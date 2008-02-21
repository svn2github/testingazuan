package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class VerificationRequest implements AsyncCallback {
	
	private final IVerificationCallback callback;
	private final XObject context;
	private final XElement element;

	public VerificationRequest(XObject context, XElement element, IVerificationCallback callback) {
		if(callback == null)
			throw new IllegalArgumentException("Callback can not be null");
		this.callback = callback;
		this.context = context;
		this.element = element;
	}
	
	public void sendRequest(IEngineServiceAsync service) {
		XPath path = context.constructPath();
		service.checkExistance(path, element.getId(), this);
	}

	public String getName() {
		return "VerificationCallbackTask";
	}

	public void onFailure(Throwable caught) {
		Logger.error(caught+"");
	}

	public void onSuccess(Object result) {
		if(((Boolean)result).booleanValue()) {
			callback.successed();
		}
		else {
			callback.fail();
		}
	}

}
