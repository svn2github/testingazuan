package com.tensegrity.palowebviewer.modules.engine.client.usermessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class QueueListenerCollection {
	
	private final List liseners = new ArrayList();
	
	
	public void addListener(IUserMessageQueueListener listener) {
		if(listener == null){
			throw new IllegalArgumentException("listener can't be null");
		}
		liseners.add(listener);

	}

	public void removeListener(IUserMessageQueueListener listener) {
		liseners.remove(listener);
	}

	public void firePushMesage(IUserMessage msg){
		for (Iterator it = liseners.iterator(); it.hasNext();) {
			IUserMessageQueueListener item = (IUserMessageQueueListener) it.next();
			item.onMessagePush(msg);
		}
	}

	public void firePopMesage(IUserMessage msg){
		for (Iterator it = liseners.iterator(); it.hasNext();) {
			IUserMessageQueueListener item = (IUserMessageQueueListener) it.next();
			item.onMessagePop(msg);
		}
	}
}
