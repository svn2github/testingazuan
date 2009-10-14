package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.widgets.client.tree.ITreeModel;

public class TreeEncoder {
	
	private ITreeModel model;
	
    public String encodeSubTree(ITreeModel model, Object parent) {
        if(parent == null)
            parent = model.getRoot();
        this.model = model;
        return encodeSubTree(parent, 0);
    }
    
    public String encodeChildren(ITreeModel model, Object parent) {
    	this.model = model;
    	String result = "";
    	final int size = model.getChildCount(parent);
    	for (int i = 0; i < size; i++) {
    		Object child = model.getChild(parent, i);
    		result += encodeChild(0, child); 
		}
    	return result;
    }

    private String encodeSubTree(Object parent, int level) {
        String result = "";
        int size = model.getChildCount(parent);
        int nextLevel = level+1;
        for( int i = 0 ; i < size ; i++ ) { 
            Object child = model.getChild(parent, i);
            result += encodeChild(level, child);
            if(!model.isLeaf(child))
                result += encodeSubTree(child, nextLevel);
        } 
        return result;
    }

	private String encodeChild(int level, Object child) {
		String result;
		char expandable = model.isLeaf(child) ? '-' : '+';
		String id = child+"";
		result = level + "" + expandable + ":" + id + "/";
		return result;
	}

}
