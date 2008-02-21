package com.tensegrity.palowebviewer.modules.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.engine.client.l10n.PaloLocalizedStrings;
import com.tensegrity.palowebviewer.modules.widgets.client.ActionImage;
import com.tensegrity.palowebviewer.modules.widgets.client.TreeView;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;
import com.tensegrity.palowebviewer.modules.widgets.client.tab.DefaultTabPanelView;
import com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabPanelModel;

/**
 * Main frame widget for the applications.Consolidtates all other widgets.
 *
 */
public class MainFrame extends SimplePanel {
	
	private static String NAV_PANEL_ENABLED = "themes/default/img/nav-panel-enabled.gif";
	private static String NAV_PANEL_DISABLED = "themes/default/img/nav-panel-disabled.gif";
	
	private ActionImage btLogin;
	private ActionImage btLogout;
	private ActionImage btReloadTree;
	private ActionImage btSave;
	private ActionImage btSaveAs;
	private ActionImage btShowNavigationPanel;
	
	TreeView treeView;
	
	ITabPanelModel tabPanelModel;
	DefaultTabPanelView tabPanelView;
	NaviagationPanel navigationPanel;
	
	ScrollPanel treeScrollPanel;
	ScrollPanel treePanel;
	
	PaloLocalizedStrings localizedStrings;
	private ShowNavigationPanelLogic navigationPanelLogic;
	private TreeView faovoriteViewsTree;
	private ScrollPanel favoriteViewsPanel;
	
    //TODO get rid of all those actions in parameter list
	public MainFrame(IAction loginAction, IAction logoutAction, IAction reloadTreeAction, IAction saveAction, IAction saveAsAction) {
		
		localizedStrings = (PaloLocalizedStrings)GWT.create(PaloLocalizedStrings.class);
		
		btLogin = createActionImage(loginAction);
		btLogout = createActionImage(logoutAction);
		btReloadTree = createActionImage(reloadTreeAction);
		btSave = createActionImage(saveAction);
		btSaveAs = createActionImage(saveAsAction);
		btShowNavigationPanel = createActionImage(null);
		
		buildWidgets();
	}
	
	public TreeView getTreeView() {
		return treeView;
	}
	
	public void setTabPanelModel(ITabPanelModel tabPanelModel) {
		tabPanelView.setTabPanelModel(tabPanelModel);
	}
	
	private void buildWidgets() {
		//TabPanel tabPanel = new TabPanel();
		tabPanelView = new DefaultTabPanelView();
		tabPanelView.setWidth("100%");
		tabPanelView.setHeight("100%");
		
		navigationPanel = new NaviagationPanel();

		FlexTable mainTable = new FlexTable();
		mainTable.setBorderWidth(0);
		mainTable.setCellPadding(0);
		mainTable.setCellSpacing(0);
		
		treeView = new TreeView();
        treePanel = new ScrollPanel();
        treePanel.setStyleName("scroll");
        treePanel.setWidth("100%");
        treePanel.setHeight("100%");
        treePanel.add(treeView);
        
        favoriteViewsPanel = new ScrollPanel();
        faovoriteViewsTree = new TreeView();
        favoriteViewsPanel.setStyleName("scroll");
        favoriteViewsPanel.setWidth("100%");
        favoriteViewsPanel.setHeight("100%");
        favoriteViewsPanel.add(faovoriteViewsTree);


        navigationPanel.setDbExplorer(treePanel);
        navigationPanel.setFavoriteViews(favoriteViewsPanel);
        navigationPanel.setWidth("100%");
        navigationPanel.setHeight("100%");
        navigationPanel.setShowDbExplorer(true);
        navigationPanel.setShowFavoriteViews(true);

      
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		splitPanel.setLeftWidget(navigationPanel);
		splitPanel.setRightWidget(tabPanelView);
		splitPanel.setSplitPosition("200px");

		navigationPanelLogic = new ShowNavigationPanelLogic(navigationPanel, splitPanel);
		navigationPanelLogic.setLogicListener(navigationPanelLogicListener);
		navigationPanelLogic.setShowNavigationPanel(false);
		
		IAction navPanelAction = navigationPanelLogic.getShowAction();
		btShowNavigationPanel.setAction(navPanelAction);
		
		
		mainTable.setWidget(0, 0, createToolBar());
	    mainTable.setWidget(1, 0, splitPanel);
	    
	    mainTable.getFlexCellFormatter().setColSpan(0, 0, 3);
	    mainTable.getFlexCellFormatter().setHeight(0, 0, "10px");
	    
//	    mainTable.getFlexCellFormatter().setColSpan(1, 0, 3);
//	    mainTable.getFlexCellFormatter().setHeight(1, 0, "8px");
//	    
//	    mainTable.getFlexCellFormatter().setColSpan(2, 0, 3);
//	    mainTable.getFlexCellFormatter().setHeight(2, 0, "8px");
	    
	    mainTable.getFlexCellFormatter().setColSpan(1, 0, 3);
	    
	    mainTable.setBorderWidth(0);
	    mainTable.setSize("100%", "100%");
	    
	    mainTable.setStyleName("main_table");
	    
	    this.setSize("100%", "100%");
	    this.add(mainTable);
	}
	
