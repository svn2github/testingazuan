package com.tensegrity.palowebviewer.modules.widgets.client.combobox;

import com.tensegrity.palowebviewer.modules.widgets.client.list.DefaultListModel;


/**
 * Simple impelentation for {@link IListComboboxModel}.
 *
 */
public class ListComboboxModel extends DefaultListModel implements IListComboboxModel
{
    protected final ComboboxListenerCollection comboboxListners = new ComboboxListenerCollection();
    private Object selectedItem = null;

    public void addComboboxListener(IComboboxListener listener) {
        comboboxListners.addListener(listener);
    }

    public void removeComboboxListener(IComboboxListener listener) {
        comboboxListners.removeListener(listener);
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Object item) {
    	Object oldItem = this.selectedItem;
        this.selectedItem = item;
        comboboxListners.fireSelectionChanged(oldItem);
    }

}
