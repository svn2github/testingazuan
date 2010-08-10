package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.HashMap;
import java.util.Map;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;

public class PathCache {
	
	private final IPaloServerModel serverModel;
	private final Map pathMap = new HashMap();

	public PathCache(IPaloServerModel serverModel){
		this.serverModel = serverModel;
	}
	
	public void clear() {
		pathMap.clear();
	}
	
	public XObject getObject(XPath path) {
        XObject result = (XObject)pathMap.get(path);
        if(result == null) {
            result = translatePath(path);
            if(result != null)
            	pathMap.put(path, result);
        }
        return result;
	}
	
    protected XObject translatePath(XPath path) {
        return XHelper.getDenotedObject(serverModel.getRoot(), path);
    }



}
