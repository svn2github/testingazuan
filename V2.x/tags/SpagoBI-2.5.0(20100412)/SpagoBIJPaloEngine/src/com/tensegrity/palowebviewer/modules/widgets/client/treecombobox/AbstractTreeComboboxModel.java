package com.tensegrity.palowebviewer.modules.widgets.client.treecombobox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tensegrity.palowebviewer.modules.widgets.client.combobox.IComboboxListener;


/**
 * Base implementation for {@link ITreeComboboxModel}.
 *
 */
public abstract class AbstractTreeComboboxModel implements ITreeComboboxModel
{

    private final List listeners = new ArrayList();

    public void addComboboxListener(IComboboxListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener can not be null.");
        listeners.add(listener);
    }

    public void removeComboboxListener(IComboboxListener listener) {
        listeners.remove(listener);
    }

    protected void fireSelectionChanged(Object oldItem) {
        for( Iterator it = listeners.iterator () ; it.hasNext (); ) { 
            IComboboxListener listener = (IComboboxListener)it.next();
            listener.onSelectionChanged(oldItem);
        } 
    }

}
