package com.tensegrity.palowebviewer.server.security;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.AuthenticationException;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidLoginOrPasswordException;
import com.tensegrity.palowebviewer.server.IUser;
import com.tensegrity.palowebviewer.server.PaloConfiguration;
import com.tensegrity.palowebviewer.server.User;
import com.tensegrity.palowebviewer.server.exeptions.PaloWebViewerException;

/**
 * 
 * Authenificator for using with GWT Development shell
 *
 */
public class TestEnviromentAuthenificator extends BaseMd5Authentificator {

	private String[][] users = {
		{"guest", "pass"},
	};
	public TestEnviromentAuthenificator(PaloConfiguration cfg) throws PaloWebViewerException {
		users[0] = new String[]{cfg.getUser(), cfg.getPassword()};
	}

	
	public IUser authentificate(String login, String password) throws InvalidLoginOrPasswordException, AuthenticationException {
		if( login.equals("error") )
			throw new AuthenticationException("User 'error' trying to login");
		for (int i = 0; i < users.length; i++) {
			String[] pair = users[i];
			if(pair[0].equals(login) && pair[1].equals(password)){
				return new User(login);
			}
		}
		
		throw new InvalidLoginOrPasswordException();
	}

	public IUser hashAuthentificate(String login, String hash) throws InvalidLoginOrPasswordException, AuthenticationException {
		for (int i = 0; i < users.length; i++) {
			String[] pair = users[i];
			String pwdHash = calculateHash(pair[1]);
			if(pair[0].equals(login) && pwdHash.equals(hash)){
				return new User(login);
			}
		}
		
		throw new InvalidLoginOrPasswordException();
	}

}