package com.tensegrity.palowebviewer.modules.widgets.client.tree;

import com.tensegrity.palowebviewer.modules.widgets.client.dispose.IDisposable;


/**
 * Base class for {@link ITreeModel} proxy. It just redirects all calls to base model.
 *
 */
public abstract class ProxyTreeModel extends AbstractTreeModel implements IDisposable
{

    private final ITreeModel model;
    private final ITreeModelListener modelListener = new ITreeModelListener() {

        public void treeNodesChanged(TreeModelEvent e){
            subModelNodesChanged(e);
        }

        public void treeNodesInserted(TreeModelEvent e){
            subModelNodesInserted(e);
        }

        public void treeNodesRemoved(TreeModelEvent e){
            subModelNodesRemoved(e);
        }

        public void treeStructureChanged(TreeModelEvent e){
            subModelStructureChanged(e);
        }

    };

    public Object getChild(Object parent, int index) {
        return getTreeModel().getChild(parent, index);
    }

    public int getChildCount(Object parent) {
        return getTreeModel().getChildCount(parent);
    }

    public int getIndexOfChild(Object parent, Object child) {
        return getTreeModel().getIndexOfChild(parent, child);
    }
    
    public Object getRoot() {
        return getTreeModel().getRoot();
    }

    public boolean isLoaded(Object object) {
        return getTreeModel().isLoaded(object);
    }

    public void load(Object object) {
        getTreeModel().load(object);
    }

    public boolean isLeaf(Object node) {
        return getTreeModel().isLeaf(node);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        getTreeModel().valueForPathChanged(path, newValue);
    }

    public ITreeModel getTreeModel() {
        return model;
    }

    public ProxyTreeModel(ITreeModel model){
        if(model == null)
            throw new IllegalArgumentException("Model can not be null");
        this.model = model;
        this.model.addTreeModelListener(modelListener);
    }

    protected void subModelNodesChanged(TreeModelEvent e) {
    }

    protected void subModelNodesInserted(TreeModelEvent e) {
    }

    protected void subModelNodesRemoved(TreeModelEvent e) {
    }

    protected void subModelStructureChanged(TreeModelEvent e) {
    }

	public void dispose() {
		model.removeTreeModelListener(modelListener);
	}
    
}
