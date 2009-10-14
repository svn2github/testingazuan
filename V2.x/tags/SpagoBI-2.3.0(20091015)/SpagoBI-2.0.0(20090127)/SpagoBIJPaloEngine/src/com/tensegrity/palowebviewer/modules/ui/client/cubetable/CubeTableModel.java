package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.Iterator;
import java.util.List;

import com.tensegrity.palowebviewer.modules.engine.client.ICellUpdateCallback;
import com.tensegrity.palowebviewer.modules.engine.client.IEngine;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserCallback;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessage;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.UserMessage;
import com.tensegrity.palowebviewer.modules.paloclient.client.IElementType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XConsolidatedType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XInvalidType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XNumericType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XStringType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.MutableXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.INodeStateListener;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.taskchain.IChainTask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.TaskQueue;
import com.tensegrity.palowebviewer.modules.widgets.client.combobox.IComboboxListener;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModelListener;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreeModelEvent;


public class CubeTableModel implements ICubeTableModel
{

	private final TaskQueue taskQueue = TaskQueue.getInstance();
    private final XCube cube;
    private boolean modelDisposed = false;
    private boolean modelValid = false;
    
    
    private boolean allowDataLoad = false;
    private final IPaloServerModel serverModel;
    private final PaloTreeModel treeModel;
    private XView view;
    private final CubeModelListenerCollection listeners = new CubeModelListenerCollection();
    
    private final XPointConstructor pointConstructor = new XPointConstructor(this);
    private final XQueryConstructor queryConstructor = new XQueryConstructor(this);
    private final CubeTableValidator tableValidator = new CubeTableValidator(this);


    private final CubeHeaderModel xHeader;
    private final CubeHeaderModel yHeader;
    private final ViewExpander viewExpander = new ViewExpander();
    private final AxisDimensionList xDimensions;
    private final AxisDimensionList yDimensions;
    private final DimensionList sliceDimensions;
    private final INodeStateListener xHeaderNodeStateListener = new INodeStateListener() {

        public void nodeStateChanged(HeaderTreeNode node) {
            if(listeners.isEventLocked())
                return;
            HeaderTreeNode yRoot = yHeader.getHeaderRoot();
			CubeTableModel cubeTableModel = CubeTableModel.this;
			NodeStateChangeTask task = new NodeStateChangeTask(cubeTableModel, node, yRoot, node.isOpen());
			task.setDescription(node.getNode()+"");
            taskQueue.add(task);
            fireModified();
            listeners.fireTopTreeNodeStateChanged(node);
        }

    };
    private final INodeStateListener yHeaderNodeStateListener = new INodeStateListener() {

        public void nodeStateChanged(HeaderTreeNode node) {
            if(listeners.isEventLocked())
                return;
            HeaderTreeNode xRoot = xHeader.getHeaderRoot();
			CubeTableModel cubeTableModel = CubeTableModel.this;
			NodeStateChangeTask task = new NodeStateChangeTask(cubeTableModel, xRoot, node, node.isOpen());
			task.setDescription(node.getNode()+"");
			taskQueue.add(task);
            fireModified();
            listeners.fireLeftTreeNodeStateChanged(node);
        }

    };
    private final ITreeModelListener topAxisTreeModelListener = new ITreeModelListener() {

        public void treeNodesChanged(TreeModelEvent e) {
        	fireChildrenChanged(e);
        }

        public void treeNodesInserted(TreeModelEvent e) {
        	fireChildrenChanged(e);
        }

        public void treeNodesRemoved(TreeModelEvent e) {
        	fireChildrenChanged(e);

        }

        public void treeStructureChanged(TreeModelEvent e) {
        	fireChildrenChanged(e);
        }
        
        private void fireChildrenChanged(TreeModelEvent e) {
        	ITreeModel model = e.getSource();
        	Object[] path = e.getPath();
        	if(path != null && path.length >1) {
        		Object parent = path[path.length -1];
        		listeners.fireTopChildrenChanged(model, parent);
        	}
        	else {
        		cubeStructureChanged();
        	}
        }

    };
    
