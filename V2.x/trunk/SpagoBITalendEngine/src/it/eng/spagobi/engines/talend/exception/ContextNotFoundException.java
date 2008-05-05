/**

 LICENSE: see COPYING file
  
**/
package it.eng.spagobi.engines.talend.exception;

/**
 * @author Andrea Gioia
 *
 */
public class ContextNotFoundException extends TalendEngineException {
	
	/**
	 * Instantiates a new context not found exception.
	 */
	public ContextNotFoundException() {}
	
	/**
	 * Instantiates a new context not found exception.
	 * 
	 * @param msg the msg
	 */
	public ContextNotFoundException(String msg) {
		super(msg);
	}
}
