/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class DimensionNode extends PaloTreeNode {
    private ElementsFolder elementsFolder;
    private SubsetsFolder subsetsFolder;

    public DimensionNode(PaloTreeModel model, XDimension dimension) {
        super(model, dimension);
        getElementsFolder();
        getSubsetsFolder();
    }
    
    public XDimension getDimension() {
    	return (XDimension)getXObject();
    }

    public ElementsFolder getElementsFolder() {
        if(elementsFolder == null){
            elementsFolder = new ElementsFolder(getPaloTreeModel(), (XDimension)getValue());
            addChild(elementsFolder);
        }
        return elementsFolder;
    }

    public SubsetsFolder getSubsetsFolder() {
        if(subsetsFolder == null){
            subsetsFolder = new SubsetsFolder(getPaloTreeModel(), (XDimension)getValue());
            addChild(subsetsFolder);
        }
        return subsetsFolder;
    }

    protected void loadChildren() {
        getElementsFolder().loadChildren();
        getSubsetsFolder().loadChildren();
    }

    public boolean isInitialized() {
        return true;
    }

    public void reinit() {
        getElementsFolder().reinit();
        getSubsetsFolder().reinit();
    }

    public void childrenChanged(XObject[] oldChildren, int type) {
    	FolderNode folder = null;
    	switch(type) {
    	case IXConsts.TYPE_ELEMENT_NODE: {
    		folder = getElementsFolder();
    		break;
    	}
    	case IXConsts.TYPE_SUBSET: {
    		folder = getSubsetsFolder();
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