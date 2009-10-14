package com.tensegrity.palowebviewer.modules.ui.client;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.engine.client.AbstractServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IEditorCloseCallback;
import com.tensegrity.palowebviewer.modules.engine.client.IEditorSaveCallback;
import com.tensegrity.palowebviewer.modules.engine.client.IEngine;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModelListener;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.OfferSaveModifiedDialog;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.OfferSaveModifiedDialogListener;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.AbstractAction;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;
import com.tensegrity.palowebviewer.modules.widgets.client.util.GuiHelper;


/**
 * Abstract implementation for {@link IXObjectEditor} interface.
 * It provides support for listeners, event firing and actions.
 */
public abstract class AbstractXObjectEditor implements IXObjectEditor {

    private final List listenerList = new ArrayList();
    private XObject object;
    private IEngine engine;
    private boolean modified;
    private boolean sourceChanged = false;
    
 
    /**
     * Save action object.
     * Call to it's {@link  IAction#onActionPerformed} is delegated to {@link  #doSave} method.
     */
    protected final IAction saveAction = new AbstractAction() {

        public void onActionPerformed(Object arg) {
            doSave(null);
        }

    }; 

    /**
     * "Save As" action object.
     * Call to it's {@link  IAction#onActionPerformed} is delegated to {@link  #doSaveAs} method.
     */
    protected final IAction saveAsAction = new AbstractAction() {

        public void onActionPerformed(Object arg) {
            doSaveAs(null);
        }
    };
    
    public boolean isInitialized() {
    	return false;
    }
    
    protected abstract boolean isObjectPart(XObject object);
    
    private final IPaloServerModelListener serverModelListener = new AbstractServerModelListener() {


		public void onChildArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
			Logger.debug("AbstrctXObjectEditor["+getXObject()+"].onChildrenArryChanged(" + path[path.length-1]+")");
        	if(hasChanged(path, oldChildren, type))
        		sourceChanged = true;
		}

		public void onUpdateComplete() {
			if(sourceChanged && isInitialized()){
				sourceChanged = false;
				onSourceChanged();
				fireSourceChanged();
			}
		}
		
		public void objectRenamed(XObject object) {
			if(object.getId().equals(getXObject().getId())){
				fireObjectRenamed();
			}
		}

    };
    
    protected void onSourceChanged() {
    	
    }
    
    protected abstract boolean hasChanged(XObject[] path, XObject[] oldChildren, int type);

    /**
     * Returns link to {@link  IPaloServerModel palo server model} for the idited {@link  XObject}.
     */
    public IPaloServerModel getPaloServerModel() {
        return getEngine().getPaloServerModel();
    }
    
    public IEngine getEngine() {
        return engine;
    }

    /**
     * {@inheritDoc}
     */    
    public IAction getSaveAction() {
        return saveAction;
    }

    /**
     * {@inheritDoc}
     */    
    public IAction getSaveAsAction() {
        return saveAsAction;
    }

    /**
     * {@inheritDoc}
     */    
    public boolean isModified() {
        return modified;
    } 
    
    /**
     * {@inheritDoc}
     */
    public XObject getXObject() {
        return object;
    }

    public AbstractXObjectEditor(IEngine engine, XObject object) {
        this.engine = engine;
        getPaloServerModel().addListener(serverModelListener);
        setObject(object);
    }

    /**
     * {@inheritDoc}
     */
    public void addEditorListener(IEditorListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener can not be null.");
        listenerList.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeEditorListener(IEditorListener listener) {
        listenerList.remove(listener);
    }

    /**
     * Marks the editor as modified or unmodified.
     * @param modified - state to set;
     * If <code>modified</code> is true then state changed to modified and {@link  #saveAction} is enabled.
     * Else state changed to unmodified and {@link  #saveAction} is disabled.
     * Listeners are notified about the event.
     */
    protected void setModified(boolean modified) {
        //if(this.modified != modified) {
        this.modified = modified;
        if(this.modified)
            fireModified();
        else
            fireUnmodified();
            
        //}
    }

    /**
     * Call to this method is redirected from {@link  #saveAction}.
     */
    protected abstract void doSave(IEditorSaveCallback callback);

    /**
     * Call to this method is redirected from {@link  #saveAsAction}.
     */
    protected abstract void doSaveAs(IEditorSaveCallback callback);

    /**
     * Set {@link XObject} to edit.
     * @param object - object to set.
     */
    protected void setObject(XObject object) {
        this.object = object;
    }

    /**
     * Notifies editor listeners that editor now has unsaved changes.
     */
    protected void fireModified() {
    	Object[] listeners = listenerList.toArray();
    	for (int i = 0; i < listeners.length; i++) {
            IEditorListener listener = (IEditorListener)listeners[i];
            listener.onModified(this);
        } 
    }

    /**
     * Notifies editor listeners that editor now has no unsaved changes.
     */
    protected void fireUnmodified() {
    	Object[] listeners = listenerList.toArray();
    	for (int i = 0; i < listeners.length; i++) {
            IEditorListener listener = (IEditorListener)listeners[i];
            listener.onUnmodified(this);
        } 
    }

    /**
     * Notifies editor listeners that edited object has been changed.
     */
    protected void fireSourceChanged() {
    	Logger.debug("fireSourceChanged("+getXObject()+")");
    	Object[] listeners = listenerList.toArray();
    	for (int i = 0; i < listeners.length; i++) {
            IEditorListener listener = (IEditorListener)listeners[i];
            listener.onSourceChanged(this);
        } 
    }
    
	protected void fireObjectRenamed() {
    	Logger.debug("fireObjectRenamed("+getXObject()+")");
    	Object[] listeners = listenerList.toArray();
    	for (int i = 0; i < listeners.length; i++) {
            IEditorListener listener = (IEditorListener)listeners[i];
            listener.onObjectRenamed(this);
        } 
	}



	public void dispose() {
        IPaloServerModel paloServerModel = getPaloServerModel();
		if(paloServerModel != null)
            paloServerModel.removeListener(serverModelListener);
	}

	public void close(final IEditorCloseCallback callback){
		if(isModified()){
			String question = "'" + getTitle() + "' has been modified. Do you want to save it?";//TODO: localize it.
			final OfferSaveModifiedDialog dialog = new OfferSaveModifiedDialog(question);
			dialog.addListener(new SaveDialogListener(dialog, callback));
			GuiHelper.centerShowDialog(dialog);
		}
		else{
			callback.onClose();
		}
	}
	
	
	
	protected abstract void doSaveModified(IEditorSaveCallback callback);
	
	private class SaveDialogListener implements OfferSaveModifiedDialogListener {
		private final OfferSaveModifiedDialog dialog;
		private final IEditorCloseCallback callback;
		
		public SaveDialogListener(OfferSaveModifiedDialog dialog, IEditorCloseCallback callback) {
			this.dialog = dialog;
			this.callback = callback;
		}
		
		public void onCancel() {
			dialog.hide();
		}

		public void onNo() {
			dialog.hide();
			callback.onClose();
		}

		public void onYes() {
			dialog.hide();
			doSaveModified(new IEditorSaveCallback(){
				public void onSave() {
					callback.onClose();
				}
			});
		}
	}

}
