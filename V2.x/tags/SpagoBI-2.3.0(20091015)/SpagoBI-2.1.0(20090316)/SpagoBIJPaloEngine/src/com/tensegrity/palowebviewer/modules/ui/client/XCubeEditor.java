package com.tensegrity.palowebviewer.modules.ui.client;

import com.tensegrity.palowebviewer.modules.engine.client.IEditorSaveCallback;
import com.tensegrity.palowebviewer.modules.engine.client.IEngine;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XConsolidatedElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.TypeCastVisitor;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.AbstractCubeTableModelListener;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeTableModel;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.ICubeTableModel;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.ICubeTableModelListener;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.IDimensionList;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.ErrorDialog;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.SaveViewAsDialog;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.SaveViewAsDialogListener;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;
import com.tensegrity.palowebviewer.modules.ui.client.loaders.ILoaderCallback;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;
import com.tensegrity.palowebviewer.modules.util.client.ICallback;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;


/**
 * Model of the cube editor. Used to load and maintain state of displayed 
 * {@link XCube) or {@link XView}.
 *
 */
public class XCubeEditor extends AbstractXObjectEditor
{

	private ICubeTableModel cubeTableModel;
	private PaloTreeModel treeModel;
    private XView xView;
    private IsObjectPartVisitor isObjectPartVisitor = new IsObjectPartVisitor();
    private CubeEditorLoader loader;
    private boolean loaded = false;
    private boolean loading = false;
    private final ILoaderCallback loaderCallback = new ILoaderCallback() {

		public void loaded() {
			setLoaded(true);
			loading = false;
			setModified(false);
			getCubeTableModel().setAllowDataLoad(true);
			executeCallback();
		}

    };
	private ILoaderCallback callback;
	private int showLevels;
	
    /**
     * Return XCube of the editor.
     */
    public XCube getCube() {
        return (XCube)super.getXObject();
    }

    public XCubeEditor (IEngine engine, PaloTreeModel treeModel, XCube cube) {
        this(engine, treeModel, cube, null);
    }

    public XCubeEditor (IEngine engine, PaloTreeModel treeModel, XCube cube, XView view) {
        super(engine, cube);
        this.treeModel = treeModel;
        this.xView = view;
        saveAsAction.setEnabled(true);
        saveAction.setEnabled(false);
        addEditorListener(cubeEditorListener);
        loader = new CubeEditorLoader(this);
        loader.setCallback(loaderCallback);
    }
    
    public void setLoaderCallback(ILoaderCallback callback) {
    	this.callback = callback;
    }
    
    public void setShowLevels(int value) {
    	this.showLevels = value;
    	this.loader.setShowLevels(value);
    }
    
    public boolean isLoaded () {
    	return loaded;
    }
    
    public void load() {
    	if(!loading) {
    		loading = true;
    		loader.load();
    	}
    }

    public XView getView () {
        return xView;
    }
    
    public void setModified(boolean value) {
    	if(isLoaded()){
    		Logger.debug(this+".setModified("+value+")");
    		super.setModified(value);
    	}
    }
    
    public int getShowLevels() {
    	return showLevels;
    }
    
    public String toString() {
    	return "XCubeEditor["+getXObject().getName()+"]";
    }

    /**
     * {@inheritDoc}
     */    
    public String getTitle() {
    	String r = (getView() == null) ? getCube().getName() : getView().getName();
        return r;
    }
    
    public boolean isInitialized() {
    	boolean result = loadCube();
    	return result;
    }
    
    protected void onSourceChanged() {
    	if(isLoaded())
    		reinitCubeTableModel();
    }
    
    protected boolean hasChanged(XObject[] path, XObject[] oldChildren, int type) {
    	XObject object = path[path.length -1];
    	boolean r = false;
    	switch (type) {
		case IXConsts.TYPE_DIMENSION:
			r = (object == getCube()); 
			break;
		case IXConsts.TYPE_AXIS:
			r = (object == getView());
			break;
		case IXConsts.TYPE_ELEMENT_NODE:
			if(oldChildren != null){
				r = checkBelonging(object);
			}
			break;
		}
    	return r; 
    }
    
