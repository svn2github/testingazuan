package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageQueue;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XViewPath;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class EngineServiceAsyncDelegator implements IEngineServiceAsync {
	
	private final IEngineServiceAsync service;
	private final List listeners = new ArrayList();
	private final IUserMessageQueue messageQueue;
	private final List callsQueue = new ArrayList();
	private int invocations = 0;
	private final int PARALLEL_CALLS = 1;

	public EngineServiceAsyncDelegator(IEngineServiceAsync service, IUserMessageQueue messageQueue){
		this.service = service;
		this.messageQueue = messageQueue;
	}

	/* public methods */
	public void addListener(IRequestListener listener){
		if(listener == null) 
			throw new IllegalArgumentException("lisntener can't be null");
		listeners.add(listener);
	}
	
	public void removeListener(IRequestListener listener){
		listeners.remove(listener);
	}
	
	private void fireBeforeSend(){
		for (Iterator it = listeners.iterator(); it.hasNext();) {
			IRequestListener listener = (IRequestListener) it.next();
			listener.beforeSend();
		}
	}

	private void fireAfterReceive(){
		for (Iterator it = listeners.iterator(); it.hasNext();) {
			IRequestListener listener = (IRequestListener) it.next();
			listener.afterRecieve();
		}
	}
	
	private void queueCall(AsyncCaller caller){
		callsQueue.add(caller);
		tryMakeCall();
	}
	
	private void tryMakeCall(){
		if((invocations < PARALLEL_CALLS) && !callsQueue.isEmpty()){
			++invocations;
			if(invocations > 1){
				Logger.warn("doing parallel call #" + invocations);
			}
			AsyncCaller caller = (AsyncCaller) callsQueue.remove(0);
			caller.invoke();
		}
	}
	
	private void onCallReturn(){
		--invocations;
		tryMakeCall();
	}
	
	/* wrapped methods */
	
	public void authenticate(final String login, final String password, final boolean remember,
			AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.authenticate(login, password, remember, callback);
			}
		});
		
	}

	public void forceReload(AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.forceReload(callback);
			}
		});
	}

	public void getClientProperties(AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.getClientProperties(callback);
			}
		});
	}

	public void isAuthenticated(AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.isAuthenticated(callback);
			}
		});
	}

	public void logoff(AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.logoff(callback);
			}
		});
	}

	public void query(final XQueryPath[] query, AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.query(query, callback);
			}
		});
	}

	public void saveView(final XViewPath viewPath, AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.saveView(viewPath, callback);
			}
		});
	}

	public void updateData(final XPath cubePath,final  IXPoint point,final IResultElement value,
			AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.updateData(cubePath, point, value, callback);
			}
		});
	}

	public void loadChildren(final XPath path, final int type, AsyncCallback callback){
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.loadChildren(path, type, callback);
			}
		});
		
	}

	public void getParentsOf(final XPath contextPath, final String elementId, AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.getParentsOf(contextPath, elementId, callback);
			}
		});
	}

	public void loadDefaultView(final XPath path, AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.loadDefaultView(path, callback);
			}
		});
	}

	public void checkExistance(final XPath contextPath, final String elementId, AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.checkExistance(contextPath, elementId, callback);
			}
		});
	}

	public void  checkExistance(final XPath path, AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.checkExistance(path, callback);
			}
		});
	}

	public void loadFavoriteViews(AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.loadFavoriteViews(callback);
			}
		});
	}

	public void loadChild(final XPath path, final String childId, final int type, AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.loadChild(path, childId, type, callback);
			}
		});
	}

	public void loadChildByName(final XPath path, final String name, final  int type, AsyncCallback callback) {
		queueCall(new AsyncCaller(callback){
			protected void remoteIncoke(AsyncCallback callback) {
				service.loadChildByName(path, name, type, callback);
			}
		});
	}
	
	//private classes

	private class Delegator implements AsyncCallback{
		private AsyncCallback callback;
		
		public Delegator(AsyncCallback callback){
			this.callback = callback;
		}
		
		public void onFailure(Throwable caught) {
			onCallReturn();
			caught.printStackTrace();
			try {
				if(caught instanceof RuntimeException) {
					messageQueue.pushMessage(new ServiceCallFailMessage(caught));
				}
				callback.onFailure(caught);
			}
			finally {
				fireAfterReceive();
			}
		}

		public void onSuccess(Object result) {
			onCallReturn();
			try {
				callback.onSuccess(result);
			}
			finally {
				fireAfterReceive();
			}
		}
	}
	
	private abstract class AsyncCaller{
		
		private AsyncCallback callback;
		
		public AsyncCaller(AsyncCallback cb){
			this.callback = new Delegator(cb);
		}
		
		public void invoke(){
			fireBeforeSend();
			remoteIncoke(callback);
		}
		
		protected abstract void remoteIncoke(AsyncCallback callback);
	}

}
