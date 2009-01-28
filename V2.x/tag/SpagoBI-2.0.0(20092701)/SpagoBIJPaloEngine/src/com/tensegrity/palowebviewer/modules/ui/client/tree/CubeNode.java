/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class CubeNode extends PaloTreeNode {
    private CubeDimensionsFolderNode cubeDimensionsFolder;
    private ViewFolderNode viewFolder;

    public CubeDimensionsFolderNode getCubeDimensionsFolder() {
        if(cubeDimensionsFolder == null){
            cubeDimensionsFolder = new CubeDimensionsFolderNode(getPaloTreeModel(), (XCube)getValue());
            addChild(cubeDimensionsFolder);
        }
        return cubeDimensionsFolder;
    }

    public ViewFolderNode getViewFolder() {
        if(viewFolder == null) {
            viewFolder = new ViewFolderNode(getPaloTreeModel(), (XCube)getValue());
            addChild(viewFolder);
        }
        return viewFolder;
    }

    public CubeNode(PaloTreeModel model, XCube cube) {
        super(model, cube);
        getCubeDimensionsFolder();
        getViewFolder();
    }

    protected void loadChildren() {
    	reinit();
    }

    public void reinit() {
   		getCubeDimensionsFolder().reinit();
        getViewFolder().reinit();
    }

    public void childrenChanged(XObject[] oldChildren, int type) {
    	FolderNode folder = null;
    	switch(type) {
    	case IXConsts.TYPE_VIEW: {
    		folder = getViewFolder();
    		break;
    	}
    	case IXConsts.TYPE_DIMENSION: {
    		folder = getCubeDimensionsFolder();
    	}
    	}
    	if(folder != null)
    		folder.childrenChanged(oldChildren, type);
    }

    public boolean isInitialized() {
        return true;
    }

    protected XObject[] getXObjectChildren() {
        return new XObject[0];
    }

    protected PaloTreeNode createNode(XObject obj) {
        return null;
    }
    
	protected int getChildType() {
		return -1;
	}


}