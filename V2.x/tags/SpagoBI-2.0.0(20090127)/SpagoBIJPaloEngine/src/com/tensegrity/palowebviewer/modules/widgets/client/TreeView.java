package com.tensegrity.palowebviewer.modules.widgets.client;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModelListener;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreeModelEvent;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreePath;


/**
 * Widget that displays tree accordingly to given {@link ITreeModel}. It works with dynamicly 
 * loadable model so if the part of the model isn't loaded yet, but requested for display, then 
 * TreeView shows {@link LoadingLabel} and request the model to load the part.
 * 
 * CSS:
 * <ol> 
 * <li>tensegrity-gwt-clickable - for nodes that have click actions.</li>
 * <li>tensegrity-gwt-tree-item - all tree node</li>
 * <li>tensegrity-gwt-tree- - entire tree</li>
 * </ol>
 */
public class TreeView extends Tree implements ITreeViewItem
{

    private ITreeModel model;
    private IWidgetFactory widgetFactory;
    private IActionFactory actionFactory;
    private final boolean showRoot;
    private ITreeViewItem rootItem;
    private TreeItem loading = null;
    private boolean selectExpanded = true;

    private class TreeViewItem extends TreeItem implements ITreeViewItem, ClickListener{

        private Object node;
        private TreeItem loading = null;
        private IAction clickAction;
        boolean childrenInited = false;


        public void onClick(Widget source){
            doClickAction();
        }

        public void setClickAction(IAction action) {
            this.clickAction = action;
            if(this.clickAction != null) {
                getWidget().addStyleName("tensegrity-gwt-clickable");
            }
            else {
                getWidget().removeStyleName("tensegrity-gwt-clickable");
            }
        }

        public void doClickAction() {
            IAction action = getClickAction();
            if(action != null) {
                action.onActionPerformed(getNode());
            }
        }

        public TreeViewItem(Widget widget, Object node) {
            super(widget);
            setStyleName("tensegrity-gwt-tree-item");
            this.node = node;
            reinit();
        }
        
        public void setWidget(Widget widget) {
        	Widget oldWidget = getWidget();
        	if(oldWidget instanceof SourcesClickEvents && oldWidget != null){
                SourcesClickEvents source = (SourcesClickEvents)oldWidget;
                source.removeClickListener(this);
        	}
        	super.setWidget(widget);
        	if(widget instanceof SourcesClickEvents && widget != null){
                SourcesClickEvents source = (SourcesClickEvents)widget;
                source.addClickListener(this);
        	}
        }

        public void reinit() {
            PerformanceTimer timer = new PerformanceTimer(this+".reinit()");
            timer.start();
            childrenInited = false;
            if(!isLeaf()){
                if(isObjectLoaded()&& getState())
                    initChildren();
                else
                    setLoading();
            }
            else {
            	removeLoading();
            }
            timer.report();
        }

        public TreeItem addItem(Widget widget) {
            removeLoading();
            return super.addItem(widget);
        }

        public void addItem(TreeItem item) {
            removeLoading();
            super.addItem(item);
        }

        public void addItem(ITreeViewItem item) {
            addItem((TreeItem)item);
        }

        public Object getNode() {
            return node;
        }

        public void insertItem(ITreeViewItem item, int position) {
            removeLoading();
            if(position == getChildCount())
                this.addItem(item);
            else {
                insertItemIntoPositions(item, position);
            }
        }

        protected void insertItemIntoPositions(ITreeViewItem item, int position) {
            TreeItem[] children = getChildren();
            removeItems();
            for( int i = 0 ; i <= children.length ; i++ ) { 
                if(i == position)
                    this.addItem(item);
                if(i<children.length)
                    this.addItem(children[i]);
            } 
        }

        public String toString() {
            return "TreeViewItem["+getNode()+"]";
        }

        public void stateChanged() {

            initChildren();
        }

        public boolean isChildrenInited() {
            boolean result = !isLoading();
            if(result)
                result = childrenInited;
            /*
               if(result){
               int size = getTreeModel().getChildCount(node);
               result = size == getChildCount();
               }
               */
            return childrenInited;
        }

        protected boolean isObjectLoaded() {
            return getTreeModel().isLoaded(node);
        }

        protected boolean isLeaf() {
            return getTreeModel().isLeaf(node);
        }

        protected TreeItem[] getChildren() {
            int size = getChildCount();
            TreeItem[] result = new TreeItem[size];
            for( int i = 0 ; i < size ; i++ ) { 
                result[i] = getChild(i);
            } 
            return result;
        }

