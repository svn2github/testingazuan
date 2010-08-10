package com.tensegrity.palowebviewer.modules.ui.client.dimensions;

import com.tensegrity.palowebviewer.modules.engine.client.IEngine;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.ui.client.tree.DimensionNode;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel;
import com.tensegrity.palowebviewer.modules.ui.client.tree.SubsetNode;
import com.tensegrity.palowebviewer.modules.widgets.client.combobox.IComboboxListener;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.SubTreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.DefaultTreeComboboxModel;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.ITreeComboboxModel;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.IValidator;


public class DefaultDimensionModel implements IDimensionModel
{

    private final XDimension dimension;
    private final ITreeComboboxModel comboboxModel;
    private final SubsetComboboxModel subsetListModel;
    private final SubTreeModel elementsTreeModel;
    private final PaloTreeModel paloTreeModel;
    private final IComboboxListener subsetComboboxListener = new IComboboxListener(){
        public void onSelectionChanged(Object oldItem) {
            XObject root = (XSubset)subsetListModel.getSelectedItem();
            Object node;
            if(root != null) {
            	SubsetNode subsetNode = (SubsetNode)paloTreeModel.getNodeForXObject(root);
            	node = subsetNode.getElementsFolder();
            }
            else {
            	root = getDimension();
            	DimensionNode dimNode = (DimensionNode)paloTreeModel.getNodeForXObject(root);
            	node = dimNode.getElementsFolder();
            }
            elementsTreeModel.setRoot(node);
        }

		public void onInvalidItemSet(Object item) {
		}
    };

    public ITreeModel getTreeModel() {
        return getComboboxModel().getTreeModel();
    }

    public ITreeComboboxModel getComboboxModel() {
        return this.comboboxModel;
    }

    public ISubsetListModel getSubsetListModel() {
        return subsetListModel;
    }

    public XDimension getDimension() {
        return dimension;
    }

    public DefaultDimensionModel (XDimension dimension, IEngine engine, PaloTreeModel paloTreeModel, XSubset subset, XElement selected, IUserCallback invalidItemCallback) {
        this.dimension = dimension;
        this.paloTreeModel = paloTreeModel;
        DimensionNode dimNode = (DimensionNode)paloTreeModel.getNodeForXObject(dimension);
        Object root = dimNode.getElementsFolder();
        elementsTreeModel = new SubTreeModel(paloTreeModel, root);
        subsetListModel = new SubsetComboboxModel(dimNode.getSubsetsFolder());
        subsetListModel.addComboboxListener(subsetComboboxListener);
        subsetListModel.setSelectedItem(subset);
        IValidator validator =  new SelectedElementValidator(engine, elementsTreeModel, invalidItemCallback);
        comboboxModel = new DefaultTreeComboboxModel(elementsTreeModel, selected, validator);
    }

    public XElement getSelectedElement() {
    	Object selected = getComboboxModel().getSelectedItem();
    	XElement result = null;
    	if(selected instanceof XElement) {
    		result = (XElement)selected;
    	}
        return result;
    }

	public void setSelectedElement(final XElement element) {
		/*ITreeModel model = getComboboxModel().getTreeModel();
		ElementFinder finder = new ElementFinder(element);
		TreeUtil.visitTree(model, finder);
		if(finder.hasFound())*/
		getComboboxModel().setSelectedItem(element);
	}

	public void dispose() {
		elementsTreeModel.dispose();
		subsetListModel.dispose();
	};

}
