/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;

public class IndexNodeFinder {

    private HeaderTreeNode root;
    private int index;
    private HeaderTreeNode result;
    private int childCount;

    public void setRoot(HeaderTreeNode node, int childCount) {
        root = node;
        this.childCount = childCount;
    }

    public HeaderTreeNode findNode(int index) {
        this.index = index;
        result = null;
        scanChildren(root, 0, childCount);
        return result;
    }

    private void scanChildren(HeaderTreeNode node) {
        int size = node.getChildCount();
        int first = node.getSubTreeNodeCount();
        scanChildren(node, first, size);
    }

    private void scanChildren(HeaderTreeNode node, int i0, int i1) {
        for( int i = i0 ; i < i1 ; i++ ) { 
            scanNode(node.getChild(i));
        } 
    }

    private void scanNode(HeaderTreeNode node) {
        if(index == 0)
            result = result==null ? node : result;
        else {
            index--;
            scanChildren(node);
        }
    }

}