package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;

public interface IDimensionListListener {
	
	public void dimensionAdded(IDimensionList list, IDimensionModel model);
	
	public void dimensionRemoved(IDimensionList list, IDimensionModel model);
	
	public void dimensionMoved(IDimensionList list, IDimensionModel model);

}
