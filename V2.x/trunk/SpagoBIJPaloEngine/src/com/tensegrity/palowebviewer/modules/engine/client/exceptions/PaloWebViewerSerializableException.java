package com.tensegrity.palowebviewer.modules.engine.client.exceptions;

import com.google.gwt.user.client.rpc.SerializableException;

/**
 * This exception servers as base exceptions for all WPalo excpetions
 */
public class PaloWebViewerSerializableException extends SerializableException {

	private static final long serialVersionUID = 4896953123857471624L;

	/**
	 * default constructor needed to deserialization
	 *
	 */
	public PaloWebViewerSerializableException() {
		super();
	}

	/**
	 * Constructor with description for base exception 
	 * @param msg describes exception
	 */
	public PaloWebViewerSerializableException(String msg) {
		super(msg);
	}
	
	

}
