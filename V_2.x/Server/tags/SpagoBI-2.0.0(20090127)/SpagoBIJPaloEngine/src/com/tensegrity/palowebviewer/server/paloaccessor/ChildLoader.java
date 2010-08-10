package com.tensegrity.palowebviewer.server.paloaccessor;

import org.palo.api.Connection;
import org.palo.api.Cube;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.ElementNode;

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

class ChildLoader extends TypeCastVisitor implements IXConsts{
	private Connection connection;
	private int type;
	private XObject[] children;
	
	public ChildLoader(int type, Connection connection) {
		this.connection = connection;
		this.type = type;
	}

	public XObject[] getChildren() {
		return children;
	}

	public void visitAxis(XAxis axis) {
		//axis will be fully loaded as view child
	}

	public void visitConsolidatedElement(XConsolidatedElement consolidatedElement) {
		throw new RuntimeException("there is no children in consolidatedElement");
	}

	public void visitCube(XCube xcube) {
        Cube cube = (Cube) xcube.getNativeObject();
		if(type == TYPE_DIMENSION){
            XDatabase db = (XDatabase) xcube.getParent();
            XDimension[] dimensions = PaloHelper.getXDimensions(cube.getDimensions(), db); 
            xcube.setDimensions(dimensions);
            children = dimensions;
		}else if(type == TYPE_VIEW){
            XView[] views = PaloHelper.getXViews(xcube);
            xcube.setViews(views);
            children = views;
		}
	}

	public void visitDatabase(XDatabase database) {
        Database db = (Database) database.getNativeObject();

		if(type == TYPE_DIMENSION){
            XDimension[] dimensions = PaloHelper.getXDimensions(db.getDimensions(), database);
			database.setDimensions(dimensions);
			children = dimensions;
		}else if(type == TYPE_CUBE){
            Cube[] cubes = db.getCubes();
            XCube[] xcubes = PaloHelper.getXCubes(cubes, database);
			database.setCubes(xcubes);
            children = xcubes;
		}
	}

	public void visitDimension(XDimension xDimension) {
        Dimension dimension  = (Dimension) xDimension.getNativeObject();
		if (type == TYPE_ELEMENT_NODE) {
			ElementNode[] nodes = dimension.getElementsTree();
			XElementNode[] xNodes = PaloHelper.wrapElementNodes(nodes, xDimension, xDimension);
            xDimension.setNodes(xNodes);
			children = xNodes;
		}else if (type == TYPE_SUBSET) {
            XSubset[] subSets = PaloHelper.getXSubsets(xDimension);
            xDimension.setSubsets(subSets);
            children = subSets;
		}
	}

	public void visitElement(XElement element) {
		// element has no child
	}

	public void visitRoot(XRoot root) {
		// root is initialized externally.
	}

	public void visitServer(XServer server) {
		XDatabase[] databases = PaloHelper.getXDatabases(connection, server);
		server.setDatabases(databases);
		children = databases;
	}

	public void visitSubset(XSubset subset) {
		// load subset and it's root elements
		XElementNode[] nodes = PaloHelper.getSubsetXElements(subset);
		subset.setNodes(nodes);
		children = nodes;
	}

	public void visitView(XView view) {
		XAxis[] axises = PaloHelper.getXAxises(view);
		for (int i = 0; i < axises.length; i++) {
			XAxis axis = axises[i];
			PaloHelper.loadAxis(axis);
		}
		view.setAxises(axises);
		children = axises;
	}

	public void visitElementNode(XElementNode node) {
		XObject parent = node.getParent();
		while(! (parent instanceof XDimension)){
			parent = parent.getParent();
		}
		ElementNode nativeNode = (ElementNode) node.getNativeObject();
		ElementNode[] nodes = nativeNode.getChildren();
		XElementNode[] xNodes = PaloHelper.wrapElementNodes(nodes, node, (XDimension)parent);
		node.setChildren(xNodes);
		children = xNodes;
	}
	
}
