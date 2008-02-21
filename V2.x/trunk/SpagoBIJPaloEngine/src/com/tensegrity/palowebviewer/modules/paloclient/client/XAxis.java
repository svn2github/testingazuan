package com.tensegrity.palowebviewer.modules.paloclient.client;

import com.tensegrity.palowebviewer.modules.util.client.Arrays;


public class XAxis extends XObject {

    public static final String ROWS = "rows",
           COLUMNS = "cols",
           SELECTED = "selected";

    private XDimension[] dimensions;
    private XSubset[] subsets;
    //if there is no selected element in db it will be NULL
    private XElement[] selectedElements;
    private XElementPath[] elementPaths;

    public XAxis(){

    }

    /**
     * 
     * @param id : "x", "y", "others"
     * 
     */
    public XAxis(String id,XDimension[] dimensions, XSubset[] subsets, XElementPath[] paths){
        super(id, id);
        this.dimensions = dimensions;
        this.subsets = subsets;
        this.elementPaths = paths;
    }

    public final XDimension[] getDimensions() {
        return dimensions;
    }

    public final void setDimensions(XDimension[] dimensions) {
        this.dimensions = dimensions;
    }

    public boolean equals(XObject o) {
        boolean result = super.equals(o) && (o instanceof XAxis);
        if(result){
	        XAxis axis = (XAxis) o;
	        if(axis != null) {
	            result &= Arrays.equals(getDimensions(), axis.getDimensions());
	            result &= Arrays.equals(getSubsets(), axis.getSubsets());
	            result &= Arrays.equals(getSelectedElements(), axis.getSelectedElements());
	            result &= Arrays.equals(getElementPaths(), axis.getElementPaths());
	        }
        }
        return result;
    }

    public final XSubset[] getSubsets() {
        return subsets;
    }

    public final void setSubsets(XSubset[] subsets) {
        this.subsets = subsets;
    }

    public void set(XObject object) {
        XAxis axis = (XAxis) object;
        super.set(object);
        setSubsets(axis.getSubsets());
        setDimensions(axis.getDimensions());
        setElementPaths(axis.getElementPaths());
        setSelectedElements(axis.getSelectedElements());
    }

    public final XElement[] getSelectedElements() {
        return selectedElements;
    }

    public final void setSelectedElements(XElement[] selectedElement) {
        this.selectedElements = selectedElement;
    }

    public int getTypeID() {
        return TYPE_AXIS;
    }

	public XElementPath[] getElementPaths() {
		return elementPaths;
	}

	public void setElementPaths(XElementPath[] elementPath) {
		this.elementPaths = elementPath;
	}

}
