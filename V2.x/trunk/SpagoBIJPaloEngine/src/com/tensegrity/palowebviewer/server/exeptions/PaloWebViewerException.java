package com.tensegrity.palowebviewer.server.exeptions;

/**
 * PaloWebViewerException for using with server 
 */
public class PaloWebViewerException extends Exception {

	private static final long serialVersionUID = -5420233565470359564L;

	public PaloWebViewerException() {
	}

	public PaloWebViewerException(String message) {
		super(message);
	}

	public PaloWebViewerException(Throwable cause) {
		super(cause);
	}

	public PaloWebViewerException(String message, Throwable cause) {
		super(message, cause);
	}

}
