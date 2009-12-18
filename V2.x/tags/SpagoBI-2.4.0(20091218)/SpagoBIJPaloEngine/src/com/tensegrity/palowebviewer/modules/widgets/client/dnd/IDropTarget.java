package com.tensegrity.palowebviewer.modules.widgets.client.dnd;

import com.google.gwt.user.client.ui.Widget;


/**
 * Provieds interface to a widget that can serve as a drop target in Drag & Drop operations.
 *
 */
public interface IDropTarget
{
	
	public void removeDragObject(Widget widget);

    public boolean canAcceptDrop(Widget widget, int x, int y);

    public void acceptDrop(Widget widget, int x, int y);
    
    public void cancelDrop(Widget widget);


}
