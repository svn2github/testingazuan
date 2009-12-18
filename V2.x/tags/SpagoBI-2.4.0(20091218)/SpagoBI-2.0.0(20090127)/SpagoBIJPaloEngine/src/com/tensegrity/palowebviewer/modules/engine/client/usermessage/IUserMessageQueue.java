package com.tensegrity.palowebviewer.modules.engine.client.usermessage;

public interface IUserMessageQueue {
	
	public void pushMessage(IUserMessage msg);
	
	/**
	 * 
	 * @return 1st message in queue. If there is no messages in queue null will be returned.
	 */
	public IUserMessage popMessage();
	
	public boolean isEmpty();
	
	public void addListener(IUserMessageQueueListener listener);
	
	public void removeListener(IUserMessageQueueListener listener);
	
	public void addProcessor(IMessageProcessor agregator);
	
	public void removeProcessor(IMessageProcessor agregator);
	
	public MessageFilter getMessageFilter();
}
