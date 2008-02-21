package com.tensegrity.palowebviewer.modules.widgets.client.treecombobox;

import com.tensegrity.palowebviewer.modules.widgets.client.combobox.AbstractComboboxModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModelListener;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreeModelEvent;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreeUtil;


/**
 * Default implementation for {@link ITreeComboboxModel} with tree instead of plain list. 
 * As the base {@link ITreeModel} may be partially loaded, the model have {@link IValidator}
 * that is asked if the selected item is OK. The validator is given setter because it has not 
 * does not know which method to call to set the value.
 *
 */
public class DefaultTreeComboboxModel extends AbstractComboboxModel implements ITreeComboboxModel
{

    private final ITreeModel treeModel;
    protected Object selectedItem;
    private final ITreeModelListener treeModelListener = new ITreeModelListener() {

		public void treeNodesChanged(TreeModelEvent e) {
			resetSelected();
		}

		public void treeNodesInserted(TreeModelEvent e) {
			resetSelected();
		}

		public void treeNodesRemoved(TreeModelEvent e) {
			resetSelected();
		}

		public void treeStructureChanged(TreeModelEvent e) {
			resetSelected();
		}
		
    };
	private IValidator validator;
	private ISetter valueSetter = new ISetter() {

		public void setValue(Object value) {
			Object oldItem = DefaultTreeComboboxModel.this.selectedItem;
			if(value != oldItem) {
				DefaultTreeComboboxModel.this.selectedItem = value;
				listenerCollection.fireSelectionChanged(oldItem);
			}
		}
		
	};
    
	protected void resetSelected() {
		Object item = getSelectedItem();
		validator.validateAndSet(item, valueSetter);
//		if(!isItemValid(item))
//			setInitialSelection();
	}

	protected boolean isItemValid(Object item) {
		return item != null && TreeUtil.hasTreeNode(getTreeModel(),item);
	}

    public ITreeModel getTreeModel() {
        return treeModel;
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Object node) {
        //TODO: what if the treeModel has no the node?
    	if(this.selectedItem != node) {
    		validator.validateAndSet(node, valueSetter);
//    		if(isItemValid(node)) {
//    		}
//    		else {
//    			listenerCollection.fireInvalidItemSet(node);
//    		}
    	}
    }
    
    public DefaultTreeComboboxModel (ITreeModel treeModel, Object selected) {
    	this(treeModel, selected, null);
    	
    }

    public DefaultTreeComboboxModel (ITreeModel treeModel, Object selected, IValidator validator) {
        if(treeModel == null)
            throw new IllegalArgumentException("Tree model can not be null.");
        this.validator = validator;
        if(validator == null)
        	this.validator = new DefaultValidator();
        this.treeModel = treeModel;
        setSelectedItem(selected);
        treeModel.addTreeModelListener(treeModelListener);
        resetSelected();
    }

    protected void setInitialSelection() {
        Object root = treeModel.getRoot();
        if(!treeModel.isLoaded(root)){
        	treeModel.load(root);
        }
        else {
        	int size = treeModel.getChildCount(root);
        	Object selected = null;//= root;
        	if(size >0)
            	selected = treeModel.getChild(root, 0);
        	setSelectedItem(selected);
        }
    }
    
    
    private final class DefaultValidator implements IValidator {

		public DefaultValidator() {
		}
    	
		public void validateAndSet(Object value, ISetter setter) {
			if(isItemValid(value)){
				setter.setValue(value);
			}
			
		}
    	
    }

}
