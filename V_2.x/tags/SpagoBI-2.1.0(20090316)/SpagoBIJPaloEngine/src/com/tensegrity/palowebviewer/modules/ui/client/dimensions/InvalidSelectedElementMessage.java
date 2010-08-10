package com.tensegrity.palowebviewer.modules.ui.client.dimensions;

import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;

public class InvalidSelectedElementMessage extends MissingElementMessage{
	
	public InvalidSelectedElementMessage(XObject context, XElement element, IUserCallback callback) {
		super(context,element,callback);
	}
	
	public InvalidSelectedElementMessage(XObject context, XElement element) {
		this(context, element, null);
	}

	public String getMessage() {
		String message = "Selected element '" + getElement().getName()+"' is invalid for " + getContext().getName();
		return message;
	}
	
}
