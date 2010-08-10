package com.tensegrity.palowebviewer.modules.ui.client.dimensions;

import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.engine.client.IVerificationCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.IElementType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XInvalidType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.ui.client.tree.ElementNodeNode;
import com.tensegrity.palowebviewer.modules.ui.client.tree.ElementsFolder;
import com.tensegrity.palowebviewer.modules.ui.client.tree.SubsetElementFolder;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.DefaultTreeComboboxModel;

public class SelectedElementComboboxModel extends DefaultTreeComboboxModel {

	private VerificationCallback verificationCallback;

	public SelectedElementComboboxModel(ITreeModel treeModel, XElement selected) {
		super(treeModel, selected);
	}
	
	public void setSelectedItem(Object node) {
		if(node instanceof ElementNodeNode) {
			ElementNodeNode elementNodeNode = (ElementNodeNode)node;
			checkAndSetElement(elementNodeNode);
		}
		
    }

	private void checkAndSetElement(ElementNodeNode elementNodeNode) {
		if(verificationCallback == null) {
			verificationCallback = new VerificationCallback();
		}
		verificationCallback.checkAndSetElement(elementNodeNode);
		
	}

	protected boolean isItemValid(Object item) {
		boolean result = false;
		if(item instanceof XElement) {
			XElement element = (XElement) item;
			IElementType type = element.getType();
			result = !(type instanceof XInvalidType);
		}
		return result;
	}
	
	protected void superSetSelectedItem(XElement element) {
		super.setSelectedItem(element);
	}
	
	private final class VerificationCallback implements IVerificationCallback {
		
		private XElement element;
		private PaloTreeNode rootNode;

		public void checkAndSetElement(ElementNodeNode node) {
			this.element = (XElement)node.getElement();
			rootNode = node;
			IPaloServerModel serverModel = rootNode.getPaloTreeModel().getServerModel(); 
			while (rootNode instanceof ElementNodeNode) {
				rootNode = (PaloTreeNode)rootNode.getParent();
			}
			XObject contextObject = rootNode.getXObject();
			if(contextObject instanceof XDimension){
				serverModel.checkElement((XDimension)contextObject, element, this);
			}
			else if(contextObject instanceof XSubset){
				serverModel.checkElement((XSubset)contextObject, element, this);
			}
		}

		public void fail() {
			XElementNode[] nodes = null;
			if(rootNode instanceof SubsetElementFolder){
				SubsetElementFolder folder = (SubsetElementFolder)rootNode;
				XSubset subset = (XSubset)folder.getXObject();
				nodes = subset.getNodes();
			}
			else if(rootNode instanceof SubsetElementFolder){
				ElementsFolder folder = (ElementsFolder)rootNode;
				XDimension subset = (XDimension)folder.getXObject();
				nodes = subset.getNodes();
			}
			if(nodes != null && nodes.length >0) {
				superSetSelectedItem(nodes[0].getElement());
			}
		}

		public void successed() {
			superSetSelectedItem(element);
		}
		
	}


}
