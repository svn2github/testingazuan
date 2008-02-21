/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tensegrity.palowebviewer.modules.engine.client.IEngine;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.DefaultDimensionModel;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.dispose.IDisposable;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;

class DimensionList implements IDimensionList, IDisposable{

	private final PaloTreeModel treeModel;
	protected final DimListListeners listeners = new DimListListeners(this);
	private final IEngine engine;
	private final IUserCallback invalidItemCallback;
	 
	/**
	 * @param model
	 */
	public DimensionList(PaloTreeModel treeModel, IEngine engine, IUserCallback invalidItemCallback) {
		this.treeModel = treeModel;
		this.engine = engine;
		this.invalidItemCallback = invalidItemCallback;
	}

	protected final List dimList = new ArrayList();
    private boolean creating = false;
	private int showLevels;

    protected boolean isCreating() {
		return creating;
	}

	protected void setCreating(boolean creating) {
		this.creating = creating;
	}
	
	public XAxis createAxis() {
        XAxis result = new XAxis();
        XDimension[] dimensions = new XDimension[getDimCount()];
        XSubset[] subsets = new XSubset[getDimCount()];
        XElement[] selectedElements = new XElement[getDimCount()];
        for (int i = 0; i < dimensions.length; i++) {
            IDimensionModel dimModel = getDimModel(i);
            dimensions[i] = dimModel.getDimension();
            subsets[i] = (XSubset)dimModel.getSubsetListModel().getSelectedItem();
            selectedElements[i] = dimModel.getSelectedElement();
        }
        result.setSelectedElements(selectedElements);
        result.setDimensions(dimensions);
        result.setSubsets(subsets);
        return result;
    }

    public IDimensionModel getDimModel(int i) {
        return (IDimensionModel)dimList.get(i);
    }

    public int getDimCount() {
        return dimList.size();
    }
    
    public XDimension[] getDimensions() {
    	final int size = getDimCount();
		XDimension[] result = new XDimension[size];
		for(int i =0; i < size; i++) {
			result[i] = getDimension(i);
		}
		return result;
    }

    public List getDimensionsXPaths() {
        final List result = new ArrayList();
        final int size= getDimCount();
        for( int i = 0 ; i < size ; i++ ) { 
            XDimension dimension = getDimModel(i).getDimension();
            result.add(dimension.constructPath());

        } 
        return result;
    }

    public ITreeModel getDimensionTree(int i) {
        return getDimModel(i).getTreeModel();
    }

    public boolean hasDimension(IDimensionModel dim) {
        return dimList.contains(dim);
    }

    public void insertDimension(int i, IDimensionModel dim) {
    	if(dim == null)
            throw new IllegalArgumentException("Dimension can not be null.");
        int index = dimList.indexOf(dim);
        if(index != i) {
        	removeDimension(dim);
            dimList.add(i, dim);
            if(index == -1){
            	listeners.fireDimensionAdded(dim);
            }
            else {
            	listeners.fireDimensionMoved(dim);
            }
        }
    }

    public Iterator iterator() {
        return dimList.iterator();
    }

    public void dispose(){
        int size = getDimCount();
        for(int i = 0; i < size; ++i){
            getDimModel(i).dispose();
        }
    }

    protected void removeDimension(IDimensionModel dim) {
    	if(hasDimension(dim)) {
    		dimList.remove(dim);
    		listeners.fireDimensionRemoved(dim);
    	}
    }

    protected void setDimensions (XDimension[] dimensions, XSubset[] subsets, XElement[] selected) {
        if(selected == null)
            selected = new XElement[dimensions.length];
        setCreating(true);
        for (int i = 0; i < dimensions.length; i++) { 
            IDimensionModel dimModel = new DefaultDimensionModel(dimensions[i], engine, treeModel, subsets[i], selected[i], invalidItemCallback);
            insertDimension(i, dimModel);
        }
        setCreating(false);
    }

	public void addListener(IDimensionListListener listener) {
		listeners.addListener(listener);
		
	}

	public void removeListener(IDimensionListListener listener) {
		listeners.removeListener(listener);	
	}

	public int getShowLevels() {
		return showLevels;
	}

	public void setShowLevels(int value) {
		this.showLevels = value;
	}

	public XDimension getDimension(int i) {
		return getDimModel(i).getDimension();
	}

}