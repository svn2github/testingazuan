package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.util.client.JavaScript;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.TreeUtil;

public class CubeTableAPIImpl extends Composite implements ITableAPI {

	private static String[][] htmlMapping = {
		{"&","&amp;"},
		{"<","&lt;"},
		{">","&gt;"},
		{"\"","&qout;"},
		{"\'","&#39;"},
	};
	
	private static long nextId = 0;
	private static HashMap idToWin = new HashMap();
	
	private final ArrayList listeners;
	private String id;
	private SimplePanel widget;
	private JavaScriptObject wnd;
	private final TreeEncoder treeEncoder = new TreeEncoder();
	private final List vTreeList = new ArrayList();
	private final List hTreeList = new ArrayList();
	private final Map paramsMap = new HashMap();
	
	
	private static String encodeHtml(String s){
		/*
		 * do not espcape html chars, because there is no need of them in JS
		 * TODO: remove this method. 
		for(int i = 0; i < htmlMapping.length; ++i){
			s = s.replaceAll(htmlMapping[i][0], htmlMapping[i][1]);
		}
		*/
		return s;
	}
	
	private static String decodeHtml(String s){
		for(int i = htmlMapping.length-1; i>=0; --i){
			s = s.replaceAll(htmlMapping[i][1], htmlMapping[i][0]);
		}
		return s;
	}
	
	public CubeTableAPIImpl() {
		listeners = new ArrayList();
		nextId++;
		id = Long.toString(nextId);
		idToWin.put(id, this);
	}
	
	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.ui.client.cubetable.ITableAPI#initWidget()
	 */
	public void initWidget() {
		widget = new SimplePanel();
		widget.setSize("100%", "100%");
		debug("adding iframe id : " + id);
		addIframe(widget.getElement(), "cubetable.html?id="+id);
		super.initWidget(widget);
	}
	
	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.ui.client.cubetable.ITableAPI#clean()
	 */
	public void clean() {
		if(wnd != null) {
			debug("clean()");
			vTreeList.clear();
			hTreeList.clear();
			clean(wnd);
		}
	}
	
	protected void debug(String msg) {
		Logger.debug("CubeTableAPIImpl."+msg);
	}

	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.ui.client.cubetable.ITableAPI#insertTree(java.lang.String, int, java.lang.String)
	 */
	public void insertTree(int direction, ITreeModel tree) {
		insertTree(direction, 0, tree);
	}
	public Widget getWidget() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.ui.client.cubetable.ITableAPI#changeZstate(boolean)
	 */
	public void changeZstate(boolean value) {
		if(wnd != null) {
			debug("changeZstate("+value+")");
			changeZstate(wnd, value);
		}
	}

	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.ui.client.cubetable.ITableAPI#insertTree(java.lang.String, int, int, java.lang.String)
	 */
	public void insertTree(int direction, int pos, ITreeModel tree) {
		if( wnd != null) {
			String encodedTree = treeEncoder.encodeSubTree(tree, null);
			//JS do works propertly with '-symbol; str.replace('\'', ' ')
			List treeList = getTreeList(direction);
			treeList.add(pos, tree);
			encodedTree = encodeHtml(encodedTree);
			debug("insertTree("+direction+", "+pos+", '"+encodedTree+"')");
			insertTree(wnd, direction, pos, encodedTree);
		}
	}
	
