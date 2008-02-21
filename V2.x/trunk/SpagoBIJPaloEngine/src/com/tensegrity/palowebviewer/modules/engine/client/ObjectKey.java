package com.tensegrity.palowebviewer.modules.engine.client;

public class ObjectKey {
	
	private final Object value;
	
	public ObjectKey(Object value) {
		this.value = value;
	}
	
	public Object getObject () {
		return value;
	}
	
	public boolean equals(Object o) {
		boolean result = o instanceof ObjectKey;
		if(result) {
			ObjectKey key = (ObjectKey)o;
			result = key.value == value;
		}
		return result;
	}
	
	public int hashCode() {
		int result = 0;
		if(value != null)
			result = value.hashCode();
		return result;
	}

}
