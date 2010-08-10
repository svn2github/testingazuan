package com.tensegrity.palowebviewer.modules.paloclient.client;


/**
 *
 * Notices client, than some element is invalid; (for example, already deleted). 
 */
public class XInvalidType extends XElementType {

	public XInvalidType(){
		super("invalid");
	}
	
	public static final XInvalidType instance = new XInvalidType();
}
