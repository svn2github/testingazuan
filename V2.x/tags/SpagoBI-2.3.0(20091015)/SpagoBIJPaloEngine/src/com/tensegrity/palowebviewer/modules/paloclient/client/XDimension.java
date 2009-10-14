package com.tensegrity.palowebviewer.modules.paloclient.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;


public class XDimension extends XObject{

    private XElementNode[] nodes;
    private XSubset[] subsets;

    public XDimension(){

    }

    public XDimension(String id, String name) {
        super(id, name);
    }

    public XElementNode[] getNodes() {
		return nodes;
	}

	public void setNodes(XElementNode[] nodes) {
		this.nodes = nodes;
		XHelper.setParent(nodes, this);
	}

	public XSubset[] getSubsets() {
        return subsets;
    }

    public void setSubsets(XSubset[] subsets) {
        this.subsets = subsets;
        if(subsets != null)
            XHelper.setParent(subsets, this);
    }

    public boolean equals(Object o) {
        if(o instanceof XDimension)
            return equals((XDimension)o);
        else
            return false;
    }

    public boolean equals(XDimension dimension) {
        if(dimension == null)
            return false;
        return super.equals(dimension);
    }

    public void set(XObject object) {
        if(!(object instanceof XDimension))
            throw new IllegalArgumentException("XObject has to be of type XDimension");
        super.set(object);
        XDimension dimension = (XDimension) object;
        setNodes(dimension.getNodes());
        setSubsets(dimension.getSubsets());
    }

    public int getTypeID() {
        return TYPE_DIMENSION;
    }

}