    private final ITreeModelListener leftAxistreeModelListener = new ITreeModelListener() {

        public void treeNodesChanged(TreeModelEvent e) {
        	fireChildrenChanged(e);
        }

        public void treeNodesInserted(TreeModelEvent e) {
        	fireChildrenChanged(e);
        }

        public void treeNodesRemoved(TreeModelEvent e) {
        	fireChildrenChanged(e);
        }

        public void treeStructureChanged(TreeModelEvent e) {
        	fireChildrenChanged(e);
        }
        
        private void fireChildrenChanged(TreeModelEvent e) {
        	ITreeModel model = e.getSource();
        	Object[] path = e.getPath();
        	if(path != null && path.length >1) {
        		Object parent = path[path.length -1];
        		listeners.fireLeftChildrenChanged(model, parent);
        	}
        	else {
        		cubeStructureChanged();
        	}
        }


    };
    
    private final IComboboxListener subsetComboboxListener = new IComboboxListener() {

		public void onSelectionChanged(Object oldItem) {
			fireSelectionChanged();
			fireModified();
		}

		public void onInvalidItemSet(Object item) {
		}
    
    };
    private final IComboboxListener dimensnionComboboxListener = new IComboboxListener() {

        public void onSelectionChanged(Object oldItem) {
        	fireSelectionChanged();
        	if(!sliceDimensions.isCreating()&& isModelValid() && oldItem != null){
        		taskQueue.add(new SelectedElementChangeTask());
        		fireModified();
        	}
        }

		public void onInvalidItemSet(Object item) {
			if (item instanceof XElement) {
				XElement element = (XElement) item;
				IElementType type = element.getType();
	        	if (type instanceof XInvalidType) {
	        		String message = "The selected element '" + element.getName() + "' is invalid.";
	        		message += " First element will be selected.";
	        		IUserMessage msg = UserMessage.createWarningMsg(message, new ModifiedCallback());
	        		engine.getUserMessageQueue().pushMessage(msg);
				}
			}
		}

    };
    private final IDimensionListListener dimensinListListener = new IDimensionListListener() {

    	public void dimensionAdded(IDimensionList list, IDimensionModel model) {
            removeDimension(list, model);
			model.getSubsetListModel().addComboboxListener(subsetComboboxListener);
			fireModified();
            cubeStructureChanged();
		}

		public void dimensionRemoved(IDimensionList list, IDimensionModel model) {
			model.getSubsetListModel().removeComboboxListener(subsetComboboxListener);
		}

		public void dimensionMoved(IDimensionList list, IDimensionModel model) {
		
		}
   	
    };
    private final IDimensionListListener sliceDimensinListListener = new IDimensionListListener() {

		public void dimensionAdded(IDimensionList list, IDimensionModel model) {
			model.getComboboxModel().addComboboxListener(dimensnionComboboxListener);
		}

		public void dimensionRemoved(IDimensionList list, IDimensionModel model) {
			model.getComboboxModel().removeComboboxListener(dimensnionComboboxListener);
		}

		public void dimensionMoved(IDimensionList list, IDimensionModel model) {
			fireModified();
			
		}

    };
    private final IUserCallback invalidItemCallback = new IUserCallback() {

		public void execute() {
			fireModified();
		}
    	
    };
    
	private final IEngine engine;
	private ExpandRules rules;
	private boolean changeStructure;
	private int dataLoadCount;
    

