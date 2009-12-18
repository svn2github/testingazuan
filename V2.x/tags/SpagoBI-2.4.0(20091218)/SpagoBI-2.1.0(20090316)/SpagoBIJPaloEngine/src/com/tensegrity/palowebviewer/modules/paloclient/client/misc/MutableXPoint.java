package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;

public class MutableXPoint implements IHaveCoordinates, IXPoint, IsSerializable {
	
	private IResultElement value;
	
	private List dimensions = new ArrayList();
	private List coordinates  = new ArrayList();

	public XPath getElementPath(XPath dimensionPath) {
		XPath r = null;
		for(int  i = 0; (i < dimensions.size()) && (r == null); ++i){
			if(dimensions.get(i).equals(dimensionPath))
				r = (XPath) coordinates.get(i);
		}
		return r;
	}

	public IResultElement getValue() {
		return value;
	}
	
	public void setValue(IResultElement value){
		this.value = value;
	}
	
	public void addCoordinate(XDimension dimension, XElement coordinate){
		XPath dimensionPath = dimension.constructPath();
		for (Iterator it = dimensions.iterator(); it.hasNext();) {
			XPath path = (XPath) it.next();
			if(path.covers(dimensionPath)){
				throw new IllegalArgumentException("dimension " + dimensionPath + " already added");
			}
		}
		XPath pointPath = XHelper.getPathTo(dimensionPath, coordinate);
		dimensions.add(dimensionPath);
		coordinates.add(pointPath);
	}

	public List getDimensions() {
		return dimensions;
	}
	

}
