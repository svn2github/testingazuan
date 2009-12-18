package com.tensegrity.palowebviewer.modules.widgets.client.section;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

/**
 * Basic expandeble section
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.tensegrity-gwt-section { the section itself }</li>
 * <li>.tensegrity-gwt-sectionIcon { the expand/collapse icon }</li>
 * <li>.tensegrity-gwt-sectionTitle { the sections tytle }</li>
 * <li>.tensegrity-gwt-sectionIcon-collapsed { additional style for collapsed icon }</li>
 * <li>.tensegrity-gwt-sectionIcon-expanded { additional style for expanded icon }</li>
 * </ul>
 */
public abstract class BasicSection extends Grid {
	
	private String title;
	private boolean expanded;
	
	private Hyperlink hlIcon;
	private Widget widget;
	
	public BasicSection(String title, boolean expanded, Widget widget) {
		super(1, 1);
		this.title         = title;
		this.expanded      = expanded;
		this.widget	 	   = widget;
		
		setStyleName( getSectionStyle() );
		
		hlIcon = new Hyperlink();
		hlIcon.setStyleName( getSectionIconStyle() );
		hlIcon.addClickListener(clickListener);
		
		updateImage();
	}
	
	/**
	 * Callback for expand section 
	 */
	protected abstract void expand();

	/**
	 * Callback for collapse section 
	 */
	protected abstract void collapse();


	/**
	 * 
	 * @return Widget - the expand/collapse icon widget
	 */
	protected Widget getIconWidget() {
		return hlIcon;
	}
	
	/**
	 * @return Widget - the nested widget
	 **/
	public Widget getWidget() {
		return widget;
	}

	/**
	 * @return ClickListener - listener for expand/collapse elements
	 **/
	protected ClickListener getClickListener() {
		return clickListener;
	}

	/**
	 * Make the section expanded or collapsed
	 * 
	 * @param expanded true==expanded, false==collapsed
	 */
	public void setExpanded(boolean expanded) {
		if ( this.expanded == expanded)
			return;
		
		this.expanded = expanded;
		
		if ( isExpanded() )
			expand();
		else
			collapse();
		
		updateImage();
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isExpanded() {
		return expanded;
	}
	
	private void updateImage() {
		if ( isExpanded() ) {
			hlIcon.removeStyleName( getSectionIconCollapsedStyle() );
			hlIcon.addStyleName( getSectionIconExpandedStyle() );
		} else {
			hlIcon.removeStyleName( getSectionIconExpandedStyle() );
			hlIcon.addStyleName( getSectionIconCollapsedStyle() );			
		}
	}
	
	protected String getSectionStyle() {
		return "tensegrity-gwt-section";
	}
	
	protected String getSectionTitleStyle() { 
		return "tensegrity-gwt-sectionTitle";
	}
	
	protected String getSectionIconStyle() {
		return "tensegrity-gwt-sectionIcon";
	}
	
	protected String getSectionIconExpandedStyle() { 
		return "tensegrity-gwt-sectionIcon-expanded";
	}
	
	protected String getSectionIconCollapsedStyle() { 
		return "tensegrity-gwt-sectionIcon-collapsed";
	}
	
	private ClickListener clickListener = new ClickListener() {

		public void onClick(Widget sender) {
			setExpanded( !isExpanded() );
		}
		
	};
	
}