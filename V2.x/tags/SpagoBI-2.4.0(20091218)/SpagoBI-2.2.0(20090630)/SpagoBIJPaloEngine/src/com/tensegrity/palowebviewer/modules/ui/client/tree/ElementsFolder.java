/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class ElementsFolder extends FolderNode {

    public String getFolderName() {
        return "Elements";
    }

    public ElementsFolder(PaloTreeModel model, XDimension dimension) {
        super(model, dimension);
    }
    
    public XDimension getDimension() {
    	return (XDimension)getXObject();
    }

    protected XObject[] getXObjectChildren() {
        XDimension dimension = (XDimension)getValue();
        return dimension.getNodes();
    }

    protected PaloTreeNode createNode(XObject obj) {
        return new ElementNodeNode(getPaloTreeModel(), (XElementNode)obj);
    }
    
    protected int getChildType() {
		return IXConsts.TYPE_ELEMENT_NODE;
	}

}