package com.tensegrity.palowebviewer.modules.ui.client;

import java.util.ArrayList;
import java.util.List;


public class UIManagerListenerCollection implements IUIManagerListener {
	
	private final List listenerList = new ArrayList();
	
	public void addListener(IUIManagerListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Listener can not be null.");
		}
		listenerList.add(listener);
	}
	
	public void removeListener(IUIManagerListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Listener can not be null.");
		}
		listenerList.add(listener);
	}
	
	
	public void onBusy() {
		Object[] listeners = listenerList.toArray();
		for (int i = 0; i < listeners.length; i++) {
			IUIManagerListener listener = (IUIManagerListener)listeners[i];
			listener.onBusy();
		}
	}

	public void onFree() {
		Object[] listeners = listenerList.toArray();
		for (int i = 0; i < listeners.length; i++) {
			IUIManagerListener listener = (IUIManagerListener)listeners[i];
			listener.onFree();
		}

	}

}
