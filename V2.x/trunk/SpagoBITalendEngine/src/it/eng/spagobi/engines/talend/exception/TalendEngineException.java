/**

 LICENSE: see COPYING file
  
**/
package it.eng.spagobi.engines.talend.exception;

/**
 * @author Andrea Gioia
 *
 */
public class TalendEngineException extends Exception {
	
	/**
	 * Instantiates a new talend engine exception.
	 */
	public TalendEngineException() {}
	
	/**
	 * Instantiates a new talend engine exception.
	 * 
	 * @param msg the msg
	 */
	public TalendEngineException(String msg) {
		super(msg);
	}
	
	/**
	 * Instantiates a new talend engine exception.
	 * 
	 * @param msg the msg
	 * @param e the e
	 */
	public TalendEngineException(String msg, Throwable e) {
		super(msg, e);
	}
}
