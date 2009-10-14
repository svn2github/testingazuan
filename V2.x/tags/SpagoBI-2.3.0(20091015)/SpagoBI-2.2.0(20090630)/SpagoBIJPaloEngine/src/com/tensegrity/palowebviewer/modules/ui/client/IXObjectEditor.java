package com.tensegrity.palowebviewer.modules.ui.client;

import com.tensegrity.palowebviewer.modules.engine.client.IEditorCloseCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;


/**
 * Interface for {@link XObject} editor. 
 */
public interface IXObjectEditor
{

    /**
     * Returns save action for this editor.
     */
    public IAction getSaveAction();

    /**
     * Returns "save as " action for this editor.
     * It could be disabled if "save as" action can not be performed in this editor.
     */
    public IAction getSaveAsAction();

    /**
     * Returns title for the editor.
     * 
     * @return editors title.
     */
    public String getTitle();

    /**
     * Returns XObject for the editor.
     * 
     * @return editors title.
     */
    public XObject getXObject();

    /**
     * Tells if there is any unsaved modifications.
     * 
     * @return true if there unsaved changes, false otherwise.
     */
    public boolean isModified();

    /**
     * Add listener for the editor. It will be notified about editor's events.
     */
    public void addEditorListener(IEditorListener listenere);

    /**
     * Remove editor listener.
     */
    public void removeEditorListener(IEditorListener listenere);

    /**
     * Dispose editor
     * (Unsubscribe all listeners)
     */
    public void dispose();
    
    /**
     * @return 
     */
    public void close(IEditorCloseCallback callback);
    
}
