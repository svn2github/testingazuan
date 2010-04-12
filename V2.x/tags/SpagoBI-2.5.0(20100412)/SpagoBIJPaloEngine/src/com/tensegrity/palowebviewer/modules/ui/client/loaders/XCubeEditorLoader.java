package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.ui.client.AbstractEditorListener;
import com.tensegrity.palowebviewer.modules.ui.client.IEditorListener;
import com.tensegrity.palowebviewer.modules.ui.client.IXObjectEditor;
import com.tensegrity.palowebviewer.modules.ui.client.XCubeEditor;

public class XCubeEditorLoader extends AbstractLoader {
	private final IEditorListener editorListener = new AbstractEditorListener() {

		public void onSourceChanged(IXObjectEditor editor) {
			if(cubeEditor.isInitialized()){
				cubeEditor.removeEditorListener(this);
				executeCallback();
			}
		}

	};
	private final XCubeEditor cubeEditor;
	
	public XCubeEditorLoader(XCubeEditor cubeEditor) {
		this.cubeEditor = cubeEditor;
	}
	
	public void load() {
		cubeEditor.addEditorListener(editorListener);
		editorListener.onSourceChanged(cubeEditor);	
	}
	
	public String getDescription() {
		return "XCubeEditorLoader";
	}

}