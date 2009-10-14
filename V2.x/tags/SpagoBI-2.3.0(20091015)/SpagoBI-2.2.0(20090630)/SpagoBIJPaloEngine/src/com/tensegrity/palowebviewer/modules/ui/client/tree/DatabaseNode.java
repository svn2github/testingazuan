/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class DatabaseNode extends PaloTreeNode {

    private CubesFolderNode cubesFolder;
    private DimensionsFolderNode dimensionsFolder;

    public CubesFolderNode getCubesFolder() {
        if(cubesFolder == null)
            cubesFolder = new CubesFolderNode(getPaloTreeModel(), (XDatabase)getValue());
        return cubesFolder;
    }

    public DimensionsFolderNode getDimensionsFolder() {
        if(dimensionsFolder == null)
            dimensionsFolder = new DimensionsFolderNode(getPaloTreeModel(), (XDatabase)getValue());
        return dimensionsFolder;
    }

    public DatabaseNode(PaloTreeModel model, XDatabase database) {
        super(model, database);
    }

    public void load() {
        addChild(getDimensionsFolder());
        addChild(getCubesFolder());
    }
    
    public boolean isInitialized() {
    	return getChildCount() > 0;
    }

    public void childrenChanged(XObject[] oldChildren, int type) {
    	FolderNode folder = null;
    	switch(type) {
    	case IXConsts.TYPE_CUBE: {
    		folder = getCubesFolder();
    		break;
    	}
    	case IXConsts.TYPE_DIMENSION: {
    		folder = getDimensionsFolder();
    	}
    	}
    	if(folder != null)
    		folder.childrenChanged(oldChildren, type);
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