package com.tensegrity.palowebviewer.modules.engine.client.exceptions;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;

/**
 *
 * This expcetion occures in situation, when some XPath is invalid. Generally
 * that means absence of some object, because connetion is out-of-date
 *
 */
public class InvalidObjectPathException extends PaloWebViewerSerializableException {

	private static final long serialVersionUID = -8783873543300941460L;
	private XPath path;

	/**
	 * default constructor needed to deserialization
	 *
	 */
	public InvalidObjectPathException(){
		
	}
	
	/**
	 * Constructor with message description an path
	 * @param msg describes a situation why the exception occures.
	 * @param path specifies the path, on which the exception occured. {@link XPath}
	 */
	public InvalidObjectPathException(String msg, XPath path){
		super(msg);
		this.path = path;
	}
	
	/**
	 * returns the path, on which exception was occured.
	 * @return the path, on which exception was occured.
	 */
	public XPath getPath() {
		return path;
	}
	

}
