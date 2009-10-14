package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.ResultString;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.ErrorDialog;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.IDimensionModel;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;


public class CubeTableView {

    private final static String PATH_SEPARATOR = "/";
    private ICubeTableModel model;
    private ITableAPI cubeTable;
    private final IFormatter formatter;
    private boolean initialisation = false;
	private boolean rebuilding;

    protected void startInitialization(){
        initialisation = true;
    }

    protected void stopInitialization(){
        initialisation = false;
    }

    public boolean isInitialization() {
        return initialisation;
    }
    
    public CubeTableView(IFormatter formatter) {
        model = null;
        this.formatter = formatter;
        cubeTable = new CubeTableAPIImpl();
        cubeTable.addListener(tableListener);
        cubeTable.initWidget();
    }

    public Widget getWidget() {
        return cubeTable.getWidget();
    }

    public void setModel(ICubeTableModel value) {
        if(model != null) {
            model.removeListener(modelListener);
        }
        model = value;
        if(model != null) {
            model.addListener(modelListener);
        }
    }

    public ICubeTableModel getModel() {
        return model;
    }
    
	public void setColumnMinVisibleString(String value){
		cubeTable.setParameter(ITableAPI.MIN_WIDTH, value);
	}
	
	public void setColumnMaxVisibleString(String value){
		cubeTable.setParameter(ITableAPI.MAX_WIDTH, value);
	}
	
	public void setHintTime(int value){
		cubeTable.setParameter(ITableAPI.HINT_TIME, "" + value);
	}

    private void cubeTableLoaded() {
    	//setting parameters
    	
        rebuildCube();
    }

    protected void expandNodes(CubeHeaderModel headerModel, int direction) {
        HeaderTreeNode root = headerModel.getHeaderRoot();
        expandChildren(root, direction);
    }

    protected void expandChildren(HeaderTreeNode node, int direction) {
        final int size = node.getChildCount();
        for (int i = 0; i < size; i++) {
            HeaderTreeNode child = node.getChild(i);
            expandNode(child, direction);
        }
    }

    protected void expandNode(HeaderTreeNode node, int direction) {
        if(node.isOpen()){
            String path = node.getTreePath();
            cubeTable.expandTree(direction, path);
            //cubeTable.updateData();

        }
        expandChildren(node, direction);
    }

    private void rebuildCube() {
    	if(!rebuilding)
    	{
    		try {
		    	rebuilding = true;
		    	Logger.debug("CubeTableView.rebuildCube() : " + getModel().getView());
		       
		    	if ( model != null) {
		    		startInitialization();
		    		if(!model.isModelValid() || !model.isLoaded()){
		    			cleanTable();
		    			hideTable();
		    		}
		    		else{
		    			cleanTable();
		    			showTable();
		    		}
		    		stopInitialization();
		    	}
		    	else {
		    		cleanTable();
		        }
			} finally {
	    		rebuilding = false;
	    	}
    	}
    }

	private void cleanTable() {
		model.removeListener(dataListener);
		cubeTable.clean();
	}

	private void showTable() {
		IDimensionList xDim = model.getXDimensions();
		IDimensionList yDim = model.getYDimensions();
		getWidget().setVisible(true);
		CubeHeaderModel xHeaderModel = model.getXHeaderModel();
		addDimensions(xDim, xHeaderModel,ITableAPI.DIRECTION_HORIZONTAL);
		CubeHeaderModel yHeaderModel = model.getYHeaderModel();
		addDimensions(yDim, yHeaderModel,ITableAPI.DIRECTION_VERTICAL);
		//cubeTable.updateData();
		expandChildren(xHeaderModel.getHeaderRoot(), ITableAPI.DIRECTION_HORIZONTAL);
		expandChildren(yHeaderModel.getHeaderRoot(), ITableAPI.DIRECTION_VERTICAL);
		model.addListener(dataListener);
		model.reloadData();
	}

	private void addDimensions(IDimensionList dimList, CubeHeaderModel headerModel, int direction) {
		for (int i=0; i < dimList.getDimCount(); i++) {
		    IDimensionModel dimModel = dimList.getDimModel(i);
		    ITreeModel treeModel = dimModel.getTreeModel(); 
		    cubeTable.insertTree(direction,i, treeModel);
		}
		//expandNodes(headerModel, direction);
	}

	private void hideTable() {
		getWidget().setVisible(false);
		model.removeListener(dataListener);
		if(model.isLoaded()) {
			String reason = model.getInvalidReason();
			String text = "Cube model is not valid" + (reason != null ? " : " + reason : "") + "."; 
			ErrorDialog.showError(text);
		}
	}

    protected HeaderTreeNode translatePath(int direction, String path) {
        if(path == null)
            throw new IllegalArgumentException("Path can not be null");
        CubeHeaderModel headerModel = getHeader(direction);
        Logger.debug("path = '"+path+"'");
        String[] nodeNames = path.split(PATH_SEPARATOR);
        HeaderTreeNode node = headerModel.getHeaderRoot();
        for( int i = 0 ; i < nodeNames.length ; i++ ) { 
            String name = nodeNames[i];
            if("".equals(name))
                continue;
            else {
                node = node.getChildByName(name);
            }
        } 
        return node;
    }

