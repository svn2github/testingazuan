package com.tensegrity.palowebviewer.modules.ui.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.widgets.client.DefaultLableFactory;

public class XObjectLabelFactory extends DefaultLableFactory {
	
	public String getLabel(Object object) {
		String result;
		if (object instanceof XObject) {
			XObject xObject = (XObject) object;
			result = xObject.getName();
		}
		else
			result = super.getLabel(object);
		return result;
	}

}
