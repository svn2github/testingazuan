package com.tensegrity.palowebviewer.modules.ui.client.favoriteviews;

import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.widgets.client.LabelWidgetFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.LabeledImage;

public class FavoriteViewsWidgetFactory extends LabelWidgetFactory {

	public Widget createWidgetFor(Object object) {
		Widget result = null;
		if(object instanceof FavoriteConnectionFolder) {
			FavoriteConnectionFolder folder = (FavoriteConnectionFolder)object;
			result =  new LabeledImage("favoriteviews-connection-folder", folder.getName());
		}
		else if(object instanceof FavoriteFolder) {
			FavoriteFolder folder = (FavoriteFolder)object;
			result =  new LabeledImage("favoriteviews-folder", folder.getName());
		}
		else if(object instanceof ViewLink) {
			ViewLink link = (ViewLink)object;
			result =  new LabeledImage("favoriteviews-view-link", link.getName());
		}
		else {
			result = super.createWidgetFor(object);
		}
		return result;
	}

}
