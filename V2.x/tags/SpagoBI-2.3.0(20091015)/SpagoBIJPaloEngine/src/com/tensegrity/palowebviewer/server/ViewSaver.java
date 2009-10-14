/**
 * 
 */
package com.tensegrity.palowebviewer.server;

import java.util.Iterator;
import java.util.List;

import org.palo.api.Axis;
import org.palo.api.Connection;
import org.palo.api.Cube;
import org.palo.api.CubeView;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.Property;
import org.palo.api.Subset;
import org.palo.api.utils.ElementPath;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XStringizedElementPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XViewPath;
import com.tensegrity.palowebviewer.server.paloaccessor.PaloAccessor;
import com.tensegrity.palowebviewer.server.paloaccessor.PaloHelper;

public final class ViewSaver implements IXConsts{
    private XViewPath viewPath;
    private Connection connection;
    private XCube xCube;
    private Cube cube;
    private Database database;
    private CubeView view;
    private String viewName;
    private String viewId;
    private final PaloAccessor accessor;
    
    public ViewSaver(PaloAccessor accessor) {
    	this.accessor = accessor;
    }
    
    public void save() throws InvalidObjectPathException {
        initFields();
        view = addCubeView();
        addAxes();
        addDescription();
        view.save();
        viewId = view.getId();
    }

    protected void addDescription() {
        String description = viewPath.getDescription();
        if(description != null){
            view.setDescription(description);
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setViewPath(XViewPath viewPath) {
        this.viewPath = viewPath;
    }

    protected void initFields() throws InvalidObjectPathException {
        XPath cubePath = viewPath.getCube();
    	xCube = (XCube) accessor.getLastObject(cubePath, connection);
        //xCube.setDimensions((XDimension[])builder.loadChildren(cubePath, TYPE_DIMENSION, connection));
    	xCube.setViews((XView[])accessor.loadChildren(cubePath, TYPE_VIEW, connection));
        viewName = viewPath.getViewName();
        viewId = viewPath.getViewId();
        cube = (Cube) xCube.getNativeObject();
        database = cube.getDatabase();
    }

    protected CubeView addCubeView() {
        CubeView result;
        CubeView prevView = PaloHelper.getCubeView(cube, viewId);
        if(prevView != null){
            result = prevView;
            Axis[] axes = result.getAxes();
            for (int i = 0; i < axes.length; i++) {
                result.removeAxis(axes[i]);
            }
        }
        else {
        	Property[] props = {};
            result = cube.addCubeView(viewName, props);
        }
        return result;
    }

    protected Dimension getDimension (XPath path) throws InvalidObjectPathException {
        return (Dimension)getXDimension(path).getNativeObject();
    }

    protected XDimension getXDimension (XPath path) throws InvalidObjectPathException{
        XDimension result = (XDimension) accessor.getLastObject(path, connection);
        return result;
    }

    protected Subset getSubset (XPath path) throws InvalidObjectPathException {
        return (Subset)getXSubset(path).getNativeObject();
    }

    protected XSubset getXSubset (XPath path) throws InvalidObjectPathException{
        XSubset result = (XSubset) accessor.getLastObject(path, connection);
        return result;
    }

    protected Element getElement (XPath path) throws InvalidObjectPathException {
        return (Element)getXElement(path).getNativeObject();
    }

    protected XElement getXElement (XPath path) throws InvalidObjectPathException{
        XElement result = (XElement) accessor.getLastObject(path, connection);
        return result;
    }

    public void addDimension(Axis axis, XPath dimensionPath) throws InvalidObjectPathException {
        Dimension dimension = getDimension(dimensionPath);
        axis.add(dimension);
        setActiveSubset(axis, dimensionPath, dimension);
        setSelectedElement(axis, dimensionPath, dimension);
    }

	private void setSelectedElement(Axis axis, XPath dimensionPath, Dimension dimension) throws InvalidObjectPathException {
		XPath selectedElementPath = viewPath.getSelectedElemetn(dimensionPath);
        if(selectedElementPath != null){
            Element element = getElement(selectedElementPath);
            axis.setSelectedElement(dimension, element);
        }
	}

	private void setActiveSubset(Axis axis, XPath dimensionPath, Dimension dimension) throws InvalidObjectPathException {
		XPath subsetPath = viewPath.getActiveSubset(dimensionPath);
        if(subsetPath != null){
            Subset subset = getSubset(subsetPath);
            axis.setActiveSubset(dimension, subset);
        }
	}

    protected Element[] getElementPath(Element element) {
        Element[] parents = element.getParents();
        int size = parents == null? 1 : parents.length +1;
        Element[] elementPath = new Element[size];
        if(size > 1)
            System.arraycopy(parents, 0, elementPath, 0, parents.length);
        elementPath[size-1] = element;
        return elementPath;
    }

    public void addDimensions(Axis axis, XPath axisPath) throws InvalidObjectPathException {
        for(Iterator it2 = viewPath.getDimensions(axisPath).iterator(); it2.hasNext(); ){
            XPath dimensionPath = (XPath) it2.next();
            addDimension(axis, dimensionPath);
        }
    }

    private void addAxis(XPath axisPath) throws InvalidObjectPathException {
        String axisName = axisPath.getLastComponent().getName();
        Axis axis = view.addAxis(axisName, axisName);
        addDimensions(axis, axisPath);
        addExpandedPaths(axis, axisPath);
    }
    
    private void addExpandedPath(XStringizedElementPath path, Axis axis){
		String[] dimId = path.getDimensionIds();
		ElementPath nativeElementPath = new ElementPath();
		for (int i = 0; i < dimId.length; i++) {
			String dimensionId = dimId[i];
			String[] elementId = path.getPart(dimensionId);
			Dimension dimension = database.getDimensionById(dimensionId);
			Element[] elements = new Element[elementId.length];
			for (int j = 0; j < elementId.length; j++) {
				String elementName = elementId[j];
				elements[j] = dimension.getElementById(elementName);
			}
			nativeElementPath.addPart(dimension, elements);
		}
		axis.addExpanded(nativeElementPath);
    }

    private void addExpandedPaths(Axis axis, XPath axisPath) {
    	List paths = viewPath.getExpandedPaths(axisPath);
    	if(paths != null){
	    	for (Iterator it = paths.iterator(); it.hasNext();) {
				XStringizedElementPath path = (XStringizedElementPath) it.next();
				addExpandedPath(path, axis);
			}
    	}
	}

	private void addAxes() throws InvalidObjectPathException {
        List axises = viewPath.getAxises();
        for (Iterator it = axises.iterator(); it.hasNext();) {
            XPath axisPath = (XPath) it.next();
            addAxis(axisPath);
        }
    }

	public String getSavedViewId() {
		return viewId;
	}

}