        protected void initChildren() {
            if(getState() && !isChildrenInited() && isObjectLoaded()){
                clear();
                int size = model.getChildCount(node);
                for( int i = 0 ; i < size; i++ ) { 
                    Object child = model.getChild(node, i);
                    ITreeViewItem childItem = createTreeItemFor(child);
                    addItem(childItem);
                }
                childrenInited = true;
            }
        }

        protected IAction getClickAction() {
            return clickAction;
        }

        protected void setLoading() {
            if(!isLoading()){
                clear();
                loading = addItem(new LoadingLabel());
            }
        }

        protected boolean isLoading() {
            return loading != null;
        }

        protected void removeLoading() {
            if(isLoading()){
                this.removeItem(loading);
                loading = null;
            }
        }

        protected void clear() {
            removeLoading();
            removeItems();
        }

		public ITreeViewItem getChildItem(int i) {
			return (ITreeViewItem)getChild(i);
		}

    }

    private final ITreeModelListener modelListener = new ITreeModelListener() {

        public void treeNodesChanged(TreeModelEvent e){
            TreePath path = e.getTreePath();
            int[] childIndices = e.getChildIndices();
            ITreeModel treeModel = getTreeModel();
            if(childIndices == null) {
                //consider root has been chanded
                Widget widget = createWidgetFor(treeModel.getRoot());
                getRootItem().setWidget(widget);
            }
            else {
                ITreeViewItem parentItem = getItemForPath(path);
                if(parentItem == null || !parentItem.isChildrenInited())
                    return;
                Object parent = path.getLastPathComponent();
                for( int i = 0 ; i < childIndices.length ; i++ ) { 
                    int index = childIndices[i];
                    TreeViewItem item = (TreeViewItem)parentItem.getChild(index);
                    Object child = treeModel.getChild(parent, index);
                    Widget widget = createWidgetFor(child);
                    item.setWidget(widget);
                    IAction action = getClickActionFor(child);
                    item.setClickAction(action);
                } 
            }
        }

        public void treeNodesInserted(TreeModelEvent e){
            TreePath path = e.getTreePath();
            int[] childIndices = e.getChildIndices();
            ITreeModel treeModel = getTreeModel();
            Object parent = path.getLastPathComponent();
            ITreeViewItem parentItem = getItemForPath(path);
            if(parentItem == null)
                return;
            for( int i = 0 ; i < childIndices.length ; i++ ) { 
                int index = childIndices[i];
                Object child = treeModel.getChild(parent, index);
                ITreeViewItem childItem = createTreeItemFor(child);
                parentItem.insertItem(childItem, index);
            } 
        }

        public void treeNodesRemoved(TreeModelEvent e){
            TreePath path = e.getTreePath();
            int[] childIndices = e.getChildIndices();
            ITreeViewItem parentItem = getItemForPath(path);
            if(parentItem == null || !parentItem.isChildrenInited())
                return;
            for( int i = childIndices.length -1 ; i>=0; i-- ) { 
                int index = childIndices[i];
                parentItem.getChild(index).remove();
            } 
        }

        public void treeStructureChanged(TreeModelEvent e){
            TreePath path = e.getTreePath();
            if(path == null)
                TreeView.this.reinit();
            else {
                ITreeViewItem item = getItemForPath(path);
                if(item != null)
                    item.reinit();
            }
        }

    };

    private final TreeListener treeListener = new TreeListener() {

        public void onTreeItemSelected(TreeItem item) {
        }

        public void onTreeItemStateChanged(TreeItem item) {
        	if(isSelectExpanded())
        		setSelectedItem(item, true);
            ITreeViewItem treeViewItem = (ITreeViewItem)item;
            Object node = treeViewItem.getNode();
            if(!getTreeModel().isLoaded(node)){
                getTreeModel().load(node);
            }
            treeViewItem.stateChanged();
        }

    };
	

    public void setSelectExpanded(boolean  value) {
    	this.selectExpanded = value;
    }
    
    public boolean isSelectExpanded() {
    	return selectExpanded;
    }

    public void setTreeModel(ITreeModel model) {
        if(this.model != null)
            this.model.removeTreeModelListener(modelListener);
        this.model = model;
        if(this.model != null)
            this.model.addTreeModelListener(modelListener);
        reinit();
    }

    public ITreeModel getTreeModel() {
        return model;
    }

    public void setWidgetFactory (IWidgetFactory factory) {
        if(factory == null)
            throw new IllegalArgumentException("Widget factory was null");
        this.widgetFactory = factory;
    }

