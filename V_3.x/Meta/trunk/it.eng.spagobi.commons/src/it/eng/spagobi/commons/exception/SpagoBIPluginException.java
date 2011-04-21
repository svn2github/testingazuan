/**
 * 
 */
package it.eng.spagobi.commons.exception;

/**
* @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIPluginException extends RuntimeException {

	private static final long serialVersionUID = -2775618353105672486L;

	public SpagoBIPluginException(String msg) {
		super(msg);
	}
	
	public SpagoBIPluginException(String msg, Throwable t) {
		super(msg, t);
	}
}
