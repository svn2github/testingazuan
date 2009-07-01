package com.tensegrity.palowebviewer.modules.widgets.client.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Collection of {@link IListModelListener} that can add/remove and notify listeners.
 *
 */
public class ListModelListnerCollection
{

    private final List listeners = new ArrayList();

    public void addListener(IListModelListener listener){
        if(listener == null)
            throw new IllegalArgumentException("Listener can not be null");
        listeners.add(listener);
    }

    public void removeListener(IListModelListener listener){
        listeners.remove(listener);
    }

    public void fireContentsChanged(ListModelEvent event) {
        for( Iterator it = listeners.iterator () ; it.hasNext (); ) { 
            IListModelListener listener = (IListModelListener)it.next();
            listener.contentsChanged(event);
        } 
    }

    public void fireIntervalAdded(ListModelEvent event) {
        for( Iterator it = listeners.iterator () ; it.hasNext (); ) { 
            IListModelListener listener = (IListModelListener)it.next();
            listener.intervalAdded(event);
        } 
    }

    public void fireIntervalRemoved(ListModelEvent event) {
        for( Iterator it = listeners.iterator () ; it.hasNext (); ) { 
            IListModelListener listener = (IListModelListener)it.next();
            listener.intervalRemoved(event);
        } 
    }

    public void fireContentsChanged(int i0, int i1) {
        final ListModelEvent event = new ListModelEvent(i0, i1);
        fireContentsChanged(event);
    }

    public void fireIntervalAdded(int i0, int i1) {
        final ListModelEvent event = new ListModelEvent(i0, i1);
        fireIntervalAdded(event);
    }

    public void fireIntervalRemoved(int i0, int i1) {
        final ListModelEvent event = new ListModelEvent(i0, i1);
        fireIntervalRemoved(event);
    }


}
