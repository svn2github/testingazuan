/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.edit;

import it.eng.qbe.query.ExpressionNode;

import java.util.List;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class ExpressionUtilities {

	
	/**
	 * Update the where clause expression
	 * @param node root of the expression
	 * @param fieldName name of this field
	 * @param connector the new connector
	 */
	public static void updateParentOperation(ExpressionNode node,String fieldName,String connector){
		ExpressionNode nodeToUpdate = findParentNode(node, fieldName);
		if(nodeToUpdate!=null){
			nodeToUpdate.setValue(connector);
		}
	}
	
	private static ExpressionNode findParentNode(ExpressionNode node,String fieldName){
		if(node!=null){
			List<ExpressionNode> children = node.getChildNodes();
			if(children!=null){
				//check if the field is the child of the passed node..
				//if so the node to update is the passed node
				for(int i=0; i<children.size(); i++){
					if(children.get(i).getValue().equals(fieldName)){
						return node;
					}
				}
				//control if the field live in this subtree
				for(int i=0; i<children.size(); i++){
					ExpressionNode findNodeInChild = findParentNode(children.get(i),fieldName);
					if(findNodeInChild!=null){
						return findNodeInChild;
					}
				}
			}
		}
		//the field doesn't live in this subtree
		return null;
	}
	
	/**
	 * Update the where clause expression
	 * @param node root of the expression
	 * @param fieldName name of this field
	 * @param connector the new connector
	 */
	public static void updateNodeName(ExpressionNode node,String oldFieldName,String newFieldName){
		ExpressionNode nodeToUpdate = findNode(node, oldFieldName);
		if(nodeToUpdate!=null){
			nodeToUpdate.setValue(newFieldName);
		}
	}
	
	private static ExpressionNode findNode(ExpressionNode node,String fieldName){
		if(node!=null){
			if(node.getValue().equals(fieldName)){
				return node;
			}else{
				List<ExpressionNode> children = node.getChildNodes();
				if(children!=null){
					for(int i=0; i<children.size(); i++){
						ExpressionNode findNodeInChild = findNode(children.get(i),fieldName);
						if(findNodeInChild!=null){
							return findNodeInChild;
						}
					}
				}
			}
		}
		//the field doesn't live in this subtree
		return null;
	}
	
	/**
	 * Remove a node from the Expression..
	 * In general.. it searches in the tree for the node to remove..
	 * When it finds the node, the method removes the parent node (the parent node is
	 * the operation of the node to remove).. The children of the parent node are adopted 
	 * from the grandparent
	 * @param granParent
	 * @param parent
	 * @param nodeName
	 * @return the new root of the expression
	 */
	public static ExpressionNode removeNode(ExpressionNode grandParent,ExpressionNode parent,String nodeName){
		if(grandParent==null){//we are in the root
			if(parent.getType().equals("NO_NODE_OP")){//the expression contains only one node
				if(parent.getValue().equals(nodeName)){
					parent = null;
				}
				return parent;
			}
		}
		List<ExpressionNode> children = parent.getChildNodes();
		if (children!=null && children.size()>0){
			int i = 0;
			for(i=0; i<children.size(); i++){
				if(children.get(i).getValue().equals(nodeName)){
					break;
				}
			}
			if(i<children.size()){//we have found the node to remove
				if(grandParent==null){//it works only for binary trees
					//the parent node is the root and the node to delete is the right child..
					//so the new root of the expression is the left child
					return children.get(0);
				}
				children.remove(i);
				grandParent.setValue(parent.getValue());
				grandParent.setType(parent.getType());
				grandParent.getChildNodes().remove(parent);
				grandParent.getChildNodes().addAll(children);
				parent=null;
			}else{//we have to continue the search
				for(i=0; i<children.size(); i++){
					removeNode(parent, children.get(i), nodeName);
				}
			}
		}
		return parent;
	}
	
}