	private CubeHeaderModel getHeader(int direction) {
		CubeHeaderModel headerModel = null;
        if(direction == ITableAPI.DIRECTION_VERTICAL) {
            headerModel = getModel().getXHeaderModel(); 
        }
        else if(direction == ITableAPI.DIRECTION_HORIZONTAL) {
            headerModel = getModel().getYHeaderModel(); 
        }
        else {
            throw new IllegalArgumentException("unknown direction = " + direction);
        }
		return headerModel;
	}

    private ITableAPIListener tableListener = new ITableAPIListener() {

        public void onCollapse(String path) {}

        public void onExpand(String path) {}

        public void onLoaded() {
            cubeTableLoaded();
        }

        public void onStateChanged(int direction, String path) {
            if(!isInitialization()) {
                HeaderTreeNode node = translatePath(direction, path);
                node.toggleOpen();
            }
        }

        public boolean canCellBeEdited(String xTree, String yTree) {
            if(!isInitialization()) {
                HeaderTreeNode xNode = translatePath(ITableAPI.DIRECTION_VERTICAL, xTree);
                HeaderTreeNode yNode = translatePath(ITableAPI.DIRECTION_HORIZONTAL, yTree);
                return model.canCellBeEdited(xNode, yNode);
            }
            else
                return false;
        }

        public void onCellUpdate(String xTree, String yTree, String newValue) {
            if(!isInitialization()) {
                IResultElement value = new ResultString(newValue);
                HeaderTreeNode xNode = translatePath(ITableAPI.DIRECTION_VERTICAL, xTree);
                HeaderTreeNode yNode = translatePath(ITableAPI.DIRECTION_HORIZONTAL, yTree);
                /*
                 * we do not check/parse the value on client side. It will be done on server 
                 * side using JPalo API
                 */
                model.updateCell(xNode, yNode, value);
                /*
                if(getModel().isCellConsolidated(xNode, yNode)) {
                	value = new ResultString(newValue);	
                }
                else {
                	try{
                		IElementType type = getModel().getCellType(xNode, yNode);
                		value = formatter.unformat(newValue, type);
                	}catch(NumberFormatException nfe){
                		nfe.printStackTrace();
                	}
                }
                if(value != null)
                	model.updateCell(xNode, yNode, value);
                */
            }
        }
        
        public boolean validate(String xTree, String yTree, String value) {
        	/*
            boolean r = true;
            HeaderTreeNode xNode = translatePath(ITableAPI.DIRECTION_VERTICAL, xTree);
            HeaderTreeNode yNode = translatePath(ITableAPI.DIRECTION_HORIZONTAL, yTree);
            if(!getModel().isCellConsolidated(xNode, yNode)) {
            	try{
            		IElementType type = getModel().getCellType(xNode, yNode);
            		formatter.unformat(value, type);
            	}
            	catch(NumberFormatException nfe){
            		r = false;
            	}
            }
            return r;
            */
        	/* 
        	 * obsolete method. All checks are performed on server side.
        	 */
        	return true;
        }
		public boolean isSelectedElementsPlain() {
			return getModel().isSelectedElementsPlain();
		}
    };
    
    private final IDataListener dataListener = new IDataListener() {
    	
        public void cellChanged(int row, int column, IResultElement value) {
            String text = formatter.format(value);
            cubeTable.setCellValue(row, column, text);
        }
	
    };

    private final ICubeTableModelListener modelListener = new ICubeTableModelListener() {

        public void structureChanged() {
            rebuildCube();
        }

        public void modified() {

        }

        public void updateFinished() {
            PerformanceTimer timer = new PerformanceTimer("updateData");
            timer.start();
            cubeTable.updateData();
            timer.report();
        }

		public void onModelDispose() {
			setModel(null);
		}

		public void zStateChanged(boolean value) {
			Logger.debug("zStateChanged("+value+")");
			cubeTable.changeZstate(value);
			
		}

		public void leftChildrenChanged(ITreeModel model, Object parent) {
			cubeTable.insertChildren(ITableAPI.DIRECTION_VERTICAL, parent, model);
			
		}

		public void topChildrenChanged(ITreeModel model, Object parent) {
			cubeTable.insertChildren(ITableAPI.DIRECTION_HORIZONTAL, parent, model);
		}

		public void leftTreeNodeStateChanged(HeaderTreeNode node) {
			cubeTable.expandTree(ITableAPI.DIRECTION_VERTICAL, node.getTreePath());
			
		}

		public void topTreeNodeStateChanged(HeaderTreeNode node) {
			cubeTable.expandTree(ITableAPI.DIRECTION_HORIZONTAL, node.getTreePath());
		}

		public void selectionChanged() {
			
		}

    };

}
