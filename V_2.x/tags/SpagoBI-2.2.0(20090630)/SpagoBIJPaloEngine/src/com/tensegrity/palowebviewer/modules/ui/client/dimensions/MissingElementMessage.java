package com.tensegrity.palowebviewer.modules.ui.client.dimensions;

import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserCallback;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessage;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;

public abstract class MissingElementMessage implements IUserMessage {
	
	private XObject context;
	private XElement element;
	private IUserCallback callback;
	
	public MissingElementMessage(XObject context, XElement element, IUserCallback callback) {
		this.context = context;
		this.element = element;
		this.callback = callback;
	}
	
	public MissingElementMessage(XObject context, XElement element) {
		this(context, element, null);
	}

	public IUserCallback getCallback() {
		return callback;
	}

	public abstract String getMessage();

	public IUserMessageType getType() {
		return IUserMessageType.ERROR;
	}

	public XObject getContext() {
		return context;
	}

	public XElement getElement() {
		return element;
	}
	
	public boolean equals(Object o) {
		boolean result = o instanceof MissingElementMessage;
		if(result) {
			MissingElementMessage another = (MissingElementMessage)o;
			result = context == another.context && element == another.element;
		}
		return result;
	}
	

}
