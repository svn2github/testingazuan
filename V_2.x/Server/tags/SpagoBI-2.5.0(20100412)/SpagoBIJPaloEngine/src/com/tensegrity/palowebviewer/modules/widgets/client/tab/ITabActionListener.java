package com.tensegrity.palowebviewer.modules.widgets.client.tab;

public interface ITabActionListener {

	/**
	 * Invoked when user tryes to close tab.
	 * @return true, if tab should be closed.
	 */
	public void tryClose(ITabCloseCallback callback);
}
