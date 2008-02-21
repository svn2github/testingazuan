package com.tensegrity.palowebviewer.modules.widgets.client.combobox;


/**
 * Listener for the {@link IComboboxModel} selected item selection.
 *
 */
public interface IComboboxListener
{

    public void onSelectionChanged(Object oldItem);
    
    public void onInvalidItemSet(Object item);

}
