package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageQueue;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XFolder;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.XViewLink;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPathElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.util.client.ICallback;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;


/**
 * Implementation of {@link  IPaloServerModel}.
 * It lazy loads Palo server's structure.
 */
public class PaloServerModel implements IPaloServerModel
{
    private PerformanceTimer updateTimer;
    private int updateCounter = 0;
	private XRoot root;
	private boolean stateOn = false;
	private IUserMessageQueue userMessageQueue;
	private final IEngineServiceAsync engineService;
    private final PaloServerModelListenerCollection listeners = new PaloServerModelListenerCollection();
    private final PathCache pathCache = new PathCache(this);
    private final DatabaseDimensionCache dbDimCache = new DatabaseDimensionCache();
    private final DimensionElementCache dimElementCache = new DimensionElementCache();
    private final DimensionSubsetCache dimSubsetCache = new DimensionSubsetCache();
    private final CubeViewCache cubeViewCache = new CubeViewCache();
    private final LoadingMap loadingMap = new LoadingMap();
    private final ObjectUpdater updater;
    private XFolder favoriteViewsRoot = new XFolder();
    private final IObjectUpdaterListener updaterListener = new IObjectUpdaterListener() {

		public void childrenArrayChanged(XObject[] path, XObject[] oldChildren, int type) {
			listeners.fireChildArrayChanged(path, oldChildren, type);
		}

		public void objectRenamed(XObject object) {
			listeners.fireObjectRenamed(object);
		}
    	
    };
    private final IErrorCallback errorCallback = new IErrorCallback () {

		public void onError(Throwable t) {
        	listeners.fireError(t);
        }
    	
    };
    private IErrorCallback invalideObjectPathCallback = new IErrorCallback() {

		public void onError(Throwable t) {
			if(t instanceof InvalidObjectPathException) {
				InvalidObjectPathException exception = (InvalidObjectPathException)t;
				XPath path = exception.getPath();
				removeFromStructure(path);
				listeners.fireObjectInvalide(path);
			}
			else {
				listeners.fireError(t);
			}
		}

    };
    
    /**
     * {@inheritDoc}
     */
    public void clear() {
        root = null;
        pathCache.clear();
        dbDimCache.clear();
        dimElementCache.clear();
        loadingMap.clear();
        setFavoriteViews( new XFolder());
        listeners.fireModelChanged();
    }

    /**
     * {@inheritDoc}
     */
    public XRoot getRoot() {
        if(root == null) {
            root = new XRoot();
            if(!isOn()){
                root.setServers(new XServer[0]);
            }
        }
        return root;
    }

    public PaloServerModel (IEngineServiceAsync engineService) {
        this.engineService = engineService;
        updater = new ObjectUpdater();
        updater.setDbDimCache(dbDimCache);
        updater.setDimElementCache(dimElementCache);
        updater.setDimSubsetCache(dimSubsetCache);
        updater.setCubeViewCache(cubeViewCache);
        updater.addListener(updaterListener);
    }

