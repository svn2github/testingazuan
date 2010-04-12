package com.tensegrity.palowebviewer.modules.widgets.client.section;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Vertical expandable section
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.tensegrity-gwt-section { the section itself }</li>
 * <li>.tensegrity-gwt-verticalSection { additional style for the verticalSection }</li>
 * <li>.tensegrity-gwt-sectionTitle { the sections tytle }</li> 
 * <li>.tensegrity-gwt-sectionIcon { the expand/collapse icon }</li>
 * <li>.tensegrity-gwt-sectionIcon-collapsed { additional style for collapsed icon }</li>
 * <li>.tensegrity-gwt-sectionIcon-expanded { additional style for expanded icon }</li>
 * </ul>
 */
public class VerticalSection extends BasicSection {

	protected VerticalPanel paHeader;
	protected HorizontalPanel paSection;
	
	public VerticalSection(String title, boolean expanded, Widget body) {
		super(title, expanded, body );
		
		initWidgets();
	}
	
	protected void initWidgets() {
		// build header
		paHeader = new VerticalPanel();
		paHeader.setHeight("100%");
		paHeader.setSpacing(3);
		paHeader.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		paHeader.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		paHeader.add(super.getIconWidget());
		buildVerticalText(paHeader);
		
		//Label rest = new Label("");
		//rest.setStyleName("separator_v");
		HTML rest = new HTML("&nbsp;", true);
		paHeader.add(rest);
		paHeader.setCellHeight(rest, "100%");

		//build section
		paSection = new HorizontalPanel();
		paSection.setHeight("100%");
		paSection.add(paHeader);
		
		if ( getWidget() != null ) {
			paSection.add( getWidget() );
			if ( !isExpanded() )
				getWidget().setVisible(false);
		}
		
		addStyleName( getVerticalSectionStyle() );		
		setHeight("100%");
		setWidget(0, 0, paSection);
	}
	
	public void addHeaderStyleName(String style) {
		paHeader.addStyleName(style);
	}
	
	private void buildVerticalText(VerticalPanel panel) {
		if ( getTitle() == null || "".equals( getTitle() ) )
			return;
		
		char[] ch =  getTitle().toCharArray();
		for (int i = 0; i < ch.length; i++) {
			Label laTitle = new Label( String.valueOf(ch[i]) );
			laTitle.setStyleName( getSectionTitleStyle() );
			laTitle.addClickListener( getClickListener() );
			panel.add(laTitle);
		}
	}
	
	protected void collapse() {
		if ( getWidget() != null)
			getWidget().setVisible(false);
	}

	protected void expand() {
		if ( getWidget() != null)
			getWidget().setVisible(true);
	}
	
	private String getVerticalSectionStyle() {
		return "tensegrity-gwt-verticalSection";
	}
	
}

