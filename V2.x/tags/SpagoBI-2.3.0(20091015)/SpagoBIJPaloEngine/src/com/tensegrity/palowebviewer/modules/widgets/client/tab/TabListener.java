package com.tensegrity.palowebviewer.modules.widgets.client.tab;


/**
 * Event listener interface for tab events
 */

public interface TabListener {

	  public void onTabClose(ITabElement tab, int tabIndex);

	  public void onTabSelect(ITabElement tab, int tabIndex);

}

