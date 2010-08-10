package com.tensegrity.palowebviewer.modules.engine.client;

import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageQueue;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class EngineLogger extends ProxyEngine {
	
	public void addAuthenticateListener(IAuthListener authListener) {
		debug("addAuthenticateListener");
		super.addAuthenticateListener(authListener);
	}

	public void addErrorListener(IErrorListener errorListener) {
		debug("addErrorListener");
		super.addErrorListener(errorListener);
	}

	public void addRequestListener(IRequestListener listener) {
		debug("addRequestListener");
		super.addRequestListener(listener);
	}

	public void authenticate() {
		debug("authenticate()");
		super.authenticate();
	}

	public void authenticate(String login, String password, boolean remember) {
		debug("authenticate(login='"+login+"', password='"+password+"', remember="+ remember+")");
		super.authenticate(login, password, remember);
	}

	public IClientProperties getClientProperties() {
		return super.getClientProperties();
	}

	public IPaloServerModel getPaloServerModel() {
		return super.getPaloServerModel();
	}

	public IUserMessageQueue getUserMessageQueue() {
		return super.getUserMessageQueue();
	}

	public void logout() {
		debug("logout");
		super.logout();
	}

	public void removeAuthenticateListener(IAuthListener authListener) {
		debug("removeAuthenticateListener");
		super.removeAuthenticateListener(authListener);
	}

	public void removeErrorListener(IErrorListener errorListener) {
		debug("removeErrorListener");
		super.removeErrorListener(errorListener);
	}

	public void removeRequestListener(IRequestListener listener) {
		debug("removeRequestListener");
		super.removeRequestListener(listener);
	}

	public EngineLogger(IEngine engine) {
		super(engine);
		engine.addAuthenticateListener(authListener);
		engine.addRequestListener(recquestListener);
		engine.addErrorListener(errorListener);
	}

	private void debug(String message) {
		Logger.debug("[Engine] " + message);
	}
	
	private final IAuthListener authListener = new IAuthListener() {

		public void onAuthFailed(String cause) {
			debug("onAuthFailed('"+cause+"')");
		}

		public void onAuthSuccess() {
			debug("onAuthSuccess");
			
		}

		public void onLogoff() {
			debug("onLogoff");			
		}
		
	};
	
	private final IErrorListener errorListener = new IErrorListener() {

		public void onError(Throwable caught) {
			debug("onError(" + caught + ")");
		} 
		
	};
	
	private final IRequestListener recquestListener = new IRequestListener() {

		public void afterRecieve() {
			debug("afterRecieve");
		}

		public void beforeSend() {
			debug("beforeSend");
		}
		
	};

}
