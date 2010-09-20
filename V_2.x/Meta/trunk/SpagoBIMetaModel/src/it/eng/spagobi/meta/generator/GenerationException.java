/**
 * 
 */
package it.eng.spagobi.meta.generator;

/**
* @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class GenerationException extends RuntimeException {
	public GenerationException(String msg) {
		super(msg);
	}
	
	public GenerationException(String msg, Throwable t) {
		super(msg, t);
	}
}
