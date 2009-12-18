package com.tensegrity.palowebviewer.modules.widgets.client.tree;

import com.tensegrity.palowebviewer.modules.util.client.Arrays;



/**
 * A kind of {@link ProxyTreeModel} that gives acces to only some subtree of the base model.
 * The root of the {@link SubTreeModel} can be any node of the base model. 
 *
 */
public class SubTreeModel extends ProxyTreeModel 
{

    private Object root;

    public Object getRoot() {
        return root;
    }

    public SubTreeModel(ITreeModel model, Object root){
        super(model);
        if(root == null)
            throw new IllegalArgumentException("Root can not be null.");
        setRoot(root);
    }

	public void setRoot(Object root) {
		if(this.root != root) {
			this.root = root;
			fireTreeStructureChanged();
		}
	}

    protected TreePath translatePath(TreePath path) {
    	TreePath result = null;
        if ( path == null ) {
        	result = new TreePath();
        }
        else {
        	Object root = getRoot();
        	Object[] pathArray = path.getPath();
        	int rootIndex = Arrays.indexOf(pathArray, root);
        	if(rootIndex >= 0){
        		result = creatSubPath(pathArray, rootIndex);
        	}
        }
        return result;
    }

	private TreePath creatSubPath(Object[] pathArray, int rootIndex) {
		TreePath result;
		Object[] newPath = new Object[pathArray.length - rootIndex];
		for( int i = rootIndex ; i < pathArray.length ; i++ ) { 
			newPath[i-rootIndex] = pathArray[i];
		} 
		result = new TreePath(newPath);
		return result;
	}

    protected TreeModelEvent translateEvent(TreeModelEvent event) {
        TreeModelEvent result = null;
        TreePath path = event.getTreePath();
        path = translatePath(path);
        if(path!= null){
            int[] childIndices = event.getChildIndices();
            Object[] children = event.getChildren();
            result = new TreeModelEvent(this, path, childIndices, children);
        }
        return result;
    }

    protected void subModelNodesChanged(TreeModelEvent e) {
        e = translateEvent(e);
        if(e != null)
            fireTreeNodesChanged(e);
    }

    protected void subModelNodesInserted(TreeModelEvent e) {
        e = translateEvent(e);
        if(e != null)
            fireTreeNodesInserted(e);
    }

    protected void subModelNodesRemoved(TreeModelEvent e) {
        e = translateEvent(e);
        if(e != null)
            fireTreeNodesRemoved(e);
    }

    protected void subModelStructureChanged(TreeModelEvent e) {
        e = translateEvent(e);
        if(e != null)
            fireTreeStructureChanged(e);
    }

}
