/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;

class DataReloader {


    /**
	 * 
	 */
	private final ICubeTableModel cubeTableModel;

	/**
	 * @param model
	 */
	public DataReloader(ICubeTableModel model) {
		cubeTableModel = model;
	}

	private XQueryPath[] queryArray;
    private int queryNr;
    private int[] xArray;
    private HeaderTreeNode xNode;
    private int[] yArray;
    private HeaderTreeNode yNode;

    public void reloadData(HeaderTreeNode xNode, HeaderTreeNode yNode) {
        this.xNode = xNode;
        this.yNode = yNode;
        List xNodes = getQueryNodes(xNode);
        List yNodes = getQueryNodes(yNode);
        int size = xNodes.size()*yNodes.size();
        queryArray = new XQueryPath[size];
        xArray = new int[size];
        yArray = new int[size];
        queryNr = 0;
        for( Iterator itX = xNodes.iterator () ; itX.hasNext (); ) { 
            HeaderTreeNode nodeX = (HeaderTreeNode)itX.next();
            reloadData(yNodes, nodeX); 
        } 
        sendQuery();
    }

	private void reloadData(List yNodes, HeaderTreeNode nodeX) {
		for( Iterator itY = yNodes.iterator () ; itY.hasNext (); ) { 
		    HeaderTreeNode nodeY = (HeaderTreeNode)itY.next();
		    addQuery(nodeX, nodeY);
		}
	}

    protected void addQuery(HeaderTreeNode xNode,HeaderTreeNode yNode) {
        queryArray[queryNr] = getQueryConstructor().constructQuerry(xNode, yNode);
        xArray[queryNr] = getQueryConstructor().getX();
        yArray[queryNr] = getQueryConstructor().getY();
        queryNr++;
    }

	private XQueryConstructor getQueryConstructor() {
		return getCubeTableModel().getQueryConstructor();
	}

    protected void collectQueryNodes(List result, HeaderTreeNode node) {
        if(node.isNextToLastLayer()) {
            collectNextToLastLayerNode(result, node);
        }
        else if(node.isLastLayer()) {
        	result.add(node);
        }
        else if(node.isOpen()) {
        	collectOpenNode(result, node); 
        }
        else {
        	collectClosedNode(result, node); 
        }
    }

	private void collectClosedNode(List result, HeaderTreeNode node) {
		int size = node.getSubTreeNodeCount();
		collectChildNodes(result, node, size, 0);
	}

	private void collectOpenNode(List result, HeaderTreeNode node) {
		int first = 0;
		if(node == xNode || node == yNode) {
			first = node.getSubTreeNodeCount();
		}
		int size = node.getChildCount();
		collectChildNodes(result, node, size, first);
	}

	private void collectNextToLastLayerNode(List result, HeaderTreeNode node) {
		if(!node.isRoot() && !(xNode ==node || yNode == node))
		    result.add(node);
		if(node.isOpen()) {
		    int size = node.getChildCount();
		    int first = node.getSubTreeNodeCount();
		    collectChildNodes(result, node, size, first);
		}
	}

	private void collectChildNodes(List result, HeaderTreeNode node, int size, int first) {
		for( int i = first; i < size; i++ ) { 
            HeaderTreeNode child = node.getChild(i);
            collectQueryNodes(result, child);
        }
	}

    protected List getQueryNodes(HeaderTreeNode node) {
        List result = new ArrayList();
        collectQueryNodes(result, node);
        return result;
    }

    protected void sendQuery() {
        DataQuery callback = new DataQuery(getCubeTableModel(), queryArray, xArray, yArray);
        callback.sendQuery();
    }

	public ICubeTableModel getCubeTableModel() {
		return cubeTableModel;
	}

}