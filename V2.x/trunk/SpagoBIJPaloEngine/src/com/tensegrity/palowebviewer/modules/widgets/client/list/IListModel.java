package com.tensegrity.palowebviewer.modules.widgets.client.list;


/**
 * Plane list model of objects.
 *
 */
public interface IListModel
{

    /**
     * Adds a listener to the list that's notified each time a change to the data model occurs.
     */
    public void addListModelListener(IListModelListener listener);

    /**
     * Returns the value at the specified index.
     */
    public Object getElementAt(int index);

    /**
     * Returns the length of the list.
     */
    public int getSize();

    /**
     * Removes a listener from the list that's notified each time a change to the data model occurs.
     */
    public void removeListModelListener(IListModelListener listener);



}
