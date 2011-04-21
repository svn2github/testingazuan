/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

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
