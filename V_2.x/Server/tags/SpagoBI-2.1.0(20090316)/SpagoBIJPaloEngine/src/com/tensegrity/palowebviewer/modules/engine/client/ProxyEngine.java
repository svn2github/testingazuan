package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageQueue;

public class ProxyEngine implements IEngine {
	
	private final IEngine engine;

	public ProxyEngine(IEngine engine) {
		this.engine = engine;
	}

	public void addAuthenticateListener(IAuthListener authListener) {
		engine.addAuthenticateListener(authListener);
	}

	public void addErrorListener(IErrorListener errorListener) {
		engine.addErrorListener(errorListener);
	}

	public void addRequestListener(IRequestListener listener) {
		engine.addRequestListener(listener);

	}

	public void authenticate() {
		engine.authenticate();
	}

	public void authenticate(String login, String password, boolean remember) {
		engine.authenticate(login, password, remember);
	}

	public IClientProperties getClientProperties() {
		return engine.getClientProperties();
	}

	public IPaloServerModel getPaloServerModel() {
		return engine.getPaloServerModel();
	}

	public IUserMessageQueue getUserMessageQueue() {
		return engine.getUserMessageQueue();
	}

	public void logout() {
		engine.logout();
	}

	public void removeAuthenticateListener(IAuthListener authListener) {
		engine.removeAuthenticateListener(authListener);

	}

	public void removeErrorListener(IErrorListener errorListener) {
		engine.removeErrorListener(errorListener);

	}

	public void removeRequestListener(IRequestListener listener) {
		engine.removeRequestListener(listener);
	}

}