    public CubeTableModel (IEngine engine, PaloTreeModel treeModel, XCube cube, XView view) {
        this.treeModel = treeModel;
        this.engine = engine;
        this.serverModel = engine.getPaloServerModel();
        this.cube = cube;
        this.view = view;
    	xHeader = new CubeHeaderModel(serverModel);
        yHeader = new CubeHeaderModel(serverModel);
        xDimensions = new AxisDimensionList(treeModel, engine, xHeader, topAxisTreeModelListener, invalidItemCallback);
        yDimensions = new AxisDimensionList(treeModel, engine, yHeader, leftAxistreeModelListener, invalidItemCallback);
        sliceDimensions = new DimensionList(treeModel, engine, invalidItemCallback);
        xDimensions.addListener(dimensinListListener);
        yDimensions.addListener(dimensinListListener);
        sliceDimensions.addListener(dimensinListListener);
        sliceDimensions.addListener(sliceDimensinListListener);
        xHeader.addNodeStateListener(xHeaderNodeStateListener);
        yHeader.addNodeStateListener(yHeaderNodeStateListener);
        viewExpander.addCubeHeader(xHeader);
        viewExpander.addCubeHeader(yHeader);
        try {
        	listeners.lockEvents();
            buildModel();
        }
        finally{
        	listeners.unlockEvents();
        }
    }
    
    public void setAllowDataLoad(boolean value) {
    	Logger.debug(getCube().getName()+".setAllowDataLoad("+value+")");
    	allowDataLoad = value;
    }
    
    public boolean isAllowDataLoad() {
    	return allowDataLoad && listeners.hasDataListeners();
    }

    public boolean canCellBeEdited(HeaderTreeNode xNode, HeaderTreeNode yNode) {
        //validate that point not contains consolidated elements
        MutableXPoint point = pointConstructor.getXPoint(xNode, yNode);
        List dimensions = point.getDimensions();
        boolean result = !isLoadingData(); //deny cell editing during query
        if(result){
            for (Iterator it = dimensions.iterator(); it.hasNext() && result;) {
                XPath dimension = (XPath) it.next();
                XPath element = point.getElementPath(dimension);
                XElement xElement = (XElement)serverModel.getObject(element);
                result = !xElement.getType().equals(XConsolidatedType.instance); 
            }
        }
        return result;
    }

    public boolean isLoadingData() {
		return dataLoadCount>0;
	}

	public void dispose() {
    	modelDisposed = true;
    	listeners.fireOnDispose();
        listeners.clear();
        xHeader.dispose();
        yHeader.dispose();
        sliceDimensions.dispose();
        xDimensions.dispose();
        yDimensions.dispose();
        Logger.debug("CubeTableModel has been disposed");
    }
    
	public boolean isExpanded() {
		return !getViewExpander().isExpanding();
	}

    public XCube getCube() {
        return cube;
    }
    
    public IEngine getEngine () {
    	return engine;
    }

    public IPaloServerModel getPaloServerModel() {
        return serverModel;
    }

    public IDimensionList getSliceDimensions() {
        return sliceDimensions;
    }

    protected PaloTreeModel getTreeModel() {
        return treeModel;
    }

    public XView getView() {
        return view;
    }
    
    public boolean isDisposed() {
    	return modelDisposed;
    }

    public IDimensionList getXDimensions() {
        return xDimensions;
    }

    public IDimensionList getYDimensions() {
        return yDimensions;
    }

    public CubeHeaderModel getXHeaderModel() {
        return xHeader;
    }

    public CubeHeaderModel getYHeaderModel() {
        return yHeader;
    }
    
    public ViewExpander getViewExpander() {
    	return viewExpander;
    }
    
    public boolean isLoaded() {
    	return tableValidator.isLoaded();
    }

    public boolean isSelectedElementsPlain() {
        boolean result = true;
        IDimensionList list = getSliceDimensions();
        final int size = list.getDimCount();
        for(int i = 0; i < size; i++ ) {
            XElement element = list.getDimModel(i).getSelectedElement();
            if (XConsolidatedType.instance.equals(element.getType())) {
                result = false;
                break;
            }
        }
        return result;
    }
    
    public boolean isCellConsolidated(HeaderTreeNode xNode, HeaderTreeNode yNode) {
    	boolean result = !isSelectedElementsPlain();
    	if(!result) {
    		result = XElementType.isConsolidated(xNode.getElement()) || XElementType.isConsolidated(yNode.getElement());
    	}
    	return result;
    }

