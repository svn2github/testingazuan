package com.tensegrity.palowebviewer.modules.paloclient.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;
import com.tensegrity.palowebviewer.modules.util.client.Arrays;

public class XElementPath implements IsSerializable {
	private static final int NOT_INITIALIZED = -1;
	/**
	 *@gwt.typeArgs <com.tensegrity.palowebviewer.modules.paloclient.client.XDimension>
	 */
	private List dimensions = new ArrayList(); //keeps dimension order
	/**
	 *@gwt.typeArgs <com.tensegrity.palowebviewer.modules.paloclient.client.XDimension,com.tensegrity.palowebviewer.modules.paloclient.client.XElement[]>
	 */
	private Map map = new HashMap(); //key: Dimension , value: XElement[] of element path
	private int hashCode = NOT_INITIALIZED;
	
	/**
	 * adds dimension name and element names; 
	 */
	public void addPart(XDimension dimension, XElement[] path){
		if(dimension == null) throw new IllegalArgumentException("dimension can't be null");
		if(path == null) throw new IllegalArgumentException("path can't be null");
		if(dimensions.contains(dimension)) throw new RuntimeException("dimension '" + dimension + "' already added");
		dimensions.add(dimension);
		map.put(dimension, path);
		hashCode = NOT_INITIALIZED;
	}
	
	public XDimension[] getDimensions(){
		return (XDimension[])XArrays.toArray(dimensions, IXConsts.TYPE_DIMENSION);
	}
	
	public XElement[] getPart(XDimension dimension){
		return (XElement[])map.get(dimension);
	}

	public boolean equals(Object obj) {
		boolean r = false;
		if (obj instanceof XElementPath) {
			XElementPath path = (XElementPath) obj;
			r = dimensions.equals(path.dimensions);
			for(Iterator it = map.entrySet().iterator(); it.hasNext() && r;){
				Map.Entry entry = (Map.Entry)it.next();
				XElement[] myElements = (XElement[]) entry.getValue();
				XElement[] otherElements = (XElement[]) path.map.get(entry.getKey());
				r = Arrays.equals(myElements, otherElements);
			}
		}
		return r;
	}
	
	public String toString() {
		String result = "XElementPath[";
		for (Iterator it = dimensions.iterator(); it.hasNext();) {
			XDimension dimension = (XDimension) it.next();
			XElement[] elements = getPart(dimension);
			result += dimension.getName();
			result += "=>";
			result += Arrays.toString(elements);
			result += " ";
		}
		result += "]";
		return result;
	}

	public int hashCode() {
		if(hashCode == NOT_INITIALIZED){
			hashCode = XArrays.hashCode(getDimensions());
		}
		return hashCode;
	}
	
	
}
