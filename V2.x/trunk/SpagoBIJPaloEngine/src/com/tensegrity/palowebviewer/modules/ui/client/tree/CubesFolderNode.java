/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class CubesFolderNode extends FolderNode {

    public String getFolderName() {
        return "Cubes";
    }

    public CubesFolderNode(PaloTreeModel model, XDatabase database) {
        super(model, database);
    }

    protected XObject[] getXObjectChildren() {
        XDatabase database = (XDatabase)getValue();
        return database.getCubes();
    }

    protected PaloTreeNode createNode(XObject obj) {
        return new CubeNode(getPaloTreeModel(), (XCube)obj);
    }
    
    public int getChildType() {
    	return IXConsts.TYPE_CUBE;
    }

}