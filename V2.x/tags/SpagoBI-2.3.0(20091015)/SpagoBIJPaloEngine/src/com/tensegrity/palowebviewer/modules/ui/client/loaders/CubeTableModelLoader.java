package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.ui.client.XCubeEditor;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.AbstractCubeTableModelListener;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.ICubeTableModel;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.ICubeTableModelListener;

public class CubeTableModelLoader extends AbstractLoader {
	
	private final XCubeEditor editor;
	private final ICubeTableModelListener tableModelListener = new AbstractCubeTableModelListener() {

		public void structureChanged() {
			checkLoaded();
		}

		public void selectionChanged() {
			checkLoaded();
		}
		
	};
	private ICubeTableModel tableModel;
	private boolean loaded = false;


	public CubeTableModelLoader(XCubeEditor editor) {
		this.editor = editor;
	}

	public void load() {
		loaded = false;
		this.tableModel = editor.getCubeTableModel();
		tableModel.addListener(tableModelListener);
		if(tableModel.getXHeaderModel().getRoot() != null || tableModel.isLoaded() && !tableModel.isModelValid()) {
			tableModelListener.structureChanged();
		}
		else {
			//TODO: this is a hack, get rid of it.
			tableModel.isModelValid();
		}
	}

	public String getDescription() {
		return "CubeTableModelLoader";
	}

	private void checkLoaded() {
		if(!loaded && tableModel.isLoaded()){
			loaded = true;
			tableModel.removeListener(tableModelListener);
			executeCallback();
		}
	}

}