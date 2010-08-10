package com.tensegrity.palowebviewer.modules.widgets.client.tab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.util.client.JavaScript;

/**
 * A horizontal bar of folder-style tabs
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.gwt-TabBar { the tab bar itself }</li>
 * <li>.gwt-TabBar .gwt-TabBarFirst { the left edge of the bar }</li>
 * <li>.gwt-TabBar .gwt-TabBarRest { the right edge of the bar }</li>
 * <li>.gwt-TabBar .gwt-TabBarItem { unselected tabs }</li>
 * <li>.gwt-TabBar .gwt-TabBarItem-selected { additional style for selected
 * tabs } </li>
 * </ul>
 * <p>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.TabBarExample}
 * </p>
 */
public class GTabBar extends Composite {

	private static final int MAX_TITLE_WIDTH = 100;

	private HorizontalPanel panel = new HorizontalPanel();

	private Widget selectedTab;

	private List exTabListeners = new ArrayList();

	/**
	 * Creates an empty tab bar.
	 */
	public GTabBar() {
		initWidget(panel);
		sinkEvents(Event.ONCLICK);
		setStyleName("tensegrity-gwt-TabBar");

		panel.setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);

		HTML first = new HTML("&nbsp;");
		HTML rest = new HTML("&nbsp;");
		first.setStyleName("tensegrity-gwt-TabBar-TabBarFirst");
		rest.setStyleName("tensegrity-gwt-TabBar-TabBarRest");
		first.setHeight("100%");
		rest.setHeight("100%");

		panel.add(first);
		panel.add(rest);
		panel.setCellHeight(first, "100%");
		panel.setCellWidth(rest, "100%");
	}
	
	/**
	 * 
	 * Adds a new tab with the specified text, icon and close button
	 * 
	 * @param text the new tab's text
	 * @param asHTML <code>true</code> to treat the specified text as html
	 * @param imgURL the tab's icon URL
	 * @param closeable <code>true</code> with close button
	 */
	public void addTab(String text, String imgURL, boolean closeable, boolean selected) {
		HorizontalPanel tab = new HorizontalPanel();
		tab.setSpacing(3);
		tab.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		if ( imgURL != null )
			addTabIcon(imgURL, tab);
		Label title = createTitleWidget(text);
		title.addClickListener(selectClickListener);
		tab.add(title);
		if ( closeable )
			addTabCloseIcon(tab);
		tab.setStyleName("tensegrity-gwt-TabBar-tabBarItem");
		panel.insert(tab, panel.getWidgetCount() - 1);
		if ( selected )
			selectTab( tab );
		else
			setSelectionStyle(tab, false);
		markFirstTab();
	}

	private void markFirstTab() {
		if(getTabCount() >0) {
			Widget widget = panel.getWidget(1);
			widget.addStyleName("first");
		}
	}

	private void addTabCloseIcon(HorizontalPanel tab) {
		Image btClose = new Image("tab_close.png");
		btClose.addClickListener(closeClickListener);
		tab.add(btClose);
	}

	private void addTabIcon(String imgURL, HorizontalPanel tab) {
		Image img = new Image(imgURL);
		tab.add(img);
		img.addClickListener(selectClickListener);
	}

	public void addTabListener(TabListener listener) {
		exTabListeners.add(listener);
	}


	/**
	 * Gets the number of tabs present.
	 * 
	 * @return the tab count
	 */
	public int getTabCount() {
		return panel.getWidgetCount() - 2;
	}

	/**
	 * Removes the tab at the specified index.
	 * 
	 * @param index the index of the tab to be removed
	 */
	public void removeTab(int index) {
		checkTabIndex(index);

		// (index + 1) to account for 'first' place holder widget.
		Widget toRemove = panel.getWidget(index + 1);
		if (toRemove == selectedTab)
			selectedTab = null;
		panel.remove(toRemove);
		markFirstTab();
	}

	public void selectTab(int index) {
		checkTabIndex(index);
		if (index != -1) {
			selectTab( panel.getWidget(index + 1) );
		} else {
			selectTab(null);
		}
	}
	
	public void setTabTitle(String title, int index){
		
		HorizontalPanel pa = (HorizontalPanel)panel.getWidget(index+1);
		for (int i = 0; i < pa.getWidgetCount(); i++) {
			Widget w = pa.getWidget(i);
			if(w instanceof Label){
				setTitle(title, (Label)w);
			}
		}
	}

	private void setTitle(String title, Label label) {
		String tooltip = title;
		title = JavaScript.cutToFit(title, MAX_TITLE_WIDTH);
		label.setText(title);
		JavaScript.setTooltip(label, tooltip);
	}
	
	public void setTabIcon(String iconUrl, int index){
		HorizontalPanel pa = (HorizontalPanel)panel.getWidget(index+1);
		for (int i = 0; i < pa.getWidgetCount(); i++) {
			Widget w = pa.getWidget(i);
			if(w instanceof Image){
				Image img = (Image) w;
				img.setUrl(iconUrl);
			}
		}
	}
	
	public void selectTab(Widget w) {
		if ( selectedTab != null )
			setSelectionStyle( selectedTab, false );
		
		selectedTab = w;
		setSelectionStyle(selectedTab, true);
	}
	
	private Label createTitleWidget(String title){
		Label r = new Label();
		r.setWordWrap(false);
		setTitle(title, r);
		return r;
	}

	private void checkTabIndex(int index) {
		if ((index < -1) || (index >= getTabCount()))
			throw new IndexOutOfBoundsException();
	}

	private void setSelectionStyle(Widget item, boolean selected) {
		
		if (item != null) {
			if ( selected ) {
				item.removeStyleName("tensegrity-gwt-TabBar-tabBarItem-not-selected");
				item.addStyleName("tensegrity-gwt-TabBar-tabBarItem-selected");
			} else {
				item.removeStyleName("tensegrity-gwt-TabBar-tabBarItem-selected");
				item.addStyleName("tensegrity-gwt-TabBar-tabBarItem-not-selected");
			}
		}
	}
	
	private ClickListener selectClickListener = new ClickListener() {
		
		public void onClick(Widget sender) {
			for (int i = 1; i < panel.getWidgetCount() - 1; ++i) {
				if ( panel.getWidget(i) instanceof HorizontalPanel) {
					HorizontalPanel pa =  (HorizontalPanel)panel.getWidget(i);
					if (pa.getWidget(0) == sender || pa.getWidget(1) == sender) {
						fireTabSelected(i);
						return;
					}
				}
			}
		}

		private void fireTabSelected(int i) {
			for (Iterator it = exTabListeners.iterator(); it.hasNext(); ) {
				TabListener listener = (TabListener)it.next();
				listener.onTabSelect(null, i - 1);
			}
		}
	};

	private ClickListener closeClickListener = new ClickListener() {
		
		public void onClick(Widget sender) {
			for (int i = 1; i < panel.getWidgetCount() - 1; ++i) {
				if ( panel.getWidget(i) instanceof HorizontalPanel) {
					HorizontalPanel pa =  (HorizontalPanel)panel.getWidget(i);
					if ( pa.getWidget(2) == sender ) {
						fireTabClose(i);
						return;
					}
				}
			}
		}

		private void fireTabClose(int i) {
			for (Iterator it = exTabListeners.iterator(); it.hasNext(); ) {
				TabListener listener = (TabListener)it.next();
				listener.onTabClose(null, i - 1);
			}
		}
	};
	
}
