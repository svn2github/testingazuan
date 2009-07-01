package com.tensegrity.palowebviewer.server.paloaccessor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.palo.api.Axis;
import org.palo.api.Connection;
import org.palo.api.Consolidation;
import org.palo.api.Cube;
import org.palo.api.CubeView;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.api.Subset;
import org.palo.api.SubsetState;
import org.palo.api.exceptions.PaloIOException;
import org.palo.api.ext.subsets.SubsetHandler;
import org.palo.api.ext.subsets.SubsetHandlerRegistry;
import org.palo.api.ext.subsets.states.FlatState;
import org.palo.api.ext.subsets.states.HierarchicalState;
import org.palo.api.ext.subsets.states.RegExState;
import org.palo.api.utils.ElementPath;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.IElementType;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXSubsetType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XConsolidatedElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XConsolidatedType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.XFlatSubsetType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XInvalidType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XManualHierarchySubsetType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XNumericType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRegexpSubsetType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRuleType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.paloclient.client.XStringType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.util.client.Arrays;
import com.tensegrity.palowebviewer.server.IConnectionPool;
import com.tensegrity.palowebviewer.server.PaloConfiguration;


public class PaloHelper {
	
	private static final Logger logger = Logger.getLogger(PaloHelper.class);
	
	public static XElement[] convert(ElementNode[] nodes, XDimension dimension){
		XElement[] r = new XElement[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			Element element = nodes[i].getElement();
			XElement xElement = wrapElement(element, dimension);
			r[i] = xElement;
		}
		return r;
	}
	
	public static ElementNode[] getParentPath(ElementNode[] rootNodes, String elementId) throws InvalidObjectPathException{
		ElementNode node = findElementNode(elementId, rootNodes);
		if(node == null) throw new InvalidObjectPathException("no element with id '" + elementId + "' found", null);
		List list = new ArrayList();
		ElementNode parent = node.getParent();
		while(parent != null){
			list.add(0, parent);
			parent = parent.getParent();
		}
		ElementNode[] r = (ElementNode[]) list.toArray(new ElementNode[0]);
		return r;
	}
	
	public static XAxis wrapAxis(Axis axis, XView parent){
    	XAxis r = new XAxis(axis.getId(), null, null, null);
    	r.setNativeObject(axis);
    	r.setParent(parent);
    	return r;
    }
    
	public static Axis findAxisById(CubeView cubeView, String id) {
        Axis[] axises = cubeView.getAxes();
        Axis r = null;
        for (int i = 0; (i < axises.length) && (r == null) ; i++) {
            if(axises[i].getId().equals(id)){
                r = axises[i];
            }
        }
        return r;
    }

    
	public static XSubset wrapSubset(Subset subset, XObject parent){
        XSubset r = null;
        if(subset != null) {
        	r = new XSubset(subset.getId(), subset.getName(), null);
        	r.setParent(parent);
        	r.setType(getSubsetType(subset));
        	r.setNativeObject(subset);
        }
        return r;
    }
    
	public static IXSubsetType getSubsetType(Subset subset){
    	IXSubsetType r = null;
    	SubsetState state = subset.getActiveState();
    	if(state != null){
	    	String id = state.getId(); 
	    	if(HierarchicalState.ID.equals(id)){
	    		r = XManualHierarchySubsetType.getInstance();
	    	}else if(RegExState.ID.equals(id)){
	    		r = XRegexpSubsetType.getInstance();
	    	}else if(FlatState.ID.equals(id)){
	    		r = XFlatSubsetType.getInstance();
	    	}else{
	    		throw new RuntimeException("unexpected subset type : " + id + " for subset " +subset.getName());
	    	}
    	}
    	return r;
    }
    
	public static IElementType wrapElementType(Element element){
        int type = element.getType();
        IElementType xType = null;
        switch(type){
        	case Element.ELEMENTTYPE_NUMERIC: {
        		xType = XNumericType.instance;
        		break;
        	}
        	case Element.ELEMENTTYPE_STRING: {
        		xType = XStringType.instance;
        		break;
        	}
        	case Element.ELEMENTTYPE_RULE: {
        		xType = XRuleType.instance;
        		break;
        	}
        	case Element.ELEMENTTYPE_CONSOLIDATED:{
        		xType = XConsolidatedType.instance;
        		break;
        	}
        	default:
        		throw new RuntimeException("unexpected element type : " 
        				+ element.getTypeAsString() + " for element " + element);
        }
        return xType;
    }
    
