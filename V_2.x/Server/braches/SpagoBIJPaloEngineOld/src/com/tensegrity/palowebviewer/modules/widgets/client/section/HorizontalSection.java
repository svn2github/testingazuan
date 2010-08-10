package com.tensegrity.palowebviewer.modules.widgets.client.section;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Horizonral expandable section
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.tensegrity-gwt-section { the section itself }</li>
 * <li>.tensegrity-gwt-horizontalSection { additional style for the horizontalSection }</li>
 * <li>.tensegrity-gwt-sectionTitle { the sections tytle }</li> 
 * <li>.tensegrity-gwt-sectionIcon { the expand/collapse icon }</li>
 * <li>.tensegrity-gwt-sectionIcon-collapsed { additional style for collapsed icon }</li>
 * <li>.tensegrity-gwt-sectionIcon-expanded { additional style for expanded icon }</li>
 * </ul>
 */
public class HorizontalSection extends BasicSection {

	protected HorizontalPanel paHeader;
	protected VerticalPanel paSection;
	
	public HorizontalSection(String title, boolean expanded, Widget body) {
		super(title, expanded, body );
		
		initWidgets();
	}
	
	private void initWidgets() {
		// build header
		createPaHeader();
		//build section
		createSection();
	}

	private void createSection() {
		paSection = new VerticalPanel();
		paSection.setWidth("100%");
		paSection.add(paHeader);
		
		if ( getWidget() != null ) {
			paSection.add( getWidget() );
			if ( !isExpanded() )
				getWidget().setVisible(false);
		}
		
		addStyleName( getHorizontalSectionStyle() );		
		setWidth("100%");
		setWidget(0, 0, paSection);
	}

	private void createPaHeader() {
		paHeader = new HorizontalPanel();
		paHeader.setSpacing(3);
		paHeader.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		paHeader.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		paHeader.add(super.getIconWidget());
		paHeader.setWidth("100%");
		Label laTitle = new Label(getTitle());
		laTitle.setStyleName( getSectionTitleStyle() );
		laTitle.addClickListener(getClickListener());
		paHeader.add(laTitle);
		
		//Label rest = new Label("");
		//rest.setStyleName("separator");
		HTML rest = new HTML("&nbsp", true);
		paHeader.add(rest);
		paHeader.setCellWidth(rest, "100%");
	}
	
	public void addHeaderStyleName(String style) {
		paHeader.addStyleName(style);
	}
	
	protected void collapse() {
		if ( getWidget() != null)
			getWidget().setVisible(false);
	}

	protected void expand() {
		if ( getWidget() != null)
			getWidget().setVisible(true);
	}
	
	private String getHorizontalSectionStyle() {
		return "tensegrity-gwt-horizontalSection";
	}

}
