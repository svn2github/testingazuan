/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.tree;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreePath;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.NodeTreeModel.TreeNode;

class TreePathConverter {

    private final List nodePath = new ArrayList();
    private TreeNode parentNode;
    private TreeNode node;
    private final PaloTreeModel treeModel;
    
    TreePathConverter(PaloTreeModel treeModel){
    	this.treeModel = treeModel;
    }

    public TreePath convert(XObject[] path) {
        init();
        for( int i = 1 ; i < path.length ; i++ ) { 
            node = parentNode.getChildByValue(path[i]);
            if(node == null)
            	tryNodesWithFolders(path[i]);
            if(node == null)
                throw new IllegalArgumentException("There was no TreePath for given XObject path("+path[i]+")");
            nodePath.add(node);
            parentNode = node;
        }
        return new TreePath(nodePath.toArray());
    }
    
    protected void tryNodesWithFolders(XObject object) {
    	if(parentNode instanceof DatabaseNode) {
    		DatabaseNode databaseNode = (DatabaseNode)parentNode;
    		node = getChildForDatabaseNode(databaseNode, object);
    	}
    	else if(parentNode instanceof DimensionNode) {
    		DimensionNode dimensionNode = (DimensionNode)parentNode;
    		node = getChildForDimensionNode(dimensionNode, object);
    	}
    	else if(parentNode instanceof SubsetNode) {
    		SubsetNode subsetNode = (SubsetNode)parentNode;
    		node = getChildForSubsetNode(subsetNode, object);
    	}
    	else if(parentNode instanceof CubeNode) {
    		CubeNode cubeNode = (CubeNode)parentNode;
    		node = getChildForCubeNode(cubeNode, object);
    	}
    }

    protected TreeNode getChildForDatabaseNode (DatabaseNode databaseNode, XObject xObject) {
        TreeNode node = databaseNode.getDimensionsFolder().getChildByValue(xObject);
        if(node != null)
            nodePath.add(databaseNode.getDimensionsFolder());
        else {
            node = databaseNode.getCubesFolder().getChildByValue(xObject);
            if(node != null)
                nodePath.add(databaseNode.getCubesFolder());
        }
        return node;
    }

    protected TreeNode getChildForDimensionNode(DimensionNode dimensionNode, XObject xObject) {
        TreeNode node = dimensionNode.getElementsFolder().getChildByValue(xObject);
        if(node != null)
            nodePath.add(dimensionNode.getElementsFolder());
        else {
            SubsetsFolder subsetsFolder = dimensionNode.getSubsetsFolder();
			node = subsetsFolder.getChildByValue(xObject);
            if(node != null)
                nodePath.add(subsetsFolder);
			else
				node = tryAddSubsetNode(dimensionNode, xObject, subsetsFolder);
        }
        return node;

    }
    
    protected TreeNode getChildForSubsetNode(SubsetNode subsetNode, XObject xObject) {
        SubsetElementFolder elementsFolder = subsetNode.getElementsFolder();
		TreeNode node = elementsFolder.getChildByValue(xObject);
        if(node != null)
            nodePath.add(elementsFolder);
        return node;
    }

	private TreeNode tryAddSubsetNode(DimensionNode dimensionNode, XObject xObject, SubsetsFolder subsetsFolder) {
		TreeNode node = null;
		if (xObject instanceof XSubset && !subsetsFolder.isInitialized()) {
			XSubset subset = (XSubset)xObject;
			if(dimensionNode.getXObject() == subset.getParent()) {
				node = new SubsetNode(treeModel, subset);
				subsetsFolder.addChild(node);
			}
		}
		return node;
	}

    protected TreeNode getChildForCubeNode(CubeNode cubeNode, XObject xObject) {
    	TreeNode node = null;
   		node = cubeNode.getCubeDimensionsFolder().getChildByValue(xObject);
       	if(node != null)
       		nodePath.add(cubeNode.getCubeDimensionsFolder());
       	else {
       		node = cubeNode.getViewFolder().getChildByValue(xObject);
       		if(node != null)
       			nodePath.add(cubeNode.getViewFolder());
       	}
        return node;
    }

    protected void init() {
        nodePath.clear();
        parentNode = treeModel.getRootNode();
        nodePath.add(parentNode);
    }

}