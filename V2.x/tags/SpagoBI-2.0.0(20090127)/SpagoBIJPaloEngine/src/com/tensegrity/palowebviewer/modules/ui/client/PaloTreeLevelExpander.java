package com.tensegrity.palowebviewer.modules.ui.client;

import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;
import com.tensegrity.palowebviewer.modules.widgets.client.ITreeViewItem;
import com.tensegrity.palowebviewer.modules.widgets.client.TreeView;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModelListener;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreeModelEvent;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.ICoboboxViewListener;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.TreeCombobox;

public class PaloTreeLevelExpander {
	
	private int level;
	private final TreeCombobox combobox;
	private final ICoboboxViewListener viewListener = new ICoboboxViewListener () {

		public void onDropdownOpen() {
			if(treeModelListener == null) {
				treeModelListener = new TreeModelListener();
				getTreeView().getTreeModel().addTreeModelListener(treeModelListener);
			}
			expandLevels();
		}
		
	};
	private ITreeModelListener treeModelListener = null; 
	public PaloTreeLevelExpander(TreeCombobox combobox, int level) {
		this.combobox = combobox;
		this.level = level;
		combobox.addListener(viewListener);
		
	}
	
	protected TreeCombobox getTreeCombobox() {
		return combobox;
	}
	
	public int getLevel() {
		return level;
	}
	
	protected void expandLevels() {
		TreeView item = getTreeView();
		boolean selectExpanded = item.isSelectExpanded();
		try {
			item.setSelectExpanded(false);
			expandDown(item, getLevel());
		}
		finally {
			item.setSelectExpanded(selectExpanded);
		}
	}
	
	protected void expandDown(ITreeViewItem item, int level) {
		if(level>0) {
			item.setState(true);
			PaloTreeNode node = (PaloTreeNode)item.getNode();
			if(node.isInitialized()) {
				int size = item.getChildCount();
				for(int i = 0; i < size; i++) {
					ITreeViewItem child = item.getChildItem(i);
					expandDown(child, level -1);
				}
			}
		}
	}

	private TreeView getTreeView() {
		return getTreeCombobox().getTreeView();
	}

	private final class TreeModelListener implements ITreeModelListener {

		public void treeNodesChanged(TreeModelEvent e) {}

		public void treeNodesInserted(TreeModelEvent e) {}

		public void treeNodesRemoved(TreeModelEvent e) {}

		public void treeStructureChanged(TreeModelEvent e) {
			Object[] path = e.getPath();
			if(path == null || path.length <= getLevel())
				expandLevels();
		}
		
	}

	
}
