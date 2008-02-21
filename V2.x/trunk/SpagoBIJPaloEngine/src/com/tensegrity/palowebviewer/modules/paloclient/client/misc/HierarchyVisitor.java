/**
 * 
 */
package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

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

public class HierarchyVisitor extends TypeCastVisitor{

    private final IXVisitor visitor;
    private int depth = -1;

    public void setDepth(int value) {
        this.depth = value;
    }

    private void visit(XObject[] xObjs){
        if(xObjs != null)
            for( int i = 0 ; i < xObjs.length ; i++ ) {
                if(xObjs[i] != null)
                    visit(xObjs[i]);
            } 
    }

    public void visit(XObject object) {
        if(depth !=0 && !visitor.hasFinished()) {
            visitor.visit(object);
            depth--;
            super.visit(object);
            depth++;
        }
    }

    public HierarchyVisitor(IXVisitor visitor) {
        this.visitor = visitor;
    }

    public void visitRoot(XRoot root){
        visit(root.getServers());
    }

    public void visitServer(XServer server) {
        visit(server.getDatabases());
    }

    public void visitDatabase(XDatabase database) {
        visit(database.getCubes());
        visit(database.getDimensions());
    }

    public void visitDimension(XDimension dimension) {
        visit(dimension.getNodes());
        visit(dimension.getSubsets());
    }

    public void visitConsolidatedElement(XConsolidatedElement consolidatedElement) {

    }

    public void visitCube(XCube cube) {
        visit(cube.getDimensions());
        visit(cube.getViews());
    }

    public void visitSubset(XSubset subset) {
        visit(subset.getNodes());
    }

    public void visitView(XView view) {
        visit(view.getAxises());
    }

    public void visitAxis(XAxis axis) {
    	/*
        visit(axis.getDimensions());
        visit(axis.getSubsets());
        */
    }

    public void visitElement(XElement element) {
        //no children
    }

	public boolean hasFinished() {
		return visitor.hasFinished();
	}

	public void visitElementNode(XElementNode node) {
		visit(node.getChildren());
	}

}