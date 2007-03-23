package it.eng.spagobi.utilities.messages;

import it.eng.spago.base.RequestContainer;

/**
 * This is the interface for classes that implements logic
 * to retrieve localized messages to be used in JSP pages 
 */
public interface IMessageBuilder {

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


	/**
	 * Get the localized text contained into the resource
	 * @param resourceName The complete name of the resource
	 * @return the localized text contained into the resource
	 */
	public String getMessageTextFromResource(String resourceName);
	
}
