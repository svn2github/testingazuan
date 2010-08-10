package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XConsolidatedElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementType;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.TypeCastVisitor;

public class ReloadSubTreeCallback extends TypeCastVisitor {
	
	private final PaloServerModel serverModel;
	private XObject object;
	
	private final IInitCallback initCallback = new IInitCallback() {

		public void onSuccess(XObject[] children) {
			reloadSubTrees(children);
		}

	};

	public ReloadSubTreeCallback(PaloServerModel serverModel, XObject object) {
		this.serverModel = serverModel;
		this.object = object;
	}
	
	public void sendQuery() {
		visit(object);
	}

	public void visitAxis(XAxis axis) {
	}

	public void visitConsolidatedElement(XConsolidatedElement consolidatedElement) {
	}

	public void visitCube(XCube cube) {
		XView[] views = cube.getViews();
		if(needReload(views)) {
			serverModel.loadChildren(cube, IXConsts.TYPE_VIEW, initCallback);
		}
		else {
			reloadSubTrees(serverModel.getCachedViews(cube));
		}
	}

	public void visitDatabase(XDatabase database) {
		XDimension[] dimensions = database.getDimensions();
		if(needReload(dimensions))
			serverModel.loadChildren(database, IXConsts.TYPE_DIMENSION, initCallback);
		XCube[] cubes = database.getCubes();
		if(needReload(cubes))
			serverModel.loadChildren(database, IXConsts.TYPE_CUBE, initCallback);
	}

	public void visitDimension(XDimension dimension) {
		if(object.getTypeID() != IXConsts.TYPE_CUBE) {
			XElementNode[] nodes = dimension.getNodes();
			if(needReload(nodes))
				serverModel.loadChildren(dimension, IXConsts.TYPE_ELEMENT_NODE, initCallback);
			XSubset[] subsets = dimension.getSubsets();
			if(needReload(subsets))
				serverModel.loadChildren(dimension, IXConsts.TYPE_SUBSET, initCallback);
		}
	}

	public void visitElement(XElement element) {
	}

	public void visitElementNode(XElementNode node) {
		XElementNode[] children = node.getChildren();
		if(needReload(children) && isConsolidated(node))
			serverModel.loadChildren(node, IXConsts.TYPE_ELEMENT_NODE, initCallback);
	}

	private boolean isConsolidated(XElementNode node) {
		XElement element = node.getElement();
		boolean result = XElementType.isConsolidated(element);
		return result;
	}

	public void visitRoot(XRoot root) {
		XServer[] servers = root.getServers();
		if(needReload(servers))
			serverModel.loadChildren(root, IXConsts.TYPE_SERVER, initCallback);
	}

	public void visitServer(XServer server) {
		XDatabase[] databases = server.getDatabases();
		if(needReload(databases))
			serverModel.loadChildren(server, IXConsts.TYPE_DATABASE, initCallback);
	}

	public void visitSubset(XSubset subset) {
		XElementNode[] nodes = subset.getNodes();
		if(needReload(nodes))
			serverModel.loadChildren(subset, IXConsts.TYPE_ELEMENT_NODE, initCallback);
	}

	public void visitView(XView view) {
		XAxis[] axises = view.getAxises();
		if(needReload(axises))
			serverModel.loadChildren(view, IXConsts.TYPE_AXIS, initCallback);
	}
	
	protected boolean needReload(XObject[] children) {
		return children != null;
	}

	private void reloadSubTrees(XObject[] children) {
		for (int i = 0; i < children.length; i++) {
			new ReloadSubTreeCallback(serverModel, children[i]).sendQuery();
		}
	}

}
