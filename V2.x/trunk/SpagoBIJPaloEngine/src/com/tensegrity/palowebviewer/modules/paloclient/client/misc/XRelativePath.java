package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;


public class XRelativePath extends XPath {
	
	private XPath parentPath;
	private transient XPathElement[] fullPath;
	
	public XRelativePath(){
		
	}
	
	private void constructFullPath(){
		List path = new ArrayList(Arrays.asList(parentPath.getPath()));
		path.addAll(Arrays.asList(super.getPath()));
		XPathElement[] r = new XPathElement[path.size()];
		for (int i = 0; i < r.length; i++) {
			r[i] = (XPathElement)path.get(i);
		}
		fullPath = r; 
	}
	
	public XRelativePath(XPath parentPath, XObject[] xRelativePath){
		super(xRelativePath);
		this.parentPath = parentPath;
		if(parentPath == null || xRelativePath == null) 
			throw new IllegalArgumentException("parentPath or xRelative can't be null");
		constructFullPath();
	}
	
	public XRelativePath(XPath parentPath, XObject object){
		this(parentPath, new XObject[]{object});
		if(object == null) 
			throw new IllegalArgumentException("object can't be null");
		constructFullPath();
	}

	public XPathElement[] getPath() {
		if(fullPath == null){
			constructFullPath();
		}
		return fullPath;
	}

	public String toString() {
		return parentPath.toString() + super.toString();
	}
	
}