    /* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.ui.client.cubetable.ITableAPI#expandTree(java.lang.String, int)
	 */
    public void expandTree(int direction, String treePath) {
    	if(wnd != null) {
    		String message = "expandTree("+treePath+", "+direction+")";
			PerformanceTimer timer = new PerformanceTimer(message);
    		timer.start();
    		debug(message);
    		expandTree(wnd, treePath, direction);
    		timer.report();
    	}
    }
	
	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.ui.client.cubetable.ITableAPI#setCellValue(int, int, java.lang.String)
	 */
	public void setCellValue(int row, int column, String value) {
		if(wnd != null) {
			debug("setCellValue(" + row + ", " + column + ", '" + value+"')");
			setCellValue(wnd, row, column, value);
		}
	}

	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.ui.client.cubetable.ITableAPI#updateData()
	 */
	public void updateData() {
		if(wnd != null) {
			debug("updateData()");
			updateData(wnd);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.ui.client.cubetable.ITableAPI#addListener(com.tensegrity.palowebviewer.modules.ui.client.cubetable.ITableAPIListener)
	 */
	public void addListener(ITableAPIListener listener) {
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.ui.client.cubetable.ITableAPI#removeListener(com.tensegrity.palowebviewer.modules.ui.client.cubetable.ITableAPIListener)
	 */
	public void removeListener(ITableAPIListener listener) {
		listeners.remove(listener);
	}
	
	public void insertChildren(int direction, Object parent, ITreeModel tree) {
		if(wnd != null) {
			int pos = getTreePosition(direction, tree);
			if(pos >= 0) {
				String encodedChildren = treeEncoder.encodeChildren(tree, parent);
				//String nodeName = parent+"";
				encodedChildren = encodeHtml(encodedChildren);
				Object[] parentPath = TreeUtil.getPathToNode(tree, parent);
				parentPath [0] = null;
				String path = encodePath(parentPath);
				path = encodeHtml(path);
				debug("insertChildren("+direction+", "+pos+", "+ path + ", '"+encodedChildren +"')");
				insertChildren(wnd, direction, pos, path, encodedChildren);
			}
		}
	}
	
	private String encodePath(Object[] parentPath) {
		String result = "";
		if(parentPath.length > 0 ) {
			result += parentPath[0];
		}
		for (int i = 1; i < parentPath.length; i++) {
			result += "/" + parentPath[i];
		}
		return result;
	}

	protected List getTreeList (int direction) {
		List result = null;
		switch(direction){
		case ITableAPI.DIRECTION_HORIZONTAL: {
			result = hTreeList;
			break;
		}
		case ITableAPI.DIRECTION_VERTICAL: {
			result = vTreeList;
			break;
		}
		}
		return result;
	}
	
	protected ITreeModel getTree(int direction, int pos) {
		return (ITreeModel)getTreeList(direction).get(pos);
	}
	
	protected int getTreePosition(int direction, ITreeModel tree) {
		return getTreeList(direction).indexOf(tree);
	}
	
	public void setParameter(String key, String value) {
		if(wnd != null){
			debug("setParameter('"+key+"', '"+value+"')");
			setParameter(wnd, key, value);
		}
		else{
			paramsMap.put(key, value);
		}
	}
	// JSNI
    public static void defineBridgeMethods() {
    	defineBridgeMethods(JavaScript.getMainWindow());
    }
    public static native void defineBridgeMethods(JavaScriptObject wnd) /*-{
    	wnd.onCubeTableLoaded = function(id, o) {
       		@com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeTableAPIImpl::onCubeTableLoaded(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(id, o);
    	}
    	wnd.stateChangeRequest = function(id, direction, path) {
       		@com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeTableAPIImpl::onStateChanged(Ljava/lang/String;ILjava/lang/String;)(id, direction, path);
    	}
    	wnd.canCellBeEdited = function(id, xTree, yTree){
    		return @com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeTableAPIImpl::canCellBeEdited(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(id, xTree, yTree);
    	}
    	wnd.updateCell = function(id, xTree, yTree, newValue){
    		@com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeTableAPIImpl::updateCell(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(id, xTree, yTree,newValue);
    	}
    	
    	wnd.validateValue = function(id, xTree, yTree, value){
    		return @com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeTableAPIImpl::validateValue(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(id, xTree, yTree, value);
    	}
    	wnd.isSelectedElementsPlain = function(id){
    		return @com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeTableAPIImpl::isSelectedElementsPlain(Ljava/lang/String;)(id);
    	}
	}-*/;
    
    public static boolean isSelectedElementsPlain(String id){
    	CubeTableAPIImpl cube = (CubeTableAPIImpl)idToWin.get(id);
    	boolean result = true;
    	if ( cube.listeners.size() > 0 ) {
    		for (int i = 0; (i < cube.listeners.size()) && result; i++) {
    			ITableAPIListener listener = (ITableAPIListener)cube.listeners.get(i);
    			result = listener.isSelectedElementsPlain();
    		}
    	}
    	return result;
    }

    public static boolean validateValue(String id, String xTree, String yTree, String value){
    	/*
    	CubeTableAPIImpl cube = (CubeTableAPIImpl)idToWin.get(id);
    	yTree = decodeHtml(yTree);
    	xTree = decodeHtml(xTree);
    	boolean r = true;
    	if ( cube.listeners.size() > 0 ) {
    		for (int i = 0; (i < cube.listeners.size()) && r; i++) {
    			ITableAPIListener listener = (ITableAPIListener)cube.listeners.get(i);
    			r = listener.validate(xTree, yTree, value);
    		}
    	}
    	Logger.debug("validation (" + xTree + " : " + yTree + ") = " + value + " : " + r);
    	return r;
    	*/
    	/*
    	 * deprecated method.
    	 */
    	return true;
    }

    public static void onCubeTableLoaded(String id, JavaScriptObject o) {
    	Logger.debug("onCubeTableLoaded(), id : " + id);
    	CubeTableAPIImpl cube = (CubeTableAPIImpl)idToWin.get(id);
    	cube.wnd = o;
    	cube.setParameters();
    	if ( cube.listeners.size() > 0 ) {
    		for (int i = 0; i < cube.listeners.size(); i++) {
    			ITableAPIListener listener = (ITableAPIListener)cube.listeners.get(i);
    			listener.onLoaded();
    		}
    	}
    	
    }
    
    private void setParameters() {
    	for(Iterator it = paramsMap.entrySet().iterator(); it.hasNext(); ){
    		Map.Entry entry = (Map.Entry) it.next();
    		String key = (String) entry.getKey();
    		String value = (String) entry.getValue();
    		setParameter(wnd, key, value);
    	}
	}

	public static boolean canCellBeEdited(String id, String xTree, String yTree){
    	boolean r = true;
    	xTree = decodeHtml(xTree);
    	yTree = decodeHtml(yTree);
		CubeTableAPIImpl cube = (CubeTableAPIImpl)idToWin.get(id);
		for (int i = 0; i < cube.listeners.size() && r ; i++) {
			ITableAPIListener listener = (ITableAPIListener)cube.listeners.get(i);
			r  = listener.canCellBeEdited(xTree, yTree);
		}
		Logger.debug("can cell be edited : " + r);
    	return r;
    }
    
	public static void onStateChanged(String id, int direction, String path) {
		Logger.debug("onStateChanged("+direction+", "+ path+")");
		path = decodeHtml(path);
    	CubeTableAPIImpl cube = (CubeTableAPIImpl)idToWin.get(id);
    	if ( cube.listeners.size() > 0 ) {
    		for (int i = 0; i < cube.listeners.size(); i++) {
    			ITableAPIListener listener = (ITableAPIListener)cube.listeners.get(i);
    			listener.onStateChanged(direction, path);
    		}
    	}
	}
	
	public static void updateCell(String id, String xTree, String yTree, String value){
		if(Logger.isOn()) {
			Logger.debug("updateCell("+xTree+", "+ yTree + ", " +value+")");
		}
		xTree = decodeHtml(xTree);
		yTree = decodeHtml(yTree);
		CubeTableAPIImpl cube = (CubeTableAPIImpl)idToWin.get(id);
		for (int i = 0; i < cube.listeners.size(); i++) {
			ITableAPIListener listener = (ITableAPIListener)cube.listeners.get(i);
			listener.onCellUpdate(xTree, yTree, value);
		}
	}
	
	private static native void changeZstate(JavaScriptObject wnd, boolean value) /*-{
		wnd.changeZstate(value);
	}-*/;
    
	private static native void addIframe(Element e, String u) /*-{
		$wnd.addIframe(e, u);
	}-*/;
	
	private static native void clean(JavaScriptObject wnd) /*-{
		wnd.clean();
	}-*/;
	
	private static native void insertTree(JavaScriptObject wnd, int direction, int pos, String str) /*-{
		wnd.insertTree(direction, str, pos);
	}-*/;
	
	private static native void insertChildren(JavaScriptObject wnd, int direction, int pos, String parent, String children)/*-{
		wnd.insertChildren(direction, pos, parent, children);
	}-*/;
	
	private static native void setCellValue(JavaScriptObject wnd, int row, int column, String data) /*-{
		wnd.cubeTableSetCellValue(row, column, data);
	}-*/;

	private static native void expandTree(JavaScriptObject wnd, String treePath, int direction) /*-{
		wnd.expand(treePath, direction);
	}-*/;
	

	private static native void updateData(JavaScriptObject wnd) /*-{
		wnd.updateData();
	}-*/;

	private static native void setParameter(JavaScriptObject wnd, String key, String value) /*-{
		wnd.setParameter(key,value);
	}-*/;


}
