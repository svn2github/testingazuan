package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tensegrity.palowebviewer.modules.engine.client.AbstractServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModelListener;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode.NodeOpenOperation;
import com.tensegrity.palowebviewer.modules.ui.client.tree.ElementNodeNode;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;
import com.tensegrity.palowebviewer.modules.widgets.client.dispose.IDisposable;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.AbstractTreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreePath;

public class CubeHeaderModel extends AbstractTreeModel implements IDisposable
{

    private final List treeModels = new ArrayList();
    private HeaderTreeNode root = null;
    protected final NodeStateListenerCollection nodeStateListeners= new NodeStateListenerCollection();
    private final List expandedNodes = new ArrayList();
    private final IPaloServerModel paloServerModel;
    private final List openOperations = new ArrayList();
	private boolean canExpand = true;

    public void addTreeModel(int i, ITreeModel model){
        if(model == null)
            throw new IllegalArgumentException("Tree model can not be null.");
        if(treeModels.contains(model))
            throw new IllegalArgumentException("IntegrationTreeModel can not hold the same model twice.");
        if(model == this)
            throw new IllegalArgumentException("The model can not contain itself.");
        treeModels.add(i, model);
    }
    
    public void setCanExpand(boolean value) {
    	this.canExpand = value;
    	if(canExpand) {
    		Object[] operations = openOperations.toArray();
			for (int i = 0; i < operations.length; i++) {
				NodeOpenOperation operation = (NodeOpenOperation)operations[i];
				if(operation.doOperation()) {
					break;
				}
			}
    	}
    }
    
    public boolean canExpand() {
    	return canExpand ;
    }

    public void removeTreeModel(ITreeModel model){
        treeModels.remove(model);
    }
    
    List getExpandedNodes() {
    	return expandedNodes;
    }

    public ITreeModel getTreeModel(int i) {
        return (ITreeModel)treeModels.get(i);
    }

    public int getTreeModelCount() {
        return treeModels.size();
    }

    public CubeHeaderModel (IPaloServerModel serverModel) {
    	this.paloServerModel = serverModel;
    	serverModel.addListener(serverModelListener);
    }

    public HeaderTreeNode getHeaderRoot() {
        if(root == null && getTreeModelCount()>0) {
            ITreeModel model = getTreeModel(0);
            Object node = model.getRoot();
            root = new HeaderTreeNode(node, model, null);
        }
        return root;
    }

    public void addNodeStateListener(INodeStateListener listener) {
        nodeStateListeners.addListener(listener);
    }

    public void removeNodeStateListener(INodeStateListener listener) {
        nodeStateListeners.removeListener(listener);
    }

    public int getLayerNr(HeaderTreeNode node){
        return node.getLayerNr();
    }

    public int getLayerDepth(int i){
        ITreeModel model = getTreeModel(i);
        return calculateDepth(model)-1;
    }

    public int getLayerCount(){
        return treeModels.size();
    }

    public int getLayerNodeCount(int i) {
        ITreeModel model = getTreeModel(i);
        Object root = model.getRoot();
        return getNodeCount(model, root);

    }

    public HeaderTreeNode getChild(HeaderTreeNode parent, int index) {
        return (HeaderTreeNode)getChild(parent, index);
    }

    public int getWidth() {
        final int size = getLayerCount();
        int result = 1;
        for( int i = 0 ; i < size ; i++ ) { 
            result *= (getLayerNodeCount(i)-1);
        } 
        return result;
    }

    public void clear() {
        expandedNodes.clear();
        treeModels.clear();
        openOperations.clear();
        root = null;
    }

    public Object getChild(Object parent, int index) {
        HeaderTreeNode node = (HeaderTreeNode)parent;
        return node.getChild(index);
    }

    public int getChildCount(Object parent) {
        HeaderTreeNode node = (HeaderTreeNode)parent;
        return node.getChildCount();
    }

    public int getIndexOfChild(Object parent, Object child) {
        HeaderTreeNode node = (HeaderTreeNode)parent;
        return node.getIndexOfChild(child);
    }

    public Object getRoot() {
        return getHeaderRoot();
    }

