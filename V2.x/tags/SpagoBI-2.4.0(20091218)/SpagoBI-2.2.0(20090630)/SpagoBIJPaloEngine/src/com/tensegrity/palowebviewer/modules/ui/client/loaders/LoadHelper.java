package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.ui.client.XCubeEditor;

public class LoadHelper {
	
	public void loadCubeEditor(XCubeEditor editor, ILoaderCallback callback, int showLevels) {
		LoaderChain loaderChain = constructCubeEditorLoader(editor, showLevels);
		loaderChain.setCallback(callback);
		loaderChain.load();
	}

	public LoaderChain constructCubeEditorLoader(XCubeEditor editor, int showLevels) {
		LoaderChain loaderChain = new LoaderChain();
		loaderChain.addLoader(new XCubeEditorLoader(editor));
		loaderChain.addLoader(constructCubeTableLoader(editor));
		XHeaderExpander xExpander = new XHeaderExpander(editor);
		xExpander.setShowLevels(showLevels);
		loaderChain.addLoader(xExpander);
		YHeaderExpander yExpander = new YHeaderExpander(editor);
		yExpander.setShowLevels(showLevels);
		loaderChain.addLoader(xExpander);
		loaderChain.addLoader(new ViewExpanderLoader(editor));
		loaderChain.addLoader(new FinalCubeEditorLoader(editor));
		return loaderChain;
	}

	public IChainLoader constructCubeTableLoader(XCubeEditor editor) {
		IPaloServerModel serverModel = editor.getEngine().getPaloServerModel();
		XCube cube = editor.getCube();
		ILoaderChain result = new LoaderChain();
		DatabaseDimensionLoader dbLoader = new DatabaseDimensionLoader((XDatabase)cube.getParent());
		dbLoader.setServerModel(serverModel);
		result.addLoader(dbLoader);
		XCubeLoader cubeLoader = new XCubeLoader();
		cubeLoader.setServerModel(serverModel);
		cubeLoader.setCube(cube);
		result.addLoader(cubeLoader);
		CubeDimensionsLoader cubeDimensionsLoader = new CubeDimensionsLoader();
		cubeDimensionsLoader.setServerModel(serverModel);
		cubeDimensionsLoader.setCube(cube);
		result.addLoader(cubeDimensionsLoader);
		return result; 
	}

}
