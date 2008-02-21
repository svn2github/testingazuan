package com.tensegrity.palowebviewer.modules.engine.client.exceptions;

/**
 * This class serves as base exception for all auth-exceptions. 
 * @author dmol
 *
 */
public class AuthenticationException extends PaloWebViewerSerializableException {

	private static final long serialVersionUID = -4935609835237619655L;
	
	/**
	 * default constructor needed to deserialization
	 *
	 */
	public AuthenticationException() {
		super();
	}

	/**
	 * Constructor with description for base exception 
	 * @param msg describes exception
	 */
	public AuthenticationException(String msg) {
		super(msg);
	}
	
}
