package com.tensegrity.palowebviewer.server.security;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.AuthenticationException;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidLoginOrPasswordException;
import com.tensegrity.palowebviewer.server.IUser;

public interface IAuthenificator {
	
	/**
	 * Authenificate user by login and password
	 * 
	 * @param login - User login
	 * @param password - User password
	 * @return
	 * @throws InvalidLoginOrPasswordException - Invalid login or(and) password
	 * @throws AuthenticationError - Throws if can't check user credentials because of internal error
	 */
	public IUser authentificate(String login, String password) throws InvalidLoginOrPasswordException, AuthenticationException;
	
	/**
	 * Authenificate user by login and password hash
	 * 
	 * @param login - User login
	 * @param hash - User password hash
	 * @return
	 * @throws InvalidLoginOrPasswordException - Invalid login or(and) password
	 * @throws AuthenticationError - Throws if can't check user credentials because of internal error
	 */
	public IUser hashAuthentificate(String login, String hash) throws InvalidLoginOrPasswordException, AuthenticationException;
	
	/**
	 * Calculates hash for given key (password)
	 * 
	 * @param key
	 * @return hash
	 */
	public String calculateHash(String key);
	
}
