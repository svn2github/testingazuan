/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class DimensionsFolderNode extends FolderNode {

    public String getFolderName() {
        return "Dimensions";
    }

    public DimensionsFolderNode(PaloTreeModel model, XDatabase database) {
        super(model, database);
    }

    protected XObject[] getXObjectChildren() {
        XDatabase database = (XDatabase)getValue();
        return database.getDimensions();
    }

    protected PaloTreeNode createNode(XObject obj) {
        return new DimensionNode(getPaloTreeModel(), (XDimension)obj);
    }
    
    public int getChildType() {
    	return IXConsts.TYPE_DIMENSION;
    }

}