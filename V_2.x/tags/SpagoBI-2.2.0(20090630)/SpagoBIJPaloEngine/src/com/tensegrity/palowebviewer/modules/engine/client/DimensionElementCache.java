package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;

public class DimensionElementCache {
	
	private final Map map = new HashMap();
	
	
	public void clear() {
		map.clear();
	}
	
	public XElement[] getElements(XDimension dim, XElement[] elements) {
		if(dim == null)
			throw new IllegalArgumentException("Dimension can not be null.");
		List list = getElementList(dim);
		XElement[] result = new XElement[elements.length];
		for (int i = 0; i < elements.length; i++) {
			result[i] = getElement(dim, elements[i], list);
		}
		return result;
	}
	
	public XElement getElement(XDimension dim, XElement element) {
		if(dim == null)
			throw new IllegalArgumentException("Dimension can not be null.");
		List list = getElementList(dim);
		XElement result = getElement(dim, element, list);
		return result;
	}

	protected XElement getElement(XDimension dim, XElement element, List list) {
		int index = XArrays.findIndexById(list, element.getId());
		XElement result = element;
		if(index >= 0){
			result = (XElement)list.get(index);
			result.setName(element.getName());
		}
		else {
			list.add(element);
			element.setParent(dim);
		}
		//result.setType(element.getType());
		return result;
	}
	
	protected List getElementList(XDimension dim) {
		ObjectKey key = new ObjectKey(dim);
		List result = (List)map.get(key);
		if(result == null) {
			result = new ArrayList();
			map.put(key, result);
		}
		return result;
	}
	
}
