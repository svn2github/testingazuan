
package it.eng.qbe.javascript;


/**
 * @author Andrea Zoppello
 * 
 * This is interface for object responsible of generating URLS
 * 
 * Object Implementing this interface are for example used to change urls associated
 * to node of a tree
 *
 */
public interface IURLGenerator {

	public String generateURL(Object source);
	
	public String generateURL(Object source, Object addtionalParameter);
	
	public String generateURL(Object source, Object source2, Object addtionalParameter);
}
