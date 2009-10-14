package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;

public class ObjectUpdaterListenerCollection implements IObjectUpdaterListener{
	
	private final List listenerList = new ArrayList();
	
	public void addListener(IObjectUpdaterListener listner) {
		if(listner == null)
			throw new IllegalArgumentException("Listener can not be null.");
		listenerList.add(listner);
	}
	
	public void removeListener(IObjectUpdaterListener listner) {
		listenerList.remove(listner);
	}
	
	public void childrenArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
		Object[] listeners = listenerList.toArray();
		for (int i = 0; i < listeners.length; i++) {
			IObjectUpdaterListener listener = (IObjectUpdaterListener) listeners[i];
			listener.childrenArrayChanged(path, oldChildren, type);
		}
	}

	public void objectRenamed(XObject object) {
		Object[] listeners = listenerList.toArray();
		for (int i = 0; i < listeners.length; i++) {
			IObjectUpdaterListener listener = (IObjectUpdaterListener) listeners[i];
			listener.objectRenamed(object);
		}
	}

}