	public void setCanShowDbExplorer(boolean value) {
		navigationPanel.setShowDbExplorer(value);
	}
	
	public void setCanShowFavoriteViews(boolean value) {
		navigationPanel.setShowFavoriteViews(value);
	}
	
	private Widget createToolBar() {
		HorizontalPanel toolBar = new HorizontalPanel();
		toolBar.setSpacing(3);
		toolBar.setStyleName("view_panel");

		btLogin.setEnabledURL("themes/default/img/login_24.gif");
		btLogin.setWidth("13");
		btLogin.setHeight("15");
		btLogin.setDisabledURL("themes/default/img/login_24_dis.gif");
		btLogin.setTitle(localizedStrings.actionLoginHint());
		toolBar.add(btLogin);
		
		btLogout.setEnabledURL("themes/default/img/logout_24.gif");
		btLogout.setWidth("12");
		btLogout.setHeight("15");
		btLogout.setDisabledURL("themes/default/img/logout_24_dis.gif");
		btLogout.setTitle(localizedStrings.actionLogoutHint());
		toolBar.add(btLogout);
		
		btReloadTree.setEnabledURL("themes/default/img/reload_24.gif");
		btReloadTree.setWidth("14");
		btReloadTree.setHeight("15");
		btReloadTree.setDisabledURL("themes/default/img/reload_24_dis.gif");
		btReloadTree.setTitle(localizedStrings.actionReloadTreeHint());
		toolBar.add(btReloadTree);

		btSave.setEnabledURL("themes/default/img/save_24.gif");
		btSave.setWidth("15");
		btSave.setHeight("15");
		btSave.setDisabledURL("themes/default/img/save_24_dis.gif");
		btSave.setTitle(localizedStrings.actionSaveHint());
		toolBar.add(btSave);

		btSaveAs.setEnabledURL("themes/default/img/save-as_24.gif");
		btSaveAs.setWidth("15");
		btSaveAs.setHeight("15");
		btSaveAs.setDisabledURL("themes/default/img/save-as_24_dis.gif");
		btSaveAs.setTitle(localizedStrings.actionSaveAsHint());
		toolBar.add(btSaveAs);
		
		btShowNavigationPanel.setEnabledURL(NAV_PANEL_ENABLED);
		btShowNavigationPanel.setWidth("15");
		btShowNavigationPanel.setHeight("15");
		changeNavPanelActionIcon();
		toolBar.add(btShowNavigationPanel);
		
		HTML rest = new HTML("&nbsp");
		toolBar.add(rest);
		toolBar.setCellWidth(rest, "100%");
		
		return toolBar;
	}
	
	private void changeNavPanelActionIcon() {
		String iconURL = navigationPanel.isVisible() ? NAV_PANEL_ENABLED : NAV_PANEL_DISABLED;
		btShowNavigationPanel.setEnabledURL(iconURL);
	}
	
	private ActionImage createActionImage(IAction action) {
		ActionImage ai = new ActionImage();
		ai.setAction(action);
		return ai;
	}

	public void setNavigationPanelWidth(int width) {
		navigationPanelLogic.setNavigationPanelWidth(width);
	}

	public void setShowNavigationPanel(boolean value) {
		navigationPanelLogic.setShowNavigationPanel(value);
		
	}

	public TreeView getFavoriteViewsView() {
		return faovoriteViewsTree;
	}
	
	private final IShowNavigationPanelLogicListner navigationPanelLogicListener = new IShowNavigationPanelLogicListner () {

		public void onPanelStatePChanged() {
			changeNavPanelActionIcon();
		}
		
	};

	public void expandFavoriteConnections() {
		TreeView view = getFavoriteViewsView();
		final int size = view.getItemCount();
		for(int i = 0; i< size; i++) {
			 TreeItem item = view.getItem(i);
			 item.setState(true);
		}
	}
	
}
