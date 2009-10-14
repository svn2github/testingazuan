package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.ui.client.XCubeEditor;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.ICubeTableModel;

public class XHeaderExpander extends HeaderExpander {

	public XHeaderExpander(XCubeEditor editor) {
		super(editor);
	}

	protected CubeHeaderModel getHeader() {
		ICubeTableModel cubeTableModel = getCubeTableModel();
		return cubeTableModel.getXHeaderModel(); 
	}
	
	public String getDescription() {
		return "XHeaderExpander";
	}

}