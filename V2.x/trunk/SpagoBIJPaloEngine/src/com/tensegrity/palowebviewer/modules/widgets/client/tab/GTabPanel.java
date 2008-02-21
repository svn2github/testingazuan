package com.tensegrity.palowebviewer.modules.widgets.client.tab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.util.client.JavaScript;

/**
 * A panel that represents a tabbed set of pages, each of which contains another
 * widget. Its child widgets are shown as the user selects the various tabs
 * associated with them. The tabs can contain arbitrary HTML.
 * 
 * <p>
 * <img class='gallery' src='TabPanel.png'/>
 * </p>
 * 
 * <p>
 * Note that this widget is not a panel per se, but rather a
 * {@link com.google.gwt.user.client.ui.Composite} that aggregates a
 * {@link com.google.gwt.user.client.ui.TabBar} and a
 * {@link com.google.gwt.user.client.ui.DeckPanel}. It does, however, implement
 * {@link com.google.gwt.user.client.ui.HasWidgets}.
 * </p>
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.gwt-TabPanel { the tab panel itself }</li>
 * <li>.gwt-TabPanelBottom { the bottom section of the tab panel (the deck
 * containing the widget) }</li>
 * </ul>
 * 
 * <p>
 * <h3>Example</h3> {@example com.google.gwt.examples.TabPanelExample}
 * </p>
 */
public class GTabPanel extends Composite {

	private List children = new ArrayList();

	private DeckPanel deck = new DeckPanel();

	private GTabBar tabBar = new GTabBar();

	private List exTabListeners = new ArrayList();

	/**
	 * Creates an empty tab panel.
	 */
	public GTabPanel() {
		VerticalPanel panel = new VerticalPanel();
		panel.add(tabBar);
		panel.add(deck);

		panel.setCellHeight(deck, "100%");
		tabBar.setWidth("100%");

		tabBar.addTabListener(tabListener);
		initWidget(panel);
		setStyleName("tensegrity-gwt-TabPanel");
		deck.setStyleName("tensegrity-gwt-TabPanelBottom");
		deck.setWidth("100%");
		deck.setHeight("100%");
	}

	public void add(Widget w) {
		throw new UnsupportedOperationException(
				"A tabText parameter must be specified with add().");
	}
	
	
	
	public void add(ITabElement tab, boolean selected) {
		DefaultTabElement dTab = DefaultTabPanelModel.toDefaultTabElement(tab);
		
		if ( dTab.getWidget() == null )
			throw new IllegalArgumentException("Widget is null");
		
		children.add(dTab);
		tabBar.addTab(dTab.getTitle(), dTab.getImgURL(), dTab.isCloseable(), selected);
		deck.add( dTab.getWidget() );
		
		if ( selected )
			deck.showWidget(deck.getWidgetCount()-1);
	}

	public void addTabListener(TabListener listener) {
		exTabListeners.add(listener);
	}

	public ITabElement getTab(int index) {
		return (ITabElement)children.get(index);
	}

	public int getTabsCount() {
		return children.size();
	}

	public int getWidgetIndex(Widget widget) {
		return children.indexOf(widget);
	}

	public Iterator iterator() {
		return children.iterator();
	}

	/**
	 * Removes the given widget, and its associated tab.
	 * 
	 * @param widget the widget to be removed
	 */
	public boolean remove(ITabElement tab) {
		int index = children.indexOf(tab);
		if (index == -1)
			return false;

		children.remove(tab);
		tabBar.removeTab(index);
		deck.remove( tab.getWidget() );
		return true;
	}

	public void removeTabListener(TabListener listener) {
		exTabListeners.remove(listener);
	}

	/**
	 * Programmatically selects the specified tab.
	 * 
	 * @param index the index of the tab to be selected
	 */
	public void selectTab(ITabElement tab) {
		if (tab == null)
			return;
		
		int idx = children.indexOf(tab);
		tabBar.selectTab( idx );
		deck.showWidget( idx );
	}
	
	public void changeTitleTab(ITabElement tab){
		int idx = children.indexOf(tab);
		String title = tab.getTitle();

		tabBar.setTabTitle(title, idx);
	}
	
	public void changeIconTab(ITabElement tab){
		int idx = children.indexOf(tab);
		tabBar.setTabIcon(tab.getImgURL(), idx);
	}

	private TabListener tabListener = new TabListener() {
		
		public void onTabClose(ITabElement tab, int tabIndex) {
			for (Iterator it = exTabListeners.iterator(); it.hasNext(); ) {
				TabListener listener = (TabListener)it.next();
				listener.onTabClose((ITabElement)children.get(tabIndex), tabIndex);
			}
		}

		public void onTabSelect(ITabElement tab, int tabIndex) {
			for (Iterator it = exTabListeners.iterator(); it.hasNext(); ) {
				TabListener listener = (TabListener)it.next();
				listener.onTabSelect((ITabElement)children.get(tabIndex), tabIndex);
			}
		}

	};
	
}