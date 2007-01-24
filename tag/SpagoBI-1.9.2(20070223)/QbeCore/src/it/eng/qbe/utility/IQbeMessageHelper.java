package it.eng.qbe.utility;

import it.eng.spago.base.RequestContainer;

/**
 * @author Andrea Zoppello
 * 
 * This is the interface for classes that implements logic
 * to retrieve localized messages to be used in JSP pages 
 */
public interface IQbeMessageHelper {

	/**
	 * @param aRequestContainer: Spago Request Container
	 * @param code: the message code
	 * @return: the message associated with code in the default bundle
	 */
	public String getMessage(RequestContainer aRequestContainer, String code);
	
	/**
	 * @param aRequestContainer: Spago Request Container
	 * @param code: the message code
	 * @param bundle: the bundle to use
	 * @return  the message associated with code in the given bundle
	 */
	public String getMessage(RequestContainer aRequestContainer, String code, String bundle);
}
