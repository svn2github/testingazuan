package com.tensegrity.palowebviewer.modules.engine.client.exceptions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.SerializableException;
import com.tensegrity.palowebviewer.modules.engine.client.l10n.PaloLocalizedStrings;

public abstract class LocalizedException extends SerializableException {
	
	private String message;
	
	public LocalizedException() {
		super();
	}
	
	protected abstract String getLocalizedMessage(PaloLocalizedStrings localizedStrings);

	public String getMessage() {
		
		if ( message == null ) {
			PaloLocalizedStrings localizedStrings = (PaloLocalizedStrings)GWT.create(PaloLocalizedStrings.class);
			message = getLocalizedMessage(localizedStrings);
		}
		
		return message;
	}
	
}