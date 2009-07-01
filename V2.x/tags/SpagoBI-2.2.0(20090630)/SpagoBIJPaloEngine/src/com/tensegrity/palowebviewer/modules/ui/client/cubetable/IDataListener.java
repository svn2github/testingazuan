package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;

public interface IDataListener {
	
	public void cellChanged(int row, int column, IResultElement value);

}
