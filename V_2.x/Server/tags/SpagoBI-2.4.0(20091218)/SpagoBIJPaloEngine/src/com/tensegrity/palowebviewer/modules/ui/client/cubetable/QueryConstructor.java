/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.Iterator;

import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IHaveCoordinates;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;

abstract class QueryConstructor {

    public abstract IHaveCoordinates getQuery();
    
    private final ICubeTableModel cubeTableModel;
    
    public QueryConstructor(ICubeTableModel model) {
    	this.cubeTableModel = model;
    }

protected void queryNodeParents(HeaderTreeNode node) {
    int layerNr = node.getLayerNr();
    HeaderTreeNode parent = node;
    while (layerNr>0) {
        while(parent.getLayerNr()== layerNr){
            parent = parent.getParent();
        }
        addNodeToQuery(parent);
        layerNr--;
    }
}

protected void addNodeToQuery(HeaderTreeNode node) {
    XDimension dimension = node.getDimension();
    XElement element = node.getElement();
    getQuery().addCoordinate(dimension, element);
}

protected void queryChildren(HeaderTreeNode node) {
    if(node == null)
        return;
    int size = node.getChildCount();
    for( int i = 0 ; i < size ; i++ ) { 
        HeaderTreeNode child = node.getChild(i);
        queryNode(child);
    } 
}

protected void queryForHeader(CubeHeaderModel header) {
    HeaderTreeNode root = header.getHeaderRoot();
    queryChildren(root);
} 

protected void queryNode(HeaderTreeNode node) {
    addNodeToQuery(node);
    if(node.isOpen()) {
        queryChildren(node);
    }
    else {
        int size = node.getSubTreeNodeCount();
        for( int i = 0 ; i < size ; i++ ) { 
            HeaderTreeNode child = node.getChild(i);
            queryNode(child);
        } 
    }
} 

protected void querySliceDimensions() {
    for( Iterator it = cubeTableModel.getSliceDimensions().iterator () ; it.hasNext (); ) { 
        IDimensionModel dimModel = (IDimensionModel)it.next();
        XDimension dim = dimModel.getDimension();
        XElement element = dimModel.getSelectedElement();
        getQuery().addCoordinate(dim, element);
    } 
}

protected void querySubTreeChildren(HeaderTreeNode node) {
    if(node == null)
        return;
    int size = node.getSubTreeNodeCount();
    for( int i = 0 ; i < size ; i++ ) { 
        HeaderTreeNode child = node.getChild(i);
        queryNode(child);
    } 
}

protected void queryThisLayerChildren(HeaderTreeNode node) {
    int offset = node.getSubTreeNodeCount();
    int size = node.getChildCount();
    for( int i = offset; i < size ; i++ ) { 
        HeaderTreeNode child = node.getChild(i);
        queryNode(child);
    } 
}

public ICubeTableModel getCubeTableModel() {
	return cubeTableModel;
}


}