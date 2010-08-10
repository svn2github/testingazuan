package com.tensegrity.palowebviewer.modules.widgets.client.combobox;



/**
 * Model of widget that can have one selected Item.
 *
 */
public interface IComboboxModel
{

    public void addComboboxListener(IComboboxListener listener);

    public void removeComboboxListener(IComboboxListener listener);

    public Object getSelectedItem();

    public void setSelectedItem(Object node);

}
