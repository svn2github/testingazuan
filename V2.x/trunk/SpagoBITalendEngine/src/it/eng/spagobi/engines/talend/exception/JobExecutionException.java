/**

 LICENSE: see COPYING file
  
**/
package it.eng.spagobi.engines.talend.exception;

/**
 * @author Andrea Gioia
 *
 */
public class JobExecutionException extends TalendEngineException {
	
	/**
	 * Instantiates a new job execution exception.
	 */
	public JobExecutionException() {}
	
	/**
	 * Instantiates a new job execution exception.
	 * 
	 * @param msg the msg
	 */
	public JobExecutionException(String msg) {
		super(msg);
	}
	
	/**
	 * Instantiates a new job execution exception.
	 * 
	 * @param msg the msg
	 * @param e the e
	 */
	public JobExecutionException(String msg, Throwable e) {
		super(msg, e);
	}
}
