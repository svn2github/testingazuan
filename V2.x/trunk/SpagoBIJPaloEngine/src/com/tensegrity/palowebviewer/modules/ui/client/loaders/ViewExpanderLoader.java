package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.ui.client.XCubeEditor;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.ICubeTableModel;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.INodeStateListener;

public class ViewExpanderLoader extends AbstractLoader {
	
	private final INodeStateListener nodeStateListener = new INodeStateListener() {

		public void nodeStateChanged(HeaderTreeNode node) {
			checkExpanded();
		}
		
	};
	private final XCubeEditor editor;
	
	
	public ViewExpanderLoader(XCubeEditor editor) {
		this.editor = editor;
	}

	public String getDescription() {
		return "ViewExpanderLoader";
	}

	public void load() {
		subscribe();
		checkExpanded();
	}

	private void checkExpanded() {
		if(getTableModel().isExpanded()){
			unsubscribe();
			executeCallback();
		}
	}

	private void subscribe() {
		ICubeTableModel model = getTableModel();
		model.getXHeaderModel().addNodeStateListener(nodeStateListener);
		model.getYHeaderModel().addNodeStateListener(nodeStateListener);
	}

	private ICubeTableModel getTableModel() {
		return editor.getCubeTableModel();
	}
	
	private void unsubscribe() {
		ICubeTableModel model = getTableModel();
		model.getXHeaderModel().removeNodeStateListener(nodeStateListener);
		model.getYHeaderModel().removeNodeStateListener(nodeStateListener);
	}


}
