package com.tensegrity.palowebviewer.modules.widgets.client;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IAction;
import com.tensegrity.palowebviewer.modules.widgets.client.actions.IPropertyListener;

/**
 * ActionImage widget. Contains enabled/disabled image
 * 
 * CSS: tensegrity-gwt-clickable
 * 
 */
public class ActionImage extends Composite {

	private IAction action;
	private String enabledURL;
	private String disabledURL;
	private Image image;
	
	public ActionImage() {
		image = new Image();
		image.addClickListener(clickListener);
		super.initWidget(image);
	}
	
	public void setTitle(String title) {
		image.setTitle(title);
	}
	
	public String getDisabledURL() {
		return disabledURL;
	}

	public void setDisabledURL(String disabledURL) {
		this.disabledURL = disabledURL;
		updateImage();
	}

	public String getEnabledURL() {
		return enabledURL;
	}

	public void setEnabledURL(String enabledURL) {
		this.enabledURL = enabledURL;
		updateImage();
	}
	
	private void updateImage() {
		if ( ( ( action == null && image.getUrl() != disabledURL ) ||
				( action != null && !action.isEnabled() && image.getUrl() != disabledURL ) )
				&& disabledURL != null ) {
			image.setUrl(disabledURL);
		}
		if ( action != null && action.isEnabled() && image.getUrl() != enabledURL && enabledURL != null) {
			image.setUrl(enabledURL);
		}
	}
	
	private void updateStyle() {
		if (action != null && action.isEnabled()) {
			image.addStyleName("tensegrity-gwt-clickable");
		} else {
			image.removeStyleName("tensegrity-gwt-clickable");
		}
	}

	public void setAction(IAction action) {
		if ( this.action != null )
			this.action.removePropertyListener(propertyListener);
		
		this.action = action;
		
		if ( this.action != null ) {
			action.addPropertyListener(propertyListener);
			updateImage();
			updateStyle();
		}
	}
	
	public void add(Widget w) {
		throw new IllegalStateException("ActionImage can't contain any child");
	}

	public boolean remove(Widget w) {
		throw new IllegalStateException("You can't widget from ActionImage");
	}

	public void setWidget(Widget w) {
		throw new IllegalStateException("ActionImage can't contain any child");
	}
	
	//-- Internal classes section --	

	private IPropertyListener propertyListener = new IPropertyListener() {

		public void onDisabled() {
			updateImage();
			updateStyle();
		}

		public void onEnabled() {
			updateImage();
			updateStyle();
			
		}
		
	};
	
	private ClickListener clickListener = new ClickListener() {

		public void onClick(Widget sender) {
			if ( action != null && action.isEnabled() )
				action.onActionPerformed(null);
		}
		
	};
	
}