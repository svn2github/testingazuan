package com.tensegrity.palowebviewer.modules.widgets.client.combobox;

import java.util.ArrayList;
import java.util.List;


/**
 * Collection of {@link IComboboxListener} that can add/remove and notify listeners.
 *
 */
public class ComboboxListenerCollection
{

    private final List listenerList = new ArrayList();

    public void addListener(IComboboxListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener can not be null.");
        listenerList.add(listener);
    }

    public void removeListener(IComboboxListener listener) {
        listenerList.remove(listener);
    }

    public void fireSelectionChanged(Object oldItem) {
    	Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IComboboxListener listener = (IComboboxListener)listeners[i];
            listener.onSelectionChanged(oldItem);
        } 
    }
    
    public void fireInvalidItemSet(Object item) {
    	Object[] listeners = listenerList.toArray();
        for (int i = 0; i < listeners.length; i++) {
            IComboboxListener listener = (IComboboxListener)listeners[i];
            listener.onInvalidItemSet(item);
        } 
    }

}
