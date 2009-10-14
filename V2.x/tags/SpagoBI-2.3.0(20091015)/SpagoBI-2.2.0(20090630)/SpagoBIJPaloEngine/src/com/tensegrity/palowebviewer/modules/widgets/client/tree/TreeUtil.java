package com.tensegrity.palowebviewer.modules.widgets.client.tree;

import java.util.ArrayList;
import java.util.List;


/**
 * A set of useful utilities to work with {@link ITreeModel}.
 *
 */
public class TreeUtil
{

    public static void visitTree(ITreeModel model, ITreeVisitor visitor) {
        if(model == null)
            throw new IllegalArgumentException("Model can not be null.");
        Object root = model.getRoot();
        visitTree(model, visitor, root);
    }
    
    public static void visitTree(ITreeModel model, ITreeVisitor visitor, Object root) {
        if(model == null)
            throw new IllegalArgumentException("Model can not be null.");
        if(visitor == null)
            throw new IllegalArgumentException("Visitor can not be null.");
    	if(!visitor.hasFinished()) {
    		visitNode(model, visitor, null, root);
    	}
    }

	private static void visitNode(ITreeModel model, ITreeVisitor visitor, Object parent, Object node) {
		visitor.visitNode(parent, node);
		visitChildNodes(model, node, visitor);
		visitor.leaveNode(parent, node);
	}
    

    private static void visitChildNodes(ITreeModel model, Object node, ITreeVisitor visitor) {
        final int size = model.getChildCount(node);
        for( int i = 0 ; i < size; i++ ) {
        	if(visitor.hasFinished())
        		break;
            Object child = model.getChild(node, i);
            visitNode(model, visitor, node, child);
        } 

    }

    public static int getTreeDepth(ITreeModel model, Object parent) {
        int result = 0;
        if(parent == null)
            parent = model.getRoot();
        if(parent != null) {
            result = 1;
            int max = collectChildDepth(model, parent); 
            result += max;
        }
        return result;
    }

	private static int collectChildDepth(ITreeModel model, Object parent) {
		final int size = model.getChildCount(parent);
		int max = 0;
		for( int i = 0 ; i < size ; i++ ) { 
		    Object child = model.getChild(parent, i);
		    int depth = getTreeDepth(model, child);
		    max = depth>max ? depth : max;
		}
		return max;
	}
	
	public static Object[] getPathToNode(ITreeModel model, Object node){
		NodePathFinder finder = new NodePathFinder(node);
    	visitTree(model, finder);
    	return finder.getResult().toArray();
	}
    
    public static boolean hasTreeNode(ITreeModel model, Object node) {
    	NodeFinder finder = new NodeFinder(node);
    	visitTree(model, finder);
    	return finder.hasFound();
    }

    public static int getTreeDepth(ITreeModel model) {
        return getTreeDepth(model, null);
    }

    public interface IIDGenerator {

        public String getId(Object object);

    }
    
    public static class NodeFinder implements ITreeVisitor {
    	
    	private final Object findNode;
    	private boolean found = false;
    	
    	public NodeFinder(Object value) {
    		findNode = value;
    	}

		public void visitNode(Object parent, Object node) {
			if(!found)
				found = findNode.equals(node);
		
		}
		
		public boolean hasFound() {
			return found;
		}

		public boolean hasFinished() {
			return hasFound();
		}

		public void leaveNode(Object parent, Object node) {
			
		}
    	
    }

    public static class NodePathFinder implements ITreeVisitor {
    	
    	private final Object node;
    	private boolean found = false;
    	private final List result = new ArrayList();
    	
    	public List getResult () {
    		return result;
    	}
    	
    	public NodePathFinder(Object value) {
    		node = value;
    	}

		public void visitNode(Object parent, Object node) {
			if(!found){
				result.add(node);
				found = this.node.equals(node);
			}
		}
		
		public boolean hasFound() {
			return found;
		}

		public boolean hasFinished() {
			return hasFound();
		}

		public void leaveNode(Object parent, Object node) {
			if(!found){
				result.remove(node);
			}			
		}
    	
    }


}
