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
import com.tensegrity.palowebviewer.modules.util.client.Assertions;
import com.tensegrity.palowebviewer.modules.util.client.ICallback;

public class PaloServerModelProxy implements IPaloServerModel {

    private final IPaloServerModel paloServerModel;

    protected IPaloServerModel getPaloServerModel() {
        return paloServerModel;
    }

    public PaloServerModelProxy(IPaloServerModel paloServerModel) {
        Assertions.assertNotNull(paloServerModel, "paloServerModel");
        this.paloServerModel = paloServerModel;
    }

    public void addListener(IPaloServerModelListener listener) {
        paloServerModel.addListener(listener);
    }

    public void checkElement(XDimension dim, XElement element, IVerificationCallback callback) {
        paloServerModel.checkElement(dim, element, callback);
    }

    public void checkElement(XSubset subset, XElement element, IVerificationCallback callback) {
        paloServerModel.checkElement(subset, element, callback);
    }

    public void clear() {
        paloServerModel.clear();
    }

    public XElement getElement(XDimension dimension, XElement element) {
        return paloServerModel.getElement(dimension, element);
    }

    public XObject getObject(XPath path) {
        return paloServerModel.getObject(path);
    }

    public XRoot getRoot() {
        return paloServerModel.getRoot();
    }

    public boolean isObjectValid(XObject object) {
        return paloServerModel.isObjectValid(object);
    }

    public boolean isOn() {
        return paloServerModel.isOn();
    }

    public void loadChildren(XObject object, int type) {
        paloServerModel.loadChildren(object, type);
    }

    public void loadDefaultView(XCube cube) {
        paloServerModel.loadDefaultView(cube);
    }

    public void loadPath(XObject hierarchyRoot, XElement element, IElementPathCallback callback) {
        paloServerModel.loadPath(hierarchyRoot, element, callback);
    }

    public void query(XQueryPath[] queries, IQueryCallback callback) {
        paloServerModel.query(queries, callback);
    }

    public void reloadSubTree(XObject obejct) {
        paloServerModel.reloadSubTree(obejct);
    }

    public void reloadTree() {
        paloServerModel.reloadTree();
    }

    public void removeListener(IPaloServerModelListener listener) {
        paloServerModel.removeListener(listener);
    }

    public void saveView(XView view, ICallback callback) {
        paloServerModel.saveView(view, callback);
    }

    public void turnOff() {
        paloServerModel.turnOff();
    }

    public void turnOn() {
        paloServerModel.turnOn();
    }

    public void updateCell(XPath cube, IXPoint point, IResultElement value, ICellUpdateCallback callback) {
        paloServerModel.updateCell(cube, point, value, callback);
    }

    public String toString() {
        return "PaloServerModelProxy["+getPaloServerModel()+"]";
    }

	public void checkAndSave(XView view, ICallback callback) {
		paloServerModel.checkAndSave(view, callback);
	}

	public boolean isUpdatingHierarchy() {
		return paloServerModel.isUpdatingHierarchy();
	}

	public XFolder getFavoriteViewsRoot() {
		return paloServerModel.getFavoriteViewsRoot();
	}

	public void loadFavoriteViews() {
		paloServerModel.loadFavoriteViews();
	}

	public void loadView(XViewLink link, ILoadViewCallback callback) {
		paloServerModel.loadView(link, callback);
		
	}
	
	public void loadView(String[] path, ILoadViewCallback callback) {
		paloServerModel.loadView(path, callback);
	}

}
