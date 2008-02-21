package com.tensegrity.palowebviewer.modules.paloclient.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;

/**
 * 
 * This is base object wrapper for all jpalo-objects.
 * 
 */
public abstract class XObject implements IXConsts, IsSerializable{
	
	public final static String NEW_ID = ""; //id to identify new'ly created view.
	
    private String name;
    private String id;
    //palo api object. Is null on client-side.
    private transient Object nativeObject;
    private transient XObject parent;
    private transient XPath path;
    private transient XObject[] objectPath;

    public abstract int getTypeID();

    public String toString() {
        return "XObject[ " + getName()+"]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public XObject(String id, String name) {
        this.name = name;
        this.id = id;
    }

    //default constructor to allow (de)serialization
    public XObject(){
    }

    /**
     * copies fields of object to this. Parent object is copied too.
     * 
     */
    public void set(XObject object) {
        if(object == null)
            throw new IllegalArgumentException("XObject was null.");
        name = object.getName();
        id = object.getId();
        parent = object.getParent();
    }

    public Object getNativeObject() {
        return nativeObject;
    }
    
    /** parent object. When working with XObject parent should be set(except for XRoot).
    * This field is transient, so, every time when client (java script interpreter) receives XObject
    * this field should be set.
    * @param nativeObject - JPalo object.
    **/
    public void setNativeObject(Object nativeObject) {
        this.nativeObject = nativeObject;
    }

    public final XObject getParent() {
        return parent;
    }

    public final void setParent(XObject parent) {
        this.parent = parent;
    }

    public boolean equals(Object o) {
        if(o instanceof XObject)
            return equals((XObject)o);
        else
            return false;
    }

    public boolean equals(XObject o) {
        if(o == null)
            return false;
        String id = o.getId();
        return getId() != null? getId().equals(id) : id == null;
    }

    public int hashCode() {
        String id = getId();
        return id==null ? 0 : id.hashCode();
    }
    
    /**
     * This method should be invoked when it parent are setted. Otherwise IllegalStateException will be thrown.
     * @return XPath
     */
    public final XPath constructPath(){
    	if(path == null){
    		path = new XPath(getPathArray());
    	}
		return path;
    }

    public XObject[] getPathArray() {
    	if(objectPath == null){
	        List path = new ArrayList();
	        XObject parent = getParent();
	        if(parent == null && !(this instanceof XRoot)){
	            throw new IllegalStateException("can't construct path for " + this + " because it's parent is null");
	        }
	        parent = collectPath(path, parent);
	        path.add(this);
	        XObject[] objsPath = XArrays.toArray(path);
	        this.objectPath = objsPath;
    	}
    	return objectPath;
    }

	private XObject collectPath(List path, XObject parent) {
		while(parent != null){
		    path.add(0,parent);
		    XObject prevParent = parent;
		    parent = parent.getParent();
		    if(parent == null && !(prevParent instanceof XRoot)){
		        throw new IllegalStateException("can't construct path for " + prevParent + " because it's parent is null");
		    }
		}
		return parent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		clearXPath();
	}

	private void clearXPath() {
		this.path = null;
	}

}
