package com.tensegrity.palowebviewer.modules.ui.client.favoriteviews;

import com.tensegrity.palowebviewer.modules.paloclient.client.XFavoriteNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XFolder;
import com.tensegrity.palowebviewer.modules.paloclient.client.XViewLink;
import com.tensegrity.palowebviewer.modules.ui.client.favoriteviews.FavoriteViewsModel.FavoriteNode;

public class StructureCreator {
	private final FavoriteViewsModel model;

	public StructureCreator(FavoriteViewsModel model) {
		if(model == null) {
			throw new IllegalArgumentException("Model can not be null.");
		}
		this.model = model;
	}

	public FavoriteNode createStructure(XFavoriteNode xNode) {
		FavoriteNode result = null;
		if(xNode == null){
			throw new IllegalArgumentException("XNode can not be null.");
		}
		else if(xNode instanceof XFolder) {
			XFolder folder = (XFolder)xNode;
			result = createFolderNode(folder);
		}
		else if(xNode instanceof XViewLink) {
			result = createLinkNode(xNode);
		}
		else {
			throw new IllegalArgumentException("Unknown type of xNode: "+xNode);
		}
		return result;
		
	}

	private ViewLink createLinkNode(XFavoriteNode xNode) {
		return new ViewLink((XViewLink)xNode, model);
	}

	private FavoriteFolder createFolderNode(XFolder xFolder) {
		FavoriteFolder folder = xFolder.isConnection() ? new FavoriteConnectionFolder(xFolder, model): new FavoriteFolder(xFolder, model);
		int size = xFolder.getChildCount();
		for(int i = 0; i < size; i++) {
			XFavoriteNode xNode = xFolder.getChild(i);
			FavoriteNode node = createStructure(xNode);
			folder.addChild(node);
		}
		return folder;
	}

}