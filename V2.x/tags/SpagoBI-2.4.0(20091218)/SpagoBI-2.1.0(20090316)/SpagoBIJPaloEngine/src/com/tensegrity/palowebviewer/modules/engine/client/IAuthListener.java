package com.tensegrity.palowebviewer.modules.engine.client;

/**
 * Listener for authentication events.
 */
public interface IAuthListener {
	
	/**
	 * User credentials founded
	 */
	public void onAuthSuccess();

	/**
	 * User credentials not founded
	 */
	public void onAuthFailed(String cause);
	
	/**
	 *  User logged off.
	 */
	public void onLogoff();
	
}
