/**
 * 
 */
package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XConsolidatedElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.TypeCastVisitor;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;

class ObjectUpdater extends TypeCastVisitor
{

    private final XObjectReplacer replacer = new XObjectReplacer();
    private XObject updated;
    private XObject[] children;
    private XObject[] oldChildren;
    private int type;
    private boolean childrenChanged;
    private DatabaseDimensionCache dbDimCache;
    private DimensionElementCache dimElementCache;
    private DimensionSubsetCache dimSubsetCache;
    private CubeViewCache cubeViewCache;
    private final ObjectUpdaterListenerCollection listeners = new ObjectUpdaterListenerCollection();
    
    public ObjectUpdater() {
    }
    
    public void addListener (IObjectUpdaterListener listener) {
    	 listeners.addListener(listener);
    }
    
    public void removeListener (IObjectUpdaterListener listener) {
    	listeners.removeListener(listener);
    }

    public void updateChildren(XObject[] path, XObject[] children, int type) {
    	this.children = children;
    	this.updated = path[path.length -1];
    	this.type = type;
    	visit(updated);
    }

    public XObject getUpdatedObject() {
        return updated;
    }

    public void setUpdatedObject(XObject object) {
        if(object == null)
            throw new IllegalArgumentException("Object can not be null");
        this.updated = object;
    }

    public void visit(XObject object) {
    	replacer.reset();
    	childrenChanged = false;
        super.visit(object);
        if(replacer.hasChanged()|| childrenChanged){
        	fireChildrenArrayChanged(oldChildren, type);
        }
        XObject[] renamedObjects = replacer.getRenamedObjects();
        fireObjectRenamed(renamedObjects);
    }

    protected void updateWith(XObject object) {
        final XObject updtedObject = getUpdatedObject();
        final XObject parent = updtedObject.getParent();
        updtedObject.set(object);
        updtedObject.setParent(parent);
    }

    public void visitRoot(XRoot root) {
        oldChildren = root.getServers();
        replacer.replace(children, oldChildren);
        XServer[] newServers = new XServer[children.length];
        for (int i = 0; i < newServers.length; i++) {
			newServers[i] = (XServer)children[i];
		}
        root.setServers(newServers);
    }

    public void visitServer(XServer server){
        oldChildren = server.getDatabases();
        replacer.replace(children, oldChildren);
        XDatabase[] newDatabases = new XDatabase[children.length];
        for (int i = 0; i < newDatabases.length; i++) {
        	newDatabases[i] = (XDatabase)children[i];
		}
        server.setDatabases(newDatabases);
    }

    public void visitDatabase(XDatabase database){
    	switch(type) {
    	case IXConsts.TYPE_CUBE: {
    		updateCubes(database);
           	break;
    	}
    	case IXConsts.TYPE_DIMENSION: {
    		updateDimensions(database);
    		break;
    	}
    	}
    }

	protected void updateCubes(XDatabase database) {
		oldChildren = database.getCubes();
		replacer.replace(children, oldChildren);
		XCube[] newCubes = new XCube[children.length];
		for (int i = 0; i < newCubes.length; i++) {
			newCubes[i] = (XCube)children[i];
		}
		database.setCubes(newCubes);
	}

	protected void updateDimensions(XDatabase database) {
		oldChildren = database.getDimensions();
		replacer.replace(children,oldChildren);
		XDimension[] newDimensions = new XDimension[children.length];
		for (int i = 0; i < newDimensions.length; i++) {
			XDimension dim = (XDimension)children[i];
			newDimensions[i] = dbDimCache.getDimension(database, dim);
		}
		database.setDimensions(newDimensions);
	}

    public void visitDimension(XDimension dimension){
    	switch(type){
    	case IXConsts.TYPE_ELEMENT_NODE:{
            updateElements(dimension);
            break;
    	}
    	case IXConsts.TYPE_SUBSET: {
            updateSubsets(dimension);
            break;
    	}
    	}
    }

	private void updateSubsets(XDimension dimension) {
		oldChildren = dimension.getSubsets();
		replacer.replace(children, oldChildren);
		XSubset[] newSubsets = new XSubset[children.length];
		for (int i = 0; i < newSubsets.length; i++) {
			newSubsets[i] = (XSubset)children[i];
		}
		dimension.setSubsets(newSubsets);
	}

	private void updateElements(XDimension dimension) {
		oldChildren = dimension.getNodes();
		replacer.replace(children, oldChildren);
		XElementNode[] newNodes = new XElementNode[children.length];
		for (int i = 0; i < newNodes.length; i++) {
			XElementNode node = (XElementNode)children[i];
			XElement element = node.getElement();
			element = dimElementCache.getElement(dimension, element);
			node.setElement(element);
			newNodes[i] = node;
		}
		dimension.setNodes(newNodes);
	}

