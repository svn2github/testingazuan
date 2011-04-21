/**
 * 
 */
package it.eng.spagobi.meta.generator;

/**
* @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class GenerationException extends RuntimeException {

	private static final long serialVersionUID = -2775618353105672486L;

	public GenerationException(String msg) {
		super(msg);
	}
	
	public GenerationException(String msg, Throwable t) {
		super(msg, t);
	}
}
