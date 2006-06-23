package it.eng.spago.cms.exceptions;

/**
 *
 *  Exception throw when the path of an element it's not in the correct format
 *
 */

public class PathNotValidException extends Exception {

	/**
	 *  Public constructor with no description message.
	 */
	public PathNotValidException() {
		super();
	}
	
	/**
	 * Public constructor with a description message
	 * @param msg The message of description
	 */
	public PathNotValidException(String msg) {
		super(msg);
	}
	
	public PathNotValidException(String msg, Throwable t) {
		super(msg, t);
	}
	
}
