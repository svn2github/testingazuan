package com.tensegrity.palowebviewer.modules.widgets.client.treecombobox;

import java.util.ArrayList;
import java.util.List;

/**
 * Listener collection for combobox view.
 *
 */
public class ComboboxViewListeners {
	
	private final List listenerList = new ArrayList();
	
	public void addListener(ICoboboxViewListener listener) {
		if(listener == null)
			throw new IllegalArgumentException("Listener can not be null.");
		listenerList.add(listener);
	}
	
	public void removeListener(ICoboboxViewListener listener) {
		listenerList.remove(listener);
	}
	
	public void fireOnDropdownOpen() {
		Object[] listeners = listenerList.toArray();
		for (int i = 0; i < listeners.length; i++) {
			ICoboboxViewListener listner = (ICoboboxViewListener)listeners[i];
			listner.onDropdownOpen();
		}
	}

}