    public void visitConsolidatedElement(XConsolidatedElement consolidatedElement){
    	// no children
    }

	private XDimension findParentDimension(XObject object) {
		return (XDimension)XHelper.findParentByType(object, IXConsts.TYPE_DIMENSION);
	}

    public void visitCube(XCube cube){
    	switch(type) {
    	case IXConsts.TYPE_VIEW: {
            updateViews(cube);
    		break;
    	}
    	case IXConsts.TYPE_DIMENSION: {
            updateDimensions(cube);
            break;
    	}
    	}
    }

	private void updateViews(XCube cube) {
		oldChildren = cube.getViews();
		replacer.replace(children, oldChildren);
		XView[] newViews = new XView[children.length];
		for (int i = 0; i < newViews.length; i++) {
			newViews[i] = (XView)children[i];
		}
		newViews = cubeViewCache.getViews(cube, newViews);
		cube.setViews(newViews);
	}

	private void updateDimensions(XCube cube) {
		oldChildren = cube.getDimensions();
		childrenChanged = !XArrays.equalsByName(oldChildren, children);
		replacer.replace(children, oldChildren);
		if(childrenChanged) {
			XDatabase database = findDatabase(cube);
			XDimension[] newDimensions = new XDimension[children.length];
			for (int i = 0; i < newDimensions.length; i++) {
				XDimension dim = (XDimension)children[i];
				newDimensions[i] = dbDimCache.getDimension(database, dim);
			}
			cube.setDimensions(newDimensions);
		}
	}

    public void visitSubset(XSubset subset){
    	oldChildren = subset.getNodes();
        replacer.replace(children, oldChildren);
        XElementNode[] newElementNodes = new XElementNode[children.length];
        XDimension dimension = findParentDimension(subset);
        for (int i = 0; i < newElementNodes.length; i++) {
        	XElementNode elementNode = (XElementNode)children[i];
			newElementNodes[i] = elementNode;
        	XElement element = elementNode.getElement();
			element = dimElementCache.getElement(dimension, element);
			elementNode.setElement(element);
        }
        subset.setNodes(newElementNodes);
    }

    public void visitView(XView view){
        oldChildren = view.getAxises();
        replacer.replace(children, oldChildren);
        XAxis[] newAxes = new XAxis[children.length];
    	for (int i = 0; i < newAxes.length; i++) {
    		newAxes[i] = (XAxis)children[i];
    	}
    	
        view.setAxises(newAxes);
    	for (int i = 0; i < newAxes.length; i++) {
    		visitAxis(newAxes[i]);
    	}
    }
    
    protected XDatabase findDatabase(XObject object) {
    	return (XDatabase)XHelper.findParentByType(object, IXConsts.TYPE_DATABASE);
    }

    protected boolean updateDimensions(XAxis axis, XAxis updated) {
        XDimension[] oldDimensions = updated.getDimensions();
        XDimension[] newDimensions = axis.getDimensions();
        boolean result = !XArrays.equalsByName(oldDimensions, newDimensions);
        XDimension[] dbDimensions = findDatabase(updated).getDimensions();
        subWithOld(dbDimensions, newDimensions);
        axis.setDimensions(newDimensions);
        return result;
    }

    protected boolean updateSubsets(XAxis axis, XAxis updated) {
        XSubset[] oldSubsets = updated.getSubsets();
        XSubset[] newSubsets = axis.getSubsets();
        XDimension[] newDimensions = axis.getDimensions();
        boolean result = !XArrays.equalsByName(oldSubsets, newSubsets);
        for (int i = 0; i < newSubsets.length; i++) {
            if(newSubsets[i] != null)
                newSubsets[i] = dimSubsetCache.getSubset(newDimensions[i], newSubsets[i]);
        }
        axis.setSubsets(newSubsets);
        return result;
    }

    protected boolean updateSelectedElements(XAxis axis, XAxis updated) {
        XElement[] oldSelectedElements = updated.getSelectedElements();
        final XElement[] newSelectedElements = axis.getSelectedElements();
        XDimension[] newDimensions = axis.getDimensions();
        boolean result = !XArrays.equalsByName(oldSelectedElements, newSelectedElements);
        if(newSelectedElements != null) {
        	for (int i = 0; i < newDimensions.length; i++) {
        		XElement element = newSelectedElements[i];
        		if(element != null) {
        			newSelectedElements[i] = dimElementCache.getElement(newDimensions[i], element);
        		}
			}
        }
        axis.setSelectedElements(newSelectedElements);
        return result;
    }

