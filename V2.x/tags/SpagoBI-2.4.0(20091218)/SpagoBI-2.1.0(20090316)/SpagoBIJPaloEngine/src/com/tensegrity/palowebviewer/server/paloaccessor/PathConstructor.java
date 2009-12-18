package com.tensegrity.palowebviewer.server.paloaccessor;

import org.palo.api.Axis;
import org.palo.api.Connection;
import org.palo.api.Cube;
import org.palo.api.CubeView;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.api.Subset;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XConsolidatedElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.TypeCastVisitor;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPathElement;

class PathConstructor extends TypeCastVisitor {

    private XPath path;
    private Connection connection;
    private XPathElement pathElement;
    private XObject object;
    private final XObjectPathCache cache;
    
    public PathConstructor(XObjectPathCache cache){
    	this.cache = cache;
    }

    public void setXPath(XPath path) {
        this.path = path;
    }

    public XPath getXPath() {
        return path;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection () {
        return connection;
    }

    public XObject getLastObject() throws InvalidObjectPathException{
        XPathElement[] namedPath = path.getPath();
        if(namedPath.length>0) {
            object = new XRoot();
            if(namedPath[0].getTypeId() != IXConsts.TYPE_ROOT)
                throw new InvalidObjectPathException("The first object was not XRoot("+namedPath[0]+")", path);
            for (int i = 1; i < namedPath.length; i++) {
                pathElement = namedPath[i];
                XPathElement[] splittedPath = new XPathElement[i+1];
                System.arraycopy(namedPath, 0, splittedPath, 0, i+1);
                XPath partialPath = new XPath(splittedPath);
                XObject cached =  cache.getCachedObject(partialPath);
                if(cached == null){
                	nextObject();
                	cache.put(partialPath, object);
                }
                else {
                	object = cached;
                }
            }
        }else{
            object = null;
        }
        return object;
    }

    protected void nextObject() throws InvalidObjectPathException {
        XObject parent = object;
        visit(parent);
        if((object == null) || (object == parent)) 
            throw new InvalidObjectPathException("can't construct element " + pathElement + " for parent " + parent+ 
            		". Path = " + path, path);
        if(object.getParent() == null)
            object.setParent(parent);
    }

    public void visitAxis(XAxis axis) {
    }

    public void visitConsolidatedElement(XConsolidatedElement consolidatedElement) {
        visitElement(consolidatedElement);
    }

    public void visitElement(XElement xElement){
        Element element = (Element) xElement.getNativeObject();
        Dimension dimension = element.getDimension();
		Element nativeElement = dimension.getElementById(pathElement.getId());
        object = PaloHelper.wrapElement(nativeElement, xElement);
    }

    public void visitCube(XCube xCube) {
        Cube cube = (Cube) xCube.getNativeObject();
        int type = pathElement.getTypeId();
        String id = pathElement.getId();
        switch(type){
        case IXConsts.TYPE_DIMENSION: {
        	Dimension dimension = cube.getDatabase().getDimensionById(id);
            object = PaloHelper.wrapDimension(dimension);
        	break;
        }
        case IXConsts.TYPE_VIEW: {
        	CubeView view = PaloHelper.getCubeView(cube, id);
            object= PaloHelper.wrapView(view);
        	break;
        }
        default: {
        	throw new IllegalStateException("Cube have no children of type "+ XHelper.typeToString(type));
        }
        }
    }

    public void visitDatabase(XDatabase database) {
        Database db = (Database) database.getNativeObject();
        int type = pathElement.getTypeId();
        String id = pathElement.getId();
        switch(type){
        case IXConsts.TYPE_DIMENSION: {
        	Dimension dimension = db.getDimensionById(id); 
        	object = PaloHelper.wrapDimension(dimension);
        	break;
        }
        case IXConsts.TYPE_CUBE: {
        	Cube cube = db.getCubeById(id);
            object = PaloHelper.wrapCube(cube);
        	break;
        }
        default: {
        	throw new IllegalStateException("Database have no children of type "+ XHelper.typeToString(type));
        }
        }
    }

    public void visitDimension(XDimension xDimension) {
        Dimension dimension = (Dimension) xDimension.getNativeObject();
        int type = pathElement.getTypeId();
        String id = pathElement.getId();
        switch(type){
        case IXConsts.TYPE_ELEMENT_NODE: {
        	ElementNode[] nodes = dimension.getElementsTree();
        	for (int i = 0; i < nodes.length; i++) {
				ElementNode node = nodes[i];
				if(node.getElement().getId().equals(id)){
					object = PaloHelper.wrapElementNode(node, xDimension, xDimension);
					break;
				}
			}
        	break;
        }
        case IXConsts.TYPE_SUBSET: {
        	Subset subset = dimension.getSubset(id);  
            object = PaloHelper.wrapSubset(subset, xDimension);
        	break;
        }
        case IXConsts.TYPE_CONSOLIDATED_ELEMENT:
        case IXConsts.TYPE_ELEMENT: {
        	Element element = dimension.getElementById(id);
        	object = PaloHelper.wrapElement(element, xDimension);
        	break;
        }
        default: {
        	throw new IllegalStateException("Dimension have no children of type "+ XHelper.typeToString(type)+ "("+pathElement+")");
        }
        }
    }

    public void visitRoot(XRoot root) {
        object = PaloHelper.wrapServer(connection);
    }

    public void visitServer(XServer server) {
    	String id = pathElement.getId();
		Database database = connection.getDatabaseById(id);
        object = PaloHelper.wrapDatabase(database);
    }

    public void visitSubset(XSubset subset) {
    	ElementNode[] nodes = PaloHelper.getSubsetNativeNodes(subset);
    	String id = pathElement.getId();
		ElementNode node = PaloHelper.findElementNode(id, nodes);
    	XDimension dimension = (XDimension) subset.getParent();
    	object = PaloHelper.wrapElementNode(node, subset, dimension);
    }

    public void visitView(XView xView) {
        CubeView cubeView = (CubeView) xView.getNativeObject();
        Axis axis = PaloHelper.findAxisById(cubeView, pathElement.getId()); 
        object = PaloHelper.wrapAxis(axis, xView);
    }

	public void visitElementNode(XElementNode xNode) {
		ElementNode node = (ElementNode) xNode.getNativeObject();
		String id = pathElement.getId();
		ElementNode childNode = PaloHelper.findElementNode(id, node.getChildren());
		XDimension dimension = (XDimension) XHelper.findParentByType(xNode, TYPE_DIMENSION);
		object = PaloHelper.wrapElementNode(childNode, xNode, dimension);
	}

};
