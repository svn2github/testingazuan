package it.eng.spago.cms.exceptions;

public class ValueCastException extends Exception {
	
	public ValueCastException() {
		super();
	}
	
	public ValueCastException(String error) {
		super(error);
	}
	
	public ValueCastException(String error, Throwable exception) {
		super(error, exception);
	}
	
}
