package com.tensegrity.palowebviewer.modules.paloclient.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;


public class XCube extends XObject{

    private XDimension[] dimensions;
    private XView[] views;
    private XView defaultView;

    public XCube(){

    }

    public XCube(String id, String name) {
        super(id, name);
    }

    public XDimension[] getDimensions() {
        return dimensions;
    }

    public void setDimensions(XDimension[] dimensions) {
        this.dimensions = dimensions;
    }

    public XView[] getViews() {
        return views;
    }

    public void setViews(XView[] views) {
        this.views = views;
        XHelper.setParent(views, this);
    }

    public void set(XObject object) {
        if(!(object instanceof XCube))
            throw new IllegalArgumentException("XObject has to be of type XCube");
        super.set(object);
        setViews(((XCube)object).getViews());
        setDimensions(((XCube)object).getDimensions());
    }

    public boolean equals(Object o) {
        if(o instanceof XCube)
            return equals((XCube)o);
        else
            return false;
    }

    public boolean equals(XCube cube) {
        if(cube == null)
            return false;
        return super.equals(cube);
    }

    public int getTypeID() {
        return TYPE_CUBE;
    }

	public XView getDefaultView() {
		return defaultView;
	}

	public void setDefaultView(XView defaultView) {
		this.defaultView = defaultView;
		if(defaultView != null)
			defaultView.setParent(this);
	}

    
}
