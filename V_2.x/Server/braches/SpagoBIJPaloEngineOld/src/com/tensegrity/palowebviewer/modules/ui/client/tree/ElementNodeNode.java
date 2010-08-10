package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class ElementNodeNode extends PaloTreeNode implements IXConsts {
	
	
    public boolean isLeaf() {
    	boolean result = true;
    	XElementNode elementNode = getElementNode();
		XElement element = elementNode.getElement();
		XElementNode[] nodeChildren = elementNode.getChildren();
		if(nodeChildren == null) {
			result = !XElementType.isConsolidated(element);
		}
		else {
			result = nodeChildren.length == 0;
		}
		return  result;
	}

	public ElementNodeNode(PaloTreeModel model, XElementNode node) {
        super(model, node);
    }

	protected PaloTreeNode createNode(XObject obj) {
		return new ElementNodeNode(getPaloTreeModel(),(XElementNode)obj);
	}

	protected int getChildType() {
		return IXConsts.TYPE_ELEMENT_NODE;
	}

	protected XObject[] getXObjectChildren() {
		XElementNode node = getElementNode();
		return node.getChildren();
	}

	public XElementNode getElementNode() {
		XElementNode node = (XElementNode)getXObject();
		return node;
	}
	
	public XElement getElement() {
		return getElementNode().getElement();
	}

}
