package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import java.util.ArrayList;
import java.util.List;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.XRoot;



public class XHelper {
	
	private final static String[] TYPE_NAMES = new String[IXConsts.MAX_TYPE_ID+1];
	static {
        TYPE_NAMES[IXConsts.TYPE_ROOT] = IXConsts.TYPE_NAME_ROOT;
        TYPE_NAMES[IXConsts.TYPE_SERVER] = IXConsts.TYPE_NAME_SERVER;
        TYPE_NAMES[IXConsts.TYPE_DATABASE] = IXConsts.TYPE_NAME_DATABASE;
        TYPE_NAMES[IXConsts.TYPE_CUBE] = IXConsts.TYPE_NAME_CUBE;
        TYPE_NAMES[IXConsts.TYPE_DIMENSION] = IXConsts.TYPE_NAME_DIMENSION;
        TYPE_NAMES[IXConsts.TYPE_ELEMENT] = IXConsts.TYPE_NAME_ELEMENT;
        TYPE_NAMES[IXConsts.TYPE_CONSOLIDATED_ELEMENT] = IXConsts.TYPE_NAME_CONSOLIDATED_ELEMENT;
        TYPE_NAMES[IXConsts.TYPE_VIEW] = IXConsts.TYPE_NAME_VIEW;
        TYPE_NAMES[IXConsts.TYPE_SUBSET] = IXConsts.TYPE_NAME_SUBSET;
        TYPE_NAMES[IXConsts.TYPE_AXIS] = IXConsts.TYPE_NAME_AXIS;
        TYPE_NAMES[IXConsts.TYPE_ELEMENT_NODE] = IXConsts.TYPE_NAME_ELEMENT_NODE;
	}

    public static void visitHierarchy(XObject obj, IXVisitor visitor){
        visitHierarchy(obj, visitor, -1);
    }

    public static void visitHierarchy(XObject obj, IXVisitor visitor, int depth){
    	if(!visitor.hasFinished()) {
    		HierarchyVisitor hierarchyVisitor = new HierarchyVisitor(visitor);
    		hierarchyVisitor.setDepth(depth);
    		hierarchyVisitor.visit(obj);
    	}
    }
    
    public static void setParent(XObject[] children, XObject parent) {
    	if(children != null){
	        for( int i = 0 ; i < children.length ; i++ ) { 
	        	XObject child = children[i];
				if(child!= null)
	        		child.setParent(parent);
	        }
    	}
    }

    public static boolean hierarchyContains(XObject root, XObject checked) {
        ExistanceChecker checker = new ExistanceChecker(checked);
        visitHierarchy(root, checker);
        return checker.wasFound();
    }

    public static String typeToString(int type) {
    	String result = "";
    	if(0 <= type && type < TYPE_NAMES.length){
    		result = TYPE_NAMES[type];
    	}
    	else {
    		result = "unknown("+type+")";
    	}
    	return result;
    }
    
    public static int typeNameToId(String typeName) {
    	int result = -1;
		for (int i = 0; i < TYPE_NAMES.length; i++) {
			if(typeName.equals(TYPE_NAMES[i])) {
				result = i;
				break;
			}
		}
		return result;
    }

    public static boolean isInPath(XObject[] path, XObject object) {
        boolean result = false;
        for (int i = 0; (i < path.length) && !result; i++) {
            result = path[i] == object;
        }
        return result;
    }

    public static boolean equalsById(XObject obj1, XObject obj2) {
        boolean result = true;
        if(obj1 == null)
            result = obj2 == null;
        else if(obj2 == null)
            result = false;
        else {
            result = obj1.getTypeID() == obj2.getTypeID();
            result &= obj1.getId().equals(obj2.getId());
        }
        return result;

    }

    public static XObject findInHierarchyById(XObject[] objects, String id){
        XObject r = XArrays.findById(objects, id);
        if(r == null){
            for (int i = 0; (i < objects.length) && (r == null); i++) {
                XObject root = objects[i];
                IdFinder finder = new IdFinder(id);
                visitHierarchy(root, finder);
                r = finder.getResult();
            }
        }
        return r;
    }
    
    public static XObject findParentByType(XObject object, int type){
    	XObject parent = object.getParent();
    	return findBackByType(parent, type);
    }
    
    public static XObject findBackByType(XObject object, int type){
    	while(object != null && object.getTypeID() != type){
    		object = object.getParent();
    	}
    	return object;
    }

    public static XObject getDenotedObject(XRoot root, XPath path) {
        if(root == null)
            throw new IllegalStateException("Root can not be null.");
        if(path == null)
            throw new IllegalStateException("Path can not be null.");
        XObject result = root; 
        XPathElement[] pathArray = path.getPath();
        for( int i = 1 ; i < pathArray.length ; i++ ) { 
            result = getChild(result, pathArray[i]);
        } 
        return result;
    }

    public static XRelativePath getPathTo(XPath basePath, XObject object){
        List elementPath = new ArrayList();
        //elementPath.add(object);
        XPathElement lastBasePath = basePath.getLastComponent();
        XPathElement[] strPath = object.constructPath().getPath();
        XObject[] objPath = object.getPathArray();
        int idx = strPath.length-1;
        while(!lastBasePath.equals(strPath[idx]) && (idx >= 0)){
            elementPath.add(0,objPath[idx--]);
        }
        XObject[] pathArray = new XObject[elementPath.size()];
        for (int i = 0; i < pathArray.length; i++) {
            pathArray[i] = (XObject) elementPath.get(i);
        }
        XRelativePath xElementPath = new XRelativePath(basePath, pathArray);
        return xElementPath;
    }

    private static XObject getChild(XObject parent, XPathElement pathElement) {
        if(parent == null)
            throw new IllegalArgumentException("Parent can not be null");
        if(pathElement == null)
            throw new IllegalArgumentException("Path element can not be null");

        GetChildVisitor visitor = new GetChildVisitor(pathElement);
        visitor.visit(parent);
        XObject result = visitor.getResult();
        if(result == null) 
            throw new RuntimeException("can't construct element " + pathElement + " for parent " + parent);
        return result;
    }

}
