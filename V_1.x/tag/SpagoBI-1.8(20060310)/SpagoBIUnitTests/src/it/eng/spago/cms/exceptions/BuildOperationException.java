package it.eng.spago.cms.exceptions;

public class BuildOperationException extends Exception {
	
	public BuildOperationException() {
		super();
	}
	
	public BuildOperationException(String error) {
		super(error);
	}
	
	public BuildOperationException(String error, Throwable exception) {
		super(error, exception);
	}
	
}
