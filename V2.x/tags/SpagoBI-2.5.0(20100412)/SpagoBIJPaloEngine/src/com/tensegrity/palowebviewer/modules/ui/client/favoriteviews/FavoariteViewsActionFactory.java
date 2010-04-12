package com.tensegrity.palowebviewer.modules.ui.client.favoriteviews;

import com.tensegrity.palowebviewer.modules.ui.client.UIManager;
import com.tensegrity.palowebviewer.modules.widgets.client.IActionFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.AbstractAction;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;

public class FavoariteViewsActionFactory implements IActionFactory {
	
	private final UIManager uiManager;

	public FavoariteViewsActionFactory(UIManager uiManager) {
		if(uiManager == null)
			throw new IllegalArgumentException("UIManager can not be null.");
		this.uiManager = uiManager;
	
	}

	public IAction getActionFor(Object object) {
		IAction result = null;
		if(object instanceof ViewLink) {
			ViewLink link = (ViewLink)object;
			result = new OpenViewAction(link, uiManager);
		}
		return result;
	}

}


class OpenViewAction extends AbstractAction {
	
	private final ViewLink link;
	private final UIManager uiManager;

	public OpenViewAction(ViewLink link, UIManager uiManager) {
		if(link == null)
			throw new IllegalArgumentException("Link can not be null");
		this.link = link;
		this.uiManager = uiManager;
		setEnabled(true);
	}

	public void onActionPerformed(Object arg) {
		uiManager.openView(link);
	}

}