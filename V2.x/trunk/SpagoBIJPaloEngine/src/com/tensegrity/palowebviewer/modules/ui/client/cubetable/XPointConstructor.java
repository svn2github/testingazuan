/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IHaveCoordinates;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.MutableXPoint;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;

class XPointConstructor extends QueryConstructor{

    private MutableXPoint point;

    public IHaveCoordinates getQuery() {
        return point;
    }

    public MutableXPoint getXPoint(HeaderTreeNode xNode, HeaderTreeNode yNode){
        point = new MutableXPoint();
        queryNodeParents(xNode);
        queryNodeParents(yNode);
        addNodeToQuery(xNode);
        addNodeToQuery(yNode);
        querySliceDimensions();
        return point;
    }

    public XPointConstructor(ICubeTableModel model) {
    	super(model);
    }

}