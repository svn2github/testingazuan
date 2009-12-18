package com.tensegrity.palowebviewer.modules.ui.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.tensegrity.palowebviewer.modules.util.client.JavaScript;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.AbstractAction;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;

public class ShowNavigationPanelLogic {
	
	private final NaviagationPanel navigationPanel;
	private final HorizontalSplitPanel splitPanel;
	private int splitPosition;
	private final INavigationPanelListener panelListner = new INavigationPanelListener() {

		public void onComponentVisibilityChanged() {
			tryToShowNavigationPanel();
		}
		
	};
	private IShowNavigationPanelLogicListner logicListener;

	public ShowNavigationPanelLogic(NaviagationPanel navigationPanel, HorizontalSplitPanel splitPanel) {
		this.navigationPanel = navigationPanel;
		this.splitPanel = splitPanel;
		navigationPanel.addNavigationPanelListener(panelListner);
		tryToShowNavigationPanel();
	}
	
	public void setLogicListener(IShowNavigationPanelLogicListner listener) {
		this.logicListener = listener;
	}
	
	private void toggleNavigationPanel() {
		boolean visible = navigationPanel.isVisible();
		if(visible){
			hideNavigationPanel();
		}
		else {
			showNavigationPanel();
		}

	}

	private void hideNavigationPanel() {
		if(navigationPanel.isVisible()) {
			splitPosition = splitPanel.getLeftWidget().getOffsetWidth();
			navigationPanel.setVisible(false);
			Element element = JavaScript.findElementWithStyle(splitPanel.getElement(), "hsplitter");
			DOM.setStyleAttribute(element, "visibility", "hidden");
//			splitPanel.setStyleName("navigation-hidden");
			splitPanel.setSplitPosition("0px");
			firePanelStateChanged();
		}
	}

	private void firePanelStateChanged() {
		if(logicListener != null)
			logicListener.onPanelStatePChanged();
	}

	private void showNavigationPanel() {
		if(!navigationPanel.isVisible()) {
			navigationPanel.setVisible(true);
			splitPanel.setSplitPosition(splitPosition+"px");
			Element element = JavaScript.findElementWithStyle(splitPanel.getElement(), "hsplitter");
			DOM.setStyleAttribute(element, "visibility", "visible");
//			splitPanel.setStyleName("navigation-visible");
			firePanelStateChanged();
		}
	}
	
	public IAction getShowAction() {
		return showHideAction;
	}
	
	public void setNavigationPanelWidth(int width) {
		if(navigationPanel.isVisible()) {
			splitPanel.setSplitPosition(width+"px");
		}
		else {
			splitPosition = width;
		}
	}

	public void setShowNavigationPanel(boolean value) {
		if(value) {
			tryToShowNavigationPanel();
		}
		else {
			hideNavigationPanel();
		}
	}

	private final IAction showHideAction = new AbstractAction() {
		
		public void onActionPerformed(Object arg) {
			toggleNavigationPanel();
		}

	};

	private void tryToShowNavigationPanel() {
		if(navigationPanel.isShowDbExplorer() || navigationPanel.isShowFavoriteViews()){
			showHideAction.setEnabled(true);
			showNavigationPanel();
		}
		else {
			hideNavigationPanel();
			showHideAction.setEnabled(false);
		}
	}

}
