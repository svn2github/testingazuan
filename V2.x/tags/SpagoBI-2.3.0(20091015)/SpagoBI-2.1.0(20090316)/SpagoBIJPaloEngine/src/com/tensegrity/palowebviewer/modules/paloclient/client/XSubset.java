package com.tensegrity.palowebviewer.modules.paloclient.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;


public class XSubset extends XObject {
	
    private XElementNode nodes[];
    private IXSubsetType type;

    public XSubset(){

    }

    public XSubset(String id, String name, XElementNode[] nodes) {
        super(id, name);
        this.nodes = nodes;
    }
    
    public XElementNode[] getNodes() {
		return nodes;
	}

	public void setNodes(XElementNode[] nodes) {
		this.nodes = nodes;
		XHelper.setParent(nodes, this);
	}

	public void set(XObject object) {
        super.set(object);
        XSubset subset = (XSubset) object;
        setNodes(subset.getNodes());
    }

    public boolean equals(Object o) {
        if(o instanceof XSubset)
            return equals((XSubset)o);
        else
            return false;
    }

    public boolean equals(XSubset subset) {
        if(subset == null)
            return false;
        return super.equals(subset);
    }

    public int getTypeID() {
        return TYPE_SUBSET;
    }

	public IXSubsetType getType() {
		return type;
	}

	public void setType(IXSubsetType type) {
		this.type = type;
	}
    

}
