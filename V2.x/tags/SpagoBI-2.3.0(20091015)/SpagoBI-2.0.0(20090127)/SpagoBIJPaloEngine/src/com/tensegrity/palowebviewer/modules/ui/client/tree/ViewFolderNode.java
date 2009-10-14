/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class ViewFolderNode extends FolderNode {

    public String getFolderName() {
        return "Views";
    }

    public ViewFolderNode(PaloTreeModel model, XCube cube) {
        super(model, cube);
    }

    protected XObject[] getXObjectChildren() {
        XCube cube = (XCube)getValue();
        return cube.getViews();
    }

    protected PaloTreeNode createNode(XObject obj) {
        return new ViewNode(getPaloTreeModel(), (XView)obj);
    }

    protected int getChildType() {
		return IXConsts.TYPE_VIEW;
	}
    
}