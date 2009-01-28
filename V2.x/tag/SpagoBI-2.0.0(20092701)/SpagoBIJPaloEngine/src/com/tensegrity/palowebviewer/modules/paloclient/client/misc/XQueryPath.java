package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;

/**
 *
 * This class contains data query  to some cube via XPath
 * To get data you should.
 * 1. Create instance of XQueryPath via constructor XQueryPath(XCube cube)
 * 2. Add coordinates. Each cube dimension should have at least one coordinate (element), otherwise some exception will be thrownd duging data querying.
 * 3. Returned data is wrapped in XResult. Create instance of {@link com.tensegrity.palowebviewer.modules.paloclient.client.misc.XResultIterator}, using XResult and XQuery
 * 4. For each IXPoint returned by XResultIterator invoke getElementPath() to determine element 
 *   
 */
public class XQueryPath implements IHaveCoordinates, IsSerializable{
	private XPath cubePath;
	private XPath databasePath;
	//dimension coordinates; key : relative path to cube, value : list of element relative path to dimension 
	private Map dimensionMap = new HashMap();
	//to keep order of adding dimensions;
	private List dimensions = new ArrayList();
	private transient final Map requestedDimMap = new HashMap();
	
	/**
	 * Default constructor is needed to (de)serialization. Do not use it directry. 
	 */
	public XQueryPath(){
	}
	
	/**
	 * @param cube - interesting cube, to wich you are querying
	 */
	public XQueryPath(XCube cube){
		XDatabase database = (XDatabase) cube.getParent();
		databasePath = database.constructPath();
		cubePath = new XRelativePath(databasePath, cube);
	}
	
	public void addCoordinate(XDimension dimension, XElement point){
		XRelativePath dimensionPath = findRelativePath(dimension);
		if(dimensionPath == null){
			dimensionPath = new XRelativePath(databasePath, dimension);
			dimensionMap.put(dimensionPath, new ArrayList());
			requestedDimMap.put(dimensionPath, new ArrayList());
			dimensions.add(dimensionPath);
		}
		XRelativePath xElementPath = XHelper.getPathTo(dimensionPath, point);
		List elements = (List)dimensionMap.get(dimensionPath);
		List requestedElements = (List)requestedDimMap.get(dimensionPath);
		if(!elements.contains(xElementPath))
			elements.add(xElementPath);
		requestedElements.add(xElementPath);
	}

	private XRelativePath findRelativePath(XDimension dimension) {
		XRelativePath dimensionPath = null;
		for (Iterator it = dimensions.iterator(); it.hasNext();) {
			XRelativePath path = (XRelativePath) it.next();
			XPathElement pathElement = path.getPath()[path.getPath().length-1];
			String id = pathElement.getId();
			if(id.equals(dimension.getId())){
				dimensionPath = path;
				break;
			}
		}
		return dimensionPath;
	}
	
	public XPath getCubePath(){
		return cubePath;
	}
	
	/** 
	 * @return dimensions Xpath list in order they added
	 */
	public List getDimensions(){
		return dimensions;
	}
	
	/**
	 * @param dimensionPath to dimension
	 * @return list of paths to elements
	 */
	public List getPoinstPath(XPath dimensionPath){
		return (List)dimensionMap.get(dimensionPath);
	}
	
	public List getRequestedElements(XPath dimensionPath) {
		return (List)requestedDimMap.get(dimensionPath);
	}

	public String toString() {
		String r = "XQueryPath for " + cubePath + " : \n";
		for (Iterator it = dimensions.iterator(); it.hasNext();) {
			XPath dimensionPath = (XPath) it.next();
			r += "  " + dimensionPath + " :\n";
			List points = getPoinstPath(dimensionPath);
			for (Iterator it2 = points.iterator(); it2.hasNext();) {
				XPath pointPath = (XPath) it2.next();
				r += "    " + pointPath + "\n";
			}
			
		}
		return r;
	}
	
}
