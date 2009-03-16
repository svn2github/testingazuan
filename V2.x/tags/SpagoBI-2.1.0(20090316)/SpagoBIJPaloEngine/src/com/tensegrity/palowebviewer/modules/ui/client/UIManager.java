package com.tensegrity.palowebviewer.modules.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.tensegrity.palowebviewer.modules.engine.client.AbstractServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IAuthListener;
import com.tensegrity.palowebviewer.modules.engine.client.IClientProperties;
import com.tensegrity.palowebviewer.modules.engine.client.IEngine;
import com.tensegrity.palowebviewer.modules.engine.client.IErrorListener;
import com.tensegrity.palowebviewer.modules.engine.client.ILoadViewCallback;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModelListener;
import com.tensegrity.palowebviewer.modules.engine.client.IRequestListener;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IMessageAcceptor;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageQueue;
import com.tensegrity.palowebviewer.modules.paloclient.client.XFolder;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.XViewLink;
import com.tensegrity.palowebviewer.modules.ui.client.action.IXActionListener;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.DefaultFormatter;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.ErrorDialog;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.LoginDialog;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.LoginDialogListener;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.InvalidElementMessageAgregator;
import com.tensegrity.palowebviewer.modules.ui.client.favoriteviews.FavoariteViewsActionFactory;
import com.tensegrity.palowebviewer.modules.ui.client.favoriteviews.FavoriteViewsModel;
import com.tensegrity.palowebviewer.modules.ui.client.favoriteviews.FavoriteViewsWidgetFactory;
import com.tensegrity.palowebviewer.modules.ui.client.favoriteviews.ViewLink;
import com.tensegrity.palowebviewer.modules.ui.client.messageacceptors.MissingExpandedElementAcceptor;
import com.tensegrity.palowebviewer.modules.ui.client.tree.DatabaseBrowserTreeModel;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITaskQueueListener;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.TaskQueue;
import com.tensegrity.palowebviewer.modules.widgets.client.IWidgetFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.TreeView;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.AbstractAction;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;
import com.tensegrity.palowebviewer.modules.widgets.client.util.GuiHelper;

/**
 * 
 * UIManager - User Interface manager.
 * Manages UI Layuts, Actions, dialogs and other UI components 
 *
 */
public class UIManager {
	
	private IEngine engine;
	private LoginDialog loginDialog;
	private boolean loginDialogOpened;
	private PaloTreeModel paloTreeModel;
	private FavoriteViewsModel favoriteViewsModel;
	private TabManager tabManager;
    private IXObjectEditorViewFactory editorViewFactory;
    private IWidgetFactory treeWidgetFactory;
    private IXEditorFactory editorFactory;
    private IIconFactory iconFactory;
    private DatabaseBrowserTreeModel databaseBrowserTree;
    private final DefaultFormatter formatter = new DefaultFormatter();
    private SimplePanel glassPanel;
    private int busyCounter = 0;
    private final TaskQueue taskQueue = TaskQueue.getInstance();
	private final UserMessageProcessor messageProcessor;
	private final UIManagerListenerCollection listenerCollection = new UIManagerListenerCollection();
	private IClientProperties clientProperties;
	private MainFrame mainFrame;
	private IWidgetFactory favoriteViewsWidgetFactory;
	private boolean tableOnly;
	private String[] tablePath;
	private String user;
	private String password;
	private boolean editorOnly;

    /**
     * Create new instanse of UIManager
     * 
     * @param engine the Application Engine
     */
    public UIManager (IEngine engine) {
    	RequestParamParser paramParser = new RequestParamParser();
    	this.password = paramParser.getPassword();
    	this.user = paramParser.getUser();
    	this.tableOnly = paramParser.isTableOnly();
    	this.editorOnly = paramParser.isEditorOnly();
    	this.tablePath = paramParser.getTablePath();
    	// Temporary enabled.
    	GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler(){
			public void onUncaughtException(Throwable e) {
				ErrorDialog.showError(e);
			}
    	});
        this.engine = engine;
        IUserMessageQueue userMessageQueue = engine.getUserMessageQueue();
        userMessageQueue.addProcessor(new InvalidElementMessageAgregator());
		this.messageProcessor = new UserMessageProcessor(this, userMessageQueue);
        loginDialogOpened = false;

