package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessage;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageQueue;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageQueueListener;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageType;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class UserMessageQueueLogger implements IUserMessageQueueListener{
	
	private final IUserMessageQueue queue;

	public UserMessageQueueLogger(IUserMessageQueue queue) {
		this.queue = queue;
		this.queue.addListener(this);
	}

	public void onMessagePop(IUserMessage msg) {
		
	}

	public void onMessagePush(IUserMessage msg) {
		IUserMessageType type = msg.getType();
		Logger.info("(USER MESSAGE)[" + type.getName() + "] "+msg.getMessage());
	}
	
}