	private boolean checkBelonging(XObject object) {
		boolean r = false;
		ICubeTableModel model = cubeTableModel;
		if(model != null){
			XObject parent = XHelper.findBackByType(object, IXConsts.TYPE_SUBSET);
			if(parent == null) parent = XHelper.findBackByType(object, IXConsts.TYPE_DIMENSION);
			r = checkBelonging(parent, model.getXDimensions()) || checkBelonging(parent, model.getYDimensions());  
		}
		return r;
	}

	private boolean checkBelonging(XObject parent, IDimensionList dimensions) {
		boolean r = false;
		int size = dimensions.getDimCount();
		for (int i = 0; i < size; ++i) {
			IDimensionModel model = dimensions.getDimModel(i);
			ITreeModel treeModel = model.getTreeModel();
			PaloTreeNode rootNode = (PaloTreeNode) treeModel.getRoot();
			r = rootNode.getXObject() == parent;
		}
		return r;
	}

	private void setLoaded(boolean value) {
		loaded = value;
	}
	
	protected void executeCallback() {
		if(callback != null)
			callback.loaded();
	}
    
    protected void reinitCubeTableModel() {
    	if(cubeTableModel != null){
    		cubeTableModel.dispose();
    	}
    	try {
    	cubeTableModel = new CubeTableModel(getEngine(), treeModel, getCube(), getView());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	cubeTableModel.addListener(modelListener);
    	cubeTableModel.setShowLevels(getShowLevels());
    }
    
    protected boolean loadCube() {
    	boolean result = loadDimensions();
    	result &= loadView();
    	return result;
    }
    
    protected boolean loadView() {
    	boolean result =  true;
    	XView view = getView();
    	if(view != null && view.getAxises() == null) {
    		result = false;
    		IPaloServerModel paloServerModel = getPaloServerModel();
    		paloServerModel.loadChildren(view, IXConsts.TYPE_AXIS);
    	}
    	return result;
    }
    
    protected boolean loadDimensions() {
    	boolean result = loadDBDimensions();
    	if(result)
    		result = getCube().getDimensions() != null; 
    	if(!result){
    		IPaloServerModel paloServerModel = getPaloServerModel();
    		paloServerModel.loadChildren(getCube(), IXConsts.TYPE_DIMENSION);
    	}
    	return result;
    }
    
    protected boolean loadDBDimensions() {
    	XDatabase db = (XDatabase)getCube().getParent();
    	boolean result = db.getDimensions() != null;
    	if(!result) {
    		IPaloServerModel paloServerModel = getPaloServerModel();
    		paloServerModel.loadChildren(db, IXConsts.TYPE_DIMENSION);
    	}
    	return result;
    }
    
    protected void doSave(IEditorSaveCallback callback) {
    	//for view only
		XView view = getCubeTableModel().rebuildView();
		ViewSavedCallback viewSavedCallback = new ViewSavedCallback();
		viewSavedCallback.setView(view);
		viewSavedCallback.setCallback(callback);
		getPaloServerModel().saveView(view, viewSavedCallback);
    }

    protected void doSaveAs(IEditorSaveCallback callback) {
		SaveViewAsDialog dialog = new SaveViewAsDialog();
		dialog.addListener(new SaveViewAsListener(dialog, callback));
		dialog.showDialog();
    }
    
	public final ICubeTableModel getCubeTableModel() {
		if(cubeTableModel == null)
			reinitCubeTableModel();
		return cubeTableModel;
	}
	
	public XObject getXObject() {
		XObject r = getView();
		if(r == null) r= getCube();
		return r;
	}
	
	public void dispose() {
		if(cubeTableModel != null) {
			cubeTableModel.removeListener(modelListener);
			cubeTableModel.dispose();
		}
		super.dispose();
	}
	
	protected boolean isObjectPart(XObject object) {
		isObjectPartVisitor.visit(object);
		return isObjectPartVisitor.getResult();
	}

	private class SaveViewAsListener implements SaveViewAsDialogListener{
		
		private IEditorSaveCallback callback;
		private SaveViewAsDialog dialog;

		public SaveViewAsListener(SaveViewAsDialog dialog, IEditorSaveCallback callback) {
			this.callback = callback;
			this.dialog = dialog;
		}

		public void onCancel() {
			dialog.hide();
		}

		public void onOk(String name, String description) {
			if("".equals(name)){
				ErrorDialog.showError("name can't be empty");
			}
			else {
					XView view = getCubeTableModel().createView();
					view.setId(XView.NEW_ID);
					view.setName(name);
					view.setDescription(description);
					ViewSavedCallback callback = new ViewSavedCallback();
					callback.setDialog(dialog);
					callback.setView(view);
					callback.setCallback(this.callback);
					getPaloServerModel().saveView(view, callback);
			}
		}
    	
    };
    
    private class ViewSavedCallback implements ICallback {
    	
    	private SaveViewAsDialog dialog;
		private XView view;
		private IEditorSaveCallback callback;

		ViewSavedCallback() {
    		
    	}
    	
		public void setCallback(IEditorSaveCallback callback) {
			this.callback = callback;
		}

		public void setView(XView view) {
			this.view = view;
		}

		public void setDialog(SaveViewAsDialog dialog) {
			this.dialog = dialog;
		}

		public void execute() {
			if(dialog!= null) {
				dialog.hide();
			}
			xView = view;
			setModified(false);
			if(callback != null){
				callback.onSave();
			}
		}
	}
    
    private final ICubeTableModelListener modelListener = new AbstractCubeTableModelListener() {
    	private boolean allowSave = false;

		public void modified() {
			if(allowSave == false){
				allowSave = true;
				saveAsAction.setEnabled(true);
			}
			setModified(true);
		}
    	
    };

    protected IEditorListener cubeEditorListener = new IEditorListener(){

		public void onModified(IXObjectEditor editor) {
			getSaveAsAction().setEnabled(true);
			getSaveAction().setEnabled(getView() != null); //only for view.
		}

		public void onSourceChanged(IXObjectEditor editor) {
		}

		public void onUnmodified(IXObjectEditor editor) {
			getSaveAction().setEnabled(false);
		}

		public void onObjectRenamed(IXObjectEditor editor) {
		}
    	
    };
    
	public void doSaveModified(IEditorSaveCallback callback) {
		if(getView() == null){
			doSaveAs(callback);
		}else{
			doSave(callback);
		}
	}

	private class IsObjectPartVisitor extends TypeCastVisitor {
		
		private boolean result = false;
		
		public boolean hasFinished() {
			return getResult();
		}
		
		public boolean getResult() {
			return result;
		}
		
		public void visit(XObject object) {
			result = false;
			super.visit(object);
		}

		public void visitAxis(XAxis axis) {
			if(!isModified()) {
				XView view = getView();
				if(view != null) {
					XAxis[] axes = view.getAxises();
					for (int i = 0; i < axes.length && !result; i++) {
						result = axes[i] == axis;
					}
				}
			}
		}

		public void visitConsolidatedElement(XConsolidatedElement consolidatedElement) {
			//visitElement(consolidatedElement);
		}

		public void visitCube(XCube cube) {
			result = cube == getCube();
		}

		public void visitDatabase(XDatabase database) {
		}

		public void visitDimension(XDimension dimension) {
			XDimension[] dimensions = getCube().getDimensions();
			for (int i = 0; i < dimensions.length && !result; i++) {
				result = dimensions[i] == dimension;
			}
		}

		public void visitElement(XElement element) {
		}

		public void visitRoot(XRoot root) {
		}

		public void visitServer(XServer server) {
		}

		public void visitSubset(XSubset subset) {
		}

		public void visitView(XView view) {
			if(!isModified())
				result = view == getView();
		}

		public void visitElementNode(XElementNode node) {
		
		}
		
	}
	
}
