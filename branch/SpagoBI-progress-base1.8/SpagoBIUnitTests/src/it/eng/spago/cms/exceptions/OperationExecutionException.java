package it.eng.spago.cms.exceptions;

public class OperationExecutionException extends Exception {
	
	public OperationExecutionException() {
		super();
	}
	
	public OperationExecutionException(String error) {
		super(error);
	}
	
	public OperationExecutionException(String error, Throwable exception) {
		super(error, exception);
	}
	
}
