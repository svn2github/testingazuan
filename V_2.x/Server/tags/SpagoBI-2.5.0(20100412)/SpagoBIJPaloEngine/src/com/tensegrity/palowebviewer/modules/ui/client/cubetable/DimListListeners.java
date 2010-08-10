package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;

public class DimListListeners {
	
	private final List listenerList = new ArrayList();
	private final IDimensionList dimList;
	
	
	public DimListListeners(IDimensionList list) {
		this.dimList = list;
	}
	
	public void addListener(IDimensionListListener listener) {
		if(listener == null)
			throw new IllegalArgumentException("Listener can not be null.");
		listenerList.add(listener);
	}
	
	public void removeListener(IDimensionListListener listener) {
		listenerList.remove(listener);
	}
	
	public void fireDimensionAdded (IDimensionModel dimModel) {
		Object[] listeners = listenerList.toArray();
		for (int i = 0; i < listeners.length; i++) {
			IDimensionListListener listener = (IDimensionListListener)listeners[i];
			listener.dimensionAdded(dimList, dimModel);
		}
	}
	
	public void fireDimensionRemoved (IDimensionModel dimModel) {
		Object[] listeners = listenerList.toArray();
		for (int i = 0; i < listeners.length; i++) {
			IDimensionListListener listener = (IDimensionListListener)listeners[i];
			listener.dimensionRemoved(dimList, dimModel);
		}
	}
	
	public void fireDimensionMoved (IDimensionModel dimModel) {
		Object[] listeners = listenerList.toArray();
		for (int i = 0; i < listeners.length; i++) {
			IDimensionListListener listener = (IDimensionListListener)listeners[i];
			listener.dimensionMoved(dimList, dimModel);
		}
	}

	
}