    public boolean isLeaf(Object node) {
        HeaderTreeNode intNode = (HeaderTreeNode)node;
        return intNode.isLeaf();
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public int getVisibleWidth() {
        HeaderTreeNode root = getHeaderRoot();
        int result = 0;
        if(root != null) {
            result = root.getVisibleRightNode().getX()+1;
        }
        return result;
    }

    public boolean hasNode(HeaderTreeNode node) {
        return this == node.getHeaderModel();
    }

    // Utility methods

    protected boolean isLast(ITreeModel model) {
        int index = treeModels.indexOf(model);
        boolean result = (index+1) == treeModels.size();
        return result;
    }

    protected ITreeModel getNext(ITreeModel model) {
        ITreeModel result = null;
        int index = treeModels.indexOf(model);
        index += 1;
        if(index < treeModels.size()){
            result = (ITreeModel)treeModels.get(index);
        }
        return result;
    }

    protected int calculateDepth(ITreeModel model) {
        Object root = model.getRoot();
        int result = 0;
        if(root != null)
            result = calculateDepth(model, root);
        return result;

    }

    protected int calculateDepth(ITreeModel model, Object node) {
        int result = 1;
        final int size = model.getChildCount(node);
        int maxDepth = 0;
        for( int i = 0 ; i < size ; i++ ) { 
            Object child = model.getChild(node, i);
            int depth = calculateDepth(model, child);
            if(depth > maxDepth)
                maxDepth = depth;
        } 
        result += maxDepth;
        return result;

    }

    protected int getNodeCount(ITreeModel model, Object node) {
        int result = 1;
        final int size = model.getChildCount(node);
        for( int i = 0 ; i < size ; i++ ) { 
            Object child = model.getChild(node, i);
            result += getNodeCount(model, child);
        } 
        return result;
    }

    // Inner classes

    public interface INodeStateListener {

        public void nodeStateChanged(HeaderTreeNode node);

    }

    public class HeaderTreeNode{
        private final static int NOT_CALCULATED = -1;

        private final Object node;
        private final ITreeModel model;
        private final Map childMap = new HashMap();
        private final HeaderTreeNode parent;
        private boolean open = false;
        private int x = NOT_CALCULATED;
        private int y = NOT_CALCULATED;
        private String path = null;
        private int occurrence = NOT_CALCULATED;
        private int nodeIndex = NOT_CALCULATED;
        private NodeOpenOperation openOperation;

        public int getOccurence() {
            if(occurrence == NOT_CALCULATED){
                int result = 0;
                int layerNr = getLayerNr();
                if(layerNr>0) {
                    HeaderTreeNode parent = getPreviousLayerParent();
                    int nodeCount = getLayerNodeCount(layerNr-1)-1;
                    result = parent.getOccurence()*nodeCount;
                    result += parent.getNodeIndex();
                }
                occurrence = result;
            }
            return occurrence;
        }
        
        public String toString() {
        	String result = "HeaderNode[";
        	result += node;
        	result += "]";
        	return result;
        }

        public int getNodeIndex() {
            if(nodeIndex == NOT_CALCULATED) {
                nodeIndex = calculateNodeIndex();
            }
            return nodeIndex;
        }

		private int calculateNodeIndex() {
			int result = 0;
			if(isRoot()) {
			    result = -1;
			}
			else {
			    HeaderTreeNode sibling = getThisLayerPreviousSibling();
			    if(sibling == null) {
			        HeaderTreeNode parent = getParent();
			        if(parent.getLayerNr() != this.getLayerNr()){
			            result = 0;
			        }
			        else {
			            result = parent.getNodeIndex() + 1;
			        }
			    }
			    else {
			        result = sibling.getThisLayerRightNode().getNodeIndex();
			        result += 1;
			    }
			}
			return result;
		}

        public int getX() {
            //if(x == NOT_CALCULATED) {
            x = -1;
            HeaderTreeNode parent = getParent();
            if(parent!=null){
                int index = parent.getIndexOfChild(this);
                if(index >0 ){
                    HeaderTreeNode child = parent.getChild(index-1);
                    HeaderTreeNode right = child.getVisibleRightNode();
                    x = right.getX()+1;
                }
                else{
                    x = parent.getX();
                    if(parent.getLayerNr()==getLayerNr())
                        x +=1;
                }
            }
            //}
            return x;
        }

        public int getY() {
            if(y == NOT_CALCULATED) {
                y = calculateY();
            }
            return y;
        }

		private int calculateY() {
			int result = -1;
			HeaderTreeNode parent = getParent();
			if(parent!=null){
			    int layerNr = getLayerNr();
			    if(parent.getLayerNr()==getLayerNr()){
			    	result = parent.getY()+1;
			    }
			    else {
			    	result = 0;
			        for( int i = 0 ; i < layerNr ; i++ ) { 
			        	result += getLayerDepth(i);
			        } 
			    }
			}
			return result;
		}

        public HeaderTreeNode getParent() {
            return parent;
        }

        public Object getNode() {
            return node;
        }

        public XDimension getDimension() {
        	PaloTreeNode node = (PaloTreeNode)getTreeModel().getRoot();
        	XObject object = node.getXObject();
        	object = XHelper.findBackByType(object, IXConsts.TYPE_DIMENSION);
        	XDimension dimension = (XDimension)object; 
            return dimension;
        }

        public XElementNode getElementNode() {
            XElementNode result = null;
            if(getNode() instanceof ElementNodeNode){
                ElementNodeNode node = (ElementNodeNode)getNode();
                result = node.getElementNode();
            }
            return result;
        }
        
        public XElement getElement() {
        	XElementNode elementNode = getElementNode();
			XElement result = null;
			if(elementNode != null)
				result = elementNode.getElement();
			return result;
        }

        public ITreeModel getTreeModel() {
            return model;
        }

        public HeaderTreeNode(Object node, ITreeModel model, HeaderTreeNode parent) {
            this.node = node;
            this.model = model;
            this.parent = parent;
        }

        public HeaderTreeNode getChild(int i) {
            HeaderTreeNode result = null;
            if(!isLastLayer()) {
                int count = getSubTreeNodeCount();
                if(i<count){
                    result = getSubTreeNode(i);
                }
                else
                    i -= count;
            }
            if(result == null){
                result = getThisLayerChild(i);
            }
            return result;
        }

		private HeaderTreeNode getThisLayerChild(int i) {
			HeaderTreeNode result;
			Object child = getTreeModel().getChild(getNode(), i);
			result = (HeaderTreeNode)childMap.get(child);
			if(result == null){
			    result = new HeaderTreeNode(child, getTreeModel(), this);
			    childMap.put(child, result);
			}
			return result;
		}

        public HeaderTreeNode getChildByName(String name) {
            if(name == null)
                throw new IllegalArgumentException("Name can not be null.");
            HeaderTreeNode result = null;
            final int size = getChildCount();
            for( int i = 0 ; i < size ; i++ ) { 
                HeaderTreeNode node = getChild(i);
                if(name.equals(node.getNode().toString())){
                    result = node;
                    break;
                }
            } 
            return result;
        }

        public HeaderTreeNode findChild(XElement element) {
            HeaderTreeNode result = null;
            final int size = getChildCount();
            for( int i = 0 ; i < size ; i++ ) { 
            	HeaderTreeNode child = getChild(i);
            	if(child.getElement() == element) {
            		result = child;
            		break;
            	}
            } 
            return result;
        }

        public int getChildCount() {
            int result = getTreeModel().getChildCount(getNode()); 
            result += getSubTreeNodeCount();
            return result; 
        }

        public int getIndexOfChild(Object child) {
            int result = 0;
            HeaderTreeNode childNode = (HeaderTreeNode)child;
            if(childNode.getLayerNr() == getLayerNr()) {
                ITreeModel model = getTreeModel();
                Object parent = getNode();
                result = model.getIndexOfChild(parent, childNode.getNode());
                if(!isLast(getTreeModel())) {
                    result += getSubTreeNodeCount();
                }
            }
            else {
                ITreeModel model = childNode.getTreeModel();
                Object node = childNode.getNode();
                result = model.getIndexOfChild(model.getRoot(), node);
            }
            return result;
        }

        public boolean isLeaf() {
            boolean result = isLast(getTreeModel());
            if(result)
                result = getTreeModel().isLeaf(getNode());
            return result;
        }

        public HeaderTreeNode getSubTreeNode(int i){
            ITreeModel model = getNext(getTreeModel());
            Object root = model.getRoot();
            Object child = model.getChild(root, i);
            HeaderTreeNode result = (HeaderTreeNode)childMap.get(child);
            if(result ==null) {
                result = new HeaderTreeNode(child, model, this);
                childMap.put(child, result);

            }
            return result;
        }

        public boolean isOpen() {
            return open || (getParent()==null);
        }

        public String getTreePath() {
            if(path == null) {
                path = "/";
                if(!isRoot()) {
                    XElement element = getElement();
                    String name = element.getName();
                    HeaderTreeNode parent = getParent();
                    path = parent.getTreePath();
                    if(parent.getLayerNr()!=getLayerNr())
                        path += "/";
                    path += name+"/";
                }
            }
            return path;
        }

        public void toggleOpen() {
            setOpen(!isOpen());
        }
        
        public boolean isLoaded() {
        	Object node = getNode();
        	return getTreeModel().isLoaded(node);
        }

        public void setOpen(boolean value) {
            if(open != value && openOperation == null){
            	if(isExpandable()) {
            		openOperation = new NodeOpenOperation(value);
            		openOperation.enqueue();
            	}
            }
        }

		public boolean isExpandable() {
			return !getTreeModel().isLeaf(getNode());
		}

        public int getLayerNr() {
            return treeModels.indexOf(getTreeModel());
        }

        public boolean isVisible() {
            boolean result = true;
            HeaderTreeNode parent = getParent();
            if(parent != null) {
                result = parent.isVisible();
                if(result && parent.getLayerNr() == getLayerNr())
                    result = parent.isOpen();
            }
            return result;
        }

        public boolean isRoot() {
            return getParent()==null;
        }

        public int getSubTreeNodeCount() {
            int result = 0;
            if(!isRoot()) {
                ITreeModel model = getNextLayer();
                if(model != null) {
                    result = model.getChildCount(model.getRoot());
                }
            }
            return result;
        }

        public int getVisibleChildCount () {
            int result = 0;
            if(isOpen())
                result = getChildCount();
            else
                result = getSubTreeNodeCount();
            return result;
        }

        public CubeHeaderModel getHeaderModel() {
            return CubeHeaderModel.this;
        }

        public boolean isNextToLastLayer() {
            int nr = getLayerNr()+2;
            return nr == getLayerCount();
        }

        public boolean isLastLayer() {
            int nr = getLayerNr()+1;
            return nr == getLayerCount();
        }

        protected HeaderTreeNode getRightNode() {
            HeaderTreeNode result = this;
            int size = getChildCount();
            if(size != 0) {
                result = getChild(size -1).getRightNode();
            }
            return result;
        }

        protected HeaderTreeNode getThisLayerRightNode() {
            HeaderTreeNode result = this;
            int size = getChildCount();
            if(size - getSubTreeNodeCount()!= 0) {
                result = getChild(size -1).getThisLayerRightNode();
            }
            return result;
        }

        protected HeaderTreeNode getVisibleRightNode() {
            HeaderTreeNode result = this;
            if(isVisible()) {
            	int size = isOpen()? getChildCount() : getSubTreeNodeCount();
            	if(size != 0) {
            		result = getChild(size -1).getVisibleRightNode();
            	}
            }
            return result;
        }

        protected HeaderTreeNode getThisLayerPreviousSibling() {
            HeaderTreeNode result = getPreviousSibling();
            if(result != null && result.getLayerNr() != getLayerNr())
                result = null;
            return result;
        }

        protected HeaderTreeNode getPreviousSibling() {
            HeaderTreeNode parent = getParent();
            HeaderTreeNode result = null;
            if(parent != null){
                int index = parent.getIndexOfChild(this);
                if(index >0)
                    result = parent.getChild(index - 1);
            }
            return result;
        }

        protected ITreeModel getNextLayer() {
            ITreeModel result = null;
            int nr = getLayerNr()+1;
            if(nr < getLayerCount())
                result = CubeHeaderModel.this.getTreeModel(nr);
            return result;
        }

        public HeaderTreeNode getPreviousLayerParent() {
            int layerNr = getLayerNr();
            HeaderTreeNode result = getParent();
            while(result != null && result.getLayerNr() == layerNr){
                result = result.getParent();
            }
            return result;
        }
        
        public final class NodeOpenOperation {
        	private final boolean value;
        	public NodeOpenOperation(boolean value) {
        		this.value = value;
        	}
        	
        	public void enqueue() {
        		openOperations.add(this);
        		doOperation();
        	}
        	
        	public boolean doOperation() {
        		boolean result = false;
        		if(isLoaded() ){
        			if(canExpand()) {
        				Object node = getNode();
        				if(!getTreeModel().isLeaf(node)) {
        					open = value;
        					if(open && !isRoot() ){
        						expandedNodes.add(HeaderTreeNode.this);
        					}
        					else{
        						expandedNodes.remove(HeaderTreeNode.this);
        					}
        					nodeStateListeners.fireNodeStateCanged(HeaderTreeNode.this);
        				}
        				remove();
        				result = true;
        			}
        		}
        		else {
        			getPaloServerModel().loadChildren(getElementNode(), IXConsts.TYPE_ELEMENT_NODE);
        		}
        		return result;
        	}

			private void remove() {
				openOperation = null;
				openOperations.remove(this);
			}

        }

    }

    public void dispose() {
        clear();
        paloServerModel.removeListener(serverModelListener);
    }

	protected IPaloServerModel getPaloServerModel() {
		return paloServerModel;
	}
	
	protected final IPaloServerModelListener serverModelListener = new AbstractServerModelListener() {

		public void onChildArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
			Object[] operations = openOperations.toArray();
			for (int i = 0; i < operations.length; i++) {
				NodeOpenOperation operation = (NodeOpenOperation)operations[i];
				operation.doOperation();
			}
		}
		
	};
    
}
