/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class SubsetNode extends PaloTreeNode {

    private SubsetElementFolder elementsFolder;

    public SubsetNode(PaloTreeModel model, XSubset dimension) {
        super(model, dimension);
        getElementsFolder();
    }
    
    public XDimension getDimension() {
    	return (XDimension)getXObject();
    }

    public SubsetElementFolder getElementsFolder() {
        if(elementsFolder == null){
            elementsFolder = new SubsetElementFolder(getPaloTreeModel(), (XSubset)getValue());
            addChild(elementsFolder);
        }
        return elementsFolder;
    }


    protected void loadChildren() {
        getElementsFolder().loadChildren();
    }

    public boolean isInitialized() {
        return true;
    }

    public void reinit() {
        getElementsFolder().reinit();
    }

    public void childrenChanged(XObject[] oldChildren, int type) {
    	FolderNode folder = null;
    	switch(type) {
    	case IXConsts.TYPE_ELEMENT_NODE: {
    		folder = getElementsFolder();
    		break;
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