	public static XElement wrapElement(Element element, XObject parent){
        XElement r = null;
        if(element != null) {
        	IElementType type = wrapElementType(element);
        	String name = element.getName();
        	String id = element.getId();
        	if(type.equals(XConsolidatedType.instance)){
        		r = new XConsolidatedElement(id, name);
        	}else{
        		r = new XElement(id, name, type);
        	}
        	r.setNativeObject(element);
        	r.setParent(parent);
        }
        return r;
    }

	public static XElement[] getXElements(Consolidation[] consolidation, XObject parent){
        XElement[] r = new XElement[consolidation.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = wrapElement(consolidation[i].getChild(), parent);
        }
        return r;
    }

	public static XCube wrapCube(Cube cube){
		XCube r = null;
		if(cube != null) {
			r = new XCube(cube.getId(), cube.getName());
			int viewCount = cube.getCubeViewCount();
			if(viewCount == 0) r.setViews(new XView[]{});
			r.setNativeObject(cube);
		}
        return r;
    }

	public static XCube[] getXCubes(Cube[] cubes, XDatabase db){
        List list = new ArrayList();
        for (int i = 0; i < cubes.length; i++) {
            if(!cubes[i].getName().startsWith("#")){
                XCube cube = wrapCube(cubes[i]);
                cube.setParent(db);
                list.add(cube);
            }
        }
        return (XCube[])list.toArray(new XCube[0]);
    }

    
	public static XDimension wrapDimension(Dimension dimension){
    	XDimension r = null;
    	if(dimension != null ) {
    		r = new XDimension(dimension.getId(), dimension.getName());
    		if(dimension.getElementCount() == 0) r.setNodes(new XElementNode[0]);
    		if(dimension.getSubsets().length == 0) r.setSubsets(new XSubset[0]);
    		r.setNativeObject(dimension);
    	}
        return r;
    }
    
	public static XDimension[] getXDimensions(Dimension[] dimensions, XDatabase db){
        List list = new ArrayList();
        for (int i = 0; i < dimensions.length; i++) {
            if(!dimensions[i].getName().startsWith("#")){
                XDimension dimension = wrapDimension(dimensions[i]);
                dimension.setParent(db);
                list.add(dimension);
            }
        }
        return (XDimension[])list.toArray(new XDimension[0]);
    }

	public static XDatabase wrapDatabase(Database db){
    	XDatabase r = null;
    	if(db != null) {
    		r = new XDatabase(db.getId(), db.getName(), null);
    		r.setNativeObject(db);
    	}
    	return r;
    }
	
    public static int[] getDimensionOrder(XCube xCube, List dimensionPaths){
        int[] result = new int[dimensionPaths.size()];
        XDimension[] cubeDimensions = xCube.getDimensions();
        for(int i = 0; i < result.length; ++i){
            XPath cubePath = cubeDimensions[i].constructPath();
            int index = dimensionPaths.indexOf(cubePath);
            if(index >= 0)
                result[index] = i;
        }
        return result;
    }
    
	public static XDatabase[] getXDatabases(Connection connection, XServer server){
        Database[] dbs = connection.getDatabases();
        Database[] systemDbs = connection.getSystemDatabases();
        XDatabase[] r = new XDatabase[dbs.length - systemDbs.length];
        for (int i = 0, idx = 0; i < dbs.length; i++) {
            Database database = dbs[i];
            if(!isSystemDb(systemDbs, database)){
            XDatabase xdb = wrapDatabase(database);
            r[idx] = xdb;
            r[idx].setParent(server);
            ++idx;
            }
        }
        return r;
    }

	private static boolean isSystemDb(Database[] systemDbs, Database database) {
		boolean skip = Arrays.indexOf(systemDbs, database)>=0;
		return skip;
	}


	public static XServer wrapServer(Connection connection){
        String service = connection.getService();
        XServer r = new XServer(connection.getServer(), service, connection.getUsername(), null);
        r.setNativeObject(connection);
        return r;
    }

	public static XServer[] getXServers(IConnectionPool pool, PaloConfiguration cfg, XRoot root){
        XServer[] r = new XServer[pool.getServerCount()];
        String[] servers = pool.getServerNames();
        String[] services = pool.getServerServices();
        for (int i = 0; i < servers.length; i++) {
            PaloConfiguration.PaloServer server = cfg.getServer(servers[i], services[i]);
            r[i] = new XServer(server.getHost(), server.getService(), server.getLogin(), null);
            r[i].setParent(root);
            r[i].setDispName(server.getDispName());
        }
        return r;
    }

