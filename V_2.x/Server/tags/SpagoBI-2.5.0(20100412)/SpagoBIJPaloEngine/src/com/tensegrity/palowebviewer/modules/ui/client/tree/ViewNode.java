/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class ViewNode extends PaloTreeNode {

    public boolean isLeaf() {
        return true;
    }

    public ViewNode(PaloTreeModel model, XView view) {
        super(model, view);
    }

    protected void loadChildren() {
    }

	protected PaloTreeNode createNode(XObject obj) {
		return null;
	}

	protected XObject[] getXObjectChildren() {
		return null;
	}
	
	protected int getChildType() {
		return -1;
	}

}