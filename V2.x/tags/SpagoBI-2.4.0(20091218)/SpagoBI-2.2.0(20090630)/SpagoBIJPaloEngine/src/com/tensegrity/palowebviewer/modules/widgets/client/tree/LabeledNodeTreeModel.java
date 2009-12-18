package com.tensegrity.palowebviewer.modules.widgets.client.tree;


/**
 * Tree model whose nodes are strings.
 *
 */
public class LabeledNodeTreeModel extends NodeTreeModel {
	
	public class TextNode extends TreeNode{
		private String label;
		
		public TextNode(String label){
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public boolean isLoaded() {
			return true;
		}

		public boolean isLeaf() {
			return getChildCount() == 0;
		}
		
	};
	
	public TextNode createNode(String label, TextNode parent){
		TextNode r = new TextNode(label);
		parent.addChild(r);
		return r;
	}
	
	public TextNode createNode(String label){
		TextNode r = new TextNode(label);
		return r;
	}
	
}
