package com.tensegrity.palowebviewer.modules.paloclient.client;


public class XElement extends XObject{

    private IElementType type;

    public XElement(){

    }

    public XElement(String id, String name, IElementType type) {
        super(id, name);
        this.type = type;
    }

    public IElementType getType() {
        return type;
    }

    public void setType(IElementType type) {
        this.type = type;
    }

    public XDimension getDimension() {
        return (XDimension)getParent();
    }

    public boolean equals(Object o) {
        if(o instanceof XElement)
            return equals((XElement)o);
        else
            return false;
    }

    public boolean equals(XElement element) {
        if(element == null)
            return false;
        boolean result = super.equals(element);
        IElementType type = this.getType();
        result &= type != null ? type.equals(element.getType()) : element.getType() == null;
        return result;
    }

    public int getTypeID() {
        return TYPE_ELEMENT;
    }

}
