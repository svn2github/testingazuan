/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.favoriteviews;

import com.tensegrity.palowebviewer.modules.paloclient.client.XFolder;
import com.tensegrity.palowebviewer.modules.ui.client.favoriteviews.FavoriteViewsModel.FavoriteNode;

public class FavoriteFolder extends FavoriteNode {

	public FavoriteFolder(XFolder folder, FavoriteViewsModel model) {
		super(folder, model);
	}
	
	public boolean isLeaf() {
		return getChildCount() == 0;
	}

}