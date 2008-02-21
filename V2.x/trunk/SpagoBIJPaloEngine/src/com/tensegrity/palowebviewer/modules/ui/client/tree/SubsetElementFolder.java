package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class SubsetElementFolder extends FolderNode {

	public SubsetElementFolder(PaloTreeModel model, XSubset object) {
		super(model, object);
	}

	public String getFolderName() {
		return "Elements";
	}
	
	public XSubset getSubset() {
		return (XSubset)getXObject();
	}

	protected PaloTreeNode createNode(XObject obj) {
        return new ElementNodeNode(getPaloTreeModel(), (XElementNode)obj);
	}

	protected int getChildType() {
		return IXConsts.TYPE_ELEMENT_NODE;
	}

	protected XObject[] getXObjectChildren() {
		return getSubset().getNodes();
	}

}
