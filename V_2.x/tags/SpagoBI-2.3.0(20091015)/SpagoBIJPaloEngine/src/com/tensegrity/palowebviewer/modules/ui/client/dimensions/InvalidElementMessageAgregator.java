package com.tensegrity.palowebviewer.modules.ui.client.dimensions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IMessageProcessor;

public class InvalidElementMessageAgregator implements IMessageProcessor {
	List messageList = null;
	CompositInvalidElementMessage agregationMessage = null;

	public void processMessages(List list) {
		messageList = new ArrayList();
		agregationMessage = null;
		findMessages(list);
		agregate(list);
	}

	private void agregate(List list) {
		if(messageList.size() >0) {
			if(agregationMessage == null){
				agregationMessage = new CompositInvalidElementMessage();
				list.add(agregationMessage);
			}
			for (Iterator it = messageList.iterator(); it.hasNext();) {
				InvalidSelectedElementMessage message = (InvalidSelectedElementMessage) it.next();
				agregationMessage.addMessage(message);
			}
		}
	}

	private void findMessages(List list) {
		for (Iterator it = list.iterator(); it.hasNext();) {
			Object msg = it.next();
			if (msg instanceof InvalidSelectedElementMessage) {
				InvalidSelectedElementMessage message = (InvalidSelectedElementMessage) msg;
				messageList.add(message);
				it.remove();
			}
			else if (msg instanceof CompositInvalidElementMessage) {
				agregationMessage = (CompositInvalidElementMessage) msg;
			}
		}
	}

}
