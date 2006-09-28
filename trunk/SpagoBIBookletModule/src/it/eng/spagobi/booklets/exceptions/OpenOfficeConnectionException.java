package it.eng.spagobi.booklets.exceptions;

public class OpenOfficeConnectionException extends Exception {

	public OpenOfficeConnectionException() {
		super();
	}
	
	public OpenOfficeConnectionException(String message) {
	 	super(message);
	}
	 
	public OpenOfficeConnectionException(String message, Throwable cause) {
	    super(message, cause);
	} 
	
	public OpenOfficeConnectionException(Throwable cause) {
	    super(cause);
	}
	
}
