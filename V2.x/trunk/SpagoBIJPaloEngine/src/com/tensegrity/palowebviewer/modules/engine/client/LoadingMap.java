package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.HashMap;
import java.util.Map;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;

public class LoadingMap {
	
	
	private final Map map = new HashMap();
	
	
	public void clear() {
		map.clear();
	}
	
	public boolean isLoading(XObject object, int childType) {
		Object key = getKey(object, childType);
		return map.containsKey(key);
	}

	public LoadChildrenCallback getLoader(XObject object, int childType) {
		Object key = getKey(object, childType);
		return (LoadChildrenCallback)map.get(key);
	}
	
	public void setLoading(XObject object, int childType, LoadChildrenCallback loader) {
		Object key = getKey(object, childType);
		map.put(key, loader);
	}
	
	public void setFinishLoading(XObject object, int childType) {
		Object key = getKey(object, childType);
		map.remove(key);
	}
	
	protected Object getKey(XObject object, int childType) {
		return new ChildrenKey(object, childType);
	}
	
	public static final class ChildrenKey {
		
		private final XObject object;
		private final int childType;

		public ChildrenKey(XObject object, int childType) {
			if(object == null)
				throw new IllegalArgumentException("Object can not be null");
			this.object = object;
			this.childType = childType;
		}
		
		public boolean equals(Object obj) {
			boolean result = false;
			if (obj instanceof ChildrenKey) {
				ChildrenKey key = (ChildrenKey) obj;
				result = object == key.object && childType == key.childType;
			}
			return result;
		}
		
		public int hashCode() {
			return object.hashCode(); 
		}
		
	}

}
