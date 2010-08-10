package com.tensegrity.palowebviewer.server.exeptions;

/**
 * ConfigurationException for using with server 
 */
public class ConfigurationException extends PaloWebViewerException {
	
	private static final long serialVersionUID = 1508462679191154801L;
	
	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}
	
}
