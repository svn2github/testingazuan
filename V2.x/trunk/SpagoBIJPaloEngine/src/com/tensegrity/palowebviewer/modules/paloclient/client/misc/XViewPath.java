package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tensegrity.palowebviewer.modules.paloclient.client.XAxis;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;

public class XViewPath implements IsSerializable {
	private XPath databasePath;
	private XPath viewPath;
	private XPath cubePath;
	private Map dimensions = new HashMap(); //key: axis XPath, value: list of dimension XPaths
	private Map subsets = new HashMap(); //key: dimension XPath, value: subset XPath
	private Map selectedElements = new HashMap();  //key: dimension XPath, value: element Xpath
	// temporal hack
	private Map expandedPaths = new HashMap(); //key: axis XPath, value: list of XStringizedElementPath
	private String description;
	private String viewName;
	private String viewId;
	
	public XViewPath(){
		
	}
	
	public XViewPath(XView view){
		description = view.getDescription();
		viewName = view.getName();
		viewId = view.getId();
		XDatabase db = (XDatabase)view.getParent().getParent();
		databasePath = db.constructPath();
		cubePath = XHelper.getPathTo(databasePath, view.getParent());
		viewPath = XHelper.getPathTo(cubePath, view);
		XAxis[] axises = view.getAxises();
		for (int i = 0; i < axises.length; i++) {
			XAxis axis = axises[i];
			addAxis(axis);
		}
	}

	private void addAxis(XAxis axis) {
		XPath axisPath = XHelper.getPathTo(viewPath, axis);
		List dimPaths = new ArrayList();
		XDimension[] axisDims = axis.getDimensions();
		XSubset[] axisSubsets = axis.getSubsets();
		XElement[] axisSelectedElements = axis.getSelectedElements();
		XElementPath[] axisExpandedPaths = axis.getElementPaths();
		for (int j = 0; j < axisDims.length; j++) {
			XDimension dimension = axisDims[j];
			XPath dimPath = XHelper.getPathTo(databasePath, dimension);
			dimPaths.add(dimPath);
			
			XSubset subset = axisSubsets[j];
			addSubset(dimPath, subset);
			
			if(axisSelectedElements != null){
				XElement element = axisSelectedElements[j];
				addSelectedElement(dimPath, element);
			}
			
			if(axisExpandedPaths != null){
				addExpanded(axisPath, axisExpandedPaths);
			}
		}
		dimensions.put(axisPath, dimPaths);
		
	}

	private void addExpanded(XPath axisPath, XElementPath[] axisExpandedPaths) {
		List paths = new ArrayList();
		for (int i = 0; i < axisExpandedPaths.length; i++) {
			XElementPath path = axisExpandedPaths[i];
			XStringizedElementPath sPath = XStringizedElementPath.stringize(path);
			//TODO: hack for GWT bug
			paths.add(sPath.toList());
		}
		expandedPaths.put(axisPath, paths);
	}

	private void addSelectedElement(XPath dimPath, XElement element) {
		if(element != null){
			XPath elementPath = XHelper.getPathTo(dimPath, element);
			selectedElements.put(dimPath, elementPath);
		}
	}

	private void addSubset(XPath dimPath, XSubset subset) {
		if(subset != null){
			XPath subsetPath = XHelper.getPathTo(dimPath, subset);
			subsets.put(dimPath, subsetPath);
		}
	}
	
	public XPath getCube(){
		return cubePath;
	};
	
	public XPath getView(){
		return viewPath;
	}
	
	/**
	 * @return list of axis paths
	 */
	public List getAxises(){
		return new ArrayList(dimensions.keySet());
	}
	
	public List getDimensions(XPath axis){
		List r = null;
		for(Iterator it = dimensions.entrySet().iterator(); it.hasNext() && (r == null); ){
			Map.Entry entry = (Map.Entry) it.next();
			if(entry.getKey().equals(axis)){
				r = (List) entry.getValue();
			}
		}
		return r;
	}
	
	public XPath getActiveSubset(XPath dimension){
		return (XPath)subsets.get(dimension);
	}
	
	public XPath getSelectedElemetn(XPath dimension){
		return (XPath)selectedElements.get(dimension);
	}
	
	public List getExpandedPaths(XPath axis){
		//TODO: hack for GWT bug
		List result = null;
		List paths = (List)expandedPaths.get(axis);
		if(paths != null) {
			result = new ArrayList();
			for (Iterator it = paths.iterator(); it.hasNext();) {
				List list = (List) it.next();
				result.add(XStringizedElementPath.fromList(list));
			}
		}
		return result;
	}
	
	public String getDescription() {
		return description;
	}

	public String getViewName() {
		return viewName;
	}

	public String getViewId() {
		return viewId;
	}
	
}
