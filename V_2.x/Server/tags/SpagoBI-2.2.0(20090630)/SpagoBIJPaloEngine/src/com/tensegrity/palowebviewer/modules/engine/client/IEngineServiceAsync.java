package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XViewPath;

/** 
 * Asynchronous Interfaces for {@link IEngineService}.
 */
public interface IEngineServiceAsync {
	
	public void isAuthenticated(AsyncCallback callback);
	
	public void authenticate(String login, String password, boolean remember, AsyncCallback callback);
	
	public void logoff(AsyncCallback callback);
	
	public void getParentsOf(XPath contextPath, String elementId, AsyncCallback callback);

	public void loadChildren(XPath path, int type, AsyncCallback callback);
	
	public void checkExistance(XPath path, AsyncCallback callback);
	
	public void checkExistance(XPath contextPath, String elementId, AsyncCallback callback);
	
	public void loadDefaultView(XPath path, AsyncCallback callback);
	
	public void query(XQueryPath[] query, AsyncCallback callback);
	
	public void updateData(XPath cubePath, IXPoint point, IResultElement value, AsyncCallback callback);
	
	public void saveView(XViewPath viewPath, AsyncCallback callback);
	
	public void forceReload(AsyncCallback callback);
	
	public void getClientProperties(AsyncCallback callback);

	public void loadFavoriteViews(AsyncCallback callback);

	public void loadChild(XPath path, String childId, int type,  AsyncCallback callback);

	public void loadChildByName(XPath path, String name, int type, AsyncCallback callback);
	
}
