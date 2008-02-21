package com.tensegrity.palowebviewer.modules.ui.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.engine.client.AbstractServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IEditorCloseCallback;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModelListener;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.ErrorDialog;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.IErrorDialogCallback;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.TaskQueue;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.ActionProxy;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;
import com.tensegrity.palowebviewer.modules.widgets.client.tab.DefaultTabElement;
import com.tensegrity.palowebviewer.modules.widgets.client.tab.DefaultTabPanelModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabActionListener;
import com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabCloseCallback;
import com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabElement;
import com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabPanelModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tab.TabPanelModelListener;

public class TabManager {


    private final IXEditorFactory xEditorFactory;
    private final IXObjectEditorViewFactory xObjectEditorViewFactory;
    private final IIconFactory iconFactory;
    private final ITabPanelModel tabPanelModel;
    private boolean onFlag;
    private final ActionProxy saveAsActionProxy;
    private final ActionProxy saveActionProxy;
    private final IPaloServerModel paloServerModel;
    private final List openingObject = new ArrayList();
    


    public TabManager(IXEditorFactory xEditorFactory, IXObjectEditorViewFactory xObjectEditorViewFactory,
    		IIconFactory iconFactory, IPaloServerModel paloServerModel) {
        this.xEditorFactory = xEditorFactory;
        this.xObjectEditorViewFactory = xObjectEditorViewFactory;
        this.iconFactory = iconFactory;
        saveAsActionProxy = new ActionProxy();
        saveActionProxy = new ActionProxy();
        tabPanelModel = new DefaultTabPanelModel();
        tabPanelModel.addTabPanelModelListeners(tabPanelModelListener);
        this.paloServerModel = paloServerModel;
        paloServerModel.addListener(serverModelListener);
        onFlag = false;
    }

    public void openXObjectEditorTab(XObject xObject) {
    	if(!selectExistingTab(xObject)) {
    		openNewTab(xObject);
    	}
    }

	private void openNewTab(XObject xObject) {
   	 // if not, open a new one
        if(!openingObject.contains(xObject)) {
        	openingObject.add(xObject);
        	TaskQueue.getInstance().add(new OpenEditorTask(xObject));
        }
	}

	private boolean selectExistingTab(XObject xObject) {
		boolean result = false;
		for (Iterator it = tabPanelModel.getTabs().iterator(); it.hasNext(); ){
            ITabElement tab = (ITabElement)it.next();
            Object attachment = tab.getAttachedObject();
            if(attachment instanceof IXObjectEditor && attachment != null){
                IXObjectEditor editor = (IXObjectEditor)attachment;
                if ( xObject == editor.getXObject() ) {
                    tabPanelModel.selectTab(tab);
                    result = true;
                    break;
                }
            }
        }
		return result;
	}

    private final class OpenEditorTask implements ITask{
    	private final XObject xObject;
    	
    	public OpenEditorTask(XObject object) {
    		this.xObject = object;
    	}

		public void execute() {
	        IXObjectEditor xObjectEditor = xEditorFactory.getEditor(xObject);
	        Widget xObjectEditorView = xObjectEditorViewFactory.getXObjectEditorViewFactory(xObjectEditor);
	        String icon = iconFactory.getIconUrl(xObjectEditor);
	        DefaultTabElement tab = new DefaultTabElement(icon, xObjectEditor.getTitle(), 
	        		true, xObjectEditorView, tabPanelModel, new EditorActionsDelegator(xObjectEditor));
	        xObjectEditor.addEditorListener(new TabTitleChangeListener(tab));
	        tab.attachObject(xObjectEditor);
	        tabPanelModel.add(tab);
	        openingObject.remove(xObject);
		}
		
		public String getName() {
			return "OpenEditorTask";
		}

    	
    }

    public void turnOn() {
        onFlag = true;
    }

    public void turnOff() {
        onFlag = false;

        while ( tabPanelModel.getTabs().size() > 0 ) {
        	ITabElement tabElement = (ITabElement)tabPanelModel.getTabs().get(0);
            tabPanelModel.forceClose(tabElement);
        }
    }

    public boolean hasModifiedTabs() {
        // TODO implement hasModifiedTabs()
        return false;
    }

    public ITabPanelModel getTabPanelModel() {
        return tabPanelModel;
    }

    public IAction getSaveAction() {
        return saveActionProxy;
    }

    public IAction getSaveAsAction() {
        return saveAsActionProxy;
    }
    
    private final IPaloServerModelListener serverModelListener = new AbstractServerModelListener() {

		public void modelChanged() {
		}

		public void onChildArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
			closeInvalideObjectTabs();
		}

