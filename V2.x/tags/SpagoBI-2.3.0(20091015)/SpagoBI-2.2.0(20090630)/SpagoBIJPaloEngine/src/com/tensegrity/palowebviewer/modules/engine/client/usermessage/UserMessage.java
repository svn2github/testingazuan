package com.tensegrity.palowebviewer.modules.engine.client.usermessage;

public class UserMessage implements IUserMessage {
	
	
	private final String msg;
	private final IUserMessageType type;
	private final IUserCallback callback;
	
	
	public static IUserMessage createErrorMsg(String msg, IUserCallback callback) {
		return new UserMessage(msg, IUserMessageType.ERROR, callback);
	}
	
	public static IUserMessage createErrorMsg(String msg) {
		return createErrorMsg(msg, null);
	}
	
	public static IUserMessage createWarningMsg(String msg, IUserCallback callback) {
		return new UserMessage(msg, IUserMessageType.WARN, callback);
	}
	
	public static IUserMessage createWarningMsg(String msg) {
		return createWarningMsg(msg, null);
	}
	
	public UserMessage(String msg, IUserMessageType type) {
		this(msg, type, null);
	}

	public UserMessage(String msg, IUserMessageType type, IUserCallback callback) {
		this.msg = msg;
		this.type = type;
		this.callback = callback;
	}

	public IUserCallback getCallback() {
		return callback;
	}

	public String getMessage() {
		return msg;
	}

	public IUserMessageType getType() {
		return type;
	}

}
