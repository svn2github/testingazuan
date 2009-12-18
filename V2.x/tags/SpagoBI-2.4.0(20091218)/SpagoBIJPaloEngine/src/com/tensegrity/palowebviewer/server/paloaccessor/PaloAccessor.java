package com.tensegrity.palowebviewer.server.paloaccessor;

import java.util.Locale;

import org.palo.api.Connection;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.api.ext.subsets.SubsetHandler;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XResult;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XViewPath;
import com.tensegrity.palowebviewer.server.IConnectionPool;
import com.tensegrity.palowebviewer.server.PaloConfiguration;
import com.tensegrity.palowebviewer.server.ViewSaver;

public class PaloAccessor implements IXConsts{

	
	private final XObjectPathCache objCache = new XObjectPathCache(); //key: xpath, value: XObject

    /**
     * constructs root element for server
     */
    public XRoot getRoot(IConnectionPool pool){
        XRoot root = new XRoot();
        return root;
    }
    
    /**
    *
    * constructs object for given path. This object has parent, so always there is way to access root.
    * @param connection - connection for given path. You should supply correct connection to this path,
    * according to server. Server name - is second object in path string array. 
    * @throws InvalidObjectPathException 
    */
   public XObject getLastObject(XPath path, Connection connection) throws InvalidObjectPathException{
   	XObject r = objCache.getObject(path, connection);
   	return r; 
   }

   /**
    * initializes root object.
    */
   public XRoot init(XRoot root, IConnectionPool pool, PaloConfiguration cfg){
       root.setServers(PaloHelper.getXServers(pool, cfg, root));
       return root;
   }

	public XObject[] loadChildren(XPath path, int type, Connection connection) throws InvalidObjectPathException{
		XObject obj = getLastObject(path, connection);
		ChildLoader loader = new ChildLoader(type, connection);
		loader.visit(obj);
		XObject[] children = loader.getChildren();
		if(children == null){
			throw new RuntimeException("can't load children for " + path + " of type " + type);
		}
		return children;
	};
	
	public XElement[] getParentsOf(XPath ctx, String elementId, Connection connection) throws InvalidObjectPathException{
		XObject ctxObject = getLastObject(ctx, connection);
		XDimension xDimension; //parent
		int ctxType = ctxObject.getTypeID();
		ElementNode[] rootNodes;
		if(ctxType == TYPE_DIMENSION){
			xDimension = (XDimension) ctxObject;
			Dimension dimension = (Dimension) xDimension.getNativeObject();
			rootNodes = dimension.getElementsTree();
		}else if(ctxType == TYPE_SUBSET){
			XSubset xSubset = (XSubset) ctxObject;
			xDimension = (XDimension) xSubset.getParent();
			rootNodes = PaloHelper.getSubsetNativeNodes(xSubset);
		}else{
			throw new InvalidObjectPathException("wrong context : " + ctx, ctx);
		}
		ElementNode[] list = PaloHelper.getParentPath(rootNodes, elementId);
		XElement[] r = PaloHelper.convert(list, xDimension);
		return r;
	}
    
    public XResult fetch(XQueryPath query, Connection connection) throws InvalidObjectPathException{
        XResultFetcher fetcher = new XResultFetcher(this, query, connection);
        return fetcher.fetch();
    }

    public void update(XPath cubePath, IXPoint point, IResultElement value, Connection connection, Locale locale) throws InvalidObjectPathException{
        CellUpdater updater = new CellUpdater(this);
        updater.setConection(connection);
        updater.setPoint(point);
        updater.setCubePath(cubePath);
        updater.setResultElement(value);
        updater.setLocale(locale);
        updater.update();
    }

    public String save(XViewPath viewPath, Connection connection) throws InvalidObjectPathException{
        ViewSaver viewSaver = new ViewSaver(this);
        viewSaver.setViewPath(viewPath);
        viewSaver.setConnection(connection);
        viewSaver.save();
        return viewSaver.getSavedViewId();
    }

    public boolean checkExistance(XPath ctx, String id, Connection connection) throws InvalidObjectPathException{
    	boolean r = false;
    	XObject ctxObject = getLastObject(ctx, connection);
    	int ctxType = ctxObject.getTypeID();
		if(ctxType == TYPE_DIMENSION){
			Dimension d = (Dimension) ctxObject.getNativeObject();
			r = d.getElementById(id) != null;
		}else if(ctxType == TYPE_SUBSET){
			XSubset s = (XSubset) ctxObject;
			SubsetHandler handler = PaloHelper.getSubetHandler(s);
			Element[] elements = handler.getVisibleElements();
			Element element = (Element) IdFinder.find(elements, id);
			r = element != null;
		}else{
			throw new InvalidObjectPathException("wrong context : " + ctx, ctx);
		}
    	return r;
    }

}
