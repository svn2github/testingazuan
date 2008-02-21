package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserCallback;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessage;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageType;

public class ServiceCallFailMessage implements IUserMessage {
	
	private final Throwable throwable;

	public ServiceCallFailMessage(Throwable t) {
		this.throwable = t;
	}

	public IUserCallback getCallback() {
		return null;
	}

	public String getMessage() {
		String message = "Application may behave incorrectly.\n Call fail on server.\n";
		if(throwable != null)
			message += "Reason: "+ throwable.getMessage();
		return message;
	}

	public IUserMessageType getType() {
		return IUserMessageType.ERROR;
	}

}
