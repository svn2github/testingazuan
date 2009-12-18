/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.engine.client.IEngine;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserCallback;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModelListener;

class AxisDimensionList extends DimensionList{

	private final CubeHeaderModel headerModel;
    private final ITreeModelListener treeModelListener;
    private final IDimensionListListener listListener = new IDimensionListListener () {

		public void dimensionAdded(IDimensionList list, IDimensionModel model) {
			ITreeModel treeModel = model.getTreeModel();
			treeModel.addTreeModelListener(treeModelListener);
		}

		public void dimensionRemoved(IDimensionList list, IDimensionModel model) {
			ITreeModel treeModel = model.getTreeModel();
			treeModel.removeTreeModelListener(treeModelListener);
		}

		public void dimensionMoved(IDimensionList list, IDimensionModel model) {
		}

    };
    
    public AxisDimensionList(PaloTreeModel treeModel, IEngine engine, CubeHeaderModel headerModel, ITreeModelListener listener, IUserCallback invalidItemCallback) {
    	super(treeModel, engine, invalidItemCallback);
    	addListener(listListener);
        this.headerModel = headerModel;
        this.treeModelListener = listener;
    }
    
    public void rebuildHeader() {
        headerModel.clear();
        int size = getDimCount();
        for( int i = 0 ; i < size ; i++ ) { 
            IDimensionModel dimModel = getDimModel(i);
            headerModel.addTreeModel(i, dimModel.getTreeModel());
        } 
    }

	public void insertDimension(int i, IDimensionModel dim) {
    	if(dim == null)
            throw new IllegalArgumentException("Dimension can not be null.");
        int index = dimList.indexOf(dim);
        if(index != i) {
        	removeDimension(dim);
            dimList.add(i, dim);
            //hanlde change dim order
            listeners.fireDimensionAdded(dim);
            listeners.fireDimensionMoved(dim);
        }
	}
    
    

}