package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InternalErrorException;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDefaultView;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XFolder;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XResult;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XViewPath;

/**
 * Interface for communication with server 
 */
public interface IEngineService extends RemoteService {
	
	/**
	 * Checks if user is already  authenticated. If not, tries to do that via cookies.
	 * <p> 
	 * If authentication succeed cookies max age setted up to 1 month.  
	 * 
	 * @return true, is authentication succeed.
	 */
	public Boolean isAuthenticated();
	
	/**
	 * This method authenticates the user. 
	 * @param login - user login. Can't be null.
	 * @param password - user password. Can't be null. 
	 * @param remember - remember user and password hash in cookies.
	 * @return true, if authentication succeed.
	 * @throws AuthenticationError if something fatal-like occurred.
	 */
	public Boolean authenticate(String login, String password, boolean remember) throws InternalErrorException;
	
	/**
	 * Log off from the server. The cookies are NOT DELETED, i.e. when user types application url the 
	 * next time {@link #authenticate()} will return TRUE.
	 *
	 */
	public void logoff();
	 
	/**
	 * This method returns parents elements in element hierarchy for particular element.
	 * @param contextPath defines context of element hierarcy. Can point only to dimension or subset. Can't be null.
	 * @param elementName element name. Can't be null.
	 * @throws InvalidObjectPathException if 
	 * 1. contextPath is invalid;
	 * 2. contextPath doesn't point to dimension or subset.
	 * 3. element with elementName can't be found. 
	 * @return array of ancestors elements.
	 */
	public XElement[] getParentsOf(XPath contextPath, String elementName) throws InvalidObjectPathException;

	/**
	 * This method constructs "default view", i.e. when user opens cube (e.g. first two dimensions are col- and row-axices etc.) 
	 * @param path points to cube. Can't be null.
	 * @return constructed default view.
	 * @throws InvalidObjectPathException if path it invalid or it doen't point to cube.
	 */
	public XDefaultView loadDefaultView(XPath path) throws InvalidObjectPathException;
	
	/**
	 * This method loads all children (XObjects) of type of object specified by path.  
	 * @param path for object. Can't be null.
	 * @param type specifies chilren type. {@link IXConsts}. If object has only one chilren type, type param have to be ingnored.
	 * @return children array. 
	 * @exception InvalidObjectPathException will be thrown if path is wrong 
	 * if type is wrong program behavior is unpredicted (e.g. runtime exception will be thrown)
	 */
	public XObject[] loadChildren(XPath path, int type) throws InvalidObjectPathException;
	
	/**
	 * This methods checks existance of objects, specified via path.
	 * @param path for object. Can't be null.
	 * @return true, if object defined via path exists; and false otherwise
	 */
	public boolean checkExistance(XPath path);
	
	/**
	 * More specified version of {@link #checkExistance(XPath)} : it checks existance by name in some context.
	 * @param contextPath context path (currently: dimension/subset)
	 * @param elementName element name
	 * @return true, if element exists, false otherwise
	 * @throws InvalidObjectPathException if contextPath is invalid or not points to dimension/subset 
	 */
	public boolean checkExistance(XPath contextPath, String elementName) throws InvalidObjectPathException;
	
	/**
	 * This method returns data for cube, specified by query. @{link XResult}
	 * This is batch version of query, e.g. it returs data for array of paths.
	 * <p>
	 * Assumption: all paths points to one server. Otherwise behavior is unpredicted (runtime exception will be thrown).
	 * @param paths - array of requests {@link XQueryPath}. It can't be null.
	 * @return array of results. 
	 * @throws InvalidObjectPathException is thrown if one of paths doesn't points to cube or is invalid. 
	 */
	public XResult[] query(XQueryPath[] paths) throws InvalidObjectPathException;
	
	/**
	 * This method sets value of some cell of cube pointed by point
	 * @param cubePath points to target cube
	 * @param point contains coordinates of point. @{link IXPoint}
	 * @param value of the point. 
	 * @throws InvalidObjectPathException if cubePath is not valid path.
	 */
	public void updateData(XPath cubePath, IXPoint point, IResultElement value)  throws InvalidObjectPathException;
	
	/**
	 * This method updates view if the one already exists and adds view otherwise. 
	 * @param viewPath defines the view. @{link XViewPath}.
	 * @return id of saved or newly created view
	 * @throws InvalidObjectPathException one of paths in viewPath is invalid.
	 */
	public String saveView(XViewPath viewPath)  throws InvalidObjectPathException;
	
	
	/**
	 * forces reload of all connection pools, e.g. all opened connections are marked to be reload. 
	 * They will be actualy reloaded on the next use (e.g. query, updateData etc.)
	 */
	public void forceReload();
	
	/**
	 * Retuns client properties from server.
	 * @return client properties.
	 */
	public IClientProperties getClientProperties();
	
	/**
	 * Loads favorite views from server.
	 * @return root folder of all favorite views. @{link XFolder}
	 */
	public XFolder loadFavoriteViews();
	
	/**
	 * This methods loads single child specified by path to parend, id and type
	 * @param path poinst to parent object.
	 * @param childId child identificator
	 * @param type child type.
	 * @return loaded child. Null if child can't be found.
	 * @throws InvalidObjectPathException if path is invalid.
	 */
	public XObject loadChild(XPath path, String childId, int type) throws InvalidObjectPathException;
	
	public XObject loadChildByName(XPath path, String name, int type) throws InvalidObjectPathException;
}
