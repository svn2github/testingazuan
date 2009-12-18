package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;


public interface ICubeTableModelListener 
{

	/**
	 * called when some nodes are expanded/collapsed; dimensions added/removed from axises
	 * 
	 */
	public void modified();
	
    public void structureChanged();

    public void updateFinished();
    
    public void onModelDispose();
    
    public void zStateChanged(boolean value);
    
    public void topChildrenChanged(ITreeModel model, Object parent);
    
    public void leftChildrenChanged(ITreeModel model, Object parent);
    
    public void topTreeNodeStateChanged(HeaderTreeNode node);
    
    public void leftTreeNodeStateChanged(HeaderTreeNode node);

	public void selectionChanged();

}