		private void findInvalidObjects(final List closableTabs, List names) {
			for (Iterator it = tabPanelModel.getTabs().iterator(); it.hasNext(); ){
	            ITabElement tab = (ITabElement)it.next();
	            Object attachment = tab.getAttachedObject();
	            if(attachment instanceof IXObjectEditor && attachment != null){
	                IXObjectEditor editor = (IXObjectEditor)attachment;
	                XObject object = editor.getXObject();
	                if(!paloServerModel.isObjectValid(object)){
	                	closableTabs.add(tab);
	                	names.add(object.getName());
	                }
	            }
	        }
		}

		private String listToString(List names) {
			String objectNames = "";
			for (Iterator it = names.iterator(); it.hasNext();) {
				String name = (String) it.next();
				objectNames += name;
				if(it.hasNext()) 
					objectNames += ", ";
			}
			return objectNames;
		}
		
		public void objectInvalide(XPath path) {
			closeInvalideObjectTabs();
		}

		private void closeInvalideObjectTabs() {
			final List closableTabs = new ArrayList();
			List names = new ArrayList();
	        findInvalidObjects(closableTabs, names);
	        if(!closableTabs.isEmpty()){
	        	String objectNames = listToString(names);
	        	String text = "The following objects has been deleted : " + objectNames + ". Corresponding editors will be closed.";
	        	// String text = "ThefollowingobjectshasbeendeletedThefollowingobjectshasbeendeletedThefollowingobjectshasbeendeleted : " + objectNames + ". Corresponding editors will be closed.";
	        	ErrorDialog.showError(text, new IErrorDialogCallback(){
	        		public void onClose() {
	        			for (Iterator it = closableTabs.iterator(); it.hasNext();) {
	        				ITabElement tab = (ITabElement) it.next();
	        				tabPanelModel.forceClose(tab);
	        			}
	        		}
	        	});
	        }
		}

    	
    };

    private TabPanelModelListener tabPanelModelListener = new TabPanelModelListener() {

        public boolean onBeforeTabClosed(ITabElement tab) {
            if ( !onFlag )
                return true;
            // Send close message to all tabs
            return true;
        }

        public boolean onBeforeTabSelected(ITabElement tab) {
            return true;
        }

        public void onTabAdded(ITabElement tab) {
        }

        public void onTabClosed(ITabElement tab) {
        	Object attachment = tab.getAttachedObject();
            if(attachment instanceof IXObjectEditor && attachment != null){
                IXObjectEditor editor = (IXObjectEditor)attachment;
                editor.dispose();
            }
        }

        public void onTabSelected(ITabElement tab) {
            if(tab != null) {
                Object object = tab.getAttachedObject(); 
                if(object instanceof IXObjectEditor) {
                    IXObjectEditor editor =(IXObjectEditor)object;
                    saveAsActionProxy.setAction(editor.getSaveAsAction());
                    saveActionProxy.setAction(editor.getSaveAction());
                }

            }
            else {
                saveAsActionProxy.setAction(null);
                saveActionProxy.setAction(null);
            }
        }

		public void onTabTitleChanged(ITabElement tab) {
			// do nothing
		}

		public void onTabIconChanged(ITabElement tab) {
			//do nothing
		}

    };
    
    private class TabTitleChangeListener implements IEditorListener{
    	
    	private ITabElement element;
    	
    	public TabTitleChangeListener(ITabElement element){
    		this.element = element;
    	}
    	
		public void onModified(IXObjectEditor editor) {
			calculateTitle(editor);
		}

		public void onSourceChanged(IXObjectEditor editor) {
			element.setImgURL(iconFactory.getIconUrl(editor));
			calculateTitle(editor);
		}
		
		public void onUnmodified(IXObjectEditor editor) {
			calculateTitle(editor);
		}

		public void onObjectRenamed(IXObjectEditor editor) {
			calculateTitle(editor);
		}

		private void calculateTitle(IXObjectEditor editor) {
			boolean modifired = editor.isModified();
			String title = "";
			if(modifired)
				title += "*";
			title += editor.getTitle();
			element.setTitle(title);
		}
    	
    };
    
    private class EditorActionsDelegator implements ITabActionListener{

    	private IXObjectEditor editor;

		public EditorActionsDelegator(IXObjectEditor editor) {
			this.editor = editor;
		}

		public void tryClose(final ITabCloseCallback callback) {
			editor.close(new IEditorCloseCallback(){
				public void onClose() {
					callback.onClose();
				}
			});
		}
    	
    }
    


}
