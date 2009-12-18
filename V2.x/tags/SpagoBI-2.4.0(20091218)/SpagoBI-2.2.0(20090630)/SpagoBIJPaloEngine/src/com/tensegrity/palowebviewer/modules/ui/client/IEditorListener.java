package com.tensegrity.palowebviewer.modules.ui.client;



/**
 * Event listener for {@link IXObjectEditor}
 */
public interface IEditorListener
{

    /**
     * This method will be invoked if some unsaved changed were 
     * made to the {@link XObject}.
     * @param editor - the source of the event;
     */
    public void onModified(IXObjectEditor editor);

    /**
     * This method will be invoked if changed were saved or undone.
     * @param editor - the source of the event;
     */
    public void onUnmodified(IXObjectEditor editor);

    /**
     * This method will be invoked if {@link  XObject} has been changed some way
     * @param editor - the source of the event;
     */
    public void onSourceChanged(IXObjectEditor editor);
    
    /**
     * This method will be invoked if {@link  XObject} has been renamed
     * @param editor - the source of the event;
     */
    public void onObjectRenamed(IXObjectEditor editor);


}
