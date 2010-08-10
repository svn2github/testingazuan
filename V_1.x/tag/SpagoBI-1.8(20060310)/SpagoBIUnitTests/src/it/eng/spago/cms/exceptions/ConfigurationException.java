package it.eng.spago.cms.exceptions;

public class ConfigurationException extends Exception {

	public ConfigurationException() {
		super();
	}
	
	public ConfigurationException(String error) {
		super(error);
	}
	
	public ConfigurationException(String error, Throwable exception) {
		super(error, exception);
	}
	
}
