package com.tensegrity.palowebviewer.modules.ui.client.loaders;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.ui.client.XCubeEditor;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.HeaderLevelExpander;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.ICubeTableModel;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.IExpanderCallback;

public abstract class HeaderExpander extends AbstractLoader {
	private int showLevels = 1;
	private final XCubeEditor editor;
	private final IExpanderCallback expanderCallback = new IExpanderCallback () {

		public void expanded() {
			executeCallback();
		}
		
	};
	
	public HeaderExpander(XCubeEditor editor) {
		this.editor = editor;
	}
	
	public void setShowLevels(int value) {
		showLevels = value;
	}

	public void load() {
		XObject object = getEditor().getXObject();
		if(object.getTypeID() == IXConsts.TYPE_CUBE) {
			CubeHeaderModel header = getHeader();
			HeaderLevelExpander expander = new HeaderLevelExpander(header, showLevels, expanderCallback);
			expander.expand();
		}
		else {
			executeCallback();
		}
	}

	protected XCubeEditor getEditor() {
		return editor;
	}
	
	protected ICubeTableModel getCubeTableModel() {
		return getEditor().getCubeTableModel();
	}
	
	protected abstract CubeHeaderModel getHeader();

}