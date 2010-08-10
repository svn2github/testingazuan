/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IHaveCoordinates;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;

class XQueryConstructor extends QueryConstructor{

    public XQueryConstructor(ICubeTableModel model) {
		super(model);
	}

	private XQueryPath path;
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void reset() {
    }

    public XQueryPath getPath() {
        return path;
    }

    public IHaveCoordinates getQuery() {
        return path;
    }

    public XQueryPath constructQuerry(HeaderTreeNode xNode, HeaderTreeNode yNode) {
        path = new XQueryPath(getCubeTableModel().getCube());
        x = 0;
        y = 0;
        queryXNode(xNode);
        queryYNode(yNode);
        querySliceDimensions();
        return path;
    }

	private void queryYNode(HeaderTreeNode yNode) {
		if(yNode.isLastLayer()) {
            y = yNode.getChild(0).getX();
            queryChildren(yNode);
        }
        else {
            y = yNode.getX();
            addNodeToQuery(yNode);
            querySubTreeChildren(yNode);
        }
        queryNodeParents(yNode);
	}

	private void queryXNode(HeaderTreeNode xNode) {
		if(xNode.isLastLayer()) {
            x = xNode.getChild(0).getX();
            queryChildren(xNode);
        }
        else {
            x = xNode.getX();
            addNodeToQuery(xNode);
            querySubTreeChildren(xNode);
        }
        queryNodeParents(xNode);
	}

}