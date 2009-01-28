package com.tensegrity.palowebviewer.modules.engine.client.usermessage;

import java.util.ArrayList;
import java.util.List;

public class CompositeProcessor implements IMessageProcessor {
	
	
	private List processors = new ArrayList();

	public void addProcessor(IMessageProcessor proc) {
		if(proc == null)
			throw new IllegalArgumentException("Agregator can not be null.");
		this.processors .add(proc);
	}
	
	public void removeProcessor(IMessageProcessor proc) {
		processors.remove(proc);
	}

	public void processMessages(List list) {
		Object[] procArray = processors.toArray();
		for (int i = 0; i < procArray.length; i++) {
			IMessageProcessor proc = (IMessageProcessor)procArray[i];
			proc.processMessages(list);
		}

	}

}
