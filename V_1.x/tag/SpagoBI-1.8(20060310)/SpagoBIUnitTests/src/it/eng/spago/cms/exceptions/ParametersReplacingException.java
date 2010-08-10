package it.eng.spago.cms.exceptions;

public class ParametersReplacingException extends Exception {
	
	public ParametersReplacingException() {
		super();
	}
	
	public ParametersReplacingException(String error) {
		super(error);
	}
	
	public ParametersReplacingException(String error, Throwable exception) {
		super(error, exception);
	}
	
}
