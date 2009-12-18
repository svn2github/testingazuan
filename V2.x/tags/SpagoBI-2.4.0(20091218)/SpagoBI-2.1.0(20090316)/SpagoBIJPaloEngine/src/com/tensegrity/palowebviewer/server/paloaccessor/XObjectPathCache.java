package com.tensegrity.palowebviewer.server.paloaccessor;

import java.util.HashMap;
import java.util.Map;

import org.palo.api.Connection;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;

public class XObjectPathCache {
	
	private final Map cache = new HashMap();
	
	public XObject getCachedObject(XPath path, XObject o){
		XObject result = getCachedObject(path);
		if(result == null){
			result = o;
			put(path, o);
		}
		return result;
	}
	
	public XObject getCachedObject(XPath path){
		XObject result = (XObject)cache.get(path);
		return result;
	}
	
	public XObject getObject(XPath path, Connection connection) throws InvalidObjectPathException{
		XObject result = getCachedObject(path);
	   	if(result == null){
		        PathConstructor constructor = new PathConstructor(this);
		        constructor.setConnection(connection);
		        constructor.setXPath(path);
		        result = constructor.getLastObject();
		        put(path, result);
	   	}
		return result;
	}
	
	public void put(XPath path, XObject o) {
		cache.put(path, o);
	}
	
	public void clear() {
		cache.clear();
	}
	

}
