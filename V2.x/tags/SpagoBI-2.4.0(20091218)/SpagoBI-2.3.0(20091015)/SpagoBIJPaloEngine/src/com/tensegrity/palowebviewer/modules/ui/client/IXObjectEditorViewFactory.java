package com.tensegrity.palowebviewer.modules.ui.client;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * View widgets for XObjectEditors factory 
 *
 */
public interface IXObjectEditorViewFactory {
	
	/**
	 * Returns Editor widget for the given xObjectEditor ( XCube, XView and etc. )
	 * 
	 * @param xObjectEditor Editor
	 * @return Editor view widget
	 */
	public Widget getXObjectEditorViewFactory(IXObjectEditor xObjectEditor);

}