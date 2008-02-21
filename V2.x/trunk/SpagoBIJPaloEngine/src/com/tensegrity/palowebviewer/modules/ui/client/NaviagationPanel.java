package com.tensegrity.palowebviewer.modules.ui.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Navigation panel widget. Contains DBExplorer widget and FavoriteView panel. It maintain 
 *
 */
public class NaviagationPanel extends Composite {
	private final String DB_EXPLORER_TITLE = "<table><tr><td><img src='themes/default/img/database.gif'/></td><td>Database Explorer</td></tr></table>";
	private final String FAVORITE_VIEWS_TITLE = "<table><tr><td><img src='themes/default/img/view.gif'/></td><td>Favorite Views</td></tr></table>";
	
	private final StackPanel stackPanel;
	private Widget favoriteViews;
	private Widget dbExplorer;
	private boolean showDbExplorer;
	private boolean showFavoriteViews;
	private final List panelListners = new ArrayList();
	
	
	public void addNavigationPanelListener(INavigationPanelListener listener) {
		if(listener == null)
			throw new IllegalArgumentException("Listener can not be null.");
		this.panelListners.add(listener);
	}
	
	public NaviagationPanel() {
		stackPanel = new StackPanel();
		initWidget(stackPanel);
	}

	public void setDbExplorer(Widget dbExplorer) {
        AbsolutePanel absolutPanel = new AbsolutePanel();
        absolutPanel.setWidth("100%");
        absolutPanel.setHeight("100%");
        absolutPanel.add(dbExplorer, 0, 0);
		this.dbExplorer = absolutPanel;
		showDbExplorer();
	}

	public void setFavoriteViews(Widget favoriteViews) {
        AbsolutePanel absolutPanel = new AbsolutePanel();
        absolutPanel.setWidth("100%");
        absolutPanel.setHeight("100%");
        absolutPanel.add(favoriteViews, 0, 0);
		this.favoriteViews = absolutPanel;
		showFavoriteViews();
	}

	public boolean isShowDbExplorer() {
		return showDbExplorer;
	}

	public void setShowDbExplorer(boolean showDbExplorer) {
		this.showDbExplorer = showDbExplorer;
		showDbExplorer();
	}

	private void showDbExplorer() {
		if(dbExplorer != null){
			if(isShowDbExplorer()){
				int widgetIndex = stackPanel.getWidgetIndex(dbExplorer);
				if(widgetIndex <0){
					stackPanel.insert(dbExplorer, 0);
					widgetIndex = stackPanel.getWidgetIndex(dbExplorer);
					
					stackPanel.setStackText(widgetIndex, DB_EXPLORER_TITLE, true);
				}
			}
			else {
				stackPanel.remove(dbExplorer);
				showFirst();
			}
		}
		fireVisibilityChanged();
	}

	private void showFirst() {
		if (stackPanel.getWidgetCount() > 0) {
			stackPanel.showStack(0);
		}
	}

	public boolean isShowFavoriteViews() {
		return showFavoriteViews;
	}

	public void setShowFavoriteViews(boolean showFavoriteViews) {
		this.showFavoriteViews = showFavoriteViews;
		showFavoriteViews();
	}

	private void fireVisibilityChanged() {
		Object[] listeners = panelListners.toArray();
		for (int i = 0; i < listeners.length; i++) {
			((INavigationPanelListener)listeners[i]).onComponentVisibilityChanged();
		}
	}

	private void showFavoriteViews() {
		if(favoriteViews != null) {
			if(isShowFavoriteViews() ) {
				int widgetIndex = stackPanel.getWidgetIndex(favoriteViews);
				if(widgetIndex< 0){
					int index = stackPanel.getWidgetCount();
					stackPanel.insert(favoriteViews, index);
					widgetIndex = stackPanel.getWidgetIndex(favoriteViews);
					
					stackPanel.setStackText(widgetIndex, FAVORITE_VIEWS_TITLE, true);
				}
			}
			else {
				stackPanel.remove(favoriteViews);
				showFirst();
			}
		}
		fireVisibilityChanged();
	}

}
