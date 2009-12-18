package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;

public class CubeViewCache {
	
	private final Map map = new HashMap();
	
	public void clear() {
		map.clear();
	}
	
	public XCube getCube(XView view) {
		XCube result = null;
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			ObjectKey key = (ObjectKey) it.next();
			XCube cube = (XCube) key.getObject();
			List list = getViewList(cube);
			if(list.contains(view)) {
				result = cube;
				break;
			}
		}
		return result;
	}
	
	public XView[] getViews(XCube cube, XView[] views) {
		if(cube == null)
			throw new IllegalArgumentException("Cube can not be null.");
		List list = getViewList(cube);
		XView[] result = new XView[views.length];
		for (int i = 0; i < views.length; i++) {
			result[i] = getView(cube, views[i], list);
		}
		return result;
	}
	
	public XView getView(XCube cube, XView view) {
		if(cube == null)
			throw new IllegalArgumentException("Cube can not be null.");
		List list = getViewList(cube);
		XView result = getView(cube, view, list);
		return result;
	}

	protected XView getView(XCube cube, XView view, List list) {
		int index = XArrays.findIndexById(list, view.getId());
		XView result = view;
		if(index >= 0){
			result = (XView)list.get(index); 
		}
		else {
			list.add(view);
			view.setParent(cube);
		}
		//result.setType(view.getType());
		return result;
	}
	
	protected List getViewList(XCube cube) {
		ObjectKey key = new ObjectKey(cube);
		List result = (List)map.get(key);
		if(result == null) {
			result = new ArrayList();
			map.put(key, result);
		}
		return result;
	}

	public void removeViewWithId(String id) {
		if(id != null) {
			for (Iterator it = map.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				List list = (List)map.get(key);
				for (Iterator it2 = list.iterator(); it2.hasNext();) {
					XView view = (XView) it2.next();
					if(id.equals(view.getId())) {
						it2.remove();
						return;
					}
				}
			}
		}
	}
	
}
