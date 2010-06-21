package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.INodeStateListener;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModelListener;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreeModelEvent;

public class HeaderLevelExpander {
	
	private final CubeHeaderModel header;
	private IExpanderCallback callback;
	private final int level;
	private boolean expanded = false;
	private ITreeModelListener treeModelListener = new ITreeModelListener () {

		public void treeNodesChanged(TreeModelEvent e) {}
		public void treeNodesInserted(TreeModelEvent e) {}
		public void treeNodesRemoved(TreeModelEvent e) {}
		public void treeStructureChanged(TreeModelEvent e) {
			expandLevels();
		}
		
	};
	private INodeStateListener nodeStateListener = new INodeStateListener() {

		public void nodeStateChanged(HeaderTreeNode node) {
			expandLevels();
		}
		
	};
	private XDimension expandOnlyDim;

	public void setCallback(IExpanderCallback callback) {
		this.callback = callback;
		
	}

	public HeaderLevelExpander(CubeHeaderModel header, int level, IExpanderCallback callback) {
		this.header = header;
		this.level = level;
		setCallback(callback);
		subscribe();
	}
	
	private void subscribe() {
		final int size = header.getTreeModelCount();
		for(int i = 0; i< size; i++) {
			ITreeModel treeModel = header.getTreeModel(i);
			treeModel.addTreeModelListener(treeModelListener);
		}
		header.addNodeStateListener(nodeStateListener);
	}
	
	private void unsubscribe() {
		final int size = header.getTreeModelCount();
		for(int i = 0; i< size; i++) {
			ITreeModel treeModel = header.getTreeModel(i);
			treeModel.removeTreeModelListener(treeModelListener);
		}
		header.removeNodeStateListener(nodeStateListener);
	}

	public int getLevel() {
		return level;
	}
	
	public boolean isExpanded() {
		return expanded;
	}
	
	public void expand() {
		expanded = false;
		expandLevels();
	}
	
	protected void expandLevels() {
		if(!expanded) {
			expanded = true;
			HeaderTreeNode root = header.getHeaderRoot();
			if(root!=null) {
				expandDown(root, getLevel());
			}
			else {
				Logger.warn("ExpandLevels: root node was null");
			}
			if(isExpanded()) {
				unsubscribe();
				executeCallback();
			}
		}
	}

	private void executeCallback() {
		if(callback != null)
			callback.expanded();
	}

	protected void expandDown(HeaderTreeNode node, int level) {
		if(level > 0 && node.isExpandable()) {
			if(needExpand(node)) {
				node.setOpen(true);
				expanded &= node.isOpen();
			}
			if(node.isOpen()) {
				expandThisLayerChildren(node, level);
			}
		}
		expandNextLayerChildren(node);
	}

	protected boolean needExpand(HeaderTreeNode node) {
		return expandOnlyDim == null || node.getDimension() == expandOnlyDim;
	}

	private void expandThisLayerChildren(HeaderTreeNode node, int level) {
		int size = node.getChildCount();
		int subTreeNodeCount = node.getSubTreeNodeCount();
		for(int i= subTreeNodeCount; i< size; i++) {
			HeaderTreeNode child = node.getChild(i);
			expandDown(child, level -1);
		}
	}

	private void expandNextLayerChildren(HeaderTreeNode node) {
		int subTreeNodeCount = node.getSubTreeNodeCount();
		for(int i = 0; i< subTreeNodeCount; i++) {
			HeaderTreeNode child = node.getChild(i);
			expandDown(child, getLevel()-1);
		}
	}

	public void setExpandOnly(XDimension dimension) {
		expandOnlyDim = dimension;
	}

}
