package com.tensegrity.palowebviewer.modules.engine.client.usermessage;

public interface IUserMessage {
	public String getMessage();
	public IUserMessageType getType();
	public IUserCallback getCallback();
}
