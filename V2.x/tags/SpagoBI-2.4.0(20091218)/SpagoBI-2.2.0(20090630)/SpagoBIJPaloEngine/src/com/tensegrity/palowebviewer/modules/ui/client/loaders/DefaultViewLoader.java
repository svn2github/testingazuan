package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.engine.client.AbstractServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModelListener;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.ui.client.XCubeEditor;

public class DefaultViewLoader extends AbstractLoader {
	
	private final IPaloServerModelListener serverModelListener = new AbstractServerModelListener() {
		
		public void defaultViewLoaded(XCube cube) {
			if(cube == getCube()) {
				unsubscribe();
				executeCallback();
			}
		}
		
	};
	private XCubeEditor editor;
	
	public DefaultViewLoader(XCubeEditor editor) {
		this.editor = editor;
	}
	
	public XCube getCube() {
		return editor.getCube();
	}
	
	public IPaloServerModel getServerModel() {
		return editor.getPaloServerModel();
	}

	public void load() {
		if(isDefaultViewLoaded()){
			executeCallback();
		}
		else {
			loadDefaultView();
		}
	}

	private void unsubscribe() {
		getServerModel().removeListener(serverModelListener);
	}

	protected void loadDefaultView() {
		subscribe();
		getServerModel().loadDefaultView(getCube());
	}

	private void subscribe() {
		getServerModel().addListener(serverModelListener);
	}

	protected boolean isDefaultViewLoaded() {
		return getCube().getDefaultView() != null;
	}

	public String getDescription() {
		return "DefaultViewLoader";
	}

}
