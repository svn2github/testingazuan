package com.tensegrity.palowebviewer.modules.widgets.client.list;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.util.client.Assertions;


/**
 * Default implementation of {@link IListModel}.
 *
 */
public class DefaultListModel implements IListModel
{

    protected final ListModelListnerCollection listeners = new ListModelListnerCollection();

    private final List itemList = new ArrayList();

    public DefaultListModel () {

    }

    public void addListModelListener(IListModelListener listener) {
        listeners.addListener(listener);
    }

    public Object getElementAt(int index) {
        return itemList.get(index);
    }

    public int getSize() {
        return itemList.size();
    }

    public void removeListModelListener(IListModelListener listener) {
        listeners.removeListener(listener);
    }

    public void addItem(int index, Object item) {
        addItems(index, new Object[]{item});
    }

    public void addItems(int index, Object[] items) {
        final int index0 = index;
        final int index1 = index+items.length - 1;
        for( int i = 0 ; i < items.length; i++ ) { 
            itemList.add(index0+i, items[i]);
        } 
        listeners.fireIntervalAdded(index0, index1);
    }

    public void removeItems(int index0, int index1) {
        if(index1 < index0)
            throw new IllegalArgumentException("Right index is less then left ("+index0+", "+index1+")");
        Assertions.assertMin(index0, 0, "Index");
        if(index1 >=getSize())
            throw new IndexOutOfBoundsException("Second index can not be greater then last index of list");

        final int size = index1 - index0+1;
        for( int i = 0 ; i < size ; i++ ) { 
            itemList.remove(index0);
        } 
        listeners.fireIntervalRemoved(index0, index1);
    }

    public void removeItem(int index) {
        removeItems(index, index);
    }

}
