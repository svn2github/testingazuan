package com.tensegrity.palowebviewer.modules.engine.client.exceptions;

import com.google.gwt.core.client.GWT;
import com.tensegrity.palowebviewer.modules.engine.client.l10n.PaloLocalizedStrings;

/**
 *  This exception is thrown, when user credentials (user/password) are invalid.  
 */
public class InvalidLoginOrPasswordException extends AuthenticationException {

	private static final long serialVersionUID = -3584528185067639064L;
	
	private String message;
	
	/**
	 * default constructor needed to deserialization
	 *
	 */
	public InvalidLoginOrPasswordException() {
		super();
	}

	/**
	 * returns exception description.
	 */
	public String getMessage() {
		if ( message == null ) {
			PaloLocalizedStrings localizedStrings = (PaloLocalizedStrings)GWT.create(PaloLocalizedStrings.class);
			message = localizedStrings.errorInvalidLoginOrPassword();
		}
		return message;
	}

}