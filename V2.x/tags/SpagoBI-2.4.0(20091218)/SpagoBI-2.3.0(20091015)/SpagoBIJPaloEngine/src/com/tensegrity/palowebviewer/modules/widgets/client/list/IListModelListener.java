package com.tensegrity.palowebviewer.modules.widgets.client.list;


/**
 * Listener for changes in the {@link IListModel}.
 *
 */
public interface IListModelListener
{

    /**
     * Sent when the contents of the list has changed in a way that's too complex to characterize with the previous methods.
     */
    public void contentsChanged(ListModelEvent event);

    /**
     * Sent after the indices in the index0,index1 interval have been inserted in the data model.
     */
    public void intervalAdded(ListModelEvent event);

    /**
     * Sent after the indices in the index0,index1 interval have been removed from the data model.
     */
    public void intervalRemoved(ListModelEvent event);




}
