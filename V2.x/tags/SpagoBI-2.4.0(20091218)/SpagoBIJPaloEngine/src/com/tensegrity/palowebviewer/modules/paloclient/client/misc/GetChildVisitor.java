/**
 * 
 */
package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

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

public class GetChildVisitor extends TypeCastVisitor {

    private XObject result;
    private XPathElement pathElement;

    public GetChildVisitor(XPathElement pathElement) {
        setPathElement(pathElement);
    }

    public void setPathElement(XPathElement value) {
        this.pathElement = value;
    }

    public XPathElement getPathElement() {
        return pathElement;
    }

    public XObject getResult() {
        return result;
    }

    protected void setResult(XObject value) {
        result = value;
    }

    public void visitAxis(XAxis axis) {
    }

    public void visitConsolidatedElement(XConsolidatedElement consolidatedElement) {
    }

    public void visitCube(XCube cube) {
        int typeId = pathElement.getTypeId();
    	String id = pathElement.getId();
    	switch(typeId){
    	case IXConsts.TYPE_DIMENSION: {
    		setResult(XArrays.findById(cube.getDimensions(), id));
    		break;
    	}
    	case IXConsts.TYPE_VIEW: {
    		setResult(XArrays.findById(cube.getViews(), id));
    		break;
    	}
    	default: {
    		String message = "Cube have no children of type "+XHelper.typeToString(typeId);
			throw new IllegalStateException(message);
    	}
    	}
    }

    public void visitDatabase(XDatabase database) {
    	int typeId = pathElement.getTypeId();
    	String id = pathElement.getId();
    	switch(typeId){
    	case IXConsts.TYPE_DIMENSION: {
    		setResult(XArrays.findById(database.getDimensions(), id));
    		break;
    	}
    	case IXConsts.TYPE_CUBE: {
    		setResult(XArrays.findById(database.getCubes(), id));
    		break;
    	}
    	default: {
    		String message = "Cube have no children of type "+XHelper.typeToString(typeId);
			throw new IllegalStateException(message);
    	}
    	}
    }

    public void visitDimension(XDimension dimension) {
    	int typeId = pathElement.getTypeId();
    	String id = pathElement.getId();
    	switch(typeId){
    	case IXConsts.TYPE_ELEMENT: {
    		setResult(XArrays.findById(dimension.getNodes(), id));
    		break;
    	}
    	case IXConsts.TYPE_SUBSET: {
    		setResult(XArrays.findById(dimension.getSubsets(), id));
    		break;
    	}
    	default: {
    		String message = "Dimension have no children of type "+XHelper.typeToString(typeId);
			throw new IllegalStateException(message);
    	}
    	}
    }

    public void visitElement(XElement element) {
        // TODO Auto-generated method stub

    }

    public void visitRoot(XRoot root) {
    	XObject server = XArrays.findById(root.getServers(), pathElement.getId());
    	setResult(server);
    }

    public void visitServer(XServer server) {
        XObject database = XArrays.findById(server.getDatabases(), pathElement.getId());
		setResult(database);
    }

    public void visitSubset(XSubset subset) {

    }

    public void visitView(XView view) {
        setResult(XArrays.findById(view.getAxises(), pathElement.getId()));
    }

	public boolean hasFinished() {
		return false;
	}

	public void visitElementNode(XElementNode node) {
		setResult(XArrays.findById(node.getChildren(), pathElement.getId()));
	}

}