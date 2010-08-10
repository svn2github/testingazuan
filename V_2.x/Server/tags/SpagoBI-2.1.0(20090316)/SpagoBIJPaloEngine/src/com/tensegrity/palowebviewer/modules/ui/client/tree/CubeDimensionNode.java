/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;

public class CubeDimensionNode extends DimensionNode {

    public boolean isLeaf() {
        return true;
    }

    public CubeDimensionNode(PaloTreeModel model, XDimension dimension) {
        super(model, dimension);
    }

    public boolean  isLoaded() {
        return true;
    }

}