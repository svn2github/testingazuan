package com.tensegrity.palowebviewer.server;

/**
 * Class wich represent user and his roles
 *
 */
public class User implements IUser {
	
	private String login;

	/**
	 * creates new user
	 */
	public User() {
		this(null);
	}
	
	/**
	 * Creates user with login
	 *  @param login
	 */
	public User(String login) {
		this.login = login;
	}

	/*
	 * (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.server.IUser#getLogin()
	 */
	public String getLogin() {
		return login;
	}

	/*
	 * (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.server.IUser#setLogin(java.lang.String)
	 */
	public void setLogin(String login) {
		this.login = login;
	} 
	
}