    public void visitAxis(XAxis axis){
    	XDatabase db = (XDatabase )XHelper.findParentByType(axis, IXConsts.TYPE_DATABASE);
    	XDimension[] dimensions = axis.getDimensions();
    	dimensions = dbDimCache.getDimensions(db, dimensions);
    	axis.setDimensions(dimensions);
    	XElement[] selected = getCachedSelectedElements(axis, dimensions);
    	axis.setSelectedElements(selected);
    	
    	XElementPath[] expansions = axis.getElementPaths();
    	expansions = adoptExpansions(expansions, db);
    	axis.setElementPaths(expansions);

    	XSubset[] subsets = getCachedSubsets(axis, dimensions);
    	axis.setSubsets(subsets);
    }

	private XElement[] getCachedSelectedElements(XAxis axis, XDimension[] dimensions) {
		XElement[] selected = axis.getSelectedElements();
    	for (int i = 0; i < selected.length; i++) {
    		if(selected[i] != null)
    			selected[i] = dimElementCache.getElement(dimensions[i], selected[i]);
		}
		return selected;
	}

	private XSubset[] getCachedSubsets(XAxis axis, XDimension[] dimensions) {
		XSubset[] subsets = axis.getSubsets();
    	for (int i = 0; i < subsets.length; i++) {
    		if(subsets[i] != null) {
    			subsets[i]= dimSubsetCache.getSubset(dimensions[i], subsets[i]);
    		}
		}
		return subsets;
	}
    
    private XElementPath adoptElementPath(XElementPath path, XDatabase db){
    	XElementPath r = new XElementPath();
    	XDimension[] dims = path.getDimensions();
    	for (int i = 0; i < dims.length; i++) {
			XDimension dimension = dims[i];
			XDimension adoptedDim = dbDimCache.getDimension(db, dimension);
			XElement[] elements = path.getPart(dimension);
			XElement[] adoptedElements = dimElementCache.getElements(adoptedDim, elements);
			r.addPart(adoptedDim, adoptedElements);
		}
    	return r;
    }

    private XElementPath[] adoptExpansions(XElementPath[] expansions, XDatabase db) {
    	XElementPath[] r = new XElementPath[expansions.length];
    	for (int i = 0; i < expansions.length; i++) {
			XElementPath path = expansions[i];
			r[i] = adoptElementPath(path, db);
		}
		return r;
	}

	public void visitElement(XElement element) {
    }

    protected void setParent(XObject parent, XObject[] children) {
        if(children != null) 
            for( int i = 0 ; i < children.length ; i++ ) { 
                children[i].setParent(parent);
            } 
    }

    protected void subWithOld(XObject[] oldArray, XObject[] newArray) {
        if(oldArray == null)
            return;
        for( int i = 0 ; i < newArray.length ; i++ ) {
        	XObject newObject = newArray[i];
            XObject old = XArrays.findById(oldArray, newArray[i].getId());
            if(old != null && old.getTypeID()== newObject.getTypeID())
                newArray[i] = old;
        } 
    }

    protected void fireChildrenArrayChanged(XObject[] children, int type) {
       listeners.childrenArrayChanged(getUpdatedObject().getPathArray(), children, type);
    }
    
    protected void fireObjectRenamed(XObject[] objects) {
    	for (int i = 0; i < objects.length; i++) {
    		listeners.objectRenamed(objects[i]);	
		}
    }

	public void visitElementNode(XElementNode node) {
        oldChildren = node.getChildren();
        replacer.replace(children, oldChildren);
        XDimension dimension = (XDimension)XHelper.findParentByType(node, IXConsts.TYPE_DIMENSION);
        XElementNode[] newElementNodes = new XElementNode[children.length];
		for (int i = 0; i < newElementNodes.length; i++) {
			
			XElementNode elementNode = (XElementNode)children[i];
			XElement element = elementNode.getElement();
			element = dimElementCache.getElement(dimension, element);
			elementNode.setElement(element);
			newElementNodes[i] = elementNode;
		}
        node.setChildren(newElementNodes);
	}

	void setDbDimCache(DatabaseDimensionCache dbDimCache) {
		this.dbDimCache = dbDimCache;
	}

	void setDimElementCache(DimensionElementCache dimElementCache) {
		this.dimElementCache = dimElementCache;
	}

	void setDimSubsetCache(DimensionSubsetCache dimSubsetCache) {
		this.dimSubsetCache = dimSubsetCache;
	}
	
	void setCubeViewCache(CubeViewCache cubeViewCache) {
		this.cubeViewCache = cubeViewCache;
	}

}