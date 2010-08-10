package com.tensegrity.palowebviewer.modules.ui.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;

/**
 * Factory that generates Editors for {@link XObjects}.
 */
public interface IXEditorFactory
{

    /**
     * Create editor for given {@link XObject}.
     *
     * @param object object for which to create editor.
     *
     * @throws IllegalArgumentException if object is null or it's class is different from XCube
     * @return editor for the object.
     */
    public IXObjectEditor getEditor(XObject object) throws IllegalArgumentException;

}