    //updates cell value and then reloads data
    public void updateCell(HeaderTreeNode xNode, HeaderTreeNode yNode, IResultElement value) {
    	ITask task = new CellUpdateTask(xNode, yNode, value);
    	TaskQueue.getInstance().add(task);
    }

    public XView createView() {
        XView result = new XView();
        fillView(result);
        this.view = result;
        return result;
    }

    public XView rebuildView() {
        XView result = getView();
        fillView(result);
        return result;	
    }
    
    public void reloadData() {
        if(isModelValid() && isLoaded() && allowDataLoad) {
            if(getSliceDimensions().getDimCount() > 0){
            	listeners.fireZStateChanged(isSelectedElementsPlain());
            }
            else {
            	listeners.fireZStateChanged(true);
            }
            HeaderTreeNode xNode = getXHeaderModel().getHeaderRoot();
            HeaderTreeNode yNode = getYHeaderModel().getHeaderRoot();
            reloadData(xNode, yNode);
        }
    }
    
    public void reloadData(HeaderTreeNode xNode, HeaderTreeNode yNode) {
    	if(!serverModel.isUpdatingHierarchy()) {
    		DataReloader dataReloader = new DataReloader(this);
    		dataReloader.reloadData(xNode, yNode);
    		startDataLoad();
    	}
    }
    
    protected void startDataLoad() {
    	dataLoadCount++;
    	xHeader.setCanExpand(false);
    	yHeader.setCanExpand(false);
    }
    
    protected void finishDataLoad() {
    	if(dataLoadCount>0) {
    		dataLoadCount--;
    		if(dataLoadCount == 0) {
    			xHeader.setCanExpand(true);
    			yHeader.setCanExpand(true);
    		}
    	}
    	else {
    		Logger.warn("finishDataLoad() was called more then startDataLoad()");
    	}
    }

    public void addListener(ICubeTableModelListener listener) {
        listeners.addListener(listener);
    }

    public void removeListener(ICubeTableModelListener listener) {
        listeners.removeListener(listener);
    }
    
    public void addListener(IDataListener listener) {
        listeners.addListener(listener);
    }

    public void removeListener(IDataListener listener) {
        listeners.removeListener(listener);
    }

    //{{{3 Event firering


    protected void buildModel() {
        setModelValid(false);
        XCube cube = getCube();
        XView view = getView();
        XDimension[] xDimensions = new XDimension[0];
        XDimension[] yDimensions = xDimensions; 
        XDimension[] sliceDimensions = xDimensions;
        XSubset[] xSubsets = new XSubset[0];
        XSubset[] ySubsets = new XSubset[0];
        XSubset[] sliceSubsets = new XSubset[0];
        XElement[] sSelectedElements;
        XElement[] xSelectedElements;
        XElement[] ySelectedElements;
        if(!tableValidator.isViewValid()) {
        	view = cube.getDefaultView();
        }

        XAxis xAxis = view.getColsAxis();
        xDimensions = xAxis.getDimensions();
        xSubsets = xAxis.getSubsets();
        xSelectedElements = xAxis.getSelectedElements();

        XAxis yAxis = view.getRowsAxis();
        yDimensions = yAxis.getDimensions();
        ySubsets = yAxis.getSubsets();
        ySelectedElements = yAxis.getSelectedElements();

        XAxis sAxis = view.getSelectedAxis();
        sliceDimensions = sAxis.getDimensions();
        sliceSubsets = sAxis.getSubsets();
        

        sSelectedElements = sAxis.getSelectedElements();
        substituteWithModel(sSelectedElements, sliceDimensions);

        this.xDimensions.setDimensions(xDimensions, xSubsets, xSelectedElements);
        this.yDimensions.setDimensions(yDimensions, ySubsets, ySelectedElements);
        this.sliceDimensions.setDimensions(sliceDimensions, sliceSubsets, sSelectedElements);

        setModelValid(true);
        buildView();
    	rules = new ExpandRules(this);
    }
    
