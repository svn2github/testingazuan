package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageQueue;


/**
 * Palo AJAX Browser Engine.
 * 
 * <p>
 * Business logic interface
 * </p>
 */
public interface IEngine {

	/**
	 * Async function. Check if user authenticated.
	 * Lookup for user credentials in session and cookies
	 * 
	 * @param authListener - Async callback listener 
	 */
	public void authenticate();

	/**
	 * Async function. Authenticate user with login and password
	 * 
	 * @param login - user login
	 * @param password - password
	 * @param remember - store login and password in cookies
	 */
	public void authenticate(String login, String password, boolean remember);

	/**
	 * Adds engine authentication listener
	 * 
	 * @param IAuthListener - auth listener
	 */
	public void addAuthenticateListener(IAuthListener authListener);

	/**
	 * Remove engine authentication listener
	 * 
	 * @param IAuthListener - auth listener
	 */
	public void removeAuthenticateListener(IAuthListener authListener);
	
	/**
	 * Adds engine error listener
	 * 
	 * @param IAuthListener - error listener
	 */
	public void addErrorListener(IErrorListener errorListener);
	
	/**
	 * Remove engine error listener
	 * 
	 * @param IAuthListener - error listener
	 */
	public void removeErrorListener(IErrorListener errorListener);

	/**
	 * Get PaloServerModel
	 */
    public IPaloServerModel getPaloServerModel();

	/**
	 * Get login action source
	 */
	public void logout();
	
	public IClientProperties getClientProperties();
	
	public void addRequestListener(IRequestListener listener);
	
	public void removeRequestListener(IRequestListener listener);
	
	public IUserMessageQueue getUserMessageQueue();

}
