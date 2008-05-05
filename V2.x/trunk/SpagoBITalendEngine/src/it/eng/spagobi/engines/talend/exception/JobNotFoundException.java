/**

 LICENSE: see COPYING file
  
**/
package it.eng.spagobi.engines.talend.exception;

/**
 * @author Andrea Gioia
 *
 */
public class JobNotFoundException extends TalendEngineException {
	
	/**
	 * Instantiates a new job not found exception.
	 */
	public JobNotFoundException() {}
	
	/**
	 * Instantiates a new job not found exception.
	 * 
	 * @param msg the msg
	 */
	public JobNotFoundException(String msg) {
		super(msg);
	}
}
