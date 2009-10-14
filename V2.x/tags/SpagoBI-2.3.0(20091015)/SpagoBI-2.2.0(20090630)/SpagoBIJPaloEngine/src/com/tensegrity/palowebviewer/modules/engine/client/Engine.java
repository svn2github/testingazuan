package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageQueue;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.UserMessageQueue;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

/**
 * Implements {@link IEngine} 
 */
public class Engine implements IEngine{

    private EngineServiceAsyncDelegator engineServiceDelegator;
    private IPaloServerModel paloServerModel;
    private IClientProperties clientProperties;

    private ArrayList authListeners;
    private ArrayList errorListeners;

    private AuthAsyncCallback authAsyncCallback = new AuthAsyncCallback(null);
    private AsyncCallback authAsyncCallbackWithPW = new AuthAsyncCallback("Invalid login or password");
    
    private final IUserMessageQueue userMsgQueue = new UserMessageQueue();

    /**
     * Constructs {@link  Engine}
     */
    public Engine() {
    	new UserMessageQueueLogger(userMsgQueue);
        IEngineServiceAsync nativeService = (IEngineServiceAsync) GWT.create(IEngineService.class);

        ServiceDefTarget endPoint = (ServiceDefTarget)nativeService;
        String moduleRelativeURL = GWT.getModuleBaseURL() + "engine";
        endPoint.setServiceEntryPoint(moduleRelativeURL);
        if(Logger.isOn()) {
        	nativeService = new EngineServiceAsyncLogger(nativeService);
        }
        engineServiceDelegator = new EngineServiceAsyncDelegator(nativeService, userMsgQueue);
        
        PaloServerModel serverModel = new PaloServerModel(engineServiceDelegator);
        serverModel.setUserMessageQueue(getUserMessageQueue());
		paloServerModel = serverModel;
        if(Logger.isOn()) {
        	paloServerModel = new PaloServerModelLogger(paloServerModel);
        }

        paloServerModel.addListener(paloServerModelListener);
        authListeners = new ArrayList();
        errorListeners = new ArrayList();

    }


    /**
     * {@inheritDoc}
     */    
    public void addAuthenticateListener(IAuthListener authListener) {
        authListeners.add(authListener);
    }

    /**
     * {@inheritDoc}
     */    
    public void removeAuthenticateListener(IAuthListener authListener) {
        authListeners.remove(authListener);
    }

    /**
     * {@inheritDoc}
     */    
    public void addErrorListener(IErrorListener errorListener) {
        errorListeners.add(errorListener);
    }

    /**
     * {@inheritDoc}
     */    
    public void removeErrorListener(IErrorListener errorListener) {
        errorListeners.remove(errorListener);
    }

    /**
     * {@inheritDoc}
     */    
    public IPaloServerModel getPaloServerModel() {
        return paloServerModel;
    }

    /**
     * {@inheritDoc}
     */    
    public void authenticate() {
        engineServiceDelegator.isAuthenticated( authAsyncCallback );
    }

    /**
     * {@inheritDoc}
     */    
    public void authenticate(String login, String password, boolean remember) {
        engineServiceDelegator.authenticate( login, password, remember, authAsyncCallbackWithPW );
    }

    //-- Event fireing

    /**
     * Notifies listener that there was an error.
     * @param caught - an error that occurred.
     */
    protected void fireOnError(Throwable caught) {
        for (Iterator it = errorListeners.iterator(); it.hasNext(); ) {
            IErrorListener errorListener = (IErrorListener)it.next();
            errorListener.onError(caught);
        }
    }

    /**
     * Notifies listener that login operation succeeded
     */
    protected void fireLoginSuccess() {
        for (Iterator it = authListeners.iterator(); it.hasNext(); ) {
            IAuthListener authListener = (IAuthListener)it.next();
            authListener.onAuthSuccess();
        }
    }
    
    /**
     * Notifies listener that log off operation succeeded.
     */
    protected void fireLogoff() {
        for (Iterator it = authListeners.iterator(); it.hasNext(); ) {
            IAuthListener authListener = (IAuthListener)it.next();
            authListener.onLogoff();
        }
    }

    /**
     * Notifies listener that login operation failed.
     *
     * @param message - reason why login failed.
     */
    protected void fireLoginFailed(String message) {
        for (Iterator it = authListeners.iterator(); it.hasNext(); ) {
            IAuthListener authListener = (IAuthListener)it.next();
            authListener.onAuthFailed(message);
        }
    }

	public void logout() {
		engineServiceDelegator.logoff(logoffAsyncCallback);
	}


	private void loginAction(){
        getPaloServerModel().turnOn();
        fireLoginSuccess();
	}
	
	public IClientProperties getClientProperties() {
		return clientProperties;
	}

	public void addRequestListener(IRequestListener listener){
		engineServiceDelegator.addListener(listener);
	}
	
	public void removeRequestListener(IRequestListener listener){
		engineServiceDelegator.removeListener(listener);
	}
	
	public IUserMessageQueue getUserMessageQueue() {
		return userMsgQueue;
	}
	
	
    //-- Internal classes section --

    private class AuthAsyncCallback implements AsyncCallback {

        private String authFailedMessage = null; 

        public AuthAsyncCallback(String authFailedMessage){
            this.authFailedMessage = authFailedMessage;
        }

        public void onFailure(Throwable caught) {
            fireOnError(caught);
        }

        public void onSuccess(Object result) {
            if ( result instanceof Boolean ) {
                if  ( ((Boolean)result).booleanValue() ) {
                	new LoadConficurationCallback(new ReloadOnLoginCallback()).sendRequest();
                } 
                else {
                    getPaloServerModel().turnOff();
                    fireLoginFailed(authFailedMessage);
                }
            } 
            else {
                Exception exception = new Exception("Internal error. Not instance of a Boolean");
                fireOnError(exception);
            }
        }

    };

    private AsyncCallback logoffAsyncCallback = new AsyncCallback() {

        public void onFailure(Throwable caught) {
            Exception exception = new Exception("Internal error while trying to logoff");
            fireOnError(exception);
        }

        public void onSuccess(Object result) {
        	fireLogoff();
            getPaloServerModel().turnOff();
        }

    };
    
    


    private class ReloadOnLoginCallback implements AsyncCallback {

		public void onFailure(Throwable caught) {
           fireOnError(caught);
		}

		public void onSuccess(Object result) {
			if(getClientProperties().reloadOnLogin()){
				engineServiceDelegator.forceReload(new ForceReloadCallback());
			}else{
				loginAction();
			}
		}
    }
    
    private class ForceReloadCallback implements AsyncCallback{

		public void onFailure(Throwable caught) {
			fireOnError(caught);
		}

		public void onSuccess(Object result) {
			loginAction();
		}
    }
    
    private IPaloServerModelListener paloServerModelListener = new AbstractServerModelListener() {

		public void onError(Throwable cause) {
			fireOnError(cause);
		}
    		
    };

    private class LoadConficurationCallback implements AsyncCallback {
    	
    	private final AsyncCallback callback;
    	
    	public LoadConficurationCallback(AsyncCallback callback) {
    		this.callback = callback;
    	}
    	
    	public void sendRequest() {
    		engineServiceDelegator.getClientProperties(this);
    	}

		public void onFailure(Throwable caught) {
	        Logger.error("fail to load configuration");
		}

		public void onSuccess(Object result) {
			clientProperties = (IClientProperties)result;
			callback.onSuccess(result);
		}
    	
    }


    

}
