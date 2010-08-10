package com.tensegrity.palowebviewer.modules.widgets.client.util;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Helper class to work with GUI.
 *
 */
public class GuiHelper {
	
	public static void centerShowDialog(DialogBox dialog){
		dialog.show();
		int popupX = RootPanel.get().getOffsetWidth()/2 - dialog.getOffsetWidth()/2;
		int popupY = RootPanel.get().getOffsetHeight()/2 - dialog.getOffsetHeight()/2;
		dialog.setPopupPosition(popupX, popupY);
	}
	
}
