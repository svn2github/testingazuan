package com.tensegrity.palowebviewer.modules.engine.client.usermessage;

public interface IUserMessageQueueListener {
	
	public void onMessagePush(IUserMessage msg);
	
	public void onMessagePop(IUserMessage msg);
	
}
