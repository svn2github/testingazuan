/**

 LICENSE: see COPYING file
  
**/
package it.eng.spagobi.engines.talend.exception;

/**
 * @author Andrea Gioia
 *
 */
public class AuthenticationFailedException extends TalendEngineException {
	
	/**
	 * Instantiates a new authentication failed exception.
	 */
	public AuthenticationFailedException() {}
	
	/**
	 * Instantiates a new authentication failed exception.
	 * 
	 * @param msg the msg
	 */
	public AuthenticationFailedException(String msg) {
		super(msg);
	}
}
