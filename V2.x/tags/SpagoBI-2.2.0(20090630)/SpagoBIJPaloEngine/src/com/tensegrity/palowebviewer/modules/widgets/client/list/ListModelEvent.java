package com.tensegrity.palowebviewer.modules.widgets.client.list;


/**
 * Event that denotes changes in the {@link IListModel}.
 *
 */
public class ListModelEvent
{

    private final int index0;
    private final int index1;

    public int getIndex0(){
        return index0;
    }

    public int getIndex1(){
        return index1;
    }

    public ListModelEvent (int index0, int index1) {
        this.index0 = index0;
        this.index1 = index1;
    }

}