	public static double[] getWeights(Consolidation[] nodes){
        double[] r = new double[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            r[i] = nodes[i].getWeight();
        }
        return r;
    }

	public static XSubset[] getXSubsets(XDimension dimension) {
        Dimension dim = (Dimension) dimension.getNativeObject();
        Subset[] subsets = dim.getSubsets();
        XSubset[] r = new XSubset[subsets.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = wrapSubset(subsets[i], dimension);
        }
        return r;
    }
    
	public static XElement wrapElementWithParents(Element element, XDimension dimension){
        Element[] parents = element.getParents();
        XObject prevParent = dimension;
        if(parents != null){
            XElement[] xParents = new XElement[parents.length];
            for (int k = 0; k < parents.length; k++) {
                xParents[k] = wrapElement(parents[k], prevParent);
                prevParent = xParents[k];
            }
        }
        XElement r = wrapElement(element, prevParent);
        return r;
    }
    
	public static XElementNode wrapElementNode(ElementNode node, XObject parent, XDimension dimension){
		XElementNode r =  null;
    	if(node != null) {
    		Element element = node.getElement();
    		XElement xElement = wrapElementWithParents(element, dimension);
    		xElement.setParent(dimension);
    		r =new XElementNode(xElement);
    		if(node.getChildren().length == 0) 
    			r.setChildren(new XElementNode[0]);
    		r.setNativeObject(node);
    		r.setParent(parent);
    	}
    	return r;
    }
    
	public static XElementNode[] wrapElementNodes(ElementNode[] nodes, XObject parent, XDimension dimension){
        XElementNode[] r = new XElementNode[nodes.length];
        for (int j = 0; j < nodes.length; j++) {
        	XElementNode node = wrapElementNode(nodes[j], parent, dimension);
        	node.getElement().setParent(dimension);
        	r[j] = node;
        }
        return r;
    }
    
	public static ElementNode findElementNode(String elementId, ElementNode[] rootNodes){
    	ElementNode r = null;
    	List queue = new ArrayList();
    	for (int i = 0; (i < rootNodes.length) && (r == null); i++) {
			ElementNode node = rootNodes[i];
			if(node.getElement().getId().equals(elementId)){
				r = node;
			} else {
				ElementNode[] children = node.getChildren();
				for (int j = 0; j < children.length; j++) {
					ElementNode childNode = children[j];
					queue.add(childNode);
				}
			}
		}
    	if(r == null && !queue.isEmpty()){
    		ElementNode[] nodes = (ElementNode[]) queue.toArray(new ElementNode[0]);
    		r = findElementNode(elementId, nodes);
    	}
    	return r;
    }
    
	public static ElementNode[] getSubsetNativeNodes(XSubset xSubset){
        SubsetHandler handler = getSubetHandler(xSubset);
		ElementNode[] r;
		if(handler != null) {
			r = handler.getVisibleRootNodes();
		}else{
			r = new ElementNode[0];
		}
        return r;
    }

	public static SubsetHandler getSubetHandler(XSubset xSubset) {
		Subset subset = (Subset) xSubset.getNativeObject();
        SubsetHandlerRegistry handlerReg = SubsetHandlerRegistry.getInstance();
        SubsetHandler handler = handlerReg.getHandler(subset);
		return handler;
	}

	public static XElementNode[] getSubsetXElements(XSubset xSubset){
    	XDimension dimension = (XDimension) xSubset.getParent();
    	ElementNode[] nodes = getSubsetNativeNodes(xSubset);
    	XElementNode[] r = wrapElementNodes(nodes, xSubset, dimension);
    	return r;
    }

	public static XView wrapView(CubeView view){
    	XView r = null;
    	if(view != null) {
    		r = new XView(view.getId(), view.getName(), null);
    		r.setNativeObject(view);
    		r.setDescription(view.getDescription());
    	}
    	return r;
    }
    
	public static XView[] getXViews(XCube xcube) {
        Cube cube = (Cube) xcube.getNativeObject();
        String[] viewIds = cube.getCubeViewIds(); 
        XView[] r = new XView[viewIds.length];
        for (int i = 0; i < viewIds.length; i++) {
        	CubeView view = getCubeView(cube, viewIds[i]);
        	if(view != null){
	        	r[i] = wrapView(view);
	            r[i].setParent(xcube);
        	}
        }
        return r;
    }
    
