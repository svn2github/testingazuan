package com.tensegrity.palowebviewer.modules.widgets.client.tab;

import java.util.List;

public interface ITabPanelModel {

	/**
	 * Adds a tab to the tab panel.
	 * 
	 * @param TabElement the tab to be added
	 */
	public abstract void add(ITabElement tab);

	/**
	 * Get all tabs
	 * 
	 * @return tabs list
	 */
	public abstract List getTabs();

	/**
	 * @return selected tab
	 */
	public abstract ITabElement getSeledTab();

	/**
	 * Selects a tab
	 * 
	 * @param tab Tab to bee selected
	 * @return is operation success
	 */
	public abstract boolean selectTab(ITabElement tab);

	/**
	 * Close a tab
	 * 
	 * @param tab Tab to bee closed
	 */
	public void closeTab(ITabElement tab);
	
	public void forceClose(ITabElement tab);
	
	public void changeTitle(ITabElement tab);
	
	public void changeIcon(ITabElement tab);

	public abstract void addTabPanelModelListeners(
			TabPanelModelListener listener);

	public abstract void removeTabPanelModelListeners(
			TabPanelModelListener listener);

}