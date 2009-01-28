package com.tensegrity.palowebviewer.modules.engine.client.usermessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MessageFilter{
	
	private List acceptors = new ArrayList();
	
	public void addAcceptor(IMessageAcceptor acceptor){
		if(acceptor == null) throw new IllegalArgumentException("acceptor can't be null");
		acceptors.add(acceptor);
	}
	
	public void removeAcceptor(IMessageAcceptor acceptor){
		acceptors.remove(acceptor);
	}
	
	public  boolean acceptMessage(IUserMessage msg){
		boolean r = true;
		for (Iterator it = acceptors.iterator(); it.hasNext() && r;) {
			IMessageAcceptor acceptor = (IMessageAcceptor) it.next();
			r = acceptor.accept(msg);
		}
		return r;
	}
	

}
