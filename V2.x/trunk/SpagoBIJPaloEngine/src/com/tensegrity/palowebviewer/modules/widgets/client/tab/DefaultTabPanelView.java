package com.tensegrity.palowebviewer.modules.widgets.client.tab;

import java.util.Iterator;

import com.google.gwt.user.client.ui.Composite;

public class DefaultTabPanelView extends Composite {
	
	private GTabPanel tabPanel;
	
	private ITabPanelModel model;
	
	public DefaultTabPanelView() {
		tabPanel = new GTabPanel();
		tabPanel.addTabListener(tabPanelListener);
		initWidget(tabPanel);
	}
	
	/**
	 *  set tab model
	 * 
	 * @param model 
	 */
	public void setTabPanelModel(ITabPanelModel model) {
		// clean
		clean();	
		// init tabs with new model
		this.model = model;
		if ( this.model == null )
			return;
		addTabs();
		this.model.addTabPanelModelListeners(modelListener);
	}

	private void addTabs() {
		for ( Iterator it = this.model.getTabs().iterator(); it.hasNext(); ) {
			DefaultTabElement tab = (DefaultTabElement)it.next();
			if ( tab.getWidget() == null)
				throw new IllegalArgumentException("Widget is null");
			tabPanel.add( tab, tab.equals(this.model.getSeledTab()) );
		}
	}

	private void clean() {
		if ( this.model != null ) {
			this.model.removeTabPanelModelListeners(modelListener);
			
			for ( Iterator it = tabPanel.iterator(); it.hasNext(); ) {
				it.remove();
			}
		
			for ( Iterator it = this.model.getTabs().iterator(); it.hasNext(); ) {
				ITabElement tab = (ITabElement)it.next();
				tabPanel.remove(tab);
			}
		}
	}
	
    //-- Internal classes section --	
	
	private TabPanelModelListener modelListener = new TabPanelModelListener() {

		public boolean onBeforeTabClosed(ITabElement tab) {
			// ignore
			return true;
		}

		public boolean onBeforeTabSelected(ITabElement tab) {
			// ignore
			return true;
		}

		public void onTabAdded(ITabElement tab) {
			tabPanel.add( tab, tab.equals(model.getSeledTab()) );
		}

		public void onTabClosed(ITabElement tab) {
			tabPanel.remove(tab);
		}

		public void onTabSelected(ITabElement tab) {
			tabPanel.selectTab(tab);
		}

		public void onTabTitleChanged(ITabElement tab) {
			tabPanel.changeTitleTab(tab);
		}

		public void onTabIconChanged(ITabElement tab) {
			tabPanel.changeIconTab(tab); 
		}

	};
	
	private TabListener tabPanelListener = new TabListener() {

		public void onTabClose(ITabElement tab, int tabIndex) {
			model.closeTab(tab);
		}

		public void onTabSelect(ITabElement tab, int tabIndex) {
			model.selectTab(tab);
		}

	};
	
}
