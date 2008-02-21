package com.tensegrity.palowebviewer.modules.engine.client.usermessage;

import java.util.ArrayList;
import java.util.List;

public class UserMessageQueue implements IUserMessageQueue {
	
	private final List queue = new ArrayList();
	private final QueueListenerCollection listeners = new QueueListenerCollection();
	private final CompositeProcessor processor = new CompositeProcessor();
	private final MessageFilter filter = new MessageFilter();

	public UserMessageQueue(){
		
	}
	
	public void addListener(IUserMessageQueueListener listener) {
		listeners.addListener(listener);
	}
	
	public void removeListener(IUserMessageQueueListener listener) {
		listeners.removeListener(listener);
	}

	public IUserMessage popMessage() {
		IUserMessage msg = null;
		if(!queue.isEmpty()) {
			msg = (IUserMessage) queue.remove(0);
			listeners.firePopMesage(msg);
		}
		return msg;
	}

	public void pushMessage(IUserMessage msg) {
		if(msg == null){
			throw new IllegalArgumentException("Message can't be null");
		}
		if(filter.acceptMessage(msg)){
			queue.add(msg);
			processor.processMessages(queue);
			listeners.firePushMesage(msg);
		}
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public void addProcessor(IMessageProcessor agregator) {
		this.processor.addProcessor(agregator);
	}

	public void removeProcessor(IMessageProcessor agregator) {
		this.processor.removeProcessor(agregator);
	}

	public MessageFilter getMessageFilter(){
		return filter;
	}

}


