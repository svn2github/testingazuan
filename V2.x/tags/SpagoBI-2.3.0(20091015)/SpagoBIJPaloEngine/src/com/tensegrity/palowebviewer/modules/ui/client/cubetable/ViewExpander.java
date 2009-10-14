package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.INodeStateListener;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.XElementPathTree.PathNode;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.taskchain.AbstractChainTask;
import com.tensegrity.palowebviewer.modules.util.client.taskchain.IChainTask;

public class ViewExpander {
	
	private boolean expanding = false;
	private final XElementPathTree pathTree = new XElementPathTree();
	private final List expandingList = new ArrayList();	
	private final List headerList = new ArrayList();
	private final INodeStateListener nodeStateListener = new INodeStateListener() {

		public void nodeStateChanged(HeaderTreeNode node) {
			if(node.isOpen()) {
				onNodeOpen(node);
			}
			else {
				onNodeClose(node);
			}
		}
		
	};
	
	public IChainTask getExpandTask() {
		return new ExpandTask();
	}
	
	public ViewExpander() {
	}
	
	public boolean isExpanding() {
		return expanding || !expandingList.isEmpty();
	}
	
	protected void onNodeClose(HeaderTreeNode node) {
		PathNode pathNode = getPathNode(node);
		Logger.debug(pathNode +": closed");
		pathNode.setOpen(false);
	}
	
	public void addElementPath(XElementPath path) {
		if(Logger.isOn()) {
			Logger.debug("ViewExpander.addElementPath("+path+")");
		}
		pathTree.addPath(path);
	}
	
	public void addElementPaths(XElementPath[] paths) {
		for (int i = 0; i < paths.length; i++) {
			XElementPath path = paths[i];
			addElementPath(path);
		}
	}
	
	protected void onNodeOpen(HeaderTreeNode node) {
		expandingList.remove(node);
		XElement[] path = getElementPath(node);
		PathNode pathNode = pathTree.addPath(path);
		if(Logger.isOn()){
			Logger.debug(pathNode +": opened");
		}
		openChildNodes(node, pathNode, false);
	}

	private void openChildNodes(HeaderTreeNode node, PathNode pathNode, boolean nextLayerOnly) {
		int size = pathNode.getChildCount();
		XDimension dimension = pathNode.getDimension();
		for(int i = 0; i < size; i++) {
			PathNode childNode = pathNode.getPathNode(i);
			if(!nextLayerOnly || childNode.getDimension() != dimension) {
				HeaderTreeNode headerNode = node.findChild(childNode.getElement());
				openNode(childNode, headerNode);
			}
		}
	}

	private void openNode(PathNode pathNode, HeaderTreeNode headerNode) {
		if(headerNode != null ) {
			if(pathNode.isOpen()) {
				if( !headerNode.isOpen() && !expandingList.contains(headerNode) && headerNode.isExpandable()){
					expandingList.add(headerNode);
					headerNode.setOpen(true);
				}
				openChildNodes(headerNode, pathNode, false);
			}
			else {
				openChildNodes(headerNode, pathNode, true);
			}
		}
	}

	public void addCubeHeader(CubeHeaderModel header) {
		headerList.add(header);
		header.addNodeStateListener(nodeStateListener);
	}
	
	private PathNode getPathNode(HeaderTreeNode node) {
		XElement[] path = getElementPath(node);
		PathNode pathNode = pathTree.getNodeForPath(path);
		return pathNode;
	}

	private XElement[] getElementPath(HeaderTreeNode node) {
		List path = new ArrayList();
		while(node.getParent() != null) {
			path.add(0, node.getElement());
			node = node.getParent();
		}
		return (XElement[])XArrays.toArray(path, IXConsts.TYPE_ELEMENT);
	}

	public void expand() {
		expandingList.clear();
		expanding = true;
		try {
		for (Iterator it = headerList.iterator(); it.hasNext();) {
			CubeHeaderModel header = (CubeHeaderModel) it.next();
			HeaderTreeNode root = header.getHeaderRoot();
			onNodeOpen(root);
		}
		}
		finally {
			expanding = false;
		}
	}
	
	public XElementPath[] getExpandedPaths(XDimension[] dimensions) {
		List pathList = new ArrayList();
		PathNode root = pathTree.getTreeRoot();
		collectExpandedPaths(root, dimensions, 0, pathList);
		XElementPath[] result = new XElementPath[pathList.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = (XElementPath)pathList.get(i);
		}
		return result;
	}

	private void collectExpandedPaths(PathNode node, XDimension[] dimensions, int dimIndex, List result) {
		if(dimIndex >= dimensions.length)
			return;
		int size = node.getChildCount();
		XDimension dim = dimensions[dimIndex];
		for(int i = 0; i < size; i++) {
			PathNode child =node.getPathNode(i);
			if(child.getDimension() == dim){
				if(child.isOpen()){
					result.add(child.toElementPath());
				}
				collectExpandedPaths(child, dimensions, dimIndex, result);
				collectExpandedPaths(child, dimensions, dimIndex+1, result);
			}
			
		}
	}
	
	private class ExpandTask extends AbstractChainTask implements INodeStateListener {
		

		private boolean taskExecuted;

		public void execute() {
			taskExecuted = false;	
			subscribe();
			expand();
			checkExpanded();
		}

		private void checkExpanded() {
			if(!isExpanding() && !taskExecuted) {
				taskExecuted = true;
				unsubscribe();
				executeNextTask();
			}
		}

		private void subscribe() {
			for (Iterator it = headerList.iterator(); it.hasNext();) {
				CubeHeaderModel header = (CubeHeaderModel) it.next();
				header.addNodeStateListener(this);
			}
		}
		
		
		private void unsubscribe() {
			for (Iterator it = headerList.iterator(); it.hasNext();) {
				CubeHeaderModel header = (CubeHeaderModel) it.next();
				header.removeNodeStateListener(this);
			}
		}

		public void nodeStateChanged(HeaderTreeNode node) {
			checkExpanded();
		}

		public String getDescription() {
			return "ExpandViewTask";
		}
		
		
	}

}
