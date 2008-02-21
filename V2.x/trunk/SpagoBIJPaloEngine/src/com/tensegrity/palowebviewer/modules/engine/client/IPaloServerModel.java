package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XFolder;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.XViewLink;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.util.client.ICallback;


/**
 * Model of Palo servers.
 * It is used to load and cache Palo server and database structures.
 */
public interface IPaloServerModel
{

    /**
     * Returns root of the server. So we can ask for other things on it.
     */
    public XRoot getRoot();

    public void loadChildren(XObject object, int type);

    public void reloadTree();

    public void reloadSubTree(XObject obejct);
    
    public boolean isUpdatingHierarchy();
    
    public XFolder getFavoriteViewsRoot();
    
    public void loadFavoriteViews();
    
    /**
     * Add listener for the {@link IPaloServerModel palo server model}.
     * @param listener - listener to add. 
     */
    public void addListener(IPaloServerModelListener listener);

    /**
     * Remove listener for the {@link IPaloServerModel palo server model}.
     * @param listener - listener to remove. 
     */
    public void removeListener(IPaloServerModelListener listener);
    
    public XObject getObject(XPath path);
    
    public void saveView(XView view, ICallback successCallback);
    
    public void checkAndSave(XView view, ICallback successCallback);
    
    public XElement getElement(XDimension dimension, XElement element);
    
    /**
     *
     * checks if the object belongs to actual model
     * 
     */
    public boolean isObjectValid(XObject object);
    
    public void loadPath(XObject hierarchyRoot, XElement element, IElementPathCallback callback);
    
    /**
     * Clears the {@link XRoot root} of the model and notifies listeners({@link  IPaloServerModelListener#modelChanged}).
     */
    public void clear();
    
    public void loadDefaultView(XCube cube);
    
    public void checkElement(XDimension dim, XElement element, IVerificationCallback callback);
    
    public void checkElement(XSubset dim, XElement element, IVerificationCallback callback);
    
    /**
     * Tells whether this model is on.
     */
    public boolean isOn();
    
    /**
     * Turns on the model.
     * Turns on the model, so it can be used to communicate with server.
     */
    public void turnOn();
    
    /**
     * Turns off the model.
     * Turns off the model, so it can not be used to communicate with server.
     * Call to {@link  #loadObject} will be ignored.
     */
    public void turnOff();
    
    /**
     * Updates value pointed by path. Callback is invoked if update successed
     * @param path
     * @param value
     * @param callback - can be null
     */
    public void updateCell(XPath cube, IXPoint point, IResultElement value, ICellUpdateCallback callback);
    
    public void query(XQueryPath[] queries, IQueryCallback callback);
    
    public void loadView(XViewLink link, ILoadViewCallback callback);

	public void loadView(String[] path, ILoadViewCallback loadViewCallback);

}
