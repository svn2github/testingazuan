package com.tensegrity.palowebviewer.server.exeptions;

/*
 * PaloWebViewerRuntimeException for using with server
 */
public class PaloWebViewerRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -2030172096324133062L;

	public PaloWebViewerRuntimeException() {
	}

	public PaloWebViewerRuntimeException(String message) {
		super(message);
	}

	public PaloWebViewerRuntimeException(Throwable cause) {
		super(cause);
	}

	public PaloWebViewerRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
