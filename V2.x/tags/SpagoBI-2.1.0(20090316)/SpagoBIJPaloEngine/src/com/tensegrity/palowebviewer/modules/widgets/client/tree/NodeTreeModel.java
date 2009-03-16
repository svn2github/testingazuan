package com.tensegrity.palowebviewer.modules.widgets.client.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A kind of {@link ITreeModel} that contains {@link TreeNode} 
 * objects instead of just plain objects. That simpifies the development of {@link ITreeModel}
 * because developer need to implement different kind of {@link TreeNode}s not the complex 
 * structure in one {@link ITreeModel} class. 
 *
 */
public class NodeTreeModel extends AbstractTreeModel
{

    private TreeNode root;

    public class TreeNode {

        private TreeNode parent;
        private List children;
        private Object value;


        public boolean isInTree() {
            boolean result = false;
            for( TreeNode node = this; node != null ; node = node.getParent() ) { 
                result = (node == getRootNode());
                if(result)
                    break;
            } 
            return result;
        }

        public TreeNode getChildByValue(Object value) {
            List childList = getChildList();
            TreeNode result = null;
            for( Iterator it = childList.iterator () ; it.hasNext (); ) { 
                TreeNode child = (TreeNode)it.next();
                Object childValue = child.getValue();
                boolean match = value == null ? childValue==null : value.equals(childValue);
                if(match){
                    result = child;
                    break;
                }
            } 
            return result;
        }

        public boolean isLeaf() {
            return false;
        }

        public void addChild(TreeNode child) {
            if(child == null)
                throw new IllegalArgumentException("Child was null");
            List childList = getChildList();
            int size = childList.size();
            childList.add(child);
            child.setParent(this);
            if(isInTree())
                fireTreeNodesInserted(getPath(), new int[]{size});
        }

        public void removeChild(TreeNode child) {
            int index = indexOf(child);
            if(index >=0) {
                getChildList().remove(child);
                if(isInTree())
                    fireTreeNodesRemoved(getPath(), new int[index]);
            }
        }
        
        public void removeAll() {
        	clear();
        }

        protected void clear() {
            int size = getChildList().size();
            int[] childIndices = new int[size];
            for( int i = 0 ; i < size ; i++ ) { 
                childIndices[i] = i; 
            } 
            getChildList().clear();
            if(isInTree())
                fireTreeNodesRemoved(getPath(), childIndices);
        }

        public int indexOf(TreeNode child) {
            return children.indexOf(child);
        }

        protected List getChildList() {
            if(children == null) {
                children = new ArrayList();
            }
            return children;
        }

        public TreePath getParentPath() {
            TreePath result = null;
            TreeNode parent = getParent();
            if(parent == null)
                result = new TreePath();
            else
                result = parent.getPath();
            return result;
        }

        public TreeNode getChild(int i) {
            return (TreeNode)getChildList().get(i);
        }

        public TreePath getPath() {
            return getParentPath().pathByAddingChild(this);
        }

        public int getChildCount() {
            return getChildList().size();
        }

        public void setValue(Object value) {
            this.value = value;
            TreeNode parent = getParent();
            int[] childIndices = null;
            if(parent != null)
                childIndices = new int[]{parent.indexOf(this)};
            fireTreeNodesChanged(getParentPath(), childIndices);
        }

        public Object getValue() {
            return this.value;
        }

        public void setParent(TreeNode parent){
            this.parent = parent;
        }

        public TreeNode getParent() {
            return parent;
        }

        public TreeNode() {

        }

        public boolean isLoaded() {
            return true;
        }

        public void load() {
        }

    }

    public void setRoot(TreeNode node) {
        if(this.root != node) {
            this.root = node;
            fireTreeStructureChanged();
        }
    }

    public TreeNode getRootNode() {
        return root;
    }

    public NodeTreeModel () {

    }

    public Object getChild(Object parent, int index) {
        if(parent == null )
            throw new IllegalArgumentException("Parent was null");
        if(!(parent instanceof TreeNode) )
            throw new IllegalArgumentException("Parent have to be of type TreeNode");
        TreeNode node = (TreeNode)parent;
        return node.getChild(index);
    }

    public int getChildCount(Object parent) {
        if(parent == null )
            throw new IllegalArgumentException("Parent was null");
        if(!(parent instanceof TreeNode) )
            throw new IllegalArgumentException("Parent have to be of type TreeNode");
        TreeNode node = (TreeNode)parent;
        return node.getChildCount();
    }

    public int getIndexOfChild(Object parent, Object child) {
        if(parent == null )
            throw new IllegalArgumentException("Parent was null");
        if(!(parent instanceof TreeNode) )
            throw new IllegalArgumentException("Parent have to be of type TreeNode");
        if(!(child instanceof TreeNode) )
            throw new IllegalArgumentException("Child have to be of type TreeNode");
        TreeNode node = (TreeNode)parent;
        return node.indexOf((TreeNode)child);
    }

    public Object getRoot() {
        return getRootNode();
    }

    public boolean isLeaf(Object node) {
        if(node == null )
            throw new IllegalArgumentException("Node was null");
        if(!(node instanceof TreeNode) )
            throw new IllegalArgumentException("Node have to be of type TreeNode");
        return ((TreeNode)node).isLeaf();
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public boolean isLoaded(Object object) {
        if(!(object instanceof TreeNode))
            throw new IllegalArgumentException("Object has to be of type TreeNode, was "+object);
        return ((TreeNode)object).isLoaded();
    }

    public void load(Object object) {
        if(!(object instanceof TreeNode))
            throw new IllegalArgumentException("Object has to be of type TreeNode, was "+object);
        ((TreeNode)object).load();
    }

}
