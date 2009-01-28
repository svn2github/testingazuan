package com.tensegrity.palowebviewer.modules.ui.client.dimensions;

import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel;
import com.tensegrity.palowebviewer.modules.ui.client.tree.SubsetsFolder;
import com.tensegrity.palowebviewer.modules.widgets.client.combobox.ListComboboxModel;
import com.tensegrity.palowebviewer.modules.widgets.client.dispose.IDisposable;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModelListener;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreeModelEvent;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreePath;

public class SubsetComboboxModel extends ListComboboxModel implements ISubsetListModel, IDisposable {
	
	private final SubsetsFolder subsetFolder;
	private final PaloTreeModel paloTreeModel;
	private final ITreeModelListener treeModelListener = new ITreeModelListener() {

		public void treeNodesChanged(TreeModelEvent e) {
			handleEvent(e);
		}

		private void handleEvent(TreeModelEvent event) {
			Object changedNode = event.getTreePath().getLastPathComponent();
			if(changedNode == subsetFolder)
				resetList();
		}

		public void treeNodesInserted(TreeModelEvent e) {
			handleEvent(e);
		}

		public void treeNodesRemoved(TreeModelEvent e) {
			handleEvent(e);
		}

		public void treeStructureChanged(TreeModelEvent e) {
			handleEvent(e);
		}
		
	};
	
	protected void resetList() {
		removeAll();
		XSubset[] subsets = getDimension().getSubsets();
        if(subsets != null)
        	addItems(0, subsets);
        addNoneSubset();
	}

	private void addNoneSubset() {
		addItem(0, null);
	}

	protected void removeAll() {
		int last = this.getSize()-1;
		if(last>=0)
			this.removeItems(0, last);
	}
	
	protected XDimension getDimension() {
		return (XDimension)subsetFolder.getXObject();
	}
	
	public SubsetComboboxModel(SubsetsFolder subsetFolder) {
		this.subsetFolder = subsetFolder;
		paloTreeModel = subsetFolder.getPaloTreeModel();
		paloTreeModel.addTreeModelListener(treeModelListener);
		resetList();
	}

	public boolean isLoaded() {
		return subsetFolder.isLoaded();
	}

	public void load() {
		subsetFolder.load();
	}

	public void dispose() {
		paloTreeModel.removeTreeModelListener(treeModelListener);
	}

}
