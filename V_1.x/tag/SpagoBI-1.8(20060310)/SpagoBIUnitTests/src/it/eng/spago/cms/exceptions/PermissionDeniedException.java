package it.eng.spago.cms.exceptions;

public class PermissionDeniedException extends Exception {
	
	public PermissionDeniedException() {
		super();
	}
	
	public PermissionDeniedException(String error) {
		super(error);
	}
	
	public PermissionDeniedException(String error, Throwable exception) {
		super(error, exception);
	}
	
}
