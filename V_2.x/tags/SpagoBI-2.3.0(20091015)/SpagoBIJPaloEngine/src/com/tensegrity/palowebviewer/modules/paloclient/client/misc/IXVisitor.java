package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;

public interface IXVisitor {
	
	public boolean hasFinished();
	
	public void visit(XObject object);
	
}
