package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.ui.client.XCubeEditor;

public class FinalCubeEditorLoader extends AbstractLoader {
	
	private XCubeEditor editor;

	public FinalCubeEditorLoader(XCubeEditor editor) {
		this.editor = editor;
	}

	public void load() {
		//editor.setModified(false);
		editor.getCubeTableModel().setAllowDataLoad(true);
		executeCallback();
	}

	public String getDescription() {
		return "FinalCubeEditorLoader";
	}

}