	public static XAxis[] getXAxises(XView xview){
        CubeView view = (CubeView) xview.getNativeObject();
        Axis[] axises = view.getAxes();
        XAxis[] r = new XAxis[axises.length];
        for (int i = 0; i < axises.length; i++) {
            r[i] = wrapAxis(axises[i], xview);
        }
        return r;
    }
    
	private static XElement[] wrapElementArray(Element[] elements, XObject parent){
		XElement[] r = new XElement[elements.length];
		for (int i = 0; i < elements.length; i++) {
			r[i] = wrapElement(elements[i], parent);
		}
		return r;
	}

	private static XElementPath wrapElementPath(ElementPath path, XDimension[] dbDimensions){
		XElementPath r = new XElementPath();
		Dimension[] dims = path.getDimensions();
		for (int i = 0; i < dims.length; i++) {
			XDimension d = (XDimension)XArrays.findById(dbDimensions, dims[i].getId());
			Element[] ePath = path.getPart(dims[i]);
			XElement[] xPath = wrapElementArray(ePath, d);
			r.addPart(d, xPath);
		}
		return r;
	}
	
	private static XElementPath[] wrapElementPaths(ElementPath[] paths, XDimension[] dbDimensions){
		XElementPath[] r = new XElementPath[paths.length];
		for (int i = 0; i < paths.length; i++) {
			r[i] = wrapElementPath(paths[i], dbDimensions);
		}
		return r;
	}
	
	public static void loadAxis(XAxis xAxis){
        final XDatabase db = (XDatabase)xAxis.getParent().getParent().getParent();
        final Axis axis = (Axis) xAxis.getNativeObject();
        final Dimension[] dimensions = axis.getDimensions();
        final int size = dimensions.length;
        final XDimension[] xDimensions = getXDimensions(dimensions, db);
        final XSubset[] xSubsets = new XSubset[size];
        final XElement[] selectedElements = new XElement[size];
        xAxis.setDimensions(xDimensions);
        for (int i = 0; i < size; i++) {
        	Dimension dimension = dimensions[i];
        	XDimension xDimension = xDimensions[i];
			Subset subset = axis.getActiveSubset(dimension);
            XSubset xSubSet = null;
            
			if(subset != null){
            	xSubSet = wrapSubset(subset, xDimension);
            }
            xSubsets[i] = xSubSet;

            Element selectedElement = axis.getSelectedElement(dimension);
            XElement selectedXElement = loadSelectedElement(xAxis, dimension, xDimension, selectedElement);
            selectedElements[i] = selectedXElement;
        }
        XElementPath[] xExpandenPaths = wrapElementPaths(axis.getExpandedPaths(), xDimensions);
        xAxis.setElementPaths(xExpandenPaths);
        xAxis.setSelectedElements(selectedElements);
        xAxis.setSubsets(xSubsets);
    }

	private static XElement loadSelectedElement(XAxis xAxis, Dimension dimension, XDimension xDimension, Element selectedElement) {
		XElement selectedXElement = null;
		//logic: if element exists, it is wrapped and its parents are wrapped and set to the one.
		//Element of XInvalidType will be returned
		if(selectedElement != null){
			XElement dbSelectedXElement = wrapElement(selectedElement, null);
			selectedXElement = dbSelectedXElement;
		    Element[] selPath = selectedElement.getParents();
		    if(selPath != null){
		        for (int j = 0; j < selPath.length; j++) {
		            Element element = selPath[j];
		            XElement xElement = wrapElement(element, null);
		            dbSelectedXElement.setParent(xElement);
		            dbSelectedXElement = xElement; 
		        }
		    }
		    dbSelectedXElement.setParent(xDimension);
		    boolean elementExists = dimension.getElementById(selectedElement.getId()) != null;
		    if(!elementExists) {
		    	dbSelectedXElement.setType(XInvalidType.instance);
		    }
		}else if(xAxis.getName().equals(XAxis.SELECTED)){
			selectedXElement = new XElement(null,null, XInvalidType.instance);
		}
		return selectedXElement;
	}

	public static CubeView getCubeView(Cube cube, String viewId) {
		CubeView r = null;
		try {
			r = cube.getCubeView(viewId);
		} catch (PaloIOException e) {
			Object data = e.getData();
			if (data instanceof CubeView) {
				CubeView view = (CubeView) data;
				r = view;
			}else{
				String msg = "unable to load viewID : " + viewId + " for cube " + cube.getName() + " - " + e;
				logger.warn(msg);
			}
		}
		if(r != null){
			String msg = "loaded view " + r.getName() + " for cube " + cube.getName();
			logger.debug(msg);
		}
		return r;
	}
}
