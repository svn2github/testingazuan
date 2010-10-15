package com.tensegrity.palowebviewer.modules.widgets.client.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tensegrity.palowebviewer.modules.util.client.Logger;

/**
 * Kind of {@link ProxyTreeModel} that can filter out some nodes from the base {@link ITreeModel}.
 *
 */
public class FilterTreeModel extends ProxyTreeModel {

    private IObjectAcceptor filter;

    private boolean filtering = false;
    private final Map cache = new HashMap();
    private final List cachedNodes = new ArrayList();

    public FilterTreeModel(ITreeModel model) {
        super(model);
    }
    
    public void dispose() {
		cache.clear();
		super.dispose();
	}

    public void setFilter(IObjectAcceptor value) {
        this.filter = value;
    }

    public IObjectAcceptor getFilter() {
        return this.filter;
    }

    public void setFiltering(boolean value) {
        if(filtering != value) {
            this.filtering = value;
            rebuildTree();
        }
    }

    public boolean isFiltering() {
        return filtering && (getFilter() != null);
    }

    public Object getRoot() {
        Object result = super.getRoot();
        return result;
    }

    public int getChildCount(Object parent) {
        final int size = super.getChildCount(parent);
        int result = size;
        if(isFiltering()) {
            result = getChildren(parent).size();
        }
        return result;
    }

    public boolean isLeaf(Object node) {
        boolean result = super.isLeaf(node);
        if(!result && isLoaded(node))
            result = getChildCount(node)==0;
        return result;
    }

    public Object getChild(Object parent, int index) {
        Object result = null;
        if(isFiltering()){
            List children = getChildren(parent);
            result = children.get(index);
        }
        else 
            result = getTreeModel().getChild(parent, index);

        return result;
    }

    public int getIndexOfChild(Object parent, Object child) {
        int result = -1;
        if(isFiltering())
            result = getChildren(parent).indexOf(child);
        else
            result = super.getIndexOfChild(parent, child);
        return result;
    }

    protected boolean isVisible(Object node) {
        boolean result = true;
        if(isFiltering()) {
            IObjectAcceptor filter = getFilter();
            result = filter.accept(node);
        }
        return result;
    }

    protected List getChildren(Object parent) {
        List result = (List)cache.get(parent);
        if(result == null) {
            result = new ArrayList();
            cache.put(parent, result);
        }
        return result; 
    }
    
    protected boolean containNode(Object node) {
    	return cache.keySet().contains(node);
    }
    
    protected int[] getChildrenIndices(Object parent) {
    	final List children = getChildren(parent);
    	final int size = children.size();
    	final int[] result = new int[children.size()];
    	ITreeModel model = getTreeModel();
    	for (int i = 0; i < size; i++) {
			Object child = children.get(i);
			result[i] = model.getIndexOfChild(parent, child);
		}
    	return result;
    }
    
    
    protected Object[] translatePath(Object[] path) {
    	List result = new ArrayList();
    	for (int i = 0; i < path.length; i++) {
    		Object node = path[i];
    		if(isVisible(node))
    			result.add(node);
		}
   		return result.toArray();
    	
    }

    protected void clearCache(){
        cache.clear();
    }

    protected void rebuildTree() {
        clearCache();
        TreeUtil.visitTree(getTreeModel(), new TreeBuilder());
        fireTreeStructureChanged();
    }
    
    protected void clearSubTreeCache(Object node) {
    	List children = getChildren(node);
    	final int size = children.size();
    	for (int i = 0; i < size; i++) {
    		Object child = children.get(i);
			clearSubTreeCache(child);
    		cachedNodes.remove(child);
		}
    	cache.remove(node);
    }

    protected void rebuildSubTree(Object[] path) {
    	if(path == null || path.length == 0)
    		rebuildTree();
    	else {
    		Object node = path[path.length -1];
        	clearSubTreeCache(node);
        	TreeUtil.visitTree(getTreeModel(), new TreeBuilder(), node);
        	fireTreeStructureChanged(new TreePath(path));
    	}
    }
    
    protected boolean isVisible(Object[] path) {
    	boolean result = true;
    	for (int i = 0; (i < path.length) && result; i++) {
    		Object node = path[i];
    		result = isVisible(node);
		}
    	return result;
    }

    protected void subModelNodesChanged(TreeModelEvent e) {
    	if(isFiltering()){
    		Logger.debug("subModelNodesChanged("+e+")");
            Object[] path = e.getPath();
            int[] indices = e.getChildIndices();
            if(path != null && indices != null) {
                if(isVisible(path)) {
                	indices = translateIndices(path, indices);
                	if(indices.length >0)
                		fireTreeNodesChanged(e.getTreePath(), indices);
                }
            }else{
            	rebuildTree();
            }
        }
        else {
            fireTreeNodesChanged(e);
        }
        
    }

	protected void subModelNodesInserted(TreeModelEvent e) {
    	Logger.debug("subModelNodesInserted("+e+")");
    	subModelStructureChanged(e);
    }

    protected void subModelNodesRemoved(TreeModelEvent e) {
    	Logger.debug("subModelNodesRemoved("+e+")");
    	subModelStructureChanged(e);
    }

    protected void subModelStructureChanged(TreeModelEvent e) {
        if(isFiltering()){
            Logger.debug("subModelStructureChanged("+e+")");
            Object[] path = e.getPath();
            if(path != null) {
                if(isVisible(path)) {
                	rebuildSubTree(path);
                }
            }else{
            	rebuildTree();
            }
        }
        else {
            TreePath path = e.getTreePath();
            fireTreeStructureChanged(path);
        }
    }

    private int[] translateIndices(Object[] path, int[] indices) {
    	int[] result = new int[indices.length];
    	Object parent = path[path.length-1];
    	ITreeModel baseModel = getTreeModel();
    	int filteredChildren = 0;
    	for (int i = 0; i < indices.length; i++) {
			Object child = baseModel.getChild(parent, indices[i]);
			result[i] = getIndexOfChild(parent, child);
			if(result[i] < 0 ) {
				filteredChildren ++;
			}
		}
    	if(filteredChildren >0){
    		int[] tmp = result;
    		result = new int[tmp.length - filteredChildren];
    		int j = 0;
    		for (int i = 0; i < tmp.length; i++) {
				int index = tmp[i];
				if(index >=0){
					result[j] = index;
					j++;
				}
			}
    	}
		return result;
	}

    private class TreeBuilder implements ITreeVisitor{

        public void visitNode(Object parent, Object node) {
            if(parent == null)
                return;//node is root and we do not need to ad it anywhere
            if(isVisible(node)) {
                if(!isVisible(parent)){
                    parent = getRoot();
                }
                if(isInCache(parent)) {
                	getChildren(parent).add(node);
                	cachedNodes.add(node);
                }
            }
        }

        private boolean isInCache(Object node) {
        	boolean result = cachedNodes.contains(node) || getTreeModel().getRoot() == node;
        	return result;
		}

		public boolean hasFinished() {
            return false;
        }

		public void leaveNode(Object parent, Object node) {
		}

    }

}
