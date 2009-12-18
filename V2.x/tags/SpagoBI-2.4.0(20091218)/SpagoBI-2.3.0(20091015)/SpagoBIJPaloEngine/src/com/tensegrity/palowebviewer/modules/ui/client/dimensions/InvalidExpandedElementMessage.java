package com.tensegrity.palowebviewer.modules.ui.client.dimensions;

import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;

public class InvalidExpandedElementMessage extends MissingElementMessage{
	
	public InvalidExpandedElementMessage(XObject context, XElement element) {
		super(context, element);
	}

	public String getMessage() {
		String message = "Expanded element '" + getElement().getName()+"' is invalid for " + getContext().getName();
		return message;
	}
}
