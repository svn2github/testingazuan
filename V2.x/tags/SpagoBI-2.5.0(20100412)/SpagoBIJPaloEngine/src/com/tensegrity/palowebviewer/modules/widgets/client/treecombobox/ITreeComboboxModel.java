package com.tensegrity.palowebviewer.modules.widgets.client.treecombobox;

import com.tensegrity.palowebviewer.modules.widgets.client.combobox.IComboboxModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;


/**
 * Combobox model that have tree instead of plain list of objects.
 *
 */
public interface ITreeComboboxModel extends IComboboxModel
{

    public ITreeModel getTreeModel();

}
