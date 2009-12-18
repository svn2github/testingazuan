package com.tensegrity.palowebviewer.modules.engine.client;

/**
 * ErrorListener interface
 */
public interface IErrorListener {

	/**
     * This method is call, if some error occurred.
	 * @param caught - Error caught
	 */
	public void onError(Throwable caught);
	
}
