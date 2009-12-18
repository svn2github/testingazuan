package com.tensegrity.palowebviewer.modules.ui.client.dimensions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tensegrity.palowebviewer.modules.engine.client.usermessage.CompositCallback;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserCallback;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessage;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;

public class CompositInvalidElementMessage implements IUserMessage {
	
	private final List messageList = new ArrayList();
	
	public void addMessage(InvalidSelectedElementMessage message) {
		if(!messageList.contains(message))
			messageList.add(message);
	}

	public IUserCallback getCallback() {
		CompositCallback callback = new CompositCallback();
		for (Iterator it = messageList.iterator(); it.hasNext();) {
			IUserMessage message = (IUserMessage) it.next();
			if(message.getCallback() != null)
				callback.addCallback(message.getCallback());
		}
		return callback;
	}

	public String getMessage() {
		String message = constructMessage();
		return message;
	}

	public IUserMessageType getType() {
		return IUserMessageType.ERROR;
	}

	private String constructMessage() {
		String result = "Element used for selection is missing in the following objects: \n";
		for (Iterator it = messageList.iterator(); it.hasNext();) {
			InvalidSelectedElementMessage message = (InvalidSelectedElementMessage) it.next();
			//XElement element = message.getElement();
			XObject contextObject = message.getContext();
			String context = contextToString(contextObject);
			result += context;
			if(it.hasNext()) 
				result += ", ";
		}
		result += ".\n The default element will be selected.";
		return result;
		
	}

	private String contextToString(XObject contextObject) {
		String context = "???";
		if (contextObject instanceof XSubset) {
			context = "subset '"+contextObject.getName()+"'";
			XObject parent = contextObject.getParent();
			if(parent != null)
				context += " of dimension '"+parent.getName()+"'";
		}
		else if (contextObject instanceof XDimension) {
			context = "dimension '"+contextObject.getName()+"'";
		}
		return context;
	}

}
