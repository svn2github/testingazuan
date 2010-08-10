package com.tensegrity.palowebviewer.modules.ui.client.messageacceptors;

import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IMessageAcceptor;
import com.tensegrity.palowebviewer.modules.engine.client.usermessage.IUserMessage;
import com.tensegrity.palowebviewer.modules.ui.client.dimensions.InvalidExpandedElementMessage;

public class MissingExpandedElementAcceptor implements IMessageAcceptor {
	
	private boolean enable = false;

	public MissingExpandedElementAcceptor(boolean enable){
		this.enable = enable;
	}
	
	public boolean accept(IUserMessage msg) {
		boolean r = true;
		if(enable){
			r = ! (msg instanceof InvalidExpandedElementMessage);
		}
		return r;
	}

}