        loginAction.setEnabled(true);

        IPaloServerModel paloServerModel = engine.getPaloServerModel();
        paloServerModel.addListener(serverModelListener);
		paloTreeModel = new PaloTreeModel(paloServerModel);
        favoriteViewsModel = new FavoriteViewsModel();
        
        
        glassPanel = new SimplePanel();
		glassPanel.setStyleName("glass-panel");

        tabManager = new TabManager(getEditorFactory(), getEditorViewFactory(), getIconFactory(), paloServerModel);
        
        engine.addAuthenticateListener(authListener);
        engine.addErrorListener(errorListener);
        engine.addRequestListener(requestListener);
        taskQueue.addListener(taskQueueListener);
    }

	/**
	 * Returns  editor factory for XObjects.
	 * 
     * @return instance of IXEditorFactory;
	 */
    public IXEditorFactory getEditorFactory() {
        if(editorFactory == null)
            editorFactory = new XEditorFactory(engine, getPaloTreeModel());
        return editorFactory;
    }
    
    public void addListener(IUIManagerListener listener) {
    	listenerCollection.addListener(listener);
    }
    
    public void removeListener(IUIManagerListener listener) {
    	listenerCollection.removeListener(listener);
    }

    
    public IEngine getEngine() {
    	return engine;
    }

    /**
     * @return PaloTreeModel (for db viewer component) 
     */
    public PaloTreeModel getPaloTreeModel() {
        return paloTreeModel;
    }
    
    public FavoriteViewsModel getFavoriteViewsModel() {
    	return favoriteViewsModel;
    }
    
    public DatabaseBrowserTreeModel getFilterTreeModel() {
    	if(databaseBrowserTree == null) {
    		databaseBrowserTree = new DatabaseBrowserTreeModel(getPaloTreeModel());
    		databaseBrowserTree.setFiltering(true);
    	}
        return databaseBrowserTree;
    }

    /**
     * Starts UI. Create main frame, show it and asks for the authenification
     */
    public void start() {
    	
    	if(user != null) {
    		engine.authenticate(user, password, false);
    	}
    	else {
    		engine.authenticate();
    	}

        IAction actionSave = tabManager.getSaveAction();
        IAction actionSaveAs = tabManager.getSaveAsAction();
        logoutAction.setEnabled(false);
        reloadTreeAction.setEnabled(false);
        
		mainFrame = new MainFrame(loginAction,
				logoutAction,
				reloadTreeAction, actionSave, actionSaveAs);
		setUpTreeView(mainFrame);
		setUpFavoriteViews(mainFrame);
		
		mainFrame.setTabPanelModel(tabManager.getTabPanelModel());
		
		if(isShowMainframe()) {
			RootPanel.get().add(mainFrame);
		}
		
	}

	private boolean isShowMainframe() {
		return !(isTableOnly() || isEditorOnly());
	}

	public String[] getTablePath() {
		return tablePath;
	}

	public boolean isTableOnly() {
		return tableOnly;
	}

	public boolean isEditorOnly() {
		return editorOnly;
	}

	private void setUpFavoriteViews(MainFrame mainFrame2) {
		TreeView treeView = mainFrame.getFavoriteViewsView();
		treeView.setWidgetFactory(getFavoriteViewsWidgetFactory());
		treeView.setTreeModel(getFavoriteViewsModel());
		treeView.setActionFactory(new FavoariteViewsActionFactory(this));
	}

	private IWidgetFactory getFavoriteViewsWidgetFactory() {
        if(favoriteViewsWidgetFactory == null)
        	favoriteViewsWidgetFactory = new FavoriteViewsWidgetFactory();
        return favoriteViewsWidgetFactory;
	}

	private void setUpTreeView(MainFrame mainFrame) {
		TreeView treeView = mainFrame.getTreeView();
		treeView.setWidgetFactory(getTreeWidgetFactory());
		treeView.setTreeModel(getFilterTreeModel());
		treeView.setActionFactory(new TreeActionFactory(treeClickActionListener));
	}
    
	/**
	 * Shows login dialog in the center of the screen.
	 * Create new instance of dialog if not exists
	 * Do nithing if already opened
	 * 
	 * @param cause authenification error cause. will not be shown if null 
	 */
	private void showLoginDialog(String cause) {
		if ( loginDialog == null ) {
			loginDialog = new LoginDialog();
			loginDialog.addListener(loginDialogListener);
		}
		
		loginDialog.setCause(cause);
		
		if ( loginDialogOpened )
			return;
		GuiHelper.centerShowDialog(loginDialog);
		loginDialogOpened = true;
	}
	
	private void showBusy() {
		if(busyCounter == 0){
			DOM.addEventPreview(blokingEventPreview);
		    RootPanel.get().add(glassPanel);
		    
		    listenerCollection.onBusy();
		}
		busyCounter++;
	}
	private void hideBusy() {
		busyCounter--;
		if(busyCounter == 0){
			RootPanel.get().remove(glassPanel);
			DOM.removeEventPreview(blokingEventPreview);
			listenerCollection.onFree();
		}
	}
	
	public boolean isBusy() {
		return busyCounter > 0;
	}

	/**
	 * Hide login dialog
	 */
	private void hideLoginDialog() {
		if ( loginDialog != null ) {
			loginDialog.hide();
			loginDialogOpened = false;
		}
	}
    public IXObjectEditorViewFactory getEditorViewFactory() {
        if(editorViewFactory == null)
            editorViewFactory = new XObjectEditorViewFactory(this, formatter);
        return editorViewFactory;
    }

    public IWidgetFactory getTreeWidgetFactory() {
        if(treeWidgetFactory == null)
            treeWidgetFactory = new TreeWidgetFactory();
        return treeWidgetFactory;
    }
    
    public IIconFactory getIconFactory(){
    	if (iconFactory == null) {
			iconFactory = new DefaultIconFactory();
		}
		return iconFactory;
    }
    
    public IAction getLogoutAction() {
        return logoutAction;
    }
    
	public IClientProperties getClientProperties(){
		return clientProperties;
	}
	
	public void openView(ViewLink link) {
		if(link == null)
			throw new IllegalArgumentException("Link can not be null.");
		getPaloServerModel().loadView(link.getLink(), loadViewCallback);
	}
	
	public void openView(String[] path) {
		if(path == null)
			throw new IllegalArgumentException("Path can not be null.");
		getPaloServerModel().loadView(path, loadViewCallback);
	}

	private IPaloServerModel getPaloServerModel() {
		return getEngine().getPaloServerModel();
	}
	
	public void openEditor(XObject object) {
		if(object == null){
			Logger.warn("Trying to open editor for a null object");
		}
		else {
			tabManager.openXObjectEditorTab(object);
		}
	}
	


    //-- Internal classes section --

    private void loadFavoriteViews() {
		if(showFavoriteViews) {
			getPaloServerModel().loadFavoriteViews();
		}
	}



	private LoginDialogListener loginDialogListener = new LoginDialogListener() {

        public void onCancel() {
            hideLoginDialog();
        }

        public void onOk(String login, String password, boolean remember) {
            engine.authenticate(login, password, remember);
        }

    };
	protected boolean showFavoriteViews;

    private IAuthListener authListener = new IAuthListener() {

		public void onAuthFailed(String cause) {
            showLoginDialog(cause);
        }

        public void onAuthSuccess() {
            hideLoginDialog();
            
            tabManager.turnOn();
            logoutAction.setEnabled(true);
            loginAction.setEnabled(false);
            reloadTreeAction.setEnabled(true);
            
            clientProperties = getEngine().getClientProperties();
            formatter.setFloatSeparator(clientProperties.getFloatSeparator());
            formatter.setDecimalSeparator(clientProperties.getDesimalSeparator());
            formatter.setFractionalDigitsNumber(clientProperties.getFractionNumber());
            
            getFilterTreeModel().setShowCubeDimensions(clientProperties.showCubeDimensions());
            getFilterTreeModel().setShowDimensions(clientProperties.showDatabaseDimensions());
            mainFrame.setCanShowDbExplorer(clientProperties.isShowDbExplorer());
            showFavoriteViews = clientProperties.isShowFavoriteViews();
			mainFrame.setCanShowFavoriteViews(showFavoriteViews);
            mainFrame.setNavigationPanelWidth(clientProperties.getNavigationPanelWidth());
            mainFrame.setShowNavigationPanel(clientProperties.isShowNavigationPanel());
            
            boolean filterMissingElement = clientProperties.getNotificationMissingExpandedElement();
			IMessageAcceptor acceptor = new MissingExpandedElementAcceptor(filterMissingElement);
            getEngine().getUserMessageQueue().getMessageFilter().addAcceptor(acceptor);
            loadFavoriteViews();
    		String[] viewPath = getTablePath();
    		if(viewPath != null) {
    			getEngine().getPaloServerModel().loadView(viewPath, loadViewCallback);
    		}

        }

        public void onLogoff() {
        	loginAction.setEnabled(true);
        	logoutAction.setEnabled(false);
            reloadTreeAction.setEnabled(false);
            tabManager.turnOff();
        }
    };

    private IErrorListener errorListener = new IErrorListener() {

        public void onError(Throwable caught) {
            ErrorDialog.showError(caught);
        }
    };

    private AbstractAction loginAction = new AbstractAction() {

        public void onActionPerformed(Object arg) {
        	engine.authenticate();
        }

    };

    private IXActionListener treeClickActionListener = new IXActionListener() {

        public void onAction(XObject xObject) {
            openEditor(xObject);
        }

    };
    
    private final ILoadViewCallback loadViewCallback = new ILoadViewCallback() {

		public void onViewLoaded(XViewLink link, XView view) {
            openEditor(view);
		}

    };
    
    private AbstractAction logoutAction = new AbstractAction() {

        public void onActionPerformed(Object arg) {
            getEngine().logout();
        }

    };
    
    private AbstractAction reloadTreeAction = new AbstractAction() {

        public void onActionPerformed(Object arg) {
            getPaloServerModel().reloadTree();
            loadFavoriteViews();
        }

    };
    
    private IRequestListener requestListener = new IRequestListener(){
    	
		public void beforeSend() {
			showBusy();
		}
		
		public void afterRecieve() {
			hideBusy();
		}
    	
    };
    
    private ITaskQueueListener taskQueueListener = new ITaskQueueListener () {

		public void onTaskStart(ITask task) {

		}
		
		public void onTaskFinished(ITask task) {
			hideBusy();
		}

		public void onTaskAdded(ITask task) {
			showBusy();
		}
    	
    };
    
    private final EventPreview blokingEventPreview = new EventPreview() {

		public boolean onEventPreview(Event event) {
			return false;
		}
    	
    };
    
    private final IPaloServerModelListener serverModelListener = new AbstractServerModelListener() {

		public void onFavoriteViewsLoaded() {
			XFolder root = getPaloServerModel().getFavoriteViewsRoot();
			getFavoriteViewsModel().setStructure(root);
			mainFrame.expandFavoriteConnections();
		}
    	
    };


}
