package com.tensegrity.palowebviewer.modules.widgets.client.tab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultTabPanelModel implements ITabPanelModel {
	
	private List exTabPanelModelListeners; 
	
	private final List tabs;
	
	private DefaultTabElement selectedTab;
	
	public DefaultTabPanelModel() {
		super();
		exTabPanelModelListeners = new ArrayList();
		tabs = new ArrayList();
	}
	
	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabPanelModel#add(com.tensegrity.palowebviewer.modules.widgets.client.tab.ExTab)
	 */
	public void add(ITabElement tab) {
		if ( tab == null )
			throw new IllegalArgumentException("Tab can't be null");
		if ( tabs.indexOf( tab.getWidget() ) != -1 )
			throw new IllegalStateException("This Tab already added");
		DefaultTabElement dTab = toDefaultTabElement(tab);
		tabs.add(tab);
		selectedTab = dTab;
		
		fireTabAdded(tab);
		fireTabSelected(tab);
	}

	private void fireTabAdded(ITabElement tab) {
		for (Iterator it = exTabPanelModelListeners.iterator(); it.hasNext(); ) {
			TabPanelModelListener listener = (TabPanelModelListener)it.next();
			listener.onTabAdded(tab);
		}
	}

	
	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabPanelModel#getTabs()
	 */
	public List getTabs() {
		return tabs;
	}

	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabPanelModel#getSeledTab()
	 */
	public ITabElement getSeledTab() {
		return selectedTab;
	}
	
	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabPanelModel#selectTab(com.tensegrity.palowebviewer.modules.widgets.client.tab.ExTab)
	 */
	public boolean selectTab( ITabElement tab ) {
		DefaultTabElement dTab = null;
		if ( tab != null )
			dTab = toDefaultTabElement(tab);
		
		boolean success = askListenersToSelect(tab);
		
		if ( success ) {
			selectedTab = dTab;
			fireTabSelected(tab);
		}
		
		return success;
	}

	private void fireTabSelected(ITabElement tab) {
		for (Iterator it = exTabPanelModelListeners.iterator(); it.hasNext(); ) {
			TabPanelModelListener listener = (TabPanelModelListener)it.next();
			listener.onTabSelected(tab);
		}
	}

	private boolean askListenersToSelect(ITabElement tab) {
		boolean success = true;
		for (Iterator it = exTabPanelModelListeners.iterator(); it.hasNext(); ) {
			TabPanelModelListener listener = (TabPanelModelListener)it.next();
			if ( listener.onBeforeTabSelected(tab) == false ) {
				success = false;
				break;
			}
		}
		return success;
	}
	
	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabPanelModel#closeTab(com.tensegrity.palowebviewer.modules.widgets.client.tab.ExTab)
	 */
	public void closeTab( final ITabElement tab ) {
		boolean success = askListenersToClose(tab);
		if ( success ) {
			tab.close(new ITabCloseCallback(){
				public void onClose() {
					forceClose(tab);
				}
			});
		}
	}
	
	public void forceClose(ITabElement tab) {
		boolean success = askListenersToClose(tab);
		if(success) {
			tabs.remove(tab);
			fireTabClose(tab);
			if ( getTabs().size() > 0 )
				selectTab((ITabElement)getTabs().get(getTabs().size()-1));
			else 
				selectTab(null);
		}
	}

	private void fireTabClose(ITabElement tab) {
		for (Iterator it = exTabPanelModelListeners.iterator(); it.hasNext(); ) {
			TabPanelModelListener listener = (TabPanelModelListener)it.next();
			listener.onTabClosed(tab);
		}
	}

	private boolean askListenersToClose(ITabElement tab) {
		boolean success = true;
		for (Iterator it = exTabPanelModelListeners.iterator(); it.hasNext(); ) {
			TabPanelModelListener listener = (TabPanelModelListener)it.next();
			if ( listener.onBeforeTabClosed(tab) == false ) {
				success = false;
				break;
			}
		}
		return success;
	}
	
	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabPanelModel#addExTabPanelModelListeners(com.tensegrity.palowebviewer.modules.widgets.client.tab.ExTabPanelModelListener)
	 */
	public void addTabPanelModelListeners(TabPanelModelListener listener) {
		exTabPanelModelListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see com.tensegrity.palowebviewer.modules.widgets.client.tab.ITabPanelModel#removeExTabPanelModelListeners(com.tensegrity.palowebviewer.modules.widgets.client.tab.ExTabPanelModelListener)
	 */
	public void removeTabPanelModelListeners(TabPanelModelListener listener) {
		exTabPanelModelListeners.remove(listener);
	}
	
	public static DefaultTabElement toDefaultTabElement(ITabElement tab) {
		if ( !(tab instanceof ITabElement) )
			throw new IllegalArgumentException("Tab is ont instance of ITabElement");
		return (DefaultTabElement)tab;
	}

	public void changeTitle(ITabElement tab) {
		for (Iterator it = exTabPanelModelListeners.iterator(); it.hasNext(); ) {
			TabPanelModelListener listener = (TabPanelModelListener)it.next();
			listener.onTabTitleChanged(tab);
		}
	}

	public void changeIcon(ITabElement tab) {
		for (Iterator it = exTabPanelModelListeners.iterator(); it.hasNext(); ) {
			TabPanelModelListener listener = (TabPanelModelListener)it.next();
			listener.onTabIconChanged(tab);
		}
	}

}