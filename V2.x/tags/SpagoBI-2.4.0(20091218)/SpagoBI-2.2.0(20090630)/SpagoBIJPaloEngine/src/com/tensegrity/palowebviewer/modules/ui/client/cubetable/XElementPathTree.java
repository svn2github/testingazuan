package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.NodeTreeModel;

public class XElementPathTree extends NodeTreeModel {
	
	private static final XElement[] EMPTY_ELEMENT_ARRAY = new XElement[0];
	private final PathNode treeRoot;
	
	public class PathNode extends TreeNode{
		
		private final XElement element;
		private boolean open;
		
		public PathNode(XElement element) {
			this.element = element;
			open = false;
		}

		public XElement getElement() {
			return element;
		}
		
		public XDimension getDimension() {
			XElement element = getElement();
			XDimension dimension = null;
			if(element != null)
				dimension = element.getDimension();
			return dimension;
		}
		
		public PathNode addPathNode(XElement element) {
			if(element.getDimension() == null)
				throw new IllegalArgumentException("Element '"+element+"' has no parent.");
			PathNode newNode = createNode(element);
			addChild(newNode);
			return newNode;
		}
		
		public void setOpen(boolean value) {
			this.open = value;
			if(open == false && getChildCount() == 0) {
				getParent().removeChild(this);
			}
		}
		
		public boolean isOpen() {
			return open;
		}
		
		public PathNode copyNode() {
			PathNode result = new PathNode(getElement());
			return result;
		}
		
		public PathNode copyDimensionSubTree() {
			PathNode result= copyNode();
			int size = getChildCount();
			XDimension dimension = getDimension();
			for(int i =0; i< size; i++) {
				PathNode child = getPathNode(i);
				if(child.getDimension() == dimension) {
					PathNode childCopy = child.copyDimensionSubTree();
					result.addChild(childCopy);
				}
			}
			return result;
		}
		
		public PathNode copySubTree() {
			PathNode result= copyNode();
			int size = getChildCount();
			for(int i =0; i< size; i++) {
				PathNode child = getPathNode(i);
				PathNode childCopy = child.copySubTree();
				result.addChild(childCopy);
			}
			return result;
		}

		
		public XElementPath toElementPath() {
			XElementPath result = new XElementPath();
			XElement[] elements = getElements();
			int i1 = 0;
			while(i1 < elements.length) {
				XDimension dim = elements[i1].getDimension();
				int i2 = i1;
				for (; i2 < elements.length; i2++) {
					XElement element = elements[i2];
					if(element.getDimension() != dim)
						break;
				}
				XElement[] dimElements = (XElement[])XArrays.copy(elements, i1, i2, IXConsts.TYPE_ELEMENT);
				result.addPart(dim, dimElements);
				i1 = i2;
			}
			return result;
		}
		
		public XElement[] getElements() {
			List elementList = new ArrayList();
			PathNode node = this;
			PathNode parent = (PathNode)node.getParent();
			while(parent != null) {
				XElement element = node.getElement();
				elementList.add(0, element);
				node = parent;
				parent = (PathNode)node.getParent();
			}
			XElement[] result = (XElement[])XArrays.toArray(elementList, IXConsts.TYPE_ELEMENT);
			return result;
		}
		
		public PathNode getPathNode(int i) {
			return (PathNode)getChild(i);
		}
		
		public PathNode findNode(XElement element) {
			PathNode result = null;
			final int size = getChildCount();
			for(int i = 0; i<size; i++) {
				PathNode node = getPathNode(i);
				if(node.getElement() == element) {
					result = node;
					break;
				}
			}
			return result;
		}

		public XElement[] getOpenElements() {
			List result = new ArrayList();
			final int size = getChildCount();
			for(int i = 0; i<size; i++) {
				PathNode node = getPathNode(i);
				if(node.isOpen()) {
					result.add(node.getElement());
				}
			}
			return (XElement[])XArrays.toArray(result, IXConsts.TYPE_ELEMENT);
		}
		
		
		public String pathToString() {
			XElement element = getElement();
			String result = element == null? "" : element.getName();
			if(getParent() != null) {
				result = ((PathNode)getParent()).pathToString()+"/"+result;
			}
			return result;
		}
		
		public String toString() {
			return "PathNode["+pathToString()+"]";
		}

	}
	
	public XElementPathTree() {
		treeRoot = new PathNode(null);
		setRoot(treeRoot);
	}
	
	protected PathNode createNode(XElement element) {
		return new PathNode(element);
	}

	public void addPath(XElementPath path) {
		XElement[] elements = toElementArray(path);
		addPath(elements);
	}
	
	private XElement[] toElementArray(XElementPath path) {
		XDimension[] dimensions = path.getDimensions();
		XElement[][] elements = new XElement[dimensions.length][];
		XElement[] result = null;
		int size = 0;
		for (int i = 0; i < dimensions.length; i++) {
			elements[i] = path.getPart(dimensions[i]);
			size += elements[i].length;
		}
		result = new XElement[size];
		int offset = 0;
		for (int i = 0; i < elements.length; i++) {
			XElement[] tmpElements = elements[i];
			for (int j = 0; j < tmpElements.length; j++) {
				result[offset+j] = tmpElements[j];
			}
			offset += tmpElements.length;
		}
		return result;
	}

	public PathNode addPath(XElement[] path) {
		PathNode node = getTreeRoot();
		int newIndex= -1 ;
		final int size = path.length;
		for (int i = 0; i < size; i++) {
			PathNode nextNode = node.findNode(path[i]);
			if(nextNode == null) {
				newIndex = i;
				break;
			}
			else {
				node = nextNode;
			}
		}
		if(newIndex >=0){
			for(int i= newIndex; i< size; i++) {
				node = node.addPathNode(path[i]);
			}
		}
		
		node.setOpen(true);
		return node;
	}
	
	protected PathNode getTreeRoot() {
		return treeRoot;
	}
	
	public XElement[] getOpenChildren(XElement[] path) {
		XElement[] result = EMPTY_ELEMENT_ARRAY;
		PathNode node = getNodeForPath(path);
		if(node != null) {
			result = node.getOpenElements();
		}
		return result;
	}

	public PathNode getNodeForPath(XElement[] path) {
		PathNode result = getTreeRoot();
		final int size = path.length;
		for (int i = 0; i < size && result != null; i++) {
			result = result.findNode(path[i]);
		}
		return result;
	}

	
	

}