    public void setShowLevels(int value) {
    	rules.setLevel(value);
    }
    
    private void substituteWithModel(XElement[] selectedElemens, XDimension[] sliceDimensions) {
    	IPaloServerModel serverModel = getPaloServerModel();
    	for (int i = 0; i < selectedElemens.length; i++) {
    		XDimension dimension = sliceDimensions[i];
			XElement element = selectedElemens[i];
			if(element != null)
				selectedElemens[i] = serverModel.getElement(dimension, element);
		}
	}

	protected void buildView() {
        boolean valid = tableValidator.isViewValid();//validateView(view) && dimensionsValid;
		if(valid) {
            XAxis xAxis = view.getColsAxis();
            XAxis yAxis = view.getRowsAxis();
            viewExpander.addElementPaths(xAxis.getElementPaths());
            viewExpander.addElementPaths(yAxis.getElementPaths());
            /*
            XElement[][] expanded = xAxis.getExpanded();
            int[][][] repetitions = xAxis.getRepetitions();
            //xHeader.expand(expanded, repetitions, expanded.length);

            expanded = yAxis.getExpanded();
            repetitions = yAxis.getRepetitions();
            //yHeader.expand(expanded, repetitions, expanded.length);
             * 
             */
        }
    }

    protected void cubeStructureChanged() {
    	if(!tableValidator.isLoaded() || changeStructure)
    		return;
    	changeStructure = true;
    	if(isModelValid()) {
    		IChainTask lockEventTask = listeners.getLockEventsTask();
    		IChainTask unlockEventTask = listeners.getUnlockEventsTask();
    		IChainTask rebuildXHeaderTask = new RebuildHeaderTask(xDimensions);
    		IChainTask rebuildYHeaderTask = new RebuildHeaderTask(yDimensions);
    		IChainTask fireStructureChangeTask = listeners.getFireStructureChangedTask();
    		IChainTask expandViewTask = viewExpander.getExpandTask();
    		IChainTask ruleExpandTask = rules.getChainTask();
    		
    		lockEventTask.setNextTask(rebuildXHeaderTask);
    		rebuildXHeaderTask.setNextTask(rebuildYHeaderTask);
    		rebuildYHeaderTask.setNextTask(expandViewTask);
    		expandViewTask.setNextTask(ruleExpandTask);
    		ruleExpandTask.setNextTask(unlockEventTask);
    		unlockEventTask.setNextTask(fireStructureChangeTask);
    		lockEventTask.execute();
    	}
    	else {
    		listeners.fireStructureChanged();
    	}
    	changeStructure = false;
    }

    protected void fillView(XView view) {
        XAxis[] axes = new XAxis[3];
        axes[0] = fillColumns(view);
        axes[1] = fillRows(view);
        axes[2] = fillSelected(view);
        view.setAxises(axes);
        view.setParent(getCube());
    }

	private XAxis fillSelected(XView view) {
		XAxis axis = getSliceDimensions().createAxis();
        axis.setName(XAxis.SELECTED);
        axis.setParent(view);
        axis.setId(XObject.NEW_ID);
        return axis;
	}

	private XAxis fillRows(XView view) {
		IDimensionList dimList = getYDimensions();
		XAxis axis = createAxis(view, dimList, XAxis.ROWS);
        return axis;
	}

	private XAxis fillColumns(XView view) {
		IDimensionList dimList = getXDimensions();
		XAxis axis = createAxis(view, dimList, XAxis.COLUMNS);
        return axis;
	}

	private XAxis createAxis(XView view, IDimensionList dimList, String name) {
		XAxis axis = dimList.createAxis();
		axis.setName(name);
        axis.setParent(view);
        axis.setId(XObject.NEW_ID);
        XDimension[] dimensions = dimList.getDimensions();
        XElementPath[] paths = viewExpander.getExpandedPaths(dimensions);
        axis.setElementPaths(paths);
		return axis;
	}

