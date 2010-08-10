package com.tensegrity.palowebviewer.modules.widgets.client.combobox;



/**
 * Base implementation for {@link IComboboxModel}.
 *
 */
public abstract class AbstractComboboxModel implements IComboboxModel
{

    protected final ComboboxListenerCollection listenerCollection = new ComboboxListenerCollection();

    public void addComboboxListener(IComboboxListener listener) {
        listenerCollection.addListener(listener);
    }

    public void removeComboboxListener(IComboboxListener listener) {
        listenerCollection.removeListener(listener);
    }
    
}
