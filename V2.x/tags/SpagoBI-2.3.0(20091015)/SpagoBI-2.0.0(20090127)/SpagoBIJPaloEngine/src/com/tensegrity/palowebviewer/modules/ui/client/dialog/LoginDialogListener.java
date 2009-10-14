package com.tensegrity.palowebviewer.modules.ui.client.dialog;

public interface LoginDialogListener {

	//TODO: doc me.
	public void onOk(String login, String password, boolean remember);
	
	//TODO: doc me.
	public void onCancel();
	
}