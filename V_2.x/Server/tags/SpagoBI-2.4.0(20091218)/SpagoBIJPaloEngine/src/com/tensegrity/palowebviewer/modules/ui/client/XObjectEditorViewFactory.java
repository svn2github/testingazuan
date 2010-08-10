package com.tensegrity.palowebviewer.modules.ui.client;

import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.IFormatter;

public class XObjectEditorViewFactory implements IXObjectEditorViewFactory {

    private final UIManager uiManager;
    private final IFormatter formatter;

    /* (non-Javadoc)
     * @see com.tensegrity.palowebviewer.modules.ui.client.IXObjectEditorViewFactory#getXObjectEditorViewFactory(com.tensegrity.palowebviewer.modules.engine.client.IXObjectEditor)
     */
    public Widget getXObjectEditorViewFactory(IXObjectEditor xObjectEditor) {
        if ( xObjectEditor instanceof XCubeEditor) {
            return new CubeEditorView((XCubeEditor)xObjectEditor, uiManager, formatter);
        } 
        else {
            throw new IllegalArgumentException("Unsupported XObject class: " + xObjectEditor);
        }
    }

    public XObjectEditorViewFactory(UIManager manger, IFormatter formatter) {
        if(manger == null)
            throw new IllegalArgumentException("UIManager can not be null.");
        this.uiManager = manger;
        this.formatter = formatter;
    }

}
