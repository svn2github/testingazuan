package com.tensegrity.palowebviewer.modules.paloclient.client;



public class XConsolidatedElement extends XElement {

    private double[] weights;

    public XConsolidatedElement(){

    }

    public XConsolidatedElement(String id, String name){
        super(id, name, XConsolidatedType.instance);
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double getWeight(int child){
        return weights[child];
    }

    public boolean equals(Object o) {
        if(o instanceof XConsolidatedElement)
            return equals((XConsolidatedElement)o);
        else
            return false;
    }

    public boolean equals(XConsolidatedElement element) {
        if(element == null)
            return false;
        return super.equals(element);
    }

    public void set(XObject object) {
        super.set(object);
        XConsolidatedElement element = (XConsolidatedElement) object;
        setWeights(element.getWeights());
    }

    public int getTypeID() {
        return TYPE_CONSOLIDATED_ELEMENT;
    }

}
