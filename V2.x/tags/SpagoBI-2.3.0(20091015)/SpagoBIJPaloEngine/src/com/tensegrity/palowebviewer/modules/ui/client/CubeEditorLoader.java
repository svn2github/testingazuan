package com.tensegrity.palowebviewer.modules.ui.client;

import com.tensegrity.palowebviewer.modules.ui.client.loaders.CubeTableModelLoader;
import com.tensegrity.palowebviewer.modules.ui.client.loaders.DefaultViewLoader;
import com.tensegrity.palowebviewer.modules.ui.client.loaders.ILoader;
import com.tensegrity.palowebviewer.modules.ui.client.loaders.ILoaderCallback;
import com.tensegrity.palowebviewer.modules.ui.client.loaders.ViewExpanderLoader;
import com.tensegrity.palowebviewer.modules.ui.client.loaders.XCubeEditorLoader;
import com.tensegrity.palowebviewer.modules.ui.client.loaders.XHeaderExpander;
import com.tensegrity.palowebviewer.modules.ui.client.loaders.YHeaderExpander;


/**
 * Used to agregate Loaders of different parts of the {@link XCubeEditor}.
 *
 */
public class CubeEditorLoader {
	
	
	private final XCubeEditor cubeEditor;
	private ILoaderCallback callback;
	private int showLevels;

	public CubeEditorLoader(XCubeEditor cubeEditor) {
		this.cubeEditor = cubeEditor;
	}
	
	public void setShowLevels(int value) {
		showLevels = value;
	}
	
	public void load() {
		ILoader loader = cunstructLoaderChain();
		loader.load();
	}

	private XCubeEditorLoader cunstructLoaderChain() {
		XCubeEditorLoader editorLoader = new XCubeEditorLoader(cubeEditor);
		DefaultViewLoader defaultViewLoader = new DefaultViewLoader(cubeEditor);
		CubeTableModelLoader tableLoader = new CubeTableModelLoader(cubeEditor);
		XHeaderExpander xExpander = new XHeaderExpander(cubeEditor);
		xExpander.setShowLevels(showLevels);
		YHeaderExpander yExpander = new YHeaderExpander(cubeEditor);
		yExpander.setShowLevels(showLevels);
		ViewExpanderLoader viewExpander = new ViewExpanderLoader(cubeEditor);
		
		editorLoader.setCallback(defaultViewLoader);
		defaultViewLoader.setCallback(tableLoader);
		tableLoader.setCallback(xExpander);
		xExpander.setCallback(yExpander);
		yExpander.setCallback(viewExpander);
		viewExpander.setCallback(callback);
		return editorLoader;
	}
	
	public void setCallback(ILoaderCallback callback) {
		this.callback = callback;
	}
	
}
