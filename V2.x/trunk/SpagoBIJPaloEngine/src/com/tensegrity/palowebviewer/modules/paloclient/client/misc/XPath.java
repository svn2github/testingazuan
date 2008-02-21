package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.util.client.Escaper;

/**
 * 
 * This class represents path to XObject as array of String. If some XObject has more than one child type
 * (for example, database has dimension and cube types), then object name is prefixed with type indicator.  
 * XPath can point to Subset elements : .../subset:ss1/element1/.../elementN. 
 *
 */
public class XPath implements IXConsts, IsSerializable{
	
	private transient XPathElement[] path;
	private transient int hash;
	private String strPath;
	
	public XPath(){
	}
	
	public XPath(String path){
		this.path = splitPath(path);
		strPath = path;
		calculateHash();
	}

	public XPath(XPathElement[] path){
		this.path = path;
		calculateHash();
		strPath = myToString();
	}

	public XPath(XObject[] xPath){
		path = new XPathElement[xPath.length];
		for (int i = 0; i < xPath.length; i++) {
			XObject x = xPath[i];
			path[i] = XPathElement.create(x);
		}
		strPath = myToString();
		calculateHash();
	}
	
	private static XPathElement[] splitPath(String strPath){
		String s = strPath.substring(1); //skip 1st slash
		String[] splited = Escaper.splitAndUnescape(s, XPATH_SEPARATOR);
		XPathElement[] r = new XPathElement[splited.length];
		for (int i = 0; i < r.length; i++) {
			r[i] = XPathElement.create(splited[i]);
		}
		return r;
	}
	
	public boolean covers(XPath other){
		boolean r = equals(other);
		if(!r){
			XPathElement[] otherPath = other.getPath();
	        XPathElement[] path = getPath();
	        if(otherPath.length < path.length){
	        	for (int i = path.length-1; (i >=0 ) && r; i++) {
					r = otherPath[i].equals(path[i]);
				}
	        }
		}
		return r;
	}
	
	public boolean equals(Object o) {
        if(o instanceof XPath)
            return equals((XPath)o);
        else
            return false;
    }
	
	public boolean equals(XPath another) {
        if(another == null)
            return false;
        boolean result = true;

        XPathElement[] otherPath = another.getPath();
        XPathElement[] path = getPath();
        result = path.length == otherPath.length;
        for (int i = 0; result && (i < otherPath.length); i++) {
            result = path[i].equals(otherPath[i]);
        }
        return result;
    }
	
	public XPathElement getLastComponent(){
		XPathElement[] path = getPath();
		return path[path.length-1];
	}
	
	public XPathElement[] getPath() {
		return myGetPath();
	}
	
	public XPathElement getServer() {
		int serverElementIndex = 1;
		return getPath()[serverElementIndex];
	}
	
	public int hashCode(){
    	return hash;
    }

	public String toString(){
		return strPath;
	}
	
	private void calculateHash() {
		hash = this.path[this.path.length-1].hashCode();
	}

    private XPathElement[] myGetPath(){
		if(path == null){
			path = splitPath(strPath);
		}
		return path;
	}

    private String myToString(){
		String r;
		if(strPath == null){
			XPathElement[] path = myGetPath();
			String[] encodedPath = new String[path.length];
			for (int i = 0; i < path.length; i++) {
				encodedPath[i] = path[i].encode();
			}
			r = XPATH_SEPARATOR + Escaper.escapeAndConcat(encodedPath, XPATH_SEPARATOR);
		}else{
			r = strPath; 
		}
		return r;
	}



}
