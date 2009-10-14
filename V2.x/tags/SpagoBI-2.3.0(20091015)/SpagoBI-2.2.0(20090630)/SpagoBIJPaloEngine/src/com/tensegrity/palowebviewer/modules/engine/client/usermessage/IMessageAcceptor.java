package com.tensegrity.palowebviewer.modules.engine.client.usermessage;

public interface IMessageAcceptor {
	/*
	 * returns true, if message is accepted (should be shown to user) 
	 */
	public boolean accept(IUserMessage msg);
}
