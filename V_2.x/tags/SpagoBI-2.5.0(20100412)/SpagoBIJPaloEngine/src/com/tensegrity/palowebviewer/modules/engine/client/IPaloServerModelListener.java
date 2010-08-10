package com.tensegrity.palowebviewer.modules.engine.client;


import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;


/**
 * Listener for the {@link  IPaloServerModel}.
 */
public interface IPaloServerModelListener
{

    public void onChildArrayChanged(XObject[] path, XObject[] oldChildren, int type);

    /**
     * This method will be invoked if model was deeply changed.
     */
    public void modelChanged();
    
    public void onError(Throwable cause);
    
    public void onUpdateComplete();
    
    public void defaultViewLoaded(XCube cube);

	public void objectRenamed(XObject object);
	
	public void onFavoriteViewsLoaded();

	public void objectInvalide(XPath path);
    
}
