package com.tensegrity.palowebviewer.modules.ui.client;

import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserCallback;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessage;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageQueue;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessageType;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.ErrorDialog;
import com.tensegrity.palowebviewer.modules.ui.client.dialog.IErrorDialogCallback;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.TaskQueue;

public class UserMessageProcessor {

	private final IUserMessageQueue queue;
	private final UIManager uiManager;
	private final TaskQueue taskQueue = TaskQueue.getInstance();
	private final IUIManagerListener uiManagerListener = new IUIManagerListener () {

		public void onBusy() {
			
		}

		public void onFree() {
			startProcessing();
		}
		
	};
	private final ITask task = new ITask() {

		public void execute() {
			processNextMessage();
		}

		public String getName() {
			return "UserMessageProcessorTask";
		}
		
	};
	private boolean processing = false;

	
	public void startProcessing() {
		if(!isProcessing() && !this.queue.isEmpty() && !uiManager.isBusy()) {
			setProcessing(true);
			taskQueue.add(task);
		}
	}

	private void setProcessing(boolean value) {
		processing  = value;
		startProcessing();
	}

	private boolean isProcessing() {
		return processing;
	}

	public UserMessageProcessor(UIManager uiManager, IUserMessageQueue queue) {
		this.queue = queue;
		this.uiManager = uiManager;
		this.uiManager.addListener(uiManagerListener);
	}
	
	public void processNextMessage() {
		IUserMessage msg = queue.popMessage();
		if(msg != null) {
			IUserMessageType type = msg.getType();
			DialogCallback callback = new DialogCallback(msg);
			switch(type.getId()) {
			case IUserMessageType.ERROR_ID: {
				ErrorDialog.showError(msg.getMessage(), callback);
				break;
			}
			case IUserMessageType.WARN_ID: {
				ErrorDialog.showError(msg.getMessage(), callback);
				break;
			}
			default: {
				Logger.warn("Unknown message type '"+type.getName()+"'");
				break;
			}
			}
		}
	}
	
	private class DialogCallback implements IErrorDialogCallback {
		
		private final IUserMessage message;

		public DialogCallback(IUserMessage msg) {
			if(msg == null)
				throw new IllegalArgumentException("Message can not be null.");
			this.message = msg;
		}

		public void onClose() {
			try {
				IUserCallback callback = message.getCallback();
				if(callback != null)
					callback.execute();
			}
			finally {
				setProcessing(false);				
			}
		}
		
	}

}