    public IElementType getCellType(HeaderTreeNode xNode, HeaderTreeNode  yNode)  {
        final int size = getSliceDimensions().getDimCount();
        IElementType result = null;
        for( int i = 0 ; (i < size) && (result == null)  ; i++ ) { 
            IDimensionModel dimModel = getSliceDimensions().getDimModel(i);
            XElement element = dimModel.getSelectedElement();
            if(isStringTypeElement(element)){
                result = XStringType.instance;
            }
        }
        if((result == null) && !isStringTypeElement(xNode.getElement()) && !isStringTypeElement(yNode.getElement())){
            result = XNumericType.instance;
        }
        else{
            result = XStringType.instance;
        }
        return result;
    }

    public boolean isModelValid() {
    	boolean result = tableValidator.isModelValid();
    	if((result != modelValid) && isLoaded()){
    		modelValid = result;
    		cubeStructureChanged();
    	}
        return result;
    }
    
	public void updateFinished() {
		listeners.fireUpdateFinished();
		finishDataLoad();
	}
    
	public void setCellValue(int x, int y, IResultElement value) {
		listeners.fireCellChanged(y, x, value);
	}

    protected void rebuildHeaders() {
        if(isModelValid()) {
            xDimensions.rebuildHeader();
            yDimensions.rebuildHeader();
            viewExpander.expand();
            //rules.expand();
        }
    }

    protected void removeDimension(IDimensionList list, IDimensionModel dim) {
    	if(list != xDimensions)
    		xDimensions.removeDimension(dim);
    	if(list != yDimensions)
    		yDimensions.removeDimension(dim);
    	if(list != sliceDimensions)        	
    		sliceDimensions.removeDimension(dim);
    }

    protected void setModelValid(boolean value) {
        tableValidator.setModelValid(value);
    };

    private boolean isStringTypeElement(XElement element){
        boolean result = false;
        StringTypeFinder visitor = new StringTypeFinder();
        XHelper.visitHierarchy(element, visitor);
        if(visitor.hasFounded()){
            result = true;
        }
        return result;
    }

	//--inner anonimous-class variables
	
	public String getInvalidReason() {
		return tableValidator.getInvalidReason();
	}

	public XQueryConstructor getQueryConstructor() {
		return queryConstructor;
	}

	private void fireModified() {
		listeners.fireModified();
	}
	
	private void fireSelectionChanged() {
		listeners.fireSelectionChanged();
	}

	private final ICellUpdateCallback cellUpdateCallback = new ICellUpdateCallback(){
		public void onFinish(XPath cube, IXPoint point, IResultElement value, boolean success) {
			if(isModelValid()){
				reloadData();
			}
		}
    };
    
    	
    private final class CellUpdateTask implements ITask {
    	private final HeaderTreeNode xNode;
    	private final HeaderTreeNode yNode;
    	private final IResultElement value;
    	
    	public CellUpdateTask(HeaderTreeNode xNode, HeaderTreeNode yNode, IResultElement value) {
    		this.xNode = xNode;
    		this.yNode = yNode;
    		this.value = value;
    	}

    	public void execute() {
    		//if(isModelValid()){
    			XPath cubePath = getCube().constructPath();
    			MutableXPoint point = pointConstructor.getXPoint(xNode, yNode);
    			serverModel.updateCell(cubePath, point, value, cellUpdateCallback);
    		//}
    	}

		public String getName() {
			return "CellUpdateTask";
		}
    	
    }

    final class SelectedElementChangeTask implements ITask {

		public void execute() {
			reloadData();
		}

		public String getName() {
			return "SelectedElementChangeTask";
		}
    	
    }
    
    private final class ModifiedCallback implements IUserCallback {

		public void execute() {
			fireModified();
		}
    	
    }

}
