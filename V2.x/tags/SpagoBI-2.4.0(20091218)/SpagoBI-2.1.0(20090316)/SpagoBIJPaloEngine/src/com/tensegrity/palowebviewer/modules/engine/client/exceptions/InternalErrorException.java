package com.tensegrity.palowebviewer.modules.engine.client.exceptions;

import com.tensegrity.palowebviewer.modules.engine.client.l10n.PaloLocalizedStrings;

public class InternalErrorException extends LocalizedException {
	
	private static final long serialVersionUID = -5882774824918562155L;

	public InternalErrorException() {
		super();
	}

	protected String getLocalizedMessage(PaloLocalizedStrings localizedStrings) {
		return localizedStrings.internalServerError();
	}

}