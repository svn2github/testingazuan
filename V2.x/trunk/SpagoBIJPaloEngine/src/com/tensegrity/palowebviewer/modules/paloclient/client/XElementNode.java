package com.tensegrity.palowebviewer.modules.paloclient.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;

public class XElementNode extends XObject {

	private XElement element;
	private XElementNode[] children;
	
	public XElementNode(){
		
	}
	
	public XElementNode(XElement element){
		super(element.getId(), element.getName());
		this.element = element;
	}
	
	public int getTypeID() {
		return TYPE_ELEMENT_NODE;
	}

	public XElementNode[] getChildren() {
		return children;
	}

	public void setChildren(XElementNode[] children) {
		this.children = children;
		XHelper.setParent(children, this);
	}

	public XElement getElement() {
		return element;
	}

	public void setElement(XElement element) {
		this.element = element;
	}

}
