package com.tensegrity.palowebviewer.modules.ui.client;

import com.tensegrity.palowebviewer.modules.engine.client.IElementPathCallback;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.ElementFinder;
import com.tensegrity.palowebviewer.modules.ui.client.tree.ElementNodeNode;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;
import com.tensegrity.palowebviewer.modules.widgets.client.ITreeViewItem;
import com.tensegrity.palowebviewer.modules.widgets.client.TreeView;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModelListener;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreeModelEvent;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.ICoboboxViewListener;
import com.tensegrity.palowebviewer.modules.widgets.client.treecombobox.TreeCombobox;

public class PaloTreeExpander {
	
	private final IPaloServerModel serverModel;
	private final TreeCombobox treeCombobox;
	private final LazyExpander lazyExpander = new LazyExpander();
	private final ICoboboxViewListener viewListener = new ICoboboxViewListener () {

		public void onDropdownOpen() {
			exopandToSelectedElement();
		}
		
	};
	private final IElementPathCallback pathCallback = new IElementPathCallback() {

		public void onSuccess(XElement[] path) {
			expandPath(path);
		}
		
	};
	
	public PaloTreeExpander(IPaloServerModel serverModel, TreeCombobox treeCombobox) {
		this.serverModel = serverModel;
		this.treeCombobox = treeCombobox;
		this.treeCombobox.addListener(viewListener);
	}

	protected void exopandToSelectedElement() {
		Object item = treeCombobox.getModel().getSelectedItem();
		if (item instanceof XElement) {
			XElement element = (XElement) item;
			ITreeModel treeModel = getTreeModel();
			ElementNodeNode node = ElementFinder.findNode(treeModel, element);
			if(node != null) {
				Object[] path = node.getParentPath().getPath();
				XElement[] elementPath = retriveElements(path);
				expandPath(elementPath);
			}
			else {
				loadPathFromServer(element);
			}
			
		}
	}

	private ITreeModel getTreeModel() {
		ITreeModel treeModel = treeCombobox.getModel().getTreeModel();
		return treeModel;
	}

	protected void loadPathFromServer(XElement element) {
		ITreeModel model = getTreeModel();
		PaloTreeNode node = (PaloTreeNode)model.getRoot();
		XObject root = node.getXObject();
		serverModel.loadPath(root, element, pathCallback);
		
	}

	protected void expandPath(XElement[] elementPath) {
		lazyExpander.expand(elementPath);
	}

	private XElement[] retriveElements(Object[] path) {
		int size = 0;
		int pathSize = path.length;
		for (; size < pathSize; size++) {
			 if(!(path[pathSize - size - 1] instanceof ElementNodeNode)){
				 break;
			 }
		}
		XElement[] result = new XElement[size];
		for (int i = 0; i < size; i++) {
			result[i] = ((ElementNodeNode)path[pathSize-size+i]).getElement();
		}
		return result;
	}
	
	protected TreeView getTreeView() {
		return treeCombobox.getTreeView();
	}

	private final class LazyExpander {
		private final ITreeModelListener treeModelListener = new ITreeModelListener() {

			public void treeNodesChanged(TreeModelEvent e) {}

			public void treeNodesInserted(TreeModelEvent e) {}

			public void treeNodesRemoved(TreeModelEvent e) {}

			public void treeStructureChanged(TreeModelEvent e) {
				doExpand();
			}
			
		};
		private XElement[] path;
		private int pathIndex;
		private ITreeViewItem parent;
		
		public LazyExpander() {
			
		}

		private void subscribe() {
			getTreeModel().addTreeModelListener(treeModelListener);
		}
		
		public void expand(XElement[] path) {
			subscribe();
			this.path = path;
			this.pathIndex = 0;
			this.parent = getTreeView();
			doExpand();
		}

		protected void doExpand() {
			if(path == null)
				return;
			if(pathIndex< path.length) {
				XElement element = path[pathIndex];
				ITreeViewItem item = findItem(element);
				if(item != null) {
					item.setState(true);
					pathIndex ++;
					parent = item;
					doExpand();
				}
			}
			else {
				path = null;
				unsubscribe();
			}
		}

		private void unsubscribe() {
			getTreeModel().removeTreeModelListener(treeModelListener);
		}
		
		protected PaloTreeNode getPaloTreeNode(ITreeViewItem item){
			return (PaloTreeNode)item.getNode();
		}

		private ITreeViewItem findItem(XElement element) {
			ITreeViewItem result = null;
			PaloTreeNode parentNode = getPaloTreeNode(parent);
			if(parentNode.isInitialized()) {
				int size = parent.getChildCount();
				for(int i = 0; i < size; i ++ ) {
					ITreeViewItem item =  parent.getChildItem(i);
					Object node = item.getNode();
					if (node instanceof ElementNodeNode) {
						ElementNodeNode elementNode = (ElementNodeNode) node;
						if(XHelper.equalsById(elementNode.getElement(),element)){
							result = item;
							break;
						}
					}
				}
			}
			else
				parentNode.load();
			return result;
		}
	}

}
