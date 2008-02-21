package com.tensegrity.palowebviewer.modules.ui.client.dimensions;

import com.tensegrity.palowebviewer.modules.widgets.client.combobox.IListComboboxModel;

public interface ISubsetListModel extends IListComboboxModel {
	
	public boolean isLoaded();
	
	public void load();

}