    public void setActionFactory (IActionFactory factory) {
        //TODO: what if factory was set after TreeModel
        this.actionFactory = factory;
    }

    public IWidgetFactory getWidgetFactory () {
        return this.widgetFactory;
    }

    public IActionFactory getActionFactory () {
        return this.actionFactory;
    }

    public TreeView () {
        this(false);
    }

    public TreeView (boolean showRoot) {
        setStyleName("tensegrity-gwt-tree");
        this.showRoot = showRoot;
        setWidgetFactory(new LabelWidgetFactory());
        addTreeListener(treeListener);
    }

    public boolean getShowRoot() {
        return showRoot;
    }

    protected ITreeViewItem getRootItem() {
        if(getShowRoot())
            return rootItem;
        else
            return this;
    }

    protected ITreeViewItem getItemForPath(TreePath treePath) {
        ITreeViewItem result = getRootItem();
        ITreeModel treeModel = getTreeModel();
        Object[] path = treePath.getPath(); 
        for( int i = 1 ; i < path.length ; i++ ) { 
        	if(result == null || !result.isChildrenInited()) {
        		result = null;
        		break;
        	}
        	Object parent = path[i-1];
        	int index = treeModel.getIndexOfChild(parent, path[i]);
        	result = (ITreeViewItem)result.getChild(index);

        }
        return result;
    }

    protected Widget createWidgetFor(Object node) {
        PerformanceTimer timer = new PerformanceTimer("TreeView.createWidgetFor("+node+")");
        timer.start();
        IWidgetFactory factory = getWidgetFactory();
        Widget widget = factory.createWidgetFor(node);
        timer.report();
        return widget;
    }

    protected IAction getClickActionFor(Object node) {
        IAction action = null;
        IActionFactory factory = getActionFactory();
        if(factory != null)
            action = factory.getActionFor(node);
        return action;
    }

    protected ITreeViewItem createTreeItemFor(Object node) {
        Widget widget = createWidgetFor(node);
        TreeViewItem item = new TreeViewItem(widget, node);
        IAction action = getClickActionFor(node);
        item.setClickAction(action);
        return item;
    }

    public void reinit() {
        clear();
        if(getTreeModel() != null) {
            if(getShowRoot()){
                reinitWithRoot();
            }
            else {
                reinitRootless();
            }
        }
    }

    protected void reinitWithRoot() {
        Object root = getTreeModel().getRoot();
        rootItem = createTreeItemFor(root);
        addItem((TreeItem)rootItem);
    }

    protected void reinitRootless() {
        ITreeModel model = getTreeModel();
        Object root = model.getRoot();
        if(!model.isLeaf(root))
            if(!model.isLoaded(root) ){
                loading = addItem(new LoadingLabel());
                model.load(root);
            }
            else {
                int size = model.getChildCount(root);
                for (int i = 0; i < size; i++) {
                    Object child = model.getChild(root, i);
                    ITreeViewItem item = createTreeItemFor(child);
                    addItem(item);
                }
            }
    }

    protected void removeLoading() {
        if(loading !=null) {
            loading.remove();
            loading = null;
        }
    }

    public void addItem(ITreeViewItem itme) {
        removeLoading();
        addItem((TreeItem)itme);
    }

    public TreeItem getChild(int i) {
        return getItem(i) ;
    }

    public Object getNode() {
    	ITreeViewItem rootItem = getRootItem();
    	Object result = null;
    	if(rootItem == this){
    		result = getTreeModel().getRoot();
    	}
    	else
    		result = rootItem.getNode();
        return result;
    }

    public TreeItem[] getChildren() {
        TreeItem[] result = new TreeItem[getItemCount()];
        for (int i = 0; i < result.length; i++) {
            result[i] = getItem(i);
        }
        return result;
    }

    public void insertItem(ITreeViewItem item, int index) {
        removeLoading();
        TreeItem[] children = getChildren();
        removeItems();
        for( int i = 0 ; i <= children.length ; i++ ) { 
            if(i == index)
                this.addItem(item);
            if(i<children.length)
                this.addItem(children[i]);
        } 
    }

    public void remove() {
    }

    public void setWidget(Widget widget) {
    }

    public boolean isChildrenInited() {
        return true;
    }

    public void setClickAction(IAction action) {
        //do nothing
    }

    public void stateChanged() {
    }

	public void setState(boolean state) {
	}

	public int getChildCount() {
		return getItemCount();
	}

	public ITreeViewItem getChildItem(int i) {
		return (ITreeViewItem)getChild(i);
	}

}