    /**
     * {@inheritDoc}
     */
    public void addListener(IPaloServerModelListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener was null");
        listeners.addListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeListener(IPaloServerModelListener listener) {
        listeners.removeListener(listener);
    }
    
    public void loadPath(XObject hierarchyRoot, XElement element, IElementPathCallback callback) {
    	ElementPathLoadCallback loadCallback = new ElementPathLoadCallback();
    	loadCallback.setRoot(hierarchyRoot);
    	loadCallback.setElement(element);
    	loadCallback.setCallback(callback);
    	loadCallback.sendQuerry(getEngineService());
    }
    
	public void loadChildren(XObject object, int type) {
		loadChildren(object, type, null);
	}
	
	void loadChildren(XObject object, int type, IInitCallback callback) {
		LoadChildrenCallback initObjectCallback = new LoadChildrenCallback(this, object, type);
		initObjectCallback.setInitCallback(callback);
		initObjectCallback.setErrorCallback(invalideObjectPathCallback);
		initObjectCallback.sendQuerry(getEngineService());
	}
	
	public XElement getElement(XDimension dimension, XElement element) {
		return dimElementCache.getElement(dimension, element);
	}

    public void reloadTree() {
    	getEngineService().forceReload(new ForceReloadTreeCallback());
    }

    public void reloadSubTree(XObject object) {
        if(object == null)
            throw new IllegalArgumentException("Object can not be null");
        new ReloadSubTreeCallback (this, object).sendQuery();
    }

    /**
     * {@inheritDoc}
     */
    public void query(XQueryPath[] queries, IQueryCallback callback) {
        XQueryCallback queryCallback = new XQueryCallback(queries, callback);
        queryCallback.setErrorCallback(errorCallback);
		queryCallback.sendQuerry(getEngineService());
    }

    public XObject getObject(XPath path) {
    	return pathCache.getObject(path);
    }

	public void loadDefaultView(XCube cube) {
		new LoadDefaultViewRequest(cube, listeners).sendRequest(engineService);
	}
	
	public void checkElement(XDimension dim, XElement element, IVerificationCallback callback) {
		if(callback == null)
			throw new IllegalArgumentException("Callback can not be null.");
		if(dim == null)
			throw new IllegalArgumentException("Dimension can not be null");
		if(element == null)
			throw new IllegalArgumentException("Element can not be null");
		new VerificationRequest(dim, element,callback).sendRequest(getEngineService());
	}

	public void checkElement(XSubset subset, XElement element, IVerificationCallback callback) {
		if(callback == null)
			throw new IllegalArgumentException("Callback can not be null.");
		if(subset == null)
			throw new IllegalArgumentException("Subset can not be null");
		if(element == null)
			throw new IllegalArgumentException("Element can not be null");
		new VerificationRequest(subset, element,callback).sendRequest(getEngineService());
	}


    /**
     * {@inheritDoc}
     */
    public void turnOff() {
        if(isOn()){
            stateOn = false;
            clear();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isOn() {
        return stateOn;
    }

    /**
     * {@inheritDoc}
     */
    public void turnOn() {
        if(!isOn()){
            stateOn = true;		
            clear();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void saveView(XView view, ICallback successCallback) {
        SaveViewCallback saveViewCallback = new SaveViewCallback(view,this.listeners);
        saveViewCallback.setCubeViewCache(cubeViewCache);
        saveViewCallback.setSuccessCallback(successCallback);
		saveViewCallback.send(getEngineService());
    }
    
    public void checkAndSave(XView view, ICallback successCallback) {
    	CheckViewExistsCallback callback = new CheckViewExistsCallback(this, view);
    	callback.setSuccessCallback(successCallback);
		callback.send(getEngineService());
    }

    public void updateCell(XPath cube, IXPoint point, IResultElement value, ICellUpdateCallback callback) {
        UpdateCellCallback updateCallback = new UpdateCellCallback(cube, point, value, callback);
        updateCallback.setErrorCallback(errorCallback);
        updateCallback.sendCellUpdate(getEngineService());
    }
    
	public boolean isObjectValid(XObject object) {
		boolean result = XHelper.hierarchyContains(getRoot(), object);
		if(!result && object instanceof XView) {
			result = cubeViewCache.getCube((XView)object) != null;
		}
		return result;
	}

	public void loadView(XViewLink link, ILoadViewCallback callback) {
		String serverId = link.getConnectionId();
		String databaseId = link.getDatabaseId();
		String cubeId = link.getCubeId();
		IDChildLoader serverLoader = new IDChildLoader(this, serverId, IXConsts.TYPE_SERVER);
		IDChildLoader databaseLoader = new IDChildLoader(this, databaseId, IXConsts.TYPE_DATABASE);
		IDChildLoader cubeLoader = new IDChildLoader(this, cubeId, IXConsts.TYPE_CUBE);
		ViewLoader viewLoader = new ViewLoader(this, link);
		serverLoader.setCallback(databaseLoader);
		databaseLoader.setCallback(cubeLoader);
		cubeLoader.setCallback(viewLoader);
		viewLoader.setCallback(callback);
		serverLoader.onChildLoaded(getRoot());
	}

	public void loadView(XCube cube, String viewId, IChildLoadCallback callback) {
		ViewLoadTask task =  new ViewLoadTask(cube, viewId);
		task.setCallback(callback);
		task.setCubeViewCache(cubeViewCache);
		task.sendQuerry(getEngineService());
	}

	public void loadViewByName(XCube cube, String viewName, IChildLoadCallback callback) {
		ViewNameLoadTask task =  new ViewNameLoadTask(cube, viewName);
		task.setCallback(callback);
		task.setCubeViewCache(cubeViewCache);
		task.sendQuerry(getEngineService());
	}
	
	public void loadView(String[] path, ILoadViewCallback callback) {
		if(path == null)
			throw new IllegalArgumentException("Path can not be null");
		if(path.length<4) {
			throw new IllegalArgumentException("Path must have 4 items");
		}
		String serverName = path[0];
		String dbName = path[1];
		String cubeName = path[2];
		String viewName = null;
		if(path.length>3) {
			viewName = path[3];
		}
		NameChildLoader serverLoader = new NameChildLoader(this, serverName, IXConsts.TYPE_SERVER);
		NameChildLoader databaseLoader = new NameChildLoader(this, dbName, IXConsts.TYPE_DATABASE);
		NameChildLoader cubeLoader = new NameChildLoader(this, cubeName, IXConsts.TYPE_CUBE);
		ViewNameLoader viewLoader = new ViewNameLoader(this, viewName);
		serverLoader.setCallback(databaseLoader);
		databaseLoader.setCallback(cubeLoader);
		cubeLoader.setCallback(viewLoader);
		viewLoader.setCallback(callback);
		serverLoader.onChildLoaded(getRoot());
	}

    /**
     * Returns used {@link  IEngineService}
     *
     * @returns asynchronous interface to {@link  IEngineService}.
     *
     */
    protected IEngineServiceAsync getEngineService() {
        return this.engineService;
    }
    
    public void loadFavoriteViews () {
    	LoadFavoriteViewsCallback callback = new LoadFavoriteViewsCallback(this);
		callback.send(getEngineService());
    }

    /**
     * Sets loaded {@link XObject} to the given path
     * Sets loaded object to the path and notifies observers.
     * @param path - path that denotes loaded object.
     * @param object - loaded object itself.
     *
     */
    protected void setObject(XObject[] path, XObject[] children, int type) {
        if(children == null)
            throw new IllegalArgumentException("Children for path {"+Arrays.asList(path)+ "} was null.");
        PerformanceTimer timer = new PerformanceTimer("setObject(" + children+")");
        timer.start();
        updater.updateChildren(path, children, type);
        timer.report();
    }
    
	void startUpdateHierarchy() {
		if(updateCounter == 0) {
			updateTimer = new PerformanceTimer("Update hierarchy");
    		updateTimer.start();
		}
		updateCounter++;
	}
	
	
	
	void stopUpdateHierarchy() {
		updateCounter--;
		if(updateCounter == 0) {
			updateTimer.report();
			listeners.fireUpdateComplete();
		}
	}

	void failUpdateHierarchy (Throwable t) {
		updateCounter--;
		if(updateCounter == 0) {
			updateTimer.report("fail: "+t);
			listeners.fireUpdateComplete();
		}
	}
	
	public boolean isUpdatingHierarchy() {
		return updateCounter >0;
	}
	
	LoadingMap getLoadingMap() {
		return loadingMap;
	}
	
	private void removeFromStructure(XPath path) {
		if(path != null) {
			XPathElement lastElement = path.getLastComponent();
			switch (lastElement.getTypeId()) {
			case IXConsts.TYPE_VIEW: {
				cubeViewCache.removeViewWithId(lastElement.getId());
				break;
			}
			default:
				break;
			}
		}
		
	}

    //-- Internal classes section --
    
    private class ForceReloadTreeCallback implements AsyncCallback {

		public void onFailure(Throwable caught) {
	           Logger.error("fail to reload data");
		}

		public void onSuccess(Object result) {
	        XRoot root = getRoot();
            reloadSubTree(root);
		}
    	
    }

	public void setUserMessageQueue(IUserMessageQueue userMessageQueue) {
		this.userMessageQueue = userMessageQueue;
	}

	public IUserMessageQueue getUserMessageQueue() {
		return userMessageQueue;
	}

	public XFolder getFavoriteViewsRoot() {
		return favoriteViewsRoot;
	}

	public void setFavoriteViews(XFolder rootFolder) {
		favoriteViewsRoot = rootFolder;
		listeners.fireFavoriteViewsLoaded();
	}

	public XObject[] getCachedViews(XCube cube) {
		List viewList = cubeViewCache.getViewList(cube);
		XObject[] result = XArrays.toArray(viewList);
		return result;
	}

}
