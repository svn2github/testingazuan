package com.tensegrity.palowebviewer.modules.widgets.client;

import com.google.gwt.user.client.ui.Widget;


/**
 * Generates Widges to display given object. It is used in {@link TreeView} for example.
 *
 */
public interface IWidgetFactory
{

    public Widget createWidgetFor(Object object);

}
