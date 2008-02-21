	package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.paloclient.client.IElementType;
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
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;
import com.tensegrity.palowebviewer.modules.util.client.Arrays;

public class XObjectReplacer extends TypeCastVisitor
{
	private final static XElementNode[] EMPTY_NODE_CHILDREN = new XElementNode[0];

    private boolean hasChanged;
    private XObject oldObject;
    private XObject newObject;
	private final List nameChangeList = new ArrayList();

    public boolean hasChanged() {
        return hasChanged;
    }
    
    public XObject[] getRenamedObjects() {
    	return XArrays.toArray(nameChangeList);
    }

    public void replace(XObject[] newObjects, XObject[] oldObjects) {
    	reset();
        int size = newObjects.length;
        if(oldObjects != null) {
        	hasChanged = oldObjects.length != size;
        	for( int i = 0 ; i < size  ; i++ ) { 
        		XObject newObject = newObjects[i];
        		XObject old = XArrays.findById(oldObjects, newObject.getId());
        		if(old != null) {
        			newObjects[i] = replace(newObject, old);
        		}
        		else
        			hasChanged = true;
        	} 
        }
        else {
        	hasChanged = true;
        }
    }

    private void checkNameChanged(XObject newObject, XObject old) {
    	if(!newObject.getName().equals(old.getName())) {
    		old.setName(newObject.getName());
    		nameChangeList.add(old);
    	}
	}

	protected XObject replace(XObject newObject, XObject oldObject) {
		this.newObject = newObject;
    	if(oldObject != newObject) {
    		this.oldObject = oldObject;
        	visit(this.oldObject);
        	boolean objectChanged = (newObject == this.newObject);
        	if(!objectChanged)
        		checkNameChanged(newObject, oldObject);
			hasChanged |= objectChanged;
    	}
        return this.newObject;
    }

    public void visitAxis(XAxis axis) {
    	if(Arrays.equals(newObject, oldObject)) {
    		newObject = oldObject;
    	}
    }

    public void visitConsolidatedElement(XConsolidatedElement consolidatedElement) {
        visitElement(consolidatedElement);
    }

    public void visitElement(XElement element) {
        XElement newElement = (XElement)newObject;
        if(newElement.getType().equals(element.getType()))
            newObject = oldObject;
    }

    public void visitCube(XCube cube) {
        newObject = oldObject;
    }

    public void visitDatabase(XDatabase database) {
        newObject = oldObject;
    }

    public void visitDimension(XDimension dimension) {
        newObject = oldObject;
    }

    public void visitRoot(XRoot root) {
        newObject = oldObject;
    }

    public void visitServer(XServer server) {
        newObject = oldObject;
    }

    public void visitSubset(XSubset subset) {
        newObject = oldObject;
    }

    public void visitView(XView view) {
        newObject = oldObject;
    }
    
    public void reset() {
    	hasChanged = false;
    	nameChangeList .clear();
    }

	public void visitElementNode(XElementNode node) {
		XElementNode newElementNode = (XElementNode) newObject;
		XElement newElement = newElementNode.getElement();
		XElement oldElement = node.getElement();
		IElementType newType = newElement.getType();
		if(!oldElement.getType().equals(newType)) {
			oldElement.setType(newType);
			hasChanged = true;
			if(!XElementType.isConsolidated(oldElement)){
				
				node.setChildren(EMPTY_NODE_CHILDREN);
			}

		}
		newObject = oldObject;
		
	}


}
