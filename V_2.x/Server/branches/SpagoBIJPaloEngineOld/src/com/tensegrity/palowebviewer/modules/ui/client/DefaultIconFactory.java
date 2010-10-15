package com.tensegrity.palowebviewer.modules.ui.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;

/**
 * Default icon factory for different XObjects.
 *
 */
public class DefaultIconFactory implements IIconFactory {

	public String getIconUrl(IXObjectEditor editor) {
		String r = null;
		if (editor instanceof XCubeEditor) {
			XCubeEditor cubeEditor = (XCubeEditor) editor;
			XObject object = cubeEditor.getXObject();
			if (object instanceof XCube) {
				r = "themes/default/img/cube_on.gif";
			}else if(object instanceof XView){
				r = "themes/default/img/view.gif";
			}
		}
		return r;
	}

}
