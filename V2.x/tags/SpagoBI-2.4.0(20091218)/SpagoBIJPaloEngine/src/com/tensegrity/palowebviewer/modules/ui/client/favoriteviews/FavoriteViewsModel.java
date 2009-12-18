package com.tensegrity.palowebviewer.modules.ui.client.favoriteviews;

import com.tensegrity.palowebviewer.modules.paloclient.client.XFavoriteNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XFolder;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.NodeTreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreePath;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.NodeTreeModel.TreeNode;

public class FavoriteViewsModel extends NodeTreeModel{
	
	
	private final StructureCreator structureCreator;

	public FavoriteViewsModel() {
		this.structureCreator = new StructureCreator(this);
		setStructure(new XFolder());
	}
	
	public void setStructure(XFolder rootFolder) {
		FavoriteNode root = structureCreator.createStructure(rootFolder);
		updateStructure(root);
//		setRoot(root);
	}

	private void updateStructure(FavoriteNode root) {
		FavoriteNode oldRoot = (FavoriteNode)getRoot();
		if(oldRoot == null) {
			setRoot(root);
		}
		else {
			compareAndUpdate(oldRoot,root);
		}
		
	}

	private void compareAndUpdate(FavoriteNode oldNode, FavoriteNode node) {
		XFavoriteNode favoriteNode = node.getFavoriteNode();
		oldNode.setFavoriteNode(favoriteNode);
		final int size = node.getChildCount();
		if(oldNode.getChildCount() == size) {
			for( int i = 0; i< size; i++) {
				FavoriteNode child1 = (FavoriteNode)oldNode.getChild(i);
				FavoriteNode child2 = (FavoriteNode)node.getChild(i);
				compareAndUpdate(child1, child2);
			}
		}
		else {
			oldNode.removeAll();
			for(int i = 0; i < size; i ++) {
				TreeNode child = node.getChild(i);
				oldNode.addChild(child);
			}
		}
	}

	private boolean isChildrenEqual(FavoriteNode oldNode, FavoriteNode node) {
		int size = oldNode.getChildCount();
		boolean result = size == node.getChildCount();
		for(int i = 0; i < size && result; i++) {
			Object child1 = oldNode.getChild(i);
			Object child2 = node.getChild(i);
			result = child1.equals(child2);
		}
		return result;
	}

	public static abstract class FavoriteNode extends TreeNode {

		private XFavoriteNode node;
		private final FavoriteViewsModel model;
		
		public FavoriteNode(XFavoriteNode node, FavoriteViewsModel model) {
			model.super();
			this.model = model;
			if(node == null)
				throw new IllegalArgumentException("Node can not be null.");
			setFavoriteNode(node);
		}

		protected XFavoriteNode getFavoriteNode() {
			return node;
		}
		
		protected void setFavoriteNode(XFavoriteNode value) {
			boolean changed = !equals(node,value);
			node = value;
			if(changed && isInTree()) {
				TreeNode parent = getParent();
				TreePath path = null;
				int[] indices = null;
				if(parent != null) {
					path = parent.getPath();
					indices = new int[]{parent.indexOf(this)};
					
				}
				else {
					path = new TreePath();
				}
				model.fireTreeNodesChanged(path, indices);
			}
		}

		public String getName() {
			return node.getName();
		}
		
		public boolean isLoaded() { 
			return true;
		}
		
		public boolean equals(Object o){
			boolean result = false;
			if(o instanceof FavoriteNode) {
				XFavoriteNode node = getFavoriteNode();
				XFavoriteNode node2 = ((FavoriteNode)o).getFavoriteNode();
				result = node == null ? node2 == null : node.equals(node2);
			}
			return result;
		}

		private boolean equals(XFavoriteNode node1, XFavoriteNode node2) {
			return node1 == null ? node2 == null : node1.equals(node2);
		}

	}
	
	
}


