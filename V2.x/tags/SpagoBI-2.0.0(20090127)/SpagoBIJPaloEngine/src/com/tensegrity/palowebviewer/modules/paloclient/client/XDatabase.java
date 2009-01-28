package com.tensegrity.palowebviewer.modules.paloclient.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;


public class XDatabase extends XObject {

	
    private XDimension[] dimensions;
    private XCube[] cubes;

    public XDatabase(){

    }

    public XDatabase(String id, String name, XDimension[] dimensions) {
        super(id, name);
        this.dimensions = dimensions;
    }

    public XCube[] getCubes() {
        return cubes;
    }

    public void setCubes(XCube[] cubes) {
        this.cubes = cubes;
        XHelper.setParent(cubes, this);
    }

    public void setDimensions(XDimension[] dimensions){
        this.dimensions = dimensions;
        XHelper.setParent(dimensions, this);
    }

    public XDimension[] getDimensions() {
        return dimensions;
    }

    public void set(XObject object) {
        if(!(object instanceof XDatabase))
            throw new IllegalArgumentException("XObject has to be of type XDatabase");
        super.set(object);
            setCubes(((XDatabase)object).getCubes());
            setDimensions(((XDatabase)object).getDimensions());
    }

    public boolean equals(Object o) {
        if(o instanceof XDatabase)
            return equals((XDatabase)o);
        else
            return false;
    }

    public boolean equals(XDatabase database) {
        if(database == null)
            return false;
        return super.equals(database);
    }

    public int getTypeID() {
        return TYPE_DATABASE;
    }

}
