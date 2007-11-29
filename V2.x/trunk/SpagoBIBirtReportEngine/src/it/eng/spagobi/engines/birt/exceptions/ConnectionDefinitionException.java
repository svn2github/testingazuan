/**
 * 
 * LICENSE: see BIRT.LICENSE.txt file
 * 
 */
package it.eng.spagobi.engines.birt.exceptions;

public class ConnectionDefinitionException extends Exception {
	
	protected String description;
	
	public ConnectionDefinitionException() {
		super();
	}
	
	public ConnectionDefinitionException(String msg) {
		super(msg);
		this.description = msg;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
