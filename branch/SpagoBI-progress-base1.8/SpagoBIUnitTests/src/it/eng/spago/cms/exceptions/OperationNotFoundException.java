package it.eng.spago.cms.exceptions;

public class OperationNotFoundException extends Exception {

	public OperationNotFoundException() {
		super();
	}
	
	public OperationNotFoundException(String error) {
		super(error);
	}
	
	public OperationNotFoundException(String error, Throwable exception) {
		super(error, exception);
	}
	
}
