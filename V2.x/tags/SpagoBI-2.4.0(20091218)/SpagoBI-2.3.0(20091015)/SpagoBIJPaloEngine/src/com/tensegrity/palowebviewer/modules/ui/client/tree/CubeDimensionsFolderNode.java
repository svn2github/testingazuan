/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class CubeDimensionsFolderNode extends FolderNode {

    public String getFolderName() {
        return "Cube Dimensions";
    }

    public CubeDimensionsFolderNode(PaloTreeModel model, XCube cube) {
        super(model, cube);
    }

    protected XObject[] getXObjectChildren() {
        XCube cube = (XCube)getValue();
        return cube.getDimensions();
    }

    protected PaloTreeNode createNode(XObject obj) {
        return new CubeDimensionNode(getPaloTreeModel(), (XDimension)obj);
    }

	protected int getChildType() {
		return IXConsts.TYPE_DIMENSION;
	}

}