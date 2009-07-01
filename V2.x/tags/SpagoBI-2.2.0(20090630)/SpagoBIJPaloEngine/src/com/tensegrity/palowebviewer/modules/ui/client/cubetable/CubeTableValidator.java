package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;

public class CubeTableValidator {
	
	private final ICubeTableModel tableModel;
	private String invalidReason;
	private boolean modelValid;
	
	public CubeTableValidator(ICubeTableModel tableModel) {
		this.tableModel = tableModel;
	}
	
	public ICubeTableModel getTableModel () {
		return tableModel;
	}
	
	public String getInvalidReason() {
		return invalidReason;
	}
	
	protected void setInvalidReason(String value) {
		invalidReason = value;
	}
	
	public boolean isModelValid() {
		return modelValid && isDimensionsValid();
	}
	
	public boolean isViewValid() {
		XView view = getTableModel().getView();
		boolean result = view != null;
		if(result){
			XAxis[] axes = view.getAxises();
			result = axes != null && axes.length == 3;
		}
		return result;
	}
	
	public boolean isDimensionsValid() {
		return isXDimListValid() && isYDimListValid() && isSliceDimListValid();
	}
	
	public boolean isXDimListValid() {
		IDimensionList list = getTableModel().getXDimensions();
		return isAxisDimListValid(list,"Table has no column dimensions.");
	}
	
	public boolean isYDimListValid() {
		IDimensionList list = getTableModel().getYDimensions();
		return isAxisDimListValid(list,"Table has no row dimensions.");
	}

	private boolean isAxisDimListValid(IDimensionList list, String invalidMessage) {
		final int size = list.getDimCount();
		boolean result = size > 0;
		if(result) {
			for (int i = 0; i < size; i++) {
				IDimensionModel dimModel = list.getDimModel(i);
				result = validateAxisDim(dimModel);
				if(!result){
					String reason = "Dimension '"+dimModel.getDimension().getName()+"'";
					reason += " has no elements";
					setInvalidReason(reason);
					break;
				}
			}
		}
		else {
			setInvalidReason(invalidMessage);
		}
		return result;
	}

	protected boolean validateAxisDim(IDimensionModel dimModel) {
		boolean result = true;
		ITreeModel tree = dimModel.getTreeModel();
		Object root = tree.getRoot();
		if(tree.isLoaded(root)){
			result = tree.getChildCount(root) >0;
		}
		return result;
	}
	
	public boolean isSliceDimListValid() {
		IDimensionList list = getTableModel().getSliceDimensions();
		final int size = list.getDimCount();
		boolean result = true;
		for (int i = 0; i < size; i++) {
			IDimensionModel dimModel = list.getDimModel(i);
			if(dimModel.getSelectedElement() == null){
				result = false;
				String reason = "Dimension '" + dimModel.getDimension().getName() + "'";
				reason += " has no selected element";
				setInvalidReason(reason);
				break;
			}
		}
		return result;
	}
	
	public void setModelValid(boolean value) {
		modelValid = value;
	}
	
	public boolean isLoaded() {
		return isXDimListLoaded()&& isYDimListLoaded() && isSliceDimListLoaded();
	}
	
	public boolean isXDimListLoaded(){
		IDimensionList list = getTableModel().getXDimensions();
		return isAxisDimListLoaded(list);
	}
	
	public boolean isYDimListLoaded(){
		IDimensionList list = getTableModel().getYDimensions();
		return isAxisDimListLoaded(list);
	}
	
	public boolean isSliceDimListLoaded(){
		IDimensionList list = getTableModel().getSliceDimensions();
		boolean result = true;
		final int size = list.getDimCount();
		for (int i = 0; i < size; i++) {
			IDimensionModel dimModel = list.getDimModel(i);
			XElement selectedElement = dimModel.getSelectedElement();
			ITreeModel treeModel = dimModel.getComboboxModel().getTreeModel();
			Object root = treeModel.getRoot();
			if(treeModel.isLoaded(root) ) {
				int rootChildCount = treeModel.getChildCount(root);
				if(rootChildCount != 0) {
					result = selectedElement != null;
				}
			}
			else {
				result = (selectedElement != null);//|| treeModel.getChildCount(root)==0;
			}
		}
		return result;
	}

	private boolean isAxisDimListLoaded(IDimensionList list) {
		boolean result = true;
		final int size = list.getDimCount();
		for (int i = 0; i < size && result; i++) {
			IDimensionModel dimModel = list.getDimModel(i);
			ITreeModel tree = dimModel.getTreeModel();
			Object root = tree.getRoot();
			if(!tree.isLoaded(root)) {
				tree.load(root);
				result = false;
			}
		}
		return result;
	}

}
