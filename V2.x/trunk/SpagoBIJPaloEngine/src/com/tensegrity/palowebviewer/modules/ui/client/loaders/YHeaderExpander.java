package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.ui.client.XCubeEditor;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.ICubeTableModel;

public class YHeaderExpander extends HeaderExpander {

	public YHeaderExpander(XCubeEditor editor) {
		super(editor);
	}

	protected CubeHeaderModel getHeader() {
		ICubeTableModel cubeTableModel = getCubeTableModel();
		return cubeTableModel.getYHeaderModel(); 
	}

	public String getDescription() {
		return "YHeaderExpander";
	}

}