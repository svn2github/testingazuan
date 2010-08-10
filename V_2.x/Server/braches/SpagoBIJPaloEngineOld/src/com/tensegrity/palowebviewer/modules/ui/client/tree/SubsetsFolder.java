/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class SubsetsFolder extends FolderNode {

    public String getFolderName() {
        return "Subsets";
    }

    public SubsetsFolder(PaloTreeModel model, XDimension dimension) {
        super(model, dimension);
    }

    protected XObject[] getXObjectChildren() {
        XDimension dimension = (XDimension)getValue();
        return dimension.getSubsets();
    }

    protected PaloTreeNode createNode(XObject obj) {
        return new SubsetNode(getPaloTreeModel(), (XSubset)obj);
    }
    
    protected int getChildType() {
		return IXConsts.TYPE_SUBSET;
	}

}