package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.XViewLink;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.util.client.Arrays;
import com.tensegrity.palowebviewer.modules.util.client.ICallback;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class PaloServerModelLogger extends PaloServerModelProxy {

    public void addListener(IPaloServerModelListener listener) {
        debug("addListener");
        super.addListener(listener);
    }

    public void checkElement(XDimension dim, XElement element, IVerificationCallback callback) {
        debug("checkElement(dim = "+dim+", element= "+element+", callback ="+callback+")" );
        super.checkElement(dim, element, callback);
    }

    public void checkElement(XSubset subset, XElement element, IVerificationCallback callback) {
        debug("checkElement(subset = "+subset+", element= "+element+", callback ="+callback+")" );
        super.checkElement(subset, element, callback);
    }

    public void clear() {
        debug("clear()");
        super.clear();
    }

    public XElement getElement(XDimension dimension, XElement element) {
        return super.getElement(dimension, element);
    }

    public XObject getObject(XPath path) {
        return super.getObject(path);
    }

    public XRoot getRoot() {
        return super.getRoot();
    }

    public boolean isObjectValid(XObject object) {
        return super.isObjectValid(object);
    }

    public boolean isOn() {
        return super.isOn();
    }

    public void loadChildren(XObject object, int type) {
        debug("loadChildren(object="+object+", type="+XHelper.typeToString(type)+")");
        super.loadChildren(object, type);
    }

    public void loadDefaultView(XCube cube) {
        debug("loadDefaultView("+cube+")");
        super.loadDefaultView(cube);
    }

    public void loadPath(XObject hierarchyRoot, XElement element, IElementPathCallback callback) {
        debug("loadPath( root="+hierarchyRoot+", element="+element+", callback="+callback+")");
        super.loadPath(hierarchyRoot, element, callback);
    }

    public void query(XQueryPath[] queries, IQueryCallback callback) {
        debug("query( queries.size="+queries.length+", callback="+callback+")");
        super.query(queries, callback);
    }

    public void reloadSubTree(XObject obejct) {
        debug("reloadSubTree("+obejct+")");
        super.reloadSubTree(obejct);
    }

    public void reloadTree() {
        debug("reloadTree()");
        super.reloadTree();
    }

    public void removeListener(IPaloServerModelListener listener) {
        debug("removeListener()");
        super.removeListener(listener);
    }

    public void saveView(XView view, ICallback callback) {
        debug("saveView("+view+", "+callback+")");
        super.saveView(view, callback);
    }

    public void turnOff() {
        debug("turnOff()");
        super.turnOff();
    }

    public void turnOn() {
        debug("turnOn()");
        super.turnOn();
    }
    
    public void checkAndSave(XView view, ICallback callback) {
    	debug("checkAndSave("+view+", "+callback+")");
		super.checkAndSave(view, callback);
	}

    public void updateCell(XPath cube, IXPoint point, IResultElement value, ICellUpdateCallback callback) {
        debug("updateCell( cube="+cube+", point="+point+", value="+value+", callback="+callback+")");
        super.updateCell(cube, point, value, callback);
    }
    
    public void loadFavoriteViews() {
    	debug("loadFavoriteViews()");
        super.loadFavoriteViews();
    }

    public void loadView(XViewLink link, ILoadViewCallback callback) {
        debug("loadView(" + link + ")");
        super.loadView(link, callback);
	}
    
    public void loadView(String[] path, ILoadViewCallback callback) {
        debug("loadView(" + Arrays.toString(path) + ")");
        super.loadView(path, callback);
	}


    public PaloServerModelLogger(IPaloServerModel paloServerModel) {
        super(paloServerModel);
    }

    protected void debug(String message) {
        message = "[PaloServerModel] "+message;
        Logger.debug(message);
    }

    public String toString() {
        return "PaloServerModelLogger["+getPaloServerModel()+"]";
    }

}
