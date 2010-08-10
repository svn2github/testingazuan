package com.tensegrity.palowebviewer.modules.ui.client.dimensions;

import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.ui.client.tree.ElementNodeNode;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeVisitor;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreeUtil;

public class ElementFinder implements ITreeVisitor {
	
	private final XElement element;
	private ElementNodeNode node = null;
	
	public static ElementNodeNode findNode(ITreeModel model, XElement element) {
		ElementFinder finder = new ElementFinder(element);
		TreeUtil.visitTree(model, finder);
		return finder.getNode();
	}
	
	public ElementNodeNode getNode() {
		return node;
	}
	
	public ElementFinder(XElement element) {
		this.element = element;
	}
	
	public void visitNode(Object parent, Object node) {
		if (node instanceof ElementNodeNode) {
			ElementNodeNode myNode = (ElementNodeNode) node;
			checkElement(myNode);
		}
	}

	private void checkElement(ElementNodeNode myNode) {
		if(myNode.getElement() == element){
			node = myNode;
		}
	}
	
	public boolean hasFound() {
		return node != null; 
	}

	public boolean hasFinished() {
		return hasFound();
	}

	public void leaveNode(Object parent, Object node) {
	}
	
}