package com.tensegrity.palowebviewer.modules.ui.client.tree;


import com.tensegrity.palowebviewer.modules.engine.client.AbstractServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModelListener;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.NodeTreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreePath;

public class PaloTreeModel extends NodeTreeModel {

    private final IPaloServerModel serverModel;
    private final TreePathConverter treePathConverter = new TreePathConverter(this);


    public PaloTreeNode getNodeForXObject(XObject object) {
        XObject[] path = object.getPathArray();
        TreePath treePath = toTreePath(path);
        return (PaloTreeNode)treePath.getLastPathComponent();
    }

    public static abstract class PaloTreeNode extends TreeNode {
    	
    	private final PaloTreeModel treeModel;

        public XObject getXObject() {
        	return (XObject)getValue();
        }
        
        public PaloTreeModel getPaloTreeModel(){
        	return treeModel;
        }

        protected PaloTreeNode(PaloTreeModel treeModel, XObject object) {
        	treeModel.super();
            if(object == null)
                throw new IllegalArgumentException("Null value for XObject is illegal.");
            this.treeModel = treeModel;
            getPaloTreeModel().lockEvents();
            setValue(object);
            reinit();
            getPaloTreeModel().unlockEvents();
        }

        protected void loadChildren() {
        	XObject[] children = getXObjectChildren(); 

        	for( int i = 0 ; i < children.length ; i++ ) { 
        		TreeNode child = createNode(children[i]);
        		addChild(child);
        	} 
        }

        public void childrenChanged(XObject[] oldChildren, int type) {
            reinit();
        }

        protected void reinit() {
            if(isInitialized()){
                PerformanceTimer timer = new PerformanceTimer(toString()+".loadChildren()");
                timer.start();
                try {
                	getPaloTreeModel().lockEvents();
                    clear();
                    loadChildren();
                }
                finally{
                	getPaloTreeModel().unlockEvents();
                }
                getPaloTreeModel().fireTreeStructureChanged(getPath());
                timer.report();
            }
        }
        
        public boolean isInitialized() {
        	return getXObjectChildren() != null;
        }

        public XObject[] getXObjectPath () {
            return getXObject().getPathArray();
        }

        public boolean isLoaded() {
            return isInitialized();
        }
        
        protected abstract int getChildType();

        public void load() {
        	getPaloTreeModel().serverModel.loadChildren(getXObject(), getChildType());
        }

        public String toString() {
            //return "PaloTreeNode["+getXObject().getName()+"]";
            return getXObject().getName();
        }	

        public int hashCode() {
            return getXObject().hashCode();
        }

        protected abstract XObject[] getXObjectChildren();

        protected abstract PaloTreeNode createNode(XObject obj);

    };

    private final IPaloServerModelListener serverModelListener = new AbstractServerModelListener() {

        public void onObjectLoaded(XObject[] path, XObject object) {
            Logger.debug("object "+object+" loaded for path "+object.constructPath());
            try {
            	TreePath treePath = toTreePath(path);
            	PaloTreeNode node = (PaloTreeNode)treePath.getLastPathComponent();
            	node.reinit();
            }
            catch(IllegalArgumentException iae){
                //ignore
                //TODO: think how to make it right way
            }
        }

        public void modelChanged() {
            reinit();
        }

        public void onChildArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
            try {
                TreePath treePath = toTreePath(path);
                PaloTreeNode node = (PaloTreeNode)treePath.getLastPathComponent();
                node.childrenChanged(oldChildren, type);
            }
            catch(IllegalArgumentException iae){
                //ignore
                //TODO: think how to make it right way
            }
        }

		public void objectRenamed(XObject object) {
			TreePath treePath = toTreePath(object.getPathArray());
			PaloTreeNode node = (PaloTreeNode)treePath.getLastPathComponent();
			TreeNode parent = node.getParent();
			int index = parent.indexOf(node);
			fireTreeNodesChanged(parent.getPath(), new int[]{index});                
		}

    };

    public PaloTreeModel(IPaloServerModel serverModel){
        this.serverModel = serverModel;
        this.serverModel.addListener(serverModelListener);
        reinit();
    }
    
    protected void reinit(){
        TreeNode rootNode = new RootNode(this, serverModel.getRoot());
        setRoot(rootNode);
    }

    protected TreePath toTreePath(XObject[] path) {
        if(path == null)
            path = new XObject[0];
        return treePathConverter.convert(path); 

    }
    
    public String toString() {
    	return "PaloTreeModel";
    }

	public IPaloServerModel getServerModel() {
		return serverModel;
	}

}
