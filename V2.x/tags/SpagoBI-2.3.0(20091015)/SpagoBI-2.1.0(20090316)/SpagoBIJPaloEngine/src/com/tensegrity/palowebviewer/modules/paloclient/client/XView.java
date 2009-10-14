package com.tensegrity.palowebviewer.modules.paloclient.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;



public class XView extends XObject{

    private XAxis[] axises;
    private String description;

    public final String getDescription() {
        return description;
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    public XView(){

    }

    public XView(String id, String name, XAxis[] axises) {
        super(id, name);
        this.axises = axises;
    }

    public XAxis[] getAxises() {
        return axises;
    }

    public void setAxises(XAxis[] axises) {
        this.axises = axises;
        XHelper.setParent(axises, this);
    }

    public boolean equals(Object o) {
        if(o instanceof XView)
            return equals((XView)o);
        else
            return false;
    }

    public boolean equals(XView view) {
        boolean result = super.equals(view);
        if(result)
        	result = axisEquals(view);
        return result;
    }

    private boolean axisEquals(XView view) {
    	boolean result = true;
    	if(axises != view.axises){
    		if((axises == null) || (view.axises == null)){
    			result = false;
    		}
    		else if((axises.length == 3) && (view.axises.length == 3)) { 
    			result &= getRowsAxis().equals(view.getRowsAxis());
    			result &= getColsAxis().equals(view.getColsAxis());
    			result &= getSelectedAxis().equals(view.getSelectedAxis());
    		}
    		else{
    			result = (axises.length == 0) && (axises.length == view.axises.length);
    		}
    	}
		return result;
	}

	public void set(XObject object) {
        XView view = (XView) object;
        super.set(object);
        setAxises(view.getAxises());
    }

    private XAxis findAxis(String name){
    	XAxis r = null;
    	XAxis[] axises = getAxises();
        for (int i = 0; (i < axises.length) && (r == null); i++) {
            if(axises[i].getName().equals(name)){
                r = axises[i];
            }
        }
        if(r == null) {
        	throw new RuntimeException("can't find axis " + name + " for view" + this);
        }
        return r;
    }

    public XAxis getRowsAxis(){
        return findAxis(XAxis.ROWS);
    }

    public XAxis getColsAxis(){
        return findAxis(XAxis.COLUMNS);
    }

    public XAxis getSelectedAxis(){
        return findAxis(XAxis.SELECTED);
    }

    public int getTypeID() {
        return TYPE_VIEW;
    }
}
