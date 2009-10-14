package com.tensegrity.palowebviewer.modules.widgets.client.tab;

public interface TabPanelModelListener {
	
	public void onTabAdded(ITabElement tab);
	
	public boolean onBeforeTabClosed(ITabElement tab);
	
	public void onTabClosed(ITabElement tab);
	
	public boolean onBeforeTabSelected(ITabElement tab);

	public void onTabSelected(ITabElement tab);
	
	public void onTabTitleChanged(ITabElement tab);
	
	public void onTabIconChanged(ITabElement tab);
	
}