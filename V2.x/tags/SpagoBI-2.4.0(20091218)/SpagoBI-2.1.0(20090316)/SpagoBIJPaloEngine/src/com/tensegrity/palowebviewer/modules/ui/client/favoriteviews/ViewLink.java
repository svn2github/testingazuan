/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.favoriteviews;

import com.tensegrity.palowebviewer.modules.paloclient.client.XViewLink;
import com.tensegrity.palowebviewer.modules.ui.client.favoriteviews.FavoriteViewsModel.FavoriteNode;

public class ViewLink extends FavoriteNode {
	
	public ViewLink(XViewLink link, FavoriteViewsModel model) {
		super(link, model);
	}
	
	public boolean isLeaf() {
		return true;
	}

	public XViewLink getLink() {
		return (XViewLink)getFavoriteNode();
	}
	
}