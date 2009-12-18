package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tensegrity.palowebviewer.modules.paloclient.client.XDatabase;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;

public class DatabaseDimensionCache {
	
	private final Map map = new HashMap();
	
	public void clear() {
		map.clear();
	}
	
	public XDimension[] getDimensions(XDatabase db, XDimension[] dimensions) {
		if(db == null)
			throw new IllegalArgumentException("Database can not be null.");
		List list = getDimensionList(db);
		XDimension[] result = new XDimension[dimensions.length];
		for (int i = 0; i < dimensions.length; i++) {
			result[i] = getDimension(db, list, dimensions[i]);	
		}
		return result;
	}

	private XDimension getDimension(XDatabase db, List list, XDimension dim) {
		XDimension result = dim;
		int index = list.indexOf(dim);
		if(index >= 0){
			result = (XDimension)list.get(index); 
		}
		else {
			list.add(dim);
			dim.setParent(db);
		}
		return result;
	}
	
	public XDimension getDimension(XDatabase db, XDimension dim) {
		if(db == null)
			throw new IllegalArgumentException("Database can not be null.");
		List list = getDimensionList(db);
		XDimension result = getDimension(db, list, dim);
		return result;
	}
	
	protected List getDimensionList(XDatabase db) {
		ObjectKey key = new ObjectKey(db);
		List result = (List)map.get(key);
		if(result == null) {
			result = new ArrayList();
			map.put(key, result);
		}
		return result;
	}

}
